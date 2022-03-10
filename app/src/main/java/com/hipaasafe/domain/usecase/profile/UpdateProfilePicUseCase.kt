package com.hipaasafe.domain.usecase.profile

import com.hipaasafe.domain.repository.ProfileRepository
import com.hipaasafe.domain.usecase.base.UseCase
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadResponseModel

class UpdateProfilePicUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<ProfilePicUploadResponseModel, Any?>() {
    override suspend fun run(params: Any?): ProfilePicUploadResponseModel {
        return profileRepository.callUpdateProfilePicApi(params as ProfilePicUploadRequestModel)
    }
}