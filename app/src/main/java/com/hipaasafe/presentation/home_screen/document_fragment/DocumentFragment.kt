package com.hipaasafe.presentation.home_screen.document_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.databinding.FragmentDocumentBinding
import com.hipaasafe.domain.model.get_doctors.GetDoctorsRequestModel
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.DocumentAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.home_screen.my_network.MyNetworkViewModel
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel
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
    }

    private fun callDoctorsApi() {
        binding.apply {
            if (requireContext().isNetworkAvailable()) {
                toggleLoader(true)
                myNetworkViewModel.callPatientUpdateProfileApi(
                    GetDoctorsRequestModel(page = 1, limit = 30)
                )
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
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
                            showToast("no data")
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
            documentAdapter =
                DocumentAdapter(requireContext(), documentsList, this@DocumentFragment)
            recyclerDocuments.adapter = documentAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        setUpDocumentList()
    }

    private fun setUpDocumentList() {
        binding.apply {
            documentsList.clear()
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_ADD_DOC,
                    title = getString(R.string.upload_documents),
                    subTitle = getString(R.string.click_here_to_upload_reports_documents)
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_TITLE,
                    title = "03 Feb 2022"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_UPLOADED_DOC,
                    title = "Blood tests report"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_TITLE,
                    title = "04 Feb 2022"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_UPLOADED_DOC,
                    title = "X-Ray Chest"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_UPLOADED_DOC,
                    title = "Sonography Abdomen"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_UPLOADED_DOC,
                    title = "Images.jpeg"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_TITLE
                )
            )

            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_PENDING_DOC,
                    title = "Blood Count Report Required",
                    subTitle = "Dr Puroshottam Jangid"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_PENDING_DOC,
                    title = "X-Ray Chest Report Required",
                    subTitle = "Dr Puroshottam Jangid"
                )
            )
            if (::documentAdapter.isInitialized) {
                documentAdapter.notifyDataSetChanged()
            }
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
                for (i in doctorList){
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

                } else {
                    showToast(getString(R.string.please_select_at_least_1_doctor))
                }
            }
        }
        bottomSheetDialog.show()
    }

    override fun clickOnAddDocument(position: Int) {
        val i = Intent(requireContext(), UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd, true)
        bundle.putSerializable(Constants.DoctorsList, doctorList)
        i.putExtras(bundle)
        startActivity(i)
    }

    override fun clickOnForwardDoc(position: Int) {
        openForwardListBottomSheet()
    }

    override fun clickOnPendingDoc(position: Int) {
        val i = Intent(requireContext(), UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd, false)
        bundle.putString(Constants.PendingDocumentName, documentsList[position].title)
        bundle.putString(Constants.PendingDocumentBy, documentsList[position].subTitle)
        bundle.putSerializable(Constants.DoctorsList, doctorList)
        i.putExtras(bundle)
        startActivity(i)
    }

    override fun onItemClick(position: Int) {
        doctorList[position].isSelected = !doctorList[position].isSelected
        forwardDocAdapter.notifyItemChanged(position)
    }

}