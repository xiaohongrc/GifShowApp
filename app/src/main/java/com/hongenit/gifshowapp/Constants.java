package com.hongenit.gifshowapp;

import android.os.Environment;

import java.io.File;


public class Constants {

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * ONE_SECOND;
    public static final long ONE_HOUR = ONE_MINUTE * 60;
    public static final long ONE_DAY = ONE_HOUR * 24;
    public static final long ONE_YEAR = ONE_DAY * 365;
    public static final File APP_DIR = new File(Environment.getExternalStorageDirectory(), ".RCLiveChat");
    public static final File TEMP_DIR = new File(APP_DIR, "temp");
    public static final String FILE_PROVIDER_AUTHORITIES = "com.hongenit.gifshowapp.fileprovider";
    /**
     * 检查用户昵称是否合法的正式表达式。
     */
    public static final String NICK_NAME_REG_EXP = "^[\u4E00-\u9FA5A-Za-z0-9_\\-]+$";

}
