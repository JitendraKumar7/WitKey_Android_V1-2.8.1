package app.witkey.live.fragments.dashboard.profile.systemsetting;

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
import app.witkey.live.fragments.dashboard.profile.systemsetting.aboutus.PrivacyPolicyFragment;
import app.witkey.live.utils.EnumUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.privacyPolicyParentFrame)
    LinearLayout privacyPolicyParentFrame;
    @BindView(R.id.userAgreementParentFrame)
    LinearLayout userAgreementParentFrame;
    @BindView(R.id.aboutOurCompanyParentFrame)
    LinearLayout aboutOurCompanyParentFrame;
    @BindView(R.id.communityConventionParentFrame)
    LinearLayout communityConventionParentFrame;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static AboutUsFragment newInstance() {
        Bundle args = new Bundle();
        AboutUsFragment fragment = new AboutUsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_about_us, container, false);
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
        privacyPolicyParentFrame.setOnClickListener(this);
        userAgreementParentFrame.setOnClickListener(this);
        aboutOurCompanyParentFrame.setOnClickListener(this);
        communityConventionParentFrame.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.about_us_capital);
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
            case R.id.privacyPolicyParentFrame:
                gotoNextFragment(PrivacyPolicyFragment.newInstance(EnumUtils.AboutUs_Type.PRIVACY_POLICY));
                break;
            case R.id.userAgreementParentFrame:
                gotoNextFragment(PrivacyPolicyFragment.newInstance(EnumUtils.AboutUs_Type.USER_AGREEMENT));
                break;
            case R.id.aboutOurCompanyParentFrame:
                gotoNextFragment(PrivacyPolicyFragment.newInstance(EnumUtils.AboutUs_Type.ABOUT_OUR_COMPANY));
                break;
            case R.id.communityConventionParentFrame:
               // gotoNextFragment(PrivacyPolicyFragment.newInstance(EnumUtils.AboutUs_Type.COMMUNITY_CONVENTION));
                break;
        }
    }
}