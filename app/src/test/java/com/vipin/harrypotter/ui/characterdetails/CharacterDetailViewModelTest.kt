package com.vipin.harrypotter.ui.characterdetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharacterDetailsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase
    private lateinit var viewModel: CharacterDetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCharacterDetailsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test view model init, character details are fetched and ui state is updated`() = runTest(testDispatcher) {
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
        val savedStateHandle = SavedStateHandle().apply {
            set("characterId", "123")
        }
        every { getCharacterDetailsUseCase("123") } returns flowOf(character)

        // When
        viewModel = CharacterDetailViewModel(getCharacterDetailsUseCase, savedStateHandle)

        // Then
        viewModel.uiState.test {
            assertEquals(true, awaitItem().isLoading)

            advanceUntilIdle()

            val successState = awaitItem()
            assertEquals(character, successState.character)
            assertEquals(false, successState.isLoading)
            assertEquals(null, successState.error)
        }
    }
}
