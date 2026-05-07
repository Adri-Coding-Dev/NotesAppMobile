package com.noteapp.presentation.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.domain.model.Note
import com.noteapp.domain.usecase.DeleteNoteUseCase
import com.noteapp.domain.usecase.GetNoteByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteDetailUiState(
    val note: Note? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isDeleted: Boolean = false
)

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteDetailUiState())
    val uiState: StateFlow<NoteDetailUiState> = _uiState.asStateFlow()

    fun loadNote(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val note = getNoteByIdUseCase(id)
                _uiState.update { it.copy(note = note, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun deleteNote() {
        viewModelScope.launch {
            _uiState.value.note?.let { note ->
                try {
                    deleteNoteUseCase(note)
                    _uiState.update { it.copy(isDeleted = true) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(errorMessage = "Error al eliminar la nota") }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
