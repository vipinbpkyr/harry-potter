package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        query: String = ""
    ): Flow<List<CharacterEntity>> {
        if (page == 1) {
            repository.refreshCharacters()
        }
        return repository.getCharacters(page = page, pageSize = pageSize).map { characters ->
            if (query.isBlank()) {
                characters
            } else {
                characters.filter {
                    it.name.contains(query, ignoreCase = true) || it.actor.contains(
                        query,
                        ignoreCase = true
                    )
                }
            }
        }
    }
}
