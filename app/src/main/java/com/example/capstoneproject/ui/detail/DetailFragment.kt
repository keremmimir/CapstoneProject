package com.example.capstoneproject.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.FragmentDetailBinding
import com.example.capstoneproject.ui.list.ListViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailFragmentArgs>()
    private val viewModel: ListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            Glide.with(view.context).apply {
                load(args.data.bigImage).into(imageBig)
                load(args.data.image).override(500, 500).into(image)
            }
            title.text = args.data.title
            val genreList = args.data.genre
            if (genreList != null) {
                val joinGenre = genreList.joinToString(separator = ", ")
                genre.text = joinGenre
            }
            year.text = args.data.year
            rating.text = args.data.rating
            description.text = args.data.description

            updateFavButton(args.data.imdbId)

            favButton.setOnClickListener {
                viewModel.toggleFavorite(args.data)

            }

            backHome.setOnClickListener {
                findNavController().navigateUp()
            }

            imdbLink.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(args.data.imdbLink)))
            }

            observerData()
        }
    }

    fun observerData(){
        viewModel.favorites.observe(viewLifecycleOwner, Observer { favorites ->
            updateFavButton(args.data.imdbId)
        })
    }

    private fun updateFavButton(itemId: String?) {
        itemId?.let {
            val isFavorited = viewModel.favorites.value?.any { it.imdbId == itemId } ?: false
            binding.favButton.setBackgroundResource(
                if (isFavorited) R.drawable.popcorn else R.drawable.popcorn_border
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}