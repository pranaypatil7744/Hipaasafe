package com.hipaasafe.presentation.view_documents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hipaasafe.domain.exception.ApiError
import com.hipaasafe.domain.model.notes.AddNoteRequestModel
import com.hipaasafe.domain.model.notes.AddNoteResponseModel
import com.hipaasafe.domain.model.notes.GetNotesListRequestModel
import com.hipaasafe.domain.model.notes.GetNotesListResponseModel
import com.hipaasafe.domain.usecase.base.UseCaseResponse
import com.hipaasafe.domain.usecase.notes.AddNotesUseCase
import com.hipaasafe.domain.usecase.notes.GetNotesListUseCase

class NotesViewModel constructor(
    private val addNotesUseCase: AddNotesUseCase,
    private val getNotesListUseCase: GetNotesListUseCase
) : ViewModel() {
    val addNotesResponseData = MutableLiveData<AddNoteResponseModel>()
    val getNotesListResponseData = MutableLiveData<GetNotesListResponseModel>()
    val messageData = MutableLiveData<String>()

    fun callAddNoteApi(request:AddNoteRequestModel) {
        addNotesUseCase.invoke(
            viewModelScope, request,
            object : UseCaseResponse<AddNoteResponseModel> {
                override fun onSuccess(result: AddNoteResponseModel) {
                    addNotesResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }

    fun callGetNotesListApi(request:GetNotesListRequestModel) {
        getNotesListUseCase.invoke(
            viewModelScope, request,
            object : UseCaseResponse<GetNotesListResponseModel> {
                override fun onSuccess(result: GetNotesListResponseModel) {
                    getNotesListResponseData.value = result
                }

                override fun onError(apiError: ApiError?) {
                    messageData.value = apiError?.message
                }
            })
    }
}