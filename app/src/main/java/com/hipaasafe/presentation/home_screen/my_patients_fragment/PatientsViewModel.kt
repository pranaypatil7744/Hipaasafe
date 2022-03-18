package com.hipaasafe.presentation.home_screen.my_patients_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.CommonRequestModel
import com.hipaasafe.domain.model.get_patients.GetPatientsListRequestModel
import com.hipaasafe.domain.model.get_patients.GetPatientsListResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.patients.GetPatientsListUseCase

class PatientsViewModel constructor(private val getPatientsListUseCase: GetPatientsListUseCase) :
    ViewModel() {

    val patientsListResponseData = MutableLiveData<GetPatientsListResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callGetStaticDetailsApi(request: GetPatientsListRequestModel) {
        getPatientsListUseCase.invoke(
            viewModelScope, request,
            object : UseCaseResponse<GetPatientsListResponseModel> {
                override fun onSuccess(result: GetPatientsListResponseModel) {
                    patientsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }
}