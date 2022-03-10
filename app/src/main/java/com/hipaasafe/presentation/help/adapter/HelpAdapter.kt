package com.hipaasafe.presentation.help.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemHelpBinding
import com.hipaasafe.databinding.ItemHowCanHelpBinding
import com.hipaasafe.presentation.help.model.HelpItemType
import com.hipaasafe.presentation.help.model.HelpModel

class HelpAdapter(
    val context: Context,
    var helpList: ArrayList<HelpModel> = ArrayList(),
    var listener: HelpManager
) : RecyclerView.Adapter<HelpAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var helpBinding: ItemHelpBinding? = null
        var howCanHelpBinding: ItemHowCanHelpBinding? = null

        constructor(binding: ItemHelpBinding) : super(binding.root) {
            helpBinding = binding
        }

        constructor(binding: ItemHowCanHelpBinding) : super(binding.root) {
            howCanHelpBinding = binding
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == HelpItemType.HEADER.value) {

            val view = LayoutInflater.from(context).inflate(
                R.layout.item_how_can_help,
                parent,
                false
            )
            return ViewHolder(ItemHowCanHelpBinding.bind(view))
        } else {

            val view = LayoutInflater.from(context).inflate(
                R.layout.item_help,
                parent,
                false
            )
            return ViewHolder(ItemHelpBinding.bind(view))
        }


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = helpList[position]
        if (holder.itemViewType != HelpItemType.HEADER.value) {
            holder.helpBinding?.ivItem?.setImageResource(data.icon ?: 0)
            holder.helpBinding?.tvLabel?.text = data.label
            holder.helpBinding?.tvData?.text = data.data
            holder.itemView.setOnClickListener {
                listener.onItemClicked(data)
            }
        }
    }

    override fun getItemCount(): Int {
        return helpList.size
    }

    override fun getItemViewType(position: Int): Int {
        return helpList[position].type.value
    }

    interface HelpManager {
        fun onItemClicked(data: HelpModel)
    }
}