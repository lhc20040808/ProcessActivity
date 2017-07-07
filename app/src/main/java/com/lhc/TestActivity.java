package com.lhc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * 作者：lhc
 * 时间：2017/7/7.
 */

public class TestActivity extends AppCompatActivity {
    private TextView tv_productId;
    private TextView tv_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Process", "TestActivity启动");
        setContentView(R.layout.activity_test);
        tv_info = (TextView) findViewById(R.id.tv_info);
        tv_productId = (TextView) findViewById(R.id.tv_productId);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            tv_info.setText("Info:" + bundle.getString("infomation", "没有这个参数") + "");
            tv_productId.setText("ProductId:" + bundle.getString("productId", "没有这个参数") + "");
        }
    }
}
