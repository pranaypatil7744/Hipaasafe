package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.ModifyAppointmentRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class ModifyAppointmentUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<ModifyAppointmentResponseModel, Any?>() {
    override suspend fun run(params: Any?): ModifyAppointmentResponseModel {
        return appointmentRepository.callModifyAppointmentApi(params as ModifyAppointmentRequestModel)
    }
}