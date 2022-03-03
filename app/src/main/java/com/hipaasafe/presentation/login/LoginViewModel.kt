package com.hipaasafe.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.LoginRequestModel
import com.hipaasafe.domain.model.LoginResponseModel
import com.hipaasafe.domain.usecase.LoginUseCase
import com.hipaasafe.domain.usecase.base.UseCaseResponse

class LoginViewModel constructor(
    private val loginUseCase: LoginUseCase
):ViewModel() {

    val loginResponseData = MutableLiveData<LoginResponseModel>()
    val showProgressbar = MutableLiveData<Boolean>()
    val messageData = MutableLiveData<String>()

    fun callLoginApi(request: LoginRequestModel){
        showProgressbar.value = true
        loginUseCase.invoke(viewModelScope,request,object : UseCaseResponse<LoginResponseModel> {
            override fun onSuccess(result: LoginResponseModel) {
                loginResponseData.value = result
                showProgressbar.value = false
            }

            override fun onError(apiError: ApiError?) {
                messageData.value = apiError?.getErrorMessage()
                showProgressbar.value = false
            }

        })
    }

}