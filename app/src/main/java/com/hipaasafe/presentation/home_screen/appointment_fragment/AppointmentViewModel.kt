package com.hipaasafe.presentation.home_screen.appointment_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.appointment.*
import com.hipaasafe.domain.usecase.appointment.*
import com.hipaasafe.domain.usecase.base.UseCaseResponse

class AppointmentViewModel constructor(
    var getAppointmentsUseCase: GetAppointmentsUseCase,
    var modifyAppointmentUseCase: ModifyAppointmentUseCase,
    var addAppointmentUseCase: AddAppointmentUseCase,
    var doctorAppointmentsListUseCase: DoctorAppointmentsListUseCase,
    var getDoctorPastAppointmentsUseCase: GetDoctorPastAppointmentsUseCase,
    var stopMyQueueUseCase: StopMyQueueUseCase,
    var getMyQueueUseCase: GetMyQueueUseCase
) : ViewModel() {
    val getAppointmentsResponseData = MutableLiveData<GetAppointmentResponseModel>()
    val modifyAppointmentResponseData = MutableLiveData<ModifyAppointmentResponseModel>()
    val addAppointmentResponseData = MutableLiveData<AddAppointmentResponseModel>()
    val doctorAppointmentsListResponseData = MutableLiveData<DoctorAppointmentsResponseModel>()
    val getMyQueueResponseData = MutableLiveData<GetMyQueueResponseModel>()
    val stopMyQueueResponseData = MutableLiveData<StopMyQueueResponseModel>()
    val getDoctorPastAppointmentsListResponseData =
        MutableLiveData<GetDoctorPastAppointmentsResponseModel>()
    val messageData = MutableLiveData<String>()
    val queueMessageData = MutableLiveData<String>()


    fun callStopMyQueueApi(request:StopMyQueueRequestModel) {
        stopMyQueueUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<StopMyQueueResponseModel> {
                override fun onSuccess(result: StopMyQueueResponseModel) {
                    stopMyQueueResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    queueMessageData.value = apiError?.message
                }

            })
    }

    fun callGetMyQueueApi() {
        getMyQueueUseCase.invoke(
            viewModelScope,
            null,
            object : UseCaseResponse<GetMyQueueResponseModel> {
                override fun onSuccess(result: GetMyQueueResponseModel) {
                    getMyQueueResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    queueMessageData.value = apiError?.message
                }

            })
    }


    fun callGetDoctorPastAppointmentsListApi(request: GetDoctorPastAppointmentsRequestModel) {
        getDoctorPastAppointmentsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetDoctorPastAppointmentsResponseModel> {
                override fun onSuccess(result: GetDoctorPastAppointmentsResponseModel) {
                    getDoctorPastAppointmentsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

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