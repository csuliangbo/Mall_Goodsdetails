package com.example.itcast.customtest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * LQJ on 2016/11/6 20:21
 * 15058152353@163.com
 */
public class BottomFragment extends Fragment  {
    private View view;
    private ViewPager bottomPager;
    private FragmentManager fm;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fm=getFragmentManager();
        context=getActivity();
        view=inflater.inflate(R.layout.fragment_layout_down, container, false);
        bottomPager= (ViewPager) view.findViewById(R.id.bottomPager);
        bottomPager.setAdapter(new BottomPagerAdapter(fm,context));

        return view;
    }

//    @Override
//    public void onRefresh() {
//        if (fm.getBackStackEntryCount()==0) {
//            Fragment secondFragment = new ProductDetailFragment();
//            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//            //设置竖直方向动画 此方法最低使用版本13
//            fragmentTransaction.setCustomAnimations(R.animator.slide_fragment_vertical_right_in,
//                    R.animator.slide_fragment_vertical_left_out,
//                    R.animator.slide_fragment_vertical_left_in,
//                    R.animator.slide_fragment_vertical_right_out);
//            fragmentTransaction.add(R.id.fragment_place, secondFragment);
//            //Fragment压入栈中
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction .commit();
//        }else{
//            //Fragment弹栈
//            fm.popBackStack();
//        }
//    }


}
