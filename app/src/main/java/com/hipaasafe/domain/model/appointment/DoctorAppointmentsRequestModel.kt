package com.hipaasafe.domain.model.appointment

data class DoctorAppointmentsRequestModel(
    var page:Int =1,
    var limit:Int =10,
    var date:String ="",
    var doctor_id:String ="",
)

data class DoctorAppointmentsResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:DoctorAppointmentsData = DoctorAppointmentsData()
)

data class DoctorAppointmentsData(
    var count:Int? =0,
    var rows:ArrayList<DoctorAppointmentListModel> = ArrayList()
)
data class DoctorAppointmentListModel(
    var id:Int? = 0,
    var appointment_date:String? ="",
    var appointment_time:String? ="",
    var appointment_status:String? ="",
    var patient_details:PatientDetailsModel = PatientDetailsModel()
)

data class PatientDetailsModel(
    var age:String? = "",
    var uid:String? = "",
    var name:String? = "",
    var email:String? = "",
    var country_code:String? = "",
    var number:String? = "",
    var mobile:String? = "",
    var avatar:String? = "",
)
