package com.hipaasafe.domain.model.get_doctors

data class DoctorMyTeamResponseModel(
    var success:Boolean? =null,
    var message:String? = "",
    var data:DoctorMyTeamsData =DoctorMyTeamsData()
)

data class DoctorMyTeamsData(
    var count: Int? = 0,
    var rows: ArrayList<DoctorMyTeamsListModel>? = ArrayList()
)

data class DoctorMyTeamsListModel(
    var uid:String? = "",
    var name:String? = "",
    var country_code:String? = "",
    var number:String? = "",
    var avatar:String? = "",
    var doctor_details:DoctorDetailsModel? = DoctorDetailsModel()
)

data class DoctorMyTeamsRequestModel(
    var page:Int = 1,
    var limit:Int =10
)