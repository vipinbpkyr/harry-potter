package com.vipin.data.repository

import com.vipin.data.datasource.CharacterLocalDataSource
import com.vipin.data.datasource.CharacterRemoteDataSource
import com.vipin.data.model.mapper.toDomain
import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource
) : CharacterRepository {

    override fun getCharacters(page: Int, pageSize: Int, query: String): Flow<List<CharacterEntity>> {
        val offset = (page - 1) * pageSize
        return localDataSource.getAllCharacters(limit = pageSize, offset = offset, query = query).map { characters ->
            characters.map { it.toDomain() }
        }
    }

    override suspend fun refreshCharacters() = withContext(Dispatchers.IO) {
        val localData = localDataSource.getAllCharacters(limit = 1, offset = 0, query = "").first()
        if (localData.isEmpty()) {
            val characters = remoteDataSource.getCharacters()
            localDataSource.insertAll(characters)
        }
    }

    override fun getCharacterById(id: String): Flow<CharacterEntity> =
        localDataSource.getCharacterById(id).map { it.toDomain() }
}
