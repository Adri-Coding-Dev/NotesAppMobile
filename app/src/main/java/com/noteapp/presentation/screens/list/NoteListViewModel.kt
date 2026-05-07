/*
 * ViewModel de la lista de notas. Observa los cambios en la base de datos mediante Flow,
 * aplica búsqueda con debounce y permite ordenar y eliminar notas.
 */
package com.noteapp.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.domain.model.Note
import com.noteapp.domain.usecase.DeleteNoteUseCase
import com.noteapp.domain.usecase.GetAllNotesUseCase
import com.noteapp.domain.usecase.SearchNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoteListUiState(
    val notes: List<Note> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val recentlyDeletedNote: Note? = null,
    val sortOrder: SortOrder = SortOrder.DATE_DESC
)

enum class SortOrder { DATE_DESC, DATE_ASC, TITLE_ASC }

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteListUiState(isLoading = true))
    val uiState: StateFlow<NoteListUiState> = _uiState.asStateFlow()

    // Flow que emite la consulta de búsqueda con un retardo para evitar peticiones excesivas.
    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeNotes()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeNotes() {
        // Cada vez que cambia la consulta (con debounce), se obtienen las notas correspondientes.
        searchQueryFlow
            .debounce(300)
            .flatMapLatest { query ->
                if (query.isBlank()) getAllNotesUseCase()
                else searchNotesUseCase(query)
            }
            .onEach { notes ->
                _uiState.update { state ->
                    val sorted = when (state.sortOrder) {
                        SortOrder.DATE_DESC -> notes.sortedByDescending { it.updatedAt }
                        SortOrder.DATE_ASC -> notes.sortedBy { it.updatedAt }
                        SortOrder.TITLE_ASC -> notes.sortedBy { it.title.lowercase() }
                    }
                    state.copy(notes = sorted, isLoading = false)
                }
            }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        searchQueryFlow.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSortOrderChange(order: SortOrder) {
        _uiState.update { state ->
            val sorted = when (order) {
                SortOrder.DATE_DESC -> state.notes.sortedByDescending { it.updatedAt }
                SortOrder.DATE_ASC -> state.notes.sortedBy { it.updatedAt }
                SortOrder.TITLE_ASC -> state.notes.sortedBy { it.title.lowercase() }
            }
            state.copy(sortOrder = order, notes = sorted)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                deleteNoteUseCase(note)
                _uiState.update { it.copy(recentlyDeletedNote = note) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error al eliminar la nota") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}