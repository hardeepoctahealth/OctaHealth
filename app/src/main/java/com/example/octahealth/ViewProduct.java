package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class ViewProduct extends AppCompatActivity implements PaymentResultListener {


    TextView title,title2,content,buynow;
    ImageView image;
    String id;
    NestedScrollView nestedScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        Checkout.preload(getApplicationContext());


        image=findViewById(R.id.image);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        buynow=findViewById(R.id.buynow);
        title2=findViewById(R.id.title2);

        nestedScrollView=findViewById(R.id.nested);

        nestedScrollView.setSmoothScrollingEnabled(true);

        id=getIntent().getStringExtra("id");

        FirebaseDatabase.getInstance().getReference().child("Products").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                BlogDetails details=snapshot.getValue(BlogDetails.class);
                content.setText(details.getContent());
                title.setText(details.getTitle());
                title2.setText(details.getTitle());
                Glide.with(ViewProduct.this).load(details.getImage()).into(image);


                buynow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Checkout checkout = new Checkout();

                        try {
                            JSONObject options = new JSONObject();

                            options.put("name", "Merchant Name");
                            options.put("currency", "INR");
                            options.put("prefill.email", "email@mail.com");
                            options.put("prefill.contact", "8219583372");
                            options.put("amount", "50000");//pass amount in currency subunits
                            checkout.open(ViewProduct.this, options);


                        } catch (Exception e) {
                            Log.e("TAG", "Error in starting Razorpay Checkout", e);
                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}