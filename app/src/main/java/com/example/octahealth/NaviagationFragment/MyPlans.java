package com.example.octahealth.NaviagationFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.Choose;
import com.example.octahealth.Plans;
import com.example.octahealth.Profile;
import com.example.octahealth.R;
import com.example.octahealth.ViewPlan;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import static maes.tech.intentanim.CustomIntent.customType;

public class MyPlans extends Fragment {

    LinearLayout octastarter,octapro,octaplus;
    CircularProgressBar circularProgressBar;
    RecyclerView starterrecyclerView,plusrecyclerview,prorecyclerview;
    ImageView noplans;
    TextView noplantstext;
    FirebaseRecyclerAdapter<ViewPlan, PlansViewHolder> firebaseRecyclerAdapter,firebaseRecyclerAdapter2,firebaseRecyclerAdapter3;

    public MyPlans() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_plans, container, false);


        octastarter=view.findViewById(R.id.octastarter);
        octapro=view.findViewById(R.id.octapro);
        octaplus=view.findViewById(R.id.octaplus);
        circularProgressBar=view.findViewById(R.id.circularProgressBar);
        circularProgressBar.bringToFront();
        noplans=view.findViewById(R.id.noplansimg);
        noplantstext=view.findViewById(R.id.noplanstext);

        final ImageView profile = view.findViewById(R.id.profilepic);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
                customType(getActivity(),"fadein-to-fadeout");
            }
        });
        final ImageView switchwindow=view.findViewById(R.id.switchwindow);
        switchwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Choose.class));
                getActivity().finish();  customType(getActivity(),"fadein-to-fadeout");
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (getContext() != null)
                    Glide.with(getContext()).load(snapshot.child("profilepic").getValue(String.class)).apply(new RequestOptions().override(200, 200)).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Myplans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("id").getValue(String.class).equals("01")) {
                            octastarter.setVisibility(View.VISIBLE);
                            octastarter.scheduleLayoutAnimation();
                        } else if (dataSnapshot.child("id").getValue(String.class).equals("02")) {
                            octaplus.setVisibility(View.VISIBLE);
                            octaplus.scheduleLayoutAnimation();
                        } else if (dataSnapshot.child("id").getValue(String.class).equals("03")) {
                            octapro.setVisibility(View.VISIBLE);
                            octapro.scheduleLayoutAnimation();
                        }
                    }
                }
                else {
                    noplans.setVisibility(View.VISIBLE);
                    noplantstext.setVisibility(View.VISIBLE);
                }
                circularProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        starterrecyclerView=view.findViewById(R.id.octastarterrecyview);
        plusrecyclerview=view.findViewById(R.id.octaplusrecyview);
        prorecyclerview=view.findViewById(R.id.octaprorecyview);


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

        starterrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        starterrecyclerView.setAdapter(firebaseRecyclerAdapter);

        final Query query2 = FirebaseDatabase.getInstance().getReference().child("MyPlans");

        FirebaseRecyclerOptions<ViewPlan> options2 = new FirebaseRecyclerOptions.Builder<ViewPlan>()
                .setQuery(query2, new SnapshotParser<ViewPlan>() {
                    @NonNull
                    @Override
                    public ViewPlan parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new ViewPlan(snapshot.child("title").getValue(String.class),snapshot.child("link").getValue(String.class),snapshot.child("buttonname").getValue(String.class));
                    }
                }).build();

        firebaseRecyclerAdapter2=new FirebaseRecyclerAdapter<ViewPlan, PlansViewHolder>(options2) {
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

        plusrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        plusrecyclerview.setAdapter(firebaseRecyclerAdapter2);


        final Query query3 = FirebaseDatabase.getInstance().getReference().child("MyPlans");

        FirebaseRecyclerOptions<ViewPlan> options3 = new FirebaseRecyclerOptions.Builder<ViewPlan>()
                .setQuery(query3, new SnapshotParser<ViewPlan>() {
                    @NonNull
                    @Override
                    public ViewPlan parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new ViewPlan(snapshot.child("title").getValue(String.class),snapshot.child("link").getValue(String.class),snapshot.child("buttonname").getValue(String.class));
                    }
                }).build();

        firebaseRecyclerAdapter3=new FirebaseRecyclerAdapter<ViewPlan, PlansViewHolder>(options3) {
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

        prorecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        prorecyclerview.setAdapter(firebaseRecyclerAdapter3);

        return view;
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
    public void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter2.startListening();
        firebaseRecyclerAdapter3.startListening();

    }

    @Override
    public void onPause() {
        super.onPause();
       /* firebaseRecyclerAdapter.stopListening();
        firebaseRecyclerAdapter2.stopListening();
        firebaseRecyclerAdapter3.stopListening();
*/
    }

}