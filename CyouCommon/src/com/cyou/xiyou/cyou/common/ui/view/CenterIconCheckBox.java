package com.cyou.xiyou.cyou.common.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

/**
 * 图标居中的CheckBox，不可设置文字
 */
public class CenterIconCheckBox extends AppCompatCheckBox
{
    private Drawable buttonDrawable;

    private ColorFilter greyFilter;

    public CenterIconCheckBox(Context context)
    {
        super(context);
        init();
    }

    public CenterIconCheckBox(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    private void init()
    {
        this.greyFilter = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public void setButtonDrawable(Drawable buttonDrawable)
    {
        this.buttonDrawable = buttonDrawable;
        super.setButtonDrawable(buttonDrawable);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Drawable background = this.getBackground();

        if(background != null)
        {
            background.draw(canvas);
        }

        if(buttonDrawable != null)
        {
            int left = this.getPaddingLeft();
            int right = this.getWidth() - this.getPaddingRight();
            int top = this.getPaddingTop();
            int bottom = this.getHeight() - this.getPaddingBottom();
            int width = right - left;
            int height = bottom - top;

            if(width > 0 && height > 0)
            {
                int iconWidth = buttonDrawable.getIntrinsicWidth();
                int iconHeight = buttonDrawable.getIntrinsicHeight();
                int iconLeft = left + (width - iconWidth) / 2;
                int iconTop = top + (height - iconHeight) / 2;
                int iconRight = iconLeft + iconWidth;
                int iconBottom = iconTop + iconHeight;
                buttonDrawable.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                buttonDrawable.setColorFilter(isPressed() && isEnabled()? greyFilter: null);
                buttonDrawable.draw(canvas);
            }
        }
    }
}