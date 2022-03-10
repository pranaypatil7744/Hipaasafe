package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.repository.ProfileRepository
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfilePicUploadResponseModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*

class ProfileRepositoryImp constructor(private val apiService: ApiService) : ProfileRepository {
    override suspend fun callPatientUpdateProfile(request: ProfileEditRequestModel): ProfileEditResponseModel {
        return apiService.callPatientUpdateProfile(request)
    }

    override suspend fun callUpdateProfilePicApi(request: ProfilePicUploadRequestModel): ProfilePicUploadResponseModel {
        val requestFile = request.profile_pic.let {
            RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                it
            )
        }
        val body = requestFile.let {
            MultipartBody.Part.createFormData(
                "profile_pic",
                if (!request.profile_pic.exists()) "" else "" + Date().time + request.fileName,
                it
            )
        }
        return apiService.callUpdateProfilePicApi(body)
    }

}