package com.sportsapp.logic.recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sportsapp.R
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.Review
import com.sportsapp.logic.models.User

class ReviewRecyclerViewAdapter(
    private val reviews: ArrayList<Review>
) :
    RecyclerView.Adapter<ReviewRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.review_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val entry = reviews[position]
        val creator: User = GlobalValues.getUserLogic()!!.loadUser(entry.creator)!!
        val date =
            "${entry.creationDate?.dayOfMonth}.${entry.creationDate?.monthValue}.${entry.creationDate?.year}"

        holder.creator.text = creator.userName
        holder.date.text = date
        holder.rating.rating = entry.rating.toFloat()
        holder.rating.setIsIndicator(true)
        holder.title.text = entry.title
        holder.description.text = entry.description

    }

    // This class defines the ViewHolder object for each item in the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val creator: TextView = itemView.findViewById(R.id.reviewCreator)
        val date: TextView = itemView.findViewById(R.id.reviewDate)
        val rating: RatingBar = itemView.findViewById(R.id.reviewRating)
        val title: TextView = itemView.findViewById(R.id.reviewTitle)
        val description: TextView = itemView.findViewById(R.id.reviewDescription)
    }
}