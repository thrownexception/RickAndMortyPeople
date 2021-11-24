package com.te.rickandmortypeople.model.characters_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.te.rickandmortypeople.model.Character

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM characters WHERE name= :name")
    fun getAllNames(name: String?): List<Character>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacters(characters: List<Character>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(character: Character)
}