package com.hipaasafe.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.cometchat.pro.helpers.Logger
import com.hipaasafe.Constants

class PermissionUtils {
    companion object{
        fun getStoragePermission(activity:Activity): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }
        fun requestStoragePermissions(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                Constants.PermissionRequestCodes.STORAGE_PERMISSION_CODE
            )
        }

         fun getCameraPermission(activity: Activity): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }
        fun requestCameraPermissions(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.CAMERA
                ),
                Constants.PermissionRequestCodes.CAMERA_PERMISSION_CODE
            )
        }

        fun getCallPermission(activity: Activity):Boolean{
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
            return false
        }
        fun requestCallPermissions(activity: Activity) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.CALL_PHONE
                ),
                Constants.PermissionRequestCodes.CALL_PHONE_PERMISSION_CODE
            )
        }

        fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
                for (permission in permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) !=
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        return false
                    }
                }
            }
            return true
        }

    }
}