package com.sportsapp.logic

import com.sportsapp.logic.objects.User

class GlobalValues {

    companion object {
        private var currentUser: User? = null

        fun setUser(user: User?) {
            currentUser = user
        }

        fun getUser(): User? {
            return currentUser
        }
    }
}