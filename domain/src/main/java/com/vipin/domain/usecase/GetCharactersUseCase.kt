package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        query: String = ""
    ): Flow<List<CharacterEntity>> {
        if (page == 1 && query.isBlank()) {
            repository.refreshCharacters()
        }
        return repository.getCharacters(page = page, pageSize = pageSize, query = query)
    }
}
