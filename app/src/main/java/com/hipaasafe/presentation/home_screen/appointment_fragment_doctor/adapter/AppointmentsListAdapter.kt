package com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemDoctorAppontmentListBinding
import com.hipaasafe.domain.model.appointment.DoctorAppointmentListModel
import java.util.*
import kotlin.collections.ArrayList

class AppointmentsListAdapter(val context: Context,private val doctorAppointmentList:ArrayList<DoctorAppointmentListModel>):RecyclerView.Adapter<AppointmentsListAdapter.ViewHolder>() {
    class ViewHolder(val binding:ItemDoctorAppontmentListBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_doctor_appontment_list,parent,false)
        val binding = ItemDoctorAppontmentListBinding.bind(v)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = doctorAppointmentList[position]
        holder.binding.apply {
            tvTitle.text = data.patient_details.name
            tvSubTitle.text = data.patient_details.age
            tvStatus.text = data.appointment_status?.lowercase(Locale.ROOT)
            if (data.queue_no != null){
                tvQueue.visibility = VISIBLE
                tvQueue.text = "Queue : ${data.queue_no}"
            }else{
                tvQueue.visibility = GONE
            }
            when(data.appointment_status){
                Constants.PENDING ->{
                    tvStatus.apply {
                        text = context.getString(R.string.pending)
                        setTextColor(ContextCompat.getColor(context,R.color.carrot_orange))
                    }
                }
                Constants.CANCELLED ->{
                    tvStatus.apply {
                        text = context.getString(R.string.cancelled)
                        setTextColor(ContextCompat.getColor(context,R.color.persian_red))
                    }
                }
                Constants.CONFIRMED ->{
                    tvStatus.apply {
                        text = context.getString(R.string.confirmed)
                        setTextColor(ContextCompat.getColor(context,R.color.apple))
                    }
                }
                Constants.RESCHEDULED ->{
                    tvStatus.apply {
                        text =context.getString(R.string.rescheduled)
                        setTextColor(ContextCompat.getColor(context,R.color.carrot_orange))
                    }
                }
                Constants.COMPLETED ->{
                    tvStatus.apply {
                        text = context.getString(R.string.completed)
                        setTextColor(ContextCompat.getColor(context,R.color.apple))
                    }
                }
                Constants.REMINDER ->{
                    tvStatus.apply { 
                        text = context.getString(R.string.reminder)
                        setTextColor(ContextCompat.getColor(context,R.color.azure_radiance))
                    }
                }
                Constants.NEXT_IN_Q ->{
                    tvStatus.apply {
                        text = context.getString(R.string.next_in_q)
                        setTextColor(ContextCompat.getColor(context,R.color.azure_radiance))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return doctorAppointmentList.size
    }
}