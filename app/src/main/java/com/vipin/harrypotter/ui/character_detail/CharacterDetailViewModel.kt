package com.vipin.harrypotter.ui.character_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharacterDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CharacterDetailUiState(
    val character: CharacterEntity? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CharacterDetailUiState())
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("characterId")?.let { characterId ->
            viewModelScope.launch {
                getCharacterDetailsUseCase(characterId).collectLatest { character ->
                    _uiState.value = CharacterDetailUiState(
                        character = character,
                        isLoading = false
                    )
                }
            }
        }
    }
}