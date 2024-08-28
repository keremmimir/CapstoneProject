package com.example.capstoneproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.capstoneproject.R
import com.example.capstoneproject.databinding.ListItemBinding
import com.example.capstoneproject.model.DataModel
import com.example.capstoneproject.ui.list.ListViewModel

class MoviesAdapter(private val viewModel: ListViewModel) : ListAdapter<DataModel, MoviesAdapter.Holder>(
    DiffCallback()
) {

    var onClick: ((DataModel) -> Unit)? = null

    inner class Holder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: DataModel) {
            with(binding) {
                title.text = dataModel.title

                val genreList = dataModel.genre
                if (genreList != null) {
                    val joinGenre = genreList.joinToString(separator = ", ")
                    genre.text = joinGenre
                }
                year.text = dataModel.year
                rating.text = dataModel.rating

                Glide.with(itemView.context)
                    .load(dataModel.image)
                    .transform(RoundedCorners(30))
                    .override(300, 300)
                    .into(image)

                updateFavButton(dataModel.imdbId)

                favButton.setOnClickListener {
                    viewModel.toggleFavorite(dataModel)
                    notifyItemChanged(adapterPosition)
                }
            }

            itemView.setOnClickListener {
                onClick?.invoke(dataModel)
            }
        }

        private fun updateFavButton(itemId: String?) {
            itemId?.let {
                val isFavorited = viewModel.favorites.value?.any { it.imdbId == itemId } ?: false
                binding.favButton.setBackgroundResource(
                    if (isFavorited) R.drawable.popcorn else R.drawable.popcorn_border
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    // DiffUtil ItemCallback
    class DiffCallback : DiffUtil.ItemCallback<DataModel>() {
        override fun areItemsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
            return oldItem.imdbId == newItem.imdbId
        }

        override fun areContentsTheSame(oldItem: DataModel, newItem: DataModel): Boolean {
            return oldItem == newItem
        }
    }
}

