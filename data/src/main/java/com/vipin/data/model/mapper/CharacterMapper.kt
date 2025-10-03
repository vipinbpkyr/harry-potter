package com.vipin.data.model.mapper

import com.vipin.data.model.Character
import com.vipin.domain.entities.CharacterEntity

internal fun Character.toDomain(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        name = this.name,
        actor = this.actor,
        species = this.species,
        house = this.house,
        image = this.image,
        dateOfBirth = this.dateOfBirth,
        alive = this.alive
    )
}
