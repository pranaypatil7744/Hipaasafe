package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.patient_login.*

interface LoginRepository {

    suspend fun callPatientSendOtpApi(request: PatientSendOtpRequestModel): PatientSendOtpResponseModel

    suspend fun callPatientValidateOtpApi(request:PatientValidateOtpRequestModel):PatientValidateOtpResponseModel

    suspend fun callDoctorLoginSendOtpApi(request:DoctorLoginSendOtpRequestModel):DoctorLoginSendOtpResponseModel

    suspend fun callDoctorLoginValidateOtpApi(request:DoctorLoginValidateOtpRequestModel):DoctorLoginValidateOtpResponseModel

    suspend fun callPatientRegisterApi(request:PatientRegisterRequestModel):PatientRegisterResponseModel
}