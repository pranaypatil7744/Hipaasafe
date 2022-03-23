package com.hipaasafe.presentation.notification

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.notifications.*
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.notification.GetNotificationsUseCase
import com.hipaasafe.domain.usecase.notification.MarkReadNotificationUseCase
import com.hipaasafe.domain.usecase.notification.MuteNotificationsUseCase

class NotificationViewModel constructor(
    private val muteNotificationsUseCase: MuteNotificationsUseCase,
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markReadNotificationUseCase: MarkReadNotificationUseCase
) :
    ViewModel() {

    val muteNotificationsResponseData = MutableLiveData<MuteNotificationsResponseModel>()
    val getNotificationsResponseData = MutableLiveData<GetNotificationsResponseModel>()
    val markReadNotificationsResponseData = MutableLiveData<MarkReadNotificationResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callMarkReadNotificationsApi(request: MarkReadNotificationRequestModel) {
        markReadNotificationUseCase.invoke(
            viewModelScope, request,
            object : UseCaseResponse<MarkReadNotificationResponseModel> {
                override fun onSuccess(result: MarkReadNotificationResponseModel) {
                    markReadNotificationsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }

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
