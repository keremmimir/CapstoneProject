package com.example.capstoneproject.ui.favorite

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.MoviesRepository
import com.example.capstoneproject.data.repository.MoviesRepositoryImpl
import com.example.capstoneproject.model.DataModel
import kotlinx.coroutines.launch

class FavoriteListViewModel :
    ViewModel() {
    private val moviesRepository: MoviesRepository = MoviesRepositoryImpl()
    val favoriteDataModels = MutableLiveData<List<DataModel>>()
    val isLoading = MutableLiveData<Boolean>()
    val filteredItems = MutableLiveData<List<DataModel>>()

    fun loadFavorites(favoriteIds: Set<String>) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val movies = moviesRepository.getMovies()
                val series = moviesRepository.getSeries()
                val allList = movies + series
                val favoriteMovies = allList.filter { it.imdbId in favoriteIds }
                favoriteDataModels.value = favoriteMovies
            } catch (e: Exception) {
                Log.e("FavoriteListViewModel", "Error loading favorites", e)
            } finally {
                isLoading.value = false
            }
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