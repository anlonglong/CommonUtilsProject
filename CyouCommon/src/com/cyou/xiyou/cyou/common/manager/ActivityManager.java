package com.cyou.xiyou.cyou.common.manager;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ActivityManager
{
    private static final Stack<Activity> activityStack = new Stack<>();

    /**
     * 添加Activity到堆栈
     */
    public synchronized static void addActivity(Activity activity)
    {
        synchronized(activityStack)
        {
            activityStack.add(activity);
        }
    }

    public synchronized static void removeActivity(Activity activity)
    {
        if(activity != null)
        {
            synchronized(activityStack)
            {
                activityStack.remove(activity);
            }
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public synchronized static Activity getCurrentActivity()
    {
        synchronized(activityStack)
        {
            Activity activity = null;

            while(!activityStack.isEmpty())
            {
                activity = activityStack.lastElement();

                if(activity.isFinishing() || activity.isDestroyed())
                {
                    activityStack.pop();
                    activity = null;
                }
                else
                {
                    break;
                }
            }

            return activity;
        }
    }

    /**
     * 获取指定的Activity
     */
    public synchronized static Activity getActivity(Class<?> clazz)
    {
        Activity result = null;

        synchronized(activityStack)
        {
            for(Activity activity: activityStack)
            {
                if(activity != null && activity.getClass() == clazz)
                {
                    result = activity;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public synchronized static void finishCurrentActivity()
    {
        Activity activity;

        synchronized(activityStack)
        {
            activity = activityStack.lastElement();
        }

        if(activity != null)
        {
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public synchronized static void finishActivity(Activity activity)
    {
        if(activity != null)
        {
            synchronized(activityStack)
            {
                activityStack.remove(activity);
            }

            activity.finish();
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public synchronized static void finishActivity(Class<?> clazz)
    {
        List<Activity> activities = new ArrayList<>();

        synchronized(activityStack)
        {
            for(Activity activity: activityStack)
            {
                if(activity != null && activity.getClass() == clazz)
                {
                    activities.add(activity);
                }
            }
        }

        for(Activity activity: activities)
        {
            finishActivity(activity);
        }

        activities.clear();
    }

    /**
     * 结束所有Activity
     */
    public synchronized static void finishAllActivity()
    {
        synchronized(activityStack)
        {
            for(Activity activity: activityStack)
            {
                if(activity != null)
                {
                    activity.finish();
                }
            }

            activityStack.clear();
        }
    }

    /**
     * 结束除指定Activity外的其他所有Activity
     * @param activity 不需要结束的Activity
     */
    public synchronized static void finishAllActivityExcept(Activity activity)
    {
        synchronized(activityStack)
        {
            for(Activity mActivity: activityStack)
            {
                if(mActivity != activity && mActivity != null)
                {
                    mActivity.finish();
                }
            }

            activityStack.clear();

            if(activity != null)
            {
                activityStack.add(activity);
            }
        }
    }

    /**
     * 结束除指定Activity外的其他所有Activity
     * @param clazz 不需要结束的Activity
     */
    public synchronized static void finishAllActivityExcept(Class<?> clazz)
    {
        List<Activity> activities = new ArrayList<>();

        synchronized(activityStack)
        {
            for(Activity activity: activityStack)
            {
                if(activity != null && activity.getClass() != clazz)
                {
                    activities.add(activity);
                }
            }

            for(Activity activity: activities)
            {
                finishActivity(activity);
            }

            activities.clear();
        }
    }
}