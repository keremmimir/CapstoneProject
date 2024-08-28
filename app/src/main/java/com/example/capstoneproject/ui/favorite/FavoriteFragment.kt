package com.example.capstoneproject.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.ui.adapter.MoviesAdapter
import com.example.capstoneproject.databinding.FragmentFavoriteBinding
import com.example.capstoneproject.ui.list.ListViewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels()


    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setupViews()
    }

    private fun observeData() {
        viewModel.favorites.observe(viewLifecycleOwner, Observer { favorite ->
            favorite?.let {
                adapter.submitList(favorite)
            }
        })
    }

    private fun setupViews(){
        adapter = MoviesAdapter(viewModel)
        binding.FavRecylerView.adapter = adapter

        binding.backHome.setOnClickListener {
            findNavController().navigateUp()
        }

        adapter.onClick = {dataModel->
            val action =
                FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(data = dataModel)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

