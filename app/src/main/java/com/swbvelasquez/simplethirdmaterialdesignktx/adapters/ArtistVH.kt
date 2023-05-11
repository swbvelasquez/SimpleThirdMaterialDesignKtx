package com.swbvelasquez.simplethirdmaterialdesignktx.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.swbvelasquez.simplethirdmaterialdesignktx.R
import com.swbvelasquez.simplethirdmaterialdesignktx.databinding.ItemArtistBinding
import com.swbvelasquez.simplethirdmaterialdesignktx.entities.Artist

class ArtistVH(view:View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemArtistBinding.bind(view)

    fun bind(artist:Artist, onClickListener:(Artist)->Unit, onLongClickListener:(Artist)->Unit){
        binding.apply {
            tvName.text = artist.fullName
            tvOrder.text =  artist.order.toString()
            tvNote.text = artist.notes

            if(artist.photoUrl.isNotEmpty()) {
                Glide
                    .with(itemView.context)
                    .load(artist.photoUrl)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_sentiment_satisfied)
                    .into(imvPhoto)
            }else{
                imvPhoto.setImageResource(R.drawable.ic_account_box)
            }
        }

        itemView.setOnClickListener {
            onClickListener(artist)
        }
        itemView.setOnLongClickListener {
            onLongClickListener(artist)
            true
        }
    }
}