package com.hipaasafe.presentation.profile_edit_details.model

import com.google.gson.annotations.SerializedName
import com.hipaasafe.utils.AppUtils
import java.io.File

class ProfileEditModel(
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("email")
    var email: String? = "",
    @SerializedName("speciality")
    var speciality: String? = "",
    @SerializedName("location")
    var location: String? = "",
    @SerializedName("age")
    var age: String?="",
    @SerializedName("tags")
    var userTags: ArrayList<String>? = ArrayList(),
    @SerializedName("player_id")
    var playerId: String = AppUtils.INSTANCE?.getPlayerId() ?: "",
    @SerializedName("device_platform")
    var devicePlatform: String? = "android",
    @SerializedName("file")
    var fileToUpload: File? = null
)

data class ProfileEditResponseModel(
    var status: Int? = 0,
    var message: String? = "",
    var result: ProfileResult?

)

data class ProfileResult(
    var profile_img: String? = ""
)