package com.hipaasafe.di

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.hipaasafe.base.BaseApplication
import com.hipaasafe.data.source.remote.ApiNames
import com.hipaasafe.data.source.remote.ApiService
import com.google.gson.GsonBuilder
import com.hipaasafe.Constants
import com.hipaasafe.data.repository.*
import com.hipaasafe.domain.repository.*
import com.hipaasafe.domain.usecase.appointment.*
import com.hipaasafe.domain.usecase.doctors.DoctorMyTeamsListUseCase
import com.hipaasafe.domain.usecase.doctors.GetDoctorsUseCase
import com.hipaasafe.domain.usecase.documents.*
import com.hipaasafe.domain.usecase.login.*
import com.hipaasafe.domain.usecase.notes.AddNotesUseCase
import com.hipaasafe.domain.usecase.notes.GetNotesListUseCase
import com.hipaasafe.domain.usecase.notification.GetNotificationsUseCase
import com.hipaasafe.domain.usecase.notification.MarkReadNotificationUseCase
import com.hipaasafe.domain.usecase.notification.MuteNotificationsUseCase
import com.hipaasafe.domain.usecase.patients.GetPatientsListUseCase
import com.hipaasafe.domain.usecase.profile.PatientUpdateProfileUseCase
import com.hipaasafe.domain.usecase.profile.UpdateProfilePicUseCase
import com.hipaasafe.domain.usecase.static_details.StaticDetailsUseCase
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L

val NetworkModule = module {

    single(named("normalService")) {
        createService(get(named("normal")))
    }
    single(named("candidateService")) {
        createService(get(named("candidate")))
    }

    single(named("normal")) {
        createRetrofit(get(), ApiNames.BASE_URL)
    }
    single(named("candidate")) {
        createRetrofit(get(), ApiNames.BASE_URL_CANDIDATE)
    }

    single {
        createOkHttpClient()
    }
    single {
        GsonConverterFactory.create()
    }
    single {
        GsonBuilder().create()
    }

}

fun createOkHttpClient(): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return OkHttpClient.Builder().addInterceptor(
        ChuckerInterceptor.Builder(BaseApplication().getAppContext())
            .collector(ChuckerCollector(BaseApplication().getAppContext()))
            .maxContentLength(250000L)
            .redactHeaders(emptySet())
            .alwaysReadResponseBody(false)
            .build()
    )
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val mUrl = original.url.toString()
            val requestBuilder: Request.Builder
            val token =
                BaseApplication.preferenceUtils.getValue(Constants.PreferenceKeys.access_token)

            if (!mUrl.contains(ApiNames.PatientSendOtp) &&
                !mUrl.contains(ApiNames.DoctorLoginSendOtp) &&
                !mUrl.contains(ApiNames.PatientValidateOtp) &&
                !mUrl.contains(ApiNames.DoctorLoginValidateOtp)
            ) {
//                        // Request customization: add request headers
                requestBuilder = original.newBuilder()
                    .header("Authorization", token)
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)

            } else {
                requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .method(original.method, original.body)
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        })
        .addInterceptor(httpLoggingInterceptor)
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()
}

fun createService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)
}

fun createLoginRepository(apiService: ApiService): LoginRepository {
    return LoginRepositoryImp(apiService)
}

fun createPatientSendOtpUseCase(loginRepository: LoginRepository): PatientSendOtpUseCase {
    return PatientSendOtpUseCase(loginRepository)
}

fun createPatientValidateOtpUseCase(loginRepository: LoginRepository): PatientValidateOtpUseCase {
    return PatientValidateOtpUseCase(loginRepository)
}

fun createDoctorLoginSendOtpUseCase(loginRepository: LoginRepository): DoctorLoginSendOtpUseCase {
    return DoctorLoginSendOtpUseCase(loginRepository)
}

fun createDoctorLoginValidateOtpUseCase(loginRepository: LoginRepository): DoctorLoginValidationOtpUseCase {
    return DoctorLoginValidationOtpUseCase(loginRepository)
}

fun createPatientRegisterUseCase(loginRepository: LoginRepository): PatientRegisterUseCase {
    return PatientRegisterUseCase(loginRepository)
}

fun createProfileRepository(apiService: ApiService): ProfileRepository {
    return ProfileRepositoryImp(apiService)
}

fun createUpdateProfilePicUseCase(profileRepository: ProfileRepository): UpdateProfilePicUseCase {
    return UpdateProfilePicUseCase(profileRepository)
}

fun createPatientUpdateProfileUseCase(profileRepository: ProfileRepository): PatientUpdateProfileUseCase {
    return PatientUpdateProfileUseCase(profileRepository)
}

fun createAppointmentRepository(apiService: ApiService): AppointmentRepository {
    return AppointmentRepositoryImp(apiService)
}

fun createGetAppointmentsUseCase(appointmentRepository: AppointmentRepository): GetAppointmentsUseCase {
    return GetAppointmentsUseCase(appointmentRepository)
}

