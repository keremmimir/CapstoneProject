package com.example.capstoneproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.capstoneproject.databinding.ListItemBinding
import com.example.capstoneproject.model.DataModel

class MoviesAdapter(val dataList : ArrayList<DataModel>) : RecyclerView.Adapter<MoviesAdapter.Holder>(){

    var onClick:((DataModel)-> Unit)? = null

    inner class Holder (val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(dataModel: DataModel) {
            with(binding) {
                title.text = dataModel.title

                val genreList = dataModel.genre
                if (genreList != null){
                    val joinGenre = genreList.joinToString (separator = ", ")
                    genre.text = joinGenre
                }
                year.text = dataModel.year
                rating.text = dataModel.rating

                Glide.with(itemView.context)
                    .load(dataModel.image)
                    .transform(RoundedCorners(30))
                    .override(400,400)
                    .into(image)
            }

            itemView.setOnClickListener {
                onClick?.invoke(dataModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
