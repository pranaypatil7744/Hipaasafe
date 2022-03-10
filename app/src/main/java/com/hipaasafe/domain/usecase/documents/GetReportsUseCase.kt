package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.GetReportsListRequestModel
import com.hipaasafe.domain.model.documents.GetReportsListResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetReportsUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<GetReportsListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetReportsListResponseModel {
        return documentsRepository.callGetReportsList(params as GetReportsListRequestModel)
    }
}