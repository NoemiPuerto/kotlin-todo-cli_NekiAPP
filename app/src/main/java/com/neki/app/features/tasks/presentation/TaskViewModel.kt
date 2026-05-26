package com.neki.app.features.tasks.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.neki.app.features.tasks.domain.Priority
import com.neki.app.features.tasks.domain.SubTask
import com.neki.app.features.tasks.domain.Task
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class TaskViewModel : ViewModel() {

    val tasks = mutableStateListOf(
        Task(
            id = "1",
            title = "Analyze competitor workflows",
            description = "Study productivity app patterns",
            priority = Priority.HIGH,
            category = "Research",
            subTasks = listOf(
                SubTask("1", "Review UI flows"),
                SubTask("2", "Compare onboarding")
            )
        ),
        Task(
            id = "2",
            title = "Design task creation flow",
            description = "Define interactions for editor screen",
            priority = Priority.MEDIUM,
            category = "Design"
        ),
        Task(
            id = "3",
            title = "Implement bottom navigation",
            completed = true,
            priority = Priority.LOW,
            category = "Development"
        )
    )
    fun toggleTaskCompletion(taskId: String) {
        val index = tasks.indexOfFirst { it.id == taskId }

        if (index != -1) {
            val task = tasks[index]
            tasks[index] = task.copy(
                completed = !task.completed
            )
        }
    }

    fun addTask(title: String) {
        if (title.isBlank()) return

        tasks.add(
            0,
            Task(
                id = System.currentTimeMillis().toString(),
                title = title,
                priority = Priority.MEDIUM,
                category = "Personal"
            )
        )
    }

    fun deleteTask(taskId: String) {
        tasks.removeAll { it.id == taskId }
    }

    var selectedTask by mutableStateOf<Task?>(null)
        private set

    fun selectTask(task: Task) {
        selectedTask = task
    }

    fun clearSelectedTask() {
        selectedTask = null
    }

    fun updateTaskTitle(newTitle: String) {
        val task = selectedTask ?: return

        val updatedTask = task.copy(
            title = newTitle
        )

        val index = tasks.indexOfFirst { it.id == task.id }

        if (index != -1) {
            tasks[index] = updatedTask
            selectedTask = updatedTask
        }
    }
}
