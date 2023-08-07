package com.sportsapp.activity.main.ui.jams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportsapp.R
import com.sportsapp.databinding.FragmentJamsBinding
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.SportPlace
import com.sportsapp.logic.recycler_view.SportPlaceRecyclerViewAdapter

class JamsFragment : Fragment() {

    private lateinit var jams: RecyclerView

    private var _binding: FragmentJamsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentJamsBinding.inflate(inflater, container, false)
        val root = binding.root

        // Initialise recommendations
        jams = root.findViewById(R.id.jams)
        jams.setHasFixedSize(true)
        jams.layoutManager = LinearLayoutManager(requireContext())

        val adapter = SportPlaceRecyclerViewAdapter(findJams(), requireContext())
        jams.adapter = adapter

        return root
    }

    private fun findJams(): ArrayList<SportPlace> {
        // Find every sportPlace that has a JAM
        return GlobalValues.getJamLogic()!!.getJamSportPlaces()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}