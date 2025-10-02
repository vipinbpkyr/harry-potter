package com.vipin.domain.usecase

import javax.inject.Inject
import com.vipin.domain.entities.CharacterEntity

class SearchCharactersUseCase @Inject constructor() {
    operator fun invoke(characterEntities: List<CharacterEntity>, query: String): List<CharacterEntity> {
        return if (query.isBlank()) {
            characterEntities
        } else {
            characterEntities.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.actor.contains(query, ignoreCase = true)
            }
        }
    }
}