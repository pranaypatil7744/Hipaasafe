package com.hipaasafe.presentation.home_screen.document_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.BottomsheetForwardDocBinding
import com.hipaasafe.databinding.FragmentDocumentBinding
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.DocumentAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.ForwardDocAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel

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
                    subTitle = "By 'Dr Puroshottam Jangid'"
                )
            )
            documentsList.add(
                DocumentsModel(
                    documentItemType = DocumentItemType.ITEM_PENDING_DOC,
                    title = "X-Ray Chest Report Required",
                    subTitle = "By 'Dr Puroshottam Jangid'"
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
        }
        bottomSheetDialog.show()
    }

    override fun clickOnAddDocument(position: Int) {

    }

    override fun clickOnForwardDoc(position: Int) {
        setUpDoctorListForForward()
        openForwardListBottomSheet()
    }

    private fun setUpDoctorListForForward() {
        binding.apply { 
            doctorList.clear()
            doctorList.add(ForwardDocumentModel(title = "Dr. Sanjeev Arora"))
            doctorList.add(ForwardDocumentModel(title = "Aditi Chopra"))
            doctorList.add(ForwardDocumentModel(title = "Adarsh M Patil"))
            doctorList.add(ForwardDocumentModel(title = "Adarsh Bhargava"))
            doctorList.add(ForwardDocumentModel(title = "Adhishwar Sharma"))
            doctorList.add(ForwardDocumentModel(title = "Dr. Sanjeev Arora"))
            doctorList.add(ForwardDocumentModel(title = "Aditi Chopra"))
            doctorList.add(ForwardDocumentModel(title = "Adarsh M Patil"))
            doctorList.add(ForwardDocumentModel(title = "Adarsh Bhargava"))
        }
    }

    override fun clickOnPendingDoc(position: Int) {

    }

    override fun onItemClick(position: Int) {
        doctorList[position].isSelected = !doctorList[position].isSelected
        forwardDocAdapter.notifyItemChanged(position)
    }

}