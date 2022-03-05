package com.hipaasafe.presentation.notification

data class NotificationResponseModel(
    var status: Int? = 0,
    var message: String? = "",
    var result: ArrayList<NotificationResult> = ArrayList()
)

data class NotificationResult(
    var _id: String? = "",
    var title: String? = "",
    var message: String? = "",
    var user_img: String? = "",
    var createdAt: String? = "",
    var status: Boolean
)

data class NotificationChangeStatusRequestModel(
    var id: String?=""
)
