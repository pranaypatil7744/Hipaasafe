package com.hipaasafe.domain.model.get_doctors

import com.hipaasafe.domain.model.doctor_login.SpecialityModel

data class GetDoctorsResponseModel(
    var success: Boolean? = false,
    var message: String? = "",
    var data: DoctorsDataModel = DoctorsDataModel()
)

data class DoctorsDataModel(
    var count: Int? = 0,
    var rows: ArrayList<MyDoctorsListModel>? = ArrayList()
)

data class MyDoctorsListModel(
    var id: Int? = 0,
    var name: String? = "",
    var doctor_id: String? = "",
    var patient_id: String? = "",
    var guid: String? = "",
    var createdAt: String? = "",
    var list_doctor_details: ListDoctorDetailsModel = ListDoctorDetailsModel()
)

data class ListDoctorDetailsModel(
    var avatar: String? = "",
    var name: String? = "",
    var country_code: String? = "",
    var number: String? = "",
    var doctor_details: DoctorDetailsModel = DoctorDetailsModel()
)

data class DoctorDetailsModel(
    var id: Int? = 0,
    var experience: String? = "",
    var location: String? = "",
    var speciality: SpecialityModel = SpecialityModel()
)

data class GetDoctorsRequestModel(
    var page: Int = 0,
    var limit: Int = 10
)
