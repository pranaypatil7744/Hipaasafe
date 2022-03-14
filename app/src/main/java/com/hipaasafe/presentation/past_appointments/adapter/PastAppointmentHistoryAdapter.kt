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

class PastAppointmentHistoryAdapter(
    val context: Context,
    private val historyList: ArrayList<PastAppointmentHistoryModel>
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
            if (data.age.isNullOrEmpty()){
                tvAge.visibility = GONE
            }else{
                tvAge.visibility = VISIBLE
                tvAge.text = data.age+" Yrs"
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