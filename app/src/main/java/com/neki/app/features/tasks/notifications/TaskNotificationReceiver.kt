package com.neki.app.features.tasks.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.neki.app.R

class TaskNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (!TaskNotificationScheduler.canPostNotifications(context)) return

        TaskNotificationScheduler.ensureNotificationChannel(context)

        val taskId = intent.getStringExtra(TaskNotificationScheduler.EXTRA_TASK_ID).orEmpty()
        val title = intent.getStringExtra(TaskNotificationScheduler.EXTRA_TASK_TITLE).orEmpty()
        val message = intent.getStringExtra(TaskNotificationScheduler.EXTRA_MESSAGE).orEmpty()
        val notificationId = intent.getIntExtra(
            TaskNotificationScheduler.EXTRA_NOTIFICATION_ID,
            taskId.hashCode()
        )

        val notification = NotificationCompat.Builder(context, TaskNotificationScheduler.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(if (title.isBlank()) "NekiApp" else title)
            .setContentText(message.ifBlank { "Tienes una tarea pendiente" })
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, notification)
    }
}
