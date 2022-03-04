package com.hipaasafe.domain.model.patient_login

data class PatientRegisterRequestModel(
    var name:String = "",
    var email:String = "",
    var age:Int = 0,
)

data class PatientRegisterResponseModel(
    var success:Boolean = true,
    var message:String ="",
    var data:UserRegisterDataModel = UserRegisterDataModel()
)

data class UserRegisterDataModel(
    var id:Int? =0,
    var uid:String? ="",
    var name:String? ="",
    var email:String? ="",
    var country_code:String? ="",
    var number:String? ="",
    var role_id:Int? =0,
    var role_name:String? ="",
    var profile_img:String? ="",
    var organization_id:String? ="",
    var mute_notifications:String? ="",
    var patient_details:PatientDetailsModel? = PatientDetailsModel()
)
