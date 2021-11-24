package com.te.rickandmortypeople.model.apiService

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RMApiService {

    private val BASEURL = "https://rickandmortyapi.com/"

    fun getRMService(): RMApi{
        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(RMApi::class.java)
    }
}