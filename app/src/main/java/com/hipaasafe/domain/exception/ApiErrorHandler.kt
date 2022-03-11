package com.hipaasafe.domain.exception

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hipaasafe.domain.model.ApiErrorModel
import retrofit2.HttpException
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException


fun traceErrorException(throwable: Throwable?): ApiError {

    return when (throwable) {

        is HttpException -> {
            val apiMessageString = throwable.response()?.errorBody()?.string()
            val collectionType: Type = object : TypeToken<ApiErrorModel>() {}.type
            val response = Gson().fromJson<ApiErrorModel>(apiMessageString, collectionType)
            when (throwable.code()) {
                400 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.BAD_REQUEST
                )
                401 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.UNAUTHORIZED
                )
                403 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.FORBIDDEN
                )
                404 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.NOT_FOUND
                )
                405 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.METHOD_NOT_ALLOWED
                )
                406 -> {
                    ApiError(
                        response.message ?: throwable.message(),
                        throwable.code(),
                        ApiError.ErrorStatus.NOT_ACCEPTABLE
                    )
                }
                409 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.CONFLICT
                )
                500 -> ApiError(
                    response.message ?: throwable.message(),
                    throwable.code(),
                    ApiError.ErrorStatus.INTERNAL_SERVER_ERROR
                )
                else -> ApiError(
                    response.message ?:UNKNOWN_ERROR_MESSAGE,
                    0,
                    ApiError.ErrorStatus.UNKNOWN_ERROR
                )
            }
        }

        is SocketTimeoutException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.TIMEOUT)
        }

        is IOException -> {
            ApiError(throwable.message, ApiError.ErrorStatus.NO_CONNECTION)
        }

        else -> ApiError(UNKNOWN_ERROR_MESSAGE, 0, ApiError.ErrorStatus.UNKNOWN_ERROR)
    }
}