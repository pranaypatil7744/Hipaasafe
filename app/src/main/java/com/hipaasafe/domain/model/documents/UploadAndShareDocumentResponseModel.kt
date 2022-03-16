package com.hipaasafe.domain.model.documents

data class UploadAndShareDocumentResponseModel(
    var success: Boolean? = false,
    var message: String? = "",
    var data: UploadAndShareData = UploadAndShareData()
)

data class UploadAndShareData(
    var is_active: Boolean? = null,
    var id: Int? = 0,
    var patient_id: String? = "",
    var document_name: String? = "",
    var document_file: String? = "",
    var role_name: String? = "",
    var report_name_id: Int? = 0,
    var doctor_id: String? = "",
    var createdBy: String? = "",
    var updatedBy: String? = "",
    var updatedAt: String? = "",
    var createdAt: String? = ""
)

data class UploadAndShareDocumentRequestModel(
    var document_file: String = "",
    var document_name: String = "",
    var report_name_id: Int = 0,
    var document_request_id: Int? = null,
    var doctor_uids: ArrayList<String> = ArrayList()
)
