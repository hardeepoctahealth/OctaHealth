package com.example.octahealth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PaymentInfo extends AppCompatActivity {

    TextView amount,plantype,returnhome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);

        amount=findViewById(R.id.amount);
        plantype=findViewById(R.id.plantype);
        returnhome=findViewById(R.id.returnhome);

        amount.setText("₹"+getIntent().getStringExtra("amount"));
        plantype.setText(getIntent().getStringExtra("plantype"));

        returnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PaymentInfo.this,Home.class));
                finish();
            }
        });


    }
}