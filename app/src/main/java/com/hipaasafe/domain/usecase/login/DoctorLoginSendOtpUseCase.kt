package com.hipaasafe.domain.usecase.login

import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginSendOtpResponseModel
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.base.UseCase

class DoctorLoginSendOtpUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<DoctorLoginSendOtpResponseModel, Any?>() {
    override suspend fun run(params: Any?): DoctorLoginSendOtpResponseModel {
        return loginRepository.callDoctorLoginSendOtpApi(params as DoctorLoginSendOtpRequestModel)
    }
}