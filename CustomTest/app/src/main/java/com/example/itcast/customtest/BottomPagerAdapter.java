package com.example.itcast.customtest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * LQJ on 2016/11/6 20:21
 * 15058152353@163.com
 */
public class BottomPagerAdapter extends PagerAdapter implements FakeBottomViewPager.OnUpFreshListner, SuperSwipeRefreshLayout.OnPullRefreshListener {
    private FakeBottomViewPager fakebottomPager;
    private SuperSwipeRefreshLayout swipeRefresh;
    private View headerView;
    private TextView tv;
    private FragmentManager fm;
    Context context;
    public static final String beforeRe="下拉返回商品详情";
    public static final String afterRe="释放返回商品详情";
    public BottomPagerAdapter(FragmentManager fm,Context context) {

        this.fm = fm;
        this.context=context;
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//                ImageView imageView = new ImageView(container.getContext());
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                imageView.setImageResource(icons[position]);
//                container.addView(imageView);
        View view=View.inflate(container.getContext(),R.layout.bottom_viewpager_item,null);
    //    fakebottomPager= (FakeBottomViewPager) view.findViewById(R.id.fakebottomPager);
   //     fakebottomPager.setUpFreshListner(this);
        swipeRefresh= (SuperSwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        headerView=View.inflate(context,R.layout.swiperefresh_head_item,null);
        tv= (TextView) headerView.findViewById(R.id.topchild);

        swipeRefresh.setHeaderView(headerView);
   //     tv.setText("hahahahhahaha");
        swipeRefresh.setOnPullRefreshListener(this);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object
            object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void upFreshOpen() {
        if (fm.getBackStackEntryCount()==0) {
            Fragment secondFragment = new ProductDetailFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //设置竖直方向动画 此方法最低使用版本13
            fragmentTransaction.setCustomAnimations(R.animator.slide_fragment_vertical_right_in,
                    R.animator.slide_fragment_vertical_left_out,
                    R.animator.slide_fragment_vertical_left_in,
                    R.animator.slide_fragment_vertical_right_out);
            fragmentTransaction.add(R.id.fragment_place, secondFragment);
            //Fragment压入栈中
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction .commit();
        }else{
            //Fragment弹栈
            fm.popBackStack();
        }
    }

    @Override
    public void onRefresh() {
        if (fm.getBackStackEntryCount()==0) {
            Fragment secondFragment = new ProductDetailFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //设置竖直方向动画 此方法最低使用版本13
            fragmentTransaction.setCustomAnimations(R.animator.slide_fragment_vertical_right_in,
                    R.animator.slide_fragment_vertical_left_out,
                    R.animator.slide_fragment_vertical_left_in,
                    R.animator.slide_fragment_vertical_right_out);
            fragmentTransaction.add(R.id.fragment_place, secondFragment);
            //Fragment压入栈中
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction .commit();
        }else{
            //Fragment弹栈
            fm.popBackStack();
        }
    }

    @Override
    public void onPullDistance(int distance) {
        String threadName=Thread.currentThread().getName();
        Log.e("BottomPagerAdapter",distance+"distance"+threadName);
        if (distance>30){
            tv.setText("释放返回商品详情");
        }else{
           tv.setText("下拉返回商品详情");
       }
    }

    @Override
    public void onPullEnable(boolean enable) {
    //    tv.setText(enable?afterRe:beforeRe);
    }
}
