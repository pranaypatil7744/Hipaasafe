package com.hipaasafe.domain.model.notifications

data class MuteNotificationsResponseModel(
    var success: Boolean? = null,
    var message: String? = "",
    var data: MuteNotificationsDataModel = MuteNotificationsDataModel()
)


data class MuteNotificationsDataModel(
    var mute_notifications: Boolean? = null
)

data class MuteNotificationsRequestModel(
    var mute_notifications: Boolean? = null
)

