package com.hongenit.gifshowapp.detail

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Pair
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hongenit.gifshowapp.BaseActivity
import com.hongenit.gifshowapp.GlobalParam
import com.hongenit.gifshowapp.R
import com.hongenit.gifshowapp.bean.WaterFallFeed
import com.hongenit.gifshowapp.collect.CollectGifResponse
import com.hongenit.gifshowapp.comment.Comment
import com.hongenit.gifshowapp.events.DeleteFeedEvent
import com.hongenit.gifshowapp.events.LikeFeedEvent
import com.hongenit.gifshowapp.events.MessageEvent
import com.hongenit.gifshowapp.extension.logWarn
import com.hongenit.gifshowapp.extension.showToast
import com.hongenit.gifshowapp.extension.showToastOnUiThread
import com.hongenit.gifshowapp.network.Callback
import com.hongenit.gifshowapp.network.NetworkConst
import com.hongenit.gifshowapp.network.response.BaseResponse
import com.hongenit.gifshowapp.network.response.ResponseHandler
import com.hongenit.gifshowapp.share.ShareDialogFragment
import com.hongenit.gifshowapp.util.*
import com.hongenit.gifshowapp.util.imageloader.IImageLoader
import com.hongenit.gifshowapp.util.imageloader.MyImageLoader
import com.hongenit.gifshowapp.util.imageloader.glide.ProgressInterceptor
import com.hongenit.gifshowapp.util.imageloader.glide.ProgressListener
import kotlinx.android.synthetic.main.activity_feed_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File


/**
 * Feed详情页面，在这里显示GIF图、评论、点赞等内容。
 *
 */
class FeedDetailActivity : BaseActivity(), View.OnClickListener {


    private var likeAnimView: LottieAnimationView? = null

    private lateinit var mFeed: WaterFallFeed

    private var targetImgWidth: Int = 0

    private var targetImgHeight: Int = 0

    private lateinit var detailInfo: ViewGroup

    private lateinit var likes: ImageView

    private lateinit var likesText: TextView

    private lateinit var feedContentLayout: LinearLayout

//    private var followsButton: Button? = null

    private lateinit var adapter: FeedDetailMoreAdapter


//    private lateinit var gifPlayTarget: GifPlayTarget

    private lateinit var mGifUrl: String


    private var isLiked: Boolean = false

    private var isFollowing: Boolean = false


    private var isFeedDetailLoaded: Boolean = false

    private var gifLoadStatus = GIF_LOADING

    private var fabOffset = 0

    /**
     * 判断当前Feed是否是当前用户本人所发。
     *
     * @return 如果是本人所发返回true，否则返回false。
     */
    private val isCurrentUserPost: Boolean
        get() = UserModel.userId == mFeed.userId.toString()

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val scrollY = detailInfo.top
            feedGif.offset = scrollY
            shareFab.setOffset(fabOffset + scrollY)

