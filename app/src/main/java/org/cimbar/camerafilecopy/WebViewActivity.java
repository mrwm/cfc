package org.cimbar.camerafilecopy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.core.view.GestureDetectorCompat;


public class WebViewActivity extends Activity {

    private WebView webView;
    protected WebSettings webViewSettings;
    private ValueCallback<Uri[]> uploadMessage;
    private final static int FILECHOOSER_RESULTCODE=222;

    private GestureDetectorCompat mDetector;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        webView = findViewById(R.id.web_view);
        webViewSettings = webView.getSettings();

        // I noticed that the decoder can't read if the nav bar is covering the code, so
        // get the nav bar height and add it to the webview's margin
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int navHeight = getResources().getDimensionPixelSize(resourceId);

        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setAllowFileAccess(true);

        //webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                }
                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILECHOOSER_RESULTCODE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    return false;
                }
                return true;
            }
        });

        webView.loadUrl("file:///android_asset/cimbar_js.html");

        //webView.loadData("Local html file if cached or downloaded","text/html", "UTF-8");
        webViewSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // Set up the swipe gestures
        mDetector = new GestureDetectorCompat(this, new FlingGestureDetector());
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return mDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(this, "Swipe to decode data :)",  Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (uploadMessage == null) return;
            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            uploadMessage = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    class FlingGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final String TAG = "Gestures";

        // We only want fling gestures to trigger the view transitions, not scrolling.
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            //Log.d(TAG, "onFling: " + event1.toString() + event2.toString());
            /// Directional logic if needed
            //float angle = (float) Math.toDegrees(Math.atan2(event1.getY() - event2.getY(), event2.getX() - event1.getX()));
            //if (angle > -45 && angle <= 45) {
            //    Log.d(TAG, "Right to Left swipe performed");
            //    return true;
            //}
            //if (angle >= 135 && angle < 180 || angle < -135 && angle > -180) {
            //    Log.d(TAG, "Left to Right swipe performed");
            //    return true;
            //}
            //if (angle < -45 && angle >= -135) {
            //    Log.d(TAG, "Up to Down swipe performed");
            //    return true;
            //}
            //if (angle > 45 && angle <= 135) {
            //    Log.d(TAG, "Down to Up swipe performed");
            //    return true;
            //}
            finish();
            return true;
        }
    }

}