package com.noteapp.domain.model

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String, // HTML encoded rich text
    val createdAt: Long,
    val updatedAt: Long
)
