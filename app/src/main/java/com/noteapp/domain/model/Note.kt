/*
 * Modelo de dominio que representa una nota. Es independiente de la capa de datos.
 * El contenido se maneja como texto HTML para soportar formato enriquecido.
 */
package com.noteapp.domain.model

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String, // HTML con texto enriquecido
    val createdAt: Long,
    val updatedAt: Long
)