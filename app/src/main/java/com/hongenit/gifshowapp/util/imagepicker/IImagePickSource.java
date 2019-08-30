package com.hongenit.gifshowapp.util.imagepicker;

import android.net.Uri;

public interface IImagePickSource {
    /**
     * uri有可能是contentProvider的uri，所以不一定可以直接从Uri.getPath方法取到图片所在路径。
     * @param imageUri
     */
    void onImageLoaded(Uri imageUri);

    void onImageLoadFailed();

    void startAlbum(boolean crop);

    void startAlbum();

    void startCamera(boolean crop);

    void startCamera();

    void setCropParams(int aspectX, int aspectY, int outputX, int outputY);

    void showImageSourceChooseDialog();

    void showImageSourceChooseDialog(boolean crop);
}
