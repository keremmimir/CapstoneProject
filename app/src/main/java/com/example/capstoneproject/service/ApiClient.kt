package com.example.capstoneproject.service

import com.example.capstoneproject.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient

class ApiClient {
    private val client: OkHttpClient

    init {
        val headerInterceptor = Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("x-rapidapi-key", BuildConfig.RAPID_API_KEY)
                .header("x-rapidapi-host", BuildConfig.RAPID_API_HOST)
                .build()
            chain.proceed(request)
        }

        client = OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .build()
    }

    fun getClient(): OkHttpClient {
        return client
    }
}