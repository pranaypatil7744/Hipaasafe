package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.static_details.GetStaticDetailsResponseModel

interface StaticDetailsRepository {

    suspend fun callStaticDetailsApi():GetStaticDetailsResponseModel
}