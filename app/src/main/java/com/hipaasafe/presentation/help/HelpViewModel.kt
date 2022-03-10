package com.hipaasafe.presentation.help

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.static_details.GetStaticDetailsResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.static_details.StaticDetailsUseCase

class HelpViewModel constructor(private val staticDetailsUseCase: StaticDetailsUseCase) : ViewModel() {

    val helpDetailsResponseData = MutableLiveData<GetStaticDetailsResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callGetStaticDetailsApi() {
        staticDetailsUseCase.invoke(
            viewModelScope, null,
            object : UseCaseResponse<GetStaticDetailsResponseModel> {
                override fun onSuccess(result: GetStaticDetailsResponseModel) {
                    helpDetailsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }
}