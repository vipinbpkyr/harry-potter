package com.vipin.data.datasource

import com.vipin.data.model.Character
import com.vipin.data.remote.HarryPotterApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CharacterRemoteDataSourceImplTest {

    private lateinit var apiService: HarryPotterApiService
    private lateinit var remoteDataSource: CharacterRemoteDataSourceImpl

    @Before
    fun setUp() {
        apiService = mockk()
        remoteDataSource = CharacterRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getCharacters should call apiService's getCharacters`() = runTest {
        // Given
        val characters = listOf(mockk<Character>())
        coEvery { apiService.getCharacters() } returns characters

        // When
        val result = remoteDataSource.getCharacters()

        // Then
        coVerify { apiService.getCharacters() }
        assertEquals(characters, result)
    }
}
