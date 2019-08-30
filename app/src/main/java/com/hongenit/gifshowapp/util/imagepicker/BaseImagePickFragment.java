package com.hongenit.gifshowapp.util.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.hongenit.gifshowapp.BaseFragment;

/**
 * Created by Xiaohong on 2019-08-8.
 * desc:需要獲取圖片的頁面的基類,可以繼承它來實現調用系統相機或調用系統相冊獲取圖片uri的功能.
 */
public abstract class BaseImagePickFragment extends BaseFragment implements IImagePickSource {


//    private static final int REQUES

    private ImagePicker mPicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPicker = new ImagePicker(this);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            mPicker.restoreInstanceState(savedInstanceState);
        }
    }

    @Override
    public void setCropParams(int aspectX, int aspectY, int outputX, int outputY) {
        mPicker.setCropParams(aspectX, aspectY, outputX, outputY);
    }

    @Override
    public void startAlbum() {
        mPicker.startAlbum();
    }

    @Override
    public void startAlbum(boolean crop) {
        mPicker.startAlbum(crop);
    }

    @Override
    public void startCamera() {
        mPicker.startCamera();
    }

    @Override
    public void startCamera(boolean crop) {
        mPicker.startCamera(crop);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPicker.handleResult(requestCode, resultCode, data);
    }

    @Override
    public void showImageSourceChooseDialog() {
        mPicker.showImageSourceChooseDialog();
    }

    @Override
    public void showImageSourceChooseDialog(boolean crop) {
        mPicker.showImageSourceChooseDialog(crop);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPicker.saveInstanceState(outState);
    }

}
