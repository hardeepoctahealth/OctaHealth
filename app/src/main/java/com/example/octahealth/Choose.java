package com.example.octahealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import static maes.tech.intentanim.CustomIntent.customType;

public class Choose extends AppCompatActivity {


    LinearLayout home,comm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);


        home=findViewById(R.id.octahome);
        comm=findViewById(R.id.octacomm);


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose.this,Home.class));
                finish();
                customType(Choose.this,"fadein-to-fadeout");
            }
        });

        comm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Choose.this, CommunityAc.class));
                finish();
                customType(Choose.this,"fadein-to-fadeout");

            }
        });
    }
}