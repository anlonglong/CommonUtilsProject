package com.cyou.xiyou.cyou.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cyou.xiyou.cyou.common.manager.DBManager;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.enums.AssignType;

import java.io.Serializable;

/**
 * 使用数据库存储键值对。默认使用用户数据库，若需要使用全局数据库，请使用名称中带有“Global”的方法
 */
public class ConfigUtil
{
    private static String getString(Context context, ConfigKey key, boolean global)
    {
        LiteOrm liteOrm = DBManager.getInstance(context).getLiteOrm(global);
        Config config = liteOrm.queryById(key.getName(), Config.class);
        return config == null? null: config.getValue();
    }

    public static String getString(Context context, ConfigKey key)
    {
        return getString(context, key, false);
    }

    public static String getGlobalString(Context context, ConfigKey key)
    {
        return getString(context, key, true);
    }

    private static void putString(Context context, ConfigKey key, String value, boolean global)
    {
        if(value == null)
        {
            remove(context, key, global);
        }
        else
        {
            LiteOrm liteOrm = DBManager.getInstance(context).getLiteOrm(global);
            Config config = new Config();
            config.setKey(key.getName());
            config.setValue(value);
            liteOrm.save(config);
        }
    }

    public static void putString(Context context, ConfigKey key, String value)
    {
        putString(context, key, value, false);
    }

    public static void putGlobalString(Context context, ConfigKey key, String value)
    {
        putString(context, key, value, true);
    }

    private static boolean getBoolean(Context context, ConfigKey key, boolean defaultValue, boolean global)
    {
        String strValue = getString(context, key, global);
        return  (strValue == null || strValue.isEmpty())? defaultValue: Boolean.parseBoolean(strValue);
    }

    public static boolean getBoolean(Context context, ConfigKey key, boolean defaultValue)
    {
        return getBoolean(context, key, defaultValue, false);
    }

    public static boolean getGlobalBoolean(Context context, ConfigKey key, boolean defaultValue)
    {
        return getBoolean(context, key, defaultValue, true);
    }

    public static void putBoolean(Context context, ConfigKey key, boolean value)
    {
        putString(context, key, String.valueOf(value));
    }

    public static void putGlobalBoolean(Context context, ConfigKey key, boolean value)
    {
        putGlobalString(context, key, String.valueOf(value));
    }

    private static int getInteger(Context context, ConfigKey key, int defaultValue, boolean global)
    {
        String strValue = getString(context, key, global);
        return  (strValue == null || strValue.isEmpty())? defaultValue: CommonUtil.parseInt(strValue);
    }

    public static int getInteger(Context context, ConfigKey key, int defaultValue)
    {
        return getInteger(context, key, defaultValue, false);
    }

    public static int getGlobalInteger(Context context, ConfigKey key, int defaultValue)
    {
        return getInteger(context, key, defaultValue, true);
    }

    public static void putInteger(Context context, ConfigKey key, int value)
    {
        putString(context, key, String.valueOf(value));
    }

    public static void putGlobalInteger(Context context, ConfigKey key, int value)
    {
        putGlobalString(context, key, String.valueOf(value));
    }

    private static long getLong(Context context, ConfigKey key, long defaultValue, boolean global)
    {
        String strValue = getString(context, key, global);
        return  (strValue == null || strValue.isEmpty())? defaultValue: CommonUtil.parseLong(strValue);
    }

    public static long getLong(Context context, ConfigKey key, long defaultValue)
    {
        return getLong(context, key, defaultValue, false);
    }

    public static long getGlobalLong(Context context, ConfigKey key, long defaultValue)
    {
        return getLong(context, key, defaultValue, true);
    }

    public static void putLong(Context context, ConfigKey key, long value)
    {
        putString(context, key, String.valueOf(value));
    }

    public static void putGlobalLong(Context context, ConfigKey key, long value)
    {
        putGlobalString(context, key, String.valueOf(value));
    }

    private static void remove(Context context, ConfigKey key, boolean global)
    {
        LiteOrm liteOrm = DBManager.getInstance(context).getLiteOrm(global);
        liteOrm.delete(WhereBuilder.create(Config.class).equals("key", key.getName()));
    }

    public static void remove(Context context, ConfigKey key)
    {
        remove(context, key, false);
    }

    public static void removeGlobal(Context context, ConfigKey key)
    {
        remove(context, key, true);
    }

    public static String getUserId(Context context)
    {
        return getSPString(context, DefaultConfigKey.UserId);
    }

    public static void setUserId(Context context, String userId)
    {
        putSPString(context, DefaultConfigKey.UserId, userId);
    }

    private static String getSPString(Context context, ConfigKey key)
    {
        return getSharedPreferences(context).getString(key.getName(), null);
    }

    private static void putSPString(Context context, ConfigKey key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();

        if(value == null)
        {
            editor.remove(key.getName());
        }
        else
        {
            editor.putString(key.getName(), value);
        }

        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(Context context)
    {
        return context.getSharedPreferences("Cyou", Context.MODE_PRIVATE);
    }

    @Table("Config")
    public static class Config implements Serializable
    {
        private static final long serialVersionUID = -3788656149687135918L;

        @PrimaryKey(AssignType.BY_MYSELF)
        private String key;

        private String value;

        public String getKey()
        {
            return this.key;
        }

        public void setKey(String key)
        {
            this.key = key;
        }

        public String getValue()
        {
            return this.value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }
    }

    public interface ConfigKey
    {
        String getName();
    }

    public enum DefaultConfigKey implements ConfigKey
    {
        DeviceId, UserId, UserToken, VersionCode, IsMIUI;

        @Override
        public String getName()
        {
            return this.name();
        }
    }
}