package com.hipaasafe.domain.model.get_patients

data class GetPatientsListResponseModel(
    var success: Boolean? = null,
    var message: String? = "",
    var data:PatientsDataModel = PatientsDataModel()
)

data class PatientsDataModel(
    var count: Int? = 0,
    var rows: ArrayList<PatientsListModel> = ArrayList()
)

data class PatientsListModel(
    var id: Int? = 0,
    var name: String? = "",
    var doctor_id: String? = "",
    var patient_id: String? = "",
    var guid: String? = "",
    var createdAt: String? = "",
    var list_patient_details: PatientListDetails = PatientListDetails()
)

data class PatientListDetails(
    var avatar: String? = "",
    var name: String? = "",
    var country_code: String? = "",
    var number: String? = "",
)