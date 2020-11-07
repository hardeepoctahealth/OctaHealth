package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Plans extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ViewPlan,PlansViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        recyclerView=findViewById(R.id.plansrecyview);


        final Query query = FirebaseDatabase.getInstance().getReference().child("MyPlans");

        FirebaseRecyclerOptions<ViewPlan> options = new FirebaseRecyclerOptions.Builder<ViewPlan>()
                .setQuery(query, new SnapshotParser<ViewPlan>() {
                    @NonNull
                    @Override
                    public ViewPlan parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new ViewPlan(snapshot.child("title").getValue(String.class),snapshot.child("link").getValue(String.class),snapshot.child("buttonname").getValue(String.class));
                    }
                }).build();

        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<ViewPlan, PlansViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PlansViewHolder holder, int position, @NonNull ViewPlan model) {
                holder.title.setText(model.getTitle());

                holder.book.setText(model.getButtonname());

            }

            @NonNull
            @Override
            public PlansViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewplanitem,parent,false);
                return new PlansViewHolder(view);
            }
        };

        recyclerView.setLayoutManager(new LinearLayoutManager(Plans.this));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    private class PlansViewHolder extends RecyclerView.ViewHolder{

        TextView title,book;
        public PlansViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            book=itemView.findViewById(R.id.book);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.stopListening();
    }
}