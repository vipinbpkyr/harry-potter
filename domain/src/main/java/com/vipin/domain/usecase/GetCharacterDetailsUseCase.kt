package com.vipin.domain.usecase

import com.vipin.domain.entities.CharacterEntity
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(id: String): Flow<CharacterEntity> {
        return repository.getCharacterById(id)
    }
}