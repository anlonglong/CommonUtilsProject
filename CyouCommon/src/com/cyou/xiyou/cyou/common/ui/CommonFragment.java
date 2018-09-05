package com.cyou.xiyou.cyou.common.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import org.greenrobot.eventbus.EventBus;

public abstract class CommonFragment extends Fragment
{
    private Handler handler;

    private boolean needRealeaseHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(enableEventBus())
        {
            EventBus.getDefault().register(this);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(getViewResId(), container, false);
        init(savedInstanceState, view);
        return view;
    }

    protected abstract int getViewResId();

    protected abstract void init(@Nullable Bundle savedInstanceState, @NonNull  View view);

    @Override
    public void onDestroy()
    {
        if(needRealeaseHandler && handler != null)
        {
            handler.removeCallbacksAndMessages(null);
        }

        if(enableEventBus())
        {
            EventBus.getDefault().unregister(this);
        }

        super.onDestroy();
    }

    protected boolean enableEventBus()
    {
        return false;
    }

    protected void showOrHideKeyboard(boolean show)
    {
        Activity activity = this.getActivity();

        if(activity == null)
        {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(inputMethodManager == null)
        {
            return;
        }

        View focusView = activity.getCurrentFocus();

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

    protected final Handler getHandler()
    {
        if(handler == null)
        {
            Activity activity = this.getActivity();

            if(activity == null)
            {
                handler = new Handler();
                needRealeaseHandler = true;
            }
            else if(activity instanceof CommonActivity)
            {
                handler = ((CommonActivity)activity).getHandler();
                needRealeaseHandler = false;
            }
            else
            {
                handler = new Handler(activity.getMainLooper());
                needRealeaseHandler = true;
            }
        }

        return handler;
    }
}
