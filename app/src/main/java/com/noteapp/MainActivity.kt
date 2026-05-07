/*
 * Actividad principal de la aplicación. Está anotada con @AndroidEntryPoint para que Hilt
 * pueda inyectar dependencias. Configura el tema oscuro personalizado y establece la pantalla
 * completa (edge-to-edge). En su interior coloca la navegación definida en NoteNavGraph.
 */
package com.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.noteapp.presentation.navigation.NoteNavGraph
import com.noteapp.presentation.theme.NoteAppTheme
import com.noteapp.presentation.theme.DarkBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Permite que la interfaz ocupe toda la pantalla, incluyendo las barras del sistema.
        enableEdgeToEdge()
        setContent {
            // Aplica el tema oscuro personalizado.
            NoteAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    // Punto de entrada de la navegación (lista, detalle, edición).
                    NoteNavGraph()
                }
            }
        }
    }
}