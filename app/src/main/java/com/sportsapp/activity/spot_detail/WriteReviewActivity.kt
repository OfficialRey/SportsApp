package com.sportsapp.activity.spot_detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import android.widget.TextView
import com.sportsapp.R
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.Review

class WriteReviewActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_review)

        // Find widgets
        ratingBar = findViewById(R.id.writeReviewSelectRating)
        title = findViewById(R.id.writeReviewReviewTitle)
        description = findViewById(R.id.writeReviewReviewDescription)
        button = findViewById(R.id.writeReviewSubmitButton)

        ratingBar.onRatingBarChangeListener = UpdateRating()
        title.addTextChangedListener(TitleWatcher())
        description.addTextChangedListener(DescriptionWatcher())
        button.setOnClickListener(SubmitReview(this))
    }

    inner class UpdateRating : OnRatingBarChangeListener {
        // Ensure rating is always greater than or equal to 1
        override fun onRatingChanged(ratingBar: RatingBar?, rating: Float, fromUser: Boolean) {
            if (rating < 1) {
                ratingBar!!.rating = 1F
            }
        }
    }

    inner class TitleWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null && s.length > DatabaseHelper.TITLE_LENGTH) {
                title.text =
                    s.toString().dropLast(s.length - DatabaseHelper.TITLE_LENGTH)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    inner class DescriptionWatcher : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null && s.length > DatabaseHelper.DESCRIPTION_LENGTH) {
                description.text =
                    s.toString().dropLast(s.length - DatabaseHelper.DESCRIPTION_LENGTH)
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }

    }

    inner class SubmitReview(private var context: Context) : OnClickListener {
        override fun onClick(v: View?) {
            val review = Review(
                creator = GlobalValues.getUser()!!.userID,
                rating = ratingBar.rating.toInt(),
                title = title.text.toString(),
                description = description.text.toString()
            )
            val id = GlobalValues.getReviewLogic()!!
                .createReview(GlobalValues.getCurrentSportPlace()!!.id!!, review)
            if (id != -1) {
                // Successfully created review

                val intent = Intent(context, SpotDetailActivity::class.java)
                startActivity(intent)
            }
        }
    }
}