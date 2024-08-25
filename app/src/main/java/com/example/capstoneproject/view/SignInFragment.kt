package com.example.capstoneproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentSignInBinding
import com.example.capstoneproject.viewmodel.AuthViewModel


class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setupViews()
    }

    fun observeData() {
        authViewModel.authResult.observe(viewLifecycleOwner, Observer { result ->
            result.let {
                if (it != null) {
                    if (it.isSuccess) {
                        Toast.makeText(requireContext(), it.getOrNull(), Toast.LENGTH_LONG).show()
                        navigateToHomePage()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            it.exceptionOrNull()?.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }

    private fun setupViews(){
        with(binding) {
            SignInButton.setOnClickListener {
                val email = signInEmail.text.toString()
                val password = signInPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "Email and Password cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    authViewModel.signIn(email, password)
                }
            }
            createAccount.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
        val currentUser = authViewModel.getCurrentUser()
        if (currentUser != null){
            navigateToHomePage()
        }
    }

    private fun navigateToHomePage() {
        val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}