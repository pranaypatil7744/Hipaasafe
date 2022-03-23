package com.hipaasafe.domain.usecase.notification

import com.hipaasafe.domain.model.notifications.MarkReadNotificationRequestModel
import com.hipaasafe.domain.model.notifications.MarkReadNotificationResponseModel
import com.hipaasafe.domain.repository.NotificationsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class MarkReadNotificationUseCase constructor(private val notificationsRepository: NotificationsRepository) :
    UseCase<MarkReadNotificationResponseModel, Any?>() {
    override suspend fun run(params: Any?): MarkReadNotificationResponseModel {
        return notificationsRepository.callMarkReadNotificationApi(params as MarkReadNotificationRequestModel)
    }
}