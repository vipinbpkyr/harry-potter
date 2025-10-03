package com.vipin.harrypotter.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharactersUseCase
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

data class CharacterListUiState(
    val characters: List<CharacterEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoadingInitial: Boolean = true, // Default to true
    val isLoadingMore: Boolean = false,
    val canLoadMore: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterListUiState())
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private var currentPage = 1
    private var searchJob: Job? = null

    companion object {
        private const val PAGE_SIZE = 20
    }

    init {
        loadCharacters(isInitialLoad = true)
    }

    private fun loadCharacters(isInitialLoad: Boolean) {
        val pageToLoad = if (isInitialLoad) 1 else currentPage

        viewModelScope.launch {
            getCharactersUseCase(page = pageToLoad, pageSize = PAGE_SIZE, query = _uiState.value.searchQuery)
                .onStart {
                    _uiState.update {
                        it.copy(
                            isLoadingInitial = isInitialLoad,
                            isLoadingMore = !isInitialLoad,
                            error = null
                        )
                    }
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            error = "Failed to load characters: ${e.message}",
                            isLoadingInitial = false,
                            isLoadingMore = false
                        )
                    }
                }
                .collect { newCharacters ->
                    _uiState.update {
                        it.copy(
                            characters = if (pageToLoad == 1) newCharacters else it.characters + newCharacters,
                            canLoadMore = newCharacters.size >= PAGE_SIZE,
                            isLoadingInitial = false,
                            isLoadingMore = false
                        )
                    }
                    if (pageToLoad == 1) {
                        currentPage = 1
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        _uiState.update { it.copy(searchQuery = query) }
        currentPage = 1
        searchJob = viewModelScope.launch {
            delay(300)
            loadCharacters(isInitialLoad = true)
        }
    }

    fun loadMoreCharacters() {
        if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) return
        currentPage++
        loadCharacters(isInitialLoad = false)
    }

    fun retryInitialLoad() {
        loadCharacters(isInitialLoad = true)
    }
}
