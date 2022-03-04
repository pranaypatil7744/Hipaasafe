package com.hipaasafe.domain.usecase.login

import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpRequestModel
import com.hipaasafe.domain.model.doctor_login.DoctorLoginValidateOtpResponseModel
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.base.UseCase

class DoctorLoginValidationOtpUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<DoctorLoginValidateOtpResponseModel, Any?>() {
    override suspend fun run(params: Any?): DoctorLoginValidateOtpResponseModel {
        return loginRepository.callDoctorLoginValidateOtpApi(params as DoctorLoginValidateOtpRequestModel)
    }
}