/*
 * Módulo Hilt que enlaza la interfaz NoteRepository con su implementación concreta.
 * Al ser abstracto y usar @Binds, Hilt sabe qué implementación inyectar cuando se solicite
 * el repositorio.
 */
package com.noteapp.di

import com.noteapp.data.repository.NoteRepositoryImpl
import com.noteapp.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
}