package com.wq.androidtest.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wq.androidlibrary.activity.BaseActivity;
import com.wq.androidtest.util.FileUtil;

import java.io.File;

/**
 * Created by wangqi on 15/9/8.
 */
public class UpdateApkActivity extends BaseActivity {

    File apkFile;
    String filename = "update.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setText("update now");
        button.setTextSize(25);
        setContentView(button);
        button.setText(FileUtil.getExternalStoragePath());
        filename = FileUtil.getExternalStoragePath() + File.separator + filename;
        apkFile = new File(filename);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + apkFile.getAbsolutePath()), "application/vnd.android.package-archive");
                startActivity(intent);
            }
        });
    }
}