package com.hipaasafe.data.source.remote

import com.hipaasafe.domain.model.appointment.GetAppointmentResponseModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentRequestModel
import com.hipaasafe.domain.model.appointment.ModifyAppointmentResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsResponseModel
import com.hipaasafe.domain.model.patient_login.*
import com.hipaasafe.domain.model.reports.GetReportsListResponseModel
import com.hipaasafe.domain.model.reports.UploadReportFileResponseModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel
import okhttp3.MultipartBody
import retrofit2.http.*

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
    suspend fun callPatientRegisterApi(@Body request: PatientRegisterRequestModel): PatientRegisterResponseModel

    @PUT(ApiNames.PatientUpdateProfile)
    suspend fun callPatientUpdateProfile(@Body request: ProfileEditRequestModel): ProfileEditResponseModel

    @GET(ApiNames.GetAppointmentsApi)
    suspend fun callGetAppointmentsApi(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String,
    ): GetAppointmentResponseModel

    @PUT(ApiNames.ModifyAppointmentsApi)
    suspend fun callModifyAppointmentApi(@Body request: ModifyAppointmentRequestModel): ModifyAppointmentResponseModel

    @GET(ApiNames.GetMyDoctorsList)
    suspend fun callGetDoctorsListApi(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): GetDoctorsResponseModel

    @GET(ApiNames.GetReportsList)
    suspend fun callGetReportsList(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): GetReportsListResponseModel

    @Multipart
    @POST(ApiNames.UploadReportFileApi)
    suspend fun callUploadReportFileApi(
        @Part user_reports:MultipartBody.Part
    ):UploadReportFileResponseModel












}