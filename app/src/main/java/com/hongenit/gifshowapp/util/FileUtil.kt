package com.hongenit.gifshowapp.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.text.TextUtils
import com.hongenit.gifshowapp.Constants
import java.io.*

/**
 * Created by Xiaohong on 2019-08-08.
 * desc:
 */
object FileUtil {


    @Throws(IOException::class)
    fun createFile(parent: File): File? {
        val tempFile = File(parent, System.currentTimeMillis().toString() + "")
        var parentExists = parent.exists() && parent.isDirectory
        if (!parentExists) {
            parentExists = parent.mkdirs()
        }
        return if (parentExists) {
            if (tempFile.createNewFile()) tempFile else null
        } else null
    }


    fun createFileShareUri(context: Context, file: File): Uri {
        return if (AndroidVersion.hasNougat()) FileProvider.getUriForFile(
            context,
            Constants.FILE_PROVIDER_AUTHORITIES,
            file
        ) else Uri.fromFile(file)
    }


    @Throws(IOException::class)
    fun copyFromUri(context: Context, uri: Uri, outFile: File) {
        var path: String? = null
        try {
            path = getRealPath(context, uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!TextUtils.isEmpty(path)) {
            val file = File(path!!)
            copyFile(file, outFile)
        } else {
            var fis: FileInputStream? = null
            try {
                val pfd = context.contentResolver.openFileDescriptor(uri, "r")
                if (pfd != null) {
                    val fd = pfd.fileDescriptor
                    fis = FileInputStream(fd)
                    copyToFile(fis, outFile)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (fis != null) {
                    try {
                        fis.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
        }
    }


    @Throws(IOException::class)
    fun copyFile(inFile: File, out: File): Boolean {
        val fis = FileInputStream(inFile)
        return copyToFile(fis, out)
    }

    @Throws(IOException::class)
    fun copyToFile(inputStream: InputStream?, target: File?): Boolean {
        if (target == null) {
            throw IOException()
        }
        createNewFile(target)
        val fos = FileOutputStream(target)
        try {
            copy(inputStream!!, fos)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            try {
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return false
    }


    fun createNewFile(file: File): Boolean {
        var complete = false
        if (!file.exists() || file.isDirectory) {
            val parent = file.parentFile
            var dirExist = parent.exists() && parent.isDirectory
            if (!dirExist) {
                dirExist = parent.mkdirs()
            }
            if (dirExist) {
                try {
                    complete = file.createNewFile()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return complete
    }


    /**
     * will not close stream
     *
     * @param in
     * @param fos
     * @throws IOException
     */
    @Throws(IOException::class)
    fun copy(inputStream: InputStream, fos: FileOutputStream) {
        val buffer = ByteArray(1024)
        var len = 0
        while ({ len = inputStream.read(buffer); len }() != -1) {
            fos.write(buffer, 0, len)
        }
        fos.flush()
    }


    @SuppressLint("NewApi")
    fun getRealPath(context: Context, uri: Uri): String? {
        if (uri.toString().startsWith("content://com.google.android.apps.photos.content")) {
            return null
        }
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {

        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)

        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun getContentUriRealDiskPath(context: Context, uri: Uri): String? {
        if (uri.toString().startsWith("content://com.google.android.apps.photos.content")) {
            return null
        }
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]

                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {

                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )

                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
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
                val selectionArgs = arrayOf(split[1])

                return getDataColumn(context, contentUri, selection, selectionArgs)
            }// MediaProvider
            // DownloadsProvider
        } else if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            return getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }// File
        // MediaStore (and general)

        return null
    }


}