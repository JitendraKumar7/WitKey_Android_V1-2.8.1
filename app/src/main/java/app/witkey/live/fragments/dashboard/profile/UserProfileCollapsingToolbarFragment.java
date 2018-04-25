package app.witkey.live.fragments.dashboard.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import butterknife.ButterKnife;

public class UserProfileCollapsingToolbarFragment extends BaseFragment implements View.OnClickListener {

    Context context;

    public static UserProfileCollapsingToolbarFragment newInstance() {
        Bundle args = new Bundle();
        UserProfileCollapsingToolbarFragment fragment = new UserProfileCollapsingToolbarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_profile_collapsing_toolbar, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onClick(View v) {
    }
}