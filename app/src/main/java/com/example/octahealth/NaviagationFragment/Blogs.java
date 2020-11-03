package com.example.octahealth.NaviagationFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.BlogDetails;
import com.example.octahealth.Choose;
import com.example.octahealth.Profile;
import com.example.octahealth.R;
import com.example.octahealth.ViewBlog;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Blogs extends Fragment {

    public Blogs() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    ShimmerFrameLayout shimmer;
    FirebaseRecyclerAdapter<BlogDetails,BlogViewHolder> firebaseRecyclerAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =inflater.inflate(R.layout.fragment_blogs, container, false);

        recyclerView=view.findViewById(R.id.blogsrecyclerview);
        shimmer=view.findViewById(R.id.shimmer);

        final ImageView profile=view.findViewById(R.id.profilepic);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
            }
        });

        final ImageView switchwindow=view.findViewById(R.id.switchwindow);
        switchwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Choose.class));
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(getActivity()!=null)
                Glide.with(getContext()).load(snapshot.child("profilepic").getValue(String.class)).apply(new RequestOptions().override(200, 200)).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        shimmer.startShimmer();

        final Query query = FirebaseDatabase.getInstance().getReference().child("Blogs");

        FirebaseRecyclerOptions<BlogDetails> options = new FirebaseRecyclerOptions.Builder<BlogDetails>()
                .setQuery(query, new SnapshotParser<BlogDetails>() {
                    @NonNull
                    @Override
                    public BlogDetails parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new BlogDetails(snapshot.child("title").getValue(String.class),snapshot.child("content").getValue(String.class),snapshot.child("date").getValue(String.class),snapshot.child("image").getValue(String.class));
                    }
                }).build();



        firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<BlogDetails, BlogViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final BlogViewHolder holder, final int position, @NonNull BlogDetails model) {

                if(position==0)
                {
                    shimmer.stopShimmer();
                    shimmer.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                holder.content.setText(model.getContent());
                holder.title.setText(model.getTitle());
                holder.date.setText(model.getDate());
                Glide.with(getActivity()).load(model.getImage()).into(holder.imageView);
                holder.continueread.setText("Continue Reading...");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                                (getActivity(),holder.imageView,"imageMain");
                        Intent in = new Intent(getActivity(),ViewBlog.class);
                        in.putExtra("id",firebaseRecyclerAdapter.getRef(position).getKey());
                        startActivity(in,activityOptionsCompat.toBundle());
                    }
                });

            }

            @NonNull
            @Override
            public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.blogitem,parent,false);
                return new BlogViewHolder(view1);
            }
        };


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    private class BlogViewHolder extends RecyclerView.ViewHolder{

        TextView title,content,date,continueread;
        ImageView imageView;

        public BlogViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            content=itemView.findViewById(R.id.content);
            date=itemView.findViewById(R.id.date);
            continueread=itemView.findViewById(R.id.continuereading);
            imageView=itemView.findViewById(R.id.image);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.stopListening();
    }
}