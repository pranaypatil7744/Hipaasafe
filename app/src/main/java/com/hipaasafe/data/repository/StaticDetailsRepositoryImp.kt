package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.static_details.GetStaticDetailsResponseModel
import com.hipaasafe.domain.repository.StaticDetailsRepository

class StaticDetailsRepositoryImp constructor(private val apiService: ApiService) : StaticDetailsRepository {
    override suspend fun callStaticDetailsApi(): GetStaticDetailsResponseModel {
        return apiService.callStaticDetailsApi()
    }

}