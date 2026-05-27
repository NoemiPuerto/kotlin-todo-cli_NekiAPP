package com.neki.app.features.tasks.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.RepeatOption
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.Task
import com.neki.app.features.tasks.domain.TaskGroup

class TaskViewModel : ViewModel() {
    val availableGroups = mutableStateListOf(
        TaskGroup(
            id = "personal",
            name = "Personal"
        ),
        TaskGroup(
            id = "work",
            name = "Trabajo"
        ),
        TaskGroup(
            id = "study",
            name = "Estudio"
        ),
        TaskGroup(
            id = "health",
            name = "Salud"
        )
    )

    val tasks = mutableStateListOf(
        Task(
            id = "1",
            title = "Analyze competitor workflows",
            description = "Study productivity app patterns",
            priority = Priority.HIGH,
            dueDate = "Oct 12",
            group = TaskGroup(
                id = "1",
                name = "Research"
            ),
            subTasks = listOf(
                SubTask("1", "Review UI flows"),
                SubTask("2", "Compare onboarding")
            ),
            notificationsEnabled = true,
            repeatOption = RepeatOption.NONE
        ),

        Task(
            id = "2",
            title = "Design task creation flow",
            description = "Define interactions for editor screen",
            priority = Priority.MEDIUM,
            group = TaskGroup(
                id = "2",
                name = "Design"
            )
        ),

        Task(
            id = "3",
            title = "Implement bottom navigation",
            completed = true,
            priority = Priority.LOW,
            group = TaskGroup(
                id = "3",
                name = "Development"
            )
        )
    )

    var selectedTask by mutableStateOf<Task?>(null)
        private set

    fun toggleTaskCompletion(taskId: String) {
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index != -1) {
            val task = tasks[index]

            tasks[index] = task.copy(
                completed = !task.completed,
                updatedAt = System.currentTimeMillis()
            )
        }
    }

    fun addTask(
        title: String,
        priority: Priority,
        group: TaskGroup?,
        subTasks: List<SubTask>,
        dueDate: String?,
        dueTime: String?,
        repeatOption: RepeatOption
    ) {
        if (title.isBlank()) return

        tasks.add(
            0,
            Task(
                id = System.currentTimeMillis().toString(),
                title = title,
                priority = priority,
                group = group ?: availableGroups.first(),
                dueDate = dueDate,
                dueTime = dueTime,
                repeatOption = repeatOption,
                subTasks = subTasks
            )
        )
    }

    fun deleteTask(taskId: String) {
        tasks.removeAll { it.id == taskId }
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
        }
    }

    fun updateTask(updatedTask: Task) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }

        if (index != -1) {
            tasks[index] = updatedTask.copy(
                updatedAt = System.currentTimeMillis()
            )
            selectedTask = tasks[index]
        }
    }

    fun createGroup(name: String): TaskGroup {
        val existingGroup = availableGroups.find {
            it.name.equals(name, ignoreCase = true)
        }

        if (existingGroup != null) {
            return existingGroup
        }

        val newGroup = TaskGroup(
            id = name.lowercase().replace(" ", "_"),
            name = name
        )

        availableGroups.add(newGroup)

        return newGroup
    }
}