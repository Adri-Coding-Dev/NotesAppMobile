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
    abstract fun noteDao(): NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}
