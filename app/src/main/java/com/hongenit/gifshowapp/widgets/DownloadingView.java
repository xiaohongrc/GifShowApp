package com.hongenit.gifshowapp.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.hongenit.gifshowapp.R;
import com.hongenit.gifshowapp.extension.LogKt;

/**
 * Created by Xiaohong on 2017/7/14 0015.
 * Function: 网络加载的动效
 */
public class DownloadingView extends FrameLayout {

    private static final int STATUS_NOT_DOWNLOAD = 1001;
    private static final int STATUS_DOWNLOADING = 1002;
    private static final int STATUS_DOWNLOADED = 1003;
    private ProgressBar mIvIcon;
    private Animation mRotateAnimation;
    private String TAG = "DownloadingView";

    public DownloadingView(Context context) {
        super(context);
        init(context);
    }

    public DownloadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DownloadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.view_downloading, this);
//        mRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.sticker_loading);
//        mRotateAnimation.setRepeatCount(Animation.INFINITE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIvIcon = findViewById(R.id.iv_icon);
    }


//    public void show(boolean show) {
//        if (show) {
//            setVisibility(View.VISIBLE);
//            startRotate();
//        } else {
//            stopRotate();
//            setVisibility(View.GONE);
//        }
//    }

    /**
     * 根据下载状态设置显示状态
     *
     * @param downloadStatus
     */
    public void setLoadingStatus(int downloadStatus) {
        LogKt.logDebug(TAG, "downloadStatus = " + downloadStatus);
        switch (downloadStatus) {
            case STATUS_NOT_DOWNLOAD:
                setVisibility(View.GONE);
                hideLoading();
                break;
            case STATUS_DOWNLOADING:
                setVisibility(View.VISIBLE);
                showLoading();
                break;
            case STATUS_DOWNLOADED:
                hideLoading();
                setVisibility(View.GONE);
                break;

            default:
                break;
        }

    }

    private void showLoading() {
        mIvIcon.setVisibility(View.VISIBLE);
//        mIvIcon.setAnimation(mRotateAnimation);
//        mIvIcon.startAnimation(mRotateAnimation);
//        mRotateAnimation.start();
    }

    private void hideLoading() {
        mIvIcon.setVisibility(View.GONE);
//        mRotateAnimation.cancel();
//        mIvIcon.setAnimation(null);
    }
}
