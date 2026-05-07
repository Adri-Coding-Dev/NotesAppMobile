/*
 * Pantalla que muestra el detalle de una nota. Carga la nota desde el ViewModel, convierte el
 * contenido HTML a texto enriquecido con AnnotatedString y lo presenta. Incluye botones para
 * editar y eliminar, así como un diálogo de confirmación de borrado.
 */
package com.noteapp.presentation.screens.detail

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noteapp.presentation.components.*
import com.noteapp.presentation.theme.*
import com.noteapp.util.DateUtils
import com.noteapp.util.RichTextUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    onEditNote: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
    }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) onBack()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Volver", tint = OnBackground)
                    }
                },
                actions = {
                    uiState.note?.let {
                        IconButton(onClick = { onEditNote(noteId) }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = AccentPrimary
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = AccentError
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                ErrorSnackbar(message = data.visuals.message, onDismiss = { data.dismiss() })
            }
        },
        containerColor = DarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> LoadingOverlay()
                uiState.note == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Nota no encontrada", color = OnSurfaceSubtle)
                    }
                }
                else -> {
                    val note = uiState.note!!
                    // Convierte el HTML a una cadena enriquecida para mostrar subrayados.
                    val annotatedContent = remember(note.content) {
                        RichTextUtils.htmlToAnnotatedString(note.content)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        // Título
                        Text(
                            text = note.title.ifBlank { "Sin título" },
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = OnBackground,
                                lineHeight = 36.sp
                            )
                        )

                        Spacer(Modifier.height(12.dp))

                        // Fechas de creación y edición.
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailMetaChip(
                                label = "Creada",
                                value = DateUtils.formatFull(note.createdAt)
                            )
                            if (note.updatedAt != note.createdAt) {
                                DetailMetaChip(
                                    label = "Editada",
                                    value = DateUtils.formatShort(note.updatedAt),
                                    color = AccentWarning
                                )
                            }
                        }

                        Spacer(Modifier.height(24.dp))
                        GradientDivider()
                        Spacer(Modifier.height(24.dp))

                        // Contenido enriquecido.
                        Text(
                            text = annotatedContent,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = OnSurface,
                                lineHeight = 28.sp
                            )
                        )

                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteNote()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

// Componente privado para mostrar una pequeña etiqueta de metadato.
@Composable
private fun DetailMetaChip(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color = AccentPrimary
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color.copy(alpha = 0.12f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = label,
                color = color,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
        Text(
            text = value,
            color = OnSurfaceSubtle,
            fontSize = 12.sp
        )
    }
}