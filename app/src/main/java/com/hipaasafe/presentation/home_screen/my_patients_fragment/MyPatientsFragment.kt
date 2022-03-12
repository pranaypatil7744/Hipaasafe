package com.hipaasafe.presentation.home_screen.my_patients_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyPatientsBinding


class MyPatientsFragment : BaseFragment() {

    lateinit var binding:FragmentMyPatientsBinding
    companion object{
        fun newInstance():MyPatientsFragment{
            return MyPatientsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_patients, container, false)
    }

}