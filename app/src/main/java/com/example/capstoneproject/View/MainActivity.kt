package com.example.capstoneproject.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.capstoneproject.Model.DataModel
import com.example.capstoneproject.Service.IMDbMoviesAPI
import com.example.capstoneproject.Service.IMDbMoviesAPIService
import com.example.capstoneproject.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}

