package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.reports.*

interface DocumentsRepository {

    suspend fun callGetReportsList(request: GetReportsListRequestModel): GetReportsListResponseModel

    suspend fun callUploadReportFileApi(request: UploadReportFileRequestModel): UploadReportFileResponseModel

    suspend fun callUploadAndShareDocumentApi(request: UploadAndShareDocumentRequestModel): UploadAndShareDocumentResponseModel
}