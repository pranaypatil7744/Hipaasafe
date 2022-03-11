package com.hipaasafe.utils

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.base.BaseActivity
import com.hipaasafe.utils.PermissionUtils.Companion.getCameraPermission
import com.hipaasafe.utils.PermissionUtils.Companion.getStoragePermission
import com.hipaasafe.utils.PermissionUtils.Companion.requestCameraPermissions
import com.hipaasafe.utils.PermissionUtils.Companion.requestStoragePermissions
import java.util.*

class AddImageUtils : BaseActivity() {

    var isSelfieCamera: Boolean = false
    private var isPdf: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceUtils = PreferenceUtils(this)
        getIntentData()
    }

    private fun getIntentData() {
        intent.extras?.run {
            isPdf = getBoolean(Constants.IS_PDF, false)
            val isCamera: Boolean = getBoolean(Constants.IS_CAMERA, false)
            isSelfieCamera = getBoolean(Constants.IS_SELFIE_CAMERA, false)
            if (isPdf) {
                pickPdfFile()
            } else {
                if (isCamera) {
                    captureImage()
                } else {
                    pickImage()
                }
            }
        }
    }

    private fun pickPdfFile() {
        if (getStoragePermission(this)) {
            preferenceUtils.setValue(Constants.AskedPermission.STORAGE_PERMISSION_COUNT, 0)
            openPdfPicker()
        } else {
            requestStoragePermissions(this)
        }
    }

    private fun openPdfPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        pickPdfResult.launch(intent)
    }

    private fun pickImage() {
        if (getStoragePermission(this)) {
            preferenceUtils.setValue(Constants.AskedPermission.STORAGE_PERMISSION_COUNT, 0)
            openGallery()
        } else {
            requestStoragePermissions(this)
        }
    }

    private fun captureImage() {
        if (getCameraPermission(this)) {
            preferenceUtils.setValue(Constants.AskedPermission.CAMERA_PERMISSION_COUNT, 0)
            openCamera()
        } else {
            requestCameraPermissions(this)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = ImageUtils.INSTANCE?.createFile(this)
        var uri: Uri? = null
        file?.let {
            mCurrentPhotoPath = file.absolutePath
            uri = FileProvider.getUriForFile(
                this,
                Constants.AUTHORITY,
                it
            )
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        if (isSelfieCamera) {
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1)
        }
        captureImageResult.launch(intent)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageResult.launch(intent)
    }

    private val captureImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val bundle = Bundle()
                if (!TextUtils.isEmpty(mCurrentPhotoPath)) {
                    bundle.putString(
                        Constants.IntentExtras.EXTRA_FILE_NAME,
                        mCurrentPhotoPath?.split("/")?.last()
                    )
//                    val imageZipperFile = ImageZipper(this)
//                        .setQuality(40)
//                        .setMaxWidth(300)
//                        .setMaxHeight(700)
//                        .compressToFile(File(mCurrentPhotoPath.toString()))
                    bundle.putString(
                        Constants.IntentExtras.EXTRA_FILE_PATH,
                        mCurrentPhotoPath.toString()
                    )
                } else {
                    val imgBitmap = it.data?.extras?.get("data") as Bitmap
                    val uri: Uri? = it?.data?.data
                    bundle.putString(Constants.IntentExtras.EXTRA_FILE_NAME, "document.jpeg")
                    bundle.putString(Constants.IntentExtras.EXTRA_FILE_PATH, uri?.path)
                    bundle.putString(
                        Constants.IntentExtras.EXTRA_IMAGE_BIT_MAP,
                        ImageUtils.INSTANCE?.bitMapToString(imgBitmap)
                    )
                }
                val resultIntent = intent
                resultIntent.putExtras(bundle)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }
    private val pickPdfResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val filePath = it.data?.data as Uri
                val resultIntent = Intent()
                val realPath = MediaUtilsCometChat.getRealPath(this, filePath)
                val fileName = realPath.toString().split("/").last()

                val bundle = Bundle()
                bundle.putString(Constants.IntentExtras.EXTRA_FILE_PATH, realPath.toString())
                bundle.putString(
                    Constants.IntentExtras.EXTRA_FILE_NAME,
                    if (fileName.contains(".pdf")) fileName else Date().time.toString() + ".pdf"
                )
                resultIntent.putExtras(bundle)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }

    private val pickImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it?.resultCode == Activity.RESULT_OK) {
                val imgUri = it.data?.data as Uri
//                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imgUri)
//
//                val imageAbsolutePath: String? =
//                    ImageUtils.INSTANCE?.saveImage(bitmap, this)
                val realPath = MediaUtilsCometChat.getRealPath(this, imgUri)
                val bundle = Bundle()
                bundle.putString(
                    Constants.IntentExtras.EXTRA_FILE_NAME,
                    realPath.toString().split("/").last()
                )
//                    val imageZipperFile = ImageZipper(this)
//                        .setQuality(30)
//                        .setMaxWidth(300)
//                        .setMaxHeight(700)
//                        .compressToFile(File(imageAbsolutePath))
                bundle.putString(
                    Constants.IntentExtras.EXTRA_FILE_PATH,
                    realPath.toString()
                )
                val resultIntent = Intent()
                resultIntent.putExtras(bundle)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                setResult(Activity.RESULT_CANCELED, intent)
                finish()
            }
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.PermissionRequestCodes.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                val cameraPerCount = preferenceUtils.getIntValue(
                    Constants.AskedPermission.CAMERA_PERMISSION_COUNT,
                    0
                )
                preferenceUtils.setValue(
                    Constants.AskedPermission.CAMERA_PERMISSION_COUNT,
                    cameraPerCount.plus(1)
                )
                if (preferenceUtils.getIntValue(
                        Constants.AskedPermission.CAMERA_PERMISSION_COUNT,
                        0
                    ) >= 2
                ) {
                    DialogUtils.showPermissionDialog(
                        this,
                        getString(R.string.please_grant_the_camera_permission_to_continue),
                        getString(R.string.allow_permission),
                        getString(R.string.go_to_settings),
                        getString(R.string.deny)
                    )
                } else {
                    showLongToast(
                        this,
                        getString(R.string.please_grant_the_camera_permission_to_continue)
                    )
                    finish()
                }
                setResult(Activity.RESULT_CANCELED, intent)
            }
        } else if (requestCode == Constants.PermissionRequestCodes.STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                if (isPdf) {
                    pickPdfFile()
                } else {
                    openGallery()
                }
            } else {
                val galleryPerCount = preferenceUtils.getIntValue(
                    Constants.AskedPermission.STORAGE_PERMISSION_COUNT,
                    0
                )
                preferenceUtils.setValue(
                    Constants.AskedPermission.STORAGE_PERMISSION_COUNT,
                    galleryPerCount.plus(1)
                )
                if (preferenceUtils.getIntValue(
                        Constants.AskedPermission.STORAGE_PERMISSION_COUNT,
                        0
                    ) >= 2
                ) {
                    DialogUtils.showPermissionDialog(
                        this,
                        getString(R.string.please_grant_the_storage_permission_to_continue),
                        getString(R.string.allow_permission),
                        getString(R.string.go_to_settings),
                        getString(R.string.deny)
                    )
                } else {
                    showLongToast(
                        this,
                        getString(R.string.please_grant_the_storage_permission_to_continue)
                    )
                    finish()
                }
                setResult(Activity.RESULT_CANCELED, intent)
            }
        }
    }

}