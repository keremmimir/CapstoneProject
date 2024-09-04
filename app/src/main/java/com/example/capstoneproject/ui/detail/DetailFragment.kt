package com.example.capstoneproject.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.capstoneproject.R
import com.example.capstoneproject.constants.Constants
import com.example.capstoneproject.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        observerData()
    }

    private fun setupViews() {
        with(binding) {
            view?.let {
                Glide.with(it.context).apply {
                    load(args.data.bigImage).into(imageBig)
                    load(args.data.image).override(500, 500).into(image)
                }
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

            favButton.setOnClickListener {
                args.data.imdbId?.let { imdbId ->
                    detailViewModel.toggleFavorite(imdbId)
                    setFragmentResult(Constants.SET_FRAGMENT_RESULT, Bundle().apply {
                        putString(Constants.FRAGMENT_RESULT_IMDB_ID, imdbId)
                        putBoolean(Constants.FRAGMENT_RESULT_IS_FAVORITE, !args.data.isFavorite)
                    })
                }
            }

            backHome.setOnClickListener {
                findNavController().navigateUp()
            }

            imdbLink.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(args.data.imdbLink)))
            }
        }
        detailViewModel.setInitialFavorite(args.data.isFavorite)
    }

    private fun observerData() {
        detailViewModel.isFavorite.observe(viewLifecycleOwner) {
            updateFavButton(it)
        }
    }

    private fun updateFavButton(isFavorited: Boolean) {
        binding.favButton.setBackgroundResource(
            if (isFavorited) R.drawable.popcorn else R.drawable.popcorn_border
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}