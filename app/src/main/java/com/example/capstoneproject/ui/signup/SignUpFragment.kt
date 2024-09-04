package com.example.capstoneproject.ui.signup

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
import com.example.capstoneproject.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setupViews()
    }

    fun observeData() {
        signUpViewModel.authResult.observe(viewLifecycleOwner, Observer { event ->
            event.getContentIfNotHandled()?.let { result ->
                if (result.isSuccess) {
                    Toast.makeText(requireContext(), result.getOrNull(), Toast.LENGTH_LONG).show()
                    navigateToSignInPage()
                } else {
                    Toast.makeText(
                        requireContext(),
                        result.exceptionOrNull()?.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })

        signUpViewModel.error.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupViews() {
        with(binding) {
            SignUpButton.setOnClickListener {
                val name = signUpName.text.toString()
                val surname = signUpSurname.text.toString()
                val email = signUpEmail.text.toString()
                val password = signUpPassword.text.toString()
                if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.sign_up_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    signUpViewModel.signUp(name, surname, email, password)
                }
            }
        }
    }

    private fun navigateToSignInPage() {
        val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}