package com.hipaasafe.presentation.upload_documents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.reports.GetReportsListRequestModel
import com.hipaasafe.domain.model.reports.GetReportsListResponseModel
import com.hipaasafe.domain.model.reports.UploadReportFileRequestModel
import com.hipaasafe.domain.model.reports.UploadReportFileResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.reports.GetReportsUseCase
import com.hipaasafe.domain.usecase.reports.UploadReportsFileUseCase

class DocumentViewModel constructor(
    private val getReportsUseCase: GetReportsUseCase,
    private val uploadReportsFileUseCase: UploadReportsFileUseCase
) :
    ViewModel() {

    val getReportsListResponseData = MutableLiveData<GetReportsListResponseModel>()
    val uploadReportFileResponseData = MutableLiveData<UploadReportFileResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callUploadReportFileApi(request: UploadReportFileRequestModel) {
        uploadReportsFileUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UploadReportFileResponseModel> {
                override fun onSuccess(result: UploadReportFileResponseModel) {
                    uploadReportFileResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

    fun callGetReportListApi(request: GetReportsListRequestModel) {
        getReportsUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<GetReportsListResponseModel> {
                override fun onSuccess(result: GetReportsListResponseModel) {
                    getReportsListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }
}