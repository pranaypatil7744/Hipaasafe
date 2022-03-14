package com.hipaasafe.domain.model.appointment

data class GetMyQueueResponseModel(
    var success:Boolean? = null,
    var message:String? ="",
    var data:QueueDataModel = QueueDataModel()
)

data class QueueDataModel(
    var queue_no:Int? =0
)