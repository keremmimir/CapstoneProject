package com.example.capstoneproject.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.capstoneproject.ui.adapter.MoviesAdapter
import com.example.capstoneproject.databinding.FragmentListBinding
import com.example.capstoneproject.model.Type
import com.example.capstoneproject.ui.favorite.FavoriteViewModel
import com.google.firebase.auth.FirebaseAuth

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: MoviesAdapter
    private val args by navArgs<ListFragmentArgs>()
    private lateinit var type: Type

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        type = args.type
        setupViews()
        observeData()
        listViewModel.fetchData(type)
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
                val action = ListFragmentDirections.actionListFragmentToDetailFragment(dataModel)
                findNavController().navigate(action)
            }
        )
        binding.recylerView.adapter = adapter

        binding.backHome.setOnClickListener {
            findNavController().navigateUp()
        }
        searchBox()
    }

    private fun observeData() {
        FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
            favoriteViewModel.loadFavorites(userId)

            favoriteViewModel.favoriteIds.observe(viewLifecycleOwner) { favoriteIds ->
                listViewModel.updateMoviesWithFavorites(favoriteIds)
                listViewModel.updateSeriesWithFavorites(favoriteIds)
            }

            listViewModel.filteredItems.observe(viewLifecycleOwner) { filteredItems ->
                adapter.submitList(filteredItems)
            }

            when (type) {
                Type.MOVIES -> {
                    listViewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
                        adapter.submitList(movies)
                    })
                }

                Type.SERIES -> {
                    listViewModel.series.observe(viewLifecycleOwner, Observer { series ->
                        adapter.submitList(series)
                    })
                }
            }

            with(binding) {
                listViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
                    progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    if (isLoading) {
                        progressBar.playAnimation()
                        recylerView.visibility = View.GONE
                    } else {
                        progressBar.pauseAnimation()
                        recylerView.visibility = View.VISIBLE
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
                    listViewModel.searchItems(it)
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