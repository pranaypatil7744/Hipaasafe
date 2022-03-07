package com.hipaasafe.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.patient_login.*
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.login.*
import com.hipaasafe.listener.ValidationListener
import com.hipaasafe.utils.AppUtils

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
    var validationListener: ValidationListener? = null

    fun validatePatientRegisterData(request: PatientRegisterRequestModel) {
        if (request.name.isEmpty()) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.NAME_ERROR,
                R.string.please_enter_name
            )
            return
        }
        if (request.email.isEmpty()) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.EMAIL_ERROR,
                R.string.please_enter_email_id
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidEmail(request.email) == false) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.EMAIL_ERROR,
                R.string.please_enter_valid_email_id
            )
            return
        }
        if (request.age == 0) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.AGE_ERROR,
                R.string.please_enter_age
            )
            return
        }
        validationListener?.onValidationSuccess(Constants.SUCCESS, R.string.success)
    }

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