package com.example.itcast.customtest;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * LQJ on 2016/11/6 19:16
 * 15058152353@163.com
 */
public class FakeRightViewPager extends ViewGroup {
    private ViewPager leftViewPager;
    private TextView rightView;
    private int vpWidth;
    private int vpHeight;
    private int rightViewWidth;
    private int rightViewHeight;
    private ViewDragHelper viewDragHelper;

    public FakeRightViewPager(Context context) {
        super(context);
    }

    public FakeRightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化拖拽类
        initDragHelper();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        rightView= (TextView) getChildAt(0);
        leftViewPager= (ViewPager) getChildAt(1);
    //    rightView.setText("继续拉出");
        rightView.setBackgroundColor(Color.GRAY);
    }

    private void initDragHelper() {
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper
                .Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == leftViewPager;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return rightViewWidth;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int
                    dx) {
           //     int finalLeft=left;
                if (left < Math.max(-rightViewWidth,-100)) {
                    left =Math.max(-rightViewWidth,-100);
                } else if (left > 0) {
                    left = 0;
                }
                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int
                    top, int dx, int dy) {
                rightView.offsetLeftAndRight(dx);
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float
                    yvel) {
                if (releasedChild.getLeft() < -rightViewWidth/16||xvel<-5) {
                    close();
                    //再开启动画
                    open();
                } else {
                    //先关闭复位
                    close();

                }
            }
        });
    }

    private void open() {
        if (mListener != null){
            mListener.open();
        }
    }

    private void close() {
        viewDragHelper.smoothSlideViewTo(leftViewPager,0,0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    public FakeRightViewPager(Context context, AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (leftViewPager.canScrollHorizontally(1)){
        //    leftViewPager.canScrollHorizontally(View.SCROLL_INDICATOR_RIGHT);
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return  true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        leftViewPager.measure(widthMeasureSpec,heightMeasureSpec);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(240,MeasureSpec.EXACTLY);
        rightView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vpWidth = leftViewPager.getMeasuredWidth();
        vpHeight = leftViewPager.getMeasuredHeight();
        rightViewWidth = rightView.getMeasuredWidth();
        rightViewHeight = rightView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        leftViewPager.layout(0,0,vpWidth,vpHeight);
        rightView.layout(vpWidth,0,vpWidth+rightViewWidth,rightViewHeight);
    }

    public void setAdapter(PagerAdapter adapter){
        leftViewPager.setAdapter(adapter);
    }

    public void resetRightViewPosition(){
        close();
    }

    private OpenListener mListener;
    public void setOnOpenListener(OpenListener Listener){
        mListener = Listener;
    }
    public interface OpenListener{
        void open();
    }
}
