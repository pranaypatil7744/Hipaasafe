package com.hipaasafe.data.repository

import com.hipaasafe.data.source.remote.ApiService
import com.hipaasafe.domain.model.reports.GetReportsListRequestModel
import com.hipaasafe.domain.model.reports.GetReportsListResponseModel
import com.hipaasafe.domain.model.reports.UploadReportFileRequestModel
import com.hipaasafe.domain.model.reports.UploadReportFileResponseModel
import com.hipaasafe.domain.repository.ReportsRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*

class ReportsRepositoryImp constructor(private val apiService: ApiService) : ReportsRepository {
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
                if (!request.user_reports.exists()) "" else ""+ Date().time+ request.fileName,
                it
            )
        }
        return apiService.callUploadReportFileApi(body)
    }
}