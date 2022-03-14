package com.hipaasafe.domain.usecase.appointment

import com.hipaasafe.domain.model.appointment.GetDoctorPastAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.GetDoctorPastAppointmentsResponseModel
import com.hipaasafe.domain.repository.AppointmentRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetDoctorPastAppointmentsUseCase constructor(private val appointmentRepository: AppointmentRepository) :
    UseCase<GetDoctorPastAppointmentsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetDoctorPastAppointmentsResponseModel {
        return appointmentRepository.callDoctorPastAppointmentsListApi(params as GetDoctorPastAppointmentsRequestModel)
    }
}