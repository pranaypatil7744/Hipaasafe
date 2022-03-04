package com.hipaasafe.presentation.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(fa: FragmentActivity, private val arrayList: ArrayList<Fragment>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @NonNull
    override fun createFragment(position: Int): Fragment {
        return arrayList[position]
    }
}