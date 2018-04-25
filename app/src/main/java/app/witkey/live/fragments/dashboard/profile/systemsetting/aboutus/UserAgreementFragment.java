package app.witkey.live.fragments.dashboard.profile.systemsetting.aboutus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserAgreementFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;
// TODO HAVE TO REMOVE THIS CLASS
    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static UserAgreementFragment newInstance() {
        Bundle args = new Bundle();
        UserAgreementFragment fragment = new UserAgreementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_agreement, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setTitleBarData();
        showHideView();
        initViews();
    }

    private void initViews() {
        btnBack.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.user_agreement);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
        }
    }
}