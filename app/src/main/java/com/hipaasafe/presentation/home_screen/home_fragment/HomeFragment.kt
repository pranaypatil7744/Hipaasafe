package com.hipaasafe.presentation.home_screen.home_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hipaasafe.R
import com.hipaasafe.base.BaseFragment
import com.hipaasafe.databinding.FragmentHomeBinding
import com.hipaasafe.presentation.adapter.PagerAdapter

class HomeFragment : BaseFragment() {
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
    }

    private fun setUpView() {
        binding.apply {
            val fList: ArrayList<Fragment> = ArrayList()
            viewPager.adapter = PagerAdapter(requireActivity(), fList)
            viewPager.isUserInputEnabled = false
            navigationView.setOnNavigationItemSelectedListener {
                when (it.itemId) {

                    R.id.navigation_appointments -> {
                        viewPager.currentItem = 0
                    }
                    R.id.navigation_chat -> {
                        viewPager.currentItem = 1
                    }

                    R.id.navigation_my_patients -> {
                        viewPager.currentItem = 2
                    }
                    R.id.navigation_my_teams -> {
                        viewPager.currentItem = 3
                    }
                }
                true
            }
        }
    }

}