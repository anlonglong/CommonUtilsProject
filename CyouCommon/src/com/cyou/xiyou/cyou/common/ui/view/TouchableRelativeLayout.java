package com.cyou.xiyou.cyou.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.cyou.xiyou.cyou.common.R;

public class TouchableRelativeLayout extends RelativeLayout
{
    private Drawable touchedDrawable, borderDrawable;

    private boolean touching;

    private View[] linkageViews;

    public TouchableRelativeLayout(Context context)
    {
        super(context);
        init(null);
    }

    public TouchableRelativeLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        setWillNotDraw(false);

        if(attrs != null)
        {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TouchableRelativeLayout);

            try
            {
                this.touchedDrawable = ta.getDrawable(R.styleable.TouchableRelativeLayout_touched_drawable);
                this.borderDrawable = ta.getDrawable(R.styleable.TouchableRelativeLayout_border_drawable);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
            finally
            {
                ta.recycle();
            }
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if(width > 0 && height > 0)
        {
            if(borderDrawable != null)
            {
                borderDrawable.setBounds(0, 0, width, height);
                borderDrawable.draw(canvas);
            }

            if(touching && touchedDrawable != null)
            {
                touchedDrawable.setBounds(0, 0, width, height);
                touchedDrawable.draw(canvas);
            }
        }
    }

    @Override
    public void setPressed(boolean pressed)
    {
        super.setPressed(pressed);

        if(this.isClickable() && this.touching != pressed)
        {
            this.touching = pressed;

            if(touchedDrawable != null)
            {
                this.invalidate();
            }
        }

        if(linkageViews != null && linkageViews.length > 0)
        {
            for(View view: linkageViews)
            {
                view.setPressed(pressed);
                view.invalidate();
            }
        }
    }

    public Drawable getTouchedDrawable()
    {
        return this.touchedDrawable;
    }

    public void setTouchedDrawable(Drawable touchedDrawable)
    {
        this.touchedDrawable = touchedDrawable;
    }

    public Drawable getBorderDrawable()
    {
        return this.borderDrawable;
    }

    public void setBorderDrawable(Drawable borderDrawable)
    {
        this.borderDrawable = borderDrawable;
    }

    public View[] getLinkageViews()
    {
        return this.linkageViews;
    }

    public void setLinkageViews(View...linkageViews)
    {
        this.linkageViews = linkageViews;
    }
}