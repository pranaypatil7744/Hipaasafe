package com.hipaasafe.presentation.view_documents.request_document_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemNoteBinding
import com.hipaasafe.domain.model.notes.NotesListModel

class NotesListAdapter(
    val context: Context,
    private val notesList: ArrayList<NotesListModel>
) : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    class ViewHolder(val binding:ItemNoteBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_note,parent,false)
        val binding= ItemNoteBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val data = notesList[position]
        holder.binding.apply {
            tvName.text = data.doctor_details.name
            tvMsg.text = data.notes
            tvTime.text = data.createdAt
        }
    }

    override fun getItemCount(): Int {
        return notesList.size
    }
}