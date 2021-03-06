package com.hipaasafe.presentation.home_screen.my_network.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemMyNetworkBinding
import com.hipaasafe.presentation.home_screen.my_network.model.DoctorModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.ImageUtils

class MyNetworkAdapter(
    val context: Context, private val myNetworkList: ArrayList<DoctorModel>,
    private val listener: MyNetworkClickManager
) :
    RecyclerView.Adapter<MyNetworkAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemMyNetworkBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_my_network, parent, false)
        val binding = ItemMyNetworkBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = myNetworkList[position]
        holder.binding.apply {
            tvName.text = data.name
            tvSpecialityLocation.text = AppUtils.INSTANCE?.getSpecialityWithLocationAndExperience(
                data.speciality,
                data.location,
                data.experience
            )
            ImageUtils.INSTANCE?.loadRemoteImageForGroupProfile(imgProfile,data.avatar)
            if (myNetworkList.size - 1 == position) {
                devider.visibility = GONE
            } else {
                devider.visibility = VISIBLE
            }
            btnChat.setOnClickListener {
                listener.clickOnChat(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return myNetworkList.size
    }

    interface MyNetworkClickManager {
        fun clickOnChat(position: Int)
    }
}