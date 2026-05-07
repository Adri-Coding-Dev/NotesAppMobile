/*
 * Clase Application anotada con @HiltAndroidApp, necesaria para activar la inyección de
 * dependencias con Dagger Hilt en toda la aplicación.
 */
package com.noteapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NoteApplication : Application()