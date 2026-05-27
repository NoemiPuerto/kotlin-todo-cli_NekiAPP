package com.neki.app.features.tasks.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.neki.app.features.tasks.data.TaskLocalStorage
import com.neki.app.features.tasks.domain.Attachment
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.Task
import com.neki.app.features.tasks.domain.TaskGroup
import com.neki.app.features.tasks.notifications.TaskNotificationScheduler

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val storage = TaskLocalStorage(application.applicationContext)
    private val appContext = application.applicationContext

    val availableGroups = mutableStateListOf<TaskGroup>()
    val tasks = mutableStateListOf<Task>()

    var selectedTask by mutableStateOf<Task?>(null)
        private set

    init {
        val savedGroups = storage.loadGroups()
        val savedTasks = storage.loadTasks()

        availableGroups.addAll(savedGroups.ifEmpty { defaultGroups() })
        tasks.addAll(savedTasks.ifEmpty { defaultTasks() })

        persistGroups()
        persistTasksAndNotifications()
    }

    fun toggleTaskCompletion(taskId: String) {
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index != -1) {
            val task = tasks[index]

            tasks[index] = task.copy(
                completed = !task.completed,
                updatedAt = System.currentTimeMillis()
            )

            persistTasksAndNotifications()
        }
    }

    fun addTask(
        title: String,
        priority: Priority,
        group: TaskGroup?,
        subTasks: List<SubTask>,
        dueDate: String?,
        dueTime: String?,
        repeatOption: RepeatOption,
        notificationsEnabled: Boolean,
        attachments: List<Attachment> = emptyList()
    ) {
        if (title.isBlank()) return

        tasks.add(
            0,
            Task(
                id = System.currentTimeMillis().toString(),
                title = title.trim(),
                priority = priority,
                group = group ?: availableGroups.firstOrNull(),
                dueDate = dueDate,
                dueTime = dueTime,
                repeatOption = repeatOption,
                notificationsEnabled = notificationsEnabled,
                attachments = attachments,
                subTasks = subTasks
            )
        )

        persistTasksAndNotifications()
    }

    fun deleteTask(taskId: String) {
        tasks.removeAll { it.id == taskId }
        TaskNotificationScheduler.cancel(appContext, taskId)
        persistTasksAndNotifications()
    }

    fun selectTask(task: Task) {
        selectedTask = task
    }

    fun clearSelectedTask() {
        selectedTask = null
    }

    fun updateTaskTitle(newTitle: String) {
        val task = selectedTask ?: return

        val updatedTask = task.copy(
            title = newTitle,
            updatedAt = System.currentTimeMillis()
        )

        val index = tasks.indexOfFirst { it.id == task.id }

        if (index != -1) {
            tasks[index] = updatedTask
            selectedTask = updatedTask
            persistTasksAndNotifications()
        }
    }

    fun updateTask(updatedTask: Task) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }

        if (index != -1) {
            tasks[index] = updatedTask.copy(
                updatedAt = System.currentTimeMillis()
            )
            selectedTask = tasks[index]
            persistTasksAndNotifications()
        }
    }

    fun createGroup(name: String): TaskGroup {
        val cleanName = name.trim()
        if (cleanName.isBlank()) return availableGroups.first()

        val existingGroup = availableGroups.find {
            it.name.equals(cleanName, ignoreCase = true)
        }

        if (existingGroup != null) {
            return existingGroup
        }

        val newGroup = TaskGroup(
            id = cleanName.lowercase().replace(" ", "_"),
            name = cleanName
        )

        availableGroups.add(newGroup)
        persistGroups()

        return newGroup
    }

    private fun persistTasksAndNotifications() {
        storage.saveTasks(tasks)

        tasks.forEach { task ->
            if (task.notificationsEnabled) {
                TaskNotificationScheduler.schedule(appContext, task)
            } else {
                TaskNotificationScheduler.cancel(appContext, task.id)
            }
        }
    }

    private fun persistGroups() {
        storage.saveGroups(availableGroups)
    }

    private fun defaultGroups(): List<TaskGroup> {
        return listOf(
            TaskGroup(id = "personal", name = "Personal"),
            TaskGroup(id = "work", name = "Trabajo"),
            TaskGroup(id = "study", name = "Estudio"),
            TaskGroup(id = "health", name = "Salud")
        )
    }

    private fun defaultTasks(): List<Task> {
        return listOf(
            Task(
                id = "1",
                title = "Analyze competitor workflows",
                description = "Study productivity app patterns",
                priority = Priority.HIGH,
                dueDate = "2026-10-12",
                group = TaskGroup(id = "1", name = "Research"),
                subTasks = listOf(
                    SubTask("1", "Review UI flows"),
                    SubTask("2", "Compare onboarding")
                ),
                notificationsEnabled = false,
                repeatOption = RepeatOption.NONE
            ),
            Task(
                id = "2",
                title = "Design task creation flow",
                description = "Define interactions for editor screen",
                priority = Priority.MEDIUM,
                group = TaskGroup(id = "2", name = "Design")
            ),
            Task(
                id = "3",
                title = "Implement bottom navigation",
                completed = true,
                priority = Priority.LOW,
                group = TaskGroup(id = "3", name = "Development")
            )
        )
    }
}
