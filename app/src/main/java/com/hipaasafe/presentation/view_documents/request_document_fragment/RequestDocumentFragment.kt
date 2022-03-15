package com.hipaasafe.presentation.view_documents.request_document_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentRequestDocumentBinding
import com.hipaasafe.domain.model.documents.GetReportsListRequestModel
import com.hipaasafe.domain.model.documents.RequestDocumentFromPatientRequestModel
import com.hipaasafe.presentation.comet_chat_main_screen.MainCometChatActivity
import com.hipaasafe.presentation.upload_documents.DocumentViewModel
import com.hipaasafe.presentation.view_documents.ViewDocumentsActivity
import com.hipaasafe.presentation.view_documents.request_document_fragment.adapter.RequestDocumentAdapter
import com.hipaasafe.presentation.view_documents.request_document_fragment.model.RequestDocumentModel
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class RequestDocumentFragment : BaseFragment(), RequestDocumentAdapter.RequestDocumentListener {

    lateinit var binding: FragmentRequestDocumentBinding
    lateinit var requestDocumentAdapter: RequestDocumentAdapter
    var requestDocumentList: ArrayList<RequestDocumentModel> = ArrayList()
    private val documentViewModel: DocumentViewModel by viewModel()
    var selectedDocList:ArrayList<Int> = ArrayList()
    var loginUserId:String = ""
    companion object {
        fun newInstance(): RequestDocumentFragment {
            return RequestDocumentFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preferenceUtils = PreferenceUtils(requireContext())
        getPreferenceData()
        setUpAdapter()
        setUpObserver()
        callRequestDocumentListApi()
        setUpListener()
    }

    private fun getPreferenceData() {
        binding.apply {
            loginUserId = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
        }
    }

    private fun setUpListener() {
        binding.apply {
            btnRequestDocument.setOnClickListener {
                val selectedDoc = requestDocumentList.filter {
                    it.isSelected
                }
                selectedDocList.clear()
                if (selectedDoc.isNotEmpty()){
                    for (i in selectedDoc){
                        selectedDocList.add(i.documentId)
                    }
                    callRequestDocumentFromPatientApi()
                }else{
                    showToast(getString(R.string.please_select_at_least_one_document))
                }
            }
        }
    }

    private fun getRequestDocumentFromPatientRequestModel():RequestDocumentFromPatientRequestModel{
        val request = RequestDocumentFromPatientRequestModel()
        request.patient_id = (requireActivity() as ViewDocumentsActivity).patientUid
        request.hospital_reports_id = selectedDocList
        return request
    }

    private fun callRequestDocumentFromPatientApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()){
                toggleLoader(true)
                documentViewModel.callRequestDocumentFromPatientApi(getRequestDocumentFromPatientRequestModel())
            }else{
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun setUpObserver() {
        binding.apply {
            with(documentViewModel) {
                getReportsListResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data?.isNotEmpty() == true) {
                            requestDocumentList.clear()
                            for (i in it.data ?: arrayListOf()) {
                                requestDocumentList.add(
                                    RequestDocumentModel(
                                        documentName = i.title.toString(),
                                        documentId = i.id
                                    )
                                )
                            }
                            if (::requestDocumentAdapter.isInitialized) {
                                requestDocumentAdapter.notifyDataSetChanged()
                            }
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                requestDocumentFromPatientResponseData.observe(requireActivity()){
                    toggleLoader(false)
                    if (it.success == true){
                            val viewDocumentsActivity = (requireActivity() as ViewDocumentsActivity)
                        viewDocumentsActivity.apply {
                            setFragment(viewDocumentsActivity.documentFragment)
                            isRequestedListView = false
                            showNotesView()
                        }
                    }else{
                        showToast(it.message.toString())
                    }
                }

                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun callRequestDocumentListApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerRequestDocument.visibility = VISIBLE
                documentViewModel.callGetReportListApi(
                    request = GetReportsListRequestModel(
                        doctor_id = loginUserId
                    )
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerRequestDocument.visibility = GONE
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun setUpAdapter() {
        binding.apply {
            requestDocumentAdapter = RequestDocumentAdapter(requireContext(), requestDocumentList,this@RequestDocumentFragment)
            recyclerRequestDocument.adapter = requestDocumentAdapter
        }
    }

    override fun clickOnDocument(position: Int) {
        val data = requestDocumentList[position]
        data.isSelected = !data.isSelected
        requestDocumentAdapter.notifyItemChanged(position)
    }
}