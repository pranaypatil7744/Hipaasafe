package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.CommonRequestModel
import com.hipaasafe.domain.model.get_patients.GetPatientsListResponseModel
import com.hipaasafe.domain.repository.PatientsRepository

class PatientsRepositoryImp constructor(private val apiService: ApiService) : PatientsRepository {
    override suspend fun getPatientsListApi(request: CommonRequestModel): GetPatientsListResponseModel {
        return apiService.callPatientsListApi(request.page, request.limit)
    }
}