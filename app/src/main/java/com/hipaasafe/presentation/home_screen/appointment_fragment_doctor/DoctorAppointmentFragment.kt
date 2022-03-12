package com.hipaasafe.presentation.home_screen.appointment_fragment_doctor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentDoctorAppointmentBinding


class DoctorAppointmentFragment : BaseFragment() {

    companion object{
        fun newInstance():DoctorAppointmentFragment{
            return DoctorAppointmentFragment()
        }
    }
    lateinit var binding:FragmentDoctorAppointmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDoctorAppointmentBinding.inflate(inflater,container,false)
        return binding.root
    }

}