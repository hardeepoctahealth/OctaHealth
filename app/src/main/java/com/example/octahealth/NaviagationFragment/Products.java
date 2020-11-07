package com.example.octahealth.NaviagationFragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.Benefit;
import com.example.octahealth.Choose;
import com.example.octahealth.CustomizePlan;
import com.example.octahealth.ProductDetails;
import com.example.octahealth.Profile;
import com.example.octahealth.R;
import com.example.octahealth.ViewProduct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.islamkhsh.CardSliderAdapter;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class Products extends Fragment {

    CircularProgressBar circularProgressBar;
    CardSliderViewPager productslider;
    ArrayList<ProductDetails> products;
    FirebaseRecyclerAdapter<Benefit, BenefitsViewHolder> firebaseRecyclerAdapter;

    public Products() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        circularProgressBar = view.findViewById(R.id.circularProgressBar);

        productslider = view.findViewById(R.id.productsslider);
        products = new ArrayList<>();

        final ImageView profile = view.findViewById(R.id.profilepic);
        final ImageView switchwindow = view.findViewById(R.id.switchwindow);
        switchwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Choose.class));
                getActivity().finish();
                customType(getActivity(), "fadein-to-fadeout");
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

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Profile.class));
                customType(getActivity(), "fadein-to-fadeout");
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    products.add(new ProductDetails(dataSnapshot.child("title").getValue(String.class), dataSnapshot.child("content").getValue(String.class), dataSnapshot.child("image").getValue(String.class), dataSnapshot.child("actualprice").getValue(String.class), dataSnapshot.child("discountedprice").getValue(String.class), dataSnapshot.getKey()));
                }

                productslider.setAdapter(new ProductAdapter(products));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productsitem, parent, false);
            return new ProductsViewHolder(view);
        }

        @Override
        public void bindVH(@NotNull final ProductsViewHolder holder, final int i) {

            if (i == 0) {
                circularProgressBar.setVisibility(View.GONE);
                productslider.scheduleLayoutAnimation();
            }

            holder.title.setText(Products.get(i).getTitle());
            Glide.with(getActivity()).load(Products.get(i).getImage()).apply(new RequestOptions().override(800, 500)).into(holder.imageView);
            holder.discountedprice.setText("₹" + Products.get(i).getDiscountedprice() + "/mo");
            holder.actualprice.setText("₹" + Products.get(i).getActualprice());
            holder.content.setText(Products.get(i).getContent());
            holder.actualprice.setPaintFlags(holder.actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Log.i("id", Products.get(i).getId());

            if (Products.get(i).getTitle().equals("Customized Plan")) {
                holder.viewplan.setText("Customize");
                holder.actualprice.setText("");
                holder.discountedprice.setText("");

            }

            holder.viewplan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Products.get(i).getTitle().equals("Customized Plan")) {
                        Intent intent = new Intent(getActivity(), CustomizePlan.class);
                        startActivity(intent);

                    } else {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation
                                (getActivity(), holder.imageView, "imageMain");
                        Intent intent = new Intent(getActivity(), ViewProduct.class);
                        intent.putExtra("id", Products.get(i).getId());
                        startActivity(intent, activityOptionsCompat.toBundle());
                    }
                }
            });

            final Query query = FirebaseDatabase.getInstance().getReference().child("Products").child(Products.get(i).getId()).child("benefits");

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
                }

                @NonNull
                @Override
                public BenefitsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.benefitsitem, parent, false);
                    return new BenefitsViewHolder(view);
                }

                @Override
                public int getItemCount() {
                    if (super.getItemCount() > 5) {
                        return 5;
                    } else {
                        return super.getItemCount();
                    }
                }
            };

            holder.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            holder.recyclerView.setAdapter(firebaseRecyclerAdapter);


            if (firebaseRecyclerAdapter != null) {
                firebaseRecyclerAdapter.startListening();
            }
        }

    }


    private class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView title, content, discountedprice, actualprice, viewplan;
        ImageView imageView;
        RecyclerView recyclerView;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.image);
            actualprice = itemView.findViewById(R.id.actualprice);
            discountedprice = itemView.findViewById(R.id.discountedprice);
            recyclerView = itemView.findViewById(R.id.benefitsrecyclerview);
            viewplan = itemView.findViewById(R.id.viewplan);
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
    public void onResume() {
        super.onResume();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }

}