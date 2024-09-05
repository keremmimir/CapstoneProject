package com.example.capstoneproject.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
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
                    showSignOutConfirmationDialog()
                    true
                }

                else -> {
                    navController.navigate(item.itemId)
                    true
                }
            }
        }

        observerData()
    }

    private fun showSignOutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.logout_title))
            .setMessage(getString(R.string.logout_message))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                signOut()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun signOut() {
        mainViewModel.signOut()
    }

    private fun observerData() {
        mainViewModel.authResult.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                result.onSuccess { message ->
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    navController.navigate(R.id.signInFragment)
                }.onFailure { exception ->
                    Toast.makeText(this, exception.message ?: "Unknown error", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}


