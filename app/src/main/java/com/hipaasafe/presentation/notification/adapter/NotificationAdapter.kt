package com.hipaasafe.presentation.notification.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.databinding.ItemNotificationsBinding
import com.hipaasafe.presentation.notification.NotificationResult
import com.hipaasafe.utils.ImageUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationAdapter(
    val context: Context,
    var notificationList: ArrayList<NotificationResult>,
    var listener: NotificationManager
) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(val binding: ItemNotificationsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notifications, parent, false)
        val item = ItemNotificationsBinding.bind(view)
        return NotificationViewHolder(item)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val data = notificationList[position]
        holder.binding.apply {
            tvNotification.text = data.message
            TextViewCompat.setTextAppearance(
                tvNotification,
                if (data.isRead) R.style.metropolis_regular_small else R.style.metropolis_bold_small
            )
            if (!TextUtils.isEmpty(data.user_img)) {
                ImageUtils.INSTANCE?.loadRemoteImageForProfile(
                    icProfile, data.user_img
                )
            } else {
                icProfile.setImageResource(R.drawable.ic_default_profile_picture)
            }
            if (position == notificationList.size - 1) {
                devider.visibility = View.GONE

            } else {
                devider.visibility = View.VISIBLE
            }

            tvTime.text = timeAgoDisplay(data.createdAt ?: "")
            holder.itemView.setOnClickListener {
                listener.onNotificationItemClicked(position)
            }
        }
    }

    private fun timeAgoDisplay(self: String): String {
        val suffix = "Ago"
        val convTime = "";
        try {
            val dateFormat = SimpleDateFormat(Constants.DATE_TIME_FORMAT)
            val pasTime = dateFormat.parse(self)
            val nowTime = Date();

            val dateDiff = nowTime.time - pasTime.time;
            val second = TimeUnit.MILLISECONDS.toSeconds(dateDiff);
            val minute = TimeUnit.MILLISECONDS.toMinutes(dateDiff);
            val hour = TimeUnit.MILLISECONDS.toHours(dateDiff);
            val day = TimeUnit.MILLISECONDS.toDays(dateDiff);
            when {
                second < 60 -> {
                    return second.toString().plus(" s $suffix")
                }
                minute < 60 -> {
                    return "$minute m $suffix"
                }
                hour < 24 -> {
                    return "$hour h $suffix"
                }
                day >= 7 -> {
                    return when {
                        day > 360 -> {
                            (day / 360).toString() + " y " + suffix
                        }
                        day > 30 -> {
                            (day / 30).toString() + " m " + suffix
                        }
                        else -> {
                            (day / 7).toString() + " w " + suffix
                        }
                    }
                }
                day < 7 -> {
                    return "$day d $suffix"
                }
            }
        } catch (e: Exception) {

        }
        return convTime
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    interface NotificationManager {
        fun onNotificationItemClicked(position: Int)
    }
}