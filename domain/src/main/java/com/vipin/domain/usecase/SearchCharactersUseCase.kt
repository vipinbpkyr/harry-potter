package com.vipin.domain.usecase

import javax.inject.Inject
import com.vipin.domain.entities.Character

class SearchCharactersUseCase @Inject constructor() {
    operator fun invoke(characters: List<Character>, query: String): List<Character> {
        return if (query.isBlank()) {
            characters
        } else {
            characters.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.actor.contains(query, ignoreCase = true)
            }
        }
    }
}