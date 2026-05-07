/*
 * Implementación del repositorio que hace de puente entre los casos de uso y la
 * fuente de datos local (Room). Convierte las entidades NoteEntity al modelo de dominio
 * Note y viceversa usando funciones de mapeo privadas.
 */
package com.noteapp.data.repository

import com.noteapp.data.local.dao.NoteDao
import com.noteapp.data.local.entity.NoteEntity
import com.noteapp.domain.model.Note
import com.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {

    override fun getAllNotes(): Flow<List<Note>> =
        noteDao.getAllNotes().map { entities -> entities.map { it.toDomain() } }

    override fun searchNotes(query: String): Flow<List<Note>> =
        noteDao.searchNotes(query).map { entities -> entities.map { it.toDomain() } }

    override suspend fun getNoteById(id: Int): Note? =
        noteDao.getNoteById(id)?.toDomain()

    override suspend fun insertNote(note: Note): Long =
        noteDao.insertNote(note.toEntity())

    override suspend fun updateNote(note: Note) =
        noteDao.updateNote(note.toEntity())

    override suspend fun deleteNote(note: Note) =
        noteDao.deleteNote(note.toEntity())

    override suspend fun deleteNoteById(id: Int) =
        noteDao.deleteNoteById(id)

    // Mapeo de entidad a modelo de dominio.
    private fun NoteEntity.toDomain() = Note(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    // Mapeo de modelo de dominio a entidad.
    private fun Note.toEntity() = NoteEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}