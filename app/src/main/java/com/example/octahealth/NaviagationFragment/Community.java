package com.example.octahealth.NaviagationFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.octahealth.NaviagationFragment.CommunityFrags.Feed;
import com.example.octahealth.NaviagationFragment.CommunityFrags.QnA;
import com.example.octahealth.R;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;


public class Community extends Fragment {

    BubbleNavigationConstraintView bubbleNavigationConstraintView;


    public Community() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_community, container, false);

        bubbleNavigationConstraintView=view.findViewById(R.id.navigation);

        Fragment fragment=new Blogs();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.cons, fragment)
                .commit();



        bubbleNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {

                Fragment fragment=null;
                FragmentManager fragmentManager = getFragmentManager();
                switch(position) {
                    default:
                    case 0:
                        fragment = new Feed();
                        break;
                    case 1:
                        fragment = new QnA();
                        break;

                }
                fragmentManager.beginTransaction()
                        .replace(R.id.cons, fragment)
                        .commit();


            }
        });

        return view;
    }
}