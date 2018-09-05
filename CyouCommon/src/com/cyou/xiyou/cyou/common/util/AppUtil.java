package com.cyou.xiyou.cyou.common.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.cyou.xiyou.cyou.common.util.ConfigUtil.DefaultConfigKey;

import java.util.List;

/**
 * Created by 003 on 2017-05-22.
 */
public class AppUtil
{
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getDeviceId(Context context)
    {
        String deviceId = ConfigUtil.getGlobalString(context, DefaultConfigKey.DeviceId);

        if(TextUtils.isEmpty(deviceId))
        {
            deviceId = DeviceCodeCreator.create(context);
            ConfigUtil.putGlobalString(context, DefaultConfigKey.DeviceId, deviceId);
        }

        return deviceId;
    }

    public static String getAppName(Context context)
    {
        String appName = null;

        try
        {
            PackageManager packageManager = context.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            appName = packageManager.getApplicationLabel(applicationInfo).toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return appName == null? "": appName;
    }

    public static String getAppVersion(Context context)
    {
        String versionName = null;

        try
        {
            PackageInfo info = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = info.versionName;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return versionName;
    }

    public static int getVersionCode(Context context)
    {
        int versionCode = 0;

        try
        {
            PackageInfo info = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return versionCode;
    }

    public static String getProcessName(Context context)
    {
        String processName = null;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfos = activityManager == null? null: activityManager.getRunningAppProcesses();

        if(processInfos != null && !processInfos.isEmpty())
        {
            int pid = android.os.Process.myPid();

            for(RunningAppProcessInfo processInfo: processInfos)
            {
                if(processInfo.pid == pid)
                {
                    processName = processInfo.processName;
                    break;
                }
            }
        }

        return processName;
    }

    public static boolean isMainProcess(Context context)
    {
        String processName = getProcessName(context);
        return processName != null && processName.equals(context.getPackageName());
    }
}