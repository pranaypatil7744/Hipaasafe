package com.hipaasafe.presentation.home_screen.appointment_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentAppointmentBinding

class AppointmentFragment : BaseFragment() {

    companion object{
        fun newInstance():AppointmentFragment{
            return AppointmentFragment()
        }
    }
    lateinit var binding:FragmentAppointmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentBinding.inflate(inflater,container,false)
        return binding.root
    }
}