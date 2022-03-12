package com.hipaasafe.presentation.home_screen.my_teams_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentMyTeamsBinding


class MyTeamsFragment : BaseFragment() {

    companion object {
        fun newInstance(): MyTeamsFragment {
            return MyTeamsFragment()
        }
    }

    lateinit var binding: FragmentMyTeamsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

}