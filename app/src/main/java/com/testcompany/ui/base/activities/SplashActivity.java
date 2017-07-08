package com.testcompany.ui.base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.testcompany.BuildConfig;
import com.testcompany.R;
import com.testcompany.utils.view.CustomTextView;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    private Handler mHandler;
    @BindView(R.id.tvAppVersion)
    CustomTextView tvAppVersion;
    @Override
    protected Integer getLayoutId() {
        return R.layout.activity_splash;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }
    private void init() {
        showAppVersion();
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 2000);

    }

    private void showAppVersion() {
        if (BuildConfig.DEBUG) {
            tvAppVersion.setText(getString(R.string.app_version).concat(BuildConfig.VERSION_NAME));
        }
    }
    private Runnable mRunnable = this::navigateToHomeActivity;

    private void navigateToHomeActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        navigateToDifferentScreen(intent,true,R.anim.enter_from_right, R.anim.exit_to_left);
    }
}
