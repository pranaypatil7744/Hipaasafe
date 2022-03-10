package com.hipaasafe.domain.usecase.doctors

import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsResponseModel
import com.hipaasafe.domain.repository.DoctorsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetDoctorsUseCase constructor(private val doctorsRepository: DoctorsRepository) :
    UseCase<GetDoctorsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetDoctorsResponseModel {
        return doctorsRepository.callGetDoctorsListApi(params as GetDoctorsRequestModel)
    }
}