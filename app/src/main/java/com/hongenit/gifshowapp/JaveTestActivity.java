package com.hongenit.gifshowapp;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Xiaohong on 2019-07-09.
 * desc:
 */
public class JaveTestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLoadingDialog();
    }
}
