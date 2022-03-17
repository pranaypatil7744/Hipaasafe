package com.hipaasafe.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.provider.Settings
import android.view.LayoutInflater
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.cometchat.pro.models.BaseMessage
import com.hipaasafe.BuildConfig
import com.hipaasafe.R
import com.hipaasafe.databinding.*

class DialogUtils {
    companion object {
        private lateinit var permissionDialog: Dialog
        private lateinit var bookingCancelConfirmationDialog: Dialog
        private lateinit var rescheduleDoneDialog: Dialog


        fun showUserProfileDialog(
            context: Context,
            userName: String,
            profilePicUrl: String,
            details: String,
            isBlock: Boolean,
            listener: DialogManager
        ) {
            val binding = LayoutDialogViewProfileBinding.inflate(LayoutInflater.from(context))
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding.root)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            dialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            binding.apply {
                tvName.text = userName
                ImageUtils.INSTANCE?.loadRemoteImageForProfile(imgDoc, profilePicUrl)
                tvSpecialtyLocExp.text = details
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
                if (isBlock) {
                    btnBlockUnBlock.apply {
                        text = context.getString(R.string.unblock)
                        icon = null
                        setTextColor(ContextCompat.getColor(context, R.color.green_600))
                    }
                } else {
                    btnBlockUnBlock.apply {
                        text = context.getString(R.string.block)
                        icon = ContextCompat.getDrawable(context, R.drawable.ic_block)
                        setTextColor(ContextCompat.getColor(context, R.color.monza))
                    }
                }
                btnBlockUnBlock.setOnClickListener {
                    dialog.dismiss()
                    listener.clickOnBlockUnblock()
                }
            }
            dialog.show()
        }


        fun showRescheduleDoneDialog(
            activity: Activity,
            listener: DialogManager
        ) {
            val binding =
                DialogRescheduleDoneBinding.inflate(LayoutInflater.from(activity))
            rescheduleDoneDialog = Dialog(activity)
            rescheduleDoneDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            rescheduleDoneDialog.setContentView(binding.root)
            rescheduleDoneDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            rescheduleDoneDialog.window?.attributes?.windowAnimations =
                R.style.DialogTheme
            rescheduleDoneDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            rescheduleDoneDialog.setCancelable(false)
            rescheduleDoneDialog.setCanceledOnTouchOutside(false)
            binding.apply {
                btnClose.setOnClickListener {
                    rescheduleDoneDialog.dismiss()
                }
                btnYes.setOnClickListener {
                    rescheduleDoneDialog.dismiss()
                   listener.onContinueClick()
                }
            }
            rescheduleDoneDialog.show()
        }



        fun showCancelConfirmationDialog(
            activity: Activity,
            listener: DialogManager,
            title: String,
            btnText: String,
            isCancel: Boolean
        ) {
            val binding =
                DialogCancelAppointmentConfirmationBinding.inflate(LayoutInflater.from(activity))
            bookingCancelConfirmationDialog = Dialog(activity)
            bookingCancelConfirmationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            bookingCancelConfirmationDialog.setContentView(binding.root)
            bookingCancelConfirmationDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            bookingCancelConfirmationDialog.window?.attributes?.windowAnimations =
                R.style.DialogTheme
            bookingCancelConfirmationDialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            bookingCancelConfirmationDialog.setCancelable(false)
            bookingCancelConfirmationDialog.setCanceledOnTouchOutside(false)
            binding.apply {
                tvTitle.text = title
                btnYes.text = btnText
                tvBack.setOnClickListener {
                    bookingCancelConfirmationDialog.dismiss()
                }
                btnYes.setOnClickListener {
                    bookingCancelConfirmationDialog.dismiss()
                    if (isCancel) {
                        listener.onCancelClick()
                    } else {
//                        listener.onContinueClick()
                        showRescheduleDoneDialog(activity,listener)
                    }
                }
            }
            bookingCancelConfirmationDialog.show()
        }


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

        fun showConfirmationDialog(
            context: Context,
            listener: DialogManager,
            icon: Int,
            title: String,
            msg: String,
            btnText: String,
            isBlock: Boolean = false,
            isDeleteGroup: Boolean = false,
            isExitGroup: Boolean = false
        ) {
            val binding = DialogBlockUserBinding.inflate(LayoutInflater.from(context))
            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(binding.root)
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogTheme
            dialog.window?.setLayout(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            binding.apply {
                imgBlock.setImageResource(icon)
                tvTitle.text = title
                tvTitleSub.text = msg
                btnBlock.text = btnText
                btnBlock.setOnClickListener {
                    dialog.dismiss()
                    when {
                        isBlock -> {
                            listener.onBlockUser()
                        }
                        isDeleteGroup -> {
                            listener.onDeleteGroup()
                        }
                        isExitGroup -> {
                            listener.onExitGroup()
                        }
                    }
                }
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
            }
            dialog.show()
        }

    }

    interface DialogManager {
        fun onContinueClick() {}
        fun onBlockUser() {}
        fun onDeleteGroup() {}
        fun onExitGroup() {}
        fun onEFaxContinueClick(faxNo: String, baseMessage: BaseMessage) {}
        fun onCancelClick() {}
        fun clickOnBlockUnblock() {
        }
    }
}