package com.vipin.harrypotter.ui.charactersearch

import android.app.Application
import app.cash.turbine.test
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharactersUseCase
import com.vipin.harrypotter.R
import com.vipin.harrypotter.utils.MainCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterSearchViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule(testDispatcher)

    @MockK
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @MockK
    private lateinit var application: Application

    private lateinit var viewModel: CharacterSearchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = CharacterSearchViewModel(getCharactersUseCase, application)
    }

    @Test
    fun `onSearchQueryChanged with non-blank query, triggers search and updates state`() = runTest {
        // Given
        val query = "Harry"
        val characters = listOf(CharacterEntity("1", "Harry Potter", "Daniel Radcliffe", "Gryffindor", "human", "", "", true))
        coEvery { getCharactersUseCase(query = query, page = 1, pageSize = 100) } returns flowOf(characters)

        viewModel.uiState.test {
            assertEquals(CharacterSearchUiState(), awaitItem())

            // When
            viewModel.onSearchQueryChanged(query)

            // Then
            assertEquals(query, awaitItem().searchQuery)
            advanceUntilIdle()
            assertTrue(awaitItem().isLoading)

            val resultState = awaitItem()
            assertFalse(resultState.isLoading)
            assertEquals(characters, resultState.characters)
            assertNull(resultState.error)
        }
    }

    @Test
    fun `onSearchQueryChanged with blank query, clears characters`() = runTest {
        viewModel.uiState.test {
            // Given
            viewModel.onSearchQueryChanged("test")
            awaitItem()
            assertEquals("test", awaitItem().searchQuery)

            // When
            viewModel.onSearchQueryChanged("")

            // Then
            val clearedState = awaitItem()
            assertEquals("", clearedState.searchQuery)
            assertTrue(clearedState.characters.isEmpty())
            advanceUntilIdle()
            ensureAllEventsConsumed()
        }
    }

    @Test
    fun `searchCharacters handles error from use case`() = runTest {
        // Given
        val query = "Harry"
        val errorMessage = "Network Error"
        coEvery { getCharactersUseCase(query = query, page = 1, pageSize = 100) } returns flow { throw Exception(errorMessage) }
        val expectedErrorString = "Failed to load characters: $errorMessage"
        every { application.getString(R.string.failed_to_load_characters, errorMessage) } returns expectedErrorString

        viewModel.uiState.test {
            assertEquals(CharacterSearchUiState(), awaitItem())

            // When
            viewModel.onSearchQueryChanged(query)

            // Then
            assertEquals(query, awaitItem().searchQuery)
            advanceUntilIdle()
            assertTrue(awaitItem().isLoading)

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertEquals(expectedErrorString, errorState.error)
        }
    }
}
