package com.hipaasafe.domain.model.patient_login

data class PatientRegisterRequestModel(
    var name:String = "",
    var email:String = "",
    var dob:String = "",
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
    var avatar:String? ="",
    var organization_id:String? ="",
    var mute_notifications:Boolean? =false,
    var patient_details:PatientDetailsModel? = PatientDetailsModel()
)
