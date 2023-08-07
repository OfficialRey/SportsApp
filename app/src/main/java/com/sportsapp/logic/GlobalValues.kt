package com.sportsapp.logic

import android.content.Context
import com.sportsapp.logic.models.SportPlace
import com.sportsapp.logic.models.User

class GlobalValues {

    companion object {
        private var currentUser: User? = null
        private var userLogic: UserLogic? = null
        private var addressLogic: AddressLogic? = null
        private var sportPlaceLogic: SportPlaceLogic? = null
        private var imageLogic: ImageLogic? = null
        private var reviewLogic: ReviewLogic? = null
        private var jamLogic: JamLogic? = null

        private var currentSportPlace: SportPlace? = null

        fun update(context: Context?) {
            userLogic = UserLogic(context)
            addressLogic = AddressLogic(context)
            sportPlaceLogic = SportPlaceLogic(context)
            imageLogic = ImageLogic(context)
            reviewLogic = ReviewLogic(context)
            jamLogic = JamLogic(context)
        }

        fun setUser(user: User?) {
            currentUser = user
        }

        fun setCurrentSportPlace(sportPlace: SportPlace) {
            currentSportPlace = sportPlace
        }

        fun getUserLogic(): UserLogic? {
            return userLogic
        }

        fun getAddressLogic(): AddressLogic? {
            return addressLogic
        }

        fun getSportPlaceLogic(): SportPlaceLogic? {
            return sportPlaceLogic
        }

        fun getImageLogic(): ImageLogic? {
            return imageLogic
        }

        fun getReviewLogic(): ReviewLogic? {
            return reviewLogic
        }

        fun getJamLogic(): JamLogic? {
            return jamLogic
        }

        fun getUser(): User? {
            return currentUser
        }

        fun getCurrentSportPlace(): SportPlace? {
            return currentSportPlace
        }
    }
}