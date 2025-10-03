package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GetCharacterDetailsUseCaseTest {

    @Test
    fun `invoke should return character by id from repository`() = runTest {
        // Given
        val characterId = "some-id"
        val expectedCharacter = CharacterEntity(
            id = characterId,
            name = "Harry Potter",
            actor = "Daniel Radcliffe",
            species = "human",
            house = "Gryffindor",
            image = "image_url",
            dateOfBirth = "31-07-1980",
            alive = true
        )
        val repository: CharacterRepository = mock()
        whenever(repository.getCharacterById(characterId)).thenReturn(flowOf(expectedCharacter))
        val useCase = GetCharacterDetailsUseCase(repository)

        // When
        val result = useCase(characterId).first()

        // Then
        assertEquals(expectedCharacter, result)
    }
}
