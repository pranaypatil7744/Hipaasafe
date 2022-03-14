package com.hipaasafe.domain.model.appointment

data class GetDoctorPastAppointmentsResponseModel(
    var success:Boolean? = null,
    var message:String? = "",
    var data:ArrayList<AppointmentItemsModel> = ArrayList()
)

data class DoctorPastAppointmentDataModel(
    var id:Int? =0,
    var appointment_id:String? ="",
    var appointment_date:String? ="",
    var appointment_time:String? ="",
    var appointment_status:String? ="",
    var patient_details:PatientDetailsModel = PatientDetailsModel()
)

data class GetDoctorPastAppointmentsRequestModel(
    var from_date:String ="",
    var to_date:String ="",
    var doctor_id:String ="",
)
