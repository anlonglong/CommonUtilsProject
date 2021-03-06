package com.cyou.xiyou.cyou.common.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.cyou.xiyou.cyou.common.R;

/**
 * Created by 003 on 2017-05-24.
 */
public class RoundRectRelativeLayout extends TouchableRelativeLayout
{
    private Paint paint;

    private Path path;

    private RectF rect;

    private float cornerRadius;

    public RoundRectRelativeLayout(Context context)
    {
        super(context);
        init(null);
    }

    public RoundRectRelativeLayout(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs)
    {
        if(attrs != null)
        {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.RoundRectRelativeLayout);

            try
            {
                this.cornerRadius = ta.getDimension(R.styleable.RoundRectRelativeLayout_corner_radius, 0);
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

        this.rect = new RectF();
        this.path = new Path();
        this.paint = new Paint();
        path.setFillType(FillType.INVERSE_WINDING);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.setWillNotDraw(false);
        this.setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    private void resetPath()
    {
        rect.set(0, 0, this.getWidth(), this.getHeight());
        path.reset();
        path.addRoundRect(rect, cornerRadius, cornerRadius, Direction.CW);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        resetPath();
    }

    @Override
    public void draw(Canvas canvas)
    {
        int saveLayerCount = 0;
        int width = this.getWidth();
        int height = this.getHeight();

        if(cornerRadius > 0 && width > 0 && height > 0)
        {
            saveLayerCount = canvas.saveLayerAlpha(0, 0, width, height, 255, Canvas.ALL_SAVE_FLAG);
        }

        super.draw(canvas);

        if(saveLayerCount != 0)
        {
            canvas.drawPath(path, paint);
            canvas.restoreToCount(saveLayerCount);
        }
    }

    public float getCornerRadius()
    {
        return this.cornerRadius;
    }

    public void setCornerRadius(float cornerRadius)
    {
        this.cornerRadius = cornerRadius;
        resetPath();
        this.invalidate();
    }
}