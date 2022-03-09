package com.hipaasafe.presentation.home_screen.appointment_fragment.model

data class UpcomingAppointmentModel(
    var appointmentItemType: AppointmentItemType,
    var name:String? = "",
    var speciality:String? = "",
    var date:String? = "",
    var time:String? = "",
    var appointmentStatus: AppointmentStatus? = AppointmentStatus.ITEM_PENDING,
    var appointment_id:String =""
)
enum class AppointmentItemType(val value: Int){
    ITEM_APPOINTMENT(1),
    ITEM_TITLE(2),
    ITEM_DIVIDER(3)
}

enum class AppointmentStatus(val value:Int){
    ITEM_PENDING(1),
    ITEM_RESCHEDULED(2),
    ITEM_CONFIRM(3),
    ITEM_CANCEL(4),
    ITEM_COMPLETED(5),
}
