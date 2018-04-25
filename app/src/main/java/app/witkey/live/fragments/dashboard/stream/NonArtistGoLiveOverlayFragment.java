package app.witkey.live.fragments.dashboard.stream;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NonArtistGoLiveOverlayFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.goLiveCancelBTN)
    ImageView goLiveCancelBTN;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static NonArtistGoLiveOverlayFragment newInstance() {
        Bundle args = new Bundle();
        NonArtistGoLiveOverlayFragment fragment = new NonArtistGoLiveOverlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_go_live_non_artist_overlay, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initViews();
    }

    private void initViews() {
        goLiveCancelBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goLiveCancelBTN:
                goBack();
                break;
        }
    }

    private void goBack() {
        getActivity().onBackPressed();
    }
}