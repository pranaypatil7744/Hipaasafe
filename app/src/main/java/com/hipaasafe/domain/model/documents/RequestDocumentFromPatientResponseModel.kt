package com.hipaasafe.domain.model.documents

data class RequestDocumentFromPatientResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:ArrayList<RequestDocumentFromPatientData> = ArrayList()
)

data class RequestDocumentFromPatientData(
    var is_active:Boolean? = null,
    var id:Int? = 0,
    var assignee_id:String? ="",
    var patient_id:String? ="",
    var hospital_tests_id:Int? = 0,
    var createdAt:String? ="",
    var updatedAt:String? ="",
)

data class RequestDocumentFromPatientRequestModel(
    var patient_id:String ?=null,
    var guid:String? =null,
    var hospital_reports_id:ArrayList<Int> = ArrayList()
)