            val gifVisibleHeight = targetImgHeight + feedGif.offset
            val imageBackgroundParams = gifBackground.layoutParams
            imageBackgroundParams.height = gifVisibleHeight
            gifBackground.layoutParams = imageBackgroundParams
            val gifImageLayoutParams = gifFrontLayout.layoutParams
            gifImageLayoutParams.height = gifVisibleHeight
            gifFrontLayout.layoutParams = gifImageLayoutParams
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            feedGif.isImmediatePin = newState == RecyclerView.SCROLL_STATE_SETTLING
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                rootLayout.requestFocus()
                hideSoftKeyboard()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_detail)

    }

    override fun onResume() {
        super.onResume()
//        fetchGifUrl() // 当对GIF播放设置做成更改时，需要重新加载GIF图
//        if (loopForever != loopForeverLast || gifPlaySpeed != gifPlaySpeedLast) {
//            loopForeverLast = loopForever
//            gifPlaySpeedLast = gifPlaySpeed
//        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.feedGif ->
                // 点击图片可以暂停和继续GIF图播放
                if (gifLoadStatus == GIF_LOAD_SUCCESS) {
//                    if (gifPlayTarget.isRunning) {
//                        gifPlayTarget.pausePlaying()
//                    } else {
//                        gifPlayTarget.resumePlaying()
//                    }
                } else if (gifLoadStatus == GIF_LOAD_FAILED) {
                    gifFrontLayout.visibility = View.VISIBLE
                    loadGif(mGifUrl)
                }
            R.id.shareFab -> {

//                val gifCacheFile = GlideUtil.getCacheFile(mFeed.gif)
//                logInfo("gifCacheFile  =$gifCacheFile")

//                FetchRealGif.start(this)

                if (gifLoadStatus != GIF_LOAD_SUCCESS) {
                    showToast(getString(R.string.unable_to_share_before_gif_loaded))
                } else if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                    showToast(getString(R.string.unable_to_share_without_sdcard))
                } else {
                    val gifCacheFile = GlideUtil.getCacheFile(mFeed.gif)
                    if (gifCacheFile != null) {
                        ShareDialogFragment().showDialog(this, gifCacheFile.path)
                    } else {
                        showToast(getString(R.string.unable_to_share_before_gif_loaded))
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onBackPressed() {
        exit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        if (messageEvent is DeleteFeedEvent) {
            val feedId = messageEvent.feedId
            if (feedId == mFeed.feedId) {
                finish()
            }
//        } else if (messageEvent is GoodCommentEvent) {
//            adapter.refreshCommentsGood(messageEvent.commentId, messageEvent.type, messageEvent.goodsCount)
//        } else if (messageEvent is DeleteCommentEvent) {
//            adapter.deleteHotComment(messageEvent.commentId)
        } else {
            super.onMessageEvent(messageEvent)
        }
    }

    override fun setupViews() {
//        if (AndroidVersion.hasLollipop()) {
//            postponeEnterTransition()
//        }
        if (loadFeed()) {
            setupToolbar()
            title = ""

            calculateImageSize()
            feedGif.layoutParams.width = targetImgWidth
            feedGif.layoutParams.height = targetImgHeight
            gifBackground.layoutParams.height = targetImgHeight
            gifFrontLayout.layoutParams.height = targetImgHeight

            // setup RecyclerView
            val layoutManager = LinearLayoutManager(this)
            detailRecyclerView.layoutManager = layoutManager
            detailRecyclerView.setHasFixedSize(true)
            setupDetailView()
            adapter = FeedDetailMoreAdapter(this, detailInfo, mFeed)
            detailRecyclerView.adapter = adapter
            detailRecyclerView.addOnScrollListener(scrollListener)
            feedGif.setOnClickListener(this)
            shareFab.setOnClickListener(this)


            MyImageLoader.displayImage(
                feedGif,
                mFeed.cover,
                null,
                object : IImageLoader.LoadImageListener {
                    override fun onLoadComplete(bitmap: Bitmap?) {
                        loadCoverOver()
                    }

                    override fun onLoadFailed() {
                        loadCoverOver()
                    }

                    private fun loadCoverOver() {
                        GlobalParam.handler.post {
                            loadGif(mFeed.gif)
                        }
                    }
                },
                this
            )


            fetchFeedDetails()

//            if (AndroidVersion.hasLollipop()) {
//                window.enterTransition.addListener(object : SimpleTransitionListener() {
//                    override fun onTransitionEnd(transition: Transition) {
//                        shareFab.setOffset(fabOffset)
//                        popFab()
//                        fetchFeedDetails()
//                    }
//                })
//            } else {
//                fetchFeedDetails()
//            }
            shareFab.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    shareFab.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    calculateFabPosition()
                }
            })
//            MobclickAgent.onEvent(this, "10003")
        } else {
            finish()
        }
    }

    /**
     * 读取传入的Feed实例。
     * @return 读取成功返回true，失败返回false。
     */
    private fun loadFeed(): Boolean {
        val feed = intent.getParcelableExtra<WaterFallFeed>(FEED)
        if (feed != null) {
            mFeed = feed
            return true
        }
        showToast(GlobalUtil.getString(R.string.load_feed_failed))
        return false
    }

    private fun calculateImageSize() {
        val imgWidth = mFeed.imgWidth
        val imgHeight = mFeed.imgHeight
        val screenWidth = DeviceInfo.screenWidth
        val screenHeight = DeviceInfo.screenHeight
        targetImgWidth = screenWidth
        targetImgHeight = screenWidth * imgHeight / imgWidth
        val maxImgHeight = screenHeight * 0.65
        if (targetImgHeight > maxImgHeight) {
            targetImgHeight = maxImgHeight.toInt()
            targetImgWidth = imgWidth * targetImgHeight / imgHeight
        }
    }

    private fun calculateFabPosition() {
        fabOffset = feedGif.height + feedContentLayout.height - (shareFab.height / 2)
        shareFab.setOffset(fabOffset)
        shareFab.setMinOffset(feedGif.minimumHeight - (shareFab.height / 2))
    }

    /**
     * 使用pop动画的方式将fab按钮显示出来。
     */
    private fun popFab() {
        shareFab.show()
        shareFab.alpha = 0f
        shareFab.scaleX = 0f
        shareFab.scaleY = 0f
        val animator = ObjectAnimator.ofPropertyValuesHolder(
            shareFab,
            PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
        )
        animator.startDelay = 300
        animator.start()
    }

    private fun setupDetailView() {
        detailInfo = layoutInflater.inflate(
            R.layout.feed_detail_info,
            detailRecyclerView,
            false
        ) as ViewGroup

        val spacingView = detailInfo.findViewById<View>(R.id.spacingView)
        val favoriteLayout = detailInfo.findViewById<LinearLayout>(R.id.favoriteLayout)
        likes = favoriteLayout.findViewById(R.id.likes)
        likeAnimView = favoriteLayout.findViewById(R.id.lottieAnimationView)
        likesText = favoriteLayout.findViewById(R.id.likesText)
        feedContentLayout = detailInfo.findViewById(R.id.feedContentLayout)
        val commentLayout = detailInfo.findViewById<LinearLayout>(R.id.commentLayout)
        val feedContent = detailInfo.findViewById<TextView>(R.id.feedContent)

        val viewStub: ViewStub? = if (isCurrentUserPost) {
            detailInfo.findViewById(R.id.feedDetailMe)
        } else {
            detailInfo.findViewById(R.id.feedDetailUser)
        }

        if (viewStub != null) {
            val userDetailRootLayout = viewStub.inflate()
            val userLayout = userDetailRootLayout.findViewById<LinearLayout>(R.id.userLayout)
            val avatar = userDetailRootLayout.findViewById<ImageView>(R.id.avatar)
            val nickname = userDetailRootLayout.findViewById<TextView>(R.id.nickname)
            val postDate = userDetailRootLayout.findViewById<TextView>(R.id.postDate)

            Glide.with(this)
                .load(mFeed.avatar)
                .placeholder(R.drawable.loading_bg_circle)
                .error(R.drawable.avatar_default)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(avatar)


            nickname.text = mFeed.nickname
            postDate.text = DateUtil.getConvertedDate(mFeed.postDate)

            val onUserClick = View.OnClickListener {
                //                UserHomePageActivity.actionStart(
//                    this@FeedDetailActivity,
//                    avatar,
//                    mFeed.userId,
//                    mFeed.nickname,
//                    mFeed.avatar,
//                    mFeed.bgImage
//                )
            }
            userLayout.setOnClickListener(onUserClick)
            postDate.setOnClickListener(onUserClick)
            avatar.setOnClickListener(onUserClick)
        }

        spacingView.layoutParams.height = targetImgHeight
        likes.isClickable = false
        likesText.text =
            String.format(
                getString(R.string.number_likes),
                GlobalUtil.getConvertedNumber(mFeed.likesCount)
            )
        likesText.setTextColor(resources.getColor(R.color.primary_text))
        feedContent.text = mFeed.content

        commentLayout.setOnClickListener {
            detailRecyclerView.smoothScrollToPosition(adapter.itemCount - 1)
            adapter.showSoftKeyboard()
        }

        likeAnimView?.setImageAssetsFolder("assets")
        likeAnimView?.setAnimation("like1.json")
        likeAnimView?.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                likeAnimView?.visibility = View.GONE

            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
                likeAnimView?.visibility = View.VISIBLE
            }
        })

        favoriteLayout.setOnClickListener {
            if (isFeedDetailLoaded) {
                likeFeed()

            }
        }
    }


    /**
     * 对Feed进行点赞或取消点赞。
     */
    private fun likeFeed() {
        var likesCount = mFeed.likesCount
        val event = LikeFeedEvent()
        event.from = LikeFeedEvent.FROM_FEED_DETAIL
        event.feed = mFeed
        if (mFeed.isLikedAlready) {
            mFeed.isLikedAlready = false
            likes.setImageResource(R.drawable.ic_like)
            likesCount--
            if (likesCount < 0) {
                likesCount = 0
            }
            event.type = LikeFeedEvent.UNLIKE_FEED
        } else {
            mFeed.isLikedAlready = true
            likes.setImageResource(R.drawable.ic_liked)
            likesCount++
            event.type = LikeFeedEvent.LIKE_FEED
            likeAnimView?.playAnimation()

        }
        event.likesCount = likesCount
        EventBus.getDefault().post(event)
        mFeed.likesCount = likesCount
        likesText.text = String.format(
            getString(R.string.number_likes),
            GlobalUtil.getConvertedNumber(likesCount)
        )

        CollectGifResponse.getResponse(
            mFeed.feedId,
            UserModel.userId,
            mFeed.isLikedAlready,
            object : Callback {
                override fun onResponse(baseResponse: BaseResponse) {
                    val response = baseResponse as CollectGifResponse
                    if (response.status == NetworkConst.STATUS_OK) {
                        if (response.collect) {
                            showToast(getString(R.string.collect_succeed))
                        } else {
                            showToast(getString(R.string.uncollect_succeed))
                        }
                    }
                }

                override fun onFailure(e: Exception) {
                    showToast(getString(R.string.collect_failed))
                }

            })
    }

    // 刷新是否收藏的状态
    private fun showFeedDetailInfo() {
        mFeed.isLikedAlready = isLiked
        if (mFeed.isLikedAlready) {
            likes.setImageResource(R.drawable.ic_liked)
        } else {
            likes.setImageResource(R.drawable.ic_like)
        }
    }

    /**
     * 获取当前Feed的详情。
     */
    fun fetchFeedDetails() {
        FetchGifDetailResponse.getResponse(mFeed.feedId, object : Callback {
            override fun onResponse(baseResponse: BaseResponse) {
                if (isFinishing) {
                    return
                }
                isFeedDetailLoaded = true
                if (!ResponseHandler.handleResponse(baseResponse)) {
                    val status = baseResponse.status
                    if (status == 0) {
                        val fetchFeedDetails = baseResponse as FetchGifDetailResponse
                        val comments = fetchFeedDetails.comments
                        adapter.setHotComments(comments as MutableList<Comment>?)
//                        likesCount = fetchFeedDetails.likesCount
                        isLiked = fetchFeedDetails.isLiked
                        isFollowing = fetchFeedDetails.isFollowing
                        showFeedDetailInfo()
                    } else if (status == 10305) {
                        showToast(GlobalUtil.getString(R.string.feed_is_deleted))
                        finishSelf()
                    } else {
                        adapter.setHotComments(null)
                        showToast(GlobalUtil.getString(R.string.fetch_feed_details_failed) + ": " + status)
                        logWarn(
                            TAG,
                            "fetch feed details failed. " + GlobalUtil.getResponseClue(
                                status,
                                baseResponse.msg
                            )
                        )
                    }
                } else {
                    adapter.setHotComments(null)
                    showToast(GlobalUtil.getString(R.string.fetch_feed_details_failed) + ": " + baseResponse.status)
                    logWarn(TAG, "fetch feed details failed. code is " + baseResponse.status)
                }
            }

            override fun onFailure(e: Exception) {
                logWarn(TAG, e.message, e)
                isFeedDetailLoaded = true
                adapter.setHotComments(null)
                ResponseHandler.handleFailure(e)
            }
        })
    }


