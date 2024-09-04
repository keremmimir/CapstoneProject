package com.example.capstoneproject.ui.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import com.example.capstoneproject.data.repository.FirebaseFavoriteRepository
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.data.repository.MoviesRepositoryImpl
import com.example.capstoneproject.model.Type
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl,
    private val favoriteRepository: FirebaseFavoriteRepository,
    authRepository: FirebaseAuthRepository
) :
    ViewModel() {

    private val userId = authRepository.getCurrentUser()?.uid
    val movies = MutableLiveData<List<DataModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val filteredItems = MutableLiveData<List<DataModel>>()


    fun fetchData(type: Type) {
        when (type) {
            Type.MOVIES -> getMovies()
            Type.SERIES -> getSeries()
        }
    }

    private fun getMovies() {
        isLoading.value = true
        viewModelScope.launch {
            val result = moviesRepository.getMovies()
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let { updateMoviesWithFavorites(it) }
                }

                result.isFailure -> {
                    Log.e("getMovies", "Error ${result.getOrNull()}")
                }
            }
            isLoading.value = false
        }
    }

    private fun getSeries() {
        isLoading.value = true
        viewModelScope.launch {
            val result = moviesRepository.getSeries()
            when {
                result.isSuccess -> {
                    result.getOrNull()?.let { updateMoviesWithFavorites(it) }
                }

                result.isFailure -> {
                    Log.e("getMovies", "Error ${result.getOrNull()}")
                }
            }
            isLoading.value = false
        }
    }

    private fun updateMoviesWithFavorites(list: List<DataModel>) {
        viewModelScope.launch {
            val favoriteIds = userId?.let { favoriteRepository.getFavoriteIds(it) } ?: emptyList()
            movies.value = list.map { movie ->
                movie.copy(isFavorite = favoriteIds.contains(movie.imdbId))
            }
        }
    }

    fun searchItems(query: String) {
        val filteredList = movies.value?.filter {
            it.title?.contains(query, ignoreCase = true) ?: false
        }
        filteredItems.value = filteredList
    }

    private fun addFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            favoriteRepository.addFavorite(userId, imdbId)
            movies.value = movies.value?.map {
                if (it.imdbId == imdbId) {
                    it.copy(isFavorite = true)
                } else {
                    it
                }
            }
        }
    }

    private fun removeFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(userId, imdbId)
            movies.value = movies.value?.map {
                if (it.imdbId == imdbId) {
                    it.copy(isFavorite = false)
                } else {
                    it
                }
            }
        }
    }

    fun toggleFavorite(imdbId: String) {
        userId?.let { id ->
            viewModelScope.launch {
                val isFav = isFavorite(userId, imdbId)
                if (isFav) {
                    removeFavorite(id, imdbId)
                } else {
                    addFavorite(userId, imdbId)
                }
            }
        }
    }

    suspend fun isFavorite(userId: String, imdbId: String): Boolean {
        return favoriteRepository.isFavorite(userId, imdbId)
    }
}

