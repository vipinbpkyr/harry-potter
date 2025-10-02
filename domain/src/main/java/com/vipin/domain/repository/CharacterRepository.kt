package com.vipin.domain.repository

import kotlinx.coroutines.flow.Flow
import com.vipin.domain.entities.Character

interface CharacterRepository {
    fun getCharacters(page: Int, pageSize: Int): Flow<List<Character>>
    suspend fun refreshCharacters()
    fun getCharacterById(id: String): Flow<Character>
}
