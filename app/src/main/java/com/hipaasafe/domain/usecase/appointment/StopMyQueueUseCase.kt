package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.StopMyQueueRequestModel
import com.hipaasafe.domain.model.appointment.StopMyQueueResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class StopMyQueueUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<StopMyQueueResponseModel, Any?>() {
    override suspend fun run(params: Any?): StopMyQueueResponseModel {
        return appointmentRepository.callStopMyQueueApi(params as StopMyQueueRequestModel)
    }
}