package com.example.octahealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class CommunityAc extends AppCompatActivity {


    WebView webView;
    CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        final ImageView switchwindow=findViewById(R.id.switchwindow);
        switchwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunityAc.this, Choose.class));
            }
        });
        webView=findViewById(R.id.web);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                if(progress == 100)
                    circularProgressBar.setVisibility(View.GONE);
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://octahealth.tribe.so/");
        circularProgressBar=findViewById(R.id.circularProgressBar);
        circularProgressBar.bringToFront();


    }
}