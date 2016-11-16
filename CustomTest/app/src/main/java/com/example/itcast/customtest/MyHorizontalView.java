package com.example.itcast.customtest;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * LQJ on 2016/11/6 19:16
 * 15058152353@163.com
 */
public class MyHorizontalView extends ViewGroup {
    //左边的自定义ProductDetailView
    private View leftChild;
    //右边的页面demo中的linearLayout
    private View rightChild;
    private int leftChildWidth;
    private int leftChildHeight;
    private int rightChildWidth;
    private int rightChildHeight;
    private ViewDragHelper viewDragHelper;

    public MyHorizontalView(Context context) {
        super(context);
    }

    public MyHorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper
                .Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                //只接管右边界面
                return rightChild == child;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return leftChildWidth;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int
                    dx) {
                //禁止把左边划出来
                if (left < 0) {
                    left = 0;
                } else if (left > leftChildWidth) {//禁止把做界面向左滑出边界外
                    left = leftChildWidth;
                }
                return left;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int
                    top, int dx, int dy) {
                if (changedView == leftChild) {
                    //这个方法一般要配合invalidate
                    rightChild.offsetLeftAndRight(dx);
                }
                if (changedView == rightChild) {
                    leftChild.offsetLeftAndRight(dx);
                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float
                    yvel) {
                int left = rightChild.getLeft();
                if (left > leftChildWidth / 5) {
                    close();
                } else {
                    open();
                }
            }
        });
    }

    private void close() {
        viewDragHelper.smoothSlideViewTo(leftChild,0,0);
        invalidate();
    }

    private void open() {
        viewDragHelper.smoothSlideViewTo(leftChild,-leftChildWidth,0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (viewDragHelper.continueSettling(true)){
            invalidate();
        }
    }
    float startX;
    float dx;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        boolean leftFling= ((ViewGroup)rightChild).getChildAt(0).canScrollHorizontally(View.SCROLL_INDICATOR_LEFT);
//        boolean rightFling=((ViewGroup)rightChild).getChildAt(0).canScrollHorizontally(View.SCROLL_INDICATOR_RIGHT);
//        if (rightFling){
//            return false;
//        }
   //     ViewPager viewPager = (ViewPager) rightChild.findViewById(R.id.right_viewPager);

        //如果是第一页并且向右滑动
//        if (viewPager.canScrollHorizontally(0)){
//            return false;
//        }
         return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        boolean leftFling= ((ViewGroup)rightChild).getChildAt(0).canScrollHorizontally(View.SCROLL_INDICATOR_LEFT);
//            boolean rightFling=((ViewGroup)rightChild).getChildAt(0).canScrollHorizontally(View.SCROLL_INDICATOR_RIGHT);
//        if (rightFling){
            viewDragHelper.processTouchEvent(event);
//        }
        return true;
    }

    public MyHorizontalView(Context context, AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        leftChild = getChildAt(0);
        rightChild = getChildAt(1);

        final FakeRightViewPager fakeVP = (FakeRightViewPager) leftChild.findViewById(R.id.vp);
        fakeVP.setOnOpenListener(new FakeRightViewPager.OpenListener() {
            @Override
            public void open() {
                MyHorizontalView.this.open();
                fakeVP.resetRightViewPosition();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(leftChild, widthMeasureSpec, heightMeasureSpec);
        measureChild(rightChild,widthMeasureSpec,heightMeasureSpec);
    }
    //在onmeasure方法执行之后多次执行
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        leftChildWidth = leftChild.getMeasuredWidth();
        leftChildHeight = leftChild.getMeasuredHeight();
        rightChildWidth = rightChild.getMeasuredWidth();
        rightChildHeight = rightChild.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        leftChild.layout(0,0,leftChildWidth,leftChildHeight);
        rightChild.layout(leftChildWidth,0,leftChildWidth+rightChildWidth,rightChildHeight);
    }
}
