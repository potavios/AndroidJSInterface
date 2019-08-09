package com.pauloos.js_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    private TextView resultFromWeb;
    private EditText textToSendToWeb;
    private Button sendToWebButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webview.addJavascriptInterface(new WebAppInterface(this), "Android");
        webview.loadUrl("https://potavios.github.io/index.html");

        resultFromWeb = findViewById(R.id.tv_resultFromWeb);
        textToSendToWeb = findViewById(R.id.et_textToWeb);
        sendToWebButton = findViewById(R.id.bt_sendToWeb);


        textToSendToWeb.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hideKeyboardFrom(getApplicationContext(), view);
            }
        });

        sendToWebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String texto = textToSendToWeb.getText().toString();
                        webview.loadUrl("javascript:callFromActivity('" + texto + "')");

                    }
                });

            }
        });

    }


    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void ShowToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void changeText(String text){
            resultFromWeb.setText(text);
        }
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }





}
