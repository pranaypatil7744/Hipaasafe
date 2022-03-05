package com.hipaasafe.domain.model.doctor_login

import com.hipaasafe.utils.AppUtils

data class DoctorLoginValidateOtpResponseModel(
    var success: Boolean = true,
    var message: String = "",
    var data: DoctorDataModel = DoctorDataModel()
)

data class DoctorDataModel(
    var id: String? = "",
    var uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var country_code: String? = "",
    var number: String? = "",
    var role_id: String? = "",
    var role_name: String? = "",
    var avatar: String? = "",
    var doctor_details: DoctorDetailsModel? = DoctorDetailsModel(),
    var access_token: String? = "",
    var refresh_token: String? = ""
)

data class DoctorDetailsModel(
    var id: String? = "",
    var uid: String? = "",
    var location: String? = "",
    var experience: String? = "",
    var speciality: String? = "",
    var tags: ArrayList<String> = ArrayList(),
    var organization_domain: String? = "",
    var createdAt: String? = "",
    var updatedAt: String? = "",
)

data class DoctorLoginValidateOtpRequestModel(
    var email: String= "",
    var otp: Int = 0,
    var player_id: String? =AppUtils.INSTANCE?.getPlayerId(),
    var device_platform:String? = "android"
)