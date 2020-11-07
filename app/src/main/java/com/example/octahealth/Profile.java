package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.NaviagationFragment.Products;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.islamkhsh.CardSliderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class Profile extends AppCompatActivity {


    ImageView profilepic, edit,back;
    TextView timespent, name,logout;
    TextView gender, dob, healthid;
    RecyclerView recyclerView;
    ArrayList<ProductDetails> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.name);
        timespent = findViewById(R.id.timespent);
        profilepic = findViewById(R.id.profilepic);
        edit = findViewById(R.id.edit);
        gender = findViewById(R.id.gender);
        dob = findViewById(R.id.dob);
        healthid = findViewById(R.id.healthid);
        back=findViewById(R.id.back);
        logout=findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null)
                {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent=new Intent(Profile.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView=findViewById(R.id.myplansrecyview);

        products=new ArrayList<>();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name.setText(snapshot.child("name").getValue(String.class));
                        if(snapshot.child("timespent").exists())
                        {timespent.setText(((snapshot.child("timespent").getValue(Long.class)) / 60000) + " m");}

                        Glide.with(getApplicationContext()).load(snapshot.child("profilepic").getValue(String.class)).into(profilepic);
                        gender.setText(snapshot.child("gender").getValue(String.class));
                        dob.setText(snapshot.child("dateofbirth").getValue(String.class));
                        healthid.setText(snapshot.child("healthid").getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Myplans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    FirebaseDatabase.getInstance().getReference().child("Products").child(dataSnapshot.child("id").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                products.add(new ProductDetails(snapshot.child("title").getValue(String.class), snapshot.child("content").getValue(String.class), snapshot.child("image").getValue(String.class), snapshot.child("actualprice").getValue(String.class), snapshot.child("discountedprice").getValue(String.class), snapshot.getKey()));


                            recyclerView.setLayoutManager(new LinearLayoutManager(Profile.this));
                            recyclerView.setAdapter(new ProductAdapter(products));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, EditProfile.class));
                customType(Profile.this,"left-to-right");
            }
        });
    }

    public class ProductAdapter extends CardSliderAdapter<ProductsViewHolder> {

        private ArrayList<ProductDetails> Products;

        public ProductAdapter(ArrayList<ProductDetails> Products) {
            this.Products = Products;
        }

        @Override
        public int getItemCount() {
            return Products.size();
        }

        @Override
        public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.planitem, parent, false);
            return new ProductsViewHolder(view);
        }

        @Override
        public void bindVH(@NotNull final ProductsViewHolder holder, final int i) {


            holder.title.setText(Products.get(i).getTitle());
            Glide.with(Profile.this).load(Products.get(i).getImage()).apply(new RequestOptions().override(800, 500)).into(holder.imageView);
            holder.content.setText(Products.get(i).getContent());
        }

    }


    private class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView title, content;
        ImageView imageView;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customType(Profile.this,"fadein-to-fadeout");
    }
}