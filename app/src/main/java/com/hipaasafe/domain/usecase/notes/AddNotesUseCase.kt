package com.hipaasafe.domain.usecase.notes

import com.hipaasafe.domain.model.notes.AddNoteRequestModel
import com.hipaasafe.domain.model.notes.AddNoteResponseModel
import com.hipaasafe.domain.repository.NotesRepository
import com.hipaasafe.domain.usecase.base.UseCase

class AddNotesUseCase constructor(private val notesRepository: NotesRepository) :
    UseCase<AddNoteResponseModel, Any?>() {
    override suspend fun run(params: Any?): AddNoteResponseModel {
        return notesRepository.callAddNoteApi(params as AddNoteRequestModel)
    }
}