package com.hipaasafe.domain.model.notes

data class GetNotesListResponseModel(
    var success: Boolean? = null,
    var message: String? = "",
    var data: NotesDataModel = NotesDataModel()
)

data class NotesDataModel(
    var count: Int? = 0,
    var rows: ArrayList<NotesListModel> = ArrayList()
)

data class NotesListModel(
    var doctor_id: String? = "",
    var patient_id: String? = "",
    var notes: String? = "",
    var createdAt: String? = "",
    var doctor_details: DoctorDetailsModel = DoctorDetailsModel()
)

data class DoctorDetailsModel(
    var name: String? = ""
)

data class GetNotesListRequestModel(
    var page:Int = 0,
    var limit:Int =10,
//    var doctor_id:String ?="",
    var guid:String =""
)

