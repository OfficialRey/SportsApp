package com.sportsapp.activity.main.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.sportsapp.R
import com.sportsapp.activity.register_login.LoginActivity
import com.sportsapp.databinding.FragmentSettingsBinding
import com.sportsapp.logic.GlobalValues

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var logoutButton: Button
    private lateinit var userName: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root = binding.root

        logoutButton = root.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener(LogOut())

        userName = root.findViewById(R.id.userName)
        userName.text = GlobalValues.getUser()?.userName

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    inner class LogOut : OnClickListener {
        override fun onClick(v: View?) {
            GlobalValues.setUser(null)

            val intent = Intent(this@SettingsFragment.requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}