package com.example.capstoneproject.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.databinding.FragmentHomeBinding
import com.example.capstoneproject.model.Type
import com.example.capstoneproject.repository.FirebaseAuthRepository
import com.example.capstoneproject.viewmodel.ListViewModel
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMovies()
        viewModel.getSeries()

        with(binding) {
            cardViewMovies.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(Type.MOVIES)
                findNavController().navigate(action)
            }
            cardViewSeries.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToListFragment(Type.SERIES)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}