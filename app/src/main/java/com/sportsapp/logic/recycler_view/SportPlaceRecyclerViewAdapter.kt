package com.sportsapp.logic.recycler_view

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.sportsapp.R
import com.sportsapp.activity.spot_detail.SpotDetailActivity
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.SportPlace

class SportPlaceRecyclerViewAdapter(
    private val places: ArrayList<SportPlace>,
    private val context: Context
) :
    RecyclerView.Adapter<SportPlaceRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.spot_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return places.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entry = places[position]
        val address = GlobalValues.getAddressLogic()!!.loadAddress(entry.id!!)
        val street = "${address?.street} ${address?.houseNumber}"
        val city = "${address?.postalCode} ${address?.city}"
        val type = GlobalValues.getSportPlaceLogic()!!.loadSportPlaceType(entry.type)

        val reviews = GlobalValues.getReviewLogic()!!.loadReviews(entry.id!!)
        var reviewCount = "(${reviews.size})"

        if (reviews.size <= 0) {
            reviewCount = "(No reviews)"
            holder.ratingBar.visibility = GONE
        }

        holder.image.setImageBitmap(entry.image)
        holder.title.text = address?.city
        holder.image.setImageBitmap(entry.image)
        holder.type.text = type
        holder.street.text = street
        holder.city.text = city
        holder.ratingBar.setIsIndicator(true)
        holder.ratingBar.rating = GlobalValues.getReviewLogic()!!.getRating(entry.id!!)
        holder.reviewCount.text = reviewCount

        holder.itemView.setOnClickListener(OnClickEntry(entry))

        if (GlobalValues.getJamLogic()!!.getJam(entry.id!!) == null) {
            holder.activityHint.visibility = GONE
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.locationImage)
        val title: TextView = itemView.findViewById(R.id.locationTitle)
        val type: TextView = itemView.findViewById(R.id.locationType)
        val street: TextView = itemView.findViewById(R.id.locationStreetLayout)
        val city: TextView = itemView.findViewById(R.id.locationCityLayout)
        val ratingBar: RatingBar = itemView.findViewById(R.id.locationRatingBar)
        val reviewCount: TextView = itemView.findViewById(R.id.locationReviewAmount)
        val activityHint: CardView = itemView.findViewById(R.id.jamActiveHintCard)
    }


    // Access detail screen for spot to show reviews and create jams
    inner class OnClickEntry(private val sportPlace: SportPlace) : OnClickListener {

        override fun onClick(v: View?) {
            GlobalValues.setCurrentSportPlace(sportPlace)
            val intent = Intent(context, SpotDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}