package com.hipaasafe.presentation.home_screen.my_patients_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemMyPatientsBinding
import com.hipaasafe.domain.model.get_patients.PatientsListModel
import com.hipaasafe.utils.ImageUtils

class MyPatientsAdapter(
    val context: Context,
    private val patientsList: ArrayList<PatientsListModel>,
    val listener: MyPatientsClickManager
) :
    RecyclerView.Adapter<MyPatientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMyPatientsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_my_patients, parent, false)
        val binding = ItemMyPatientsBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = patientsList[position]
        holder.binding.apply {
            tvName.text = data.name.toString().split("|").last()
            ImageUtils.INSTANCE?.loadRemoteImageForProfile(
                imgProfile,
                data.list_patient_details.avatar
            )
            btnChat.setOnClickListener {
                listener.onClickChat(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return patientsList.size
    }

    interface MyPatientsClickManager {
        fun onClickChat(position: Int)
    }
}