package com.hipaasafe.domain.repository

import com.hipaasafe.domain.model.documents.*

interface DocumentsRepository {

    suspend fun callGetReportsList(request: GetReportsListRequestModel): GetReportsListResponseModel

    suspend fun callUploadReportFileApi(request: UploadReportFileRequestModel): UploadReportFileResponseModel

    suspend fun callUploadAndShareDocumentApi(request: UploadAndShareDocumentRequestModel): UploadAndShareDocumentResponseModel

    suspend fun callFetchReportsApi():FetchReportsResponseModel

    suspend fun callShareDocumentApi(request:ShareDocumentRequestModel):ShareDocumentResponseModel
}