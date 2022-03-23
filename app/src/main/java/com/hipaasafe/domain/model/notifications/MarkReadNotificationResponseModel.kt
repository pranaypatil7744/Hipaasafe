package com.hipaasafe.domain.model.notifications

data class MarkReadNotificationResponseModel(
    var success:Boolean? = null,
    var message:String? = "",
    var data:MarkReadNotificationData=MarkReadNotificationData()
)

data class MarkReadNotificationData(
    var id:Int? =0,
    var title:String? ="",
    var message:String? ="",
    var redirect_to:String? ="",
    var sender_uid:String? ="",
    var receiver_uid:String? ="",
    var mark_read:Boolean? =null,
    var createdAt:String? ="",
)
data class MarkReadNotificationRequestModel(
    var notification_id:Int
)
