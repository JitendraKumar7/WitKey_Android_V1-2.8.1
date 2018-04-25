package app.witkey.live.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.augustopicciani.drawablepageindicator.interfaces.PageIndicator;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import app.witkey.live.R;

import app.witkey.live.activities.GoLiveStartPageActivity;
import app.witkey.live.adapters.ViewPagerAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.stream.NonArtistGoLiveOverlayFragment;
import app.witkey.live.items.GiftPanelItemBO;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.SmoothViewPager;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.mainPager)
    SmoothViewPager mViewPager;

    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;

    @BindView(R.id.mTabContainer)
    FrameLayout bottomTab;
    static int mSelectedPager = 0;
    static View bottomTabView;
    static ImageView bottomTabHome_;
    static ImageView bottomTabLive_;
    static ImageView bottomTabMessage_;
    static ImageView bottomTabProfile_;
    static ImageView bottomTabGolive_;

    AnimationDrawable frameAnimation;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout container;

    private ViewPagerAdapter adapter;
    public static int HOME = 0;
    public static int FAVORITES = 1;
    public static int GOLIVE = 2;
    public static int MESSAGE = 3;
    public static int PROFILE = 4;


    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {

            bottomTabView = bottomTab;
            initView(view);
            setUpAdapter();
            initTabIcons();
//        setBottomTabMargin();
            /*if (getArguments() != null) {
                int toIndex = getArguments().getInt("TO_INDEX");
                setSelectedPage(toIndex);
            }*/

        } catch (Exception e) {
            LogUtils.e("GoLiveBroadcastGiftPanelDialog", "" + e.getMessage());
        }


    }

    private void initView(View view) {

        bottomTabHome_ = (ImageView) view.findViewById(R.id.bottomTabHome);
        bottomTabHome_.setOnClickListener(this);
        bottomTabLive_ = (ImageView) view.findViewById(R.id.bottomTabLive);
        bottomTabLive_.setOnClickListener(this);
        bottomTabMessage_ = (ImageView) view.findViewById(R.id.bottomTabMessage);
        bottomTabMessage_.setOnClickListener(this);
        bottomTabProfile_ = (ImageView) view.findViewById(R.id.bottomTabProfile);
        bottomTabProfile_.setOnClickListener(this);
        bottomTabGolive_ = (ImageView) view.findViewById(R.id.bottomTabGolive);
        bottomTabGolive_.setOnClickListener(this);

        setTabSelectionIndex(HOME);

        //bottomTabGolive_.setBackgroundResource(R.drawable.animation_list);
        //frameAnimation = (AnimationDrawable) bottomTabGolive_.getBackground();
        //frameAnimation.start();

        /*container.setBaseAlpha(0.4f);
        container.setIntensity(0.35f);
        container.setMaskShape(ShimmerFrameLayout.MaskShape.RADIAL);
//        container.setDropoff(0.1f);
//        container.setTilt(0);

        container.startShimmerAnimation();*/
    }

    public static void showHideBottomTab(boolean show) {
        if (show && mSelectedPager != 2) {
            Animations.showBottomView(bottomTabView);
        } else {
            Animations.hideBottomView(bottomTabView);
        }
        bottomTabHome_.setEnabled(show);
        bottomTabLive_.setEnabled(show);
        bottomTabMessage_.setEnabled(show);
        bottomTabProfile_.setEnabled(show);
        bottomTabGolive_.setEnabled(show);
    }

    private void initTabIcons() {

        mViewPager.addOnPageChangeListener(new PageIndicator() {
            @Override
            public void setViewPager(ViewPager view) {

            }

            @Override
            public void setViewPager(ViewPager view, int initialPosition) {

            }

            @Override
            public void setCurrentItem(int item) {

            }

            @Override
            public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {

            }

            @Override
            public void notifyDataSetChanged() {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setUpViewHidingViaPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public int getSelectedPage() {
        if (mViewPager != null) {
            mSelectedPager = mViewPager.getCurrentItem();
        }
        return mSelectedPager;
    }

    public void setSelectedPage(int selectedPage) {
        mSelectedPager = selectedPage;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mSelectedPager);
                mTabLayout.setupWithViewPager(mViewPager);
            }
        });
    }

    private void setUpAdapter() {
        if (adapter == null)
            adapter = new ViewPagerAdapter(getResources(), getChildFragmentManager());

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTabLayout.setSelected(true);
//                mViewPager.setCurrentItem(2);
                mViewPager.setCurrentItem(0);
            }
        }, 100);

        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void setBottomTabMargin() {
        int screenWidth = Utils.getScreenWidth(getActivity());
        if (screenWidth > 0) {
            screenWidth = (screenWidth / 4);
            screenWidth = (screenWidth / 2);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(screenWidth, 0, screenWidth, 0);
            bottomTab.setLayoutParams(params);
        }
    }

    private void hideShowBottomTabs(boolean status) {

    }

    private void setUpViewHidingViaPosition(int position) {
        switch (position) {
            case 0:
                //Home
                setTabSelectionIndex(HOME);
                MainFragment.showHideBottomTab(true);
                bottomTab.setVisibility(View.VISIBLE);
                break;
            case 1:
                //Home
                setTabSelectionIndex(FAVORITES);
                MainFragment.showHideBottomTab(true);
                bottomTab.setVisibility(View.VISIBLE);
                break;
            case 2:
                //Stream
//                setTabSelectionIndex(FAVORITES);
                checkUserType();
                bottomTab.setVisibility(View.GONE);
                MainFragment.showHideBottomTab(false);
                GlobalBus.getBus().post(new Events.MainFragmentToGoLiveFragmentMessage("OPEN_WOWZA_VIEW"));
//                gotoNextFragment(GoLiveStartPageFragment.newInstance());
//                startActivity(new Intent(getActivity(), GoLiveStartPageActivity.class));
                break;
            case 3:
                //Chat
                setTabSelectionIndex(MESSAGE);
                MainFragment.showHideBottomTab(true);
                bottomTab.setVisibility(View.VISIBLE);
                break;
            case 4:
                //Profile
                setTabSelectionIndex(PROFILE);
                MainFragment.showHideBottomTab(true);
                bottomTab.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void checkUserType() {
        try {
            UserBO userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            if (userBO != null) {
                if (userBO.getIsArtist() == 0) {
//                    overlayView.setVisibility(View.VISIBLE);
                    setSelectedPage(0);
                    gotoNextFragment(NonArtistGoLiveOverlayFragment.newInstance());
//                    startActivity(new Intent(getActivity(), GoLiveStartPageActivity.class));
                } else {
//                    overlayView.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            LogUtils.e("checkUserType", "" + e.getMessage());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSelectedPager = mViewPager.getCurrentItem();
    }

    @Override
    public void onResume() {

        if (mSelectedPager == 1) {
            mSelectedPager = 0;
            mViewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(mSelectedPager);
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            }, 100);
        } else {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mViewPager.setCurrentItem(mSelectedPager);
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            });
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottomTabHome:
                setTabSelectionIndex(HOME);
                setSelectedPage(HOME);
                break;
            case R.id.bottomTabLive:
                setTabSelectionIndex(FAVORITES);
                setSelectedPage(FAVORITES);
//                MainFragment.showHideBottomTab(false);
                break;
            case R.id.bottomTabMessage:
                setTabSelectionIndex(MESSAGE);
                setSelectedPage(MESSAGE);
                break;
            case R.id.bottomTabProfile:
                setTabSelectionIndex(PROFILE);
                setSelectedPage(PROFILE);
                break;
            case R.id.bottomTabGolive:
                setSelectedPage(GOLIVE);
                break;
        }
    }

    private void setTabSelectionIndex(int index) {

        if (index == HOME) {

            Animations.buttonBounceAnimation(getActivity(), bottomTabHome_);
            /*bottomTabHome.setImageResource(R.drawable.ic_home);
            bottomTabLive.setImageResource(R.drawable.ic_favorites_off);
            bottomTabProfile.setImageResource(R.drawable.ic_profile_off);
            bottomTabMessage.setImageResource(R.drawable.ic_message_off);*/
        } else if (index == FAVORITES) {

            Animations.buttonBounceAnimation(getActivity(), bottomTabLive_);
            /*bottomTabHome.setImageResource(R.drawable.ic_home_off);
            bottomTabLive.setImageResource(R.drawable.ic_favorites);
            bottomTabProfile.setImageResource(R.drawable.ic_profile_off);
            bottomTabMessage.setImageResource(R.drawable.ic_message_off);*/
        } else if (index == MESSAGE) {

            Animations.buttonBounceAnimation(getActivity(), bottomTabMessage_);
            /*bottomTabHome.setImageResource(R.drawable.ic_home_off);
            bottomTabLive.setImageResource(R.drawable.ic_favorites_off);
            bottomTabProfile.setImageResource(R.drawable.ic_profile_off);
            bottomTabMessage.setImageResource(R.drawable.ic_message);*/
        } else if (index == PROFILE) {

            Animations.buttonBounceAnimation(getActivity(), bottomTabProfile_);
            /*bottomTabHome.setImageResource(R.drawable.ic_home_off);
            bottomTabLive.setImageResource(R.drawable.ic_favorites_off);
            bottomTabProfile.setImageResource(R.drawable.ic_profile);
            bottomTabMessage.setImageResource(R.drawable.ic_message_off);*/
        }
    }
}