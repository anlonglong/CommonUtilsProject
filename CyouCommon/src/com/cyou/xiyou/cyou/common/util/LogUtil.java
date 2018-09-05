package com.cyou.xiyou.cyou.common.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LogUtil
{
    private static boolean logEnabled = false;

    private static DateFormat DATE_FORMAT_SIMPLE = new SimpleDateFormat("yyyyMMdd", Locale.CHINA);

    private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);

    private static String logDir;

    public static void i(String tag, String msg)
    {
        if(logEnabled)
        {
            Log.i(tag, msg);
            saveLog(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable t)
    {
        if(logEnabled)
        {
            Log.i(tag, msg, t);
            saveLog(tag, msg, t);
        }
    }

    public static void d(String tag, String msg)
    {
        if(logEnabled)
        {
            Log.d(tag, msg);
            saveLog(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable t)
    {
        if(logEnabled)
        {
            Log.d(tag, msg, t);
            saveLog(tag, msg, t);
        }
    }

    public static void e(String tag, String msg)
    {
        if(logEnabled)
        {
            Log.e(tag, msg);
            saveLog(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable t)
    {
        if(logEnabled)
        {
            Log.e(tag, msg, t);
            saveLog(tag, msg, t);
        }
    }

    public static void v(String tag, String msg)
    {
        if(logEnabled)
        {
            Log.v(tag, msg);
            saveLog(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable t)
    {
        if(logEnabled)
        {
            Log.v(tag, msg, t);
            saveLog(tag, msg, t);
        }
    }

    public static void setLogEnabled(boolean enabled)
    {
        logEnabled = enabled;
    }

    public static void setLogDir(String dir)
    {
        logDir = dir;

        if(logEnabled)
        {
            Log.i("LogDir", dir);
        }
    }

    private static void saveLog(String tag, String msg)
    {
        saveLog(tag, msg, null);
    }

    private synchronized static void saveLog(String tag, String msg, Throwable t)
    {
        if(!TextUtils.isEmpty(logDir))
        {
            Date date = new Date();
            String logFileName = DATE_FORMAT_SIMPLE.format(date) + ".log";
            File dirFile = new File(logDir);
            File logFile = new File(logDir, logFileName);

            if(!dirFile.exists() || !dirFile.isDirectory())
            {
                StorageUtil.deleteFile(dirFile);
                StorageUtil.mkdirs(dirFile);
            }

            if(!logFile.exists() || !logFile.isFile())
            {
                StorageUtil.deleteFile(logFile);
            }

            StringBuilder log = new StringBuilder();
            log.append(DATE_FORMAT.format(date));
            log.append(' ');
            log.append(tag);
            log.append(": ");
            log.append(msg);
            log.append('\n');

            if(t != null)
            {
                log.append(t.getMessage());
                log.append('\n');
            }

            try
            {
                IOUtil.writeToFile(logFile, log.toString(), null, true);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
