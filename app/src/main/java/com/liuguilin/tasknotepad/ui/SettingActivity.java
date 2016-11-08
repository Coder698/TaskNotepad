package com.liuguilin.tasknotepad.ui;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.ui
 *  文件名:   SettingActivity
 *  创建者:   LGL
 *  创建时间:  2016/10/24 13:33
 *  描述：    设置
 */

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.liuguilin.tasknotepad.R;
import com.liuguilin.tasknotepad.TaskApplication;
import com.liuguilin.tasknotepad.utils.ShareUtil;
import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;
import net.youmi.android.normal.common.ErrorCode;
import net.youmi.android.normal.spot.SpotListener;
import net.youmi.android.normal.spot.SpotManager;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvVersion;
    private TextView tvLaoliu;
    private TextView tvAdu;
    private TextView tvGithub;
    private TextView tvFeedback;
    private static final String TAG = "TaskNoyepad";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setElevation(0);
        initView();
    }

    //初始化
    private void initView() {
        tvVersion = (TextView) findViewById(R.id.tvVersion);
        tvLaoliu = (TextView) findViewById(R.id.laoliu);
        tvAdu = (TextView) findViewById(R.id.adu);
        tvGithub = (TextView) findViewById(R.id.tvGithub);
        tvFeedback = (TextView) findViewById(R.id.tvFeedback);

        tvLaoliu.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        tvAdu.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvGithub.setOnClickListener(this);
        tvLaoliu.setOnClickListener(this);
        tvAdu.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);

        getVerson();//获取应用的版本号
        setupSpotAd();// 设置插屏广告
    }



    private void getVerson() {
        PackageManager manager = getPackageManager();
        PackageInfo info;
        try {
            info = manager.getPackageInfo(getPackageName(),0);
            String version = info.versionName;
            tvVersion.setText("版本号:"+version);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override public void onClick(View v) {
        Intent intent = null;
        Bundle b = null;
        switch (v.getId()){
            case R.id.tvGithub:
                intent = new Intent(TaskApplication.getContext(),WebViewActivity.class);
                b = new Bundle();
                b.putString("title","项目开源地址");
                b.putString("url","https://github.com/m7775223/TaskNotepad");
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.laoliu:
                intent = new Intent(TaskApplication.getContext(),WebViewActivity.class);
                b = new Bundle();
                b.putString("title","刘某人程序员博客");
                b.putString("url","http://blog.csdn.net/qq_26787115");
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.adu:
                intent = new Intent(TaskApplication.getContext(),WebViewActivity.class);
                b = new Bundle();
                b.putString("title","程序员杜鹏程博客");
                b.putString("url","http://blog.csdn.net/m366917");
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.tvFeedback:
                ShareUtil.feedback(this);
                break;
        }
    }

    /**
     * 设置插屏广告
     */
    private void setupSpotAd() {
        // 设置插屏图片类型，默认竖图
        //		// 横图
        //		SpotManager.getInstance(mContext).setImageType(SpotManager
        // .IMAGE_TYPE_HORIZONTAL);
        // 竖图
        SpotManager.getInstance(this).setImageType(SpotManager.IMAGE_TYPE_VERTICAL);

        // 设置动画类型，默认高级动画
        //		// 无动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_NONE);
        //		// 简单动画
        //		SpotManager.getInstance(mContext).setAnimationType(SpotManager
        // .ANIMATION_TYPE_SIMPLE);
        // 高级动画
        SpotManager.getInstance(this)
            .setAnimationType(SpotManager.ANIMATION_TYPE_ADVANCED);

                // 展示插屏广告
                SpotManager.getInstance(SettingActivity.this).showSpot(SettingActivity.this, new SpotListener() {

                    @Override
                    public void onShowSuccess() {
                        Log.d(TAG, "插屏展示成功");
                    }

                    @Override
                    public void onShowFailed(int errorCode) {
                        Log.d(TAG, "插屏展示失败");
                        switch (errorCode) {
                            case ErrorCode.NON_NETWORK:
                                Toast.makeText(SettingActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.NON_AD:
                                Toast.makeText(SettingActivity.this, "暂无广告", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.RESOURCE_NOT_READY:
                                Log.e(TAG, "资源还没准备好");
                                Toast.makeText(SettingActivity.this, "请稍后再试", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.SHOW_INTERVAL_LIMITED:
                                Log.e(TAG, "展示间隔限制");
                                Toast.makeText(SettingActivity.this, "请勿频繁展示", Toast.LENGTH_SHORT).show();
                                break;
                            case ErrorCode.WIDGET_NOT_IN_VISIBILITY_STATE:
                                Log.e(TAG, "控件处在不可见状态");
                                Toast.makeText(SettingActivity.this, "请设置插屏为可见状态", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onSpotClosed() {
                        Log.d(TAG, "插屏被关闭");
                    }

                    @Override
                    public void onSpotClicked(boolean isWebPage) {
                        Log.d(TAG, "插屏被点击");
                        Log.i(TAG, String.format("是否是网页广告？%s", isWebPage ? "是" : "不是"));
                    }
                });
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 插播广告
        SpotManager.getInstance(this).onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 插播广告
        SpotManager.getInstance(this).onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 插播广告
        SpotManager.getInstance(this).onDestroy();

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
            super.onBackPressed();
        }
    }
}
