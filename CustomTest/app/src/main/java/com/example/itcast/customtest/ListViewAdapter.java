package com.example.itcast.customtest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * LQJ on 2016/11/5 16:31
 * 15058152353@163.com
 */
public class ListViewAdapter extends BaseAdapter{
    private Context context;

    public ListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv=new TextView(context);
        tv.setText("我是bottom fragment的"+position+"数据");
        return tv;
    }
}
