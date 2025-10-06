package com.vipin.harrypotter.ui.charactersearch

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharactersUseCase
import com.vipin.harrypotter.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterSearchUiState(
    val characters: List<CharacterEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CharacterSearchViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase,
    private val application: Application
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterSearchUiState())
    val uiState: StateFlow<CharacterSearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchJob?.cancel()
        if (query.isNotBlank()) {
            searchJob = viewModelScope.launch {
                delay(300) // Debounce
                searchCharacters(query)
            }
        } else {
            _uiState.update { it.copy(characters = emptyList()) }
        }
    }

    private fun searchCharacters(query: String) {
        viewModelScope.launch {
            getCharactersUseCase(query = query, page = 1, pageSize = 100)
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            error = application.getString(R.string.failed_to_load_characters, e.message),
                            isLoading = false
                        )
                    }
                }
                .collect { characters ->
                    _uiState.update { it.copy(characters = characters, isLoading = false) }
                }
        }
    }
}