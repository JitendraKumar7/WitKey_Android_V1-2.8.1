package app.witkey.live.fragments.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lion.materialshowcaseview.IShowcaseSequenceListener;
import com.lion.materialshowcaseview.MaterialShowcaseSequence;
import com.lion.materialshowcaseview.MaterialShowcaseView;
import com.lion.materialshowcaseview.ShowcaseConfig;
import com.lion.materialshowcaseview.TutorialViewType;

import org.greenrobot.eventbus.Subscribe;

import app.witkey.live.R;
import app.witkey.live.activities.GoLiveActivity;
import app.witkey.live.adapters.dashboard.home.HomeViewPagerAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.home.HottestFragment;
import app.witkey.live.fragments.dashboard.home.broadcasters.BroadcastersFragment;
import app.witkey.live.fragments.dashboard.home.broadcasters.SearchBroadcasterFragment;
import app.witkey.live.items.StreamBO;
import app.witkey.live.stream.GoLiveStreamActivity;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    private static final String SHOWCASE_ID = "sequence example";
    int mSelectedPager = 0;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;

    @BindView(R.id.btnMore)
    ImageView btnMore;

    @BindView(R.id.mHomeTabLayout)
    TabLayout mHomeTabLayout;
    @BindView(R.id.mHomeChildPager)
    ViewPager mViewPager;

    @BindView(R.id.viewTopLayer)
    View viewTopLayer;

    AnimationDrawable frameAnimation;
    private boolean mHeaderIsShow;
    HomeViewPagerAdapter homeViewPagerAdapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
        setUpAdapter();
        initTabIcons();
    }

    //this method use to show tutorial on home screen....
    private void presentShowcaseSequence() {

        HottestFragment.callServerForResult = true;
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(150); // half second between each showcase view
        config.setMaskColor(getResources().getColor(R.color.grey_transparent));

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(getActivity());

        sequence.setConfig(config);

        String[] generalAppTutorialArray = getResources().getStringArray(R.array.general_app_tutorial);

        IShowcaseSequenceListener iShowcaseSequenceListener = new IShowcaseSequenceListener() {
            @Override
            public void onShowcaseSkipClicked(MaterialShowcaseView showcaseView) {
                viewTopLayer.setClickable(false);
            }

            @Override
            public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView.DismissedType dismissedType) {
            }

            @Override
            public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
            }
        };

        MaterialShowcaseView oneShowcaseView = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.ButtonSearch)
                .setTarget(btnSearch)
                .setRadius(btnSearch.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[0])
                .setDismissOnTouch(true)
                .build();

        oneShowcaseView.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(oneShowcaseView);

