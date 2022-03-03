package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.LoginRequestModel
import com.hipaasafe.domain.model.LoginResponseModel
import com.hipaasafe.domain.repository.LoginRepository

class LoginRepositoryImp(private val apiService: ApiService): LoginRepository {

    override suspend fun callLoginApi(request: LoginRequestModel): LoginResponseModel {
        return apiService.callLoginApi(request)
    }

}