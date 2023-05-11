package com.swbvelasquez.simplethirdmaterialdesignktx.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.swbvelasquez.simplethirdmaterialdesignktx.R
import com.swbvelasquez.simplethirdmaterialdesignktx.entities.Artist

class ArtistAdapter(private var artistList : MutableList<Artist>,
                    private val onClickListener:(Artist)->Unit,
                    private val onLongClickListener:(Artist)->Unit):RecyclerView.Adapter<ArtistVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistVH {
        return ArtistVH(LayoutInflater.from(parent.context).inflate(R.layout.item_artist,parent,false))
    }

    override fun onBindViewHolder(holder: ArtistVH, position: Int) {
        holder.bind(artistList[position].apply { order = position +1 },onClickListener,onLongClickListener)
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    fun setList(list: MutableList<Artist>) {
        artistList = list
        notifyDataSetChanged()
    }

    fun remove(artist: Artist) {
        if (artistList.contains(artist)) {
            artistList.remove(artist)
            notifyDataSetChanged()
        }
    }
}