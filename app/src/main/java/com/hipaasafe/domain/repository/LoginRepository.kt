package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.LoginRequestModel
import com.hipaasafe.domain.model.LoginResponseModel

interface LoginRepository {

    suspend fun callLoginApi(request: LoginRequestModel): LoginResponseModel


}