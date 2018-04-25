package app.witkey.live.fragments.dashboard.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import app.witkey.live.adapters.dashboard.profile.MyProfileAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.home.broadcasters.BroadcastersFragment;
import app.witkey.live.fragments.dashboard.profile.profilesetting.ProfileSettingFragment;
import app.witkey.live.fragments.dashboard.profile.systemsetting.SystemSettingFragment;
import app.witkey.live.items.FeaturedBO;
import app.witkey.live.items.ProfileItemsBO;
import app.witkey.live.items.UserBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edited by developer on 10/03/2017.
 */

public class UserProfileFragment_old extends BaseFragment implements View.OnClickListener, MyProfileAdapter.ClickListeners {

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;
    // TODO HAVE TO REMOVE THIS CLASS
    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnSettings)
    ImageView btnSettings;

    @BindView(R.id.userProfileImageView)
    ImageView userProfileImageView;

    @BindView(R.id.giftsCountTextView)
    CustomTextView giftsCountTextView;
    @BindView(R.id.fanBaseCountTextView)
    CustomTextView fanBaseCountTextView;
    @BindView(R.id.userStatusTextView)
    CustomTextView userStatusTextView;

    @BindView(R.id.userLevelBoxParentFrame)
    RelativeLayout userLevelBoxParentFrame;
    @BindView(R.id.userLevelBoxTextView)
    CustomTextView userLevelBoxTextView;

    @BindView(R.id.diamondBoxParentFrame)
    RelativeLayout diamondBoxParentFrame;
    @BindView(R.id.diamondsCountBoxTextView)
    CustomTextView diamondsCountBoxTextView;

    @BindView(R.id.includedLayoutProfile)
    View includedLayoutProfile;

    @BindView(R.id.momentsProfileToggleParentFrame)
    RadioGroup momentsProfileToggleParentFrame;

    @BindView(R.id.momentsParentFrame)
    RelativeLayout momentsParentFrame;
    @BindView(R.id.profileParentFrame)
    RelativeLayout profileParentFrame;

    @BindView(R.id.profileRecyclerView)
    RecyclerView profileRecyclerView;
    @BindView(R.id.optionView)
    LinearLayout optionView;

    @BindView(R.id.profileSettingOption)
    CustomTextView profileSettingOption;

    @BindView(R.id.systemSettingOption)
    CustomTextView systemSettingOption;

    private MyProfileAdapter myProfileAdapter;
    private List<ProfileItemsBO> profileItemsBOs;
    private Animations mProfileSettingAnim;
    private UserBO userBO;

    @BindView(R.id.noMomentsParentFrame)
    RelativeLayout noMomentsParentFrame;
    @BindView(R.id.momentsRecyclerView)
    RecyclerView momentsRecyclerView;

    private FeaturedAdapter featuredAdapter;
    private List<FeaturedBO> featuredList;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static UserProfileFragment_old newInstance() {
        Bundle args = new Bundle();
        UserProfileFragment_old fragment = new UserProfileFragment_old();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews();

        setUpFeaturedData();
        // TODO HERE
//        setUpFeaturedRecycler(featuredList);

        setUpData();
        setUpProfileRecycler(profileItemsBOs);
        populateUserProfileData();
    }

    private void setUpData() {

        profileItemsBOs = new ArrayList<>();

        profileItemsBOs.add(new ProfileItemsBO("WITKEY DOLLARS EARNED", R.drawable.dollar, "217,988"));
        profileItemsBOs.add(new ProfileItemsBO("STORED CHIPS VALUE", R.drawable.chip, "32,000"));
        profileItemsBOs.add(new ProfileItemsBO("USER LEVEL", R.drawable.moment, "110"));
        profileItemsBOs.add(new ProfileItemsBO("BROADCASTER RANK", R.drawable.diamond, "150"));
    }

    private void setUpProfileRecycler(final List<ProfileItemsBO> profileItemsBOs) {

        if (profileItemsBOs != null && profileItemsBOs.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            profileRecyclerView.setItemAnimator(new DefaultItemAnimator());
            profileRecyclerView.setLayoutManager(linearLayoutManager);
            myProfileAdapter = new MyProfileAdapter(profileItemsBOs, getContext(), profileRecyclerView);
            profileRecyclerView.setAdapter(myProfileAdapter);

            myProfileAdapter.setClickListener(this);
        } else {
            //showNoResult(true);
        }

    }

    private void initViews() {

        btnBack.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

        giftsCountTextView.setOnClickListener(this);
        fanBaseCountTextView.setOnClickListener(this);

        userLevelBoxParentFrame.setOnClickListener(this);
        diamondBoxParentFrame.setOnClickListener(this);

        mProfileSettingAnim = new Animations();
        systemSettingOption.setOnClickListener(this);
        profileSettingOption.setOnClickListener(this);
        optionView.setOnClickListener(this);

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
                break;
            case R.id.btnSettings:
                mProfileSettingAnim.performScaleAnimation(getActivity(), includedLayoutProfile, 0.95f, 0.0f);
                break;

            case R.id.giftsCountTextView:
                break;
            case R.id.fanBaseCountTextView:
                gotoNextFragment(MyFansFragment.newInstance());
                break;

            case R.id.userLevelBoxParentFrame:
                break;
            case R.id.diamondBoxParentFrame:
                break;

            case R.id.systemSettingOption:
                gotoNextFragment(SystemSettingFragment.newInstance());
                break;
            case R.id.profileSettingOption:
                gotoNextFragment(ProfileSettingFragment.newInstance());
                break;
            case R.id.optionView:
                mProfileSettingAnim.performScaleAnimation(getActivity(), includedLayoutProfile, 0.95f, 0.0f);
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

    @Override
    public void onRowClick(int position) {
        if (position == 0) {
            gotoNextFragment(BroadcastersFragment.newInstance());
        } else if (position == 1) {
            gotoNextFragment(MyWalletFragment.newInstance());
        } else if (position == 2) {
            gotoNextFragment(UserLevelFragment.newInstance());
        } else if (position == 3) {
            startActivity(new Intent(getActivity(), BroadcastRankActivity.class));
        }
    }

    // METHOD TO POPULATE ALL USER PROFILE DATA
    private void populateUserProfileData() {

        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        if (userBO != null) {
            profileNameTextView.setText(userBO.getUsername());
            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
            Utils.setImageSimple(userProfileImageView, userBO.getProfilePictureUrl(), getActivity());
        }

    }

    private void setUpFeaturedData() {
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