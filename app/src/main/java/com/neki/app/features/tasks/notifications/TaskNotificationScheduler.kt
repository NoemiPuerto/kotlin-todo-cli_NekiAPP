package com.neki.app.features.tasks.notifications

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.neki.app.features.tasks.domain.Task
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TaskNotificationScheduler {
    const val CHANNEL_ID = "task_reminders"
    const val EXTRA_TASK_ID = "extra_task_id"
    const val EXTRA_TASK_TITLE = "extra_task_title"
    const val EXTRA_MESSAGE = "extra_message"
    const val EXTRA_NOTIFICATION_ID = "extra_notification_id"

    private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

    private val reminderOffsets = listOf(
        ReminderOffset("three_days", 3L * 24L * 60L * 60L * 1000L, "Esta tarea se acerca."),
        ReminderOffset("two_days", 2L * 24L * 60L * 60L * 1000L, "Tal vez podrías avanzar un poco hoy."),
        ReminderOffset("one_hour", 60L * 60L * 1000L, "Queda poco tiempo para esta tarea."),
        ReminderOffset("due", 0L, "Es momento de revisar esta tarea.")
    )

    fun schedule(context: Context, task: Task) {
        cancel(context, task.id)

        if (!task.notificationsEnabled) return

        val dueAt = task.toTriggerMillis() ?: return
        ensureNotificationChannel(context)

        reminderOffsets.forEach { reminder ->
            val triggerAt = dueAt - reminder.offsetMillis
            if (triggerAt > System.currentTimeMillis()) {
                scheduleSingleReminder(context, task, reminder, triggerAt)
            }
        }
    }

    fun cancel(context: Context, taskId: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        reminderOffsets.forEach { reminder ->
            val pendingIntent = taskPendingIntentOrNull(context, taskId, reminder.key)
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    fun ensureNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val existingChannel = manager.getNotificationChannel(CHANNEL_ID)
        if (existingChannel != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Recordatorios de tareas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Recordatorios tranquilos para tareas con fecha y hora"
        }

        manager.createNotificationChannel(channel)
    }

    fun canPostNotifications(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun scheduleSingleReminder(
        context: Context,
        task: Task,
        reminder: ReminderOffset,
        triggerAt: Long
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = taskPendingIntent(
            context = context,
            taskId = task.id,
            taskTitle = task.title,
            reminderKey = reminder.key,
            message = reminder.message,
            flag = PendingIntent.FLAG_UPDATE_CURRENT
        )

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms() -> {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }

            Build.VERSION.SDK_INT in Build.VERSION_CODES.M until Build.VERSION_CODES.S -> {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }

            else -> {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }
        }
    }

    private fun taskPendingIntent(
        context: Context,
        taskId: String,
        taskTitle: String,
        reminderKey: String,
        message: String,
        flag: Int
    ): PendingIntent {
        val notificationId = notificationRequestCode(taskId, reminderKey)
        val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
            putExtra(EXTRA_TASK_ID, taskId)
            putExtra(EXTRA_TASK_TITLE, taskTitle)
            putExtra(EXTRA_MESSAGE, message)
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }

        return PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            flag or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun taskPendingIntentOrNull(
        context: Context,
        taskId: String,
        reminderKey: String
    ): PendingIntent? {
        val intent = Intent(context, TaskNotificationReceiver::class.java)

        return PendingIntent.getBroadcast(
            context,
            notificationRequestCode(taskId, reminderKey),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun notificationRequestCode(taskId: String, reminderKey: String): Int {
        return "$taskId:$reminderKey".hashCode()
    }

    private fun Task.toTriggerMillis(): Long? {
        val date = runCatching { LocalDate.parse(dueDate.orEmpty()) }.getOrNull() ?: return null
        val time = dueTime?.toLocalTimeOrNull() ?: LocalTime.of(9, 0)

        return LocalDateTime.of(date, time)
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    private fun String.toLocalTimeOrNull(): LocalTime? {
        return runCatching {
            LocalTime.parse(uppercase(Locale.US), timeFormatter)
        }.getOrNull()
    }

    private data class ReminderOffset(
        val key: String,
        val offsetMillis: Long,
        val message: String
    )
}
