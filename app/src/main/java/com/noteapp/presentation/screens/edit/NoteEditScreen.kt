package com.noteapp.presentation.screens.edit

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.noteapp.presentation.components.*
import com.noteapp.presentation.theme.*

@Composable
fun NoteEditScreen(
    noteId: Int?,
    onSave: () -> Unit,
    onBack: () -> Unit,
    viewModel: NoteEditViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val contentFocusRequester = remember { FocusRequester() }

    // TextFieldValue tracks both text+spans AND cursor/selection
    var contentFieldValue by remember {
        mutableStateOf(TextFieldValue(annotatedString = AnnotatedString("")))
    }

    // Sync field value when note loads
    LaunchedEffect(uiState.contentAnnotated) {
        val current = contentFieldValue.annotatedString
        if (current.text != uiState.contentAnnotated.text ||
            current.spanStyles != uiState.contentAnnotated.spanStyles) {
            contentFieldValue = contentFieldValue.copy(annotatedString = uiState.contentAnnotated)
        }
    }

    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSave()
    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    val hasSelection = uiState.selectionStart < uiState.selectionEnd

    Scaffold(
        topBar = {
            EditTopBar(
                isEditing = uiState.isEditing,
                isSaving = uiState.isLoading,
                onBack = onBack,
                onSave = viewModel::saveNote
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                ErrorSnackbar(message = data.visuals.message, onDismiss = { data.dismiss() })
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Rich text toolbar
            RichTextToolbar(
                hasSelection = hasSelection,
                isUnderlined = uiState.isSelectionUnderlined,
                onUnderlineClick = {
                    viewModel.toggleUnderline()
                    // Re-sync field value after toggle
                    contentFieldValue = contentFieldValue.copy(
                        annotatedString = viewModel.uiState.value.contentAnnotated
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                // Title field
                BasicTextField(
                    value = uiState.title,
                    onValueChange = viewModel::onTitleChange,
                    textStyle = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        lineHeight = 34.sp
                    ),
                    cursorBrush = SolidColor(AccentPrimary),
                    decorationBox = { inner ->
                        Box {
                            if (uiState.title.isEmpty()) {
                                Text(
                                    "Título de la nota",
                                    style = TextStyle(
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = OnSurfaceSubtle.copy(alpha = 0.5f),
                                        lineHeight = 34.sp
                                    )
                                )
                            }
                            inner()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))
                GradientDivider()
                Spacer(Modifier.height(16.dp))

                // Rich text content field
                BasicTextField(
                    value = contentFieldValue,
                    onValueChange = { newValue ->
                        contentFieldValue = newValue
                        viewModel.onContentChange(newValue.annotatedString)
                        val sel = newValue.selection
                        viewModel.onSelectionChange(sel.start, sel.end)
                    },
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = OnSurface,
                        lineHeight = 26.sp
                    ),
                    cursorBrush = SolidColor(AccentPrimary),
                    decorationBox = { inner ->
                        Box {
                            if (contentFieldValue.text.isEmpty()) {
                                Text(
                                    "Escribe tu nota aquí...",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        color = OnSurfaceSubtle.copy(alpha = 0.5f),
                                        lineHeight = 26.sp
                                    )
                                )
                            }
                            inner()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 300.dp)
                        .focusRequester(contentFocusRequester)
                )

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditTopBar(
    isEditing: Boolean,
    isSaving: Boolean,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    Column(Modifier.background(DarkBackground)) {
        TopAppBar(
            title = {
                Text(
                    text = if (isEditing) "Editar nota" else "Nueva nota",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = OnBackground
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.Close, "Cancelar", tint = OnSurface)
                }
            },
            actions = {
                SaveButton(isSaving = isSaving, onClick = onSave)
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
        )
        GradientDivider()
    }
}

@Composable
private fun SaveButton(isSaving: Boolean, onClick: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (isSaving) 0.95f else 1f,
        label = "save_scale"
    )
    Button(
        onClick = onClick,
        enabled = !isSaving,
        colors = ButtonDefaults.buttonColors(
            containerColor = AccentPrimary,
            contentColor = OnPrimary,
            disabledContainerColor = AccentPrimary.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        modifier = Modifier
            .padding(end = 8.dp)
            .scale(scale)
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                color = OnPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text("Guardar", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
        }
    }
}

@Composable
private fun RichTextToolbar(
    hasSelection: Boolean,
    isUnderlined: Boolean,
    onUnderlineClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DarkSurface)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Label
        Text(
            text = "FORMATO",
            color = OnSurfaceSubtle,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(end = 8.dp)
        )

        // Underline toggle button
        FormatButton(
            label = "U",
            active = isUnderlined && hasSelection,
            enabled = hasSelection,
            textDecoration = TextDecoration.Underline,
            activeColor = AccentSecondary,
            onClick = onUnderlineClick
        )

        Spacer(Modifier.weight(1f))

        // Selection hint
        AnimatedVisibility(
            visible = !hasSelection,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = "Selecciona texto para aplicar formato",
                color = OnSurfaceSubtle.copy(alpha = 0.6f),
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }

        AnimatedVisibility(
            visible = hasSelection,
            enter = fadeIn() + slideInHorizontally(),
            exit = fadeOut() + slideOutHorizontally()
        ) {
            ChipLabel(
                text = if (isUnderlined) "Quitar subrayado" else "Aplicar subrayado",
                color = AccentSecondary
            )
        }
    }
}

@Composable
private fun FormatButton(
    label: String,
    active: Boolean,
    enabled: Boolean,
    textDecoration: TextDecoration,
    activeColor: Color,
    onClick: () -> Unit
) {
    val animatedBg by animateColorAsState(
        targetValue = if (active) activeColor.copy(alpha = 0.2f) else Color.Transparent,
        label = "btn_bg"
    )
    val animatedBorder by animateColorAsState(
        targetValue = if (active) activeColor else DividerColor,
        label = "btn_border"
    )
    val animatedText by animateColorAsState(
        targetValue = when {
            active -> activeColor
            enabled -> OnSurface
            else -> OnSurfaceSubtle.copy(alpha = 0.4f)
        },
        label = "btn_text"
    )

    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(animatedBg)
            .border(1.dp, animatedBorder, RoundedCornerShape(8.dp))
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = animatedText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            style = TextStyle(textDecoration = if (active) textDecoration else TextDecoration.None)
        )
    }
}
