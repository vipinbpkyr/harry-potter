package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCharactersUseCaseTest {

    private lateinit var repository: CharacterRepository
    private lateinit var getCharactersUseCase: GetCharactersUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getCharactersUseCase = GetCharactersUseCase(repository)
    }

    @Test
    fun `invoke with page 1 and no query should refresh and get characters`() = runTest {
        // Given
        val characters = listOf(CharacterEntity("1", "Harry", "Daniel", "human", "Gryffindor", "img", "dob", true))
        coEvery { repository.refreshCharacters() } returns Unit
        every { repository.getCharacters(1, 20, "") } returns flowOf(characters)

        // When
        getCharactersUseCase(1, 20, "")

        // Then
        coVerify { repository.refreshCharacters() }
        verify { repository.getCharacters(1, 20, "") }
    }

    @Test
    fun `invoke with page greater than 1 should only get characters`() = runTest {
        // Given
        val characters = listOf(CharacterEntity("2", "Ron", "Rupert", "human", "Gryffindor", "img", "dob", true))
        every { repository.getCharacters(2, 20, "") } returns flowOf(characters)

        // When
        getCharactersUseCase(2, 20, "")

        // Then
        verify { repository.getCharacters(2, 20, "") }
        coVerify(exactly = 0) { repository.refreshCharacters() }
    }

    @Test
    fun `invoke with query should return filtered characters from repository`() = runTest {
        // Given
        val query = "Harry"
        val filteredCharacters = listOf(
            CharacterEntity("1", "Harry Potter", "Daniel Radcliffe", "human", "Gryffindor", "img", "dob", true)
        )
        every { repository.getCharacters(1, 20, query) } returns flowOf(filteredCharacters)

        // When
        val result = getCharactersUseCase(1, 20, query).first()

        // Then
        verify { repository.getCharacters(1, 20, query) }
        assertEquals(filteredCharacters, result)
        coVerify(exactly = 0) { repository.refreshCharacters() }
    }
}
