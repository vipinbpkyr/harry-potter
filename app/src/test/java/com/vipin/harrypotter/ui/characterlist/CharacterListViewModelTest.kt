package com.vipin.harrypotter.ui.characterlist

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.usecase.GetCharactersUseCase
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
class CharacterListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getCharactersUseCase: GetCharactersUseCase
    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getCharactersUseCase = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test initial load, characters are fetched and ui state is updated`() = runTest {
        // Given
        val characters = listOf(CharacterEntity("1", "Harry", "Daniel", "human", "Gryffindor", "img", "dob", true))
        whenever(getCharactersUseCase(1, 20)).thenReturn(flowOf(characters))

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertEquals(characters, uiState.characters)
        assertEquals(false, uiState.isLoadingInitial)
    }

    @Test
    fun `test search, characters are filtered and ui state is updated`() = runTest {
        // Given
        val characters = listOf(
            CharacterEntity("1", "Harry Potter", "Daniel", "human", "Gryffindor", "img", "dob", true),
            CharacterEntity("2", "Hermione Granger", "Emma", "human", "Gryffindor", "img", "dob", true)
        )
        whenever(getCharactersUseCase(1, 20)).thenReturn(flowOf(characters))
        viewModel = CharacterListViewModel(getCharactersUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.onSearchQueryChanged("Harry")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertEquals(1, uiState.characters.size)
        assertEquals("Harry Potter", uiState.characters.first().name)
    }

    @Test
    fun `test load more, new characters are added to the list`() = runTest {
        // Given
        val initialCharacters = (1..20).map { CharacterEntity(it.toString(), "Name$it", "Actor$it", "species", "house", "img", "dob", true) }
        val moreCharacters = (21..30).map { CharacterEntity(it.toString(), "Name$it", "Actor$it", "species", "house", "img", "dob", true) }
        whenever(getCharactersUseCase(1, 20)).thenReturn(flowOf(initialCharacters))
        whenever(getCharactersUseCase(2, 20)).thenReturn(flowOf(moreCharacters))

        // When
        viewModel = CharacterListViewModel(getCharactersUseCase)
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadMoreCharacters()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value
        assertEquals(30, uiState.characters.size)
        assertEquals(false, uiState.isLoadingMore)
    }
}
