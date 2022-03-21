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
import com.hipaasafe.domain.model.documents.FetchReportsRequestModel
import com.hipaasafe.domain.model.documents.RemoveRequestDocumentRequestModel
import com.hipaasafe.domain.model.documents.ShareDocumentRequestModel
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.DocumentAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkViewModel
import com.hipaasafe.presentation.upload_documents.DocumentViewModel
import com.hipaasafe.presentation.upload_documents.UploadDocumentsActivity
import com.hipaasafe.presentation.view_documents.ViewDocumentsActivity
import com.hipaasafe.utils.PreferenceUtils
import com.hipaasafe.utils.enum.LoginUserType
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
    var isForPatientDocuments: Boolean = false
    var patientUid: String = ""
    var selectedDoctorUid: String = ""
    var selectedDoctorUidForList: String = ""
    var isAttachmentFlow: Boolean = false
    var attachmentShareTo: String = ""
    var isShowUploadDoc: Boolean = true
    var guid:String =""
    var loginUserType:Int = 0

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
        preferenceUtils = PreferenceUtils(requireContext())
        getPreferenceData()
        setUpAdapter()
        setUpObserver()
        setUpListener()
        if (isForPatientDocuments){
            guid = (requireActivity() as ViewDocumentsActivity).groupId
        }else{
            callDoctorsApi()
        }
        callFetchReportsApi()
    }

    private fun getPreferenceData() {
        binding.apply {
            loginUserType = preferenceUtils.getValue(Constants.PreferenceKeys.role_id).toIntOrNull()?:0
            if (loginUserType == LoginUserType.PATIENT.value){
                patientUid = preferenceUtils.getValue(Constants.PreferenceKeys.uid)
            }
        }
    }

    private fun setUpListener() {
        binding.apply {
            layoutNoInternet.btnRetry.setOnClickListener {
                callFetchReportsApi()
            }
//            swipeMyPatient.setOnRefreshListener {
//                swipeMyPatient.isRefreshing = false
//                callFetchReportsApi()
//            }
        }
    }

     fun callFetchReportsApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                recyclerDocuments.visibility = VISIBLE
                documentViewModel.callFetchReportsApi(
                    request = FetchReportsRequestModel(
                        patient_id = patientUid.ifEmpty { null },guid = guid.ifEmpty { null }
                    )
                )
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
                                        guid = i.guid,
                                        doctorId = i.doctor_id
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

