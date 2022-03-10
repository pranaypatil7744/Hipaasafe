package com.hipaasafe.domain.model.documents

data class UploadAndShareDocumentResponseModel(
    var success: Boolean? = false,
    var message: String? = "",
    var data: UploadAndShareData = UploadAndShareData()
)

data class UploadAndShareData(
    var is_active: Boolean? = null,
    var id: Int? = 0,
    var document_file: String? = "",
    var report_name_id: Int? = 0,
    var updatedAt: String? = "",
    var createdAt: String? = ""
)

data class UploadAndShareDocumentRequestModel(
    var document_file: String = "",
    var report_name_id: Int = 0,
    var doctor_uids: ArrayList<String> = ArrayList()
)
