package com.hipaasafe.domain.usecase.static_details

import com.hipaasafe.domain.model.static_details.GetStaticDetailsResponseModel
import com.hipaasafe.domain.repository.StaticDetailsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class StaticDetailsUseCase constructor(private val staticDetailsRepository: StaticDetailsRepository) :
    UseCase<GetStaticDetailsResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetStaticDetailsResponseModel {
        return staticDetailsRepository.callStaticDetailsApi()
    }
}