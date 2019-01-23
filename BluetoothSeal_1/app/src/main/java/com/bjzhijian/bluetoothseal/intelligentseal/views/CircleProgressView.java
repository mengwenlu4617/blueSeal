package com.bjzhijian.bluetoothseal.intelligentseal.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.bjzhijian.bluetoothseal.intelligentseal.R;
import com.fastwork.library.mutils.MDensityUtil;

/**
 * Created by lenovo on 2019/1/15.
 * 圆形进度条
 */

public class CircleProgressView extends View {
    // 逐渐透明度
    public static final float[] SWEEP_GRADIENT = new float[]{0f, 0.05f, 1.0f};
    // 逐渐变化的颜色值（数组长度和透明度数组保持一致）
    public static final int[] SWEEP_GRADIENT_COLORS = new int[]{Color.WHITE,
            Color.parseColor("#ff407dc6"), Color.parseColor("#ff407dc6")};
    public static final int[] SWEEP_GRADIENT_COLORS1 = new int[]{Color.parseColor("#407dc6"),
            Color.parseColor("#407dc6")};

    //色带的宽度
    private float mStripeWidth;
    //总体大小
    private int mHeight;
    private int mWidth;

    //绘制圆弧的进度值
    private int mProgress = 0;

    //小圆的颜色
    private int mBackground;
    //大圆颜色
    private int mFrontColor;
    private int mTextColor;

    //中心百分比文字大小
    private float mTextSize;

    private Paint bgpaint;
    private Paint frontpaint;
    private Paint textpaint;

    private float x, y, radius;

    private SweepGradient mColorShard, mColorShard1;
    private RectF rect;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        mStripeWidth = a.getDimension(R.styleable.CircleProgressView_stripe_width, MDensityUtil.dip2px(context, 10));
        mProgress = a.getInteger(R.styleable.CircleProgressView_progress, 25);
        mBackground = a.getColor(R.styleable.CircleProgressView_bgcolor, 0xaa407dc6);
        mTextColor = a.getColor(R.styleable.CircleProgressView_text_color, 0xff407dc6);
        mTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressView_text_size, MDensityUtil.dip2px(context, 10));
        a.recycle();

        mColorShard = new SweepGradient(0, 0, SWEEP_GRADIENT_COLORS, SWEEP_GRADIENT);
        mColorShard1 = new SweepGradient(0, 0, SWEEP_GRADIENT_COLORS1, null);

        bgpaint = new Paint();
        bgpaint.setAntiAlias(true);
        bgpaint.setColor(mBackground);
        bgpaint.setStrokeWidth(6);
        bgpaint.setStyle(Paint.Style.STROKE);

        frontpaint = new Paint();
        frontpaint.setAntiAlias(true);
        frontpaint.setShader(mColorShard);
        frontpaint.setStrokeWidth(mStripeWidth);
        frontpaint.setStyle(Paint.Style.STROKE);
        frontpaint.setDither(true);
        frontpaint.setStrokeCap(Paint.Cap.ROUND);

        textpaint = new Paint();
        textpaint.setAntiAlias(true);
        textpaint.setColor(mTextColor);
        textpaint.setTextSize(mTextSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        x = mWidth / 2;
        y = mHeight / 2;
        rect = new RectF();
        radius = (mWidth - mStripeWidth) / 5 * 2;
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, bgpaint);

        rect.set(mStripeWidth / 2, mStripeWidth / 2, mWidth - mStripeWidth / 2, mHeight - mStripeWidth / 2);
        if (mProgress > 50) {
            frontpaint.setShader(mColorShard);
            canvas.drawArc(rect, -90, 180, false, frontpaint);
            frontpaint.setShader(mColorShard1);
            canvas.drawArc(rect, 90, 3.6f * (mProgress - 50), false, frontpaint);
        } else {
            frontpaint.setShader(mColorShard);
            canvas.drawArc(rect, -90, 3.6f * mProgress, false, frontpaint);
        }
        String text = mProgress + "%";
        textpaint.setTextSize(mTextSize);
        float textLength = textpaint.measureText(text);
        textpaint.setColor(mTextColor);
        float textx = x - textLength / 2;
        Paint.FontMetricsInt fontMetrics = textpaint.getFontMetricsInt();
        float texty = (mHeight - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(text, textx, texty, textpaint);
    }

    //外部设置百分比数
    public void setProgress(int percent) {
        if (percent > 100) {
            throw new IllegalArgumentException("percent must less than 100!");
        }
        this.mProgress = percent;
        this.invalidate();
    }

    public void setTextColor(int textColor) {
        this.mTextColor = textColor;
    }

    public void setFrontColor(int textColor) {
        this.mFrontColor = textColor;
        frontpaint.setColor(mFrontColor);
    }
}
