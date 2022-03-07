package com.hipaasafe.domain.usecase.profile

import com.hipaasafe.domain.repository.ProfileRepository
import com.hipaasafe.domain.usecase.base.UseCase
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel

class PatientUpdateProfileUseCase constructor(private val profileRepository: ProfileRepository) :
    UseCase<ProfileEditResponseModel, Any?>() {
    override suspend fun run(params: Any?): ProfileEditResponseModel {
        return profileRepository.callPatientUpdateProfile(params as ProfileEditRequestModel)
    }
}