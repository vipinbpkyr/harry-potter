package com.vipin.data.repository

import com.vipin.data.local.CharacterDao
import com.vipin.data.model.mapper.toDomain
import com.vipin.data.remote.HarryPotterApiService
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apiService: HarryPotterApiService,
    private val characterDao: CharacterDao
): CharacterRepository {

    override fun getCharacters(page: Int, pageSize: Int): Flow<List<CharacterEntity>> {
        val offset = (page - 1) * pageSize
        return characterDao.getAllCharacters(limit = pageSize, offset = offset).map { characters ->
            characters.map { it.toDomain() }
        }
    }

    override suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        val localData = characterDao.getAllCharacters(limit = 1, offset = 0).first()
        if (localData.isEmpty()) {
            val characters = apiService.getCharacters()
            characterDao.insertAll(characters)
        }
    }

    override fun getCharacterById(id: String): Flow<CharacterEntity> = characterDao.getCharacterById(id).map { it.toDomain() }
}
