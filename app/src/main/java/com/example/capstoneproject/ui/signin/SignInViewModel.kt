package com.example.capstoneproject.ui.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

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