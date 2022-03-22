package com.hipaasafe.domain.model.notifications

data class GetNotificationsResponseModel(
    var success: Boolean? = null,
    var message: String? = "",
    var data: NotificationsData = NotificationsData()
)

data class NotificationsData(
    var count: Int? = 0,
    var rows: ArrayList<NotificationItems> = ArrayList()
)

data class NotificationItems(
    var id: Int = 0,
    var title: String? = "",
    var message: String? = "",
    var redirect_to: String? = "",
    var sender_uid: String? = "",
    var receiver_uid: String? = "",
    var mark_read: Boolean? = false,
    var createdAt: String? = "",
    var sender_details: SenderDetailsModel = SenderDetailsModel()
)

data class SenderDetailsModel(
    val name: String? = "",
    var avatar: String? = ""
)

data class GetNotificationsRequestModel(
    var page: Int,
    var limit: Int
)