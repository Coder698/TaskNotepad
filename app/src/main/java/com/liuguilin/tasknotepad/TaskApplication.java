package com.liuguilin.tasknotepad;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad
 *  文件名:   TaskApplication
 *  创建者:   LGL
 *  创建时间:  2016/10/24 13:45
 *  描述：    Application
 */

import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class TaskApplication extends Application {
    private static Context context;
    private static RequestQueue queues ;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        queues = Volley.newRequestQueue(getApplicationContext());
    }

    public static Context getContext(){
        return context;
    }
    public static RequestQueue getHttpQueues() {
        return queues;
    }
}
