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
import com.example.octahealth.BlogDetails;
import com.example.octahealth.ProductDetails;
import com.example.octahealth.R;
import com.example.octahealth.ViewBlog;
import com.example.octahealth.ViewProduct;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class Products extends Fragment {

    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<ProductDetails, ProductsViewHolder> firebaseRecyclerAdapter;
    CircularProgressBar circularProgressBar;


    public Products() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        recyclerView = view.findViewById(R.id.productsrecyclerview);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);

        final Query query = FirebaseDatabase.getInstance().getReference().child("Products");

        FirebaseRecyclerOptions<ProductDetails> options = new FirebaseRecyclerOptions.Builder<ProductDetails>()
                .setQuery(query, new SnapshotParser<ProductDetails>() {
                    @NonNull
                    @Override
                    public ProductDetails parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new ProductDetails(snapshot.child("title").getValue(String.class), snapshot.child("content").getValue(String.class), snapshot.child("image").getValue(String.class));
                    }
                }).build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ProductDetails, ProductsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ProductsViewHolder holder, final int position, @NonNull ProductDetails model) {

                if (position == 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    circularProgressBar.setVisibility(View.GONE);
                }

                holder.content.setText(model.getContent());
                holder.title.setText(model.getTitle());
                Glide.with(getActivity()).load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                                (getActivity(), holder.imageView, "imageMain");
                        Intent in = new Intent(getActivity(), ViewProduct.class);
                        in.putExtra("id", firebaseRecyclerAdapter.getRef(position).getKey());
                        startActivity(in, activityOptionsCompat.toBundle());
                    }
                });

            }

            @NonNull
            @Override
            public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.productsitem, parent, false);
                return new ProductsViewHolder(view1);
            }
        };


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return view;
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