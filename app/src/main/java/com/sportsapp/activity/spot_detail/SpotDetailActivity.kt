package com.sportsapp.activity.spot_detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sportsapp.R
import com.sportsapp.activity.create_jam.CreateJamActivity
import com.sportsapp.log.CustomLogger
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.Jam
import com.sportsapp.logic.recycler_view.ReviewRecyclerViewAdapter

class SpotDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spotReviewHint: TextView

    private lateinit var writeReview: Button
    private lateinit var jamButton: Button

    private var currentJam: Jam? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot_detail)
        GlobalValues.update(this)

        recyclerView = findViewById(R.id.spotReviewView)
        spotReviewHint = findViewById(R.id.spotReviewHint)
        writeReview = findViewById(R.id.writeReviewButton)
        jamButton = findViewById(R.id.createJamButton)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        writeReview.setOnClickListener(WriteReview(this))
        jamButton.setOnClickListener(CreateJam(this))


        val sportPlace = GlobalValues.getCurrentSportPlace()!!

        // Create reviews
        val reviews =
            GlobalValues.getReviewLogic()!!.loadReviews(sportPlace.id!!)

        if (reviews.size <= 0) {
            spotReviewHint.visibility = VISIBLE
            recyclerView.visibility = GONE
        } else {
            // Create recycler view objects

            val adapter = ReviewRecyclerViewAdapter(reviews)
            recyclerView.adapter = adapter
        }

        // If jam is running, change layout
        currentJam = GlobalValues.getJamLogic()!!.getJam(sportPlace.id!!)
        if (currentJam != null) {
            jamButton.text = getString(R.string.join_jam)
            jamButton.setOnClickListener(JoinJam())

            // Check if user is part of jam
            for (participant in currentJam!!.participants) {
                if (participant.userID == GlobalValues.getUser()!!.userID) {
                    CustomLogger.logDebug("DEBUG", participant.userName)
                    // Local user is part of jam
                    jamButton.text = getString(R.string.leave_jam)
                    jamButton.setOnClickListener(LeaveJam())
                    break
                }
            }
        }
    }

    inner class WriteReview(private var context: Context) : OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, WriteReviewActivity::class.java)
            startActivity(intent)
        }
    }

    inner class CreateJam(private var context: Context) : OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context, CreateJamActivity::class.java)
            startActivity(intent)
        }
    }

    inner class JoinJam : OnClickListener {
        override fun onClick(v: View?) {
            GlobalValues.getJamLogic()!!
                .addUserToJam(GlobalValues.getUser()!!.userID, currentJam?.id!!)

            val intent = Intent(this@SpotDetailActivity, SpotDetailActivity::class.java)
            startActivity(intent)
        }
    }

    inner class LeaveJam : OnClickListener {
        override fun onClick(v: View?) {
            GlobalValues.getJamLogic()!!
                .removeUserFromJam(GlobalValues.getUser()!!.userID)

            val intent = Intent(this@SpotDetailActivity, SpotDetailActivity::class.java)
            startActivity(intent)
        }
    }
}