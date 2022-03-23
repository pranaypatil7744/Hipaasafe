package com.hipaasafe.presentation.notification

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.databinding.ActivityNotificationBinding
import com.hipaasafe.domain.model.notifications.GetNotificationsRequestModel
import com.hipaasafe.domain.model.notifications.MarkReadNotificationRequestModel
import com.hipaasafe.presentation.notification.adapter.NotificationAdapter
import com.hipaasafe.utils.isNetworkAvailable
import org.koin.android.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity(), NotificationAdapter.NotificationManager {

    lateinit var binding: ActivityNotificationBinding
    lateinit var notificationAdapter: NotificationAdapter

    var notificationList: ArrayList<NotificationResult> = ArrayList()
    var selectedItemPosition:Int =0
    private val notificationViewModel: NotificationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpToolbar()
        setUpObserver()
        setUpNotificationAdapter()
    }

    override fun onResume() {
        super.onResume()
        getNotificationListApi()
    }

    private fun setUpObserver() {
        binding.apply {
            with(notificationViewModel) {
                getNotificationsResponseData.observe(this@NotificationActivity) {
                    toggleLoader(false)
                    if (it.success == true) {
                        if (it.data.count != null && it.data.count != 0) {
                            layoutNoData.root.visibility = GONE
                            notificationList.clear()
                            for (i in it.data.rows){
                                notificationList.add(
                                    NotificationResult(
                                        _id = i.id.toString(),
                                        title = i.title,
                                        message = i.message,
                                        isRead = i.mark_read?:false,
                                        createdAt = i.createdAt,
                                        user_img = i.sender_details.avatar
                                    )
                                )
                            }
                            if (::notificationAdapter.isInitialized) {
                                notificationAdapter.notifyDataSetChanged()
                            }
                        } else {
                            layoutNoData.root.visibility = VISIBLE
                        }
                    } else {
                        showToast(it.message.toString())
                    }
                }

                markReadNotificationsResponseData.observe(this@NotificationActivity){
                    toggleLoader(false)
                    if (it.success == true){
                        notificationList[selectedItemPosition].isRead = true
                        notificationAdapter.notifyItemChanged(selectedItemPosition)
                    }else{
                        showToast(it.message.toString())
                    }
                }

                messageData.observe(this@NotificationActivity){
                    toggleLoader(false)
                    showToast(it.toString())
                }
            }
        }
    }

    private fun getNotificationListApi() {
        binding.apply {
            if (isNetworkAvailable()) {
                toggleLoader(true)
                layoutNoInternet.root.visibility = GONE
                notificationViewModel.callGetNotificationsApi(
                    request = GetNotificationsRequestModel(page = 1,20)
                )
            } else {
                layoutNoInternet.root.visibility = VISIBLE
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

    private fun setUpNotificationAdapter() {
        notificationAdapter = NotificationAdapter(this, notificationList, this)
        binding.recyclerNotifications.apply {
            adapter = notificationAdapter
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.apply {
            divider.visibility = VISIBLE
            btnBack.visibility = VISIBLE
            btnBack.setOnClickListener {
                finish()
            }
            tvDate.visibility = GONE
            tvTitle.text = getString(R.string.notifications)
        }

    }

    override fun onNotificationItemClicked(position: Int) {
        val data = notificationList[position]
        selectedItemPosition = position
        if (!data.isRead) {
            callMarkReadApi(data._id.toString())
        }
    }

    private fun callMarkReadApi(notificationId:String) {
        binding.apply {
            if (isNetworkAvailable()){
                toggleLoader(true)
                notificationViewModel.callMarkReadNotificationsApi(request = MarkReadNotificationRequestModel(
                    notification_id = notificationId.toIntOrNull()?:0
                ))
            }else{
                showToast(getString(R.string.no_internet_connection))
            }
        }
    }

}