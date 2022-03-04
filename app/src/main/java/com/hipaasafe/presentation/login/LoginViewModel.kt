package com.hipaasafe.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.patient_login.*
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.login.*

class LoginViewModel constructor(
    private val patientSendOtpUseCase: PatientSendOtpUseCase,
    private val doctorLoginSendOtpUseCase: DoctorLoginSendOtpUseCase,
    private val patientValidateOtpUseCase: PatientValidateOtpUseCase,
    private val doctorLoginValidationOtpUseCase: DoctorLoginValidationOtpUseCase,
    private val patientRegisterUseCase: PatientRegisterUseCase
) : ViewModel() {

    val patientSendOtpResponseData = MutableLiveData<PatientSendOtpResponseModel>()
    val patientValidateOtpResponseData = MutableLiveData<PatientValidateOtpResponseModel>()
    val doctorLoginSendOtpResponseData = MutableLiveData<DoctorLoginSendOtpResponseModel>()
    val doctorLoginValidateOtpResponseData = MutableLiveData<DoctorLoginValidateOtpResponseModel>()
    val patientRegisterResponseData = MutableLiveData<PatientRegisterResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()


    fun callPatientRegisterApi(request: PatientRegisterRequestModel) {
        showProgressbar.value = true
        patientRegisterUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PatientRegisterResponseModel> {
                override fun onSuccess(result: PatientRegisterResponseModel) {
                    patientRegisterResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    fun callDoctorLoginSendOtpApi(request: DoctorLoginSendOtpRequestModel) {
        showProgressbar.value = true
        doctorLoginSendOtpUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<DoctorLoginSendOtpResponseModel> {
                override fun onSuccess(result: DoctorLoginSendOtpResponseModel) {
                    doctorLoginSendOtpResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    fun callPatientSendOtpApi(request: PatientSendOtpRequestModel) {
        showProgressbar.value = true
        patientSendOtpUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PatientSendOtpResponseModel> {
                override fun onSuccess(result: PatientSendOtpResponseModel) {
                    patientSendOtpResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    fun callPatientValidateOtpApi(request: PatientValidateOtpRequestModel) {
        showProgressbar.value = true
        patientValidateOtpUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<PatientValidateOtpResponseModel> {
                override fun onSuccess(result: PatientValidateOtpResponseModel) {
                    patientValidateOtpResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

    fun callDoctorLoginValidateOtpApi(request: DoctorLoginValidateOtpRequestModel) {
        showProgressbar.value = true
        doctorLoginValidationOtpUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<DoctorLoginValidateOtpResponseModel> {
                override fun onSuccess(result: DoctorLoginValidateOtpResponseModel) {
                    doctorLoginValidateOtpResponseData.value = result
                    showProgressbar.value = false
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.getErrorMessage()
                    showProgressbar.value = false
                }
            })
    }

}