package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetCharactersUseCaseTest {

    private val repository: CharacterRepository = mock()

    @Test
    fun `invoke with page 1 should refresh and get characters`() = runTest {
        // Given
        val useCase = GetCharactersUseCase(repository)
        val characters = listOf(CharacterEntity("1", "Harry", "Daniel", "human", "Gryffindor", "img", "dob", true))
        whenever(repository.getCharacters(1, 20)).thenReturn(flowOf(characters))

        // When
        useCase(1, 20)

        // Then
        verify(repository).refreshCharacters()
        verify(repository).getCharacters(1, 20)
    }

    @Test
    fun `invoke with page greater than 1 should only get characters`() = runTest {
        // Given
        val useCase = GetCharactersUseCase(repository)
        val characters = listOf(CharacterEntity("2", "Ron", "Rupert", "human", "Gryffindor", "img", "dob", true))
        whenever(repository.getCharacters(2, 20)).thenReturn(flowOf(characters))

        // When
        useCase(2, 20)

        // Then
        verify(repository).getCharacters(2, 20)
    }

    @Test
    fun `invoke with query should return filtered characters`() = runTest {
        // Given
        val useCase = GetCharactersUseCase(repository)
        val characters = listOf(
            CharacterEntity("1", "Harry Potter", "Daniel Radcliffe", "human", "Gryffindor", "img", "dob", true),
            CharacterEntity("2", "Hermione Granger", "Emma Watson", "human", "Gryffindor", "img", "dob", true)
        )
        whenever(repository.getCharacters(1, 20)).thenReturn(flowOf(characters))

        // When
        val result = useCase(1, 20, "Harry").first()

        // Then
        Assert.assertEquals(1, result.size)
        Assert.assertEquals("Harry Potter", result.first().name)
    }
}
