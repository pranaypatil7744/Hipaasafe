package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.UploadReportFileRequestModel
import com.hipaasafe.domain.model.documents.UploadReportFileResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class UploadReportsFileUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<UploadReportFileResponseModel, Any?>() {
    override suspend fun run(params: Any?): UploadReportFileResponseModel {
        return documentsRepository.callUploadReportFileApi(params as UploadReportFileRequestModel)
    }
}