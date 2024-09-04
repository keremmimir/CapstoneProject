package com.example.capstoneproject.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstoneproject.Event
import com.example.capstoneproject.data.repository.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository
) : ViewModel() {

    val authResult = MutableLiveData<Event<Result<String>?>>()

    fun signUp(name: String, surname: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(name, surname, email, password)
            authResult.value = Event(result)
        }
    }
}