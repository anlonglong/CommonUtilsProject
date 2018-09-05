package com.cyou.xiyou.cyou.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

/**
 * Created by anlonglong on 2018/7/23.
 * Email： 940752944@qq.com
 *
 * @author aillen
 * asset资源目录中后缀名为.properties文件的内容只能读不能往连写，但是可以把里面的
 * 数据写到外部存储。
 */
public class AssetUtil {
    /**
     * 读取asset资源目录中后缀名为.properties文件中的值
     *
     * @param context  上下文
     * @param fileName 文件名
     * @param key      key
     * @return key对应的值
     */
    public static String getPropertyValue(Context context, String fileName, String key) {
        Properties properties = getProperties(context, fileName);
        return !properties.isEmpty() && properties.containsKey(key) ? properties.getProperty(key) : "";
    }

    public static Properties getProperties(Context context, String fileName) {
        AssetManager assetManager = getAssets(context);
        InputStream inputStream = null;
        Properties properties = new Properties();
        try {
            inputStream = assetManager.open(fileName);
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static AssetManager getAssets(Context context) {
        return context.getResources().getAssets();
    }

    public static Set<String> getKeySet(Context context, String fileName) {
        return getProperties(context, fileName).stringPropertyNames();
    }

    /**
     * copy assets中的文件到copy->SD卡 with key && value
     *
     * @param activity
     * @param fileName
     * @param key
     * @param value
     */
    public static void saveToSD(Activity activity, String fileName, String key, String value) {
        Properties properties = getProperties(activity, fileName);
        properties.setProperty(key, value);
        BufferedWriter outputStream;
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "properties");
            if (!file.exists()) {
                file.mkdir();
            }
            outputStream = new BufferedWriter(new FileWriter(new File(file, "data.properties"), true));
            StringBuilder sb = new StringBuilder();
            sb.append(key).append("=").append(value);
            outputStream.write(sb.toString(), 0, sb.length());
            outputStream.flush();
            properties.store(outputStream, "");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
