package com.hipaasafe.domain.model.patient_login

import com.hipaasafe.utils.AppUtils

data class PatientValidateOtpRequestModel(
    var country_code:String ="",
    var number:String ="",
    var otp:Int =0,
    var player_id: String? =AppUtils.INSTANCE?.getPlayerId(),
    var device_platform:String? = "android"
    )

data class PatientValidateOtpResponseModel(
    var success:Boolean = true,
    var message:String ="",
    var data:UserDataModel = UserDataModel()
)

data class UserDataModel(
    var id:Int? =0,
    var uid:String? ="",
    var name:String? ="",
    var email:String? ="",
    var country_code:String? ="",
    var number:String? ="",
    var role_id:Int? =0,
    var role_name:String? ="",
    var avatar:String? ="",
    var organization_id:String? ="",
    var mute_notifications:Boolean? =false,
    var patient_details:PatientDetailsModel? = PatientDetailsModel(),
    var access_token:String? ="",
    var refresh_token:String? ="",
)
data class PatientDetailsModel(
    var dob:String? ="",
    var profile_update:Boolean= false
)
