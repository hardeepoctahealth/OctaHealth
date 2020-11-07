package com.example.octahealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.octahealth.NaviagationFragment.Blogs;
import com.example.octahealth.NaviagationFragment.HomeFrag;
import com.example.octahealth.NaviagationFragment.MyPlans;
import com.example.octahealth.NaviagationFragment.Offers;
import com.example.octahealth.NaviagationFragment.Products;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleToggleView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.irfaan008.irbottomnavigation.SpaceItem;
import com.irfaan008.irbottomnavigation.SpaceNavigationView;
import com.irfaan008.irbottomnavigation.SpaceOnClickListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import static maes.tech.intentanim.CustomIntent.customType;

public class Home extends AppCompatActivity {

    FrameLayout frameLayout;
    SpaceNavigationView spaceNavigationView;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        frameLayout = findViewById(R.id.cons);

        spaceNavigationView = findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Articles", R.drawable.blog));
        spaceNavigationView.addSpaceItem(new SpaceItem("Products", R.drawable.products1));
        spaceNavigationView.addSpaceItem(new SpaceItem("Offers", R.drawable.offers1));
        spaceNavigationView.addSpaceItem(new SpaceItem("My Plans", R.drawable.myplansicon));

        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.showIconOnly();
        spaceNavigationView.setCentreButtonSelectable(true);


        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Fragment fragment = new HomeFrag();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                        .replace(R.id.cons, fragment)
                        .commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (itemIndex) {
                    default:
                    case 0:
                        fragment = new Blogs();
                        break;
                    case 1:
                        fragment = new Products();
                        break;
                    case 2:
                        fragment = new Offers();
                        break;
                    case 3:
                        fragment = new MyPlans();
                        break;

                }
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                        .replace(R.id.cons, fragment)
                        .commit();

            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                spaceNavigationView.setCentreButtonSelected();
            }
        }, 100);


        Fragment fragment = new HomeFrag();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.cons, fragment)
                .commit();


     /*   bubbleNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                Fragment fragment = null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (position) {
                    default:
                    case 0:
                        fragment = new Blogs();
                        break;
                    case 1:
                        fragment = new Products();
                        break;
                    case 2:
                        fragment = new HomeFrag();
                        break;
                    case 3:
                        fragment = new Offers();
                        break;
                    case 4:
                        fragment = new MyPlans();
                        break;
                }
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                        .replace(R.id.cons, fragment)
                        .commit();

            }
        });*/
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("Profiles").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Myplans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);*/
    }
}