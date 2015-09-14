package com.wq.androidtest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.wq.androidtest.R;
import com.wq.androidtest.adapter.DemoEntryAdapter;
import com.wq.androidtest.model.DemoEntryModel;

import java.util.ArrayList;

public class DemoTableActivity extends BaseActivity {



    GridView mDemoEntrys;
    DemoEntryAdapter mAdapter;
    ArrayList<DemoEntryModel> demoEntryModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_table);
        mDemoEntrys = (GridView) findViewById(R.id.gv_demo_entry);
        Intent intent = getIntent();
        if (intent == null) {
            initData();
        } else {
            demoEntryModels = (ArrayList<DemoEntryModel>) intent.getSerializableExtra(FUNC_MODELS);
        }
        if (demoEntryModels == null) {
            initData();
        }
        mAdapter = new DemoEntryAdapter(this, demoEntryModels);
        mDemoEntrys.setAdapter(mAdapter);
        mAdapter.getView(0,null,mDemoEntrys).requestFocus();

//        mDemoEntrys.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent i = null;
//                if (demoEntryModels.get(position).getChilds() != null) {
//                    i = new Intent(DemoTableActivity.this, DemoTableActivity.class);
//                    i.putExtra(FUNC_MODELS, (Serializable) demoEntryModels.get(position).getChilds());
//                } else {
//                    i = new Intent(DemoTableActivity.this, demoEntryModels.get(position).getClazz());
//                }
//                i.putExtra(TITLE,demoEntryModels.get(position).getDes());
//                startActivity(i);
//            }
//        });

    }

    private void initData() {
        demoEntryModels = new ArrayList<>();
        demoEntryModels.add(new DemoEntryModel("快速入口", ScreenInfoActivity.class));
        demoEntryModels.add(new DemoEntryModel("test focused", FocusTesetActivity.class));
        demoEntryModels.add(new DemoEntryModel("show ip", ShowIPActivity.class));
        demoEntryModels.add(new DemoEntryModel("eventbus", EventBusDemoActivity.class));
        demoEntryModels.add(new DemoEntryModel("update", UpdateApkActivity.class));
        demoEntryModels.add(new DemoEntryModel("textstyle", TextActivity.class));
        demoEntryModels.add(new DemoEntryModel("mListView height", ListViewHeightAcitvity.class));
        demoEntryModels.add(new DemoEntryModel("animUtilTest", AnimationUtilTestActivity.class));
        demoEntryModels.add(new DemoEntryModel("flowlayout", FlowLayoutActivity.class));
        demoEntryModels.add(new DemoEntryModel("screeninfo", ScreenInfoActivity.class));
        demoEntryModels.add(new DemoEntryModel("clipboard", ClipboardActivity.class));

        //custom view
        ArrayList<DemoEntryModel> list = new ArrayList<>();
        list.add(new DemoEntryModel("ratingbar", RatingActivity.class));
        list.add(new DemoEntryModel("progressbutton", ProgressButtonActivity.class));
        list.add(new DemoEntryModel("circleprogress", CircleProgressActivity.class));
        DemoEntryModel customModel = new DemoEntryModel("custom views", null, list);
        demoEntryModels.add(customModel);
    }


}
