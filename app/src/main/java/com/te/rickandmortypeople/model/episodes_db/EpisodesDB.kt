package com.te.rickandmortypeople.model.episodes_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.te.rickandmortypeople.model.Episode

@Database(entities = [Episode::class], version = 1)
@TypeConverters(EpisodeConverters::class)
abstract class EpisodesDB : RoomDatabase() {

    abstract fun episodesDao(): EpisodesDao

    companion object{
        @Volatile private var  instance: EpisodesDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            EpisodesDB::class.java,
            "episodes"
        ).fallbackToDestructiveMigration().build()

    }


}