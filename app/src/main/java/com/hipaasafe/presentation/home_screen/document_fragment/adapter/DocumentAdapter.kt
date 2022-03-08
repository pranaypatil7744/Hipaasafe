package com.hipaasafe.presentation.home_screen.document_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemAddDocumentBinding
import com.hipaasafe.databinding.ItemDocumentTitleBinding
import com.hipaasafe.databinding.ItemPendingDocumentBinding
import com.hipaasafe.databinding.ItemUploadedDocumentBinding
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentItemType
import com.hipaasafe.presentation.home_screen.document_fragment.model.DocumentsModel

class DocumentAdapter(
    val context: Context, private val documentList: ArrayList<DocumentsModel>,
    var listener: DocumentClickManager
) :
    RecyclerView.Adapter<DocumentAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var itemAddDocumentBinding: ItemAddDocumentBinding? = null
        var itemUploadedDocumentBinding: ItemUploadedDocumentBinding? = null
        var itemPendingDocumentBinding: ItemPendingDocumentBinding? = null
        var itemDocumentTitleBinding: ItemDocumentTitleBinding? = null

        constructor(binding: ItemDocumentTitleBinding) : super(binding.root) {
            itemDocumentTitleBinding = binding
        }

        constructor(binding: ItemAddDocumentBinding) : super(binding.root) {
            itemAddDocumentBinding = binding
        }

        constructor(binding: ItemUploadedDocumentBinding) : super(binding.root) {
            itemUploadedDocumentBinding = binding
        }

        constructor(binding: ItemPendingDocumentBinding) : super(binding.root) {
            itemPendingDocumentBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            DocumentItemType.ITEM_ADD_DOC.value -> {
                val v =
                    LayoutInflater.from(context).inflate(R.layout.item_add_document, parent, false)
                val binding = ItemAddDocumentBinding.bind(v)
                ViewHolder(binding)
            }
            DocumentItemType.ITEM_PENDING_DOC.value -> {
                val v = LayoutInflater.from(context)
                    .inflate(R.layout.item_pending_document, parent, false)
                val binding = ItemPendingDocumentBinding.bind(v)
                ViewHolder(binding)
            }
            DocumentItemType.ITEM_UPLOADED_DOC.value -> {
                val v = LayoutInflater.from(context)
                    .inflate(R.layout.item_uploaded_document, parent, false)
                val binding = ItemUploadedDocumentBinding.bind(v)
                ViewHolder(binding)
            }
            DocumentItemType.ITEM_TITLE.value -> {
                val v = LayoutInflater.from(context)
                    .inflate(R.layout.item_document_title, parent, false)
                val binding = ItemDocumentTitleBinding.bind(v)
                ViewHolder(binding)
            }
            else -> {
                val v = LayoutInflater.from(context)
                    .inflate(R.layout.item_document_title, parent, false)
                val binding = ItemDocumentTitleBinding.bind(v)
                ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = documentList[position]
        holder.apply {
            when (itemViewType) {
                DocumentItemType.ITEM_TITLE.value -> {
                    itemDocumentTitleBinding?.apply {
                        tvTitle.text = data.title
                    }
                }
                DocumentItemType.ITEM_ADD_DOC.value -> {
                    itemAddDocumentBinding?.apply {
                        tvTitle.text = data.title
                        tvSubTitle.text = data.subTitle
                    }
                    itemView.setOnClickListener {
                        listener.clickOnAddDocument(position)
                    }
                }
                DocumentItemType.ITEM_UPLOADED_DOC.value -> {
                    itemUploadedDocumentBinding?.apply {
                        tvTitle.text = data.title
                        if (data.title?.contains(".jpeg") == true){
                            imgDoc.setImageResource(R.drawable.ic_default_img)
                        }else{
                            imgDoc.setImageResource(R.drawable.ic_default_pdf)
                        }
                        btnForward.setOnClickListener {
                            listener.clickOnForwardDoc(position)
                        }
                    }
                }
                DocumentItemType.ITEM_PENDING_DOC.value -> {
                    itemPendingDocumentBinding?.apply {
                        tvTitle.text = data.title
                        tvSubTitle.text = "By '"+data.subTitle+"'"
                    }
                    itemView.setOnClickListener {
                        listener.clickOnPendingDoc(position)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return documentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return documentList[position].documentItemType.value
    }

    interface DocumentClickManager {
        fun clickOnAddDocument(position: Int)
        fun clickOnForwardDoc(position: Int)
        fun clickOnPendingDoc(position: Int)
    }
}