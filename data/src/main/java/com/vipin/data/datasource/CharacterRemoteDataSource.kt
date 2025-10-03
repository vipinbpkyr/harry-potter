package com.vipin.data.datasource

import com.vipin.data.model.Character
import com.vipin.data.remote.HarryPotterApiService
import javax.inject.Inject

interface CharacterRemoteDataSource {
    suspend fun getCharacters(): List<Character>
}

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val apiService: HarryPotterApiService
) : CharacterRemoteDataSource {
    override suspend fun getCharacters(): List<Character> = apiService.getCharacters()
}
