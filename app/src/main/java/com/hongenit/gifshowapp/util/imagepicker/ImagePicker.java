package com.hongenit.gifshowapp.util.imagepicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.hongenit.gifshowapp.BaseActivity;
import com.hongenit.gifshowapp.BaseFragment;
import com.hongenit.gifshowapp.Constants;
import com.hongenit.gifshowapp.R;
import com.hongenit.gifshowapp.extension.MyToastKt;
import com.hongenit.gifshowapp.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImagePicker {

    private BaseActivity mActivity;
    private BaseFragment mFragment;
    private Context mContext;
    private IImagePickSource mSource;
    private Runnable mPermissionPendingTask;
    private File mCropSource;

    ImagePicker(BaseActivity context) {
        this.mActivity = context;
        this.mContext = context;
        mSource = (IImagePickSource) context;
        init();
    }


    public ImagePicker(BaseFragment context) {
        this.mFragment = context;
        mContext = context.getContext();
        mSource = (IImagePickSource) context;
        init();
    }

    private void init() {
        mImageSources = new String[]{mContext.getString(R.string.capture),
                mContext.getString(R.string.album)
        };
        initDialog();


    }

    private void initDialog() {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        startCamera(mCrop);
                        break;
                    case 1:
                        startAlbum(mCrop);
                        break;
                    default:
                        break;
                }
                dialog.dismiss();
            }
        };
        ListAdapter adapter = new ArrayAdapterWithIcon(mContext, mImageSources, new Integer[]{R.drawable.ic_cameram_image_source_pick, R.drawable.ic_album_image_source_pick});
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Theme_AppCompat_Dialog);
        String title = mContext.getString(R.string.image_source_pick_title);
        builder.setTitle(Html.fromHtml(title));
        builder.setAdapter(adapter, listener);
        mSourceChooseDialog = builder.create();

    }


    private static final int REQUEST_CODE_CAMERA = 300;

    private static final int REQUEST_CODE_ALBUM = 301;

    private static final int REQUEST_CODE_HANDLE_IMAGE = 302;

    private static final int REQUEST_CODE_CROP_IMAGE = 303;

    private static final String INSTANCE_KEY_IMAGE_URI = "image_uri";
    private static final String INSTANCE_KEY_CROP_IMAGE_URI = "crop_image_uri";
    private static final String INSTANCE_KEY_IMAGE_FILE = "image_uri";
    private static final String INSTANCE_KEY_CROP_IMAGE_FILE = "crop_image_uri";
    private static final String INSTANCE_KEY_CROP_SOURCE = "crop_source";

    private static final String TAG = "BaseImagePick";

    private Dialog mSourceChooseDialog;
    private String mImageSources[];

    private Uri mImageUri;
    private Uri mImageCropUri;
    private File mImageTempFile;
    private File mImageCropTempFile;
    private boolean mCrop = false;

    private int mAspectX, mAspectY, mOutputX, mOutputY;

    public void saveInstanceState(Bundle outState) {
        if (mImageUri != null) {
            outState.putParcelable(INSTANCE_KEY_IMAGE_URI, mImageUri);
            outState.putSerializable(INSTANCE_KEY_IMAGE_FILE, mImageTempFile);
        }
        if (mImageCropUri != null) {
            outState.putParcelable(INSTANCE_KEY_CROP_IMAGE_URI, mImageCropUri);
            outState.putSerializable(INSTANCE_KEY_CROP_IMAGE_FILE, mImageCropTempFile);
        }
        if (mCropSource != null) {
            outState.putSerializable(INSTANCE_KEY_CROP_SOURCE, mCropSource);
        }
    }

    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INSTANCE_KEY_IMAGE_URI)) {
                mImageUri = savedInstanceState.getParcelable(INSTANCE_KEY_IMAGE_URI);
                mImageTempFile = (File) savedInstanceState.getSerializable(INSTANCE_KEY_IMAGE_FILE);
            }
            if (savedInstanceState.containsKey(INSTANCE_KEY_CROP_IMAGE_URI)) {
                mImageCropUri = savedInstanceState.getParcelable(INSTANCE_KEY_CROP_IMAGE_URI);
                mImageCropTempFile = (File) savedInstanceState.getSerializable(INSTANCE_KEY_CROP_IMAGE_FILE);
            }
            if (savedInstanceState.containsKey(INSTANCE_KEY_CROP_SOURCE)) {
                mCropSource = (File) savedInstanceState.getSerializable(INSTANCE_KEY_CROP_SOURCE);
            }
        }
    }

    public void showImageSourceChooseDialog() {
        showImageSourceChooseDialog(false);
    }

    public void showImageSourceChooseDialog(IImagePickSource iImagePickSource) {
        mSource = iImagePickSource;
        showImageSourceChooseDialog(false);
    }

    public void showImageSourceChooseDialog(final boolean crop) {
        mCrop = crop;
        mSourceChooseDialog.show();
    }

    public void startAlbum() {
        startAlbum(false);
    }

    public void startAlbum(final boolean crop) {
        mCrop = crop;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (mActivity != null) {
            mActivity.startActivityForResult(intent, REQUEST_CODE_ALBUM);
        } else if (mFragment != null) {
            mFragment.startActivityForResult(intent, REQUEST_CODE_ALBUM);
        }
    }

    public void startCamera() {
        startCamera(false);
    }


    public void startCamera(final boolean crop) {
//        if (mCapturePermissionRequestFlow.isPermissionGranted()) {

        try {
            mCrop = crop;
            mImageTempFile = FileUtil.INSTANCE.createFile(Constants.TEMP_DIR);
            if (mImageTempFile != null) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // ContentValues values = new ContentValues();
                mImageUri = FileUtil.INSTANCE.createFileShareUri(mContext, mImageTempFile);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//请求URI授权读取
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//请求URI授权写入
                if (mActivity != null) {
                    mActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } else if (mFragment != null) {
                    mFragment.startActivityForResult(intent, REQUEST_CODE_CAMERA);
                }
            } else {
                MyToastKt.showToast(mContext.getString(R.string.cannot_create_new_file), Toast.LENGTH_SHORT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private class ArrayAdapterWithIcon extends ArrayAdapter<String> {

        private List<Integer> images;

        public ArrayAdapterWithIcon(Context context, List<String> items, List<Integer> images) {
            super(context, android.R.layout.select_dialog_item, items);
            this.images = images;
        }

        private ArrayAdapterWithIcon(Context context, String[] items, Integer[] images) {
            super(context, android.R.layout.select_dialog_item, items);
            this.images = Arrays.asList(images);
        }

        public ArrayAdapterWithIcon(Context context, int items, int images) {
            super(context, android.R.layout.select_dialog_item, context.getResources().getStringArray(items));

            final TypedArray imgs = context.getResources().obtainTypedArray(images);
            this.images = new ArrayList<Integer>() {{
                for (int i = 0; i < imgs.length(); i++) {
                    add(imgs.getResourceId(i, -1));
                }
            }};

            // recycle the array
            imgs.recycle();
        }

        @Override
        @NonNull
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(images.get(position), 0, 0, 0);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
            }
            textView.setCompoundDrawablePadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getContext().getResources().getDisplayMetrics()));
            textView.setPaddingRelative((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28, getContext().getResources().getDisplayMetrics()), 0, 0, 0);
            textView.setTextColor(mContext.getResources().getColor(R.color.text_image_source_pick_item));
            return view;
        }
    }

    public void setCropParams(int aspectX, int aspectY, int outputX, int outputY) {
        mAspectX = aspectX;
        mAspectY = aspectY;
        mOutputX = outputX;
        mOutputY = outputY;
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CAMERA:
                    handleCameraResult(data);
                    break;
                case REQUEST_CODE_ALBUM:
                    handleAlbumResult(data);
                    break;
                case REQUEST_CODE_HANDLE_IMAGE:
                    break;
                case REQUEST_CODE_CROP_IMAGE:
                    handleCropResult(data);
                    break;
                default:
                    break;
            }
        } else {
        }
    }

    private void handleCropResult(Intent data) {
        if (mSource == null) return;
        if (mImageCropTempFile.exists() && mImageCropTempFile.length() > 0) {
            mSource.onImageLoaded(Uri.fromFile(mImageCropTempFile));
        } else if (data != null) {
            Uri resultData = data.getData();
            if (resultData != null) {
                mSource.onImageLoaded(resultData);
            } else {
                mSource.onImageLoadFailed();
            }
        } else {
            mSource.onImageLoadFailed();
        }


    }

    private void handleAlbumResult(Intent data) {
        boolean completed = false;
        File outFile = null;
        Uri resultData = data.getData();
        if (resultData != null) {
            mImageUri = resultData;
            try {
                outFile = FileUtil.INSTANCE.createFile(Constants.TEMP_DIR);
                FileUtil.INSTANCE.copyFromUri(mContext, resultData, outFile);
                completed = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (completed && outFile != null && outFile.length() != 0) {
            Uri imageFileUri = FileUtil.INSTANCE.createFileShareUri(mContext, outFile);
            handleLoadedImage(imageFileUri, outFile);
        } else if (mSource != null) {
            mSource.onImageLoadFailed();
        }
    }

    private void handleCameraResult(Intent data) {
//        LogUtils.e(TAG, "camera result");
        if (data != null) {
            Uri resultData = data.getData();
            if (data.getData() != null) {
                mImageUri = resultData;
            }
        }
//        LogUtils.e(TAG, "camera image uri " + mImageUri);
        handleLoadedImage(mImageUri, mImageTempFile);
    }

    private void handleLoadedImage(Uri imageUri, File source) {
        if (mSource == null) return;
        if (mCrop) {
            mCropSource = source;
            if (!startCropPage(imageUri)) {
                mSource.onImageLoaded(Uri.fromFile(source));
            }
        } else {
            Uri uri;
            if (source.length() > 0) {
                uri = Uri.fromFile(source);
            } else {
                uri = imageUri;
            }
            if (uri != null) {
                mSource.onImageLoaded(uri);
            } else {
                mSource.onImageLoadFailed();
            }
        }
    }

    private boolean startCropPage(Uri imageUri) {
        boolean complete = false;
        try {
            mImageCropTempFile = FileUtil.INSTANCE.createFile(Constants.TEMP_DIR);
            mImageCropUri = FileUtil.INSTANCE.createFileShareUri(mContext, mImageCropTempFile);
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setPackage(mContext.getPackageName());
//            Intent intent = new Intent(Intent.ACTION_EDIT);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//请求URI授权读取
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//请求URI授权写入
            //可以选择图片类型，如果是*表明所有类型的图片
            intent.setDataAndType(imageUri, "image/*");
//            intent.setData(imageUri);
            // 下面这个crop = true是设置在开启的Intent中设置显示的VIEW可裁剪
            intent.putExtra("crop", "true");
            // aspectX aspectY 是宽高的比例，这里设置的是正方形（长宽比为1:1）
            intent.putExtra("aspectX", mAspectX);
            intent.putExtra("aspectY", mAspectY);
            // outputX outputY 是裁剪图片宽高
            intent.putExtra("outputX", mOutputX);
            intent.putExtra("outputY", mOutputY);
            //裁剪时是否保留图片的比例，这里的比例是1:1
            intent.putExtra("scale", true);
            //是否是圆形裁剪区域，设置了也不一定有效
            //intent.putExtra("circleCrop", true);
            //设置输出的格式
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            //是否将数据保留在Bitmap中返回
            intent.putExtra("return-data", false);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCropUri);

            if (mActivity != null) {
                mActivity.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
            } else if (mFragment != null) {
                mFragment.startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
            }
            complete = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return complete;
    }


}
