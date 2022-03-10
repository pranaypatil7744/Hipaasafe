package com.hipaasafe.di

import com.hipaasafe.presentation.help.HelpViewModel
import com.hipaasafe.presentation.home_screen.appointment_fragment.AppointmentViewModel
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkViewModel
import com.hipaasafe.presentation.login.LoginViewModel
import com.hipaasafe.presentation.profile_edit_details.ProfileViewModel
import com.hipaasafe.presentation.upload_documents.DocumentViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val AppModule = module {
    viewModel { LoginViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProfileViewModel(get(),get()) }
    viewModel { MyNetworkViewModel(get()) }
    viewModel { HelpViewModel(get()) }
    viewModel { DocumentViewModel(get(),get(),get(),get()) }
    viewModel { AppointmentViewModel(get(), get()) }

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
    single { createModifyAppointmentUseCase(get()) }

    single { createDoctorsRepository(get(named("normalService"))) }
    single { createGetDoctorsUseCase(get()) }

    single { createReportsRepository(get(named("normalService"))) }
    single { createFetchReportsUseCase(get()) }
    single { createShareReportsUseCase(get()) }
    single { createGetReportsUseCase(get()) }
    single { createUploadReportFileUseCase(get()) }
    single { createUploadAndShareDocumentUseCase(get()) }

    single { createStaticDetailsRepository(get(named("normalService"))) }
    single { createStaticDetailsUseCase(get()) }

}