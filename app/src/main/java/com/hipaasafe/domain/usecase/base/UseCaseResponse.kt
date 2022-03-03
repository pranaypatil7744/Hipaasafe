package com.hipaasafe.domain.usecase.base

import com.hipaasafe.domain.exception.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result:Type)

    fun onError(apiError: ApiError?)
}