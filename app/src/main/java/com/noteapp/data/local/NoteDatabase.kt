/*
 * Definición de la base de datos Room. Declara la entidad NoteEntity y expone el DAO
 * necesario para acceder a la tabla "notes". Se utiliza un companion object para
 * centralizar el nombre de la base de datos.
 */
package com.noteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noteapp.data.local.dao.NoteDao
import com.noteapp.data.local.entity.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    // Método abstracto que Room implementa para proporcionar el DAO.
    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}