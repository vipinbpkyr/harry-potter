package com.vipin.data.datasource

import com.vipin.data.local.CharacterDao
import com.vipin.data.model.Character
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

class CharacterLocalDataSourceImplTest {

    private lateinit var characterDao: CharacterDao
    private lateinit var localDataSource: CharacterLocalDataSourceImpl

    @Before
    fun setUp() {
        characterDao = mockk()
        localDataSource = CharacterLocalDataSourceImpl(characterDao)
    }

    @Test
    fun `getAllCharacters should call dao's getAllCharacters`() = runTest {
        // Given
        val characters = listOf(mockk<Character>())
        every { characterDao.getAllCharacters(10, 0, "") } returns flowOf(characters)

        // When
        val result = localDataSource.getAllCharacters(10, 0, "").first()

        // Then
        verify { characterDao.getAllCharacters(10, 0, "") }
        assertEquals(characters, result)
    }

    @Test
    fun `getCharacterById should call dao's getCharacterById`() = runTest {
        // Given
        val character = mockk<Character>()
        every { characterDao.getCharacterById("1") } returns flowOf(character)

        // When
        val result = localDataSource.getCharacterById("1").first()

        // Then
        verify { characterDao.getCharacterById("1") }
        assertEquals(character, result)
    }

    @Test
    fun `insertAll should call dao's insertAll`() = runTest {
        // Given
        val characters = listOf(mockk<Character>())
        coEvery { characterDao.insertAll(characters) } returns Unit

        // When
        localDataSource.insertAll(characters)

        // Then
        coVerify { characterDao.insertAll(characters) }
    }
}
