package app.witkey.live.fragments.dashboard.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.goLiveTutorial)
    LinearLayout goLiveTutorial;

    @BindView(R.id.generalAppTutorial)
    LinearLayout generalAppTutorial;


    public static TutorialFragment newInstance() {
        Bundle args = new Bundle();
        TutorialFragment fragment = new TutorialFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        goLiveTutorial.setOnClickListener(this);
        generalAppTutorial.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goLiveTutorial:
                GlobalBus.getBus().post(new Events.TutorialFragmentToHomeFragmentMessage("GO_LIVE"));
                //gotoNextFragment(TutorialScreenViewFragment.newInstance(false));
                break;

            case R.id.generalAppTutorial:
                GlobalBus.getBus().post(new Events.TutorialFragmentToHomeFragmentMessage("GENERAL_APP"));
                //gotoNextFragment(TutorialScreenViewFragment.newInstance(true));
                break;
        }
    }
}