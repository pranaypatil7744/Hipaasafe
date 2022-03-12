package com.hipaasafe.presentation.home_screen.appointment_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.appointment.*
import com.hipaasafe.domain.usecase.appointment.AddAppointmentUseCase
import com.hipaasafe.domain.usecase.appointment.DoctorAppointmentsListUseCase
import com.hipaasafe.domain.usecase.appointment.GetAppointmentsUseCase
import com.hipaasafe.domain.usecase.appointment.ModifyAppointmentUseCase
import com.hipaasafe.domain.usecase.base.UseCaseResponse

class AppointmentViewModel constructor(
    var getAppointmentsUseCase: GetAppointmentsUseCase,
    var modifyAppointmentUseCase: ModifyAppointmentUseCase,
    var addAppointmentUseCase: AddAppointmentUseCase,
    var doctorAppointmentsListUseCase: DoctorAppointmentsListUseCase
) : ViewModel() {
    val getAppointmentsResponseData = MutableLiveData<GetAppointmentResponseModel>()
    val modifyAppointmentResponseData = MutableLiveData<ModifyAppointmentResponseModel>()
    val addAppointmentResponseData = MutableLiveData<AddAppointmentResponseModel>()
    val doctorAppointmentsListResponseData = MutableLiveData<DoctorAppointmentsResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callDoctorAppointmentsListApi(request: DoctorAppointmentsRequestModel) {
        doctorAppointmentsListUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<DoctorAppointmentsResponseModel> {
                override fun onSuccess(result: DoctorAppointmentsResponseModel) {
                    doctorAppointmentsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

    fun callAddAppointmentApi(request: AddAppointmentRequestModel) {
        addAppointmentUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<AddAppointmentResponseModel> {
                override fun onSuccess(result: AddAppointmentResponseModel) {
                    addAppointmentResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

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