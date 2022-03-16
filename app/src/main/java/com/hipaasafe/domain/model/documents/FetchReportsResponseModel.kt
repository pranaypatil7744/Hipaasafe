package com.hipaasafe.domain.model.documents

import com.hipaasafe.domain.model.get_doctors.DoctorDetailsModel

data class FetchReportsResponseModel(
    var success: Boolean? = null,
    var message: String? = "",
    var data: FetchReportData? = FetchReportData()
)

data class FetchReportData(
    var documents: ArrayList<UploadedDocuments> = ArrayList(),
    var documents_request: ArrayList<RequestedDocuments> = ArrayList()
)

data class UploadedDocuments(
    var id: Int? = 0,
    var document_name: String? = "",
    var document_file: String? = "",
    var patient_id: String? = "",
    var doctor_id: String? = "",
    var report_name_id: Int? = 0,
    var is_active: Boolean? = null,
    var role_name: String? = "",
    var createdBy: String? = "",
    var updatedBy: String? = "",
    var createdAt: String? = "",
    var updatedAt: String? = "",
    var hospital_tests: ReportDataModel = ReportDataModel(),
    var doctor_details:DoctorDetails = DoctorDetails()
    )

data class DoctorDetails(
    var name:String =""
)

data class RequestedDocuments(
    var id: Int? = 0,
    var assignee_id: String? = "",
    var patient_id: String? = "",
    var hospital_tests_id: Int? = 0,
    var is_active: Boolean? = null,
    var createdAt: String? = "",
    var updatedAt: String? = "",
    var hospital_tests: ReportDataModel = ReportDataModel(),
    var assignee: AssigneeDataModel = AssigneeDataModel()
)

data class AssigneeDataModel(
    var uid:String? = "",
    var name:String? =""
)

data class ReportDataModel(
    var id: Int? = 0,
    var title: String? = ""
)

data class FetchReportsRequestModel(
    var patient_id:String =""
)
