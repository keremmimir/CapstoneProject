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
import androidx.navigation.fragment.navArgs
import com.example.capstoneproject.viewmodel.ListViewModel
import com.example.capstoneproject.adapter.MoviesAdapter
import com.example.capstoneproject.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels()

    private lateinit var adapter: MoviesAdapter
    private val args by navArgs<ListFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter(viewModel)
        val type = args.type
        viewModel.fetchData(type)

        setupViews()
        observeData()
    }

    private fun setupViews() {
        adapter = MoviesAdapter(viewModel)
        binding.recylerView.adapter = adapter

        adapter.onClick = { data ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(data)
            findNavController().navigate(action)
        }

        binding.backHome.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeData() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            adapter.submitList(movies)
        })

        viewModel.movies.observe(viewLifecycleOwner, Observer { series ->
            adapter.submitList(series)
        })

        viewModel.movies.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Log.e("ListFragment", "Error: $it")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}