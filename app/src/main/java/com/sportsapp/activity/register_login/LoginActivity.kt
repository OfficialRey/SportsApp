package com.sportsapp.activity.register_login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sportsapp.R
import com.sportsapp.logic.LoginLogic
import com.google.android.material.R.color.*
import com.sportsapp.activity.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var loginUserName: TextView
    private lateinit var loginPassword: TextView
    private lateinit var loginButton: Button
    private lateinit var loginChangeToRegister: TextView
    private lateinit var feedbackText: TextView

    private lateinit var loginLogic: LoginLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginLogic = LoginLogic(this)

        // Find widgets
        loginUserName = findViewById(R.id.loginUsername)
        loginPassword = findViewById(R.id.loginPassword)
        loginButton = findViewById(R.id.loginButton)
        loginChangeToRegister = findViewById(R.id.changeToRegister)
        feedbackText = findViewById(R.id.loginFeedback)

        // Login listeners
        val textListener = TextChangeListener()
        loginUserName.addTextChangedListener(textListener)
        loginPassword.addTextChangedListener(textListener)

        loginButton.setOnClickListener(LoginButtonListener(this))
        loginChangeToRegister.setOnClickListener(ChangeToRegisterListener(this))
    }

    private inner class TextChangeListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val userName = loginUserName.text
            val password = loginPassword.text
            loginButton.isEnabled = userName.isNotBlank() && password.isNotBlank()
        }
    }

    private inner class LoginButtonListener(var activity: LoginActivity) :
        View.OnClickListener {
        override fun onClick(v: View?) {
            resetStyles()
            val userName = loginUserName.text.toString()
            val password = loginPassword.text.toString()
            if (loginLogic.existsUser(userName)) {
                if (loginLogic.checkPassword(userName, password)) {
                    loginLogic.logInUser(userName)
                    // User logged in
                    intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    loginUserName.setTextColor(getColor(design_default_color_error))
                    feedbackText.text = getString(R.string.login_password)
                }
            } else {
                loginUserName.setTextColor(getColor(design_default_color_error))
                feedbackText.text = getString(R.string.login_username_exists)
            }
        }
    }

    private inner class ChangeToRegisterListener(var activity: LoginActivity) :
        View.OnClickListener {
        override fun onClick(v: View?) {
            intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("PrivateResource")
    private fun resetStyles() {
        loginUserName.setTextColor(getColor(m3_default_color_primary_text))
        loginPassword.setTextColor(getColor(m3_default_color_primary_text))
        feedbackText.text = getString(R.string.empty_text)
    }
}