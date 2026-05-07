/*
 * Módulo de Dagger Hilt que provee la base de datos Room y el DAO de notas.
 * Se instala en el componente SingletonComponent para que estén disponibles en toda la app
 * con ámbito de singleton.
 */
package com.noteapp.di

import android.content.Context
import androidx.room.Room
import com.noteapp.data.local.NoteDatabase
import com.noteapp.data.local.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDatabase =
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()
}