package com.cyou.xiyou.cyou.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class DeviceCodeCreator
{
    private static final String TAG = DeviceCodeCreator.class.getSimpleName();

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public synchronized static String create(Context context)
    {
        StringBuilder code = new StringBuilder();
        String[] infos = {getDeviceID(context), getAndroidID(context), getSerialNumber()};
        int count = 0;

        for(String info: infos)
        {
            if(info != null)
            {
                code.append(info);
                count++;
            }
        }

        if(count < 3)
        {
            String installtionID = getInstalltionID(context);

            if(installtionID != null)
            {
                code.append(installtionID);
            }
            else
            {
                code.append(UUID.randomUUID().toString());
            }
        }

        String codeStr;

        try
        {
            byte[] bytes = code.toString().getBytes("UTF-8");
            codeStr = UUID.nameUUIDFromBytes(bytes).toString();
        }
        catch(Throwable t)
        {
            codeStr = Digest.computeMD5(code.toString());
        }

        return codeStr;
    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    private static String getDeviceID(Context context)
    {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("HardwareIds")
        String deviceID = tm == null? null: tm.getDeviceId();

        if(deviceID != null)
        {
            deviceID = deviceID.toLowerCase().trim();

            if(isInvalid(deviceID) || deviceID.contains("zeros") || deviceID.contains("asterisks"))
            {
                deviceID = null;
            }
        }

        return deviceID;
    }

    private static String getAndroidID(Context context)
    {
        @SuppressLint("HardwareIds")
        String androidID = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

        if(androidID != null)
        {
            androidID = androidID.toLowerCase().trim();

            if(isInvalid(androidID) || androidID.equals("9774d56d682e549c"))
            {
                androidID = null;
            }
        }

        return androidID;
    }

    private static String getSerialNumber()
    {
        @SuppressLint("HardwareIds")
        String sn = Build.SERIAL;

        if(sn != null)
        {
            sn = sn.toLowerCase().trim();

            if(isInvalid(sn))
            {
                sn = null;
            }
        }

        return sn;
    }

    private static String getInstalltionID(Context context)
    {
        String id = Installation.id(context);

        if(id != null)
        {
            id = id.toLowerCase().trim();

            if(isInvalid(id))
            {
                id = null;
            }
        }

        return id;
    }

    private static boolean isInvalid(String str)
    {
        boolean invalid = str.isEmpty() || str.contains("null") || str.contains("unknown");

        if(!invalid)
        {
            invalid = str.replaceAll("0", "").isEmpty();
        }

        return invalid;
    }

    private static class Installation
    {
        public synchronized static String id(Context context)
        {
            String id = null;
            File installation = new File(context.getFilesDir(), "INSTALLATION");

            try
            {
                if(!installation.exists())
                {
                    writeInstallationFile(installation);
                }

                id = readInstallationFile(installation);
            }
            catch(Throwable t)
            {
                LogUtil.e(TAG, "Get installation id fail.", t);
            }

            return id;
        }

        private static String readInstallationFile(File installation) throws IOException
        {
            RandomAccessFile file = new RandomAccessFile(installation, "r");
            byte[] bytes = new byte[(int)file.length()];
            file.readFully(bytes);
            file.close();
            return new String(bytes);
        }

        private static void writeInstallationFile(File installation) throws IOException
        {
            FileOutputStream out = new FileOutputStream(installation);
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
            out.close();
        }
    }
}