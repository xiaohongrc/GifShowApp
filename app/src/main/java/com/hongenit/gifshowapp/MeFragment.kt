package com.hongenit.gifshowapp

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hongenit.gifshowapp.account.LoginActivity
import com.hongenit.gifshowapp.callback.PermissionListener
import com.hongenit.gifshowapp.events.MessageEvent
import com.hongenit.gifshowapp.events.UserInfoUpdateEvent
import com.hongenit.gifshowapp.extension.logDebug
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.meinfo.ModifyUserInfoActivity
import com.hongenit.gifshowapp.util.UserModel
import com.hongenit.gifshowapp.util.imageloader.MyImageLoader
import kotlinx.android.synthetic.main.fragment_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Created by Xiaohong on 2019-07-18.
 * desc:
 */
class MeFragment : BaseFragment() {

    private var mImageRequest: Int = 0
    private val REQUEST_ICON = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_me, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        ivAvatar.setOnClickListener {
            activity?.let {
                ModifyUserInfoActivity.actionStart(it)
            }
        }
        ivEditInfo.setOnClickListener {
            activity?.let {
                ModifyUserInfoActivity.actionStart(it)
            }
        }

        tv_logout.setOnClickListener {
            println("logout")
            showLogoutDialog()
        }

        fillUserInfo()

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        logDebug("onMessageEvent")
        if (messageEvent is UserInfoUpdateEvent) {
            fillUserInfo()
        }
    }


    private fun fillUserInfo() {
        val signInUser = UserModel.getSignInUser()
        // 加载头像
        MyImageLoader.displayRoundIcon(ivAvatar, signInUser.avatar, resources.getDrawable(R.drawable.icon_head_default))
        // 昵称
        tvNickname.text = signInUser.nickname

        // 个人简介
        var description = signInUser.description
        if (description.isNullOrEmpty()) {
            description = getString(R.string.description_is_empty)
        }
        tvSelfIntro.text = description

        //        fillUserGenderAge(signInUser)

    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(activity, R.style.AppAlertDialogStyle)
        builder.setTitle(R.string.attention).setMessage(R.string.logout_message)
            .setPositiveButton(R.string.logout, object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    doLogout()
                }
            }).setNegativeButton(R.string.cancel, null).show()
    }

    fun doLogout() {
        UserModel.logOut()
        activity?.let {
            LoginActivity.actionStart(it)
            it.finish()
        }

    }


    // 编辑换头像
    private fun changeIcon() {
        mImageRequest = REQUEST_ICON

        handlePermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), object : PermissionListener {
            override fun onGranted() {
//                showImageSourceChooseDialog(true)
            }

            override fun onDenied(deniedPermissions: List<String>) {
                showToast(getString(R.string.no_permission_cant_continue))
            }
        })
    }


    // 展示性别年龄
    private fun fillUserGenderAge(signinUser: User) {
        var bgGenderResId = 0
        var iconGenderResId = 0

        if (signinUser.gender == User.GENDER_MALE) {
            bgGenderResId = R.drawable.bg_gender_male
            iconGenderResId = R.drawable.profile_male
        } else {
            bgGenderResId = R.drawable.bg_gender_female
            iconGenderResId = R.drawable.profile_female
        }
        ivGender.background = resources.getDrawable(bgGenderResId)
        ivGender.setImageResource(iconGenderResId)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


//    override fun onImageLoaded(imageUri: Uri?) {
//        if (imageUri != null) {
//            context?.let {
//                val path = FileUtil.getContentUriRealDiskPath(it, imageUri)
//                if (TextUtils.isEmpty(path)) {
//                    onImageLoadFailed()
//                } else {
//                    if (mImageRequest == REQUEST_ICON) {
////                        updateIcon(path)
//                        println("---path = $path")
//                    }
//                }
//            }
//        } else {
//            onImageLoadFailed()
//        }
//
//    }
//
//    override fun onImageLoadFailed() {
//        showToast(getString(R.string.image_load_failed), Toast.LENGTH_SHORT)
//    }
//

}