package com.hipaasafe.domain.model.notes

data class AddNoteResponseModel(
    var success: Boolean? = null,
    var message: String? = "",
    var data: AddNoteDataModel = AddNoteDataModel()
)

data class AddNoteDataModel(
    var is_active: Boolean? = null,
    var id: Int? = 0,
    var doctor_id: String? = "",
    var patient_id: String? = "",
    var notes: String? = "",
    var createdBy: String? = "",
    var updatedBy: String? = "",
    var updatedAt: String? = "",
    var createdAt: String? = ""
)

data class AddNoteRequestModel(
    var doctor_id:String? ="",
    var guid:String? ="",
    var notes:String? =""
)