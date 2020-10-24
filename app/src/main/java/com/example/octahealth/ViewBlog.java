package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewBlog extends AppCompatActivity {

    TextView title,content,date;
    ImageView image;
    String id;
    NestedScrollView nestedScrollView;
    long startTime;
    long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_blog);


        image=findViewById(R.id.image);
        title=findViewById(R.id.title);
        content=findViewById(R.id.content);
        date=findViewById(R.id.date);

        nestedScrollView=findViewById(R.id.nested);

        nestedScrollView.setSmoothScrollingEnabled(true);

        id=getIntent().getStringExtra("id");

        FirebaseDatabase.getInstance().getReference().child("Blogs").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                BlogDetails details=snapshot.getValue(BlogDetails.class);
                content.setText(details.getContent());
                title.setText(details.getTitle());
                date.setText(details.getDate());
                Glide.with(ViewBlog.this).load(details.getImage()).into(image);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();

    }

    @Override
    protected void onPause() {
        super.onPause();
        endTime = System.currentTimeMillis();
        final long timeSpend = endTime - startTime;

        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            FirebaseDatabase.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child("timespent").exists())
                    {
                        snapshot.getRef().child("timespent").setValue((snapshot.child("timespent").getValue(Long.class)+ timeSpend));
                    }
                    else {
                        snapshot.getRef().child("timespent").setValue(timeSpend);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        Log.i("time", String.valueOf(timeSpend));
    }
}