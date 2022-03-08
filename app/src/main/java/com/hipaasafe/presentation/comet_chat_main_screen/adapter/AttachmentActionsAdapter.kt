package com.hipaasafe.presentation.comet_chat_main_screen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemAttachmentActionBinding
import com.hipaasafe.presentation.comet_chat_main_screen.model.AttachmentMenuModel

data class AttachmentActionsAdapter(
    val context: Context,
    private val itemsList: ArrayList<AttachmentMenuModel>,
    private val listener: AttachmentActions
) : RecyclerView.Adapter<AttachmentActionsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAttachmentActionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_attachment_action, parent, false)
        val binding = ItemAttachmentActionBinding.bind(view)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemsList[position]
        holder.binding.apply {
            tvMenuName.text = data.menuName
            data.menuIcon?.let { imgMenu.setImageResource(it) }
            btnMenu.setOnClickListener {
                listener.clickOnAttachmentMenu(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    interface AttachmentActions {
        fun clickOnAttachmentMenu(position: Int)
    }
}
