package com.te.rickandmortypeople.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.te.rickandmortypeople.R
import com.te.rickandmortypeople.databinding.FragmentEpisodesListBinding
import com.te.rickandmortypeople.model.Episode
import com.te.rickandmortypeople.viewmodel.EpisodesViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.te.rickandmortypeople.model.Character
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EpisodesListFragment : Fragment() {


    private val viewModel: EpisodesViewModel by viewModels()
    private val listAdapter = EpisodesListAdapter(arrayListOf())
    private lateinit var binding: FragmentEpisodesListBinding
    private var episodesRetrieved: Array<String> = arrayOf()
    private var episodes: ArrayList<String> = arrayListOf()
    private var episodesToSend: ArrayList<Int> = arrayListOf()
    private var savedCharacter: Character? = null

    private val episodesListObserver = Observer<List<Episode>> {
        it?.let {
            binding.episodeList.visibility = View.VISIBLE
            listAdapter.updateEpisodeList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_episodes_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchDataOnline()
        viewModel.episodes.observe(viewLifecycleOwner, episodesListObserver)

        arguments?.let {
            episodesRetrieved = EpisodesListFragmentArgs.fromBundle(it).episodes
            episodesRetrieved.toCollection(episodes)
            savedCharacter = EpisodesListFragmentArgs.fromBundle(it).character
        }
        episodes.forEach {
            val newInt = it.toInt()
            episodesToSend.add(newInt)
        }
        viewModel.fetchEpisodes(episodesToSend)

        binding.episodeList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }
}