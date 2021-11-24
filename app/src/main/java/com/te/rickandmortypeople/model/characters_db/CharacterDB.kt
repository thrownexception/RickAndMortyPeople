package com.te.rickandmortypeople.model.characters_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.te.rickandmortypeople.model.Character
import com.te.rickandmortypeople.model.episodes_db.EpisodeConverters


@Database(entities = [Character::class], version = 1)
@TypeConverters(EpisodeConverters::class)
abstract class CharacterDB: RoomDatabase(){

    abstract fun characterDao(): CharacterDao

    companion object{
        @Volatile private var instance: CharacterDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            CharacterDB::class.java,
            "characters"
        ).fallbackToDestructiveMigration().build()
    }
}