package com.te.rickandmortypeople.view.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.te.rickandmortypeople.R
import com.te.rickandmortypeople.databinding.FragmentCharacterDetailsBinding
import com.te.rickandmortypeople.model.Character
import com.te.rickandmortypeople.model.CharacterPalette

class CharacterDetailsFragment : Fragment() {

    var character: Character? = null
    private lateinit var dataBinding: FragmentCharacterDetailsBinding
    private var episodes: Array<String> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_character_details, container, false)
        setHasOptionsMenu(true)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            character = CharacterDetailsFragmentArgs.fromBundle(it).character
        }

        character?.image?.let {
            setUpBackgroundColour(it)
        }

        character?.episode?.let { getEpisodes(it) }


        dataBinding.episodesButton.setOnClickListener{

            val action = CharacterDetailsFragmentDirections.actionDetailsToEpisodes(episodes, character)
            Navigation.findNavController(it).navigate(action)
        }
        dataBinding.character = character
    }

    private fun setUpBackgroundColour(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColour = palette?.lightMutedSwatch?.rgb ?: 0
                            dataBinding.palette = CharacterPalette(intColour)
                        }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }

    private fun getEpisodes(urls: List<String>){

        var episodeNum: String
        var episodeList: ArrayList<String> = arrayListOf()

        for (value in urls){
            episodeNum = value.substringAfter("https://rickandmortyapi.com/api/episode/")
            episodeList.add(episodeNum)
        }
        episodes = episodeList.toTypedArray()
    }
}