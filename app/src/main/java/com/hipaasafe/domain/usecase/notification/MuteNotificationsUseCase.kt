package com.hipaasafe.domain.usecase.notification

import com.hipaasafe.domain.model.notifications.MuteNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.MuteNotificationsResponseModel
import com.hipaasafe.domain.repository.NotificationsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class MuteNotificationsUseCase constructor(private val notificationsRepository: NotificationsRepository) :
    UseCase<MuteNotificationsResponseModel, Any?>() {
    override suspend fun run(params: Any?): MuteNotificationsResponseModel {
        return notificationsRepository.callMuteNotificationsApi(params as MuteNotificationsRequestModel)
    }
}