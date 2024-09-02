package com.example.capstoneproject.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.capstoneproject.ui.adapter.MoviesAdapter
import com.example.capstoneproject.databinding.FragmentFavoriteBinding
import com.google.firebase.auth.FirebaseAuth

class FavoriteListFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MoviesAdapter
    private val favoriteListViewModel: FavoriteListViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

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

    private fun setupViews() {
        adapter = MoviesAdapter(
            onFavoriteToggle = { dataModel ->
                FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
                    dataModel.imdbId?.let { imdbId ->
                        favoriteViewModel.toggleFavorite(
                            userId,
                            imdbId
                        )
                    }
                }
            },
            isFavorite = { imdbId ->
                favoriteViewModel.favoriteIds.value?.contains(imdbId) ?: false
            },
            onDetailToggle = { dataModel ->
                val action =
                    FavoriteListFragmentDirections.actionFavoriteFragmentToDetailFragment(dataModel)
                findNavController().navigate(action)
            }
        )

        binding.FavRecylerView.adapter = adapter

        binding.backHome.setOnClickListener {
            findNavController().navigateUp()
        }
        searchBox()
    }

    private fun observeData() {
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            favoriteViewModel.loadFavorites(userId)

            favoriteViewModel.favoriteIds.observe(viewLifecycleOwner) { favoriteIds ->
                favoriteListViewModel.loadFavorites(favoriteIds)
            }

            favoriteListViewModel.favoriteDataModels.observe(viewLifecycleOwner) { favoriteMovies ->
                adapter.submitList(favoriteMovies)
            }

            favoriteListViewModel.filteredItems.observe(viewLifecycleOwner) { filteredItems ->
                adapter.submitList(filteredItems)
            }

            with(binding) {
                favoriteListViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    if (isLoading) {
                        progressBar.playAnimation()
                        FavRecylerView.visibility = View.GONE
                    } else {
                        progressBar.pauseAnimation()
                        FavRecylerView.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun searchBox() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    favoriteListViewModel.searchItems(it)
                }
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

