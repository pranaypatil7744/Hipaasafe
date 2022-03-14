package com.hipaasafe.presentation.view_documents.request_document_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemRequestDocumentsBinding
import com.hipaasafe.presentation.view_documents.request_document_fragment.model.RequestDocumentModel

class RequestDocumentAdapter(
    val context: Context,
    private val requestDocumentList: ArrayList<RequestDocumentModel>,
    private val listener:RequestDocumentListener
) : RecyclerView.Adapter<RequestDocumentAdapter.ViewHolder>() {

    class ViewHolder(val binding:ItemRequestDocumentsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_request_documents,parent,false)
        val binding = ItemRequestDocumentsBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = requestDocumentList[position]
        holder.binding.apply {
            tvDocumentName.text = data.documentName
            if (data.isSelected){
                imgSelect.setImageResource(R.drawable.ic_item_selected)
            }else{
                imgSelect.setImageResource(R.drawable.ic_item_not_selected)
            }
            holder.itemView.setOnClickListener {
                listener.clickOnDocument(position)
            }
        }
    }

    override fun getItemCount(): Int {
      return requestDocumentList.size
    }

    interface RequestDocumentListener{
        fun clickOnDocument(position: Int)
    }
}