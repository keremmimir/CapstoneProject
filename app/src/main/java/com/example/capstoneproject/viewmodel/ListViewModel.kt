package com.example.capstoneproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.repository.FirebaseFavoriteRepository
import com.example.capstoneproject.model.Type
import com.example.capstoneproject.service.IMDbMoviesAPI
import com.example.capstoneproject.service.IMDbMoviesAPIService
import com.example.capstoneproject.service.response.toDataModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ListViewModel() : ViewModel() {
    private val imdbMoviesAPI: IMDbMoviesAPI = IMDbMoviesAPIService.getIMDbAPI()
    val movies = MutableLiveData<List<DataModel>>()
    val series = MutableLiveData<List<DataModel>>()
    val favorites = MutableLiveData<List<DataModel>>()
    val error = MutableLiveData<String>()

    private val firestore = FirebaseFavoriteRepository()
    private val auth = FirebaseAuth.getInstance()

    init {
        auth.addAuthStateListener { auth ->
            val user = auth.currentUser
            user?.uid?.let {
                viewModelScope.launch {
                    loadFavorites(it)
                }
            }
        }
    }

    fun fetchData(type: Type) {
        when (type) {
            Type.MOVIES -> getMovies()
            Type.SERIES -> getSeries()
        }
    }

    fun getMovies() {
        if (movies.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response = imdbMoviesAPI.getMovies()
                    if (response.isSuccessful) {
                        movies.value = response.body()?.map { it.toDataModel() }
                        val user = auth.currentUser
                        user?.uid?.let {
                            loadFavorites(it)
                        }
                    } else {
                        error.value = "Error : ${response.message()}"
                    }
                } catch (e: Exception) {
                    error.value = e.message
                }
            }
        }
    }


    fun getSeries() {
        if (series.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response = imdbMoviesAPI.getSeries()
                    if (response.isSuccessful) {
                        series.value = response.body()?.map { it.toDataModel() }
                        val user = auth.currentUser
                        user?.uid?.let {
                            loadFavorites(it)
                        }
                    } else {
                        error.value = "Error : ${response.message()}"
                    }

                } catch (e: Exception) {
                    error.value = e.message
                }
            }
        }
    }


    fun toggleFavorite(dataModel: DataModel) {
        val user = auth.currentUser
        user?.let {
            viewModelScope.launch {
                try {
                    val isFavorited = dataModel.imdbId?.let { imdbId ->
                        firestore.isFavorite(user.uid, imdbId)
                    } ?: false

                    dataModel.imdbId?.let { imdbId ->
                        if (isFavorited) {
                            firestore.removeFavorite(user.uid, imdbId)
                        } else {
                            firestore.addFavorite(user.uid, imdbId)
                        }
                    }
                    loadFavorites(user.uid)
                } catch (e: Exception) {
                    error.value = e.message
                }
            }
        }
    }

    private suspend fun loadFavorites(userId: String) {
        try {
            val favoriteIds = firestore.getFavoriteIds(userId)
            val combinedList =
                (movies.value ?: emptyList()) + (series.value ?: emptyList())
            val favoriteList = combinedList.filter { it.imdbId in favoriteIds }
            favorites.value = favoriteList
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}

