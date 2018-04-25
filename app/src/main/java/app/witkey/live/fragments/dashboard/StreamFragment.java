package app.witkey.live.fragments.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.stream.GoLiveStartPageFragment;

public class StreamFragment extends BaseFragment {

    private Context context;
    private String TAG = this.getClass().getSimpleName();
// TODO HAVE TO REMOVE THIS CLASS

    public static StreamFragment newInstance() {
        Bundle args = new Bundle();
        StreamFragment fragment = new StreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stream, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //addMainFragment();
    }

    private void addMainFragment() {

        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainer, GoLiveStartPageFragment.newInstance());
        ft.commit();
    }
}