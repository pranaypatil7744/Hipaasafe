package com.hipaasafe.domain.model.doctor_login

data class DoctorLoginSendOtpResponseModel(
    var success:Boolean = true,
    var message:String ="",
    var data:DataModel = DataModel()
)

data class DataModel(
    var otp:String? = ""
)

data class DoctorLoginSendOtpRequestModel(
    var email:String =""
)
