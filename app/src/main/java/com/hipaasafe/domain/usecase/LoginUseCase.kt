package com.hipaasafe.domain.usecase

import com.hipaasafe.domain.model.LoginRequestModel
import com.hipaasafe.domain.model.LoginResponseModel
import com.hipaasafe.domain.repository.LoginRepository
import com.hipaasafe.domain.usecase.base.UseCase

class LoginUseCase constructor(private val loginRepository: LoginRepository) :
    UseCase<LoginResponseModel, Any?>() {

    override suspend fun run(params: Any?): LoginResponseModel {
        return loginRepository.callLoginApi(params as LoginRequestModel)
    }
}