package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.notifications.*
import com.hipaasafe.domain.repository.NotificationsRepository

class NotificationsRepositoryImp constructor(private val apiService: ApiService) :
    NotificationsRepository {
    override suspend fun callMuteNotificationsApi(request: MuteNotificationsRequestModel): MuteNotificationsResponseModel {
        return apiService.callMuteNotificationApi(request)
    }

    override suspend fun callGetNotificationsApi(request: GetNotificationsRequestModel): GetNotificationsResponseModel {
        return apiService.callGetNotificationsApi(request.page,request.limit)
    }

    override suspend fun callMarkReadNotificationApi(request: MarkReadNotificationRequestModel): MarkReadNotificationResponseModel {
        return apiService.callMarkReadNotificationApi(request)
    }
}