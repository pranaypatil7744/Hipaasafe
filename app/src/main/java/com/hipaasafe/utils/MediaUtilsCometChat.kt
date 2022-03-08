package com.hipaasafe.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.hipaasafe.R
import com.hipaasafe.settings.CometChatFeatureRestriction
import java.io.*
import java.util.*
import kotlin.math.min

class MediaUtilsCometChat {

    companion object {

        fun playSendSound(context: Context?, ringId: Int) {
            if (CometChatFeatureRestriction.isMessagesSoundEnabled()) {
                val mMediaPlayer = MediaPlayer.create(context, ringId)
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
                mMediaPlayer.start()
                mMediaPlayer.setOnCompletionListener { mediaPlayer ->
                    var mp = mediaPlayer
                    if (mediaPlayer != null) {
                        mediaPlayer.stop()
                        mediaPlayer.release()
                        mp = null
                    }
                }
            }
        }

        fun getRealPath(context: Context?, fileUri: Uri?): File {
            Log.d("", "getRealPath: " + fileUri?.path)
            val realPath: String
            if (fileUri?.let { isGoogleDrive(it) } == true) {
                return saveDriveFile(context!!, fileUri)
            } else if (Build.VERSION.SDK_INT < 30) {
                realPath = fileUri?.let { getRealPathFromURI(context!!, it) }!!
            } else {
                //(Build.VERSION.SDK_INT == 30)
                realPath = fileUri?.let { getRealPathFromN(context, it) }!!
            }

            return File(realPath)
        }


        @RequiresApi(Build.VERSION_CODES.R)
        private fun getRealPathFromN(context: Context?, uri: Uri): String? {
            val returnUri = uri
            val returnCursor = context?.contentResolver?.query(
                returnUri,
                null, null, null, null
            )
            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            /*
             * Get the column indexes of the data in the Cursor,
             *     * move to the first row in the Cursor, get the data,
             *     * and display it.
             * */
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
            returnCursor?.moveToFirst()
            val name = returnCursor?.getString(nameIndex!!)
            val size = returnCursor?.getLong(sizeIndex!!)
            val file = File(context?.filesDir, name)
            try {
                val inputStream = context?.contentResolver?.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable = inputStream!!.available()

                //int bufferSize = 1024;
                val bufferSize = min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream.read(buffers).also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                Log.e("File Size", "Size " + file.length())
                inputStream.close()
                outputStream.close()
                Log.e("File Path", "Path " + file.path)
                Log.e("File Size", "Size " + file.length())
            } catch (e: Exception) {
                Log.e("Exception", e.message!!)
            }
            return file.path
        }

        private fun getRealPathFromURI(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
                    var id = DocumentsContract.getDocumentId(uri)
                    if (id != null) {
                        if (id.startsWith("raw:")) {
                            return id.substring(4)
                        }
                        if (id.startsWith("msf:")) {
                            id = id.substring(4)
                        }
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
                            val path: String = getDataColumn(
                                context,
                                contentUri,
                                null,
                                null
                            )!!
                            if (path != null) {
                                return path
                            }
                        } catch (e: Exception) {
                        }
                    }

                    // path could not be retrieved using ContentResolver, therefore copy file to accessible cache using streams
                    val fileName = CometChatUtils.getFileName(context, uri)
                    val cacheDir = CometChatUtils.getDocumentCacheDir(context)
                    val file = CometChatUtils.generateFileName(fileName, cacheDir)
                    var destinationPath: String? = null
                    if (file != null) {
                        destinationPath = file.absolutePath
                        saveFileFromUri(context, uri, destinationPath)
                    }
                    return destinationPath
                } else if (isMediaDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(
                        split[1]
                    )
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {

                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                    context,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return null
        }

        private fun isGooglePhotosUri(uri: Uri): Boolean {
            return "com.google.android.apps.photos.content" == uri.authority
        }

        fun saveFileFromUri(context: Context, uri: Uri, destinationPath: String) {
            var ips: InputStream? = null
            var bos: BufferedOutputStream? = null
            try {
                ips = context.contentResolver.openInputStream(uri)
                bos = BufferedOutputStream(FileOutputStream(destinationPath, false))
                val buf = ByteArray(1024)
                ips!!.read(buf)
                do {
                    bos.write(buf)
                } while (ips.read(buf) != -1)
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    ips?.close()
                    bos?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun getDataColumn(
            context: Context,
            uri: Uri?,
            selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(
                column
            )
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection, selection, selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }

        private fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        private fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        private fun isGoogleDrive(uri: Uri): Boolean {
            return uri.authority!!.contains("com.google.android.apps.docs.storage")
        }

        fun saveDriveFile(context: Context?, uri: Uri?): File {
            var file: File? = null
            try {
                if (uri != null) {
                    file = File(context?.cacheDir, CometChatUtils.getFileName(context, uri).toString())
                    val inputStream = context?.contentResolver?.openInputStream(uri)
                    try {
                        val output: OutputStream = FileOutputStream(file)
                        try {
                            val buffer = ByteArray(4 * 1024) // or other buffer size
                            var read: Int
                            while (inputStream!!.read(buffer).also { read = it } != -1) {
                                output.write(buffer, 0, read)
                            }
                            output.flush()
                        } finally {
                            output.close()
                        }
                    } finally {
                        inputStream!!.close()
                        //Upload Bytes.
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(context, "File Uri is null", Toast.LENGTH_LONG).show()
            }
            return file!!
        }

        fun getMimeType(context: Context, uri: Uri): String? {
            var mimeType: String? = null
            mimeType = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
                val cr: ContentResolver = context.contentResolver
                cr.getType(uri)
            } else {
                val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                    uri.toString()
                )
                MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase(Locale.ROOT)
                )
            }
            return mimeType
        }

//        fun setFileIconFromMime(fileType: String, imgFile: ImageView) {
//            when (fileType) {
//                "text/plane", "text/html" -> {
//                    imgFile.setImageResource(R.drawable.ic_img_txt)
//                }
//                "application/pdf" -> {
//                    imgFile.setImageResource(R.drawable.img_pdf)
//                }
//                "application/msword", "application/vnd.ms-word.document.macroEnabled.12",
//                "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> {
//                    imgFile.setImageResource(R.drawable.img_word)
//                }
//                "application/vnd.ms.excel",
//                "application/vnd.ms-excel.sheet.macroEnabled.12",
//                "application/vnd.ms-excel.sheet.binary.macroEnabled.12",
//                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" -> {
//                    imgFile.setImageResource(R.drawable.img_xls)
//                }
//                "application/mspowerpoint", "application/vnd.ms-powerpoint",
//                "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> {
//                    imgFile.setImageResource(R.drawable.img_ppt)
//                }
//                "application/zip" -> {
//                    imgFile.setImageResource(R.drawable.img_zip)
//                }
//                else -> {
//                    imgFile.setImageResource(R.drawable.ic_img_txt)
//                }
//            }
//        }
    }
}