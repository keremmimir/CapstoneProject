package com.example.capstoneproject.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
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
) : ViewModel() {

    private val userId = authRepository.getCurrentUser()?.uid
    val favoriteDataModels = MutableLiveData<List<DataModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val filteredItems = MutableLiveData<List<DataModel>>()
    val error = MutableLiveData<Event<String>>()

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
                error.value = Event(e.message ?: "Error")
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
            try {
                favoriteRepository.removeFavorite(userId, imdbId)
                val newList = favoriteDataModels.value?.toMutableList()
                newList?.removeIf {
                    it.imdbId == imdbId
                }
                favoriteDataModels.value = newList
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
            }
        }
    }

    private fun addFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.addFavorite(userId, imdbId)
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
            }
        }
    }

    fun toggleFavorite(imdbId: String) {
        userId?.let { id ->
            viewModelScope.launch {
                try {
                    val isFav = isFavorite(id, imdbId)
                    if (isFav) {
                        removeFavorite(id, imdbId)
                    } else {
                        addFavorite(id, imdbId)
                    }
                } catch (e: Exception) {
                    error.value = Event(e.message ?: "Error")
                }
            }
        }
    }

    suspend fun isFavorite(userId: String, imdbId: String): Boolean {
        return try {
            favoriteRepository.isFavorite(userId, imdbId)
        } catch (e: Exception) {
            error.value = Event(e.message ?: "Error")
            false
        }
    }

    fun searchItems(query: String) {
        val currentFavorites = favoriteDataModels.value ?: emptyList()
        val filteredList = currentFavorites.filter {
            it.title?.contains(query, ignoreCase = true) ?: false
        }
        filteredItems.value = filteredList
    }
}