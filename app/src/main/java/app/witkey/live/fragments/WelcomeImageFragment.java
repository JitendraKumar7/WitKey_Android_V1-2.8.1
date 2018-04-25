package app.witkey.live.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.witkey.live.R;
import app.witkey.live.items.SlidingObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class WelcomeImageFragment extends Fragment {


    SlidingObject slidingObject;


    public WelcomeImageFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(SlidingObject slidingObject) {
        WelcomeImageFragment fragment = new WelcomeImageFragment();
        Bundle args = new Bundle();
        args.putSerializable("slidingObject", slidingObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            slidingObject = (SlidingObject) getArguments().getSerializable("slidingObject");
        } else
            slidingObject = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_welcome, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (slidingObject != null) {

        }
    }

}
