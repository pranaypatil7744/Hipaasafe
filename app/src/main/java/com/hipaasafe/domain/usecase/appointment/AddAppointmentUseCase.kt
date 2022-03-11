package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.AddAppointmentRequestModel
import com.hipaasafe.domain.model.appointment.AddAppointmentResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class AddAppointmentUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<AddAppointmentResponseModel, Any?>() {
    override suspend fun run(params: Any?): AddAppointmentResponseModel {
        return appointmentRepository.callBookAppointmentApi(params as AddAppointmentRequestModel)
    }
}