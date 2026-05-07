/*
 * Pantalla principal que muestra la lista de notas. Incluye búsqueda, ordenación y FAB
 * para crear nueva nota. Utiliza un LazyColumn con animaciones en los elementos.
 */
package com.noteapp.presentation.screens.list

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noteapp.domain.model.Note
import com.noteapp.presentation.components.*
import com.noteapp.presentation.theme.*
import com.noteapp.util.DateUtils
import com.noteapp.util.RichTextUtils

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    onNoteClick: (Int) -> Unit,
    onCreateNote: () -> Unit,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    var showSortMenu by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            NoteListTopBar(
                searchQuery = uiState.searchQuery,
                onSearchChange = viewModel::onSearchQueryChange,
                onSortClick = { showSortMenu = true },
                sortMenu = {
                    SortDropdownMenu(
                        expanded = showSortMenu,
                        currentOrder = uiState.sortOrder,
                        onOrderSelected = {
                            viewModel.onSortOrderChange(it)
                            showSortMenu = false
                        },
                        onDismiss = { showSortMenu = false }
                    )
                }
            )
        },
        floatingActionButton = {
            PrimaryFab(onClick = onCreateNote)
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                ErrorSnackbar(
                    message = data.visuals.message,
                    onDismiss = { data.dismiss() }
                )
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> LoadingOverlay()
                uiState.notes.isEmpty() -> {
                    EmptyNotesPlaceholder(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item {
                            Text(
                                text = "${uiState.notes.size} nota${if (uiState.notes.size != 1) "s" else ""}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceSubtle,
                                    letterSpacing = 1.sp
                                ),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                        items(
                            items = uiState.notes,
                            key = { it.id }
                        ) { note ->
                            NoteCard(
                                note = note,
                                onClick = { onNoteClick(note.id) },
                                onDelete = { viewModel.deleteNote(note) },
                                modifier = Modifier.animateItemPlacement(
                                    animationSpec = tween(300)
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

// Barra superior con campo de búsqueda expandible y menú de ordenación.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteListTopBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSortClick: () -> Unit,
    sortMenu: @Composable () -> Unit
) {
    var searchExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkBackground)
    ) {
        TopAppBar(
            title = {
                if (!searchExpanded) {
                    Column {
                        Text(
                            text = "MisNotas",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = OnBackground
                            )
                        )
                    }
                }
            },
            actions = {
                if (searchExpanded) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text("Buscar notas...", color = OnSurfaceSubtle, fontSize = 14.sp)
                        },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentPrimary,
                            unfocusedBorderColor = DividerColor,
                            focusedTextColor = OnBackground,
                            unfocusedTextColor = OnBackground,
                            cursorColor = AccentPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                    )
                }
                IconButton(onClick = {
                    searchExpanded = !searchExpanded
                    if (!searchExpanded) onSearchChange("")
                }) {
                    Icon(
                        imageVector = if (searchExpanded) Icons.Default.Close else Icons.Outlined.Search,
                        contentDescription = "Buscar",
                        tint = if (searchExpanded) AccentPrimary else OnSurface
                    )
                }
                Box {
                    IconButton(onClick = onSortClick) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "Ordenar",
                            tint = OnSurface
                        )
                    }
                    sortMenu()
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
        )
        GradientDivider()
    }
}

// Menú desplegable para cambiar el criterio de ordenación.
@Composable
private fun SortDropdownMenu(
    expanded: Boolean,
    currentOrder: SortOrder,
    onOrderSelected: (SortOrder) -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(DarkSurface)
    ) {
        SortOrder.entries.forEach { order ->
            val label = when (order) {
                SortOrder.DATE_DESC -> "Más recientes primero"
                SortOrder.DATE_ASC -> "Más antiguas primero"
                SortOrder.TITLE_ASC -> "Título (A-Z)"
            }
            DropdownMenuItem(
                text = {
                    Text(
                        text = label,
                        color = if (currentOrder == order) AccentPrimary else OnSurface
                    )
                },
                onClick = { onOrderSelected(order) },
                leadingIcon = {
                    if (currentOrder == order) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = AccentPrimary, modifier = Modifier.size(16.dp))
                    }
                }
            )
        }
    }
}

// Tarjeta individual de nota con acento de color y vista previa del contenido.
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val preview = RichTextUtils.stripHtml(note.content).take(120)

    // Acento de color basado en el id para variedad visual.
    val accentColor = remember(note.id) {
        listOf(AccentPrimary, AccentSecondary, AccentWarning, Pink80.copy(alpha = 0.8f))
            .getOrElse(note.id % 4) { AccentPrimary }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Barra superior de acento.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(accentColor, accentColor.copy(alpha = 0.0f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = note.title.ifBlank { "Sin título" },
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = OnBackground
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.DeleteOutline,
                            contentDescription = "Eliminar",
                            tint = AccentError.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (preview.isNotBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = preview,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = OnSurfaceSubtle,
                            lineHeight = 18.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ChipLabel(
                        text = DateUtils.formatShort(note.updatedAt),
                        color = accentColor
                    )
                }
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            onConfirm = {
                onDelete()
                showDeleteDialog = false
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}