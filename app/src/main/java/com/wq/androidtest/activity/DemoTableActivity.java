package com.wq.androidtest.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.wq.androidlibrary.activity.BaseActivity;
import com.wq.androidtest.R;
import com.wq.androidtest.model.DemoEntryModel;
import com.wq.androidtest.adapter.DemoEntryAdapter;
import com.wq.androidtest.view.focus.CircleProgressBar;

import java.util.ArrayList;

public class DemoTableActivity extends BaseActivity {

    private static final String FUNC_MODELS = "func_models";

    GridView mDemoEntrys;
    DemoEntryAdapter mAdapter;
    ArrayList<DemoEntryModel> demoEntryModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_table);
        mDemoEntrys = (GridView) findViewById(R.id.gv_demo_entry);
        Intent intent = getIntent();
        if(intent == null)
        {
            initData();
        }
        else {
           demoEntryModels = (ArrayList<DemoEntryModel>) intent.getSerializableExtra(FUNC_MODELS);
        }
        if(demoEntryModels == null)
        {
           initData();
        }
        mAdapter = new DemoEntryAdapter(this, demoEntryModels);
        mDemoEntrys.setAdapter(mAdapter);

        mDemoEntrys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(DemoTableActivity.this, demoEntryModels.get(position).getClazz());
                startActivity(i);
            }
        });

    }

    private void initData() {
        demoEntryModels = new ArrayList<>();
        demoEntryModels.add(new DemoEntryModel("快速入口", QuickTestActivity.class));
        demoEntryModels.add(new DemoEntryModel("test focused", FocusTesetActivity.class));
        demoEntryModels.add(new DemoEntryModel("show ip", ShowIPActivity.class));
        demoEntryModels.add(new DemoEntryModel("eventbus", EventBusDemoActivity.class));
        demoEntryModels.add(new DemoEntryModel("update", UpdateApkActivity.class));
        demoEntryModels.add(new DemoEntryModel("ratingbar", RatingActivity.class));
        demoEntryModels.add(new DemoEntryModel("progressbutton", ProgressButtonActivity.class));
        demoEntryModels.add(new DemoEntryModel("circleprogress", CircleProgressActivity.class));
        demoEntryModels.add(new DemoEntryModel("textstyle", TextActivity.class));
    }


}
