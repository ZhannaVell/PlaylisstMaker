package com.example.playlisstmaker.search.ui.adapter

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlisstmaker.R
import com.example.playlisstmaker.databinding.SearchTrackBinding
import com.example.playlisstmaker.search.domain.model.Track
import com.example.playlisstmaker.utils.ImageUrlHelper

class TrackViewHolder(private val binding: SearchTrackBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(track: Track) {

        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = track.trackTime

        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.spacing_s)

        Glide.with(itemView.context)
            .load(ImageUrlHelper.getCoverArtwork(track.artworkUrl100))
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .centerCrop()
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivTrackCover)
    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SearchTrackBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }


}