package com.example.capstoneproject.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.adapter.MoviesAdapter
import com.example.capstoneproject.databinding.FragmentFavoriteBinding
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.viewmodel.ListViewModel


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels()


    private lateinit var adapter: MoviesAdapter
    val favoriteMoviesList = ArrayList<DataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter(favoriteMoviesList, viewModel)
        binding.FavRecylerView.adapter = adapter

        observeData()

        binding.backHome.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeData() {
        viewModel.favorites.observe(viewLifecycleOwner, Observer { favorite ->
            favorite?.let {
                adapter.updateData(favorite)

                adapter.onClick = {dataModel->
                    val action =
                        FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(data = dataModel )
                    findNavController().navigate(action)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

