package com.example.capstoneproject.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.repository.FirebaseFavoriteRepository
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
            user?.uid?.let { loadFavorites(it) }
        }

        getMovies()
        getSeries()
    }

    fun getMovies() {
        if (movies.value.isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val response = imdbMoviesAPI.getMovies()
                    if (response.isSuccessful) {
                        val user = auth.currentUser
                        user?.uid?.let { loadFavorites(it) }
                        movies.value = response.body()?.map { it.toDataModel() }
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
                        val user = auth.currentUser
                        user?.uid?.let { loadFavorites(it) }
                        series.value = response.body()?.map { it.toDataModel() }
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
        user?.let { user ->
            dataModel.imdbId?.let {
                firestore.isFavorite(user.uid, it) { isFavorited ->
                    if (isFavorited) {
                        firestore.removeFavorite(user.uid, dataModel.imdbId)
                    } else {
                        firestore.addFavorite(user.uid, dataModel.imdbId)
                    }
                    loadFavorites(user.uid)
                }
            }
        }
    }

    private fun loadFavorites(userId: String) {
        firestore.getFavoriteIds(userId) { favoriteIds ->
            val combinedList = (movies.value ?: emptyList()) + (series.value ?: emptyList())
            val favoriteList = combinedList.filter { it.imdbId in favoriteIds }
            favorites.value = favoriteList
        }
    }
}