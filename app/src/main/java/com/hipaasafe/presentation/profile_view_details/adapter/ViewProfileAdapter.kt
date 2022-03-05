package com.hipaasafe.presentation.profile_view_details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemViewProfileDetailsBinding
import com.hipaasafe.presentation.profile_view_details.model.ViewProfileModel

class ViewProfileAdapter(val context: Context,private val viewProfileList:ArrayList<ViewProfileModel>):RecyclerView.Adapter<ViewProfileAdapter.ViewHolder>() {
    class ViewHolder(val binding:ItemViewProfileDetailsBinding):RecyclerView.ViewHolder(binding.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_view_profile_details,parent,false)
        val binding = ItemViewProfileDetailsBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = viewProfileList[position]
        holder.binding.apply {
            tvTitle.text = data.title
            tvSubTitle.text = data.subTitle
            data.icon?.let { imgMenu.setImageResource(it) }
        }
    }

    override fun getItemCount(): Int {
        return viewProfileList.size
    }
}