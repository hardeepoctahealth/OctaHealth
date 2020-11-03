package com.example.octahealth.NaviagationFragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.Benefit;
import com.example.octahealth.Choose;
import com.example.octahealth.OfferDetails;
import com.example.octahealth.Profile;
import com.example.octahealth.R;
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
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Offers extends Fragment {


    ArrayList<OfferDetails> details;
    CircularProgressBar circularProgressBar;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter<Benefit, OffersHolder> firebaseRecyclerAdapter;
    String premium;

    public Offers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        circularProgressBar = view.findViewById(R.id.circularProgressBar);

        details = new ArrayList<>();
        final ImageView profile = view.findViewById(R.id.profilepic);
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
        recyclerView = view.findViewById(R.id.offersrecyclerview);

        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Myplans").exists()) {
                    premium = "yes";
                } else {
                    premium = "no";
                }
                if (getActivity() != null)
                    Glide.with(getActivity()).load(snapshot.child("profilepic").getValue(String.class)).apply(new RequestOptions().override(200, 200)).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        final Query query = FirebaseDatabase.getInstance().getReference().child("Offers");

        FirebaseRecyclerOptions<Benefit> options = new FirebaseRecyclerOptions.Builder<Benefit>()
                .setQuery(query, new SnapshotParser<Benefit>() {
                    @NonNull
                    @Override
                    public Benefit parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Benefit(snapshot.getKey());
                    }
                }).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Benefit, OffersHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OffersHolder holder, int position, @NonNull Benefit model) {

                holder.title.setText(model.getTitle());

                FirebaseDatabase.getInstance().getReference().child("Offers").child(model.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        details.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (premium.equals("yes"))
                                details.add(new OfferDetails(dataSnapshot.child("title").getValue(String.class), dataSnapshot.child("image").getValue(String.class), dataSnapshot.child("code").getValue(String.class)));
                            else {
                                if (dataSnapshot.child("premium").getValue(String.class).equals("no")) {
                                    details.add(new OfferDetails(dataSnapshot.child("title").getValue(String.class), dataSnapshot.child("image").getValue(String.class), dataSnapshot.child("code").getValue(String.class)));
                                }
                            }
                        }

                        holder.cardSliderViewPager.setAdapter(new OfferAdapter(details));


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @NonNull
            @Override
            public OffersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.offersitem, parent, false);
                return new OffersHolder(view1);
            }
        };


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    private class OffersHolder extends RecyclerView.ViewHolder {

        TextView title;
        CardSliderViewPager cardSliderViewPager;

        public OffersHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            cardSliderViewPager = itemView.findViewById(R.id.viewPager);
        }
    }

    public class OfferAdapter extends CardSliderAdapter<OfferAdapter.OfferViewHolder> {

        private ArrayList<OfferDetails> offers;

        public OfferAdapter(ArrayList<OfferDetails> offers) {
            this.offers = offers;
        }

        @Override
        public int getItemCount() {
            return offers.size();
        }

        @Override
        public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
            return new OfferViewHolder(view);
        }

        @Override
        public void bindVH(@NotNull final OfferViewHolder holder, final int i) {

            if (i == 0) {
                circularProgressBar.setVisibility(View.GONE);
            }

            holder.code.setText(offers.get(i).getCode());
            holder.title.setText(offers.get(i).getTitle());
            Glide.with(getActivity()).load(offers.get(i).getImage()).apply(new RequestOptions().override(800, 500)).into(holder.image);

            holder.code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Code", offers.get(i).getCode());
                    clipboard.setPrimaryClip(clip);

                    Toast.makeText(getActivity(), "Code Copied", Toast.LENGTH_SHORT).show();
                }
            });

        }

        class OfferViewHolder extends RecyclerView.ViewHolder {

            TextView code, title;
            ImageView image;

            public OfferViewHolder(View view) {
                super(view);

                code = view.findViewById(R.id.code);
                image = view.findViewById(R.id.image);
                title = view.findViewById(R.id.title);
            }
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