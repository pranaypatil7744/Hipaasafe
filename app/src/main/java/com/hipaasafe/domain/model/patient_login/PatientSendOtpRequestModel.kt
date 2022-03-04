package com.hipaasafe.domain.model.patient_login

data class PatientSendOtpRequestModel(
    var country_code:String = "",
    var number:String = ""
)

data class PatientSendOtpResponseModel(
    var success:Boolean = true,
    var message:String = "",
    var data:DataModel = DataModel()
)

data class DataModel(
    var otp:String? = ""
)
