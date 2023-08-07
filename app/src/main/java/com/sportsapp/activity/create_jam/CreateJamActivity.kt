package com.sportsapp.activity.create_jam

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.sportsapp.R
import com.sportsapp.activity.spot_detail.SpotDetailActivity
import com.sportsapp.logic.GlobalValues
import com.sportsapp.logic.models.Jam
import com.sportsapp.logic.models.Time
import java.time.LocalDate
import java.util.Calendar

class CreateJamActivity : AppCompatActivity() {

    private lateinit var updateDateTimeButton: Button
    private lateinit var submitButton: Button
    private lateinit var maxParticipantCount: TextView

    private lateinit var jamDate: TextView
    private lateinit var startTime: TextView
    private lateinit var endTime: TextView
    private lateinit var inputHint: TextView

    private var day = 0
    private var month = 0
    private var year = 0

    private var startHour = 0
    private var startMinute = 0

    private var endHour = 0
    private var endMinute = 0

    private var participants = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_jam)

        GlobalValues.update(this)

        jamDate = findViewById(R.id.jamDate)
        startTime = findViewById(R.id.jamStartTime)
        endTime = findViewById(R.id.jamEndTime)
        inputHint = findViewById(R.id.invalidInputJam)
        updateDateTimeButton = findViewById(R.id.updateDateTimeButton)
        submitButton = findViewById(R.id.submitJamButton)
        maxParticipantCount = findViewById(R.id.maxParticipantCount)

        updateDateTimeButton.setOnClickListener(UpdateJam())
        submitButton.setOnClickListener(SubmitJam())

        submitButton.isEnabled = false
    }

    inner class UpdateJam : OnClickListener {
        override fun onClick(v: View?) {
            val calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            val dialog = DatePickerDialog(this@CreateJamActivity, OnDateSet(), year, month, day)
            dialog.show()
        }
    }

    inner class OnDateSet : OnDateSetListener {
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            this@CreateJamActivity.day = dayOfMonth
            this@CreateJamActivity.month = month
            this@CreateJamActivity.year = year

            val text = "$dayOfMonth.$month.$year"
            jamDate.text = text

            val calendar: Calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            val dialog = TimePickerDialog(
                this@CreateJamActivity,
                OnStartTimeSet(),
                hour,
                minute,
                true
            )
            dialog.show()
        }
    }

    inner class OnStartTimeSet : OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            startHour = hourOfDay
            startMinute = minute

            val text = "$hourOfDay:$minute"
            startTime.text = text

            val dialog = TimePickerDialog(
                this@CreateJamActivity,
                OnEndTimeSet(),
                hourOfDay,
                minute,
                true
            )
            dialog.show()
        }
    }

    inner class OnEndTimeSet : OnTimeSetListener {
        override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
            endHour = hourOfDay
            endMinute = minute

            val text = "$hourOfDay:$minute"
            endTime.text = text

            val difference = (endHour * 60 + endMinute) - (startHour * 60 + startMinute)
            if (difference < 30) {
                Toast.makeText(
                    this@CreateJamActivity,
                    "A Jam has to be at least 30 minutes long!",
                    Toast.LENGTH_LONG
                ).show()
                val dialog = TimePickerDialog(
                    this@CreateJamActivity,
                    OnEndTimeSet(),
                    startHour,
                    startMinute,
                    true
                )
                dialog.show()
            }
            submitButton.isEnabled = true
        }
    }

    inner class SubmitJam : OnClickListener {
        override fun onClick(v: View?) {
            if (maxParticipantCount.text.isNotBlank()) {
                participants = maxParticipantCount.text.toString()
                    .toInt()
                if (participants > 1) {
                    inputHint.visibility = GONE
                    val jam = Jam(
                        sportPlaceID = GlobalValues.getCurrentSportPlace()!!.id!!,
                        participantAmount = participants,
                        date = LocalDate.of(year, month, day),
                        startTime = Time(startHour, startMinute),
                        endTime = Time(endHour, endMinute),
                        participants = arrayListOf(GlobalValues.getUser()!!)
                    )
                    GlobalValues.getJamLogic()!!.createJam(jam)
                    val intent = Intent(this@CreateJamActivity, SpotDetailActivity::class.java)
                    startActivity(intent)
                } else {
                    inputHint.visibility = VISIBLE
                }
            } else {
                inputHint.visibility = VISIBLE
            }
        }
    }
}