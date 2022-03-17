package com.hipaasafe.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.Call
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User
import com.google.android.material.snackbar.Snackbar
import com.hipaasafe.AppConfig
import com.hipaasafe.Constants
import com.hipaasafe.R
import com.hipaasafe.call_manager.CometChatCallActivity
import com.hipaasafe.call_manager.CometChatStartCallActivity
import com.hipaasafe.presentation.comet_chat_main_screen.MainCometChatActivity
import com.hipaasafe.presentation.home_screen.HomeActivity
import org.json.JSONObject
import java.io.File
import java.io.IOException

class CometChatUtils {
    companion object{
        private const val TAG = "CometChatUtils"

        fun startCallIntent(
            context: Context, user: User, type: String?,
            isOutgoing: Boolean, sessionId: String
        ) {
            val videoCallIntent = Intent(context, CometChatCallActivity::class.java)
            videoCallIntent.putExtra(Constants.CometChatConstant.NAME, user.name)
            videoCallIntent.putExtra(Constants.CometChatConstant.UID, user.uid)
            videoCallIntent.putExtra(Constants.CometChatConstant.SESSION_ID, sessionId)
            videoCallIntent.putExtra(Constants.CometChatConstant.AVATAR, user.avatar)
            videoCallIntent.action = type
            videoCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (isOutgoing) {
                videoCallIntent.type = "outgoing"
            } else {
                videoCallIntent.type = "incoming"
            }
            context.startActivity(videoCallIntent)
        }
        fun startGroupIntent(group: Group,context: Context) {
            val intent = Intent(context, MainCometChatActivity::class.java)
            intent.putExtra(Constants.CometChatConstant.GUID, group.guid)
            intent.putExtra(Constants.CometChatConstant.GROUP_OWNER, group.owner)
            intent.putExtra(Constants.CometChatConstant.AVATAR, group.icon)
            intent.putExtra(Constants.CometChatConstant.NAME, group.name)
            intent.putExtra(Constants.CometChatConstant.GROUP_TYPE, group.groupType)
            intent.putExtra(Constants.CometChatConstant.TYPE, CometChatConstants.RECEIVER_TYPE_GROUP)
            intent.putExtra(Constants.CometChatConstant.MEMBER_COUNT, group.membersCount)
            intent.putExtra(Constants.CometChatConstant.GROUP_DESC, group.description)
            intent.putExtra(Constants.CometChatConstant.GROUP_PASSWORD, group.password)
            context.startActivity(intent)
        }
        fun userIntent(user: User,context: Context) {
            val intent = Intent(context, MainCometChatActivity::class.java)
            intent.putExtra(Constants.CometChatConstant.UID, user.uid)
            intent.putExtra(Constants.CometChatConstant.AVATAR, user.avatar)
            intent.putExtra(Constants.CometChatConstant.STATUS, user.status)
            intent.putExtra(Constants.CometChatConstant.NAME, user.name)
            intent.putExtra(Constants.CometChatConstant.TYPE, CometChatConstants.RECEIVER_TYPE_USER)
            context.startActivity(intent)
        }

        fun getSenderName(data: JSONObject): String? {
            val entities = data.getJSONObject("entities")
            val sender = entities.getJSONObject("sender")
            val entity = sender.getJSONObject("entity")
            val name = entity.getString("name")
            return name
        }

        fun startGroupCallIntent(
            context: Context, group: Group, type: String?,
            isOutgoing: Boolean, sessionId: String
        ) {
            val videoCallIntent = Intent(context, CometChatCallActivity::class.java)
            videoCallIntent.putExtra(Constants.CometChatConstant.NAME, group.name)
            videoCallIntent.putExtra(Constants.CometChatConstant.UID, group.guid)
            videoCallIntent.putExtra(Constants.CometChatConstant.SESSION_ID, sessionId)
            videoCallIntent.putExtra(Constants.CometChatConstant.AVATAR, group.icon)
            videoCallIntent.action = type
            videoCallIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            if (isOutgoing) {
                videoCallIntent.type = "outgoing"
            } else {
                videoCallIntent.type = "incoming"
            }
            context.startActivity(videoCallIntent)
        }

        fun getFileName(context: Context?, uri: Uri): String? {
            val mimeType = context?.contentResolver?.getType(uri)
            var filename: String? = null
            if (mimeType == null && context != null) {
                val path = getPath(context, uri)
                filename = if (path == null) {
                    getName(uri.toString())
                } else {
                    val file = File(path)
                    file.name
                }
            } else {
                val returnCursor = context?.contentResolver?.query(
                    uri, null,
                    null, null, null
                )
                if (returnCursor != null) {
                    val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    returnCursor.moveToFirst()
                    filename = returnCursor.getString(nameIndex)
                    returnCursor.close()
                }
            }
            return filename
        }

        fun getPath(context: Context?, uri: Uri): String? {
            val absolutePath = getImagePathFromUri(context, uri)
            return absolutePath ?: uri.toString()
        }

        fun getImagePathFromUri(context: Context?, aUri: Uri?): String? {
            var imagePath: String? = null
            if (aUri == null) {
                return imagePath
            }
            if (DocumentsContract.isDocumentUri(context, aUri)) {
                val documentId = DocumentsContract.getDocumentId(aUri)
                if ("com.android.providers.media.documents" == aUri.authority) {
                    val id = DocumentsContract.getDocumentId(aUri)
                    if (id != null && id.startsWith("raw:")) {
                        return id.substring(4)
                    }
                    val contentUriPrefixesToTry = arrayOf(
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads"
                    )
                    for (contentUriPrefix in contentUriPrefixesToTry) {
                        val contentUri = ContentUris.withAppendedId(
                            Uri.parse(contentUriPrefix),
                            java.lang.Long.valueOf(id!!)
                        )
                        try {
                            val path = MediaUtilsCometChat.getDataColumn(context!!, contentUri, null, null)
                            if (path != null) {
                                return path
                            }
                        } catch (e: java.lang.Exception) {
                        }
                    }

                    // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                    val fileName: String = getFileName(context!!, aUri)!!
                    val cacheDir: File = getDocumentCacheDir(context)!!
                    val file: File = generateFileName(fileName, cacheDir)!!
                    var destinationPath: String? = null
                    if (file != null) {
                        destinationPath = file.absolutePath
                        MediaUtilsCometChat.saveFileFromUri(context, aUri, destinationPath)
                    }
                    imagePath = destinationPath
                } else if ("com.android.providers.downloads.documents" == aUri.authority) {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        java.lang.Long.valueOf(documentId)
                    )
                    imagePath = getImagePath(contentUri, null!!, context!!)
                }
            } else if ("content".equals(aUri.scheme, ignoreCase = true)) {
                imagePath = getImagePath(aUri, null!!, context!!)
            } else if ("file".equals(aUri.scheme, ignoreCase = true)) {
                imagePath = aUri.path
            }
            return imagePath
        }

