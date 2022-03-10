package com.hipaasafe.domain.usecase.documents

import com.hipaasafe.domain.model.documents.ShareDocumentRequestModel
import com.hipaasafe.domain.model.documents.ShareDocumentResponseModel
import com.hipaasafe.domain.repository.DocumentsRepository
import com.hipaasafe.domain.usecase.base.UseCase

class ShareDocumentUseCase constructor(private val documentsRepository: DocumentsRepository) :
    UseCase<ShareDocumentResponseModel, Any?>() {
    override suspend fun run(params: Any?): ShareDocumentResponseModel {
        return documentsRepository.callShareDocumentApi(params as ShareDocumentRequestModel)
    }
}