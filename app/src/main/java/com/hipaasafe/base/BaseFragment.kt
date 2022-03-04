package com.hipaasafe.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment:Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}