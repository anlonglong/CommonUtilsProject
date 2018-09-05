package com.cyou.xiyou.cyou.common.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
/**
 * @author anlonglong on 2018/9/5.
 * Email： 940752944@qq.com
 */
@SuppressWarnings("all")
public class NotificationUtil {
    public static int sNotifyId = 0;
    //如果是悬浮通知， 值为NotificationManager.IMPORTANCE_HIGH
    public static int sChannledImportant = NotificationManager.IMPORTANCE_DEFAULT;
    public static String sChannledId = "渠道ID";
    private static Context sContext;
    private static NotificationManager manager;


    //注入上下文 在Application中初始化
    public static void init(Context context) {
        sContext = context;
        initNotificationManager();
    }


    public static void initNotificationManager() {
        if (null == manager) {
            manager = (NotificationManager) sContext.getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (null == manager) {
            return;
        }
        if (isAndroidN() && !manager.areNotificationsEnabled()) {
            //用户关掉通知以后，无法收到任何的通知
            Log.e("NotificationUtils", "通知渠道没有打开(⊙︿⊙)");
            return;
        }
    }

    public static void notify(@NonNull final String channledId,
                              @NonNull CharSequence title, @NonNull CharSequence text,
                              @NonNull CharSequence subText, @DrawableRes int smallIconResId,
                              boolean autocancel, PendingIntent pendingIntent, int pri) {
        NotificationCompat.Builder builder = createBuilder(channledId, title, text, smallIconResId, subText, autocancel, pendingIntent, pri);
        initNotificationChannel(channledId);
        manager.notify(sNotifyId, builder.build());
    }

    //id一般为0
    public static void notify(Notification notification, int id) {
        //NotificationManager manager = getNotificationManager(context);
        if (isAndroidO()) {
            initNotificationChannel(notification.getChannelId());
        }
        if (null == manager) {
            return;
        }
        manager.notify(id, notification);
    }


    //带进度通知的练习
    private static void progressNoti(@NonNull final String channledId, final NotificationManager manager, final NotificationCompat.Builder builder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //builder.setContentTitle("下载中");
                //builder.setSubText("").setContentText("");
                for (int i = 0 ; i <= 100; i+=5) {
                    builder.setProgress(100,i,false);
                    //更新这个sID对应的通知出现的次数，显示在通知的右下角
                    initNotificationChannel(channledId);
                    manager.notify(sNotifyId, builder.build());
                    SystemClock.sleep(5000);
                }
                //setProgress(0,0,false);删除指示器
                builder.setContentTitle("下载完成").setProgress(0,0,false);
                //更新这个sID对应的通知出现的次数，显示在通知的右下角
                initNotificationChannel(channledId);
                manager.notify(sNotifyId, builder.build());
            }
        }).start();
    }



    /**
     * 通知的设计规范要求必须要有的几个参数
     */
    public static NotificationCompat.Builder createBaseBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(sContext, channledId);
        return builder.setContentTitle(title)
                .setContentText(text)
                //图标在有的手机上面不起作用，比如乐视的手机取的就是App的应用图标，一加手机添加会显示设置的icon
                .setSmallIcon(smallIconResId)
                //通知的内容锁屏是否可见
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setChannelId(channledId)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL);

    }

    //id一般为0 实现进度条通知方法1
    public static NotificationCompat.Builder createProgressBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId, int progress) {
        NotificationCompat.Builder baseBuilder = createBaseBuilder(channledId, title, text, smallIconResId);
        return baseBuilder.setProgress(100,progress,false);
    }

    //id一般为0 实现进度条通知方法2
    public static void notifyWithProgress(@NonNull String channledId, CharSequence title, @DrawableRes int smallIconResId, ProgressListener listener) {
        initNotificationChannel(channledId);
        NotificationCompat.Builder baseBuilder = createBaseBuilder(channledId,title,"",smallIconResId);
        if (null != listener) {
            listener.progress(manager, baseBuilder, sNotifyId);
        }
    }

    /*
     * 带进度条事件的监听
     */
    public interface ProgressListener {
        void progress(NotificationManager manager,NotificationCompat.Builder builder, int id);
    }


    public static NotificationCompat.Builder createCancelBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId, boolean autocancel) {
        NotificationCompat.Builder builder = createBaseBuilder(channledId, title, text, smallIconResId);
        return builder.setAutoCancel(autocancel);
    }

    public static NotificationCompat.Builder createSubTextlBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId, CharSequence subText, boolean autocancel) {
        NotificationCompat.Builder builder = createBaseBuilder(channledId, title, text, smallIconResId);
        return builder.setAutoCancel(autocancel).setSubText(subText);
    }

    public static NotificationCompat.Builder createPendingIntentBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId, PendingIntent pendingIntent) {
        NotificationCompat.Builder baseBuilder = createBaseBuilder(channledId, title, text, smallIconResId);
        return baseBuilder.setContentIntent(pendingIntent);
    }

    public static NotificationCompat.Builder createNumberBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId, int numbner) {
        NotificationCompat.Builder baseBuilder = createBaseBuilder(channledId, title, text, smallIconResId);
        return baseBuilder.setNumber(numbner);
    }


    public static NotificationCompat.Builder createBuilder(@NonNull String channledId, @NonNull CharSequence title, @NonNull CharSequence text, @DrawableRes int smallIconResId, @NonNull CharSequence subText, boolean autocancel, PendingIntent pendingIntent, int pri) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(sContext, channledId);
        builder.setContentTitle(title)
                .setContentText(text)
                .setSubText(subText)
                //图标在有的手机上面不起作用，比如乐视的手机取的就是App的应用图标，一加手机添加会显示设置的icon
                .setSmallIcon(smallIconResId)
                //通知的内容锁屏是否可见
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setChannelId(channledId);
        if (autocancel) {
            builder.setAutoCancel(autocancel);
        }
        if (null != pendingIntent) {
            builder .setContentIntent(pendingIntent);
        }
        if (NotificationCompat.PRIORITY_MAX <= pri && pri >= NotificationCompat.PRIORITY_MAX){
            builder.setPriority(pri);
        }
        return builder;
    }



    //8.0 的手机必须调用这个方法
    public static void initNotificationChannel(@NonNull String channledId) {
        if (isAndroidO()) {
            NotificationChannel notificationChannel = new NotificationChannel(channledId, "推广渠道", sChannledImportant);
            notificationChannel.setShowBadge(true);
            //是否可以绕过"请勿打扰"
            notificationChannel.setBypassDnd(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.enableLights(true);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    private static boolean isAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private static boolean isAndroidN() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }


    /**
     * 一般的Activity，【需返回栈的activity】；但是要在清单文件中注册，注册形式如下
     * <activity
     * android:name=".ResultActivity"
     * android:parentActivityName=".MainActivity"> 一般是应用的主页面
     * <meta-data
     * android:name="android.support.PARENT_ACTIVITY"
     * android:value=".MainActivity" 一般是应用的主页面
     * />
     * </activity>
     * <p>
     * 这种方式创建的PendingIntent，需要在清单文件中注册一些数据,使得在通知中打开的Activity具有任务栈，
     * 可以响应返回按钮的操作，比如，在通知中打开应用中的某个Activity，按返回键以后可以回到该应用中的主页面
     * https://developer.android.google.cn/guide/topics/ui/notifiers/notifications#NotificationResponse
     *
     * @param activityClass 通知中要打开的Activity.class
     * @return PendingIntent
     */
    public static PendingIntent getRegularPendingIntent(Class<?> activityClass) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(sContext);
        Intent nextIntent = new Intent(sContext, activityClass);
        taskStackBuilder.addParentStack(activityClass)
                .addNextIntent(nextIntent);
        return taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    /**
     * 特殊的Activity【无需返回栈的activity】；
     * <activity
     * android:name=".NotificationTestActivity"
     * android:excludeFromRecents="true"
     * android:launchMode="singleTask"
     * android:taskAffinity="">
     * </activity>
     *
     * @param activityClass 通知中要打开的Activity.class
     * @return PendingIntent
     */
    public static PendingIntent getSpecialPendingIntent(Class<?> activityClass) {
        Intent intent = new Intent(sContext, activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(sContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private static void cancelNotification(int id){
        if (null != manager) {
            if (id == -1) {
                manager.cancelAll();
            } else {
                manager.cancel(id);
            }
        }
    }

    public static void cancelAllNotification() {
        cancelNotification(-1);
    }

    public static void cancelNotificationById( int id) {
        cancelNotification(id);
    }

    /**
     * 设置对应的通知渠道
     * intent需要两个额外内容来指定应用程序的包名称（也称为应用程序ID）和要编辑的通道。
     * @param context
     * @param notification
     */
    public static void openNotificationSetting(NotificationChannel notification){
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, sContext.getPackageName());
        intent.putExtra(Settings.EXTRA_CHANNEL_ID, notification.getId());
        sContext.startActivity(intent);
    }

}
