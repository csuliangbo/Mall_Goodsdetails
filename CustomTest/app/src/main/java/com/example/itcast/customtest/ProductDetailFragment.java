package com.example.itcast.customtest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * LQJ on 2016/11/6 20:21
 * 15058152353@163.com
 */
public class ProductDetailFragment extends Fragment implements FakeRightViewPager.OpenListener, ProductDetailView.OnUpFreshListner {
    private FakeRightViewPager vp;
    private ProductDetailView productDetailView;
    private View view;
    private FragmentManager fm;
    private int[] icons = new int[]{R.drawable.tu1, R.drawable.tu2, R.drawable.tu3};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.product_detail, container, false);
        vp = (FakeRightViewPager)view.findViewById(R.id.vp);
        productDetailView= (ProductDetailView)view.findViewById(R.id.productDetailView);
               initData();
        fm=getFragmentManager();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initData() {
        PagerAdapter adapter=new PagerAdapter() {
            @Override
            public int getCount() {
                return icons.length;
            }
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(icons[position]);
                container.addView(imageView);
                return imageView;
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
        };
        vp.setAdapter(adapter);
        vp.setOnOpenListener(this);
        productDetailView.setUpFreshListner(this);
    }

    @Override
    public void open() {
        //让右边的fragment顺滑进来
        if (fm.getBackStackEntryCount()==0) {
            Fragment secondFragment = new RightFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //设置水平方向动画 此方法最低使用版本13
            fragmentTransaction.setCustomAnimations(R.animator.slide_fragment_horizontal_right_in, R.animator.slide_fragment_horizontal_left_out, R.animator.slide_fragment_horizontal_left_in, R.animator.slide_fragment_horizontal_right_out);
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
    public void upFreshOpen() {
        if (fm.getBackStackEntryCount()==0) {
            Fragment threeFragment = new BottomFragment();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //设置垂直方向动画,此方法最低使用版本13
            fragmentTransaction.setCustomAnimations
                    (R.animator.slide_fragment_vertical_right_in,
                            R.animator.slide_fragment_vertical_left_out,
                            R.animator.slide_fragment_vertical_left_in,
                            R.animator.slide_fragment_vertical_right_out);
            fragmentTransaction.add(R.id.fragment_place, threeFragment);
            //Fragment压入栈中
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction .commit();
        }else{
            //Fragment弹栈
            fm.popBackStack();
        }
    }
}
