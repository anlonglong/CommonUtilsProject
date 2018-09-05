package com.cyou.xiyou.cyou.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.cyou.xiyou.cyou.common.R;

public class TouchableImageButton extends AppCompatImageButton
{
    private boolean touching;

    private boolean ignoreClickable;

    private int overlayColor;

    private ColorFilter defaultColorFilter;

    public TouchableImageButton(Context context)
    {
        super(context);
        init(null);
    }

    public TouchableImageButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        if(attrs != null)
        {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TouchableImageButton);

            try
            {
                this.overlayColor = ta.getColor(R.styleable.TouchableImageButton_overlay_color, Color.LTGRAY);
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

    protected void onPressChanged()
    {
        if(touching)
        {
            this.setColorFilter(overlayColor, PorterDuff.Mode.MULTIPLY);
        }
        else
        {
            this.setColorFilter(defaultColorFilter);
        }
    }

    @Override
    public void setPressed(boolean pressed)
    {
        super.setPressed(pressed);

        if((ignoreClickable || this.isClickable()) && this.touching != pressed)
        {
            this.touching = pressed;
            this.onPressChanged();
        }
    }

    @Override
    public void setClickable(boolean clickable)
    {
        super.setClickable(clickable);
        this.setColorFilter(defaultColorFilter);
    }

    public boolean isIgnoreClickable()
    {
        return this.ignoreClickable;
    }

    public void setIgnoreClickable(boolean ignoreClickable)
    {
        this.ignoreClickable = ignoreClickable;
    }

    public int getOverlayColor()
    {
        return overlayColor;
    }

    public void setOverlayColor(int overlayColor)
    {
        this.overlayColor = overlayColor;
    }

    public ColorFilter getDefaultColorFilter()
    {
        return this.defaultColorFilter;
    }

    public void setDefaultColorFilter(ColorFilter defaultColorFilter)
    {
        this.defaultColorFilter = defaultColorFilter;
    }
}