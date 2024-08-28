package com.example.capstoneproject.ui.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.databinding.FragmentSignInBinding
import com.example.capstoneproject.R

class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val signInViewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setupViews()
    }

    private fun observeData() {
        signInViewModel.authResult.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { result ->
                if (result.isSuccess) {
                    Toast.makeText(requireContext(), result.getOrNull(), Toast.LENGTH_LONG).show()
                    navigateToHomePage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        result.exceptionOrNull()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun setupViews() {
        with(binding) {
            signInButton.setOnClickListener {
                val email = signInEmail.text.toString()
                val password = signInPassword.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sign_in_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    signInViewModel.signIn(email, password)
                }
            }
            createAccount.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
        }
        val currentUser = signInViewModel.getCurrentUser()
        if (currentUser != null) {
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