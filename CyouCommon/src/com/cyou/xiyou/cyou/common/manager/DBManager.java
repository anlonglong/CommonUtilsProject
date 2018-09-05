package com.cyou.xiyou.cyou.common.manager;

import android.content.Context;
import android.text.TextUtils;

import com.cyou.xiyou.cyou.common.util.ConfigUtil;
import com.litesuits.orm.LiteOrm;

public class DBManager
{
    private static DBManager instance;

    private Context context;

    private LiteOrm userLiteOrm, globalLiteOrm;

    private String dbName = "default";

    public synchronized static DBManager getInstance(Context context)
    {
        if(instance == null)
        {
            instance = new DBManager(context.getApplicationContext());
        }

        return instance;
    }

    private DBManager(Context context)
    {
        this.context = context;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public LiteOrm getLiteOrm(boolean global)
    {

        LiteOrm liteOrm;
        String userId = ConfigUtil.getUserId(context);

        if(TextUtils.isEmpty(userId))
        {
            global = true;
        }

        if(global)
        {
            if(globalLiteOrm == null)
            {
                globalLiteOrm = LiteOrm.newCascadeInstance(context, dbName);
            }

            liteOrm = globalLiteOrm;
        }
        else
        {
            if(userLiteOrm == null)
            {
                userLiteOrm = LiteOrm.newCascadeInstance(context, "id_" + userId + ".db");
            }

            liteOrm = userLiteOrm;
        }

        return liteOrm;
    }

    public void close()
    {
        if(userLiteOrm != null)
        {
            userLiteOrm.close();
        }

        if(globalLiteOrm != null)
        {
            globalLiteOrm.close();
        }

        userLiteOrm = null;
        globalLiteOrm = null;
        instance = null;
    }
}