//                doctorMyTeamsListResponseData.observe(requireActivity()) {
//                    toggleLoader(false)
//                    if (it.success == true) {
//                        if (it.data.count != 0) {
//                            doctorList.clear()
//                            for (i in it.data.rows ?: arrayListOf()) {
//                                doctorList.add(
//                                    ForwardDocumentModel(
//                                        title = i.name,
//                                        icon = i.avatar,
//                                        guid = i.uid,
//                                        doctorId = i.doctor_details?.id.toString()
//                                    )
//                                )
//                            }
//                            if (::forwardDocAdapter.isInitialized) {
//                                forwardDocAdapter.notifyDataSetChanged()
//                            }
//                        } else {
//
//                        }
//                    } else {
//                        showToast(it.message.toString())
//                    }
//                }

                messageData.observe(requireActivity()) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }

            with(documentViewModel) {
                removeRequestDocResponseData.observe(requireActivity()){
                    toggleLoader(false)
                    if (it.success == true){
                        callFetchReportsApi()
                    }else{
                        showToast(it.message.toString())
                    }
                }

                fetchReportsResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        documentsList.clear()
                        if (isForPatientDocuments) {
                            documentsList.add(
                                DocumentsModel(
                                    documentItemType = DocumentItemType.ITEM_TITLE,
                                    title = getString(R.string.documents)
                                )
                            )
                        } else {
                            documentsList.add(
                                DocumentsModel(
                                    documentItemType = DocumentItemType.ITEM_ADD_DOC,
                                    title = getString(R.string.upload_documents),
                                    subTitle = getString(R.string.click_here_to_upload_reports_documents)
                                )
                            )
                        }
                        val data = it.data
                        if (data?.documents?.size != 0) {
                            for (i in it.data?.documents ?: arrayListOf()) {
                                documentsList.add(
                                    DocumentsModel(
                                        title = i.document_file,
                                        documentItemType = DocumentItemType.ITEM_UPLOADED_DOC,
                                        uploadDocumentId = i.report_name_id ?: 0,
                                        uploadedFileName = i.document_name,
                                        uploadedFileBy = i.doctor_details.name,
                                        uploadedFileType = i.hospital_tests.title
                                    )
                                )
                            }
                        }

                        if (data?.documents_request?.size != 0) {
                            if (isForPatientDocuments) {
                                documentsList.add(
                                    DocumentsModel(
                                        documentItemType = DocumentItemType.ITEM_TITLE,
                                        title = getString(R.string.requested_documents)
                                    )
                                )
                            } else {
                                documentsList.add(
                                    DocumentsModel(
                                        documentItemType = DocumentItemType.ITEM_TITLE
                                    )
                                )
                            }
                            for (i in data?.documents_request ?: arrayListOf()) {
                                documentsList.add(
                                    DocumentsModel(
                                        documentItemType = if (isForPatientDocuments) DocumentItemType.ITEM_REQUEST_DOC else DocumentItemType.ITEM_PENDING_DOC,
                                        title = i.hospital_tests.title,
                                        subTitle = i.assignee.name,
                                        uploadDocumentId = i.hospital_tests.id ?: 0,
                                        guid = i.assignee.uid,
                                        doctorId = i.assignee_id,
                                        DocumentRequestId = i.id ?: 0
                                    )
                                )
                            }
                        }
                        if (::documentAdapter.isInitialized) {
                            documentAdapter.notifyDataSetChanged()
                        }
                        if (data?.documents_request?.size == 0 && data.documents.size == 0) {
                            layoutNoData.root.visibility = VISIBLE
                        } else {
                            layoutNoData.root.visibility = GONE
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                shareReportsResponseData.observe(requireActivity()) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (isAttachmentFlow) {
//                            val data = getShareReportRequestModel()
//                            val resultIntent = requireActivity().intent
//                            val b = Bundle()
//                            b.putString(
//                                Constants.DocumentLink,
//                                Constants.BASE_URL_REPORT + data.document_file
//                            )
//                            resultIntent.putExtras(b)
//                            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
//                            requireActivity().finish()
                        } else {
                            showToast(it.message.toString())
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
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            if (isShowUploadDoc) {
                documentsList.clear()
                documentsList.add(
                    DocumentsModel(
                        documentItemType = DocumentItemType.ITEM_ADD_DOC,
                        title = getString(R.string.upload_documents),
                        subTitle = getString(R.string.click_here_to_upload_reports_documents)
                    )
                )
            }
            documentAdapter =
                DocumentAdapter(requireContext(), documentsList, this@DocumentFragment, isHideShare = isForPatientDocuments)
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
        if (isAttachmentFlow) {
            selectedDoctorsIds.clear()
            selectedDoctorsIds.add(attachmentShareTo)
        } else {
            val list = doctorList.filter {
                it.isSelected
            }
            if (list.isNotEmpty()) {
                for (i in list) {
                    selectedDoctorsIds.add(i.guid.toString())
                }
            }
        }
        val request = ShareDocumentRequestModel()
        request.document_file = documentsList[selectedItemPosition].title.toString()
        request.uids = selectedDoctorsIds
        request.type = if (isForPatientDocuments) "USER" else "GROUP"
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
        if (isAttachmentFlow) {
//            callShareReportApi()
            val data = getShareReportRequestModel()
            val resultIntent = requireActivity().intent
            val b = Bundle()
            b.putString(
                Constants.DocumentLink,
                Constants.BASE_URL_REPORT + data.document_file
            )
            resultIntent.putExtras(b)
            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
            requireActivity().finish()

        } else {
            openForwardListBottomSheet()
        }
    }

    override fun clickOnPendingDoc(position: Int) {
        selectedItemPosition = position
        val i = Intent(requireContext(), UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd, false)
        bundle.putString(Constants.PendingDocumentType, documentsList[position].title)
        bundle.putString(Constants.PendingDocumentBy, documentsList[position].subTitle)
        bundle.putString(Constants.PendingDocumentGuid, documentsList[position].guid)
        bundle.putString(Constants.PendingDocumentDoctorId, documentsList[position].doctorId)
        bundle.putInt(Constants.PendingDocumentId, documentsList[position].uploadDocumentId)
        bundle.putInt(Constants.DocumentRequestId, documentsList[position].DocumentRequestId)
        i.putExtras(bundle)
        uploadReportResult.launch(i)
    }

    override fun clickOnCancelDoc(position: Int) {
        callCancelDocApi(position)
    }

    private fun callCancelDocApi(position: Int) {
        binding.apply {
            if (requireActivity().isNetworkAvailable()){
                toggleLoader(true)
                val list:ArrayList<Int> = ArrayList()
                list.clear()
                list.add(documentsList[position].DocumentRequestId)
                documentViewModel.callRemoveRequestDocApi(request = RemoveRequestDocumentRequestModel(
                    request_ids = list
                ))
            }else{
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    override fun onItemClick(position: Int) {
        doctorList[position].isSelected = !doctorList[position].isSelected
        forwardDocAdapter.notifyItemChanged(position)
    }

}