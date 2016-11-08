package com.liuguilin.tasknotepad.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.liuguilin.tasknotepad.R;
import java.util.List;

/**
 * Created by adu on 2016/11/5.
 */

public class ShareUtil {

    /**
     * 反馈
     */
    public static void feedback(Context context) {
        Uri uri = Uri.parse("mailto:dpc_206@163.com");
        final Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (infos == null || infos.size() <= 0){
            LogUtils.showToast(context.getString(R.string.no_email_app_tip));
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 分享
     */
    public static void share(Context context, String content){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.share)));
    }
}
