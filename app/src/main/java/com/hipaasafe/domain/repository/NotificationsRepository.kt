package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.notifications.*

interface NotificationsRepository {

    suspend fun callMuteNotificationsApi(request:MuteNotificationsRequestModel):MuteNotificationsResponseModel

    suspend fun callGetNotificationsApi(request:GetNotificationsRequestModel):GetNotificationsResponseModel

    suspend fun callMarkReadNotificationApi(request:MarkReadNotificationRequestModel):MarkReadNotificationResponseModel
}