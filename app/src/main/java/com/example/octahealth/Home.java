package com.example.octahealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.octahealth.NaviagationFragment.Blogs;
import com.example.octahealth.NaviagationFragment.Offers;
import com.example.octahealth.NaviagationFragment.Products;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

public class Home extends AppCompatActivity {

    BubbleNavigationConstraintView bubbleNavigationConstraintView;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bubbleNavigationConstraintView=findViewById(R.id.navigation);
        constraintLayout=findViewById(R.id.cons);

        Fragment fragment=new Blogs();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.cons, fragment)
                .commit();



        bubbleNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                Fragment fragment=null;
                FragmentManager fragmentManager = getSupportFragmentManager();
                switch(position) {
                    default:
                    case 0:
                        fragment = new Blogs();
                        break;
                    case 1:
                        fragment = new Products();
                        break;
                    case 2 :
                        fragment = new Offers();
                        break;
                }
                fragmentManager.beginTransaction()
                        .replace(R.id.cons, fragment)
                        .commit();


            }
        });
    }
}