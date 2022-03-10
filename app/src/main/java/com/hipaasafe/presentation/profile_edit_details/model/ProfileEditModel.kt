package com.hipaasafe.presentation.profile_edit_details.model

import com.google.gson.annotations.SerializedName
import java.io.File

class ProfileEditRequestModel(
    @SerializedName("name")
    var name: String? = "",
//    @SerializedName("email")
//    var email: String? = "",
//    @SerializedName("country_code")
//    var country_code: String? = "",
//    @SerializedName("number")
//    var number: String? = "",
    @SerializedName("age")
    var age: String?="",
//    @SerializedName("file")
//    var fileToUpload: File? = null
)

data class ProfileEditResponseModel(
    var success:Boolean = true,
    var message:String = ""
)
