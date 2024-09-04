package com.example.capstoneproject.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
        }
    }
}