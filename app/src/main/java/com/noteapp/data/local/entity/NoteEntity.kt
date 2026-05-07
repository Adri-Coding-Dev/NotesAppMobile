/*
 * Entidad de Room que representa la tabla "notes". Cada campo mapea a una columna.
 * El contenido se almacena como HTML para soportar texto enriquecido (subrayado).
 */
package com.noteapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String, // Almacenado como string HTML para soportar spans de subrayado
    val createdAt: Long,
    val updatedAt: Long
)