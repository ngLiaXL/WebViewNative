package com.example.xianglongliang.myapplication;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "TAG";

    ProgressDialog dialog;
    WebView mWebView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = findViewById(R.id.webview);
        dialog = new ProgressDialog(this);

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                dialog.show();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();

            }
        });
//        mWebView.loadUrl("http://viodoc.tpddns.cn:18088/#/FamMember");
        mWebView.loadUrl("file:///android_asset/js_java_interaction.html");

        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(new JsInteration(), "android");


    }


    /**
     * 原生-H5
     * 直接访问H5里不带返回值的方法，show()为H5里的方法
     *
     * @param view
     */
    public void onClickNoParamNoReturn(View view) {
        mWebView.loadUrl("JavaScript:show()");
    }

    /**
     * 原生-H5
     * 访问H5里带参数的方法，alertMessage(message)为H5里的方法
     * //传固定字符串可以直接用单引号括起来
     *
     * @param view
     */
    public void onClickNoReturn(View view) {
        mWebView.loadUrl("javascript:alertMessage('哈哈')");
        //当出入变量名时，需要用转义符隔开
        /*String content = "9880";
        mWebView.loadUrl("javascript:alertMessage(\"" + content + "\")");*/
    }

    /**
     * 原生-H5
     * //Android调用有返回值js方法，安卓4.4以上才能用这个方法
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClickPR(View view) {
        mWebView.evaluateJavascript("sum(1,2)", new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String value) {
                Log.d(TAG, "js返回的结果为=" + value);
                Toast.makeText(MainActivity.this, "js返回的结果为=" + value, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * H5-原生
     * 自己写一个类，里面是提供给H5访问的方法
     */
    public class JsInteration {

        /**
         * 一定要写，不然H5调不到这个方法
         */
        @JavascriptInterface
        public String back() {
            return "我是java里的方法返回值";
        }
    }


}
