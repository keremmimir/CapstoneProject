package com.example.capstoneproject.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {

    private val authRepository = FirebaseAuthRepository()
    val authResult = MutableLiveData<Event<Result<String>?>>()

    fun signUp(name: String, surname: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(name,surname,email,password)
            authResult.value = Event(result)
        }
    }
}