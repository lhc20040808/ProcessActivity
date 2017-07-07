package com.lhc;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private Button btn_scheme;
    private ClipboardManager clipboardManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("file:///android_asset/" + "index.html");

        btn_scheme = (Button) findViewById(R.id.btn_scheme);
        btn_scheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("external://com.lhc/com.lhc.TestActivity?productId=600001")));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        clipboardManager.removePrimaryClipChangedListener(null);
    }

    private void initService() {
        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData clipData = clipboardManager.getPrimaryClip();
                ClipData.Item item = clipData.getItemAt(0);
                Uri uri = Uri.parse(item.getText().toString());
                String host = uri.getHost();
                if (host != null && host.equals("com.lhc")) {
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                }
            }
        });
    }
}
