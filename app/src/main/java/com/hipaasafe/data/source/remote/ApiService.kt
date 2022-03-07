package com.hipaasafe.data.source.remote

import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.patient_login.*
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST(ApiNames.PatientSendOtp)
    suspend fun callPatientSendOtpApi(@Body request: PatientSendOtpRequestModel): PatientSendOtpResponseModel

    @POST(ApiNames.PatientValidateOtp)
    suspend fun callPatientValidateOtpApi(@Body request: PatientValidateOtpRequestModel): PatientValidateOtpResponseModel

    @POST(ApiNames.DoctorLoginSendOtp)
    suspend fun callDoctorLoginSendOtpApi(@Body request: DoctorLoginSendOtpRequestModel): DoctorLoginSendOtpResponseModel

    @POST(ApiNames.DoctorLoginValidateOtp)
    suspend fun callDoctorLoginValidateOtpApi(@Body request: DoctorLoginValidateOtpRequestModel): DoctorLoginValidateOtpResponseModel

    @POST(ApiNames.PatientRegister)
    suspend fun callPatientRegisterApi(@Body request:PatientRegisterRequestModel):PatientRegisterResponseModel

    @PUT(ApiNames.PatientUpdateProfile)
    suspend fun callPatientUpdateProfile(@Body request:ProfileEditRequestModel):ProfileEditResponseModel
}