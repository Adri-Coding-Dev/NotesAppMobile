/*
 * Interfaz del repositorio de notas en el dominio. Define las operaciones que se pueden
 * realizar sobre las notas sin depender de la fuente de datos concreta.
 */
package com.noteapp.domain.repository

import com.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun searchNotes(query: String): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun deleteNoteById(id: Int)
}