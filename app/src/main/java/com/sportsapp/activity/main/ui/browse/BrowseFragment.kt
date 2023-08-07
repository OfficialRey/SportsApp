package com.sportsapp.activity.main.ui.browse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportsapp.R
import com.sportsapp.databinding.FragmentBrowseBinding
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.SportPlace
import com.sportsapp.logic.recycler_view.SportPlaceRecyclerViewAdapter

class BrowseFragment : Fragment() {

    private var _binding: FragmentBrowseBinding? = null

    private lateinit var searchResults: RecyclerView
    private lateinit var searchBar: SearchView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrowseBinding.inflate(inflater, container, false)

        val root = binding.root

        searchResults = root.findViewById(R.id.searchResults)
        searchBar = root.findViewById(R.id.searchSpot)

        searchBar.clearFocus()
        searchBar.setOnQueryTextListener(TextQuery())

        searchResults.setHasFixedSize(true)
        searchResults.layoutManager = LinearLayoutManager(requireContext())

        return root
    }

    fun applyFilter(filter: String) {
        val places = GlobalValues.getSportPlaceLogic()!!.querySportPlaces(filter)

        val toRemove = ArrayList<SportPlace>()

        for (place in places) {
            val jam = GlobalValues.getJamLogic()!!.getJam(place.id!!)
            if (jam != null) {
                if (GlobalValues.getJamLogic()?.isFull(jam)!!) {
                    toRemove.add(place)
                }
            }
        }

        places.removeAll(toRemove.toSet())

        val adapter = SportPlaceRecyclerViewAdapter(places, requireContext())
        searchResults.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class TextQuery : OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return onQueryTextChange(query)
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null) {
                if (newText.isNotBlank()) {
                    applyFilter(newText)
                    return true
                }
            }
            return false
        }
    }
}