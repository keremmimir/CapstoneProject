package com.example.capstoneproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException


abstract class BaseViewModel : ViewModel() {
    private val _event = MutableLiveData<Event<String>>()
    val event: LiveData<Event<String>> get() = _event

    protected fun <T> handleResult(result: Result<T>, successHandler: (T) -> Unit) {
        if (result.isSuccess) {
            result.getOrNull()?.let { successHandler(it) }
        } else if (result.isFailure) {
            val exception = result.exceptionOrNull() ?: Exception("Bilinmeyen hata")
            val errorMessage = handleException(exception as Exception)
            Log.e("BaseViewModel", "Hata: $errorMessage")
        }
    }

    protected fun handleException(exception: Exception) {
        val errorMessage = when (exception) {
            //is FirebaseException -> handleFirebaseException(exception)
            is HttpException -> handleHttpException(exception)
            is IOException -> "Ağ hatası: ${exception.message}"
            else -> "Beklenmeyen hata: ${exception.message}"
        }
        Log.e("BaseViewModel", errorMessage)
        _event.value = Event(errorMessage)
    }

    private fun handleHttpException(httpException: HttpException): String {
        return try {
            val errorBody = httpException.response()?.errorBody()?.string()
            val apiError = Gson().fromJson(errorBody, ApiError::class.java)
            apiError.message
        } catch (e: Exception) {
            "Bir hata oluştu: ${httpException.message()}"
        }
    }
/*
    private fun handleFirebaseException(exception: FirebaseException): String {
        return when (exception) {
            is FirebaseFirestoreException -> {
                when (exception.code) {
                    FirebaseFirestoreException.Code.PERMISSION_DENIED ->
                        "Erişim reddedildi. Bu işlem için gerekli izinlere sahip değilsiniz."
                    FirebaseFirestoreException.Code.NOT_FOUND ->
                        "Belge bulunamadı. İlgili belge mevcut değil."
                    FirebaseFirestoreException.Code.UNAVAILABLE ->
                        "Sunucu geçici olarak kullanılamıyor. Lütfen daha sonra tekrar deneyin."
                    FirebaseFirestoreException.Code.ABORTED ->
                        "İşlem iptal edildi. Lütfen tekrar deneyin."
                    else ->
                        "Firestore hatası: ${exception.message}"
                }
            }
            is FirebaseAuthException -> {
                when (exception.errorCode) {
                    "ERROR_INVALID_EMAIL" ->
                        "Geçersiz e-posta adresi. Lütfen e-posta adresinizi kontrol edin."
                    "ERROR_WRONG_PASSWORD" ->
                        "Yanlış şifre. Lütfen şifrenizi kontrol edin ve tekrar deneyin."
                    "ERROR_USER_NOT_FOUND" ->
                        "Kullanıcı bulunamadı. Lütfen kayıtlı olup olmadığınızı kontrol edin."
                    "ERROR_EMAIL_ALREADY_IN_USE" ->
                        "Bu e-posta adresi zaten kullanımda. Başka bir e-posta adresi deneyin."
                    "ERROR_WEAK_PASSWORD" ->
                        "Şifreniz çok zayıf. Daha güçlü bir şifre seçin."
                    else ->
                        "Authentication hatası: ${exception.message}"
                }
            }
            else ->
                "Firebase hatası: ${exception.message}"
        }
    }
*/
}