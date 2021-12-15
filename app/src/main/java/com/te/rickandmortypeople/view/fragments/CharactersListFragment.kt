package com.te.rickandmortypeople.view.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.te.rickandmortypeople.databinding.FragmentCharactersListBinding
import com.te.rickandmortypeople.model.Character
import dagger.hilt.android.AndroidEntryPoint
import android.view.*
import android.widget.SearchView
import com.te.rickandmortypeople.R
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.view.MenuInflater
import com.te.rickandmortypeople.view.CharactersListAdapter
import com.te.rickandmortypeople.viewmodel.CharactersListViewModel


@AndroidEntryPoint
class CharactersListFragment : Fragment() {


    private val viewModel: CharactersListViewModel by viewModels()
    private val listAdapter = CharactersListAdapter(arrayListOf())
    private lateinit var binding: FragmentCharactersListBinding

    private val charactersListObserver = Observer<List<Character>> {
        it?.let {
            if (it.isEmpty()) {
                viewModel.fetchDataOnline()
                listAdapter.updateCharactersList(it)
            } else {
                listAdapter.updateCharactersList(it)
            }
        }
    }

    private val namesListObserver = Observer<List<Character>> {
        it?.let {
            listAdapter.updateCharactersList(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchDataOnline()
        viewModel.characters.observe(viewLifecycleOwner, charactersListObserver)
        viewModel.names.observe(viewLifecycleOwner, namesListObserver)

        binding.charactersList.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = listAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)

        val item: MenuItem? = menu.findItem(R.id.search_menu)
        val searchView: SearchView = item!!.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.fetchNames(query)
                    val keyboard =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.hideSoftInputFromWindow(binding.root.windowToken, 0)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    viewModel.fetchNames(newText)
                }
                return true
            }
        })

        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                if (viewModel.charactersDB.size != 0) {
                    listAdapter.updateCharactersList(viewModel.charactersDB)
                } else {
                    viewModel.fetchDataOnline()
                    listAdapter.updateCharactersList(viewModel.charactersDB)
                    val keyboard =
                        activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    keyboard.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    searchView.setQuery("", false)
                }
                return true
            }
        })
    }
}



