package com.example.capstoneproject.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.databinding.FragmentInfoBinding
import com.google.firebase.auth.FirebaseAuth

class InfoFragment : Fragment() {

    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            val action = InfoFragmentDirections.actionFirstFragmentToHomeFragment()
            findNavController().navigate(action)
        }
        binding.signInButton.setOnClickListener {
            val action = InfoFragmentDirections.actionFirstFragmentToSignInFragment()
            findNavController().navigate(action)
        }
        binding.createAccountButton.setOnClickListener {
            val action = InfoFragmentDirections.actionFirstFragmentToSignUpFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}