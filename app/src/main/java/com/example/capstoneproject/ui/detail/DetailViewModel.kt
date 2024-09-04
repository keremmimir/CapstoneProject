package com.example.capstoneproject.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import com.example.capstoneproject.data.repository.FirebaseFavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val favoriteRepository: FirebaseFavoriteRepository,
    authRepository: FirebaseAuthRepository
) : ViewModel() {

    private val userId = authRepository.getCurrentUser()?.uid

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    val error = MutableLiveData<Event<String>>()

    fun setInitialFavorite(isFavorite: Boolean) {
        _isFavorite.postValue(isFavorite)
    }

    private fun addFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.addFavorite(userId, imdbId)
                _isFavorite.value = true
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
            }
        }
    }

    private fun removeFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.removeFavorite(userId, imdbId)
                _isFavorite.value = false
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
}