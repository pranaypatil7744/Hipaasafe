package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsResponseModel

interface DoctorsRepository {

    suspend fun callGetDoctorsListApi(request:GetDoctorsRequestModel):GetDoctorsResponseModel
}