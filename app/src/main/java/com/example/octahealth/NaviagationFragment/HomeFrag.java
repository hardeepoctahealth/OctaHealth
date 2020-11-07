package com.example.octahealth.NaviagationFragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.CarouselItem;
import com.example.octahealth.Choose;
import com.example.octahealth.Home;
import com.example.octahealth.Profile;
import com.example.octahealth.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irfaan008.irbottomnavigation.SpaceNavigationView;
import com.jama.carouselview.CarouselView;
import com.jama.carouselview.CarouselViewListener;
import com.jama.carouselview.enums.IndicatorAnimationType;
import com.jama.carouselview.enums.OffsetType;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import static maes.tech.intentanim.CustomIntent.customType;

public class HomeFrag extends Fragment {


    ConstraintLayout articles, offers, products;
    ArrayList<CarouselItem> articless, offerss, productss;
    CarouselView articlecar, offerscar, productscar;
    ShimmerFrameLayout shimmer1, shimmer2, shimmer3;
    ImageView articlesviewall, offersviewall, productsviewall;
    SpaceNavigationView spaceNavigationView;

    public HomeFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        spaceNavigationView = getActivity().findViewById(R.id.space);

        final ImageView profile = view.findViewById(R.id.profilepic);
        final ImageView switchwindow=view.findViewById(R.id.switchwindow);
        switchwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), Choose.class));
                getActivity().finish(); customType(getActivity(),"fadein-to-fadeout");
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
                customType(getActivity(),"fadein-to-fadeout");

            }
        });
        articles = view.findViewById(R.id.articlescons);
        offers = view.findViewById(R.id.offerscons);
        products = view.findViewById(R.id.productscons);

         shimmer1 = view.findViewById(R.id.shimmer);
        shimmer2 = view.findViewById(R.id.shimmer2);
        shimmer3 = view.findViewById(R.id.shimmer3);

        shimmer1.startShimmer();
        shimmer2.startShimmer();
        shimmer3.startShimmer();

        articlecar = view.findViewById(R.id.articlescarview);
        offerscar = view.findViewById(R.id.offerscarview);
        productscar = view.findViewById(R.id.productscarview);

        articlesviewall = view.findViewById(R.id.articlesviewall);
        offersviewall = view.findViewById(R.id.offersviewall);
        productsviewall = view.findViewById(R.id.productsviewall);


        articles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Blogs();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                        .replace(R.id.cons, fragment)
                        .commit();
            }
        });

        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new Offers();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                        .replace(R.id.cons, fragment)
                        .commit();
            }
        });

        products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Products();
                FragmentManager fragmentManager = getParentFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                        .replace(R.id.cons, fragment)
                        .commit();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Blogs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                articless = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    CarouselItem carouselItem = new CarouselItem(snapshot1.child("title").getValue(String.class), snapshot1.child("image").getValue(String.class));

                    articless.add(carouselItem);
                }

                articlecar.setVisibility(View.VISIBLE);
                shimmer1.setVisibility(View.GONE);
                articlecar.scheduleLayoutAnimation();
                articlecar.setSize(articless.size());
                articlecar.setResource(R.layout.image_carousel_item);
                articlecar.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
                articlecar.setCarouselOffset(OffsetType.START);
                articlecar.hideIndicator(true);
                articlecar.setCarouselViewListener(new CarouselViewListener() {
                    @Override
                    public void onBindView(View view, final int position) {


                        // Example here is setting up a full image carousel
                        final ImageView imageView = view.findViewById(R.id.image);
                        TextView title = view.findViewById(R.id.title);


                        title.setText(articless.get(position).getTitle());
                        Glide.with(getActivity()).load(articless.get(position).getImage()).into(imageView);


                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fragment = new Blogs();
                                FragmentManager fragmentManager = getParentFragmentManager();
                                fragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                                        .replace(R.id.cons, fragment)
                                        .commit();
                            }
                        });

                    }
                });

                articlecar.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                productss = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    CarouselItem carouselItem = new CarouselItem(snapshot1.child("title").getValue(String.class), snapshot1.child("image").getValue(String.class));

                    productss.add(carouselItem);
                }

                productscar.setVisibility(View.VISIBLE);
                shimmer2.setVisibility(View.GONE);
                productscar.scheduleLayoutAnimation();

                productscar.setSize(productss.size());
                productscar.setResource(R.layout.image_carousel_item);
                productscar.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
                productscar.setCarouselOffset(OffsetType.START);
                productscar.hideIndicator(true);
                productscar.setCarouselViewListener(new CarouselViewListener() {
                    @Override
                    public void onBindView(View view, final int position) {
                        // Example here is setting up a full image carousel
                        final ImageView imageView = view.findViewById(R.id.image);
                        TextView title = view.findViewById(R.id.title);


                        title.setText(productss.get(position).getTitle());
                        Glide.with(getActivity()).load(productss.get(position).getImage()).into(imageView);


                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fragment = new Products();
                                FragmentManager fragmentManager = getParentFragmentManager();
                                fragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                                        .replace(R.id.cons, fragment)
                                        .commit();
                            }
                        });

                    }
                });

                productscar.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseDatabase.getInstance().getReference().child("Offers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                offerss = new ArrayList<>();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        CarouselItem carouselItem = new CarouselItem(snapshot2.child("title").getValue(String.class), snapshot2.child("image").getValue(String.class));

                        offerss.add(carouselItem);
                    }
                }

                offerscar.setVisibility(View.VISIBLE);
                shimmer3.setVisibility(View.GONE);
                offerscar.scheduleLayoutAnimation();

                offerscar.setSize(offerss.size());
                offerscar.setResource(R.layout.image_carousel_item);
                offerscar.setIndicatorAnimationType(IndicatorAnimationType.THIN_WORM);
                offerscar.setCarouselOffset(OffsetType.START);
                offerscar.hideIndicator(true);
                offerscar.setCarouselViewListener(new CarouselViewListener() {
                    @Override
                    public void onBindView(View view, final int position) {
                        // Example here is setting up a full image carousel
                        final ImageView imageView = view.findViewById(R.id.image);
                        TextView title = view.findViewById(R.id.title);


                        title.setText(offerss.get(position).getTitle());
                        Glide.with(getActivity()).load(offerss.get(position).getImage()).into(imageView);


                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Fragment fragment = new Offers();
                                FragmentManager fragmentManager = getParentFragmentManager();
                                fragmentManager.beginTransaction()
                                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                                        .replace(R.id.cons, fragment)
                                        .commit();
                            }
                        });

                    }
                });

                offerscar.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

}