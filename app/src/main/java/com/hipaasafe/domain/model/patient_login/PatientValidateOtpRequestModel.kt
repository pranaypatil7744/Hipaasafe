package com.hipaasafe.domain.model.patient_login

data class PatientValidateOtpRequestModel(
    var country_code:String ="",
    var number:String ="",
    var otp:Int =0,
)
//TODO update model
data class PatientValidateOtpResponseModel(
    var success:Boolean = true,
    var message:String ="",
)
