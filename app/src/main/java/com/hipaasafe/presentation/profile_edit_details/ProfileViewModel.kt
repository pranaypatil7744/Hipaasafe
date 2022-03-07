package com.hipaasafe.presentation.profile_edit_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.usecase.base.UseCase
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.profile.PatientUpdateProfileUseCase
import com.hipaasafe.listener.ValidationListener
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditRequestModel
import com.hipaasafe.presentation.profile_edit_details.model.ProfileEditResponseModel
import com.hipaasafe.utils.AppUtils

class ProfileViewModel constructor(
    private val patientUpdateProfileUseCase: PatientUpdateProfileUseCase
) : ViewModel() {

    var validationListener: ValidationListener? = null
    val patientUpdateProfileResponseData = MutableLiveData<ProfileEditResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callPatientUpdateProfileApi(request: ProfileEditRequestModel) {
        patientUpdateProfileUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ProfileEditResponseModel> {
                override fun onSuccess(result: ProfileEditResponseModel) {
                    patientUpdateProfileResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

    fun validateEditProfileData(request: ProfileEditRequestModel) {
        if (request.name.isNullOrEmpty()) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.NAME_ERROR,
                R.string.please_enter_name
            )
            return
        }
        if (request.email.isNullOrEmpty()) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.EMAIL_ERROR,
                R.string.please_enter_email_id
            )
            return
        }
        if (AppUtils.INSTANCE?.isValidEmail(request.email) == false) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.EMAIL_ERROR,
                R.string.please_enter_valid_email_id
            )
            return
        }
        if (request.number.isNullOrEmpty()) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.MOBILE_ERROR,
                R.string.please_enter_mobile_number
            )
            return
        }
        if (request.number.toString().length < 6) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.MOBILE_ERROR,
                R.string.please_enter_valid_mobile_number
            )
            return
        }
        if (request.age.isNullOrEmpty()) {
            validationListener?.onValidationFailure(
                Constants.ErrorMsg.AGE_ERROR,
                R.string.please_enter_age
            )
            return
        }
        validationListener?.onValidationSuccess(Constants.SUCCESS, R.string.success)
    }
}