fun createDoctorAppointmentCountUseCase(appointmentRepository: AppointmentRepository): DoctorAppointmentCountUseCase {
    return DoctorAppointmentCountUseCase(appointmentRepository)
}

fun createStopMyQueueUseCase(appointmentRepository: AppointmentRepository): StopMyQueueUseCase {
    return StopMyQueueUseCase(appointmentRepository)
}

fun createGetMyQueueUseCase(appointmentRepository: AppointmentRepository): GetMyQueueUseCase {
    return GetMyQueueUseCase(appointmentRepository)
}

fun createGetDoctorPastAppointmentsUseCase(appointmentRepository: AppointmentRepository): GetDoctorPastAppointmentsUseCase {
    return GetDoctorPastAppointmentsUseCase(appointmentRepository)
}

fun createDoctorAppointmentsListUseCase(appointmentRepository: AppointmentRepository): DoctorAppointmentsListUseCase {
    return DoctorAppointmentsListUseCase(appointmentRepository)
}

fun createAddAppointmentUseCase(appointmentRepository: AppointmentRepository): AddAppointmentUseCase {
    return AddAppointmentUseCase(appointmentRepository)
}

fun createModifyAppointmentUseCase(appointmentRepository: AppointmentRepository): ModifyAppointmentUseCase {
    return ModifyAppointmentUseCase(appointmentRepository)
}

fun createDoctorsRepository(apiService: ApiService): DoctorsRepository {
    return DoctorsRepositoryImp(apiService)
}

fun createGetDoctorsUseCase(doctorsRepository: DoctorsRepository): GetDoctorsUseCase {
    return GetDoctorsUseCase(doctorsRepository)
}

fun DoctorMyTeamsListUseCase(doctorsRepository: DoctorsRepository): DoctorMyTeamsListUseCase {
    return DoctorMyTeamsListUseCase(doctorsRepository)
}

fun createReportsRepository(apiService: ApiService): DocumentsRepository {
    return DocumentsRepositoryImp(apiService)
}

fun createFetchReportsUseCase(documentsRepository: DocumentsRepository): FetchReportsUseCase {
    return FetchReportsUseCase(documentsRepository)
}

fun createRemoveRequestDocUseCase(documentsRepository: DocumentsRepository): RemoveRequestDocUseCase {
    return RemoveRequestDocUseCase(documentsRepository)
}

fun createRequestDocumentFromPatientUseCase(documentsRepository: DocumentsRepository): RequestDocumentFromPatientUseCase {
    return RequestDocumentFromPatientUseCase(documentsRepository)
}

fun createShareReportsUseCase(documentsRepository: DocumentsRepository): ShareDocumentUseCase {
    return ShareDocumentUseCase(documentsRepository)
}

fun createGetReportsUseCase(documentsRepository: DocumentsRepository): GetReportsUseCase {
    return GetReportsUseCase(documentsRepository)
}

fun createUploadReportFileUseCase(documentsRepository: DocumentsRepository): UploadReportsFileUseCase {
    return UploadReportsFileUseCase(documentsRepository)
}

fun createUploadAndShareDocumentUseCase(documentsRepository: DocumentsRepository): UploadAndShareDocumentUseCase {
    return UploadAndShareDocumentUseCase(documentsRepository)
}

fun createStaticDetailsRepository(apiService: ApiService): StaticDetailsRepository {
    return StaticDetailsRepositoryImp(apiService)
}

fun createStaticDetailsUseCase(staticDetailsRepository: StaticDetailsRepository): StaticDetailsUseCase {
    return StaticDetailsUseCase(staticDetailsRepository)
}

fun createPatientsRepository(apiService: ApiService): PatientsRepository {
    return PatientsRepositoryImp(apiService)
}

fun createGetPatientsListUseCase(patientsRepository: PatientsRepository): GetPatientsListUseCase {
    return GetPatientsListUseCase(patientsRepository)
}


fun createNotesRepository(apiService: ApiService):NotesRepository{
    return NotesRepositoryImp(apiService)
}
fun createGetNotesListUseCase(notesRepository: NotesRepository): GetNotesListUseCase {
    return GetNotesListUseCase(notesRepository)
}
fun createAddNotUseCase(notesRepository: NotesRepository): AddNotesUseCase {
    return AddNotesUseCase(notesRepository)
}

fun createNotificationsRepository(apiService: ApiService):NotificationsRepository{
    return NotificationsRepositoryImp(apiService)
}
fun createMuteNotificationUseCase(notificationsRepository: NotificationsRepository): MuteNotificationsUseCase {
    return MuteNotificationsUseCase(notificationsRepository)
}
fun createGetNotificationUseCase(notificationsRepository: NotificationsRepository): GetNotificationsUseCase {
    return GetNotificationsUseCase(notificationsRepository)
}
fun createMarkReadNotificationUseCase(notificationsRepository: NotificationsRepository): MarkReadNotificationUseCase {
    return MarkReadNotificationUseCase(notificationsRepository)
}