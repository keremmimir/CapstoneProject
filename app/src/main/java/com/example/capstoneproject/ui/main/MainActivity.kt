package com.example.capstoneproject.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController


        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signUpFragment, R.id.signInFragment, R.id.firstFragment -> binding.bottomNavigation.visibility =
                    View.GONE

                else -> binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.signInFragment -> {
                    signOut()
                    true
                }

                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }
    }

    private fun signOut() {
        mainViewModel.signOut()
        Toast.makeText(this, "Logout successful", Toast.LENGTH_LONG).show()
        navController.navigate(R.id.signInFragment)
    }
}

