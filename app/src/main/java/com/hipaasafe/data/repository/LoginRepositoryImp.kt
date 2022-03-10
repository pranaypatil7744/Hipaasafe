package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.patient_login.*
import com.hipaasafe.domain.repository.LoginRepository

class LoginRepositoryImp(private val apiService: ApiService): LoginRepository {
    override suspend fun callPatientSendOtpApi(request: PatientSendOtpRequestModel): PatientSendOtpResponseModel {
        return apiService.callPatientSendOtpApi(request)
    }

    override suspend fun callPatientValidateOtpApi(request: PatientValidateOtpRequestModel): PatientValidateOtpResponseModel {
        return apiService.callPatientValidateOtpApi(request)
    }

    override suspend fun callDoctorLoginSendOtpApi(request: DoctorLoginSendOtpRequestModel): DoctorLoginSendOtpResponseModel {
        return apiService.callDoctorLoginSendOtpApi(request)
    }

    override suspend fun callDoctorLoginValidateOtpApi(request: DoctorLoginValidateOtpRequestModel): DoctorLoginValidateOtpResponseModel {
        return apiService.callDoctorLoginValidateOtpApi(request)
    }

    override suspend fun callPatientRegisterApi(request: PatientRegisterRequestModel): PatientRegisterResponseModel {
        return apiService.callPatientRegisterApi(request)
    }

}