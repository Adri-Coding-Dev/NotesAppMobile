package com.noteapp.presentation.screens.edit

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.domain.model.Note
import com.noteapp.domain.usecase.GetNoteByIdUseCase
import com.noteapp.domain.usecase.InsertNoteUseCase
import com.noteapp.domain.usecase.UpdateNoteUseCase
import com.noteapp.util.RichTextUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteEditUiState(
    val noteId: Int? = null,
    val title: String = "",
    val contentAnnotated: AnnotatedString = AnnotatedString(""),
    val selectionStart: Int = 0,
    val selectionEnd: Int = 0,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
    val isEditing: Boolean = false,
    val isSelectionUnderlined: Boolean = false
)

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditUiState())
    val uiState: StateFlow<NoteEditUiState> = _uiState.asStateFlow()

    fun loadNote(id: Int?) {
        if (id == null) {
            _uiState.update { it.copy(isEditing = false) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val note = getNoteByIdUseCase(id)
                if (note != null) {
                    _uiState.update {
                        it.copy(
                            noteId = note.id,
                            title = note.title,
                            contentAnnotated = RichTextUtils.htmlToAnnotatedString(note.content),
                            isLoading = false,
                            isEditing = true
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onContentChange(annotated: AnnotatedString) {
        _uiState.update { state ->
            val isUnderlined = if (state.selectionStart < state.selectionEnd) {
                RichTextUtils.isRangeUnderlined(annotated, state.selectionStart, state.selectionEnd)
            } else false
            state.copy(contentAnnotated = annotated, isSelectionUnderlined = isUnderlined)
        }
    }

    fun onSelectionChange(start: Int, end: Int) {
        _uiState.update { state ->
            val isUnderlined = if (start < end) {
                RichTextUtils.isRangeUnderlined(state.contentAnnotated, start, end)
            } else false
            state.copy(selectionStart = start, selectionEnd = end, isSelectionUnderlined = isUnderlined)
        }
    }

    fun toggleUnderline() {
        _uiState.update { state ->
            val start = state.selectionStart
            val end = state.selectionEnd
            if (start >= end) return@update state

            val newAnnotated = if (state.isSelectionUnderlined) {
                RichTextUtils.removeUnderline(state.contentAnnotated, start, end)
            } else {
                RichTextUtils.applyUnderline(state.contentAnnotated, start, end)
            }
            state.copy(
                contentAnnotated = newAnnotated,
                isSelectionUnderlined = !state.isSelectionUnderlined
            )
        }
    }

    fun saveNote() {
        val state = _uiState.value
        if (state.title.isBlank() && state.contentAnnotated.text.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La nota no puede estar vacía") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val htmlContent = RichTextUtils.annotatedStringToHtml(state.contentAnnotated)
                val now = System.currentTimeMillis()

                if (state.noteId != null) {
                    updateNoteUseCase(
                        Note(
                            id = state.noteId,
                            title = state.title.ifBlank { "Sin título" },
                            content = htmlContent,
                            createdAt = now, // ideally preserve original; simplified here
                            updatedAt = now
                        )
                    )
                } else {
                    insertNoteUseCase(
                        Note(
                            title = state.title.ifBlank { "Sin título" },
                            content = htmlContent,
                            createdAt = now,
                            updatedAt = now
                        )
                    )
                }
                _uiState.update { it.copy(isLoading = false, isSaved = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
