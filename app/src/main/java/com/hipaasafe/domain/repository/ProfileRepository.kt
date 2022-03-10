package com.hipaasafe.domain.repository

import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadResponseModel

interface ProfileRepository {

    suspend fun callPatientUpdateProfile(request:ProfileEditRequestModel):ProfileEditResponseModel

    suspend fun callUpdateProfilePicApi(request:ProfilePicUploadRequestModel):ProfilePicUploadResponseModel
}