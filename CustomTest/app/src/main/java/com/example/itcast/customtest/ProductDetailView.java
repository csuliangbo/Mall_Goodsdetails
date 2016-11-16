package com.example.itcast.customtest;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

/**
 * LQJ on 2016/11/6 19:16
 * 15058152353@163.com
 */
public class ProductDetailView extends ViewGroup{
    //设置拖拽工具类
    private ViewDragHelper viewDragHelper;
//    private GestureDetectorCompat gestureDetector;

    private View topChild;
    private View bottomChild;
    private int topChildHeight;
    private int topChildWidth;
    private int bottomChildHeight;
    private int bottomChildWidth;

   // ListView listview;
    public ProductDetailView(Context context) {
        super(context);
        init(context);
    }

    public ProductDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public ProductDetailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topChild = getChildAt(0);
        bottomChild = getChildAt(1);
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
        topChild.layout(0,0,topChildWidth,topChildHeight);
        bottomChild.layout(0,topChildHeight,bottomChildWidth,topChildHeight+bottomChildHeight);
    }

    private void init(Context context) {
  //      gestureDetector = new GestureDetectorCompat(context,
  //              new YScrollDetector());
        viewDragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return child == topChild||child==bottomChild;
            }
            @Override
            public int getViewVerticalDragRange(View child) {
                return bottomChildHeight;
            }
            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int finalTop = top;
//                if (child == topChild) {
//                    if (finalTop < -bottomChildHeight) {
//                        finalTop = bottomChildHeight;
//                    } else if (finalTop > 0) {
//                        finalTop = 0;
//                    }
//                }
//                if (child == bottomChild) {
//                    if (finalTop > topChildHeight) {
//                        finalTop = topChildHeight;
//                    } else if (top < 0) {
//                        finalTop = 0;
//                    }
//                }
//                return top;


                if (child == topChild) {
                    // 拖动的时第一个view
                    if (top > 0) {
                        // 不让第一个view往下拖，因为顶部会白板
                        finalTop = 0;
                    }
                } else if (child == bottomChild) {
                    // 拖动的时第二个view
                    if (top < 0) {
                        // 不让第二个view网上拖，因为底部会白板
                        finalTop = 0;
                    }
                }
//			Log.e("retun  top=",child.getTop() + (finalTop - child.getTop()) / 3+"");
                // finalTop代表的是理论上应该拖动到的位置。此处计算拖动的距离除以一个参数(3)，是让滑动的速度变慢。数值越大，滑动的越慢
                return child.getTop() + (finalTop - child.getTop()) / 3;
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                //接管两个child
                if (changedView == topChild) {
                    bottomChild.offsetTopAndBottom(dy);
                //    listview.setAdapter(new ListViewAdapter(getContext()));
                }
//                if (changedView == bottomChild) {
//                    topChild.offsetTopAndBottom(dy);
//                }
                invalidate();
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == topChild) {
                    if (topChild.getTop() < -bottomChildHeight/20||yvel<-10) {
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
        viewDragHelper.smoothSlideViewTo(topChild, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }
    //被借口回调取代
    private void open() {
        viewDragHelper.smoothSlideViewTo(topChild,0,-bottomChildHeight);
        ViewCompat.postInvalidateOnAnimation(this);
   //     listview.setSelection(0);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            if (canChildScrollUp(topChild)){
                return false;
            }
        }
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
