package com.example.capstoneproject.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class SignInViewModel : ViewModel() {
    private val authRepository = FirebaseAuthRepository()

    val authResult = MutableLiveData<Event<Result<String>?>>()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signIn(email, password)
            authResult.value = Event(result)
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return authRepository.getCurrentUser()
    }
}