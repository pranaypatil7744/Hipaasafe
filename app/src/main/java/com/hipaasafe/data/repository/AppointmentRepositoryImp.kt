package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.appointment.*
import com.hipaasafe.domain.repository.AppointmentRepository

class AppointmentRepositoryImp constructor(private val apiService: ApiService) :
    AppointmentRepository {
    override suspend fun callGetAppointmentsApi(request: GetAppointmentsRequestModel): GetAppointmentResponseModel {
        return apiService.callGetAppointmentsApi(
            page = request.page,
            limit = request.limit,
            type = request.type
        )
    }

    override suspend fun callModifyAppointmentApi(request: ModifyAppointmentRequestModel): ModifyAppointmentResponseModel {
        return apiService.callModifyAppointmentApi(request)
    }

    override suspend fun callBookAppointmentApi(request: AddAppointmentRequestModel): AddAppointmentResponseModel {
        return apiService.callBookAppointmentApi(request)
    }

    override suspend fun callDoctorAppointmentsListApi(request: DoctorAppointmentsRequestModel): DoctorAppointmentsResponseModel {
        return apiService.callDoctorAppointmentsListApi(
            page = request.page,
            limit = request.limit,
            date = request.date,
            doctor_id = request.doctor_id
        )
    }


}