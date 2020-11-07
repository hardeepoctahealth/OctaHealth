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
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import static maes.tech.intentanim.CustomIntent.customType;

public class CommunityAc extends AppCompatActivity {


    WebView webView;
    CircularProgressBar circularProgressBar;
    ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        final ImageView switchwindow=findViewById(R.id.switchwindow);
        switchwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CommunityAc.this, Choose.class));
                customType(CommunityAc.this,"fadein-to-fadeout");
            }
        });
        webView=findViewById(R.id.web);
        refresh=findViewById(R.id.refresh);


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


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.reload();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(webView.getUrl().equals("https://octahealth.tribe.so/"))
        {
            finish();
        }
        else {
            webView.goBack();
        }
    }
}