//        TutorialViewType tutorialViewTypeTabs[] = {TutorialViewType.FeaturedTab, TutorialViewType.HottestTab, TutorialViewType.NewTab, TutorialViewType.GoddessTab, TutorialViewType.MachoTab};
        TutorialViewType tutorialViewTypeTabs[] = {TutorialViewType.HottestTab, TutorialViewType.NewTab, TutorialViewType.GoddessTab, TutorialViewType.MachoTab};
        //add tab sequence...
        for (int i = 0; i < mHomeTabLayout.getTabCount() - 1; i++) {

            if (mHomeTabLayout.getTabAt(i) != null) {
                TabLayout.Tab tab = mHomeTabLayout.getTabAt(i);
                if (tab != null && tab.getCustomView() != null) {
                    View tabView = tab.getCustomView();
                    MaterialShowcaseView tabSequence = new MaterialShowcaseView.Builder(getActivity())
                            .setTutorialViewType(tutorialViewTypeTabs[i])
                            .setTarget(tabView)
                            .setRadius(tabView.getWidth() / 2)
                            .setUseAutoRadius(false)
                            .setDismissOnTouch(true)
                            .setContentText(generalAppTutorialArray[i + 1]).build();
                    tabSequence.addShowcaseListener(iShowcaseSequenceListener);
                    sequence.addSequenceItem(tabSequence);
                }
            }
        }

        if (homeViewPagerAdapter.getHottestFragment() != null && homeViewPagerAdapter.getHottestFragment() instanceof HottestFragment) {

            View hottestFragmentView = homeViewPagerAdapter.getHottestFragment().getView();

            if (hottestFragmentView != null &&
                    hottestFragmentView.findViewById(R.id.recyclerView) != null &&
                    ((RecyclerView) hottestFragmentView.findViewById(R.id.recyclerView)).getAdapter() != null &&
                    ((RecyclerView) hottestFragmentView.findViewById(R.id.recyclerView)).getLayoutManager() != null &&
                    ((RecyclerView) hottestFragmentView.findViewById(R.id.recyclerView)).getLayoutManager() instanceof GridLayoutManager) {

                GridLayoutManager gridLayoutManager = (GridLayoutManager) ((RecyclerView) hottestFragmentView.findViewById(R.id.recyclerView)).getLayoutManager();

                if (gridLayoutManager.getChildAt(0) != null) {

                    View firstChild = gridLayoutManager.getChildAt(0);
                    MaterialShowcaseView ShowcaseViewHottestZero = new MaterialShowcaseView.Builder(getActivity())
                            .setTutorialViewType(TutorialViewType.HottestFirstIndex)
                            .setTarget(firstChild)
                            .setRadius(firstChild.getHeight() / 2)
                            .setUseAutoRadius(false)
                            .setContentText(generalAppTutorialArray[mHomeTabLayout.getTabCount()])
                            .setDismissOnTouch(true)
                            .build();

                    ShowcaseViewHottestZero.addShowcaseListener(iShowcaseSequenceListener);
                    sequence.addSequenceItem(ShowcaseViewHottestZero);
                }
                if (gridLayoutManager.getChildAt(2) != null) {

                    View firstChild = gridLayoutManager.getChildAt(2);
                    MaterialShowcaseView ShowcaseViewHottestZero = new MaterialShowcaseView.Builder(getActivity())
                            .setTutorialViewType(TutorialViewType.HottestFourthIndex)
                            .setTarget(firstChild)
                            .setRadius(firstChild.getWidth() / 2)
                            .setUseAutoRadius(false)
                            .setContentText(generalAppTutorialArray[mHomeTabLayout.getTabCount() + 1])
                            .setDismissOnTouch(true)
                            .build();
                    ShowcaseViewHottestZero.addShowcaseListener(iShowcaseSequenceListener);
                    sequence.addSequenceItem(ShowcaseViewHottestZero);
                }
            }
        }

        MaterialShowcaseView ShowcaseView2 = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.ButtonMore)
                .setTarget(btnMore)
                .setRadius((btnMore.getWidth() / 2) - 10)
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[mHomeTabLayout.getTabCount() + 2])
                .setDismissOnTouch(true)
                .build();
        ShowcaseView2.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(ShowcaseView2);

        MaterialShowcaseView ShowcaseView3 = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.BottomTabOne)
                .setTarget(getActivity().findViewById(R.id.bottomTabHome))
                .setRadius((int) (getActivity().findViewById(R.id.bottomTabHome).getWidth() / 1.5))
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[generalAppTutorialArray.length - 5])
                .setDismissOnTouch(true)
                .build();
        ShowcaseView3.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(ShowcaseView3);

        MaterialShowcaseView ShowcaseView4 = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.BottomTabTwo)
                .setTarget(getActivity().findViewById(R.id.bottomTabLive))
                .setRadius((int) ((getActivity().findViewById(R.id.bottomTabLive)).getWidth() / 1.5))
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[generalAppTutorialArray.length - 4])
                .setDismissOnTouch(true)
                .build();
        ShowcaseView4.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(ShowcaseView4);
        MaterialShowcaseView ShowcaseView7 = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.BottomTabThree)
                .setTarget(getActivity().findViewById(R.id.bottomTabGolive))
                .setRadius((int) ((getActivity().findViewById(R.id.bottomTabGolive)).getWidth() / 1.5))
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[generalAppTutorialArray.length - 3])
                .setDismissOnTouch(true)
                .build();
        ShowcaseView7.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(ShowcaseView7);

        MaterialShowcaseView ShowcaseView5 = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.BottomTabFour)
                .setTarget(getActivity().findViewById(R.id.bottomTabMessage))
                .setRadius((int) ((getActivity().findViewById(R.id.bottomTabMessage)).getWidth() / 1.5))
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[generalAppTutorialArray.length - 2])
                .setDismissOnTouch(true)
                .build();
        ShowcaseView5.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(ShowcaseView5);

        MaterialShowcaseView ShowcaseView6 = new MaterialShowcaseView.Builder(getActivity())
                .setTutorialViewType(TutorialViewType.BottomTabFive)
                .setTarget(getActivity().findViewById(R.id.bottomTabProfile))
                .setRadius((int) ((getActivity().findViewById(R.id.bottomTabProfile)).getWidth() / 1.5))
                .setUseAutoRadius(false)
                .setContentText(generalAppTutorialArray[generalAppTutorialArray.length - 1])
                .setDismissOnTouch(true)
                .build();
        ShowcaseView6.addShowcaseListener(new IShowcaseSequenceListener() {
            @Override
            public void onShowcaseSkipClicked(MaterialShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView.DismissedType dismissedType) {
                viewTopLayer.setClickable(false);
            }
        });
        sequence.addSequenceItem(ShowcaseView6);

        sequence.start();

    }

    private void initView() {

//        btnMore.setBackgroundResource(R.drawable.impressive_list_animation);
//        frameAnimation = (AnimationDrawable) btnMore.getBackground();
//        frameAnimation.start();

        btnSearch.setOnClickListener(this);
        btnMore.setOnClickListener(this);
    }

    private void setUpAdapter() {
        if (homeViewPagerAdapter == null)
            homeViewPagerAdapter = new HomeViewPagerAdapter(getResources(), getChildFragmentManager());

        mViewPager.setAdapter(homeViewPagerAdapter);
        mViewPager.setOffscreenPageLimit(6);
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                mHomeTabLayout.setSelected(true);
                mViewPager.setCurrentItem(1);
                mViewPager.setCurrentItem(0);
            }
        }, 100);
        mHomeTabLayout.setupWithViewPager(mViewPager);
        homeViewPagerAdapter.setTabs(mHomeTabLayout, getActivity());
    }

    private void initTabIcons() {
        mHomeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setUpViewHidingViaPosition(tab.getPosition(), tab.getCustomView());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewHidingViaPosition(int position, View view) {
        ImageView imageView = null;
//        if (view != null) {
//            imageView = (ImageView) view.findViewById(R.id.customTabImage);
//        }
        switch (position) {
            /*case 0:
                //Featured
                MainFragment.showHideBottomTab(true);
                if (imageView != null) {
                    Animations.buttonBounceAnimation(getActivity(), imageView);
                    //imageView.startAnimation(animation);
                }
                break;*/
            case 0:
                //Hottest
                MainFragment.showHideBottomTab(true);
                if (imageView != null) {
                    Animations.buttonBounceAnimation(getActivity(), imageView);
//                    imageView.startAnimation(animation);
                }
                break;
            case 1:
                //New
                MainFragment.showHideBottomTab(true);
                if (imageView != null) {
                    Animations.buttonBounceAnimation(getActivity(), imageView);
//                    imageView.startAnimation(animation);
                }
                break;
            case 2:
                //Goddess
                MainFragment.showHideBottomTab(true);
                if (imageView != null) {
                    Animations.buttonBounceAnimation(getActivity(), imageView);
//                    imageView.startAnimation(animation);
                }
                break;
//            case 3:
//                //Macho
//                MainFragment.showHideBottomTab(true);
//                if (imageView != null) {
//                    Animations.buttonBounceAnimation(getActivity(), imageView);
//                    //imageView.startAnimation(animation);
//                }
//                break;
//            case 4:
//                //Tutorial
//                MainFragment.showHideBottomTab(true);
//                if (imageView != null) {
//                    Animations.buttonBounceAnimation(getActivity(), imageView);
////                    imageView.startAnimation(animation);
//                }
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                gotoNextFragment(SearchBroadcasterFragment.newInstance());
                break;

            case R.id.btnMore:
                gotoNextFragment(BroadcastersFragment.newInstance());
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mSelectedPager = mViewPager.getCurrentItem();
    }

    @Override
    public void onResume() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mSelectedPager);
                mHomeTabLayout.setupWithViewPager(mViewPager);
                homeViewPagerAdapter.setTabs(mHomeTabLayout, getActivity());
            }
        });

        if (mSelectedPager == 2) {
        }
        super.onResume();
    }

    public int getSelectedPage() {
        if (mViewPager != null) {
            mSelectedPager = mViewPager.getCurrentItem();
        }
        return mSelectedPager;
    }

    public void setSelectedPage(int selectedPage) {
        mSelectedPager = selectedPage;
       /* new Handler().post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mSelectedPager);
                mHomeTabLayout.setupWithViewPager(mViewPager);
            }
        });
        homeViewPagerAdapter.setTabs(mHomeTabLayout, getActivity());*/
        mHomeTabLayout.getTabAt(selectedPage).select();
    }

    public void showTutorial() {
        viewTopLayer.setClickable(true);
        MaterialShowcaseView.resetSingleUse(getContext(), SHOWCASE_ID);
        presentShowcaseSequence();
        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                presentShowcaseSequence();
            }
        }, 2500);*/
    }

    @Subscribe // METHOD TO GET EVENT MESSAGE FOR SELECTION
    public void getMessage(Events.TutorialFragmentToHomeFragmentMessage TutorialFragmentToHomeFragmentMessage) {

        if (TutorialFragmentToHomeFragmentMessage.getMessage().equals("GENERAL_APP")) {
            HottestFragment.callServerForResult = false;
            setSelectedPage(0);
            showTutorial();
        } else if (TutorialFragmentToHomeFragmentMessage.getMessage().equals("GO_LIVE")) {
            startActivity(new Intent(getActivity(), EnumUtils.getCurrentGoLiveViewType())
                    .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_5)
                    .putExtra(GoLiveActivity.ARG_PARAM_3, new StreamBO()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // REGISTER EVENT BUS TO LISTEN EVENT.
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // UNREGISTER EVENT BUS
        GlobalBus.getBus().unregister(this);
    }
}


