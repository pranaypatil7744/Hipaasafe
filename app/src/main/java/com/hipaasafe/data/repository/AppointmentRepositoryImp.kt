package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentResponseModel
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


}