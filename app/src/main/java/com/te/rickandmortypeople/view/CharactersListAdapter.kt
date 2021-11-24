package com.te.rickandmortypeople.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.te.rickandmortypeople.R
import com.te.rickandmortypeople.databinding.ItemCharacterBinding
import com.te.rickandmortypeople.model.Character
import com.te.rickandmortypeople.util.CharacterClickListener

class CharactersListAdapter(private val charactersList: ArrayList<Character>):
        RecyclerView.Adapter<CharactersListAdapter.CharacterViewHolder>(),
        CharacterClickListener{

    private var listState: String? = null

    fun updateCharactersList(newCharactersList: List<Character>){
        charactersList.clear()
        charactersList.addAll(newCharactersList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemCharacterBinding>(
            inflater,
            R.layout.item_character,
            parent,
            false
        )
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.view.character = charactersList[position]
        holder.view.listener = this
    }

    override fun getItemCount(): Int = charactersList.size

    override fun onClick(v: View) {
        for (character in charactersList){
            if (v.tag == character.id){
                if (Navigation.findNavController(v).currentDestination?.id == R.id.charactersListFragment){
                    val action = CharactersListFragmentDirections.actionListToDetails(listState, character)
                    Navigation.findNavController(v).navigate(action)
                }
            }
        }
    }

    class CharacterViewHolder(var view: ItemCharacterBinding): RecyclerView.ViewHolder(view.root)
}