package com.hongenit.gifshowapp.meinfo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hongenit.gifshowapp.BaseActivity
import com.hongenit.gifshowapp.Constants
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.extension.logDebug
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.ResponseHandler
import com.hongenit.gifshowapp.util.*
import com.hongenit.gifshowapp.util.album.AlbumActivity
import cropper.CropImage
import cropper.CropImageView
import kotlinx.android.synthetic.main.activity_modify_user_info.*
import java.io.File
import java.io.IOException

/**
 * 用户修改个人信息的界面，包括昵称、个人简介、头像、背景等。
 *
 */
class ModifyUserInfoActivity : BaseActivity(), View.OnClickListener {

    private var mUserId: String = ""

    private var srcNickname: String = ""

    private var srcAvatar: String? = ""

    private var srcBgImage: String = ""

    private var srcDescription: String? = ""

    private var userAvatarPath: String = ""

    private var userBgImagePath: String = ""

    private var isEditDescription: Boolean = false

    private var photoUri: Uri? = null

    private var action: Int = 0

    private var nicknameEditWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val length = nicknameEdit.text.toString().trim { it <= ' ' }.length
            nicknameLimitText.text =
                String.format(getString(R.string.nickname_length_limit), length)
            userNickname.text = s.toString().trim { it <= ' ' }
        }
    }

    private var descriptionEditWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            val length = descriptionEdit.text.toString().trim { it <= ' ' }.length
            descriptionLimitText.text =
                String.format(getString(R.string.description_length_limit), length)
            if (TextUtils.isEmpty(s.toString().trim { it <= ' ' })) {
                userDescription.text = ""
                userDescription.visibility = View.GONE
            } else {
                userDescription.text =
                    String.format(
                        getString(R.string.description_content),
                        s.toString().trim { it <= ' ' })
                userDescription.visibility = View.VISIBLE
            }
        }
    }

//    private var userBgLoadListener: RequestListener<Any, Bitmap> = object : RequestListener<Any, Bitmap> {
//        override fun onException(e: Exception?, model: Any, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
//            return false
//        }
//
//        override fun onResourceReady(bitmap: Bitmap?, model: Any, target: Target<Bitmap>,
//                                     isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
//            if (bitmap == null) {
//                return false
//            }
//            val bitmapWidth = bitmap.width
//            val bitmapHeight = bitmap.height
//            if (bitmapWidth <= 0 || bitmapHeight <= 0) {
//                return false
//            }
//            Palette.from(bitmap)
//                    .maximumColorCount(3)
//                    .clearFilters()
//                    .setRegion(0, 0, bitmapWidth - 1, (bitmapHeight * 0.1).toInt()) // 测量图片头部的颜色，以确定状态栏和导航栏的颜色
//                    .generate { palette ->
//                        val isDark = ColorUtils.isBitmapDark(palette, bitmap)
//                        if (isDark) {
//                            save.setTextColor(ContextCompat.getColorStateList(this@ModifyUserInfoActivity, R.color.save_bg_light))
//                            setToolbarAndStatusbarIconIntoLight()
//                        } else {
//                            save.setTextColor(ContextCompat.getColorStateList(this@ModifyUserInfoActivity, R.color.save_bg_dark))
//                            setToolbarAndStatusbarIconIntoDark()
//                        }
//                    }
//
//            val left = (bitmapWidth * 0.2).toInt()
//            val right = bitmapWidth - left
//            val top = bitmapHeight / 2
//            val bottom = bitmapHeight - 1
//            logDebug(TAG, "text area top $top , bottom $bottom , left $left , right $right")
//            Palette.from(bitmap)
//                    .maximumColorCount(3)
//                    .clearFilters()
//                    .setRegion(left, top, right, bottom) // 测量图片下半部分的颜色，以确定用户信息的颜色
//                    .generate { palette ->
//                        val isDark = ColorUtils.isBitmapDark(palette, bitmap)
//                        val color: Int
//                        color = if (isDark) {
//                            ContextCompat.getColor(this@ModifyUserInfoActivity, R.color.white_text)
//                        } else {
//                            ContextCompat.getColor(this@ModifyUserInfoActivity, R.color.primary_text)
//                        }
//                        userNickname.setTextColor(color)
//                        userDescription.setTextColor(color)
//                    }
//            return false
//        }
//    }

    /**
     * 判断用户是否修改了个人信息。
     * @return 修改了个人信息返回true，否则返回false。
     */
    private val isUserInfoChanged: Boolean
        get() {
            val newDesc = descriptionEdit.text.toString()
            return !(nicknameEdit.text.toString() == srcNickname
                    && (newDesc == srcDescription || newDesc.isNullOrEmpty())
                    && TextUtils.isEmpty(userAvatarPath))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isEditDescription = intent.getBooleanExtra(EDIT_DESCRIPTION, false)
        val signInUser = UserModel.getSignInUser()
        mUserId = signInUser.user_id
        srcNickname = signInUser.nickname
        srcAvatar = signInUser.avatar
        srcDescription = signInUser.description
        if (TextUtils.isEmpty(mUserId)) { // 参数校验不合法
            showToast(getString(R.string.user_info_status_abnormal))
            finishActivity()
        }
        setContentView(R.layout.activity_modify_user_info)
        GlobalUtil.setStatusBarFullTransparent(this)
    }

    override fun setupViews() {
        setupToolbar()
        title = ""
//        transparentStatusBar()

        // load and setup data
//        loadBgImage()
        Glide.with(this)
            .load(srcAvatar)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .placeholder(R.drawable.loading_bg_circle)
            .error(R.drawable.avatar_default)
            .into(userAvatar)
        userNickname.text = srcNickname
        nicknameEdit.setText(srcNickname)
        if (TextUtils.isEmpty(srcDescription)) {
            userDescription.visibility = View.GONE
        } else {
            descriptionEdit.setText(srcDescription)
            userDescription.visibility = View.VISIBLE
            userDescription.text =
                String.format(getString(R.string.description_content), srcDescription)
        }

        nicknameLimitText.text =
            String.format(getString(R.string.nickname_length_limit), nicknameEdit.text!!.length)
        descriptionLimitText.text =
            String.format(
                getString(R.string.description_length_limit),
                descriptionEdit.text!!.length
            )

        // setup events
        save.setOnClickListener(this)
        avatarCamera.setOnClickListener(this)
        bgImageCamera.setOnClickListener(this)
        nicknameEdit.addTextChangedListener(nicknameEditWatcher)
        descriptionEdit.addTextChangedListener(descriptionEditWatcher)

        if (isEditDescription) {
            srcDescription?.let {
                descriptionEdit.setSelection(it.length)
            }
            GlobalParam.handler.postDelayed({ showSoftKeyboard(descriptionEdit) }, 100)
        } else {
            rootLayout.requestFocus()
        }
    }

