package com.example.capstoneproject.ui.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import com.example.capstoneproject.data.repository.FirebaseFavoriteRepository
import com.example.capstoneproject.data.repository.MoviesRepositoryImpl
import com.example.capstoneproject.model.DataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val moviesRepository: MoviesRepositoryImpl,
    private val favoriteRepository: FirebaseFavoriteRepository,
    authRepository: FirebaseAuthRepository
) :
    ViewModel() {
    private val userId = authRepository.getCurrentUser()?.uid
    val favoriteDataModels = MutableLiveData<List<DataModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val filteredItems = MutableLiveData<List<DataModel>>()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val moviesResult = moviesRepository.getMovies()
                val seriesResult = moviesRepository.getSeries()

                val movies = moviesResult.getOrNull() ?: emptyList()
                val series = seriesResult.getOrNull() ?: emptyList()
                val allList = movies + series

                val favoriteIds =
                    userId?.let { favoriteRepository.getFavoriteIds(it) } ?: emptyList()
                val favoriteMovies =
                    allList.filter { it.imdbId in favoriteIds }.map { it.copy(isFavorite = true) }
                favoriteDataModels.value = favoriteMovies
            } catch (e: Exception) {
                Log.e("FavoriteListViewModel", "Error loading favorites", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    fun updateFavoriteStatus(imdbId: String, isFavorite: Boolean) {
        val currentFavorites = favoriteDataModels.value ?: emptyList()
        if (!isFavorite) {
            val updatedList = currentFavorites.map { item ->
                if (item.imdbId == imdbId) {
                    item.copy(isFavorite = false)
                } else {
                    item
                }
            }
            val filteredList = updatedList.filter { item ->
                item.isFavorite
            }
            favoriteDataModels.postValue(filteredList)
        }
    }

    private fun removeFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(userId, imdbId)
            val newList = favoriteDataModels.value?.toMutableList()
            newList?.removeIf {
                it.imdbId == imdbId
            }
            favoriteDataModels.value = newList
        }
    }

    private fun addFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            favoriteRepository.addFavorite(userId, imdbId)
        }
    }

    fun toggleFavorite(imdbId: String) {
        userId?.let { id ->
            viewModelScope.launch {
                val isFav = isFavorite(id, imdbId)
                if (isFav) {
                    removeFavorite(id, imdbId)
                } else {
                    addFavorite(id, imdbId)
                }
            }
        }
    }

    suspend fun isFavorite(userId: String, imdbId: String): Boolean {
        return favoriteRepository.isFavorite(userId, imdbId)
    }

    fun searchItems(query: String) {
        val currentFavorites = favoriteDataModels.value ?: emptyList()
        val filteredList = currentFavorites.filter {
            it.title?.contains(query, ignoreCase = true) ?: false
        }
        filteredItems.value = filteredList
    }
}