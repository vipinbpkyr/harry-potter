package com.vipin.harrypotter.ui.characterdetails

import androidx.lifecycle.SavedStateHandle
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharacterDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var viewModel: CharacterDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCharacterDetailsUseCase = mock()
        savedStateHandle = SavedStateHandle().apply {
            set("characterId", "123")
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test view model init, character details are fetched and ui state is updated`() = runTest {
        // Given
        val character = CharacterEntity(
            id = "123",
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "human",
            house = "Gryffindor",
            image = "image_url",
            dateOfBirth = "31-07-1980",
            alive = true
        )
        whenever(getCharacterDetailsUseCase("123")).thenReturn(flowOf(character))

        // When
        viewModel = CharacterDetailViewModel(getCharacterDetailsUseCase, savedStateHandle)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertEquals(character, uiState.character)
        assertEquals(false, uiState.isLoading)
        assertEquals(null, uiState.error)
    }
}
