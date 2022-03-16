package com.hipaasafe.data.source.remote

import com.hipaasafe.domain.model.appointment.*
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.model.documents.*
import com.hipaasafe.domain.model.get_doctors.DoctorMyTeamResponseModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsResponseModel
import com.hipaasafe.domain.model.get_patients.GetPatientsListResponseModel
import com.hipaasafe.domain.model.get_patients.PatientsDataModel
import com.hipaasafe.domain.model.patient_login.*
import com.hipaasafe.domain.model.static_details.GetStaticDetailsResponseModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadResponseModel
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
//        @Query("page") page: Int,
//        @Query("limit") limit: Int
        @Query("doctor_id") doctor_id: String
    ): GetReportsListResponseModel

    @Multipart
    @POST(ApiNames.UploadReportFileApi)
    suspend fun callUploadReportFileApi(
        @Part user_reports: MultipartBody.Part
    ): UploadReportFileResponseModel

    @POST(ApiNames.UploadAndShareDocumentApi)
    suspend fun callUploadAndShareDocumentApi(@Body request: UploadAndShareDocumentRequestModel): UploadAndShareDocumentResponseModel

    @GET(ApiNames.GetStaticDetailsApi)
    suspend fun callStaticDetailsApi(): GetStaticDetailsResponseModel

    @GET(ApiNames.FetchReportsDocumentListApi)
    suspend fun callFetchReportsApi(
        @Query("patient_id")patient_id:String,
    ): FetchReportsResponseModel

    @POST(ApiNames.ShareDocumentApi)
    suspend fun callShareDocumentApi(@Body request: ShareDocumentRequestModel): ShareDocumentResponseModel

    @Multipart
    @POST(ApiNames.UpdateProfilePic)
    suspend fun callUpdateProfilePicApi(
        @Part profile_pic: MultipartBody.Part
    ): ProfilePicUploadResponseModel

    @POST(ApiNames.BookAppointmentApi)
    suspend fun callBookAppointmentApi(@Body request: AddAppointmentRequestModel): AddAppointmentResponseModel

    @GET(ApiNames.DoctorAppointmentsListApi)
    suspend fun callDoctorAppointmentsListApi(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("date") date: String,
        @Query("doctor_id") doctor_id: String,
    ): DoctorAppointmentsResponseModel

    @GET(ApiNames.DoctorMyTeamListApi)
    suspend fun callDoctorMyTeamsListApi(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): DoctorMyTeamResponseModel

    @GET(ApiNames.MyPatientsListApi)
    suspend fun callPatientsListApi(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): GetPatientsListResponseModel

    @GET(ApiNames.DoctorPastAppointmentsListApi)
    suspend fun callDoctorPastAppointmentListApi(
        @Query("from_date") from_date: String,
        @Query("to_date") to_date: String,
        @Query("doctor_id") doctor_id: String,
    ): GetDoctorPastAppointmentsResponseModel

    @POST(ApiNames.RequestDocumentFromPatientApi)
    suspend fun callRequestDocumentFromPatientsApi(@Body request: RequestDocumentFromPatientRequestModel
    ): RequestDocumentFromPatientResponseModel

    @GET(ApiNames.GetMyQueueApi)
    suspend fun callGetMyQueueApi():GetMyQueueResponseModel
}