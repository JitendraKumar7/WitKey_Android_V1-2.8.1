package app.witkey.live.fragments.dashboard.stream;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.StreamEndSummaryBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.customviews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiveFeedEndedFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.backBN)
    CustomTextView backBN;
    @BindView(R.id.streamTime)
    CustomTextView streamTime;
    @BindView(R.id.streamEndedTotalViewers)
    CustomTextView streamEndedTotalViewers;
    @BindView(R.id.streamEndedNewFans)
    CustomTextView streamEndedNewFans;
    @BindView(R.id.oldViewers)
    CustomTextView oldViewers;
    @BindView(R.id.oldFans)
    CustomTextView oldFans;
    @BindView(R.id.oldWitkeyDollar)
    CustomTextView oldWitkeyDollar;
    @BindView(R.id.streamEndedWitKeyDollars)
    CustomTextView streamEndedWitKeyDollars;
    @BindView(R.id.streamEndedpopularity)
    CustomTextView streamEndedpopularity;

    StreamEndSummaryBO streamEndSummaryBO;

    public static LiveFeedEndedFragment newInstance(StreamEndSummaryBO streamEndSummaryBO) {
        Bundle args = new Bundle();
        args.putParcelable("STREAMENDBO", streamEndSummaryBO);
        LiveFeedEndedFragment fragment = new LiveFeedEndedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_live_feed_ended, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if (getArguments() != null) {
            streamEndSummaryBO = getArguments().getParcelable("STREAMENDBO");
        }

        initView();
        populateStreamEndData();
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        backBN.setOnClickListener(this);
    }

    // METHOD TO POPULATE END STREAM VIEW
    private void populateStreamEndData() {

        try {
            if (streamEndSummaryBO != null) {
                streamTime.setText(streamEndSummaryBO.getEndStreamSummary());
                streamEndedTotalViewers.setText(streamEndSummaryBO.getTotal_viewrs() + "");
                streamEndedNewFans.setText(streamEndSummaryBO.getNew_fan() + "");
                streamEndedWitKeyDollars.setText(streamEndSummaryBO.getWitkey_dollar() + "");
                streamEndedpopularity.setText(streamEndSummaryBO.getPopularity() + "");
                oldViewers.setText(getString(R.string.viewers_75) + ": " + streamEndSummaryBO.getTotal_viewers_last_24() + "");
                oldFans.setText(getString(R.string.new_fans_0) + ": " + streamEndSummaryBO.getTotal_fan_last_24() + "");
                oldWitkeyDollar.setText(getString(R.string.witkey_dollars) + ": " + streamEndSummaryBO.getWitkey_dollar_last_24() + "");
            }
        } catch (Exception e) {
            LogUtils.e("populateStreamEndData", "" + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBN:
                getActivity().finish();
                break;
        }
    }
}
