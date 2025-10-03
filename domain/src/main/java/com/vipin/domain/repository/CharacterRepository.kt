package com.vipin.domain.repository

import kotlinx.coroutines.flow.Flow
import com.vipin.domain.entities.CharacterEntity

interface CharacterRepository {
    fun getCharacters(page: Int, pageSize: Int, query: String): Flow<List<CharacterEntity>>
    suspend fun refreshCharacters()
    fun getCharacterById(id: String): Flow<CharacterEntity>
}
