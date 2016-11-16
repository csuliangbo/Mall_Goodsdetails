package com.example.itcast.customtest;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

/**
 * LQJ on 2016/11/6 20:21
 * 15058152353@163.com
 */
public class FakeBottomViewPager extends ViewGroup{
    //设置拖拽工具类
    private ViewDragHelper viewDragHelper;
//    private GestureDetectorCompat gestureDetector;
    //上面一个
    private TextView topChild;
    //下面一个ViewPager
    private View bottomChild;

    private int topChildHeight;
    private int topChildWidth;
    private int bottomChildHeight;
    private int bottomChildWidth;

   // ListView listview;
    public FakeBottomViewPager(Context context) {
        super(context);
        init(context);
    }

    public FakeBottomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        topChild = new TextView(context);
        topChild.setBackgroundColor(Color.GRAY);
        topChild.setText("上拉跳至商品详情");
        //漏了这行代码会报空指针
        addView(topChild);
        init(context);
    }

    public FakeBottomViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    //在onMeasure之前执行
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
 //       topChild = (TextView) getChildAt(0);
        bottomChild =findViewById(R.id.bottomChild);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        topChildHeight = topChild.getMeasuredHeight();
        topChildWidth = topChild.getMeasuredWidth();
        bottomChildHeight = bottomChild.getMeasuredHeight();
        bottomChildWidth = bottomChild.getMeasuredWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(topChild,widthMeasureSpec,heightMeasureSpec);
        measureChild(bottomChild,widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        topChild.layout(0,-topChildHeight,bottomChildWidth,0);
        bottomChild.layout(0,0,bottomChildWidth,bottomChildHeight);
    }

    private void init(Context context) {

        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child==bottomChild;
            }

            @Override
            public int getViewVerticalDragRange(View child) {
                return bottomChildHeight;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {

                if (child == topChild) {
                    if (top < -bottomChildHeight) {
                        top = bottomChildHeight;
                    } else if (top > 0) {
                        top = 0;
                    }
                }
                if (child == bottomChild) {
                    if (top > bottomChildHeight) {
                        top = bottomChildHeight;
                    } else if (top < 0) {
                        top = 0;
                    }
                }
                return top;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                    topChild.offsetTopAndBottom(dy);
                    if (Math.abs(top)>30){
                        topChild.setText("释放返回商品详情");
                    }else{
                        topChild.setText("下拉返回商品详情");
                    }
                //    listview.setAdapter(new ListViewAdapter(getContext()));

//                if (changedView == bottomChild) {
//                    topChild.offsetTopAndBottom(dy);
//                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == bottomChild) {
                    if (bottomChild.getTop()>30||yvel>180) {//
                        Log.e("FakeBottomViewPager",yvel+"");
                        close();
                       if (upFreshListner!=null)
                           upFreshListner.upFreshOpen();
                    }else{
                        close();
                    }
                }
//                else if (releasedChild == bottomChild){
//                    if (bottomChild.getTop() > bottomChildHeight/5){
//                        close();
//                    }else{
//                        open();
//                    }
//                }
            }
        });
    }
    private OnUpFreshListner upFreshListner;

    public void setUpFreshListner(OnUpFreshListner upFreshListner) {
        this.upFreshListner = upFreshListner;
    }

    public interface OnUpFreshListner{
      void  upFreshOpen();
    }
    private void close() {
        viewDragHelper.smoothSlideViewTo(bottomChild, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //被借口回调取代，先保留
    private void open() {
        viewDragHelper.smoothSlideViewTo(topChild,0,-bottomChildHeight);
        ViewCompat.postInvalidateOnAnimation(this);
   //     listview.setSelection(0);
    }

    @Override
    public void computeScroll() {
        //配合smoothSlideViewTo使用，是固定格式
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
    //    if (ev.getAction()==MotionEvent.ACTION_DOWN){
           if(bottomChild.canScrollVertically(-1)) {
               Log.e("FakeBottomViewPager",bottomChild.canScrollVertically(-1)+"");
               return false;
           }
   //     }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }
    /**
     * 检测控件是否能上拉
     */
    public boolean canChildScrollUp(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                View lastChild = absListView.getChildAt(absListView.getChildCount() - 1);
                if (lastChild != null) {
                    return (absListView.getLastVisiblePosition() == (absListView.getCount() - 1))
                            && lastChild.getBottom() > absListView.getPaddingBottom();
                }
                else
                {
                    return false;
                }
            } else {
                return mTarget.getHeight() - mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }


    /**
     * 检测控件是否能下拉
     */
    public boolean canChildScrollDown(View mTarget) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }
}
