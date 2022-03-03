package com.hipaasafe.di

import com.hipaasafe.presentation.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val AppModule = module {
    viewModel { LoginViewModel(get()) }

    single { createLoginRepository(get(named("normalService"))) }
    single { createLoginUseCase(get()) }

}