package com.vipin.data.datasource

import com.vipin.data.local.CharacterDao
import com.vipin.data.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CharacterLocalDataSource {
    fun getAllCharacters(limit: Int, offset: Int, query: String): Flow<List<Character>>
    fun getCharacterById(id: String): Flow<Character>
    suspend fun insertAll(characters: List<Character>)
}

class CharacterLocalDataSourceImpl @Inject constructor(
    private val characterDao: CharacterDao
) : CharacterLocalDataSource {
    override fun getAllCharacters(limit: Int, offset: Int, query: String): Flow<List<Character>> =
        characterDao.getAllCharacters(limit, offset, query)

    override fun getCharacterById(id: String): Flow<Character> = characterDao.getCharacterById(id)

    override suspend fun insertAll(characters: List<Character>) = characterDao.insertAll(characters)
}
