package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.notifications.MuteNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.MuteNotificationsResponseModel
import com.hipaasafe.domain.repository.NotificationsRepository

class NotificationsRepositoryImp constructor(private val apiService: ApiService):NotificationsRepository {
    override suspend fun callMuteNotificationsApi(request: MuteNotificationsRequestModel): MuteNotificationsResponseModel {
        return apiService.callMuteNotificationApi(request)
    }
}