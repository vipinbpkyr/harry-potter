package com.vipin.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vipin.data.model.Character
import kotlinx.coroutines.flow.Flow

@Dao
internal interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<Character>)

    @Query("SELECT * FROM characters WHERE (:query = '' OR name LIKE '%' || :query || '%' OR actor LIKE '%' || :query || '%') LIMIT :limit OFFSET :offset")
    fun getAllCharacters(limit: Int, offset: Int, query: String): Flow<List<Character>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterById(id: String): Flow<Character>

    @Query("DELETE FROM characters")
    suspend fun deleteAll()
}
