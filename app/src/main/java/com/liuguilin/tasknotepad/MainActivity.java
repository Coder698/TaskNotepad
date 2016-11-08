package com.liuguilin.tasknotepad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import com.liuguilin.tasknotepad.fragment.OneFragment;
import com.liuguilin.tasknotepad.fragment.TwoFragment;
import com.liuguilin.tasknotepad.ui.SettingActivity;

import com.liuguilin.tasknotepad.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import net.youmi.android.normal.spot.SpotManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout mTablayout;
    private ViewPager mViewPager;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private FloatingActionButton fab;
    //退出标记
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);
        initView();
    }

    private void initView() {

        mTitles = new ArrayList<>();
        mTitles.add("设置任务");
        mTitles.add("任务列表");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        fab.setVisibility(View.GONE);
        //初始化View
        mTablayout = (TabLayout) findViewById(R.id.id_tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.id_view_pager);

        //预加载
        mViewPager.setOffscreenPageLimit(mTitles.size());

        //初始化List<Fragment>
        mFragments = new ArrayList<>();
        mFragments.add(new OneFragment());
        mFragments.add(new TwoFragment());

        //给ViewPage设置Adapter
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        });

        mTablayout.setupWithViewPager(mViewPager);
        mTablayout.setTabTextColors(Color.WHITE,0xFFFFCC00);
        mTablayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTablayout.setTabMode(TabLayout.MODE_FIXED);

        //监听滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    fab.setVisibility(View.GONE);
                }else {
                    fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
        }
    }

    /**
     * 监听按钮
     *
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        //如果按退出
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                exitAPP();
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 退出应用
     */
    private void exitAPP() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            LogUtils.showToast("再次点击退出应用");
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }

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
