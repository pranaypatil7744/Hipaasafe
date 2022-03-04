package com.hipaasafe.domain.usecase.login

import com.hipaasafe.domain.model.patient_login.PatientValidateOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientValidateOtpResponseModel
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.base.UseCase

class PatientValidateOtpUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<PatientValidateOtpResponseModel, Any?>() {
    override suspend fun run(params: Any?): PatientValidateOtpResponseModel {
        return loginRepository.callPatientValidateOtpApi(params as PatientValidateOtpRequestModel)
    }
}