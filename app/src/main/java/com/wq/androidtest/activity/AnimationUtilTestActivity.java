package com.wq.androidtest.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wq.androidtest.R;
import com.wq.androidtest.util.AnimationUtil;

/**
 * Created by wangqi on 15/9/13.
 */
public class AnimationUtilTestActivity extends BaseActivity{
    Button mBtn;
    ImageView mImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_util);
        mBtn = (Button) findViewById(R.id.btn);
        mImg = (ImageView) findViewById(R.id.img);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtil.scaleView(mImg,0.2f);
            }
        });
    }
}