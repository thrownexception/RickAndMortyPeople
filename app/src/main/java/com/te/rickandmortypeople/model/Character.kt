package com.te.rickandmortypeople.model


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "characters")
data class Character(

    @PrimaryKey
    var id: Int?,
    var name: String?,
    var status: String?,
    var species: String?,
    var type: String?,
    var gender: String?,
    @Embedded
    var origin: Origin?,
    @Embedded
    var location: Location?,
    var image: String?,
    var episode: List<String>

): Serializable

data class Origin(
    @SerializedName("name")
    var originName: String?,
    @SerializedName("url")
    var originUrl: String?
)

data class Location(

    @SerializedName("name")
    var locationName: String?,
    @SerializedName("url")
    var locationUrl: String?
): Serializable

data class ApiResults(
    var info: Info?,
    var results: List<Character>
): Serializable

data class Info(
    var count: Int?,
    var pages: Int?,
    var next: String?,
    var prev: String?
): Serializable

data class CharacterPalette(var colour: Int)