package com.hipaasafe.domain.repository

import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel

interface ProfileRepository {

    suspend fun callPatientUpdateProfile(request:ProfileEditRequestModel):ProfileEditResponseModel
}