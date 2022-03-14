package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.appointment.*

interface AppointmentRepository {
    suspend fun callGetAppointmentsApi(request: GetAppointmentsRequestModel): GetAppointmentResponseModel

    suspend fun callModifyAppointmentApi(request: ModifyAppointmentRequestModel): ModifyAppointmentResponseModel

    suspend fun callBookAppointmentApi(request: AddAppointmentRequestModel): AddAppointmentResponseModel

    suspend fun callDoctorAppointmentsListApi(request: DoctorAppointmentsRequestModel): DoctorAppointmentsResponseModel

    suspend fun callDoctorPastAppointmentsListApi(request: GetDoctorPastAppointmentsRequestModel): GetDoctorPastAppointmentsResponseModel
}