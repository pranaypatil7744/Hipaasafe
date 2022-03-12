package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.DoctorAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.DoctorAppointmentsResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class DoctorAppointmentsListUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<DoctorAppointmentsResponseModel, Any?>() {
    override suspend fun run(params: Any?): DoctorAppointmentsResponseModel {
        return appointmentRepository.callDoctorAppointmentsListApi(params as DoctorAppointmentsRequestModel)
    }
}