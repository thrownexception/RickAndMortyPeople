package com.te.rickandmortypeople.model.episodes_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.te.rickandmortypeople.model.Episode

@Dao
interface EpisodesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(episodes: List<Episode>)

    @Query("SELECT * FROM episodes")
    fun getAllEpisodes(): List<Episode>

    @Query("SELECT * FROM episodes WHERE id = :id")
    fun getSelectedEpisode(id: Int): Episode

}