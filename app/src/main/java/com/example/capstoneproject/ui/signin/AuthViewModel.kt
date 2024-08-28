package com.example.capstoneproject.ui.signin

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val authRepository = FirebaseAuthRepository()

    val authResult = MutableLiveData<Result<String>?>()

    fun signUp(name: String, surname: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(name,surname,email,password)
            authResult.value = result
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            authResult.value = result
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }
}