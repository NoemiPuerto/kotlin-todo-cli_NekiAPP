package com.neki.app.features.tasks.domain

data class SubTask(
    val id: String,
    val title: String,
    val completed: Boolean = false
)