//    /**
//     * 使用pop动画的方式将fab按钮显示出来。
//     */
//    private fun popProgressView() {
//        gifProgressView.visibility = View.VISIBLE
//        gifProgressView.alpha = 0f
//        gifProgressView.scaleX = 0f
//        gifProgressView.scaleY = 0f
//        val animator = ObjectAnimator.ofPropertyValuesHolder(
//            gifProgressView,
//            PropertyValuesHolder.ofFloat(View.ALPHA, 1f),
//            PropertyValuesHolder.ofFloat(View.SCALE_X, 1f),
//            PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f)
//        )
//        animator.startDelay = 200
//        animator.start()
//    }

    private fun loadGif(gifUrl: String) {


        mGifUrl = gifUrl
        println("gifUrl = $gifUrl")

        gifFrontLayout.visibility = View.VISIBLE
        loadingLottieView.playAnimation()

        ProgressInterceptor.addListener(gifUrl, object : ProgressListener {
            override fun onProgress(progress: Int) {
                println("progres  =$progress")
//                gifProgressView.setProgress(progress)
            }
        })

        MyImageLoader.displayImage(
            feedGif,
            gifUrl,
            feedGif.drawable,
            object : IImageLoader.LoadImageListener {
                override fun onLoadComplete(bitmap: Bitmap?) {
                    loadGifOver()
                }

                override fun onLoadFailed() {
                    loadGifOver()
                }

                private fun loadGifOver() {
                    gifFrontLayout.visibility = View.INVISIBLE
                    loadingLottieView.cancelAnimation()
                    gifLoadStatus = GIF_LOAD_SUCCESS

                }
            },
            this
        )

//            .listener(object : RequestListener<CustomUrl, GlideDrawable> {
//                override fun onException(
//                    e: Exception?, model: CustomUrl, target: Target<GlideDrawable>,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    logWarn(TAG, "Glide load gif failed: $gifUrl")
//                    if (e != null) {
//                        logWarn(TAG, e.message, e)
//                    } else {
//                        logWarn(TAG, "Load gif failed with exception null. url is $gifUrl")
//                    }
//                    gifLoadStatus = GIF_LOAD_FAILED
//                    gifFrontLayout.visibility = View.INVISIBLE
//                    showToast(GlobalUtil.getString(R.string.load_gif_failed))
//                    return true
//                }
//
//                override fun onResourceReady(
//                    resource: GlideDrawable, model: CustomUrl, target: Target<GlideDrawable>,
//                    isFromMemoryCache: Boolean, isFirstResource: Boolean
//                ): Boolean {
//                    gifLoadStatus = GIF_LOAD_SUCCESS
//                    gifFrontLayout.visibility = View.INVISIBLE
//                    return false
//                }
//            })
    }

    private fun finishSelf() {
        if (AndroidVersion.hasLollipop()) {
            finishAfterTransition()
        } else {
            finish()
        }
    }

    private fun saveGifToSDCard() {
        Thread(Runnable {
            try {
                if (gifLoadStatus != GIF_LOAD_SUCCESS) {
                    showToastOnUiThread(getString(R.string.unable_to_save_before_gif_loaded))
                    return@Runnable
                } else if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                    showToastOnUiThread(getString(R.string.unable_to_share_without_sdcard))
                    return@Runnable
                }
                val dir =
                    File(Environment.getExternalStorageDirectory().toString() + "/quxiang/趣享GIF")
                if (!dir.exists() && !dir.mkdirs()) {
                    showToastOnUiThread(GlobalUtil.getString(R.string.save_failed))
                    return@Runnable
                }
                val targetFile = File(dir, GlobalUtil.currentDateString + ".gif")
                if (targetFile.exists() && !targetFile.delete()) {
                    showToastOnUiThread(GlobalUtil.getString(R.string.save_failed))
                    return@Runnable
                }
                if (!targetFile.createNewFile()) {
                    showToastOnUiThread(GlobalUtil.getString(R.string.save_failed))
                    return@Runnable
                }
//                val gifCacheFile = GlideUtil.getCacheFile(mFeed.gif)
//                if (gifCacheFile == null || !gifCacheFile.exists()) {
//                    showToastOnUiThread(GlobalUtil.getString(R.string.gif_file_not_exist))
//                    return@Runnable
//                }
//                if (FileUtil.copyFile(gifCacheFile, targetFile)) {
//                    ImageUtil.insertImageToSystem(this, targetFile.path)
//                    showToastOnUiThread(
//                        String.format(GlobalUtil.getString(R.string.save_gif_success), targetFile.name),
//                        Toast.LENGTH_LONG
//                    )
//                    runOnUiThread {
//                        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                        val uri = Uri.fromFile(targetFile)
//                        intent.data = uri
//                        sendBroadcast(intent)
//                    }
//                } else {
//                    showToastOnUiThread(GlobalUtil.getString(R.string.save_failed))
//                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }).start()
    }

    /**
     * 通过点击返回键或者返回按钮退出时，检查GIF播放设置是否有变更，如果有变更则弹出对话框询问用户是否保存更改。
     */
    private fun exit() {
        finishSelf()

//        if (loopForever != loopForeverInit || gifPlaySpeed != gifPlaySpeedInit) {
//            val builder = AlertDialog.Builder(this, R.style.GifFunAlertDialogStyle)
//            builder.setMessage(GlobalUtil.getString(R.string.save_gif_play_control_modify_or_not))
//            builder.setPositiveButton(GlobalUtil.getString(R.string.save)) { _, _ ->
//                // 用户选择保存则不用做任何事情，直接finish即可
//                finishSelf()
//            }
//            builder.setNegativeButton(GlobalUtil.getString(R.string.ignore)) { _, _ ->
//                // 用户选择忽略，则需要将设置改回初始值
//                PreferenceManager.getDefaultSharedPreferences(this@FeedDetailActivity)
//                    .edit()
//                    .putBoolean(getString(R.string.key_loop_gif_play), loopForeverInit)
//                    .putString(getString(R.string.key_gif_play_speed), gifPlaySpeedInit)
//                    .apply()
//                finishSelf()
//            }
//            builder.create().show()
//        } else {
//            finishSelf()
//        }
    }

    companion object {

        private const val TAG = "FeedDetailActivity"

        const val FEED = "feed"

        private const val GIF_LOADING = 0

        private const val GIF_LOAD_SUCCESS = 1

        private const val GIF_LOAD_FAILED = 3

        fun actionStart(activity: Activity, image: View, feed: WaterFallFeed) {
            val intent = Intent(activity, FeedDetailActivity::class.java)
            intent.putExtra(FEED, feed)

            if (AndroidVersion.hasLollipop()) {
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    Pair.create(image, GlobalUtil.getString(R.string.transition_feed_detail)),
                    Pair.create(image, GlobalUtil.getString(R.string.transition_feed_detail_bg)),
                    Pair.create(
                        image,
                        GlobalUtil.getString(R.string.transition_feed_detail_image_bg)
                    )
                )

                activity.startActivity(intent, options.toBundle())
            } else {
                activity.startActivity(intent)
            }
        }
    }


}
