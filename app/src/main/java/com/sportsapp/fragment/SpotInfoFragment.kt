package com.sportsapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.sportsapp.R
import com.sportsapp.logic.GlobalValues

class SpotInfoFragment : Fragment() {

    private lateinit var spotTitleType: TextView
    private lateinit var spotTitleCity: TextView
    private lateinit var spotType: TextView
    private lateinit var spotRating: RatingBar
    private lateinit var reviewCount: TextView
    private lateinit var spotStreet: TextView
    private lateinit var spotCity: TextView
    private lateinit var spotImage: ImageView

    private lateinit var jamMarker: TextView
    private lateinit var jamDate: TextView
    private lateinit var jamTime: TextView
    private lateinit var jamParticipants: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_spot_info, container, false)

        // Find widgets
        spotTitleType = view.findViewById(R.id.spotTitleType)
        spotTitleCity = view.findViewById(R.id.spotTitleCity)
        spotType = view.findViewById(R.id.spotType)
        spotRating = view.findViewById(R.id.spotRating)
        reviewCount = view.findViewById(R.id.spotReviewAmount)
        spotStreet = view.findViewById(R.id.spotStreet)
        spotCity = view.findViewById(R.id.spotCity)
        spotImage = view.findViewById(R.id.spotImage)

        jamMarker = view.findViewById(R.id.jamMarker)
        jamDate = view.findViewById(R.id.jamDateHint)
        jamTime = view.findViewById(R.id.jamDateTime)
        jamParticipants = view.findViewById(R.id.jamParticipantInfo)

        val sportPlace = GlobalValues.getCurrentSportPlace()!!
        val address = GlobalValues.getAddressLogic()!!.loadAddress(sportPlace.address)!!
        val sportType = GlobalValues.getSportPlaceLogic()!!.loadSportPlaceType(sportPlace.type)
        val reviews =
            GlobalValues.getReviewLogic()!!.loadReviews(sportPlace.id!!)

        if (reviews.size <= 0) {
            spotRating.visibility = View.GONE
            reviewCount.visibility = View.GONE
        }

        spotRating.setIsIndicator(true)

        // Update widget content
        val reviewAmount = "(${reviews.size})"
        val street = "${address.street} ${address.houseNumber}"
        val city = "${address.postalCode} ${address.city}"
        val rating = GlobalValues.getReviewLogic()!!.getRating(sportPlace.id!!)

        spotTitleType.text = sportType
        spotTitleCity.text = address.city
        spotType.text = sportType
        spotRating.rating = rating
        reviewCount.text = reviewAmount
        spotStreet.text = street
        spotCity.text = city
        spotImage.setImageBitmap(sportPlace.image)

        val jam = GlobalValues.getJamLogic()!!.getJam(sportPlace.id!!)

        if (jam != null) {
            jamMarker.visibility = VISIBLE
            jamDate.visibility = VISIBLE
            jamTime.visibility = VISIBLE
            jamParticipants.visibility = VISIBLE

            val date = "${jam.date.dayOfMonth}.${jam.date.monthValue}.${jam.date.year}"
            val time =
                "${jam.startTime} - ${jam.endTime}"
            val participants =
                "${jam.participants.size} out of ${jam.participantAmount} participants"


            jamDate.text = date
            jamTime.text = time

            jamParticipants.text = participants
        }
        return view
    }
}