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
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.DocumentAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel
import com.hipaasafe.presentation.upload_documents.UploadDocumentsActivity

class DocumentFragment : BaseFragment(), DocumentAdapter.DocumentClickManager,
    ForwardDocAdapter.ForwardClickManager {

    lateinit var binding: FragmentDocumentBinding
    lateinit var documentAdapter:DocumentAdapter
    lateinit var forwardDocAdapter: ForwardDocAdapter
    private var documentsList: ArrayList<DocumentsModel> = ArrayList()
    private var doctorList:ArrayList<ForwardDocumentModel> = ArrayList()
    lateinit var bottomSheetForwardDocBinding: BottomsheetForwardDocBinding

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
        setUpDoctorListForForward()
    }

    private fun setUpAdapter() {
        binding.apply {
            documentAdapter = DocumentAdapter(requireContext(),documentsList,this@DocumentFragment)
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
            if (::documentAdapter.isInitialized){
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
            forwardDocAdapter = ForwardDocAdapter(requireContext(),doctorList, listener = this@DocumentFragment)
            recyclerAttendanceHistory.adapter = forwardDocAdapter
            imgClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
            btnShare.setOnClickListener {
                val checkSelected = doctorList.find {
                    it.isSelected
                }
                if (checkSelected != null){
                    bottomSheetDialog.dismiss()

                }else{
                    showToast(getString(R.string.please_select_at_least_1_doctor))
                }
            }
        }
        bottomSheetDialog.show()
    }

    override fun clickOnAddDocument(position: Int) {
        val i = Intent(requireContext(),UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd,true)
        bundle.putSerializable(Constants.DoctorsList,doctorList)
        i.putExtras(bundle)
        startActivity(i)
    }

    override fun clickOnForwardDoc(position: Int) {
        openForwardListBottomSheet()
    }

    private fun setUpDoctorListForForward() {
        binding.apply { 
            doctorList.clear()
            doctorList.add(ForwardDocumentModel(title = "Dr. Sanjeev Arora", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Aditi Chopra", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Adarsh M Patil", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Adarsh Bhargava", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Adhishwar Sharma", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Dr. Sanjeev Arora", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Aditi Chopra", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Adarsh M Patil", icon = R.drawable.ic_default_profile_picture))
            doctorList.add(ForwardDocumentModel(title = "Adarsh Bhargava", icon = R.drawable.ic_default_profile_picture))
        }
    }

    override fun clickOnPendingDoc(position: Int) {
        val i = Intent(requireContext(),UploadDocumentsActivity::class.java)
        val bundle = Bundle()
        bundle.putBoolean(Constants.IsFromAdd,false)
        bundle.putString(Constants.PendingDocumentName,documentsList[position].title)
        bundle.putString(Constants.PendingDocumentBy,documentsList[position].subTitle)
        bundle.putSerializable(Constants.DoctorsList,doctorList)
        i.putExtras(bundle)
        startActivity(i)
    }

    override fun onItemClick(position: Int) {
        doctorList[position].isSelected = !doctorList[position].isSelected
        forwardDocAdapter.notifyItemChanged(position)
    }

}