package com.hipaasafe.di

import com.hipaasafe.presentation.help.HelpViewModel
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkViewModel
import com.hipaasafe.presentation.home_screen.my_patients_fragment.PatientsViewModel
import com.hipaasafe.presentation.login.LoginViewModel
import com.hipaasafe.presentation.notification.NotificationViewModel
import com.hipaasafe.presentation.profile_edit_details.ProfileViewModel
import com.hipaasafe.presentation.upload_documents.DocumentViewModel
import com.hipaasafe.presentation.view_documents.NotesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val AppModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(),get()) }
    viewModel { MyNetworkViewModel(get(),get()) }
    viewModel { NotesViewModel(get(),get()) }
    viewModel { HelpViewModel(get()) }
    viewModel { PatientsViewModel(get()) }
    viewModel { DocumentViewModel(get(),get(),get(),get(),get(),get(),get()) }
    viewModel { AppointmentViewModel(get(), get(),get(),get(),get(),get(),get(),get()) }
    viewModel { NotificationViewModel(get(),get(),get()) }

    single { createLoginRepository(get(named("normalService"))) }
    single { createPatientSendOtpUseCase(get()) }
    single { createPatientValidateOtpUseCase(get()) }
    single { createDoctorLoginSendOtpUseCase(get()) }
    single { createDoctorLoginValidateOtpUseCase(get()) }
    single { createPatientRegisterUseCase(get()) }

    single { createProfileRepository(get(named("normalService"))) }
    single { createPatientUpdateProfileUseCase(get()) }
    single { createUpdateProfilePicUseCase(get()) }

    single { createAppointmentRepository(get(named("normalService"))) }
    single { createGetAppointmentsUseCase(get()) }
    single { createGetDoctorPastAppointmentsUseCase(get()) }
    single { createGetMyQueueUseCase(get()) }
    single { createModifyAppointmentUseCase(get()) }
    single { createAddAppointmentUseCase(get()) }
    single { createDoctorAppointmentsListUseCase(get()) }
    single { createStopMyQueueUseCase(get()) }
    single { createDoctorAppointmentCountUseCase(get()) }

    single { createDoctorsRepository(get(named("normalService"))) }
    single { DoctorMyTeamsListUseCase(get()) }
    single { createGetDoctorsUseCase(get()) }

    single { createReportsRepository(get(named("normalService"))) }
    single { createFetchReportsUseCase(get()) }
    single { createShareReportsUseCase(get()) }
    single { createGetReportsUseCase(get()) }
    single { createRequestDocumentFromPatientUseCase(get()) }
    single { createUploadReportFileUseCase(get()) }
    single { createUploadAndShareDocumentUseCase(get()) }
    single { createRemoveRequestDocUseCase(get()) }

    single { createStaticDetailsRepository(get(named("normalService"))) }
    single { createStaticDetailsUseCase(get()) }

    single { createPatientsRepository(get(named("normalService"))) }
    single { createGetPatientsListUseCase(get()) }

    single { createNotesRepository(get(named("normalService"))) }
    single { createGetNotesListUseCase(get()) }
    single { createAddNotUseCase(get()) }

    single { createNotificationsRepository(get(named("normalService"))) }
    single { createMuteNotificationUseCase(get()) }
    single { createGetNotificationUseCase(get()) }
    single { createMarkReadNotificationUseCase(get()) }

}