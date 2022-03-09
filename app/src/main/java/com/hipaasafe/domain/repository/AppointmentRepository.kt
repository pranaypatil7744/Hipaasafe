package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentResponseModel

interface AppointmentRepository {
    suspend fun callGetAppointmentsApi(request: GetAppointmentsRequestModel): GetAppointmentResponseModel

    suspend fun callModifyAppointmentApi(request: ModifyAppointmentRequestModel): ModifyAppointmentResponseModel
}