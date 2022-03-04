package com.hipaasafe.domain.usecase.login

import com.hipaasafe.domain.model.patient_login.PatientSendOtpRequestModel
import com.hipaasafe.domain.model.patient_login.PatientSendOtpResponseModel
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.base.UseCase

class PatientSendOtpUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<PatientSendOtpResponseModel, Any?>() {
    override suspend fun run(params: Any?): PatientSendOtpResponseModel {
        return loginRepository.callPatientSendOtpApi(params as PatientSendOtpRequestModel)
    }
}