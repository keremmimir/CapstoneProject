package com.example.capstoneproject.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.data.repository.FirebaseFavoriteRepository
import com.example.capstoneproject.data.repository.MoviesRepository
import com.example.capstoneproject.data.repository.MoviesRepositoryImpl
import com.example.capstoneproject.model.Type
import com.example.capstoneproject.network.IMDbMoviesAPI
import com.example.capstoneproject.network.IMDbMoviesAPIService
import com.example.capstoneproject.network.response.toDataModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ListViewModel() : ViewModel() {

    val movies = MutableLiveData<List<DataModel>>()
    val series = MutableLiveData<List<DataModel>>()
    val favorites = MutableLiveData<List<DataModel>>()
    val error = MutableLiveData<String>()
    val moviesRepository: MoviesRepository = MoviesRepositoryImpl()

    private val firestore = FirebaseFavoriteRepository()
    private val auth = FirebaseAuth.getInstance()

    private val moviesList = mutableListOf<DataModel>()
    private val seriesList = mutableListOf<DataModel>()

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
                moviesList.clear()
                moviesList.addAll(moviesRepository.getMovies())
                val user = auth.currentUser
                user?.uid?.let {
                    loadFavorites(it)
                }
                movies.value = moviesList
            }
        }
    }

    fun getSeries() {
        if (series.value.isNullOrEmpty()) {
            viewModelScope.launch {
                seriesList.clear()
                seriesList.addAll(moviesRepository.getSeries())
                val user = auth.currentUser
                user?.uid?.let {
                    loadFavorites(it)
                }
                series.value = seriesList
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
            val combinedList = moviesList + seriesList
            val favoriteList = combinedList.filter { it.imdbId in favoriteIds }
            favorites.value = favoriteList
        } catch (e: Exception) {
            error.value = e.message
        }
    }
}

