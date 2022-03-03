package com.hipaasafe.domain.model

//Sample model

data class LoginResponseModel(
    val CandidateStatus:String? = "",
    val InnovID:String? = "",
    val IsDummy:Boolean? = false,
    val IsMigrated:String? = "",
    val IsTransferred:String? = "",
    val Mobile:String? = "",
    val OTP:Int? = 0,
    val Status:String? = "",
)

data class LoginRequestModel(
    var APKVersion:String? = "",
    var AndroidVersion:String? = "",
    var BuildNo:String? = "",
    var EmployeeCode:String? = "",
    var Mobile:String? = "",
    var ModelNo:String? = "",
    var SignupSource:String? = "",
)
