package com.liuguilin.tasknotepad.ui;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.ui
 *  文件名:   SplashActivity
 *  创建者:   LGL
 *  创建时间:  2016/10/24 13:21
 *  描述：    TODO
 */

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liuguilin.tasknotepad.MainActivity;
import com.liuguilin.tasknotepad.R;
import com.liuguilin.tasknotepad.utils.PermissionHelper;
import net.youmi.android.AdManager;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SplashViewSettings;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "Task";
    private PermissionHelper mPermissionHelper;

    private TextView tvSplash;

    private String appId = "50329520ed7682a2";
    private String appSecret = "6eff0a4afdb9df8a";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initData();
    }


    private void initData() {

        // 当系统为6.0以上时，需要申请权限
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.setOnApplyPermissionListener(new PermissionHelper.OnApplyPermissionListener() {
            @Override
            public void onAfterApplyAllPermission() {
                Log.i(TAG, "All of requested permissions has been granted, so run app logic.");
                runApp();
            }
        });
        if (Build.VERSION.SDK_INT < 23) {
            // 如果系统版本低于23，直接跑应用的逻辑
            Log.d(TAG, "The api level of system is lower than 23, so run app logic directly.");
            runApp();
        } else {
            // 如果权限全部申请了，那就直接跑应用逻辑
            if (mPermissionHelper.isAllRequestedPermissionGranted()) {
                Log.d(TAG, "All of requested permissions has been granted, so run app logic directly.");
                runApp();
            } else {
                // 如果还有权限为申请，而且系统版本大于23，执行申请权限逻辑
                Log.i(TAG, "Some of requested permissions hasn't been granted, so apply permissions first.");
                mPermissionHelper.applyPermissions();
            }
        }
    }
    /**
     * 跑应用的逻辑
     */
    private void runApp() {
        tvSplash = (TextView) findViewById(R.id.tvSplash);
        rotateyAnimRun(tvSplash);
        //初始化SDK
        AdManager.getInstance(this).init(appId, appSecret,false,false);
        //设置开屏
        setupSplashAd();
    }
    /**
     * 设置开屏广告
     */
    private void setupSplashAd() {
        // 创建开屏容器
        final RelativeLayout splashLayout = (RelativeLayout) findViewById(R.id.rl_splash);
        RelativeLayout.LayoutParams params =
            new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ABOVE, R.id.view_divider);

        // 对开屏进行设置
        SplashViewSettings splashViewSettings = new SplashViewSettings();
        // 设置跳转的窗口类
        splashViewSettings.setTargetClass(MainActivity.class);
        // 设置开屏的容器
        splashViewSettings.setSplashViewContainer(splashLayout);

        // 展示开屏广告
        SpotManager.getInstance(this)
            .showSplash(this, splashViewSettings, new SpotListener() {

                @Override
                public void onShowSuccess() {
                    Log.d(TAG, "开屏展示成功");
                    splashLayout.setVisibility(View.VISIBLE);
                    splashLayout.startAnimation(AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash));
                }

                @Override
                public void onShowFailed(int errorCode) {
                    Log.d(TAG, "开屏展示失败");
                    switch (errorCode) {
                        case ErrorCode.NON_NETWORK:
                            Log.e(TAG, "无网络");
                            break;
                        case ErrorCode.NON_AD:
                            Log.e(TAG, "无广告");
                            break;
                        case ErrorCode.RESOURCE_NOT_READY:
                            Log.e(TAG, "资源还没准备好");
                            break;
                        case ErrorCode.SHOW_INTERVAL_LIMITED:
                            Log.e(TAG, "展示间隔限制");
                            break;
                        case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                            Log.e(TAG, "控件处在不可见状态");
                            break;
                    }
                }

                @Override
                public void onSpotClosed() {
                    Log.d(TAG, "开屏被关闭");
                }

                @Override
                public void onSpotClicked(boolean isWebPage) {
                    Log.d(TAG, "开屏被点击");
                    Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                }
            });
    }
    //翻转动画
    public void rotateyAnimRun(View view) {
        ObjectAnimator.ofFloat(view, "rotationX", 0.0F, 360.0F).setDuration(2000).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPermissionHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onBackPressed() {
        // 点击后退关闭插播广告
        // 当只嵌入插屏或轮播插屏其中一种广告时，不需要外层if判断
        if (SpotManager.getInstance(this).isSpotShowing() ||
            SpotManager.getInstance(this).isSlideableSpotShowing()) {
            if (SpotManager.getInstance(this).isSpotShowing()) {
                SpotManager.getInstance(this).hideSpot();
            }
            if (SpotManager.getInstance(this).isSlideableSpotShowing()) {
                SpotManager.getInstance(this).hideSlideableSpot();
            }
        } else {
            //super.onBackPressed();//禁止退出
        }
    }
}
