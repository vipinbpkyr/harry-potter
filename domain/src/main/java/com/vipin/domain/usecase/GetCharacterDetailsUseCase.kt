package com.vipin.domain.usecase

import com.vipin.domain.entities.Character
import com.vipin.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: CharacterRepository
) {
    operator fun invoke(id: String): Flow<Character> {
        return repository.getCharacterById(id)
    }
}