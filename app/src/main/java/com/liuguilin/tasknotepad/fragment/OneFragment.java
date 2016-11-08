package com.liuguilin.tasknotepad.fragment;

/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.fragment
 *  文件名:   OneFragment
 *  创建者:   LGL
 *  创建时间:  2016/10/24 13:31
 *  描述：    one
 */

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;
import com.liuguilin.tasknotepad.R;
import com.liuguilin.tasknotepad.db.TaskDBHelper;
import com.liuguilin.tasknotepad.entity.ActionEvent;
import com.liuguilin.tasknotepad.entity.DataBean;
import com.liuguilin.tasknotepad.utils.LogUtils;
import com.liuguilin.tasknotepad.utils.NetworkUtil;
import com.liuguilin.tasknotepad.view.MyDigitalClock;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OneFragment extends Fragment implements View.OnClickListener {

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String city;

    private MyDigitalClock tv_clock;

    private TextView tv_date, tv_week, city_name;
    private TextView tv_temperature, tv_week2, tv_temperature2,
            tv_week3, tv_temperature3, tv_week4, tv_temperature4;
    private ImageView Iv_img, Iv_img2, Iv_img3, Iv_img4,img_weather;

    private Button mButton;
    private EditText mEditText;
    private List<DataBean> mList = new ArrayList<>();
    private String text;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        findView(view);
        return view;
    }

    //初始化
    private void findView(View view) {
        tv_clock = (MyDigitalClock) view.findViewById(R.id.tv_clock);
        tv_date = (TextView) view.findViewById(R.id.tv_date);
        tv_week = (TextView) view.findViewById(R.id.tv_week);
        city_name = (TextView) view.findViewById(R.id.city_name);
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_week2 = (TextView) view.findViewById(R.id.tv_week2);
        tv_temperature2 = (TextView) view.findViewById(R.id.tv_temperature2);
        tv_week3 = (TextView) view.findViewById(R.id.tv_week3);
        tv_temperature3 = (TextView) view.findViewById(R.id.tv_temperature3);
        tv_week4 = (TextView) view.findViewById(R.id.tv_week4);
        tv_temperature4 = (TextView) view.findViewById(R.id.tv_temperature4);
        Iv_img = (ImageView) view.findViewById(R.id.Iv_img);
        Iv_img2 = (ImageView) view.findViewById(R.id.Iv_img2);
        Iv_img3 = (ImageView) view.findViewById(R.id.Iv_img3);
        Iv_img4 = (ImageView) view.findViewById(R.id.Iv_img4);
        mButton = (Button) view.findViewById(R.id.mButton);
        mButton.setOnClickListener(this);
        mEditText = (EditText) view.findViewById(R.id.mEditText);
        img_weather = (ImageView) view.findViewById(R.id.img_weather);

        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(myListener);
        isNetwork();//判断是否有网络
    }


    /**
     * 判断是否有网络
     */
    private void isNetwork() {
        if(NetworkUtil.isNetworkAvailable(getActivity())){
            initLocation();
            //开始定位
            mLocationClient.start();
            city_name.setText("正在定位...");
            img_weather.setImageResource(R.mipmap.icon_location);
        }else {
            new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.network_connection_error))
                .setMessage(getString(R.string.is_go_netwotk_setting)).setPositiveButton(getString(R.string.intent_setting), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    NetworkUtil.openSystemSetting(getActivity());
                }
            }).setNegativeButton(getString(R.string.finish), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    System.exit(0);
                }
            }).show();
        }
    }


    //初始化定位
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span = 0;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mButton:
                text = mEditText.getText().toString().trim();
                LogUtils.debug(text);
                if (text.toString() != null && !text.toString().equals("")) {
                    addAction();
                } else {
                    LogUtils.showToast("请输入一个任务");
                }
                break;
        }
    }


    private void addAction() {
        //一句代码
        mList.clear();
        DataBean bean = new DataBean();
        // 月-日
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
        // 时-分
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        bean.setMsg(text);
        bean.setDay(sdf.format(new Date()));
        bean.setTime(sdf1.format(new Date()));
        mList.add(bean);
        /**
         * 发送消息
         */
        ActionEvent event = new ActionEvent();
        event.setType("button");
        event.setList(mList);
        EventBus.getDefault().post(event);
        setTaskToSqlite();
        mEditText.setText("");
        LogUtils.showToast("记录成功");

    }


    /**
     * 将记录的任务插入到数据库
     */
    private void setTaskToSqlite() {
        TaskDBHelper helper = new TaskDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            DataBean bean = null;
            for (int i = 0; i < mList.size(); i++) {
                bean = mList.get(i);
                String sql = "insert into taskinfo(id,day,time,content)values(?,?,?,?)";
                db.execSQL(sql, new Object[]{bean.getId(), bean.getDay(), bean.getTime(), bean.getMsg()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            switch (location.getLocType()) {
                case BDLocation.TypeGpsLocation:
                case BDLocation.TypeNetWorkLocation:
                case BDLocation.TypeOffLineLocation:
                    getWeather(location.getCity());
                    break;
                case BDLocation.TypeServerError:
                case BDLocation.TypeNetWorkException:
                case BDLocation.TypeCriteriaException:
                    //如果定位失败，默认失败
                    getWeather("深圳");
                    break;
            }
        }
    }

    //获取天气
    private void getWeather(String city) {
        String url = "http://op.juhe.cn/onebox/weather/query?cityname="
                + city + "&key=2abbac0d44bda7f7300e852b1a0c5196";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }

            @Override
            public void onFailure(VolleyError error) {
                LogUtils.debug("````````````````" + error);
            }
        });
    }

    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            JSONObject jsonObject2 = jsonObject1.getJSONObject("data");
            JSONObject jsonObject3 = jsonObject2.getJSONObject("realtime");

            tv_date.setText(jsonObject3.getString("date"));

            city_name.setText(jsonObject3.getString("city_name"));

            JSONObject jsonObject4 = jsonObject3.getJSONObject("weather");
            tv_temperature.setText(jsonObject4.getString("temperature") + "°");

            if (jsonObject4.getString("info").equals("阴")) {
                Iv_img.setImageResource(R.mipmap.duoyun);
            } else if (jsonObject4.getString("info").equals("多云")) {
                Iv_img.setImageResource(R.mipmap.duoyun);
            } else if (jsonObject4.getString("info").equals("阵雨")) {
                Iv_img.setImageResource(R.mipmap.baoyu);
            } else if (jsonObject4.getString("info").equals("晴")) {
                Iv_img.setImageResource(R.mipmap.qing);
            } else if (jsonObject4.getString("info").equals("小雨")) {
                Iv_img.setImageResource(R.mipmap.xiaoyu);
            } else {
                Iv_img.setImageResource(R.mipmap.other);
            }

            JSONArray jsonArray = jsonObject2.getJSONArray("weather");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.get(i);
                tv_week.setText("周" + ((JSONObject) jsonArray.get(0)).getString("week"));
                tv_week2.setText("周" + ((JSONObject) jsonArray.get(1)).getString("week"));
                tv_week3.setText("周" + ((JSONObject) jsonArray.get(2)).getString("week"));
                tv_week4.setText("周" + ((JSONObject) jsonArray.get(3)).getString("week"));

                JSONObject object1 = object.getJSONObject("info");

                JSONArray jsonArray1 = object1.getJSONArray("day");


                JSONArray jsonArray2 = ((JSONObject) jsonArray.get(1)).getJSONObject("info").getJSONArray("day");
                JSONArray jsonArray3 = ((JSONObject) jsonArray.get(2)).getJSONObject("info").getJSONArray("day");
                JSONArray jsonArray4 = ((JSONObject) jsonArray.get(3)).getJSONObject("info").getJSONArray("day");

                LogUtils.debug(jsonArray2 + "---" + jsonArray3 + "---" + jsonArray4);
                if (jsonArray2.get(1).equals("阵雨")) {
                    Iv_img2.setImageResource(R.mipmap.baoyu);
                } else if (jsonArray2.get(1).equals("大雨")) {
                    Iv_img2.setImageResource(R.mipmap.baoyu);
                } else if (jsonArray2.get(1).equals("小雨")) {
                    Iv_img2.setImageResource(R.mipmap.xiaoyu);
                } else if (jsonArray2.get(1).equals("阴")) {
                    Iv_img2.setImageResource(R.mipmap.duoyun);
                } else {
                    Iv_img2.setImageResource(R.mipmap.other);
                }

                if (jsonArray3.get(1).equals("阵雨")) {
                    Iv_img3.setImageResource(R.mipmap.baoyu);
                } else if (jsonArray3.get(1).equals("大雨")) {
                    Iv_img3.setImageResource(R.mipmap.baoyu);
                } else if (jsonArray3.get(1).equals("小雨")) {
                    Iv_img3.setImageResource(R.mipmap.xiaoyu);
                } else if (jsonArray3.get(1).equals("阴")) {
                    Iv_img3.setImageResource(R.mipmap.duoyun);
                } else {
                    Iv_img3.setImageResource(R.mipmap.other);
                }

                if (jsonArray4.get(1).equals("阵雨")) {
                    Iv_img4.setImageResource(R.mipmap.baoyu);
                } else if (jsonArray4.get(1).equals("大雨")) {
                    Iv_img4.setImageResource(R.mipmap.baoyu);
                } else if (jsonArray4.get(1).equals("小雨")) {
                    Iv_img4.setImageResource(R.mipmap.xiaoyu);
                } else if (jsonArray4.get(1).equals("阴")) {
                    Iv_img4.setImageResource(R.mipmap.duoyun);
                } else {
                    Iv_img4.setImageResource(R.mipmap.other);
                }

                tv_temperature2.setText(jsonArray2.get(2).toString() + "°");
                tv_temperature3.setText(jsonArray2.get(2).toString() + "°");
                tv_temperature4.setText(jsonArray2.get(2).toString() + "°");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
