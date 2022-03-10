package com.hipaasafe.presentation.upload_documents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.reports.*
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.documents.GetReportsUseCase
import com.hipaasafe.domain.usecase.documents.UploadAndShareDocumentUseCase
import com.hipaasafe.domain.usecase.documents.UploadReportsFileUseCase

class DocumentViewModel constructor(
    private val getReportsUseCase: GetReportsUseCase,
    private val uploadReportsFileUseCase: UploadReportsFileUseCase,
    private val uploadAndShareDocumentUseCase: UploadAndShareDocumentUseCase
) :
    ViewModel() {
    val getReportsListResponseData = MutableLiveData<GetReportsListResponseModel>()
    val uploadReportFileResponseData = MutableLiveData<UploadReportFileResponseModel>()
    val uploadAndShareDocumentResponseData = MutableLiveData<UploadAndShareDocumentResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callUploadAndShareDocumentApi(request: UploadAndShareDocumentRequestModel) {
        uploadAndShareDocumentUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<UploadAndShareDocumentResponseModel> {
                override fun onSuccess(result: UploadAndShareDocumentResponseModel) {
                    uploadAndShareDocumentResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

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