package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.octahealth.NaviagationFragment.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.UUID;

public class ViewProduct extends AppCompatActivity implements PaymentResultListener {


    TextView title, title2, content, buynow,price;
    ImageView image;
    String t,a;
    String id;
    int p=0;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Benefit, BenefitsViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        Checkout.preload(getApplicationContext());


        image = findViewById(R.id.image);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        buynow = findViewById(R.id.buynow);
        title2 = findViewById(R.id.title2);
        recyclerView = findViewById(R.id.benefitsrecyclerview);
        price=findViewById(R.id.price);

        nestedScrollView = findViewById(R.id.nested);

        nestedScrollView.setSmoothScrollingEnabled(true);

        id = getIntent().getStringExtra("id");

        FirebaseDatabase.getInstance().getReference().child("Products").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final ProductDetails details = snapshot.getValue(ProductDetails.class);
                content.setText(details.getContent());
                title.setText(details.getTitle());
                title2.setText(details.getTitle());
                t=details.getTitle();
                a=details.getDiscountedprice();
                Glide.with(ViewProduct.this).load(details.getImage()).into(image);
                price.setText("₹" + details.getDiscountedprice());

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
                            options.put("amount", details.getDiscountedprice()+"00");//pass amount in currency subunits
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


        final Query query = FirebaseDatabase.getInstance().getReference().child("Products").child(id).child("benefits");

        FirebaseRecyclerOptions<Benefit> options = new FirebaseRecyclerOptions.Builder<Benefit>()
                .setQuery(query, new SnapshotParser<Benefit>() {
                    @NonNull
                    @Override
                    public Benefit parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Benefit(snapshot.child("title").getValue(String.class));
                    }
                }).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Benefit, BenefitsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BenefitsViewHolder holder, int position, @NonNull Benefit model) {
                holder.title.setText(model.getTitle());
                holder.title.setTextSize(18);
            }

            @NonNull
            @Override
            public BenefitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.benefitsitem, parent, false);
                return new BenefitsViewHolder(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(ViewProduct.this));
        recyclerView.setAdapter(firebaseRecyclerAdapter);


        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }


    private class BenefitsViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public BenefitsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Success", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(ViewProduct.this,PaymentInfo.class);
        intent.putExtra("amount",a);
        intent.putExtra("plantype",t);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Myplans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.child(id).exists())
                {
                    snapshot.child(id).getRef().child("id").setValue(id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseRecyclerAdapter!=null)
        {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(firebaseRecyclerAdapter!=null)
        {
            firebaseRecyclerAdapter.stopListening();
        }
    }
}