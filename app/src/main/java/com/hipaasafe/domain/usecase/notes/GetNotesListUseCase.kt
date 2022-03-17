package com.hipaasafe.domain.usecase.notes

import com.hipaasafe.domain.model.notes.GetNotesListRequestModel
import com.hipaasafe.domain.model.notes.GetNotesListResponseModel
import com.hipaasafe.domain.repository.NotesRepository
import com.hipaasafe.domain.usecase.base.UseCase

class GetNotesListUseCase constructor(private val notesRepository: NotesRepository) :
    UseCase<GetNotesListResponseModel, Any?>() {
    override suspend fun run(params: Any?): GetNotesListResponseModel {
        return notesRepository.callGetNotesListApi(params as GetNotesListRequestModel)
    }
}