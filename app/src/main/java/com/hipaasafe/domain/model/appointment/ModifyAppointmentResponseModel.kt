package com.hipaasafe.domain.model.appointment

data class ModifyAppointmentResponseModel(
    var success:Boolean? = false,
    var message:String? ="",
    var data:ModifyAppointmentData = ModifyAppointmentData()
)

data class ModifyAppointmentData(
    var appointment_id:String? ="",
    var doctor_id:String? ="",
    var patient_id:String? ="",
    var appointment_date:String? ="",
    var appointment_time:String? ="",
    var appointment_status:String? =""
)
data class ModifyAppointmentRequestModel(
    var appointment_id:String = "",
    var type:String = "",
)
