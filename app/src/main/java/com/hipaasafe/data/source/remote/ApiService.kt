package com.hipaasafe.data.source.remote

import com.hipaasafe.domain.model.LoginRequestModel
import com.hipaasafe.domain.model.LoginResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST(ApiNames.LoginApi)
    suspend fun callLoginApi(@Body request: LoginRequestModel): LoginResponseModel
}