        @SuppressLint("Range")
        private fun getImagePath(aUri: Uri, aSelection: String, context: Context): String? {
            try {
                var path: String? = null
                val cursor = context.contentResolver.query(aUri, null, aSelection, null, null)
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    }
                    cursor.close()
                }
                return path
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun generateFileName(name: String?, directory: File?): File? {
            var name = name ?: return null
            var file = File(directory, name)
            if (file.exists()) {
                var fileName = name
                var extension = ""
                val dotIndex = name.lastIndexOf('.')
                if (dotIndex > 0) {
                    fileName = name.substring(0, dotIndex)
                    extension = name.substring(dotIndex)
                }
                var index = 0
                while (file.exists()) {
                    index++
                    name = "$fileName($index)$extension"
                    file = File(directory, name)
                }
            }
            try {
                if (!file.createNewFile()) {
                    return null
                }
            } catch (e: IOException) {
                return null
            }
            return file
        }

        fun getDocumentCacheDir(context: Context): File? {
            val dir = File(context.cacheDir, "documents")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            return dir
        }


        fun getName(filename: String?): String? {
            if (filename == null) {
                return null
            }
            val index = filename.lastIndexOf('/')
            return filename.substring(index + 1)
        }

        fun startVideoCallIntent(context: Context, sessionId: String?) {
            val intent = Intent(context, CometChatStartCallActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(Constants.CometChatConstant.SESSION_ID, sessionId)
            context.startActivity(intent)
        }


        fun initiateCall(
            context: Context,
            receiverID: String,
            receiverType: String?,
            callType: String?
        ) {
            val call = Call(receiverID, receiverType, callType)
            val jsonObject = JSONObject()
            try {
                jsonObject.put("bookingId", 6)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            call.metadata = jsonObject
            CometChat.initiateCall(call, object : CometChat.CallbackListener<Call>() {
                override fun onSuccess(call: Call) {
                    if (receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                        AppUtils.INSTANCE?.startGroupCallIntent(
                            context,
                            call.callReceiver as Group,
                            call.type,
                            true,
                            call.sessionId
                        )
                    } else {
                        AppUtils.INSTANCE?.startCallIntent(
                            context,
                            call.callReceiver as User,
                            call.type,
                            true,
                            call.sessionId
                        )
                    }
                }

                override fun onError(e: CometChatException) {
                    Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }


        fun logoutFromComet() {
            CometChat.logout(object : CometChat.CallbackListener<String?>() {
                override fun onSuccess(p0: String?) {
                    AppUtils.INSTANCE?.logMe(TagName.COMET_SDK, "Logout from comet successfull")

                }

                override fun onError(e: CometChatException?) {
                    AppUtils.INSTANCE?.logMe(
                        TagName.COMET_SDK,
                        "Logout from comet failure : " + e?.localizedMessage
                    )
                }
            })
        }

        fun loginToComet(userId: String?, name: String?,listener:CometListener?,token:String) {
//            CometChat.login(userId?:"",AppConfig.AppDetails.AUTH_KEY,object:CometChat.CallbackListener<User?>(){
            /****
             * shailesh - sudhir
             * vijay
             * nakash
             * jigesh - suleman
             */
            CometChat.login(
                userId ?: "",
                AppConfig.AppDetails.AUTH_KEY,
                object : CometChat.CallbackListener<User?>() {
                    override fun onSuccess(p0: User?) {
                        AppUtils.INSTANCE?.logMe(TagName.COMET_SDK, "Login from comet successfull")
                        listener?.onCometLoginSuccess()
                        CometChat.registerTokenForPushNotification(token, object : CometChat.CallbackListener<String?>() {
                            override fun onSuccess(s: String?) {
                                Log.e("onSuccessPN: ", s.toString())
                            }

                            override fun onError(e: CometChatException) {
                                Log.e("onErrorPN: ", e.message.toString())
                            }
                        })
                    }

                    override fun onError(e: CometChatException?) {
                        listener?.onCometLoginFailure()
                        AppUtils.INSTANCE?.logMe(
                            TagName.COMET_SDK,
                            "Login from comet failure : " + e?.localizedMessage
                        )
                        when (e?.code) {
                            "ERR_UID_NOT_FOUND" -> {
                                /*** User is not present in db,
                                 * This scenario will not come as user is registered into comet from backend, once registration is done.
                                 * ****/
//                            registerToComet(userId,name)
                            }
                        }
                    }
                })
        }


        fun initiatecall(
            context: Context,
            recieverID: String?,
            receiverType: String?,
            callType: String?
        ) {
            val call = Call(recieverID!!, receiverType, callType)
            val jsonObject = JSONObject()
            try {
                jsonObject.put("bookingId", 6)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            call.metadata = jsonObject
            CometChat.initiateCall(call, object : CometChat.CallbackListener<Call>() {
                override fun onSuccess(call: Call) {
                    if (receiverType == CometChatConstants.RECEIVER_TYPE_GROUP) {
                        startGroupCallIntent(
                            context,
                            call.callReceiver as Group,
                            call.type,
                            true,
                            call.sessionId
                        )
                    } else {
                        startCallIntent(
                            context,
                            call.callReceiver as User,
                            call.type,
                            true,
                            call.sessionId
                        )
                    }
                }

                override fun onError(e: CometChatException) {
                    Log.e(TAG, "onError: " + e.message)
                    Snackbar.make(
                        (context as Activity).window.decorView.rootView,
                        context.getResources()
                            .getString(R.string.call_initiate_error) + ":" + e.message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            })
        }
    }
}

interface CometListener{
    fun onCometLoginSuccess()
    fun onCometLoginFailure()
}