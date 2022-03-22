package com.hipaasafe.presentation.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.notifications.GetNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.GetNotificationsResponseModel
import com.hipaasafe.domain.model.notifications.MuteNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.MuteNotificationsResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.notification.GetNotificationsUseCase
import com.hipaasafe.domain.usecase.notification.MuteNotificationsUseCase

class NotificationViewModel constructor(
    private val muteNotificationsUseCase: MuteNotificationsUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase
) :
    ViewModel() {

    val muteNotificationsResponseData = MutableLiveData<MuteNotificationsResponseModel>()
    val getNotificationsResponseData = MutableLiveData<GetNotificationsResponseModel>()
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

    fun callGetNotificationsApi(request: GetNotificationsRequestModel) {
        getNotificationsUseCase.invoke(
            viewModelScope, request,
            object : UseCaseResponse<GetNotificationsResponseModel> {
                override fun onSuccess(result: GetNotificationsResponseModel) {
                    getNotificationsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }
}
