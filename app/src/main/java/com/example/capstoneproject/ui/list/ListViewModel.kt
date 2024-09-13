package com.example.capstoneproject.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
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
    val filteredItems = MutableLiveData<List<DataModel>?>()
    val error = MutableLiveData<Event<String>>()


    fun fetchData(type: Type) {
        when (type) {
            Type.MOVIES -> getMovies()
            Type.SERIES -> getSeries()
        }
    }

    private fun getMovies() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = moviesRepository.getMovies()
                if (result.isSuccess) {
                    result.getOrNull()?.let { updateMoviesWithFavorites(it) }
                } else {
                    error.value = Event(result.exceptionOrNull()?.message ?: "Error")
                }
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun getSeries() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val result = moviesRepository.getSeries()
                if (result.isSuccess) {
                    result.getOrNull()?.let { updateMoviesWithFavorites(it) }
                } else {
                    error.value = Event(result.exceptionOrNull()?.message ?: "Error")
                }
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
            } finally {
                isLoading.value = false
            }
        }
    }

    private fun updateMoviesWithFavorites(list: List<DataModel>) {
        viewModelScope.launch {
            try {
                val favoriteIds =
                    userId?.let { favoriteRepository.getFavoriteIds(it) } ?: emptyList()
                movies.value = list.map { movie ->
                    movie.copy(isFavorite = favoriteIds.contains(movie.imdbId))
                }
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
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
            try {
                favoriteRepository.addFavorite(userId, imdbId)
                movies.value = movies.value?.map {
                    if (it.imdbId == imdbId) {
                        it.copy(isFavorite = true)
                    } else {
                        it
                    }
                }
            } catch (e: Exception) {
                error.value = Event(e.message ?: "Error")
            }
        }
    }

    private fun removeFavorite(userId: String, imdbId: String) {
        viewModelScope.launch {
            try {
                favoriteRepository.removeFavorite(userId, imdbId)
                movies.value = movies.value?.map {
                    if (it.imdbId == imdbId) {
                        it.copy(isFavorite = false)
                    } else {
                        it
                    }
                }
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


