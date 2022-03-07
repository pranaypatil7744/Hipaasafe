package com.hipaasafe.presentation.home_screen.document_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemForwardDoctorBinding
import com.hipaasafe.presentation.home_screen.document_fragment.model.ForwardDocumentModel

class ForwardDocAdapter(
    var context: Context,
    private val doctorList: ArrayList<ForwardDocumentModel>,
    var listener:ForwardClickManager
) : RecyclerView.Adapter<ForwardDocAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemForwardDoctorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_forward_doctor, parent, false)
        val binding = ItemForwardDoctorBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = doctorList[position]
        holder.binding.apply {
            tvName.text = data.title
            if (data.isSelected) {
                btnIsSelect.setImageResource(R.drawable.ic_item_selected)
            } else {
                btnIsSelect.setImageResource(R.drawable.ic_item_not_selected)
            }
            holder.itemView.setOnClickListener {
                listener.onItemClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }

    interface ForwardClickManager {
        fun onItemClick(position: Int)
    }
}