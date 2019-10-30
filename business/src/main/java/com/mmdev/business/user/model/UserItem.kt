package com.mmdev.business.user.model

import java.util.*

data class UserItem (val name: String = "",
                     var city: String = "",
                     var gender: String = "",
                     var preferedGender: String = "",
                     val mainPhotoUrl: String = "",
                     var photoURLs: ArrayList<String>? = null,
                     val userId: String = ""){

    override fun toString(): String {
        return "UserItem{\n\tname=$name,\n\tid=$userId,\n\tPhotoUrl='$mainPhotoUrl\n}"
    }

}



