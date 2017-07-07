package com.lhc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Set;

/**
 * 作者：lhc
 * 时间：2017/7/7.
 */

public class ProcessActivity extends AppCompatActivity {
    private static final String TAG = ProcessActivity.class.getSimpleName().toString();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_process);
        resolveUri();
    }

    private void resolveUri() {
        Uri uri = getIntent().getData();

        Log.e(TAG, "uri------>" + uri);

        String host = uri.getHost();
        Log.e(TAG, "host------>" + host);

        String scheme = uri.getScheme();
        Log.e(TAG, "scheme------>" + scheme);

        String path = uri.getPath();
        Log.e(TAG, "path------->" + path);

        String query = uri.getQuery();
        Log.e(TAG, "query------>" + query);

        if (path != null && !"".equals(path)) {
            distribute(uri, path.replace("/", ""));
        } else {
            finish();
        }


    }

    private void distribute(Uri uri, String path) {
        Bundle bundle = new Bundle();
        Set<String> keyNames = uri.getQueryParameterNames();
        for (String key : keyNames) {
            String value = uri.getQueryParameter(key);
            bundle.putString(key, value);
        }

        try {
            Class desPage = Class.forName(path);
            Intent intent = new Intent(this, desPage);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            finish();
        }
    }
}
