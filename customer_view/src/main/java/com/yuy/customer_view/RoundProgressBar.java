package com.yuy.customer_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Author: yuyang
 * Date:2019/10/26 22:09
 * Description:
 * Version:
 */
public class RoundProgressBar extends View {

    //属性
    private int mColor;
    private int mRadius;
    private int mLineWidth;
    private int mTextSize;
    private int mProgress = 30;

    //画笔
    private Paint mPaint;

    //RectF 和Rect 的区别是 RectF 参数为 浮点型
    private RectF progressRect;

    public RoundProgressBar(Context context) {
        super(context);
  }

    //初始化画笔
    private void initPaint() {

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);

    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        //获取属性
        mRadius = (int) ta.getDimension(R.styleable.RoundProgressBar_radius, dp2px(30));
        mColor = ta.getInt(R.styleable.RoundProgressBar_color, 0xFFFF0000);
        mLineWidth = (int) ta.getDimension(R.styleable.RoundProgressBar_line_width, dp2px(3));
        mTextSize = (int) ta.getDimension(R.styleable.RoundProgressBar_text_size, dp2px(16));
        mProgress = ta.getInt(R.styleable.RoundProgressBar_pg, 30);

        ta.recycle();

        initPaint();

    }

    private float dp2px(int dpval) {

        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpval, getResources().getDisplayMetrics());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //父控件的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        //父控件的测量宽度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);


        int width = 0; //测量宽度

        if (widthMode == MeasureSpec.EXACTLY) {
            //用户确定了大小
            width = widthSize;
        }else{
            //用户没有确认大小

            //控件自己测量自己所需要的宽度
            int needWith = measureWith() + getPaddingLeft() + getPaddingRight();

            if (widthMode == MeasureSpec.AT_MOST) {
                //宽度不超过父控件的剩余宽度
                width = Math.min(needWith, widthSize);

            }else { //MeasureSpec.UNSPECIFIED
                //满足需求值
                width = needWith;
            }
        }


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int height = 0;

        if (heightMode == MeasureSpec.EXACTLY) {
            //用户设置精确值
            height = heightSize;

        }else {
            //自己测量自己
            int needHeight = measureHeight() + getPaddingTop() + getPaddingBottom();

            if (heightMode == MeasureSpec.AT_MOST) {

                height = Math.min(needHeight, heightSize);

            }else {

                height = needHeight;
            }
        }

        width = Math.min(width, height);
        //
        setMeasuredDimension(width, width);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

       progressRect =  new RectF(0,0,w-getPaddingLeft() *2, h - getPaddingLeft()*2);
    }


    //onDraw 不要做创建对象和复杂操作
    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mLineWidth * 1.0f / 4);

        int width = getWidth();
        int height = getHeight();

        //绘制第一个圆
        canvas.drawCircle(width / 2, height / 2,
                width / 2 - getPaddingLeft() - mPaint.getStrokeWidth() / 2, mPaint);


        //绘制圆弧
        mPaint.setStrokeWidth(mLineWidth);
        canvas.save();

        //移到绘制坐标原点到此view的 做上表
        canvas.translate(getPaddingLeft(), getPaddingRight());

        //参数1： 矩形； 参数2： 起点； 参数3； 绘制弧度大小；  参数4 是否使用 中心
        float angel = mProgress * 1.0f / 100 * 360;
        canvas.drawArc(progressRect, 0, angel, false,mPaint);

        canvas.restore();

        String text = mProgress + "%";

        mPaint.setStrokeWidth(0);
        mPaint.setTextSize(mTextSize);

        //设置画笔在此view 的对齐方式
        mPaint.setTextAlign(Paint.Align.CENTER);
        int y = getHeight() / 2;

        //构建文字矩形范围
        Rect bound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bound);

        //获取文字的高度  注意中文y 中居中需要考虑descent 值 mPaint.descent()
        int textHeight = (int) (bound.height());
        canvas.drawText(text, 0, text.length(), getWidth() / 2 , y + textHeight/2, mPaint);

        mPaint.setStrokeWidth(0);
    }

    //提供外部注参
    public void setProgress(int progress) {
        mProgress = progress;
        invalidate();
    }

    public int getProgress(){
        return mProgress;
    }

    private static final String KEY_PROGRESS = "key_progress";
    private static final String INSTANCE = "instance";

    @Override
    protected Parcelable onSaveInstanceState() {

        //使用Bundle 存储
        Bundle bundle = new Bundle();
        //存此控件的数据
        bundle.putInt(KEY_PROGRESS, mProgress);
        //把父View 需要存的数据保存
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        return bundle;
    }


    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;

            //恢复父控件保存的信息
            Parcelable parcelable = bundle.getParcelable(INSTANCE);
            super.onRestoreInstanceState(parcelable);

            //此控件存储的信息
            mProgress = bundle.getInt(KEY_PROGRESS);

            return;
        }

        super.onRestoreInstanceState(state);
    }

    //
    private int measureHeight() {
        return mRadius * 2;
    }

    //
    private int measureWith() {
        return mRadius * 2;
    }

}
