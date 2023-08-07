package com.sportsapp.activity.register_login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import com.google.android.material.R.color.*
import com.sportsapp.R
import com.sportsapp.activity.main.MainActivity
import com.sportsapp.database.DatabaseHelper
import com.sportsapp.logic.GlobalValues

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerUserName: TextView
    private lateinit var registerPassword: TextView
    private lateinit var registerPasswordConfirm: TextView
    private lateinit var registerButton: Button
    private lateinit var registerChangeToLogin: TextView
    private lateinit var feedbackText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Find widgets
        registerUserName = findViewById(R.id.registerUsername)
        registerPassword = findViewById(R.id.registerPassword)
        registerPasswordConfirm = findViewById(R.id.registerPasswordConfirm)
        registerButton = findViewById(R.id.registerButton)
        registerChangeToLogin = findViewById(R.id.changeToLogin)
        feedbackText = findViewById(R.id.registerFeedback)

        // Register listeners
        val textListener = TextChangeListener()
        registerUserName.addTextChangedListener(textListener)
        registerPassword.addTextChangedListener(textListener)
        registerPasswordConfirm.addTextChangedListener(textListener)

        registerButton.setOnClickListener(RegisterButtonListener(this))
        registerChangeToLogin.setOnClickListener(ChangeToLoginListener(this))

        GlobalValues.update(this)
    }

    private inner class TextChangeListener : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            val userName = registerUserName.text
            val password = registerPassword.text
            val passwordConfirm = registerPasswordConfirm.text
            registerButton.isEnabled =
                userName.isNotBlank() && password.isNotBlank() && passwordConfirm.isNotBlank()
        }
    }

    private inner class RegisterButtonListener(var activity: RegisterActivity) : OnClickListener {
        override fun onClick(v: View?) {
            resetStyles()
            if (!GlobalValues.getUserLogic()!!.existsUser(registerUserName.text.toString())) {
                if (registerPassword.text.toString() == registerPasswordConfirm.text.toString()) {
                    if (registerUserName.length() <= DatabaseHelper.USER_NAME_LENGTH) {
                        if (registerPassword.length() <= DatabaseHelper.PASSWORD_LENGTH) {
                            // Create new user
                            GlobalValues.getUserLogic()!!.createUser(
                                registerUserName.text.toString(),
                                registerPassword.text.toString()
                            )
                            // User logged in
                            intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            feedbackText.text = getString(R.string.register_password_too_long)
                            registerPassword.setTextColor(getColor(design_default_color_error))
                            registerPasswordConfirm.setTextColor(getColor(design_default_color_error))
                        }
                    } else {
                        feedbackText.text = getString(R.string.register_username_too_long)
                        registerPassword.setTextColor(getColor(design_default_color_error))
                        registerPasswordConfirm.setTextColor(getColor(design_default_color_error))
                    }
                } else {
                    feedbackText.text = getString(R.string.register_password_wrong)
                    registerPassword.setTextColor(getColor(design_default_color_error))
                    registerPasswordConfirm.setTextColor(getColor(design_default_color_error))
                }
            } else {
                registerUserName.setTextColor(getColor(design_default_color_error))
                feedbackText.text = getString(R.string.register_username_exists)
                feedbackText.setTextColor(getColor(design_default_color_error))
            }
        }
    }

    private inner class ChangeToLoginListener(var activity: RegisterActivity) : OnClickListener {
        override fun onClick(v: View?) {
            intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("PrivateResource")
    private fun resetStyles() {
        registerUserName.setTextColor(getColor(m3_default_color_primary_text))
        registerPassword.setTextColor(getColor(m3_default_color_primary_text))
        registerPasswordConfirm.setTextColor(getColor(m3_default_color_primary_text))
        feedbackText.text = getString(R.string.empty_text)
    }
}