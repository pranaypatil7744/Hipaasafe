package com.hipaasafe.domain.model.appointment

data class AddAppointmentResponseModel(
    var success:Boolean? = null,
    var message:String? = "",
    var data:BookAppointmentDataModel = BookAppointmentDataModel()
)

data class BookAppointmentDataModel(
    var doctor_id:String? ="",
    var appointment_id:String? ="",
    var patient_id:String? ="",
    var appointment_date:String? ="",
    var appointment_time:String? ="",
    var appointment_status:String? ="",
    var organization_id:Int? =0,
    var createdBy:String? ="",
    var updatedBy:String? ="",
    var updatedAt:String? ="",
    var createdAt:String? ="",
    var id:Int?= 0,
    var uid:String? =""
)

data class AddAppointmentRequestModel(
    var doctor_id:String? ="",
    var appointment_date:String? ="",
    var appointment_time:String?="",
    var organization_id:Int =0
)