package com.example.octahealth.NaviagationFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.octahealth.OfferDetails;
import com.example.octahealth.R;
import com.github.islamkhsh.CardSliderAdapter;
import com.github.islamkhsh.CardSliderViewPager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class Offers extends Fragment {


    ArrayList<OfferDetails> details;
    CircularProgressBar circularProgressBar;

    public Offers() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_offers, container, false);

        final CardSliderViewPager cardSliderViewPager = view.findViewById(R.id.viewPager);
        circularProgressBar=view.findViewById(R.id.circularProgressBar);

        details=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Offers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    details.add(new OfferDetails(dataSnapshot.child("title").getValue(String.class),dataSnapshot.child("image").getValue(String.class),dataSnapshot.child("code").getValue(String.class)));
                }

                Log.i("det",details.get(0).getTitle());
                cardSliderViewPager.setAdapter(new OfferAdapter(details));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // add items to arraylist


        return view;
    }

    public class OfferAdapter extends CardSliderAdapter<OfferAdapter.OfferViewHolder> {

        private ArrayList<OfferDetails> offers;

        public OfferAdapter(ArrayList<OfferDetails> offers){
            this.offers = offers;
        }

        @Override
        public int getItemCount(){
            return offers.size();
        }

        @Override
        public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_item, parent, false);
            return new OfferViewHolder(view);
        }

        @Override
        public void bindVH(@NotNull OfferViewHolder holder, int i) {

            if(i==0)
            {
                circularProgressBar.setVisibility(View.GONE);
            }

            holder.code.setText(offers.get(i).getCode());
            holder.title.setText(offers.get(i).getTitle());
            Glide.with(getActivity()).load(offers.get(i).getImage()).apply(new RequestOptions().override(800, 500)).into(holder.image);

        }

        class OfferViewHolder extends RecyclerView.ViewHolder {

            TextView code,title;
            ImageView image;
            
            public OfferViewHolder(View view){
                super(view);
                
                code=view.findViewById(R.id.code);
                image=view.findViewById(R.id.image);
                title=view.findViewById(R.id.title);
            }
        }
    }
}