package org.cimbar.camerafilecopy;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WebViewActivity extends Activity {

    private WebView webView;
    protected WebSettings webViewSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webView = findViewById(R.id.web_view);
        webViewSettings = webView.getSettings();

        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setAllowFileAccess(true);

        webView.setWebViewClient(new WebViewClient());

        webView.loadUrl("https://cimbar.org/");

        //webView.loadData("Local html file if cached or downloaded","text/html", "UTF-8");
        webViewSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }


}
