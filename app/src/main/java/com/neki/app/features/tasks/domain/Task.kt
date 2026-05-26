package com.neki.app.features.tasks.domain

data class Task(
    val id: String,
    val title: String,
    val description: String = "",
    val completed: Boolean = false,

    val priority: Priority = Priority.MEDIUM,

    val dueDate: String? = null,
    val dueTime: String? = null,

    val repeatOption: RepeatOption = RepeatOption.NONE,

    val notificationsEnabled: Boolean = false,

    val attachments: List<Attachment> = emptyList(),

    val group: TaskGroup? = null,

    val subTasks: List<SubTask> = emptyList(),

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)