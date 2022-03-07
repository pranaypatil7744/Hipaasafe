package com.hipaasafe.di

import com.hipaasafe.presentation.login.LoginViewModel
import com.hipaasafe.presentation.profile_edit_details.ProfileViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val AppModule = module {
    viewModel { LoginViewModel(get(),get(),get(),get(),get()) }
    viewModel { ProfileViewModel(get()) }

    single { createLoginRepository(get(named("normalService"))) }
    single { createPatientSendOtpUseCase(get()) }
    single { createPatientValidateOtpUseCase(get()) }
    single { createDoctorLoginSendOtpUseCase(get()) }
    single { createDoctorLoginValidateOtpUseCase(get()) }
    single { createPatientRegisterUseCase(get()) }

    single { createProfileRepository(get(named("normalService"))) }
    single { createPatientUpdateProfileUseCase(get()) }


}