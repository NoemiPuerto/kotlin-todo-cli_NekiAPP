package com.neki.app.features.tasks.domain

data class Attachment(
    val id: String,
    val type: AttachmentType,
    val uri: String,
    val name: String
)