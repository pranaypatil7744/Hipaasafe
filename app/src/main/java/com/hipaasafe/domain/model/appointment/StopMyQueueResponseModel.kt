package com.hipaasafe.domain.model.appointment

data class StopMyQueueResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:StopQueueDataModel = StopQueueDataModel()
)
data class StopQueueDataModel(
    var appointment_id:String?= "",
    var doctor_id:String?= "",
    var patient_id:String?= "",
    var appointment_date:String?= "",
    var appointment_time:String?= "",
    var appointment_status:String?= ""
)

data class StopMyQueueRequestModel(
    var appointment_date:String =""
)