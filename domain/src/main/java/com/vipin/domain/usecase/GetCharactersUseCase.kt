package com.vipin.domain.usecase

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.vipin.domain.entities.Character
import com.vipin.domain.repository.CharacterRepository

class GetCharactersUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(page: Int, pageSize: Int): Flow<List<Character>> {
        if (page == 1) {
            repository.refreshCharacters()
        }
        return repository.getCharacters(page = page, pageSize = pageSize)
    }
}