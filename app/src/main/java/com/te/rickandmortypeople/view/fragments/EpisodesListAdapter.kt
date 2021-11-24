package com.te.rickandmortypeople.view.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.te.rickandmortypeople.R
import com.te.rickandmortypeople.databinding.ItemEpisodeBinding
import com.te.rickandmortypeople.model.Episode

class EpisodesListAdapter(private val episodeList: ArrayList<Episode>):
    RecyclerView.Adapter<EpisodesListAdapter.EpisodeViewHolder>() {

    fun updateEpisodeList(newEpisodeList: List<Episode>){
        episodeList.clear()
        val sortedList = newEpisodeList.sortedBy { it.id }
        episodeList.addAll(sortedList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemEpisodeBinding>(
            inflater,
            R.layout.item_episode,
            parent,
            false
        )
        return EpisodeViewHolder(view)
    }
    override fun getItemCount(): Int = episodeList.size

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.view.episode = episodeList[position]
    }

    class EpisodeViewHolder(var view: ItemEpisodeBinding): RecyclerView.ViewHolder(view.root)
}