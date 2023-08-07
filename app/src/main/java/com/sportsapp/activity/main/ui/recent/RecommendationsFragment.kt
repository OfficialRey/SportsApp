package com.sportsapp.activity.main.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportsapp.R
import com.sportsapp.logic.recycler_view.SportPlaceRecyclerViewAdapter
import com.sportsapp.databinding.FragmentRecommendationsBinding
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.SportPlaceLogic
import com.sportsapp.logic.models.SportPlace
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class RecommendationsFragment : Fragment() {

    private lateinit var recommendations: RecyclerView

    private var _binding: FragmentRecommendationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRecommendationsBinding.inflate(inflater, container, false)
        val root = binding.root

        // Initialise recommendations
        recommendations = root.findViewById(R.id.jams)
        recommendations.setHasFixedSize(true)
        recommendations.layoutManager = LinearLayoutManager(requireContext())

        val adapter = SportPlaceRecyclerViewAdapter(findRecommendations(), requireContext())
        recommendations.adapter = adapter

        return root
    }

    private fun findRecommendations(): ArrayList<SportPlace> {
        // Here is an algorithm to find recommendations
        // For simplicity we now only care about the date and get the most recent spots
        val recommendations = ArrayList<SportPlace>()
        val places = GlobalValues.getSportPlaceLogic()!!.getAllSportPlaces()
        val currentTime = LocalDateTime.now()
        for (i in 0 until places.size) {
            if (ChronoUnit.DAYS.between(
                    places[i].creationDate,
                    currentTime
                ) < RECOMMENDATION_DIFFERENCE_DAYS
            ) {
                val jam = GlobalValues.getJamLogic()!!.getJam(places[i].id!!)
                if (jam != null) {
                    if (GlobalValues.getJamLogic()?.isFull(jam)!!) {
                        continue
                    }
                }
                if (!SportPlaceLogic.containsPlace(recommendations, places[i])) {
                    recommendations.add(places[i])
                    if (recommendations.size > 5) {
                        // Do not recommend too much
                        return recommendations
                    }
                }
            }
        }
        return recommendations
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RECOMMENDATION_DIFFERENCE_DAYS = 7
    }
}