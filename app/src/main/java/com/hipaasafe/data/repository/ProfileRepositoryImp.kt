package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.repository.ProfileRepository
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel

class ProfileRepositoryImp constructor(private val apiService: ApiService) : ProfileRepository {
    override suspend fun callPatientUpdateProfile(request: ProfileEditRequestModel): ProfileEditResponseModel {
        return apiService.callPatientUpdateProfile(request)
    }

}