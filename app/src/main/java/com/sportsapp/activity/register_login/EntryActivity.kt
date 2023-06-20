package com.sportsapp.activity.register_login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sportsapp.R

class EntryActivity : AppCompatActivity() {

    // Yes I could set the starting activity in the manifest file
    // But it seems a lot more structured having a MAIN entry point

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}