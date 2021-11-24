package com.te.rickandmortypeople.model.episodes_db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EpisodeConverters {

    @TypeConverter
    fun fromList(listOfString: List<String?>): String?{
        val gson = Gson()
        return gson.toJson(listOfString)
    }

    @TypeConverter
    fun fromString(value: String): List<String>{
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }


}