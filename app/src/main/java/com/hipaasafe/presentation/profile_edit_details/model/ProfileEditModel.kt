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
    @SerializedName("dob")
    var dob: String?="",
//    @SerializedName("file")
//    var fileToUpload: File? = null
)

data class ProfileEditResponseModel(
    var success:Boolean = true,
    var message:String = "",
    var data:UpdateProfileDataModel = UpdateProfileDataModel()
)

data class UpdateProfileDataModel(
    var id:Int =0,
    var uid:String? ="",
    var name:String? ="",
    var email:String? ="",
    var country_code:String? ="",
    var number:String? ="",
    var role_id:Int? =0,
    var role_name:String? ="",
    var avatar:String? ="",
    var organization_id:Int? =0,
    var mute_notifications:Boolean? =null,
    var dob:String? ="",
)

