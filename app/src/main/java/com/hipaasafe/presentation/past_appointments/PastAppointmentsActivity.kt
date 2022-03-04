package com.hipaasafe.presentation.past_appointments

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityPastAppointmentsBinding

class PastAppointmentsActivity : BaseActivity() {
    lateinit var binding:ActivityPastAppointmentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPastAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
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