//    private fun loadBgImage() {
//        if (TextUtils.isEmpty(srcBgImage)) {
//            if (!TextUtils.isEmpty(srcAvatar)) {
//                Glide.with(this)
//                    .load(srcAvatar)
//                    .transform(BlurTransformation( 15))
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                    .listener(userBgLoadListener)
//                    .into(userBgImage)
//            }
//        } else {
//            Glide.with(this)
//                .load(srcBgImage)
//                .asBitmap()
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .listener(userBgLoadListener)
//                .into(userBgImage)
//        }
//    }

    override fun onClick(v: View) {
        when (v.id) {
            avatarCamera.id -> {
                action = TAKE_AVATAR_PICTURE
                showTakePictureDialog()
            }
            bgImageCamera.id -> {
                action = TAKE_BG_IMAGE_PICTURE
                showTakePictureDialog()
            }
            R.id.save -> saveUserInfo()
            else -> {
            }
        }
    }

    override fun onBackPressed() {
        exit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                exit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                cropPhoto(photoUri)
            }
            CHOOSE_FROM_ALBUM -> if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    showCroppedPhoto(data.getStringExtra(AlbumActivity.IMAGE_PATH))
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                showToast(getString(R.string.crop_failed))
            }
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    showCroppedPhoto(result.uri.path ?: "")
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    logWarn(TAG, "Cropping failed: " + result.error.message, result.error)
                    showToast(getString(R.string.crop_failed))
                }
            }
            else -> {
            }
        }
    }

    /**
     * 通过点击返回键或者返回按钮退出时，检查用户信息是否有变更，如果有变更则弹出对话框询问用户是否确定放弃保存。
     */
    private fun exit() {
        if (isUserInfoChanged) {
            val builder = AlertDialog.Builder(this, R.style.AppAlertDialogStyle)
            builder.setMessage(getString(R.string.confirm_to_abort_userinfo_modification))
            builder.setPositiveButton(getString(R.string.abort)) { _, _ -> finishActivity() }
            builder.setNegativeButton(getString(R.string.stay), null)
            builder.create().show()
        } else {
            finishActivity()
        }
    }

    /**
     * 清除用户在修改头像和背景图时生成的缓存，并关闭当前Activity。
     */
    private fun finishActivity() {
        cleanCacheFiles()
        finish()
    }

    /**
     * 保存用户修改的个人信息。
     */
    private fun saveUserInfo() {
        if (!validateNickname()) {
            return
        }
        if (isUserInfoChanged) {
            showLoadingDialog()
            val nickname =
                if (nicknameEdit.text.toString().trim() == srcNickname) "" else nicknameEdit.text.toString().trim()
            val description =
                if (descriptionEdit.text.toString().trim() == srcDescription) "" else descriptionEdit.text.toString().trim()
            val avatarFilePath = if (TextUtils.isEmpty(userAvatarPath)) "" else userAvatarPath
            UpdateUserInfoResponse.getResponse(
                nickname,
                description,
                avatarFilePath,
                object : Callback {
                    override fun onResponse(baseResponse: BaseResponse) {
                        if (isFinishing) {
                            return
                        }
                        if (!ResponseHandler.handleResponse(baseResponse)) {
                            dismissLoadingDialog()
                            val userInfoResponse = baseResponse as UpdateUserInfoResponse
                            val status = userInfoResponse.status
                            val signUser = userInfoResponse.signUser
                            when (status) {
                                0 -> {
                                    logDebug("user = $signUser")


                                    UserModel.saveUser(signUser)

//                                val event = ModifyUserInfoEvent()
//                                if (srcNickname != signUser.nickname) {
//                                    UserModel.saveNickname(signUser.nickname)
////                                    event.modifyNickname = true
//                                }
//                                if (srcDescription != signUser.description) {
//                                    UserUtil.saveDescription(signUser.description)
//                                    event.modifyDescription = true
//                                }
//                                if (srcAvatar != signUser.avatar) {
//                                    UserUtil.saveAvatar(signUser.avatar)
//                                    event.modifyAvatar = true
//                                    if (!TextUtils.isEmpty(userAvatarPath)) {
//                                        GlideUtil.saveImagePathToCache(userAvatarPath, signUser.avatar)
//                                    }
//                                }
//                                EventBus.getDefault().post(event)

                                    showToast(GlobalUtil.getString(R.string.save_success))
                                    finishActivity()
                                }
                                10105 -> showToast(GlobalUtil.getString(R.string.register_failed_nickname_is_used))
                                else -> {
                                    logWarn(TAG, "Modify userinfo failed. ")
                                    showToast(GlobalUtil.getString(R.string.save_failed))
                                }
                            }
                        } else {
                            dismissLoadingDialog()
                        }
                    }

                    override fun onFailure(e: Exception) {
                        logWarn(TAG, e.message, e)
                        dismissLoadingDialog()
                        ResponseHandler.handleFailure(e)
                    }
                })
        } else {
            finishActivity()
        }
    }

    /**
     * 显示选择照片的对话框。
     */
    private fun showTakePictureDialog() {
        val items =
            arrayOf<CharSequence>(getString(R.string.take_photo), getString(R.string.your_album))
        val builder = AlertDialog.Builder(this)
        when (action) {
            TAKE_AVATAR_PICTURE -> builder.setTitle(getString(R.string.select_avatar))
            TAKE_BG_IMAGE_PICTURE -> builder.setTitle(getString(R.string.select_bg_image))
            else -> return
        }
        builder.setItems(items) { _, which ->
            when (which) {
                0 -> takePhoto()
                1 -> chooseFromAlbum()
                else -> {
                }
            }
        }
        builder.show()
    }

    /**
     * 打开摄像头拍照。
     */
    private fun takePhoto() {
        if (Environment.MEDIA_MOUNTED != Environment.getExternalStorageState()) {
            showToast(getString(R.string.operation_failed_without_sdcard))
            return
        }
        // 创建 File 对象，用于存储拍照后的图片
        val outputImage = File(externalCacheDir, TEMP_PHOTO)
        try {
            if (outputImage.exists()) {
                outputImage.delete()
            }
            outputImage.createNewFile()
        } catch (e: IOException) {
            logWarn(TAG, e.message, e)
        }

        photoUri = if (AndroidVersion.hasNougat()) {
            FileProvider.getUriForFile(this, Constants.FILE_PROVIDER_AUTHORITIES, outputImage)
        } else {
            Uri.fromFile(outputImage)
        }
        // 启动相机程序
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        startActivityForResult(intent, TAKE_PHOTO)
    }

    /**
     * 从相册中选择图片。
     */
    private fun chooseFromAlbum() {
        val reqWidth = DeviceInfo.screenWidth
        var reqHeight = reqWidth
        if (action == TAKE_BG_IMAGE_PICTURE) {
            reqHeight = reqWidth * 5 / 7
        }
        AlbumActivity.actionStartForResult(
            this@ModifyUserInfoActivity,
            CHOOSE_FROM_ALBUM,
            reqWidth,
            reqHeight
        )
    }

    /**
     * 对指定图片进行裁剪。
     * @param uri
     * 图片的uri地址。
     */
    private fun cropPhoto(uri: Uri?) {
        val reqWidth = DeviceInfo.screenWidth
        var reqHeight = reqWidth
        if (action == TAKE_BG_IMAGE_PICTURE) {
            reqHeight = reqWidth * 5 / 7
        }
        CropImage.activity(uri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setFixAspectRatio(true)
            .setAspectRatio(reqWidth, reqHeight)
            .setActivityTitle(getString(R.string.crop))
            .setRequestedSize(reqWidth, reqHeight)
            .setCropMenuCropButtonIcon(R.drawable.ic_crop)
            .start(this)
    }

    private fun showCroppedPhoto(imagePath: String) {
        if (action == TAKE_AVATAR_PICTURE) {
            userAvatarPath = imagePath
            logDebug(TAG, "userAvatarPath is $userAvatarPath")
            Glide.with(this)
                .load(userAvatarPath)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.loading_bg_circle)
                .error(R.drawable.avatar_default)
                .into(userAvatar)

//            if (TextUtils.isEmpty(srcBgImage) && TextUtils.isEmpty(userBgImagePath)) {
//                Glide.with(this)
//                    .load(userAvatarPath)
//                    .transform(BlurTransformation(20))
//                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                    .into(userBgImage)
//            }

        } else if (action == TAKE_BG_IMAGE_PICTURE) {
            userBgImagePath = imagePath
            Glide.with(this)
                .load(userBgImagePath)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(userBgImage)
        }
    }

    /**
     * 设置Toolbar和状态栏上的图标为深色。
     */
    private fun setToolbarAndStatusbarIconIntoDark() {
        ViewUtils.setLightStatusBar(window, userBgImage)
        toolbar?.let { ViewUtils.setToolbarIconColor(this, it, true) }
    }

    /**
     * 设置Toolbar和状态栏上的图标颜色为浅色。
     */
    private fun setToolbarAndStatusbarIconIntoLight() {
        ViewUtils.clearLightStatusBar(window, userBgImage)
        toolbar?.let { ViewUtils.setToolbarIconColor(this, it, false) }
    }

    /**
     * 判断用户昵称是否合法。用户昵称长度必须在2-30个字符之间，并且只能包含中英文、数字、下划线和横线。
     *
     * @return 昵称合法返回true，不合法返回false。
     */
    private fun validateNickname(): Boolean {
        val nickname = nicknameEdit.text.toString().trim { it <= ' ' }
        if (nickname.length < 2) {
            showToast(getString(R.string.nickname_length_invalid))
            return false
        } else if (!nickname.matches(Constants.NICK_NAME_REG_EXP.toRegex())) {
            showToast(getString(R.string.nickname_invalid))
            return false
        }
        return true
    }

    /**
     * 退出时清除掉修改头像和背景图时所生成的图片缓存。
     */
    private fun cleanCacheFiles() {
        val outputImage = File(externalCacheDir, TEMP_PHOTO)
        if (outputImage.exists()) {
            outputImage.delete()
        }
        val croppedDir = File(cacheDir.toString() + "/cropped")
        if (croppedDir.exists()) {
            val files = croppedDir.listFiles()
            if (files != null) {
                for (file in files) {
                    file.delete()
                }
            }
        }
    }

    companion object {

        private const val TAG = "ModifyUserInfoActivity"

        const val TAKE_AVATAR_PICTURE = 0

        const val TAKE_BG_IMAGE_PICTURE = 1

        const val TAKE_PHOTO = 1000

        const val CHOOSE_FROM_ALBUM = 1001

        const val EDIT_DESCRIPTION = "edit_description"

        const val TEMP_PHOTO = "taken_photo.jpg"

        fun actionStart(activity: Activity) {
            val intent = Intent(activity, ModifyUserInfoActivity::class.java)
            activity.startActivity(intent)
        }

        fun actionEditDescription(activity: Activity) {
            val intent = Intent(activity, ModifyUserInfoActivity::class.java)
            intent.putExtra(EDIT_DESCRIPTION, true)
            activity.startActivity(intent)
        }
    }

}