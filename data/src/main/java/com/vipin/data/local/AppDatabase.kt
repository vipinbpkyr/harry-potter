package com.vipin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vipin.data.model.Character

@Database(entities = [Character::class], version = 1, exportSchema = false)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}