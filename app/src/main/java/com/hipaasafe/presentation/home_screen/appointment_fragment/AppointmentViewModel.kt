package com.hipaasafe.presentation.home_screen.appointment_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.GetAppointmentsRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentResponseModel
import com.hipaasafe.domain.usecase.appointment.GetAppointmentsUseCase
import com.hipaasafe.domain.usecase.appointment.ModifyAppointmentUseCase
import com.hipaasafe.domain.usecase.base.UseCaseResponse

class AppointmentViewModel constructor(
    var getAppointmentsUseCase: GetAppointmentsUseCase,
    var modifyAppointmentUseCase: ModifyAppointmentUseCase
) : ViewModel() {
    val getAppointmentsResponseData = MutableLiveData<GetAppointmentResponseModel>()
    val modifyAppointmentResponseData = MutableLiveData<ModifyAppointmentResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callGetAppointmentsListApi(request: GetAppointmentsRequestModel) {
        getAppointmentsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetAppointmentResponseModel> {
                override fun onSuccess(result: GetAppointmentResponseModel) {
                    getAppointmentsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

    fun callGetModifyAppointmentApi(request: ModifyAppointmentRequestModel) {
        modifyAppointmentUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ModifyAppointmentResponseModel> {
                override fun onSuccess(result: ModifyAppointmentResponseModel) {
                    modifyAppointmentResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }
}