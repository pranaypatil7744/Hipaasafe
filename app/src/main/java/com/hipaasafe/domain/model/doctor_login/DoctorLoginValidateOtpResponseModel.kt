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
    var organization_id: String? = "",
    var mute_notifications: Boolean? = false,
    var doctor_details: DoctorDetailsModel? = DoctorDetailsModel(),
    var doctors_mapped:ArrayList<DoctorsMappedModel>? = ArrayList(),
    var access_token: String? = "",
    var refresh_token: String? = ""
)
data class DoctorsMappedModel(
    var id:Int? =0,
    var uid:String? ="",
    var name:String? ="",
    var email:String? ="",
    var country_code:String? ="",
    var number:String? ="",
    var role_id:String? ="",
    var role_name:String? ="",
    var avatar:String? ="",
    var organization_id:Int? =0,
    var mute_notifications:Boolean? =null,
    var doctor_details: DoctorDetailsModel?=DoctorDetailsModel()
)

data class DoctorDetailsModel(
    var location: String? = "",
    var experience: String? = "",
    var qr_code: String? = "",
    var speciality: SpecialityModel = SpecialityModel(),
    var tags: ArrayList<SpecialityModel> = ArrayList()
)
data class SpecialityModel(
    var speciality_id:Int?=0,
    var title:String? =""
)

data class DoctorLoginValidateOtpRequestModel(
    var email: String= "",
    var otp: Int = 0,
    var player_id: String? =AppUtils.INSTANCE?.getPlayerId(),
    var device_platform:String? = "ANDROID"
)