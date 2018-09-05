package com.cyou.xiyou.cyou.common.ui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;

import com.cyou.xiyou.cyou.common.manager.ActivityManager;
import com.cyou.xiyou.cyou.common.util.CommonUtil;

import org.greenrobot.eventbus.EventBus;

public abstract class CommonActivity extends AppCompatActivity
{
    private Handler handler;

    private InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActivityManager.addActivity(this);

        if(enableEventBus())
        {
            EventBus.getDefault().register(this);
        }

        init(savedInstanceState);
    }

    protected abstract int getContentViewResId();

    protected abstract void init(Bundle savedInstanceState);

    @Override
    protected void onDestroy()
    {
        if(handler != null)
        {
            handler.removeCallbacksAndMessages(null);
        }

        if(enableEventBus())
        {
            EventBus.getDefault().unregister(this);
        }

        ActivityManager.removeActivity(this);
        super.onDestroy();
    }

    protected boolean enableEventBus()
    {
        return false;
    }

    protected void onContentViewClick(View view)
    {
        showOrHideKeyboard(false);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        super.setContentView(layoutResID);
        resetStatusBar();
        CommonUtil.getContentView(this).setOnClickListener(new OnClickListener()
        {
            public void onClick(View view)
            {
                onContentViewClick(view);
            }
        });
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        resetStatusBar();
    }

    protected Integer getStatusBarColor()
    {
        return null;
    }

    private void resetStatusBar()
    {
        Integer statusBarColor = this.getStatusBarColor();

        if(statusBarColor != null)
        {
            if(statusBarColor == 0)
            {
                CommonUtil.hideStatusBarIfSupported(this);
            }
            else
            {
                CommonUtil.setStatusBarColorIfSupported(this, statusBarColor);
            }
        }
    }

    protected final Handler getHandler()
    {
        if(handler == null)
        {
            handler = new Handler(getMainLooper())
            {
                public void handleMessage(Message msg)
                {
                    CommonActivity.this.handleMessage(msg);
                }
            };
        }

        return handler;
    }

    protected void handleMessage(Message msg)
    {}

    protected final void sendEmptyMessage(int what)
    {
        getHandler().sendEmptyMessage(what);
    }

    protected final void sendEmptyMessageDelayed(int what, long delayMillis)
    {
        getHandler().sendEmptyMessageDelayed(what, delayMillis);
    }

    protected final void sendMessage(Message msg)
    {
        getHandler().sendMessage(msg);
    }

    protected final void sendMessageDelayed(Message msg, long delayMillis)
    {
        getHandler().sendMessageDelayed(msg, delayMillis);
    }

    protected void showOrHideKeyboard(boolean show)
    {
        if(inputMethodManager == null)
        {
            inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        }

        View focusView = this.getCurrentFocus();

        if(show)
        {
            if(focusView == null)
            {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
            else
            {
                inputMethodManager.showSoftInput(focusView, 0);
            }
        }
        else if(focusView != null)
        {
            inputMethodManager.hideSoftInputFromWindow(focusView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 延时弹出键盘
     */
    protected void showKeyboardDelayed(final View focusView)
    {
        if(focusView != null)
        {
            focusView.requestFocus();
        }

        getHandler().postDelayed(new Runnable()
        {
            public void run()
            {
                if(focusView == null || focusView.isFocused())
                {
                    showOrHideKeyboard(true);
                }
            }
        }, 200);
    }

    public boolean isFinishingOrDestroyed()
    {
        return this.isFinishing() || this.isDestroyed();
    }
}