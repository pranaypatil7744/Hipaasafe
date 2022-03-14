package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.documents.*
import com.hipaasafe.domain.repository.DocumentsRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*

class DocumentsRepositoryImp constructor(private val apiService: ApiService) : DocumentsRepository {
    override suspend fun callGetReportsList(request: GetReportsListRequestModel): GetReportsListResponseModel {
        return apiService.callGetReportsList(request.page, request.limit)
    }

    override suspend fun callUploadReportFileApi(request: UploadReportFileRequestModel): UploadReportFileResponseModel {
        val requestFile = request.user_reports.let {
            RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                it
            )
        }
        val body = requestFile.let {
            MultipartBody.Part.createFormData(
                "user_reports",
                if (!request.user_reports.exists()) "" else "" + Date().time + request.fileName,
                it
            )
        }
        return apiService.callUploadReportFileApi(body)
    }

    override suspend fun callUploadAndShareDocumentApi(request: UploadAndShareDocumentRequestModel): UploadAndShareDocumentResponseModel {
        return apiService.callUploadAndShareDocumentApi(request)
    }

    override suspend fun callFetchReportsApi(): FetchReportsResponseModel {
        return apiService.callFetchReportsApi()
    }

    override suspend fun callShareDocumentApi(request: ShareDocumentRequestModel): ShareDocumentResponseModel {
        return apiService.callShareDocumentApi(request)
    }

    override suspend fun callRequestDocumentFromPatientApi(request: RequestDocumentFromPatientRequestModel): RequestDocumentFromPatientResponseModel {
        return apiService.callRequestDocumentFromPatientsApi(request)
    }
}