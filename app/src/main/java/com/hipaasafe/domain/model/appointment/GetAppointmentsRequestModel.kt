package com.hipaasafe.domain.model.appointment


data class GetAppointmentsRequestModel(
    var page:Int = 0,
    var limit:Int = 0,
    var type:String = ""
)

data class GetAppointmentResponseModel(
    var success:Boolean = false,
    var message:String? ="",
    var data:AppointmentDataModel = AppointmentDataModel()
)

data class AppointmentDataModel(
    var count:Int? =0,
    var rows:ArrayList<AppointmentItemsModel> = ArrayList()
)
data class AppointmentItemsModel(
    var id:Int? =0,
    var appointment_id:String? ="",
    var appointment_date:String? ="",
    var appointment_time:String? ="",
    var appointment_status:String? ="",
    var appointment_doctor_details:AppointmentDoctorDetailsModel = AppointmentDoctorDetailsModel()
)
data class AppointmentDoctorDetailsModel(
    var name:String? = "",
    var doctor_details:DoctorDetailsModel = DoctorDetailsModel()
)

data class DoctorDetailsModel(
    var speciality:SpecialityModel = SpecialityModel()
)

data class SpecialityModel(
    var speciality:Int? =0,
    var title:String? =""
)