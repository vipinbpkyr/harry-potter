package com.vipin.harrypotter.ui.characterlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterListUiState(
    val characters: List<CharacterEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoadingInitial: Boolean = false,
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
        loadCharacters(pageToLoad = 1, isInitialLoad = true)
    }

    private fun loadCharacters(
        pageToLoad: Int,
        isInitialLoad: Boolean = false,
        isSearchTriggered: Boolean = false
    ) {
        viewModelScope.launch {
            if (isInitialLoad) {
                _uiState.value = _uiState.value.copy(isLoadingInitial = true, error = null)
            } else if (!isSearchTriggered) { // isLoadingMore should only be true for actual "load more" actions
                _uiState.value = _uiState.value.copy(isLoadingMore = true, error = null)
            }

            getCharactersUseCase(page = pageToLoad, pageSize = PAGE_SIZE, query = _uiState.value.searchQuery)
                .onEach { newCharactersPage ->
                    _uiState.value = _uiState.value.copy(
                        characters = if (isSearchTriggered || pageToLoad == 1) newCharactersPage else _uiState.value.characters + newCharactersPage,
                        canLoadMore = newCharactersPage.size >= PAGE_SIZE,
                        isLoadingInitial = false,
                        isLoadingMore = false
                    )
                    if (pageToLoad == 1 || isSearchTriggered) {
                        currentPage = 1
                    }
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        error = "Failed to load characters: ${e.message}",
                        isLoadingInitial = false,
                        isLoadingMore = false
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchJob?.cancel()
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            isLoadingInitial = true,
            characters = emptyList(),
            canLoadMore = true,
            error = null
        )
        currentPage = 1
        searchJob = viewModelScope.launch {
            loadCharacters(pageToLoad = 1, isInitialLoad = true, isSearchTriggered = true)
        }
    }

    fun loadMoreCharacters() {
        if (_uiState.value.isLoadingMore || !_uiState.value.canLoadMore) return
        currentPage++
        loadCharacters(pageToLoad = currentPage)
    }

    fun retryInitialLoad() {
        currentPage = 1
        loadCharacters(pageToLoad = 1, isInitialLoad = true)
    }
}