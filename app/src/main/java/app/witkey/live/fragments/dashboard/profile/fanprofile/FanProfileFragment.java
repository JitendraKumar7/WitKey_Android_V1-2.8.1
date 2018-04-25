package app.witkey.live.fragments.dashboard.profile.fanprofile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.home.FeaturedAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.FeaturedBO;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FanProfileFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;

    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;
    // TODO HAVE TO REMOVE THIS CLASS
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnFilter)
    ImageView btnFilter;

    @BindView(R.id.momentsParentFrame)
    RelativeLayout momentsParentFrame;
    @BindView(R.id.profileParentFrame)
    LinearLayout profileParentFrame;

    @BindView(R.id.momentsProfileToggleParentFrame)
    RadioGroup momentsProfileToggleParentFrame;

    @BindView(R.id.noMomentsParentFrame)
    RelativeLayout noMomentsParentFrame;
    @BindView(R.id.momentsRecyclerView)
    RecyclerView momentsRecyclerView;

    @BindView(R.id.messageParentFrame)
    LinearLayout messageParentFrame;
    @BindView(R.id.followParentFrame)
    LinearLayout followParentFrame;

    private FeaturedAdapter featuredAdapter;
    private List<FeaturedBO> featuredList;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static FanProfileFragment newInstance() {
        Bundle args = new Bundle();
        FanProfileFragment fragment = new FanProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fan_profile, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews();

        setUpData();
        // TODO SET HERE
//        setUpFeaturedRecycler(featuredList);
    }

    private void initViews() {

        btnBack.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        messageParentFrame.setOnClickListener(this);
        followParentFrame.setOnClickListener(this);

        momentsProfileToggleParentFrame.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {

                    case R.id.myMomentsRB:
                        momentsProfileToggle(true);
                        break;
                    case R.id.myProfileRB:
                        momentsProfileToggle(false);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnFilter:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.followParentFrame:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;

            case R.id.messageParentFrame:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
//                MainFragment.newInstance().setSelectedPage(2);
//                getActivity().onBackPressed();
                break;
        }
    }

    private void momentsProfileToggle(boolean showMoments) {
        if (showMoments) {
            momentsParentFrame.setVisibility(View.VISIBLE);
            profileParentFrame.setVisibility(View.GONE);
        } else {
            momentsParentFrame.setVisibility(View.GONE);
            profileParentFrame.setVisibility(View.VISIBLE);
        }
    }

    private void setUpData() {
        featuredList = new ArrayList<>();

        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 1", "11 hrs ago", "10K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 2", "12 hrs ago", "11K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 3", "13 hrs ago", "12K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 4", "14 hrs ago", "13K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 5", "15 hrs ago", "14K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 6", "16 hrs ago", "15K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 7", "17 hrs ago", "16K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
    }

    /*private void setUpFeaturedRecycler(List<FeaturedBO> featuredList) {

        if (featuredList != null && featuredList.size() > 0) {
            momentsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            momentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            featuredAdapter = new FeaturedAdapter(featuredList, context, momentsRecyclerView);
            momentsRecyclerView.setAdapter(featuredAdapter);

            featuredAdapter.setClickListener(new FeaturedAdapter.ClickListeners() {
                @Override
                public void onRowClick(int position) {
                    //gotoNextFragment(FanProfileFragment.newInstance());
                }
            });

        } else {
            showNoResult(true);
        }

    }*/

    private void showNoResult(boolean isShow) {
        if (isShow) {
            noMomentsParentFrame.setVisibility(View.VISIBLE);
        } else {
            noMomentsParentFrame.setVisibility(View.GONE);
            momentsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}