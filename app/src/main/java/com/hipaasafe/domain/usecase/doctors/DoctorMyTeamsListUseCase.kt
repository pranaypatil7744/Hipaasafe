package com.hipaasafe.domain.usecase.doctors

import com.hipaasafe.domain.model.get_doctors.DoctorMyTeamResponseModel
import com.hipaasafe.domain.model.get_doctors.DoctorMyTeamsRequestModel
import com.hipaasafe.domain.repository.DoctorsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class DoctorMyTeamsListUseCase constructor(private val doctorsRepository: DoctorsRepository) :
    UseCase<DoctorMyTeamResponseModel, Any?>() {
    override suspend fun run(params: Any?): DoctorMyTeamResponseModel {
        return doctorsRepository.callDoctorMyTeamsListApi(params as DoctorMyTeamsRequestModel)
    }
}