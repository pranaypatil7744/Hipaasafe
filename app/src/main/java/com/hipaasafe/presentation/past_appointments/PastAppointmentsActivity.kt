package com.hipaasafe.presentation.past_appointments

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityPastAppointmentsBinding
import com.hipaasafe.presentation.past_appointments.adapter.PastAppointmentHistoryAdapter
import com.hipaasafe.presentation.past_appointments.model.PastAppointmentHistoryModel

class PastAppointmentsActivity : BaseActivity() {
    lateinit var binding: ActivityPastAppointmentsBinding
    lateinit var pastAppointmentHistoryAdapter: PastAppointmentHistoryAdapter
    private var pastAppointmentHistoryList: ArrayList<PastAppointmentHistoryModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpPastHistoryList()
        setUpAdapter()
    }

    private fun setUpPastHistoryList() {
        binding.apply {
            pastAppointmentHistoryList.clear()
            pastAppointmentHistoryList.add(
                PastAppointmentHistoryModel(
                    title = "Dr. Sanjeev Arora", date = "02 Feb", time = "12:00 PM"
                )
            )
            pastAppointmentHistoryList.add(
                PastAppointmentHistoryModel(
                    title = "Dr. Aditi Chopra", date = "02 Feb", time = "12:00 PM"
                )
            )
            pastAppointmentHistoryList.add(
                PastAppointmentHistoryModel(
                    title = "Dr. Rakesh Sharma", date = "02 Feb", time = "12:00 PM"
                )
            )
            pastAppointmentHistoryList.add(
                PastAppointmentHistoryModel(
                    title = "Dr. Puroshottam Jangid", date = "02 Feb", time = "12:00 PM"
                )
            )
            pastAppointmentHistoryList.add(
                PastAppointmentHistoryModel(
                    title = "Dr. Sanjeev Arora", date = "02 Feb", time = "12:00 PM"
                )
            )
        }
    }

    private fun setUpAdapter() {
        binding.apply {
            pastAppointmentHistoryAdapter = PastAppointmentHistoryAdapter(
                this@PastAppointmentsActivity,
                pastAppointmentHistoryList
            )
            recyclerPastAppointments.adapter = pastAppointmentHistoryAdapter
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.past_appointments)
            tvDate.visibility = GONE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            divider.visibility = VISIBLE
        }
    }
}