package com.hipaasafe.domain.usecase.login

import com.hipaasafe.domain.model.patient_login.PatientRegisterRequestModel
import com.hipaasafe.domain.model.patient_login.PatientRegisterResponseModel
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.base.UseCase

class PatientRegisterUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<PatientRegisterResponseModel, Any?>() {
    override suspend fun run(params: Any?): PatientRegisterResponseModel {
        return loginRepository.callPatientRegisterApi(params as PatientRegisterRequestModel)
    }
}