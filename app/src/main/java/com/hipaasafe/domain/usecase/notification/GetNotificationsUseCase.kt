package com.hipaasafe.domain.usecase.notification

import com.hipaasafe.domain.model.notifications.GetNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.GetNotificationsResponseModel
import com.hipaasafe.domain.repository.NotificationsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetNotificationsUseCase constructor(private val notificationsRepository: NotificationsRepository) :
    UseCase<GetNotificationsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetNotificationsResponseModel {
        return notificationsRepository.callGetNotificationsApi(params as GetNotificationsRequestModel)
    }
}