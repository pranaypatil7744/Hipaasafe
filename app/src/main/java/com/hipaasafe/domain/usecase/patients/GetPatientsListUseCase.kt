package com.hipaasafe.domain.usecase.patients

import com.hipaasafe.domain.model.CommonRequestModel
import com.hipaasafe.domain.model.get_patients.GetPatientsListResponseModel
import com.hipaasafe.domain.repository.PatientsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetPatientsListUseCase constructor(private val patientsRepository: PatientsRepository) :
    UseCase<GetPatientsListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetPatientsListResponseModel {
        return patientsRepository.getPatientsListApi(params as CommonRequestModel)
    }
}