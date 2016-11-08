package com.liuguilin.tasknotepad.utils;
/*
 *  项目名：  TaskNotepad 
 *  包名：    com.liuguilin.tasknotepad.utils
 *  文件名:   LogUtils
 *  创建者:   LGL
 *  创建时间:  2016/10/24 13:57
 *  描述：    Log
 */

import android.util.Log;
import android.widget.Toast;
import com.liuguilin.tasknotepad.TaskApplication;

public class LogUtils {

    public static final boolean DEBUG = true;
    public static final String TAG = "Task";
    private static Toast toast;
    public static void debug(String msg) {
        if (DEBUG) {
            System.err.println(TAG + msg);
            Log.d(TAG, msg);
        }
    }

    public static void showToast( String content) {
        if (toast == null) {  //判断Toast对象是否为空
            toast = Toast.makeText(TaskApplication.getContext(), content, Toast.LENGTH_LONG);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
