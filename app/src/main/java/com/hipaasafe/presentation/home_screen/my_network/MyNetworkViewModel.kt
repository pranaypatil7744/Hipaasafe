package com.hipaasafe.presentation.home_screen.my_network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.doctors.GetDoctorsUseCase

class MyNetworkViewModel constructor(private val getDoctorsUseCase: GetDoctorsUseCase) :
    ViewModel() {

    val myNetworkListResponseData = MutableLiveData<GetDoctorsResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callPatientUpdateProfileApi(request: GetDoctorsRequestModel) {
        getDoctorsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetDoctorsResponseModel> {
                override fun onSuccess(result: GetDoctorsResponseModel) {
                    myNetworkListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }
}