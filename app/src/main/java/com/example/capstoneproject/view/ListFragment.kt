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
import com.example.capstoneproject.model.DataModel
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

        adapter = MoviesAdapter(arrayListOf(), viewModel)
        binding.recylerView.adapter = adapter

        val listName = args.list

        observeData(listName)

        binding.backHome.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    private fun observeData(listName: String) {
        if (listName == "movies") {
            viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
                adapter.updateData(movies)

                adapter.onClick={
                    val action = ListFragmentDirections.actionListFragmentToDetailFragment(it)
                    findNavController().navigate(action)
                }
            })
        } else if (listName == "series") {
            viewModel.series.observe(viewLifecycleOwner, Observer { series ->
                adapter.updateData(series)

                adapter.onClick={
                    val action = ListFragmentDirections.actionListFragmentToDetailFragment(it)
                    findNavController().navigate(action)
                }
            })
        }
        viewModel.favorites.observe(viewLifecycleOwner, Observer { favorites ->
            adapter.notifyDataSetChanged()
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
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