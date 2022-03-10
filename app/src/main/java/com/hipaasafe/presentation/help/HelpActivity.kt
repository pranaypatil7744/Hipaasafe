package com.hipaasafe.presentation.help

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityHelpBinding
import com.hipaasafe.presentation.help.adapter.HelpAdapter
import com.hipaasafe.presentation.help.model.HelpItemType
import com.hipaasafe.presentation.help.model.HelpModel
import com.hipaasafe.utils.AppUtils
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class HelpActivity : BaseActivity(), HelpAdapter.HelpManager {
    lateinit var binding: ActivityHelpBinding
    var helpList: ArrayList<HelpModel> = ArrayList()
    lateinit var helpAdapter: HelpAdapter
    private val helpViewModel: HelpViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setUpToolbar()
        callHelpDetailsApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(helpViewModel) {
                helpDetailsResponseData.observe(this@HelpActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        helpList.clear()
                        helpList.add(HelpModel(type = HelpItemType.HEADER))
                        for (i in it.data) {
                            helpList.add(
                                HelpModel(
                                    type = if (i.key == Constants.MOBILE) HelpItemType.CALL else HelpItemType.EMAIL,
                                    icon = if (i.key == Constants.MOBILE) R.drawable.img_call else R.drawable.img_email,
                                    label = if (i.key == Constants.MOBILE) getString(R.string.you_can_call_us) else getString(
                                        R.string.send_us_an_e_mail
                                    ),
                                    data = i.value.toString()
                                )
                            )
                        }
                        setUpAdapter()
                    } else {
                        showToast(it.message.toString())
                    }
                }
                messageData.observe(this@HelpActivity) {
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun callHelpDetailsApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                helpViewModel.callGetStaticDetailsApi()
            } else {
                showToast(getString(R.string.please_check_your_internet_connection))
            }
        }
    }

    private fun toggleLoader(showLoader: Boolean) {
        toggleFadeView(
            binding.root,
            binding.contentLoading.layoutLoading,
            binding.contentLoading.imageLoading,
            showLoader
        )
    }

    private fun setUpAdapter() {
        helpAdapter = HelpAdapter(this, helpList, this)
        binding.recyclerHelp.adapter = helpAdapter
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.help)
            tvDate.visibility = GONE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun onItemClicked(data: HelpModel) {
        if (data.type == HelpItemType.EMAIL) {
            AppUtils.INSTANCE?.openMailer(this, data.data)
        } else {
            AppUtils.INSTANCE?.openDialer(this, data.data)

        }
    }
}