package com.wq.androidtest.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.wq.androidtest.R;
import com.wq.androidtest.model.DemoEntryModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by qiwang on 2015/8/27.
 */
public final class DemoEntryAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<DemoEntryModel> demoEntryModels;

    public DemoEntryAdapter(@NonNull Context context, @NonNull ArrayList<DemoEntryModel> demos) {
        demoEntryModels = demos;
        mContext = context;
    }

    @Override
    public int getCount() {
        return demoEntryModels.size();
    }

    @Override
    public Object getItem(int position) {
        return demoEntryModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView holder;
        if (convertView == null) {
            TextView textView = new TextView(mContext);
            textView.setFocusable(true);
            textView.setTextSize(25);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(5, 5, 5, 5);
            textView.setTag(textView);
            textView.setBackgroundResource(R.drawable.bg_border);
            textView.setTextColor(Color.RED);
            holder = textView;
        } else {
            holder = (TextView) convertView.getTag();
        }
        holder.setText(demoEntryModels.get(position).getDes());
        return holder;
    }

    public ArrayList<DemoEntryModel> getDemoEntryModels() {
        return demoEntryModels;
    }

    public void setDemoEntryModels(@NonNull ArrayList<DemoEntryModel> demoEntryModels) {
        this.demoEntryModels = demoEntryModels;
    }
}
