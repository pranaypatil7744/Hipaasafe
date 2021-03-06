package com.hipaasafe.presentation.past_appointments.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemAppointmentHistoryBinding
import com.hipaasafe.presentation.past_appointments.model.PastAppointmentHistoryModel
import com.hipaasafe.utils.AppUtils

class PastAppointmentHistoryAdapter(
    val context: Context,
    private val historyList: ArrayList<PastAppointmentHistoryModel>,var isPatient:Boolean
) : RecyclerView.Adapter<PastAppointmentHistoryAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemAppointmentHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(context).inflate(R.layout.item_appointment_history, parent, false)
        val binding = ItemAppointmentHistoryBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = historyList[position]
        holder.binding.apply {
            tvTitle.text = data.title
            tvSubTitle.text = data.date + "  |  " + data.time
            if (data.dob.isNullOrEmpty()){
                tvAge.visibility = GONE
            }else{
                if (isPatient){
                    tvAge.visibility = GONE
                }else{
                    val birthYear = data.dob?.split("-")?.first()
                    val age = AppUtils.INSTANCE?.calculateAge(birthYear?.toIntOrNull()?:0)
                    if (age?.toIntOrNull()?:0 != 0){
                        tvAge.visibility = VISIBLE
                        tvAge.text = "$age Yrs"
                    }else{
                        tvAge.visibility = GONE
                    }
                }
            }
            if (historyList.size - 1 == position){
                divider.visibility = GONE
            }else{
                divider.visibility = VISIBLE
            }
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }
}