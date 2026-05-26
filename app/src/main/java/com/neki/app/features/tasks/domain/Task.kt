package com.neki.app.features.tasks.domain

data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val completed: Boolean = false,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: String? = null,
    val category: String = "Personal",
    val subTasks: List<SubTask> = emptyList()
)