package com.cyou.xiyou.cyou.common.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.cyou.xiyou.cyou.common.R;
import com.cyou.xiyou.cyou.common.util.ConfigUtil.DefaultConfigKey;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by 003 on 2017-05-22.
 */
public class CommonUtil
{
    private static final String MOBILE_REGEX = "(1[3|4|5|6|7|8|9]\\d{9})|(100\\d{8})";

    public static final String MAC_REGEX = "([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}";

    public static final String MAC_NO_SPLIT_REGEX = "([0-9A-Fa-f]{2}){6}";

    private static final int DEFAULT_STATUS_BAR_HEIGHT_DP = 25;

    private static int statusBarHeight;

    private static Camera camera;

    public static int dp2px(Context context, float dpValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        float px = dpValue * scale;
        int pxInt = (int)px;
        return px == pxInt? pxInt: pxInt + 1;
    }

    public static int px2dp(Context context, float pxValue)
    {
        float scale = context.getResources().getDisplayMetrics().density;
        float dp = pxValue / scale;
        int dpInt = (int)dp;
        return dp == dpInt? dpInt: dpInt + 1;
    }

    public static float getDensityDpi(Context context)
    {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    @RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isNetworkAvailable(Context context)
    {
        boolean available = false;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null)
        {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if(networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected())
            {
                available = true;
            }
        }

        return available;
    }

    public static boolean isLocationEnabled(Context context)
    {
        boolean enabled = false;
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager != null)
        {
            enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }

