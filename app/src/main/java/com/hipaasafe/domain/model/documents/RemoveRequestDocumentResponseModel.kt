package com.hipaasafe.domain.model.documents

data class RemoveRequestDocumentResponseModel(
    var success:Boolean? = null,
    var message:String?= "",
    var data:ArrayList<RemoveDocumentData> =ArrayList()
)

data class RemoveDocumentData(
    var id:Int? = 0,
    var assignee_id:String? ="",
    var patient_id:String? ="",
    var hospital_tests_id:Int? =0,
    var is_active:Boolean? =null,
    var createdAt:String? ="",
    var updatedAt:String? ="",
)

data class RemoveRequestDocumentRequestModel(
    val request_ids: ArrayList<Int>
)
