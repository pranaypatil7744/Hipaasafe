package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.CommonRequestModel
import com.hipaasafe.domain.model.get_patients.GetPatientsListResponseModel

interface PatientsRepository {
    suspend fun getPatientsListApi(request:CommonRequestModel):GetPatientsListResponseModel
}