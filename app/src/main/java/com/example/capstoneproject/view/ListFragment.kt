package com.example.capstoneproject.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
    private val viewModel: ListViewModel by viewModels()
    private lateinit var adapter: MoviesAdapter
    private val args by navArgs<ListFragmentArgs>()
    val dataList = ArrayList<DataModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MoviesAdapter(dataList)
        binding.recylerView.adapter = adapter

        val listName = args.list
        if (listName == "movies"){
            viewModel.getMovies()
        }else{
            viewModel.getSeries()
        }
        observerData()
    }
    fun observerData(){
        viewModel.movies.observe(viewLifecycleOwner, Observer { movies ->
            movies?.let {
                dataList.clear()
                dataList.addAll(it)
                adapter.notifyDataSetChanged()

                adapter.onClick={ dataModel ->
                    val action = ListFragmentDirections.actionListFragmentToDetailFragment(dataModel)
                    findNavController().navigate(action)
                }
            }
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