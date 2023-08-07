package com.sportsapp.activity.register_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sportsapp.R
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.log.CustomLogger
import com.sportsapp.logic.GlobalValues

class SplashActivity : AppCompatActivity() {

    // Yes I could set the starting activity in the manifest file
    // But it seems a lot more structured having a bare entry point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}