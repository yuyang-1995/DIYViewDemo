package com.yuy.customer_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Author: yuyang
 * Date:2019/10/26 16:09
 * Description:
 * Version:
 */
public class TextView extends View {

    String mText = "Imooc";
    //
    public TextView(Context context) {
        super(context);
    }


    private Paint mPaint;

    //通常用于在布局文件中去编写， 系统建立此View 会反射调用此构造方法
    //所以如果想要自定义View 在布局文件中使用，一定要去复写两个参数的构造方法
    public TextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();

        //依赖创建的属性资源 创建 TypedArray 对象， 参数一： AttributeSet 参数二： 生命的属性资源文件name
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TestView);

        // 从TypedArray 中获取 各个属性的值
        boolean booleanTest = ta.getBoolean(R.styleable.TestView_test_boolean, false);
        int integerTest = ta.getInteger(R.styleable.TestView_test_integer, -1);
        float dimensionTest = ta.getDimension(R.styleable.TestView_test_dimension, 0);
        int enumTest = ta.getInt(R.styleable.TestView_test_enum, 1);

//         mText = ta.getString(R.styleable.TestView_test_string);

        int count = ta.getIndexCount();

        for (int i = 0; i < count; i++) {

            int index = ta.getIndex(i);

            switch (index) {
                case R.styleable.TestView_test_string:
                    mText = ta.getString(R.styleable.TestView_test_string);
                    break;
            }
        }


        Log.e("TAG", booleanTest + " , "
                + integerTest + " , "
                + dimensionTest + " , " + enumTest + " ," + mText);

        //回收TypedArray
        ta.recycle();
    }

    //画笔初始化
    private void initPaint() {

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);

        mPaint.setColor(0xFFFF0000);
        mPaint.setAntiAlias(true);


    }

    //三个参数的构造方法， 一般是你有一些默认的Style 的属性， 并且通常是在上面两个方法中调用
    public TextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //两个参数是父控件传过来的， 分别代表宽度相关和高度相关
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        //测量控件宽度
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


        //测量控件高度
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

                height = Math.min(height, heightSize);

            }else {

                height = needHeight;
            }
        }

        //
        setMeasuredDimension(width, height);

    }


    @Override
    protected void onDraw(Canvas canvas) {

        //绘制图像
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - mPaint.getStrokeWidth()/2 , mPaint);
//        mPaint.setStrokeWidth(1);
//        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);
//        canvas.drawLine(getWidth()/2, 0, getWidth()/2, getHeight(), mPaint);

        //绘制文字  参数4，参数5 绘制文本坐标 y为文字基线的位置
        mPaint.setTextSize(100);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        canvas.drawText(mText, 0, mText.length(), 0, getHeight(), mPaint);

    }

    private int measureHeight() {
        return 0;
    }


    //按view相关 具体实现
    private int measureWith() {
        return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mText = "8888";
        invalidate(); //重绘
        return true;
    }


    private static final String KET_TEXT = "key_text";
    private static final String INSTANCE = "instance";

    @Override
    protected Parcelable onSaveInstanceState() {

        //使用Bundle 存储
        Bundle bundle = new Bundle();
        //存此控件的数据
        bundle.putString(KET_TEXT, mText);
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
            mText = bundle.getString(KET_TEXT);

            return;
        }

        super.onRestoreInstanceState(state);
    }
}
