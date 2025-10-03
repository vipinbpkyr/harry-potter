package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCharacterDetailsUseCaseTest {

    private lateinit var repository: CharacterRepository
    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getCharacterDetailsUseCase = GetCharacterDetailsUseCase(repository)
    }

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
        every { repository.getCharacterById(characterId) } returns flowOf(expectedCharacter)

        // When
        val result = getCharacterDetailsUseCase(characterId).first()

        // Then
        verify { repository.getCharacterById(characterId) }
        assertEquals(expectedCharacter, result)
    }
}
