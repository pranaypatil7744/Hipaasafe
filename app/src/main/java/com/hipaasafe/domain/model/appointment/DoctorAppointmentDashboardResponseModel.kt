package com.hipaasafe.domain.model.appointment

data class DoctorAppointmentDashboardResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:ArrayList<DashboardDaysCountData> = ArrayList()
)

data class DashboardDaysCountData(
    var date:String? ="",
    var day:Int =0,
    var count:String? =""
)

data class DoctorAppointmentDashboardRequestModel(
    var doctor_id:String =""
)
