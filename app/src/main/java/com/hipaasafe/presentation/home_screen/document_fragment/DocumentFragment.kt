package com.hipaasafe.presentation.home_screen.document_fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.databinding.FragmentDocumentBinding
import com.hipaasafe.domain.model.documents.ShareDocumentRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.DocumentAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkViewModel
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel
import com.hipaasafe.presentation.upload_documents.DocumentViewModel
import com.hipaasafe.presentation.upload_documents.UploadDocumentsActivity
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class DocumentFragment : BaseFragment(), DocumentAdapter.DocumentClickManager,
    ForwardDocAdapter.ForwardClickManager {

    lateinit var binding: FragmentDocumentBinding
    lateinit var documentAdapter: DocumentAdapter
    lateinit var forwardDocAdapter: ForwardDocAdapter
    private var documentsList: ArrayList<DocumentsModel> = ArrayList()
    private var doctorList: ArrayList<ForwardDocumentModel> = ArrayList()
    lateinit var bottomSheetForwardDocBinding: BottomsheetForwardDocBinding
    private val myNetworkViewModel: MyNetworkViewModel by viewModel()
    private val documentViewModel: DocumentViewModel by viewModel()
    var selectedItemPosition: Int = 0
    var isForPatientDocuments:Boolean = false

    companion object {
        fun newInstance(): DocumentFragment {
            return DocumentFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setUpObserver()
        callDoctorsApi()
        callFetchReportsApi()
        setUpListener()
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                callFetchReportsApi()
                callDoctorsApi()
            }
        }
    }

    private fun callFetchReportsApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerDocuments.visibility = VISIBLE
                documentViewModel.callFetchReportsApi()
            } else {
                layoutNoInternet.root.visibility = VISIBLE
                recyclerDocuments.visibility = GONE
            }
        }
    }

    private fun callDoctorsApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                myNetworkViewModel.callMyNetworkDoctorsListApi(
                    GetDoctorsRequestModel(page = 1, limit = 30)
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
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

    private fun setUpObserver() {
        binding.apply {
            with(myNetworkViewModel) {
                myNetworkListResponseData.observe(this@DocumentFragment.viewLifecycleOwner) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data.count != 0) {
                            doctorList.clear()
                            for (i in it.data.rows ?: arrayListOf()) {
                                doctorList.add(
                                    ForwardDocumentModel(
                                        title = i.list_doctor_details.name,
                                        icon = i.list_doctor_details.avatar,
                                        guid = i.guid
                                    )
                                )
                            }
                            if (::forwardDocAdapter.isInitialized) {
                                forwardDocAdapter.notifyDataSetChanged()
                            }
                        } else {

                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }
                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(documentViewModel) {
                fetchReportsResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        documentsList.clear()
                        documentsList.add(
                            DocumentsModel(
                                documentItemType = DocumentItemType.ITEM_ADD_DOC,
                                title = getString(R.string.upload_documents),
                                subTitle = getString(R.string.click_here_to_upload_reports_documents)
                            )
                        )
                        val data = it.data
                        if (data?.documents?.size != 0) {
                            for (i in it.data?.documents ?: arrayListOf()) {
                                documentsList.add(
                                    DocumentsModel(
                                        documentItemType = DocumentItemType.ITEM_UPLOADED_DOC,
                                        title = i.hospital_tests.title,
                                        uploadDocumentId = i.report_name_id ?: 0,
                                        uploadedFileName = i.document_file
                                    )
                                )
                            }
                        }
//                        documentsList.add(
//                            DocumentsModel(
//                                documentItemType = DocumentItemType.ITEM_TITLE,
//                                title = "03 Feb 2022"
//                            )
//                        )
                        documentsList.add(
                            DocumentsModel(
                                documentItemType = DocumentItemType.ITEM_TITLE
                            )
                        )

                        if (data?.documents_request?.size != 0) {
                            for (i in data?.documents_request ?: arrayListOf()) {
                                documentsList.add(
                                    DocumentsModel(
                                        documentItemType = DocumentItemType.ITEM_PENDING_DOC,
                                        title = i.hospital_tests.title,
                                        subTitle = i.assignee.name,
                                        uploadDocumentId = i.hospital_tests.id ?: 0,
                                        guid = i.assignee_id
                                    )
                                )
                            }
                        }
                        if (::documentAdapter.isInitialized) {
                            documentAdapter.notifyDataSetChanged()
                        }
                        if (documentsList.isEmpty()){
                            layoutNoData.root.visibility = VISIBLE
                        }else{
                            layoutNoData.root.visibility = GONE
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                shareReportsResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        showToast(it.message.toString())
                    } else {
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

    private fun setUpAdapter() {
        binding.apply {
            documentAdapter =
                DocumentAdapter(requireContext(), documentsList, this@DocumentFragment)
            recyclerDocuments.adapter = documentAdapter
        }
    }

    private fun openForwardListBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottomsheet_forward_doc, null)
        bottomSheetForwardDocBinding = BottomsheetForwardDocBinding.bind(view)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(true)
        bottomSheetForwardDocBinding.apply {
            forwardDocAdapter =
                ForwardDocAdapter(requireContext(), doctorList, listener = this@DocumentFragment)
            recyclerAttendanceHistory.adapter = forwardDocAdapter
            imgClose.setOnClickListener {
                bottomSheetDialog.dismiss()
                for (i in doctorList) {
                    i.isSelected = false
                }
                forwardDocAdapter.notifyDataSetChanged()
            }
            btnShare.setOnClickListener {
                val checkSelected = doctorList.find {
                    it.isSelected
                }
                if (checkSelected != null) {
                    bottomSheetDialog.dismiss()
                    callShareReportApi()
                } else {
                    showToast(getString(R.string.please_select_at_least_1_doctor))
                }
            }
        }
        bottomSheetDialog.show()
    }

    private fun getShareReportRequestModel(): ShareDocumentRequestModel {
        val selectedDoctorsIds: ArrayList<String> = ArrayList()
        val list = doctorList.filter {
            it.isSelected
        }
        if (list.isNotEmpty()) {
            for (i in list) {
                selectedDoctorsIds.add(i.guid.toString())
            }
        }
        val request = ShareDocumentRequestModel()
        request.document_file = documentsList[selectedItemPosition].uploadedFileName.toString()
        request.doctor_uids = selectedDoctorsIds
        return request
    }

    private fun callShareReportApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                documentViewModel.callShareReportsApi(getShareReportRequestModel())
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private val uploadReportResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                callFetchReportsApi()
            }
        }

    override fun clickOnAddDocument(position: Int) {
        selectedItemPosition = position
        val i = Intent(requireContext(), UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd, true)
        i.putExtras(bundle)
        uploadReportResult.launch(i)
    }

    override fun clickOnForwardDoc(position: Int) {
        selectedItemPosition = position
        openForwardListBottomSheet()
    }

    override fun clickOnPendingDoc(position: Int) {
        selectedItemPosition = position
        val i = Intent(requireContext(), UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd, false)
        bundle.putString(Constants.PendingDocumentName, documentsList[position].title)
        bundle.putString(Constants.PendingDocumentBy, documentsList[position].subTitle)
        bundle.putString(Constants.PendingDocumentGuid, documentsList[position].guid)
        bundle.putInt(Constants.PendingDocumentId, documentsList[position].uploadDocumentId)
        i.putExtras(bundle)
        uploadReportResult.launch(i)
    }

    override fun onItemClick(position: Int) {
        doctorList[position].isSelected = !doctorList[position].isSelected
        forwardDocAdapter.notifyItemChanged(position)
    }

}