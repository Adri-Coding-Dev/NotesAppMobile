package com.noteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String, // Stored as HTML string to support rich text (underline spans)
    val createdAt: Long,
    val updatedAt: Long
)
