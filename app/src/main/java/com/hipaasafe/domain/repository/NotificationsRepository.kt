package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.notifications.MuteNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.MuteNotificationsResponseModel

interface NotificationsRepository {

    suspend fun callMuteNotificationsApi(request:MuteNotificationsRequestModel):MuteNotificationsResponseModel

}