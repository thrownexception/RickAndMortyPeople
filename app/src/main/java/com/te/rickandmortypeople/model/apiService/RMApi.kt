package com.te.rickandmortypeople.model.apiService

import com.te.rickandmortypeople.model.ApiResults
import com.te.rickandmortypeople.model.EpisodeApiResults
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RMApi {

    @GET("api/character")
    fun getCharacters(): Call<ApiResults>

    @GET("api/character")
    fun getRestOfCharacters(@Query("page") page: Int): Call<ApiResults>

    @GET("api/episode")
    fun getEpisodes(): Call<EpisodeApiResults>

    @GET("api/episode")
    fun getRestOfEpisodes(@Query("page")page: Int): Call<EpisodeApiResults>
}