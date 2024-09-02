package com.example.capstoneproject.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.FirebaseFavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel :
    ViewModel() {

    private val favoriteRepository = FirebaseFavoriteRepository()
    val favoriteIds = MutableLiveData<Set<String>>()

    fun loadFavorites(userId: String) {
        viewModelScope.launch {
            val favoriteId = favoriteRepository.getFavoriteIds(userId)
            favoriteIds.value = favoriteId
        }
    }

    private fun addFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            favoriteRepository.addFavorite(userId, imdbId)
            loadFavorites(userId)
        }
    }

    private fun removeFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            favoriteRepository.removeFavorite(userId, imdbId)
            loadFavorites(userId)
        }
    }

    fun toggleFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            val isFav = isFavorite(userId, imdbId)
            if (isFav) {
                removeFavorite(userId, imdbId)
            } else {
                addFavorite(userId, imdbId)
            }
        }
    }

    suspend fun isFavorite(userId: String, imdbId: String): Boolean {
        return favoriteRepository.isFavorite(userId, imdbId)
    }
}