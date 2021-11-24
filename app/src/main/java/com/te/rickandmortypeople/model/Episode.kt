package com.te.rickandmortypeople.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "episodes")
data class Episode(
    @PrimaryKey
    var id: Int?,
    @SerializedName("name")
    var title: String?,
    @SerializedName("air_date")
    var airDate: String?,
    @SerializedName("episode")
    var episodeNumber: String?,
    var characters: List<String>
): Serializable

data class EpisodeInfo(
    @SerializedName("count")
    var episodeCount: Int?,
    @SerializedName("pages")
    var episodePages: Int?,
    @SerializedName("next")
    var episodeNext: String?,
    @SerializedName("prev")
    var episodePrev: String?
): Serializable

data class EpisodeApiResults(
    @SerializedName("info")
    var episodeInfo: EpisodeInfo?,
    @SerializedName("results")
    var episodeResults: List<Episode>
): Serializable