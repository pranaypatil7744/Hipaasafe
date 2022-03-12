package com.hipaasafe.presentation.home_screen.appointment_fragment_doctor.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
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
            tvQueue.text = "Queue : "
        }
    }

    override fun getItemCount(): Int {
        return doctorAppointmentList.size
    }
}