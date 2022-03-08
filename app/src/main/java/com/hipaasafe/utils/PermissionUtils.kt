package com.hipaasafe.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
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

    }
}