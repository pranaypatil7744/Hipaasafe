package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.FetchReportsResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class FetchReportsUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<FetchReportsResponseModel, Any?>() {
    override suspend fun run(params: Any?): FetchReportsResponseModel {
        return documentsRepository.callFetchReportsApi()
    }
}