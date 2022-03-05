package com.hipaasafe.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.hipaasafe.utils.PreferenceUtils

open class BaseFragment:Fragment() {
    lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}