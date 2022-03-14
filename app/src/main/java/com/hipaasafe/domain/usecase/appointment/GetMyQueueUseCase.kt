package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.GetMyQueueResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetMyQueueUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<GetMyQueueResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetMyQueueResponseModel {
        return appointmentRepository.callGetMyQueueApi()
    }
}