//package com.hongenit.gifshowapp.util.permission;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import com.hongenit.gifshowapp.GlobalParam;
//import com.rcplatform.livechat.Constants;
//import com.rcplatform.livechat.LiveChatApplication;
//import com.rcplatform.livechat.bean.PermissionInfo;
//import com.rcplatform.livechat.utils.PermissionUtils;
//import com.rcplatform.livechat.utils.Utils;
//import com.rcplatform.livechat.widgets.PermissionEducateDialog;
//import com.rcplatform.videochat.log.LogUtils;
//
//public class PermissionRequestFlow {
//
//
//    private static final String TAG = "PermissionRequestFlow";
//    private boolean mShowExplainAfterNeverAsk = true;
//
//    public interface ResultListener {
//        void onPermissionGranted();
//
//        void onPermissionDenied();
//    }
//
//
//    interface Host {
//        void startPermissionExplainPage(PermissionInfo info, int requestCode);
//
//        void requestPermission(String permissions[], int requestCode);
//
//        boolean shouldExplainPermissions(String permissions[]);
//
//        boolean isPermissionGranted(String permissions[]);
//
//        Context getContext();
//
//        void startPermissionSettingPage();
//    }
//
//    private class ActivityHost implements Host {
//        private Activity mActivity;
//
//        ActivityHost(Activity activity) {
//            this.mActivity = activity;
//        }
//
//        @Override
//        public void startPermissionExplainPage(PermissionInfo info, int requestCode) {
//            PermissionRequestDescActivity.startPage(mActivity, info, requestCode);
//        }
//
//        @Override
//        public void requestPermission(String[] permissions, int requestCode) {
//            PermissionUtils.requestPermission(mActivity, permissions, requestCode);
//        }
//
//        @Override
//        public boolean shouldExplainPermissions(String[] permissions) {
//            return PermissionUtils.shouldShowPermisionExplanation(mActivity, permissions);
//        }
//
//        @Override
//        public boolean isPermissionGranted(String[] permissions) {
//            return PermissionUtils.isPermissionGranted(mActivity, permissions);
//        }
//
//        @Override
//        public Context getContext() {
//            return mActivity;
//        }
//
//        @Override
//        public void startPermissionSettingPage() {
//            Utils.startApplicationDetailPage(mActivity, mPermissionSettingRequestCode);
//
//        }
//
//    }
//
//    private class FragmentHost implements Host {
//        private Fragment mFragment;
//
//        FragmentHost(Fragment fragment) {
//            this.mFragment = fragment;
//        }
//
//        @Override
//        public void startPermissionExplainPage(PermissionInfo info, int requestCode) {
//            PermissionRequestDescActivity.startPage(mFragment, info, requestCode);
//        }
//
//        @Override
//        public void requestPermission(String[] permissions, int requestCode) {
//            PermissionUtils.requestPermission(mFragment, permissions, requestCode);
//        }
//
//        @Override
//        public boolean shouldExplainPermissions(String[] permissions) {
//            return PermissionUtils.shouldShowPermisionExplanation(mFragment.getActivity(), permissions);
//        }
//
//        @Override
//        public boolean isPermissionGranted(String[] permissions) {
//            return PermissionUtils.isPermissionGranted(mFragment.getContext(), permissions);
//        }
//
//        @Override
//        public Context getContext() {
//            return mFragment.getActivity();
//        }
//
//        @Override
//        public void startPermissionSettingPage() {
//            Utils.startApplicationDetailPage(mFragment, mPermissionSettingRequestCode);
//        }
//    }
//
//    private Host mHost;
//    private PermissionInfo mPermissionInfo;
//    private ResultListener mResultListener;
//    private int mPermissionSettingRequestCode;
//
//    public PermissionRequestFlow(Activity activity, PermissionInfo info) {
//        mHost = new ActivityHost(activity);
//        mPermissionInfo = info;
//    }
//
//    public PermissionRequestFlow(Activity activity, PermissionInfo info, boolean showExplain) {
//        mHost = new ActivityHost(activity);
//        mPermissionInfo = info;
//        mShowExplainAfterNeverAsk = showExplain;
//    }
//
//    public PermissionRequestFlow(Fragment fragment, PermissionInfo info) {
//        mHost = new FragmentHost(fragment);
//        mPermissionInfo = info;
//    }
//
//
//    public void requestPermission(int requestCode) {
//        mPermissionSettingRequestCode = requestCode;
////        if (mHost.shouldExplainPermissions(mPermissionInfo.permissmions)) {
////            mHost.startPermissionExplainPage(mPermissionInfo, requestCode);
////        } else {
//        mHost.requestPermission(mPermissionInfo.permissmions, mPermissionInfo.permissionRequestCode);
////        }
//    }
//
//    public void startPermissionSettingPage(int requestCode){
//        mPermissionSettingRequestCode = requestCode;
//        mHost.startPermissionSettingPage();
//    }
//
//    public void handlePermissionRequestResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == mPermissionInfo.permissionRequestCode) {
//            if (mHost.isPermissionGranted(mPermissionInfo.permissmions)) {
//                invokePermissionGranted();
//            } else {
//                if (!mHost.shouldExplainPermissions(permissions) && mShowExplainAfterNeverAsk) {
//                    LogUtils.d(TAG, "should not explain permissions");
//
//                    PermissionEducateDialog dialog = new PermissionEducateDialog(mHost.getContext(), mPermissionInfo);
//
//                    dialog.setActionListener(new PermissionEducateDialog.ActionListener() {
//                        @Override
//                        public void onConfirm(PermissionEducateDialog dialog) {
//                            dialog.dismiss();
//                            mHost.startPermissionSettingPage();
//                        }
//
//                        @Override
//                        public void onCancel(PermissionEducateDialog dialog) {
//                            dialog.dismiss();
//                            if (mResultListener != null) {
//                                mResultListener.onPermissionDenied();
//                            }
//                        }
//                    });
//                    dialog.show();
//
//                } else {
//                    if (mResultListener != null) {
//                        mResultListener.onPermissionDenied();
//                    }
//                }
//            }
//        }
//    }
//
//    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
//        if (mPermissionSettingRequestCode == requestCode) {
//            if (PermissionUtils.isPermissionGranted(mHost.getContext(), mPermissionInfo.permissmions)) {
//                invokePermissionGranted();
//            } else {
//                invokePermissionDenied();
//            }
//        }
//    }
//
//    private void invokePermissionDenied() {
//        LogUtils.d(TAG, "permission denied");
//        if (mResultListener != null) {
//            LogUtils.d(TAG, "permission denied called");
//            mResultListener.onPermissionDenied();
//        }
//    }
//
//    private void invokePermissionGranted() {
//        checkStoragePermissionGranted();
////        LogUtils.d(TAG, "permission granted");
//        if (mResultListener != null) {
////            Log.d(TAG, "invokePermissionGranted: ");(TAG, "permission granted called");
//            mResultListener.onPermissionGranted();
//        }
//    }
//
//    private void checkStoragePermissionGranted() {
//        boolean hasStoragePermission = false;
//        if (mPermissionInfo != null) {
//            for (String permission : mPermissionInfo.permissmions) {
//                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission) || Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
//                    hasStoragePermission = true;
//                    break;
//                }
//            }
//        }
//        if (hasStoragePermission) {
//            Intent intent = new Intent(Constants.Actions.ACTION_STORAGE_PERMISSION_GRANTED);
//            GlobalParam.INSTANCE.getContext().sendBroadcast(intent);
//        }
//    }
//
//    public void setResultListener(ResultListener listener) {
//        mResultListener = listener;
//    }
//
//    public boolean isPermissionGranted() {
//        return mHost.isPermissionGranted(mPermissionInfo.permissmions);
//    }
//}
