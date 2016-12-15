package org.hr24.almel.careertraectory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebView;

import org.hr24.almel.careertraectory.utils.SimpleWebViewClientImpl;

public class Register extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        webView = (WebView) findViewById(R.id.wv_register);

        webView.getSettings().setJavaScriptEnabled(true);
        SimpleWebViewClientImpl webViewClient = new SimpleWebViewClientImpl(this);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl("https://hr24.timepad.ru/widget/bare/event=366673");


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && this.webView.canGoBack()) {
            this.webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


}
