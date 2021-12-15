package com.te.rickandmortypeople.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.te.rickandmortypeople.model.Episode
import com.te.rickandmortypeople.model.EpisodeApiResults
import com.te.rickandmortypeople.model.apiService.RMApiService
import com.te.rickandmortypeople.model.episodes_db.EpisodesDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@HiltViewModel
class EpisodesViewModel @Inject constructor(application: Application): AndroidViewModel(application) {

    private val episodesDB by lazy { EpisodesDB(getApplication()).episodesDao() }
    private val apiService = RMApiService().getRMService()
    val episodes by lazy { MutableLiveData<List<Episode>>() }
    private var episodeList: ArrayList<Episode> = arrayListOf()
    private var job: Job? = null
    private var jobFromApi: Job? = null
    private var jobRestFromApi: Job? = null
    var coroutineScopeIntoDB = CoroutineScope(Dispatchers.IO)
    var coroutineScopeIntoDB2 = CoroutineScope(Dispatchers.IO)

    fun fetchDataOnline(){
        getEpisodes()
    }

    fun fetchEpisodes(episodesId: ArrayList<Int>){
        getEpisodesFromDB(episodesId)
    }

    private fun getEpisodes(){
        jobFromApi = CoroutineScope(Dispatchers.IO).launch {
            val call = apiService.getEpisodes()
            call.enqueue(object : Callback<EpisodeApiResults>{
                override fun onResponse(
                    call: Call<EpisodeApiResults>,
                    response: Response<EpisodeApiResults>
                ) {
                    response.body()?.episodeResults?.let {
                        coroutineScopeIntoDB.launch {
                            episodesDB.insertEpisodes(it)
                        }
                    }
                    response.body()?.episodeInfo?.episodePages?.let {
                        getTheRestOfEpisodes(it)
                    }
                }

                override fun onFailure(call: Call<EpisodeApiResults>, t: Throwable) {

                }
            })
        }
    }

    private fun getTheRestOfEpisodes(page:Int){
        if (page >= 2){
            for (x in 1..3){
                jobRestFromApi = CoroutineScope(Dispatchers.IO).launch {
                    val call = apiService.getRestOfEpisodes(x)
                    call.enqueue(object : Callback<EpisodeApiResults> {
                        override fun onResponse(
                            call: Call<EpisodeApiResults>,
                            response: Response<EpisodeApiResults>
                        ) {
                            response.body()?.episodeResults?.let {
                                coroutineScopeIntoDB2.launch {
                                    episodesDB.insertEpisodes(it)
                                }
                            }
                        }

                        override fun onFailure(call: Call<EpisodeApiResults>, t: Throwable) {

                        }
                    })
                }
            }
        }
    }

    private fun getEpisodesFromDB(episodesId: ArrayList<Int>){
        episodesId.forEach {
            job = CoroutineScope(Dispatchers.IO).launch {
                episodeList.add(episodesDB.getSelectedEpisode(it))
                episodes.postValue(episodeList)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        jobFromApi?.cancel()
        jobRestFromApi?.cancel()
        coroutineScopeIntoDB.cancel()
        coroutineScopeIntoDB2.cancel()
    }
}