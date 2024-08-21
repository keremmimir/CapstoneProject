package com.example.capstoneproject.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.capstoneproject.viewmodel.ListViewModel
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.adapter.MoviesAdapter
import com.example.capstoneproject.databinding.FragmentListBinding
import com.example.capstoneproject.repository.SharedPreferencesRepository
import com.example.capstoneproject.viewmodel.ListViewModelFactory


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListViewModel by activityViewModels {
        ListViewModelFactory(SharedPreferencesRepository(requireContext()))
    }
    private lateinit var adapter: MoviesAdapter
    private val args by navArgs<ListFragmentArgs>()
    val dataList = ArrayList<DataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = args.type
        viewModel.fetchData(type)

        observeData()
        setupViews()
    }

    private fun observeData() {
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            adapter.updateData(movies)
        })

        viewModel.series.observe(viewLifecycleOwner, Observer { series ->
            adapter.updateData(series)
        })

        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Log.e("ListFragment", "Error: $it")
            }
        })
    }
    private fun setupViews(){
        adapter = MoviesAdapter(dataList, viewModel)
        binding.recylerView.adapter = adapter

        adapter.onClick = { data ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(data)
            findNavController().navigate(action)
        }
        adapter.onClick = { data ->
            val action = ListFragmentDirections.actionListFragmentToDetailFragment(data)
            findNavController().navigate(action)
        }
        binding.backHome.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}