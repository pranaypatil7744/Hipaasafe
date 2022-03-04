package com.hipaasafe.domain.model.patient_login

import com.hipaasafe.utils.AppUtils

data class PatientValidateOtpRequestModel(
    var country_code:String ="",
    var number:String ="",
    var otp:Int =0,
    var player_id: String? =AppUtils.INSTANCE?.getPlayerId(),
    var device_platform:String? = "android"
    )
//TODO update model
data class PatientValidateOtpResponseModel(
    var success:Boolean = true,
    var message:String ="",
)
