package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.DoctorAppointmentDashboardRequestModel
import com.hipaasafe.domain.model.appointment.DoctorAppointmentDashboardResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class DoctorAppointmentCountUseCase constructor(private val appointmentRepository: AppointmentRepository):
    UseCase<DoctorAppointmentDashboardResponseModel,Any?>() {
    override suspend fun run(params: Any?): DoctorAppointmentDashboardResponseModel {
        return appointmentRepository.callDoctorDashboardCountApi(params as DoctorAppointmentDashboardRequestModel)
    }
}