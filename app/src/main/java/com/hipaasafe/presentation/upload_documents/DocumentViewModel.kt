package com.hipaasafe.presentation.upload_documents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.documents.*
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.documents.*

class DocumentViewModel constructor(
    private val getReportsUseCase: GetReportsUseCase,
    private val uploadReportsFileUseCase: UploadReportsFileUseCase,
    private val uploadAndShareDocumentUseCase: UploadAndShareDocumentUseCase,
    private val fetchReportsUseCase: FetchReportsUseCase,
    private val shareDocumentUseCase: ShareDocumentUseCase,
    private val requestDocumentFromPatientUseCase: RequestDocumentFromPatientUseCase
) :
    ViewModel() {
    val getReportsListResponseData = MutableLiveData<GetReportsListResponseModel>()
    val uploadReportFileResponseData = MutableLiveData<UploadReportFileResponseModel>()
    val uploadAndShareDocumentResponseData = MutableLiveData<UploadAndShareDocumentResponseModel>()
    val fetchReportsResponseData = MutableLiveData<FetchReportsResponseModel>()
    val requestDocumentFromPatientResponseData =
        MutableLiveData<RequestDocumentFromPatientResponseModel>()
    val shareReportsResponseData = MutableLiveData<ShareDocumentResponseModel>()
    val messageData = MutableLiveData<String>()


    fun callRequestDocumentFromPatientApi(request: RequestDocumentFromPatientRequestModel) {
        requestDocumentFromPatientUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<RequestDocumentFromPatientResponseModel> {
                override fun onSuccess(result: RequestDocumentFromPatientResponseModel) {
                    requestDocumentFromPatientResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

    fun callShareReportsApi(request: ShareDocumentRequestModel) {
        shareDocumentUseCase.invoke(
            viewModelScope,
            request,
            object : UseCaseResponse<ShareDocumentResponseModel> {
                override fun onSuccess(result: ShareDocumentResponseModel) {
                    shareReportsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

    fun callFetchReportsApi() {
        fetchReportsUseCase.invoke(
            viewModelScope,
            null,
            object : UseCaseResponse<FetchReportsResponseModel> {
                override fun onSuccess(result: FetchReportsResponseModel) {
                    fetchReportsResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }

            })
    }

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