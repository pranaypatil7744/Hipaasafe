package com.hipaasafe.data.source.remote

import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.patient_login.PatientSendOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientSendOtpResponseModel
import com.hipaasafe.domain.model.patient_login.PatientValidateOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientValidateOtpResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(ApiNames.PatientSendOtp)
    suspend fun callPatientSendOtpApi(@Body request: PatientSendOtpRequestModel): PatientSendOtpResponseModel

    @POST(ApiNames.PatientValidateOtp)
    suspend fun callPatientValidateOtpApi(@Body request: PatientValidateOtpRequestModel): PatientValidateOtpResponseModel

    @POST(ApiNames.DoctorLoginSendOtp)
    suspend fun callDoctorLoginSendOtpApi(@Body request: DoctorLoginSendOtpRequestModel): DoctorLoginSendOtpResponseModel

    @POST(ApiNames.DoctorLoginValidateOtp)
    suspend fun callDoctorLoginValidateOtpApi(@Body request: DoctorLoginValidateOtpRequestModel): DoctorLoginValidateOtpResponseModel
}