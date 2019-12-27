package com.hongenit.gifshowapp.util.album

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.hongenit.gifshowapp.BaseActivity
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.callback.PermissionListener
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.util.GlobalUtil
import cropper.CropImage

/**
 * 展示手机相册的Activity。
 *
 */
class AlbumActivity : BaseActivity() {

    private var cropWidth: Int = 0

    private var cropHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)
        GlobalUtil.setStatusBarFullTransparent(this)
        refreshPermissionStatus()
    }

    override fun setupViews() {
        setupToolbar()
        cropWidth = intent.getIntExtra(CROP_WIDTH, 0)
        cropHeight = intent.getIntExtra(CROP_HEIGHT, 0)
    }

    private fun refreshPermissionStatus() {
        handlePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionListener {
            override fun onGranted() {
                permissionsGranted()
            }

            override fun onDenied(deniedPermissions: List<String>) {
//                val fragment = NeedPermissionFragment()
//                fragment.setPermissions(deniedPermissions.toTypedArray())
//                replaceFragment(fragment)
                showToast(getString(R.string.no_permission_cant_continue))
            }
        })
    }

    fun permissionsGranted() {
        val fragment = AlbumFragment()
        fragment.setCropSize(cropWidth, cropHeight)
        replaceFragment(fragment)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_PERMISSION_SETTING -> refreshPermissionStatus()
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_CANCELED) {
                    return
                }
                val intent = Intent()
                val result = CropImage.getActivityResult(data)
                if (resultCode == RESULT_OK) {
                    intent.putExtra(IMAGE_PATH, result.uri.path)
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    logWarn(TAG, "Cropping failed: " + result.error.message, result.error)
                }
                setResult(resultCode, intent)
                finish()
            }
            else -> {
            }
        }
    }

    companion object {

        private const val TAG = "AlbumActivity"

        const val REQUEST_PERMISSION_SETTING = 1

        const val IMAGE_PATH = "image_path"

        const val CROP_WIDTH = "crop_width"

        const val CROP_HEIGHT = "crop_height"

        fun actionStartForResult(activity: Activity, requestCode: Int, cropWidth: Int, cropHeight: Int) {
            val intent = Intent(activity, AlbumActivity::class.java)
            intent.putExtra(CROP_WIDTH, cropWidth)
            intent.putExtra(CROP_HEIGHT, cropHeight)
            activity.startActivityForResult(intent, requestCode)
        }
    }


}
