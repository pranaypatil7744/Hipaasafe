package com.hipaasafe.presentation.home_screen.appointment_fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemAppointmentsBinding
import com.hipaasafe.databinding.ItemDocumentTitleBinding
import com.hipaasafe.databinding.ItemNavDividerBinding
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.AppointmentItemType
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.AppointmentStatus
import com.hipaasafe.presentation.home_screen.appointment_fragment.model.UpcomingAppointmentModel
import com.hipaasafe.utils.AppUtils

class UpcomingAppointmentAdapter(
    val context: Context,
    var upcomingAppointmentList: ArrayList<UpcomingAppointmentModel>,
    var listener: AppointmentClickManager
) : RecyclerView.Adapter<UpcomingAppointmentAdapter.ViewHolder>() {

    class ViewHolder : RecyclerView.ViewHolder {
        var itemAppointmentBinding: ItemAppointmentsBinding? = null
        var itemDividerBinding: ItemNavDividerBinding? = null
        var itemTitleBinding: ItemDocumentTitleBinding? = null

        constructor(binding: ItemNavDividerBinding) : super(binding.root) {
            itemDividerBinding = binding
        }

        constructor(binding: ItemAppointmentsBinding) : super(binding.root) {
            itemAppointmentBinding = binding
        }

        constructor(binding: ItemDocumentTitleBinding) : super(binding.root) {
            itemTitleBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            AppointmentItemType.ITEM_APPOINTMENT.value -> {
                val v =
                    LayoutInflater.from(context).inflate(R.layout.item_appointments, parent, false)
                val binding = ItemAppointmentsBinding.bind(v)
                ViewHolder(binding)
            }
            AppointmentItemType.ITEM_TITLE.value -> {
                val v = LayoutInflater.from(context)
                    .inflate(R.layout.item_document_title, parent, false)
                val binding = ItemDocumentTitleBinding.bind(v)
                ViewHolder(binding)
            }
            else -> {
                val v =
                    LayoutInflater.from(context).inflate(R.layout.item_nav_divider, parent, false)
                val binding = ItemNavDividerBinding.bind(v)
                return ViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = upcomingAppointmentList[position]
        holder.apply {
            when (itemViewType) {
                AppointmentItemType.ITEM_TITLE.value -> {
                    itemTitleBinding?.apply {
                        tvTitle.apply {
                            text = data.name
                            setTextColor(ContextCompat.getColor(context, R.color.slate_gray))
                        }
                    }
                }
                AppointmentItemType.ITEM_APPOINTMENT.value -> {
                    itemAppointmentBinding?.apply {
                        tvName.text = data.name
                        tvSpeciality.text = data.speciality
                        when (data.appointmentStatus) {
                            AppointmentStatus.ITEM_PENDING -> {
//                                val isRescheduleHide = AppUtils.INSTANCE?.checkIsRescheduleHide(data.date+" "+data.time)
                                if (data.isLessThan24){
                                    layoutPending.visibility = VISIBLE
                                    btnReschedule.visibility = GONE
                                }else{
                                    layoutPending.visibility = GONE
                                    btnReschedule.visibility = VISIBLE
                                }
                                layoutReschedule.visibility = GONE
                                imgStatus.visibility = GONE
                                tvStatus.visibility = GONE
                                tvDateTime.text = data.date + "  |   " + data.time
                                btnCancel.setOnClickListener {
                                    listener.clickedOnCancelAppointment(position)
                                }
                                btnConfirm.setOnClickListener {
                                    listener.clickedOnConfirmAppointment(position)
                                }
                                btnReschedule.setOnClickListener {
                                    listener.clickedOnRescheduleAppointment(position)
                                }
                            }
                            AppointmentStatus.ITEM_CONFIRM -> {
                                layoutPending.visibility = GONE
                                layoutReschedule.visibility = GONE
                                tvDateTime.text = data.date + "  |  " + data.time + "  |  "
                                tvStatus.apply {
                                    text = context.getString(R.string.confirmed)
                                    setTextColor(ContextCompat.getColor(context, R.color.apple))
                                    visibility = VISIBLE
                                }
                                imgStatus.apply {
                                    visibility = VISIBLE
                                    setImageResource(R.drawable.ic_check)
                                }
                                btnReschedule.apply {
                                    visibility = VISIBLE
                                    text = context.getString(R.string.cancel)
                                    setOnClickListener {
                                        listener.clickedOnCancelAppointment(position)
                                    }
                                }
                            }
                            AppointmentStatus.ITEM_CANCEL -> {
                                tvDateTime.text = data.date + "  |  " + data.time + "  |  "
                                layoutPending.visibility = GONE
                                btnReschedule.visibility = GONE
                                layoutReschedule.visibility = GONE
                                tvStatus.apply {
                                    text = context.getString(R.string.cancelled)
                                    setTextColor(
                                        ContextCompat.getColor(
                                            context,
                                            R.color.persian_red
                                        )
                                    )
                                    visibility = VISIBLE
                                }
                                imgStatus.apply {
                                    visibility = VISIBLE
                                    setImageResource(R.drawable.ic_close_red)
                                }
                            }
                            AppointmentStatus.ITEM_RESCHEDULED -> {
                                tvDateTime.text = data.date + "  |  " + data.time
                                layoutPending.visibility = GONE
                                btnReschedule.visibility = GONE
                                layoutReschedule.visibility = VISIBLE
                                imgStatus.visibility = GONE
                                tvStatus.visibility = GONE
                            }
                            AppointmentStatus.ITEM_COMPLETED -> {
                                tvDateTime.text = data.date + "  |  " + data.time
                                layoutPending.visibility = GONE
                                btnReschedule.visibility = GONE
                                layoutReschedule.visibility = GONE
                                tvStatus.apply {
                                    text = context.getString(R.string.completed)
                                    setTextColor(ContextCompat.getColor(context, R.color.apple))
                                    visibility = VISIBLE
                                }
                                imgStatus.apply {
                                    visibility = VISIBLE
                                    setImageResource(R.drawable.ic_check)
                                }
                            }
                            else -> {
                                tvDateTime.text = data.date + "  |  " + data.time
                                layoutPending.visibility = GONE
                                btnReschedule.visibility = GONE
                                layoutReschedule.visibility = GONE
                                imgStatus.visibility = GONE
                                tvStatus.visibility = GONE
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return upcomingAppointmentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return upcomingAppointmentList[position].appointmentItemType.value
    }

    interface AppointmentClickManager {
        fun clickedOnCancelAppointment(position: Int)
        fun clickedOnConfirmAppointment(position: Int)
        fun clickedOnRescheduleAppointment(position: Int)
    }
}