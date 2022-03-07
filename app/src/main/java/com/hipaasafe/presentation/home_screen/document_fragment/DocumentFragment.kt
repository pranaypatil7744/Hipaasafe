package com.hipaasafe.presentation.home_screen.document_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentDocumentBinding
import com.hipaasafe.presentation.home_screen.document_fragment.adapter.DocumentAdapter
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel

class DocumentFragment : BaseFragment(), DocumentAdapter.DocumentClickManager {

    lateinit var binding: FragmentDocumentBinding
    lateinit var documentAdapter:DocumentAdapter
    private var documentsList: ArrayList<DocumentsModel> = ArrayList()

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

    override fun clickOnAddDocument(position: Int) {

    }

    override fun clickOnForwardDoc(position: Int) {

    }

    override fun clickOnPendingDoc(position: Int) {

    }

}