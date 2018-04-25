package app.witkey.live.utils.animations.giftbullet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

import app.witkey.live.R;

public class StrokeTextView extends TextView {

    TextPaint m_TextPaint;
    int mInnerColor;
    int mOuterColor;

    public StrokeTextView(Context context, int outerColor, int innnerColor) {
        super(context);
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;

    }

    public StrokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        m_TextPaint = this.getPaint();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        this.mInnerColor = a.getColor(R.styleable.StrokeTextView_innnerColor,0xffffff);
        this.mOuterColor = a.getColor(R.styleable.StrokeTextView_outerColor,0xffffff);

    }

    public StrokeTextView(Context context, AttributeSet attrs, int defStyle, int outerColor, int innnerColor) {
        super(context, attrs, defStyle);
        m_TextPaint = this.getPaint();
        this.mInnerColor = innnerColor;
        this.mOuterColor = outerColor;

    }

    private boolean m_bDrawSideLine = true;

    /**
     *
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (m_bDrawSideLine) {
            setTextColorUseReflection(mOuterColor);
            m_TextPaint.setStrokeWidth(5);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            super.onDraw(canvas);

            setTextColorUseReflection(mInnerColor);
            m_TextPaint.setStrokeWidth(0);
            m_TextPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        }
        super.onDraw(canvas);
    }

    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        m_TextPaint.setColor(color);
    }

}
