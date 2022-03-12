package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.get_doctors.DoctorMyTeamResponseModel
import com.hipaasafe.domain.model.get_doctors.DoctorMyTeamsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsResponseModel
import com.hipaasafe.domain.repository.DoctorsRepository

class DoctorsRepositoryImp constructor(private val apiService: ApiService) : DoctorsRepository {
    override suspend fun callGetDoctorsListApi(request: GetDoctorsRequestModel): GetDoctorsResponseModel {
        return apiService.callGetDoctorsListApi(request.page, request.limit)
    }

    override suspend fun callDoctorMyTeamsListApi(request: DoctorMyTeamsRequestModel): DoctorMyTeamResponseModel {
        return apiService.callDoctorMyTeamsListApi(page = request.page, request.limit)
    }
}