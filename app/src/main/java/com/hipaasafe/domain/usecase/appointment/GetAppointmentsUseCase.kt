package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetAppointmentsUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<GetAppointmentResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetAppointmentResponseModel {
        return appointmentRepository.callGetAppointmentsApi(params as GetAppointmentsRequestModel)
    }
}