package com.vipin.harrypotter.ui.characterlist

import app.cash.turbine.test
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharactersUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import io.mockk.coEvery
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
@ExperimentalCoroutinesApi
class CharacterListViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setUp() {
        hiltRule.inject()
        Dispatchers.setMain(testDispatcher)
        getCharactersUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial load, characters are fetched and ui state is updated`() = runTest(testDispatcher) {
        // Given
        val characters = listOf(CharacterEntity("1", "Harry", "Daniel", "human", "Gryffindor", "img", "dob", true))
        coEvery { getCharactersUseCase(1, 20, "") } returns flowOf(characters)

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(true, awaitItem().isLoadingInitial)

            advanceUntilIdle()

            val loadedState = awaitItem()
            assertEquals(characters, loadedState.characters)
            assertEquals(false, loadedState.isLoadingInitial)
        }
    }

    @Test
    fun `test search, characters are filtered and ui state is updated`() = runTest(testDispatcher) {
        // Given
        val initialCharacters = listOf(CharacterEntity("1", "Ron Weasley", "Rupert Grint", "human", "Gryffindor", "img", "dob", true))
        val searchResults = listOf(CharacterEntity("2", "Harry Potter", "Daniel Radcliffe", "human", "Gryffindor", "img", "dob", true))
        coEvery { getCharactersUseCase(1, 20, "") } returns flowOf(initialCharacters)
        coEvery { getCharactersUseCase(1, 20, "Harry") } returns flowOf(searchResults)

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(true, awaitItem().isLoadingInitial)

            advanceUntilIdle()

            assertEquals(initialCharacters, awaitItem().characters)

            viewModel.onSearchQueryChanged("Harry")

            assertEquals("Harry", awaitItem().searchQuery)

            advanceUntilIdle()
            val searchLoadingState = awaitItem()
            assertEquals(true, searchLoadingState.isLoadingInitial)
            
            val resultState = awaitItem()
            assertEquals(searchResults, resultState.characters)
            assertEquals(false, resultState.isLoadingInitial)
        }
    }

    @Test
    fun `test load more, new characters are added to the list`() = runTest(testDispatcher) {
        // Given
        val initialCharacters = (1..20).map { CharacterEntity(it.toString(), "Name$it", "Actor$it", "species", "house", "img", "dob", true) }
        val moreCharacters = (21..30).map { CharacterEntity(it.toString(), "Name$it", "Actor$it", "species", "house", "img", "dob", true) }
        coEvery { getCharactersUseCase(1, 20, "") } returns flowOf(initialCharacters)
        coEvery { getCharactersUseCase(2, 20, "") } returns flowOf(moreCharacters)

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)

        // Then
        viewModel.uiState.test {
            assertEquals(true, awaitItem().isLoadingInitial)

            advanceUntilIdle()

            assertEquals(20, awaitItem().characters.size)

            viewModel.loadMoreCharacters()

            val loadingMoreState = awaitItem()
            assertEquals(true, loadingMoreState.isLoadingMore)

            advanceUntilIdle()

            val loadedMoreState = awaitItem()
            assertEquals(30, loadedMoreState.characters.size)
            assertEquals(false, loadedMoreState.isLoadingMore)
        }
    }
}
