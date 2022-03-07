package com.hipaasafe.utils

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Window
import android.widget.RelativeLayout
import com.hipaasafe.BuildConfig
import com.hipaasafe.R
import com.hipaasafe.databinding.DialogPermissionBinding

class DialogUtils {
    companion object {
        private lateinit var permissionDialog: Dialog

        fun showPermissionDialog(
            activity: Activity,
            msg: String,
            title: String,
            positiveBtn: String,
            negativeBtn: String,
            isFinish: Boolean = true,
            isOtherAction: Boolean = false,
            listener: DialogManager? = null
        ) {
            val binding = DialogPermissionBinding.inflate(LayoutInflater.from(activity))
            permissionDialog = Dialog(activity)
            permissionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            permissionDialog.setContentView(binding.root)
            permissionDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            permissionDialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            permissionDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            permissionDialog.setCancelable(false)
            permissionDialog.setCanceledOnTouchOutside(false)

            binding.apply {
                tvDeny.setOnClickListener {
                    permissionDialog.dismiss()
                    if (isFinish) {
                        activity.finish()
                    } else {
                        permissionDialog.dismiss()
                    }
                }
                tvGoToSettings.setOnClickListener {
                    if (isOtherAction) {
                        permissionDialog.dismiss()
                        listener?.onContinueClick()
                    } else {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        activity.startActivity(intent)
                        if (isFinish) {
                            activity.finish()
                        } else {
                            permissionDialog.dismiss()
                        }
                    }
                }
                tvMsg.text = msg
                tvAllowPermission.text = title
                tvDeny.text = negativeBtn
                tvGoToSettings.text = positiveBtn
            }
            permissionDialog.show()
        }

    }

    interface DialogManager {
        fun onContinueClick() {}
    }
}