        return enabled;
    }

    public static boolean isGPSEnabled(Context context)
    {
        boolean enabled = false;
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        if(locationManager != null)
        {
            enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        return enabled;
    }

    public static void startLocationSetting(Activity activity)
    {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static CharSequence fillString(Context context, int strResId, Object... args)
    {
        String str = ResourceUtil.getString(context, strResId);
        return fillString(str, args);
    }

    public static CharSequence fillHtmlString(Context context, int strResId, Object... args)
    {
        String str = ResourceUtil.getString(context, strResId);
        return fillHtmlString(str, args);
    }

    public static CharSequence fillString(String baseStr, Object... args)
    {
        return MessageFormat.format(baseStr, args);
    }

    @SuppressWarnings("deprecation")
    public static CharSequence fillHtmlString(String baseStr, Object... args)
    {
        CharSequence str = fillString(baseStr, args);
        str = Html.fromHtml(str.toString());
        return str;
    }

    public static View getContentView(Activity activity)
    {
        return ((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean hideStatusBarIfSupported(Activity activity)
    {
        boolean hasHide = false;
        Window window = activity.getWindow();

        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP)
        {
            View decorView = window.getDecorView();
            int systemUiVisibility = decorView.getSystemUiVisibility();
            decorView.setSystemUiVisibility(systemUiVisibility | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(0);
            hasHide = true;
        }
        else if(VERSION.SDK_INT >= VERSION_CODES.KITKAT)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            hasHide = true;
        }

        return hasHide;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void setStatusBarColorIfSupported(Activity activity, int color)
    {
        getContentView(activity).setFitsSystemWindows(true);
        Window window = activity.getWindow();

        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
        else if(VERSION.SDK_INT >= VERSION_CODES.KITKAT)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);
        }
    }

    public static void resetTopViewHeight(Context context, View topView)
    {
        int statusBarHeight = getStatusBarHeightKitkatOrHigh(context);
        topView.setPadding(topView.getPaddingLeft(), topView.getPaddingTop() + statusBarHeight, topView.getPaddingRight(), topView.getPaddingBottom());
        ViewGroup.LayoutParams lp = topView.getLayoutParams();

        if(lp != null && lp.height > 0)
        {
            lp.height += statusBarHeight;
        }
    }

    public static int getStatusBarHeight(int defaultHeight)
    {
        if(statusBarHeight == 0)
        {
            Resources resources = Resources.getSystem();
            int resId = resources.getIdentifier("status_bar_height", "dimen", "android");

            if(resId > 0)
            {
                statusBarHeight = resources.getDimensionPixelSize(resId);
            }

            if(statusBarHeight == 0)
            {
                statusBarHeight = defaultHeight;
            }
        }

        return statusBarHeight;
    }

    public static int getStatusBarHeight(Context context)
    {
        return getStatusBarHeight(dp2px(context, DEFAULT_STATUS_BAR_HEIGHT_DP));
    }

    public static int getStatusBarHeightKitkatOrHigh(Context context)
    {
        return getStatusBarHeightKitkatOrHigh(dp2px(context, DEFAULT_STATUS_BAR_HEIGHT_DP));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int getStatusBarHeightKitkatOrHigh(int defaultHeight)
    {
        int height = 0;

        if(VERSION.SDK_INT >= VERSION_CODES.KITKAT)
        {
            height = getStatusBarHeight(defaultHeight);
        }

        return height;
    }

    public static int parseInt(String str)
    {
        int value = 0;

        try
        {
            value = Integer.parseInt(str);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }

    public static long parseLong(String str)
    {
        long value = 0;

        try
        {
            value = Long.parseLong(str);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }

    public static double parseDouble(String str)
    {
        double value = 0;

        try
        {
            value = Double.parseDouble(str);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return value;
    }


    public static boolean validateMobile(String mobile)
    {
        return mobile != null && mobile.matches(MOBILE_REGEX);
    }

    public static boolean validateMac(String mac)
    {
        return mac != null && Pattern.matches(MAC_REGEX, mac);
    }

    public static void copyProperties(Object from, Object to, Class<?> clazz, String... excludedFields)
    {
        if(from == null || to == null)
        {
            return;
        }

        Set<String> excludedSet = new HashSet<>();

        if(excludedFields != null && excludedFields.length > 0)
        {
            excludedSet.addAll(Arrays.asList(excludedFields));
        }

        try
        {
            Field[] fields = clazz.getDeclaredFields();
            String fieldName, fieldName4Method, methodGetName, methodSetName;
            Method methodGet, methodSet;
            Object value;
            Class<?> type;

            for(Field field: fields)
            {
                fieldName = field.getName();

                if(Modifier.isStatic(field.getModifiers()) || excludedSet.contains(fieldName))
                {
                    continue;
                }

                type = field.getType();
                fieldName4Method = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                if(type.getSimpleName().equalsIgnoreCase("boolean"))
                {
                    if(fieldName.startsWith("is"))
                    {
                        methodGetName = fieldName;
                    }
                    else
                    {
                        methodGetName = "is" + fieldName4Method;
                    }
                }
                else
                {
                    methodGetName = "get" + fieldName4Method;
                }

                methodSetName = "set" + fieldName4Method;
                methodGet = clazz.getDeclaredMethod(methodGetName);
                methodSet = clazz.getDeclaredMethod(methodSetName, type);
                value = methodGet.invoke(from);
                methodSet.invoke(to, value);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List<String> getBeanFields(Class<?> clazz, boolean useJSONName)
    {
        List<String> fieldList = new ArrayList<>();

        try
        {
            Field[] fields = clazz.getDeclaredFields();
            JSONField jsonField;

            for(Field field: fields)
            {
                if(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()) || !Modifier.isPrivate(field.getModifiers()))
                {
                    continue;
                }

                if(useJSONName)
                {
                    jsonField = field.getAnnotation(JSONField.class);
                }
                else
                {
                    jsonField = null;
                }

                fieldList.add(jsonField != null? jsonField.name(): field.getName());
            }
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }

        return fieldList;
    }

    public static void setStatusBarMode(Activity activity, boolean darkMode)
    {
        Window window = activity.getWindow();

        if(VERSION.SDK_INT >= VERSION_CODES.M)
        {
            try
            {
                View decorView = window.getDecorView();
                int systemUiVisibility = decorView.getSystemUiVisibility();

                if(darkMode)
                {
                    systemUiVisibility |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                else
                {
                    systemUiVisibility &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }

                decorView.setSystemUiVisibility(systemUiVisibility);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }

        if(VERSION.SDK_INT >= VERSION_CODES.KITKAT && isMIUI(activity))
        {
            try
            {
                @SuppressLint("PrivateApi")
                Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                int darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = window.getClass().getMethod("setExtraFlags", int.class, int.class);
                extraFlagField.invoke(window, darkMode? darkModeFlag: 0, darkModeFlag);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }
    }

    public static boolean isMIUI(Context context)
    {
        Boolean miui = null;
        String miuiStr = ConfigUtil.getString(context, DefaultConfigKey.IsMIUI);

        if(miuiStr != null && !miuiStr.isEmpty())
        {
            try
            {
                miui = Boolean.parseBoolean(miuiStr);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        if(miui == null)
        {
            miui = false;
            Properties prop = new Properties();
            FileInputStream in = null;

            try
            {
                in = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
                prop.load(in);
                String[] keys = {"ro.miui.ui.version.code", "ro.miui.ui.version.name", "ro.miui.internal.storage"};

                for(String key: keys)
                {
                    if(prop.getProperty(key, null) != null)
                    {
                        miui = true;
                        break;
                    }
                }
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
            finally
            {
                if(in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }

                ConfigUtil.putBoolean(context, DefaultConfigKey.IsMIUI, miui);
            }
        }

        return miui;
    }

    public static void copyToClipboard(Context context, CharSequence text)
    {
        ClipboardManager clipboardManager = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboardManager != null)
        {
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
        }
    }

    public static void toSystemSetting(Context context)
    {
        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
    }

    public static void pendingTransitionAnimation(Activity activity, boolean finish)
    {
        if(finish)
        {
            pendingTransitionAnimation(activity, R.anim.in_leftright, R.anim.out_leftright);
        }
        else
        {
            pendingTransitionAnimation(activity, R.anim.in_rightleft, R.anim.out_rightleft);
        }
    }

    public static void pendingTransitionAnimation(Activity activity, int enterAnim, int exitAnim)
    {
        activity.overridePendingTransition(enterAnim, exitAnim);
    }

    public static void fadeView(View view, int visibility)
    {
        int oldVisibility = view.getVisibility();
        boolean show = visibility == View.VISIBLE;
        Animation animation = null;

        if(show && oldVisibility != View.VISIBLE)
        {
            animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_in);
        }
        else if(!show && oldVisibility == View.VISIBLE)
        {
            animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.fade_out);
        }

        if(animation != null)
        {
            view.setVisibility(visibility);
            view.startAnimation(animation);
        }
    }

    public static String splitMac(String mac)
    {
        StringBuilder macSb = null;

        if(!TextUtils.isEmpty(mac) && mac.matches(MAC_NO_SPLIT_REGEX))
        {
            macSb = new StringBuilder(mac);
            int length = macSb.length();

            for(int i = length - 2; i > 0; i -= 2)
            {
                macSb.insert(i, ':');
            }
        }

        return macSb == null? mac: macSb.toString();
    }

    public static String removeMacColon(String mac)
    {
        if(!TextUtils.isEmpty(mac))
        {
            mac = mac.replaceAll(":", "");
        }

        return mac;
    }

    public static long parseDate(DateFormat format, String date)
    {
        long millisecond = 0;

        try
        {
            millisecond = format.parse(date).getTime();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return millisecond;
    }

    public static String decodedURL(String url)
    {
        final String charset = "UTF-8";
        String result = url;

        if(!TextUtils.isEmpty(url))
        {
            try
            {
                result = new String(url.getBytes(), charset);
                result = URLDecoder.decode(result, charset);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String encodedURL(String url)
    {
        final String charset = "UTF-8";
        String result = url;

        if(!TextUtils.isEmpty(url))
        {
            try
            {
                result = new String(url.getBytes(), charset);
                result = URLEncoder.encode(result, charset);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 是否安装了支付宝
     */
    public static boolean alipayInstalled(Context context)
    {
        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    public static void startCamera(Activity activity, Uri targetUri, int requestCode)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT >= VERSION_CODES.N)
        {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, targetUri.getPath());
            Uri uri = activity.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, requestCode);
        }
        else
        {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, targetUri);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static boolean hasDigit(String str)
    {
        return !TextUtils.isEmpty(str) && str.matches(".*\\d+.*");
    }

    public static boolean switchFlashlight(Context context, boolean on)
    {
        boolean success = false;

        try
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                CameraManager cameraManager = (CameraManager)context.getSystemService(Context.CAMERA_SERVICE);

                if(cameraManager != null)
                {
                    cameraManager.setTorchMode("0", on);
                    success = true;
                }
            }
            else
            {
                if(on && camera == null)
                {
                    FeatureInfo[] featureInfos = context.getPackageManager().getSystemAvailableFeatures();

                    for(FeatureInfo info: featureInfos)
                    {
                        if(info.name.equals(PackageManager.FEATURE_CAMERA_FLASH))
                        {
                            camera = Camera.open();
                            Camera.Parameters parameters = camera.getParameters();
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(parameters);
                            camera.startPreview();
                            success = true;
                            break;
                        }
                    }
                }
                else if(!on && camera != null)
                {
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    success = true;
                }
            }
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }

        return success;
    }
}