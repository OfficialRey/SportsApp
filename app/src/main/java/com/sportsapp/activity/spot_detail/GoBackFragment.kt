package com.sportsapp.activity.spot_detail

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import com.sportsapp.R
import com.sportsapp.activity.main.MainActivity

class GoBackFragment : Fragment() {

    private lateinit var button: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_go_back, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.goBackButton)

        button.setOnClickListener(GoBack())
    }

    inner class GoBack : OnClickListener {
        override fun onClick(v: View?) {
            val intent = Intent(context!!, MainActivity::class.java)
            startActivity(intent)
        }
    }
}