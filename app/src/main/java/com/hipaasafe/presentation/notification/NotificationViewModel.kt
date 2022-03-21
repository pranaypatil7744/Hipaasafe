package com.hipaasafe.presentation.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.notifications.MuteNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.MuteNotificationsResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.notification.MuteNotificationsUseCase

class NotificationViewModel constructor(private val muteNotificationsUseCase: MuteNotificationsUseCase) :
    ViewModel() {

    val muteNotificationsResponseData = MutableLiveData<MuteNotificationsResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callMuteNotificationsApi(request: MuteNotificationsRequestModel) {
        muteNotificationsUseCase.invoke(
            viewModelScope, request,
            object : UseCaseResponse<MuteNotificationsResponseModel> {
                override fun onSuccess(result: MuteNotificationsResponseModel) {
                    muteNotificationsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }
}