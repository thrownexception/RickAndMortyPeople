package com.te.rickandmortypeople.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.te.rickandmortypeople.model.ApiResults
import com.te.rickandmortypeople.model.Character
import com.te.rickandmortypeople.model.apiService.RMApiService
import com.te.rickandmortypeople.model.characters_db.CharacterDB
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class CharactersListViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    val characters by lazy { MutableLiveData<List<Character>>() }
    val names by lazy { MutableLiveData<List<Character>>() }
    private val apiService = RMApiService().getRMService()
    var charactersDB: ArrayList<Character> = arrayListOf()
    private var namesDB: ArrayList<Character> = arrayListOf()
    private var jobFromDB: Job? = null
    private var jobFromApi: Job? = null
    var jobRestFromApi: Job? = null
    private var jobCheckDB: Job? = null
    var coroutineScopeIntoDB = CoroutineScope(Dispatchers.IO)
    private var coroutineScopeIntoDB2 = CoroutineScope(Dispatchers.IO)
    private var coroutineScopeNames = CoroutineScope(Dispatchers.IO)
    private val db by lazy { CharacterDB(getApplication()).characterDao() }

    fun fetchDataOnline() {
        getCharacters()
    }

    fun fetchNames(name: String?) {
        getNames(name)
    }

    private fun getCharacters() {
        jobFromApi = CoroutineScope(Dispatchers.IO).launch {
            val call1 = apiService.getCharacters()
            call1.enqueue(object : Callback<ApiResults> {
                override fun onResponse(call: Call<ApiResults>, response: Response<ApiResults>) {
                    response.body()?.results?.let {
                        coroutineScopeIntoDB.launch {
                            db.insertCharacters(it)
                        }
                    }
                    response.body()?.info?.pages?.let { getTheRest(it) }
                }

                override fun onFailure(call: Call<ApiResults>, t: Throwable) {
                }
            })
        }
    }

    private fun getTheRest(page: Int) {
        if (page >= 2) {
            var endRange = page + 1
            for (x in 2..endRange) {
                coroutineScopeIntoDB2.launch {
                    val call2 = apiService.getRestOfCharacters(x)
                    call2.enqueue(object : Callback<ApiResults> {
                        override fun onResponse(
                            call: Call<ApiResults>,
                            response: Response<ApiResults>
                        ) {
                            response.body()?.results?.let {
                                jobRestFromApi = CoroutineScope(Dispatchers.IO).launch {
                                    db.insertCharacters(it)
                                }
                            }
                        }

                        override fun onFailure(call: Call<ApiResults>, t: Throwable) {
                        }
                    })
                }
            }
        }
        getCharactersFromDatabase()
    }

    private fun getCharactersFromDatabase() {
        jobFromDB = CoroutineScope(Dispatchers.Default).launch {
            charactersDB.addAll(db.getAllCharacters())
            characters.postValue(db.getAllCharacters())
        }
    }

    private fun getNames(name: String?) {
        coroutineScopeNames.launch {
            namesDB.clear()
            var newName = name?.lowercase()
            var newName2 = (name?.get(0)?.uppercase() + newName?.substring(1))
            if (charactersDB.size == 0) {
                getCharactersFromDatabase()
            }
            for (character in charactersDB) {
                if (name != null) {
                    if (character.name?.length!! >= newName2.length) {
                        if (character.name?.subSequence(0, newName2.length)
                                ?.equals(newName2) == true
                        ) {
                            namesDB.add(character)
                        }
                    }
                }
            }
            names.postValue(namesDB)
        }
    }

    override fun onCleared() {
        super.onCleared()
        jobFromApi?.cancel()
        jobRestFromApi?.cancel()
        jobFromDB?.cancel()
        jobCheckDB?.cancel()
        coroutineScopeIntoDB.cancel()
        coroutineScopeIntoDB2.cancel()
        coroutineScopeNames.cancel()
    }
}