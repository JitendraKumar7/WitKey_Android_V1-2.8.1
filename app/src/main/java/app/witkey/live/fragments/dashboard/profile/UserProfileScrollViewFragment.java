package app.witkey.live.fragments.dashboard.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MomentsAdapter;
import app.witkey.live.adapters.dashboard.profile.MyProfileAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.home.broadcasters.BroadcastersFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.fragments.dashboard.profile.moments.UserMoments;
import app.witkey.live.fragments.dashboard.profile.moments.UserMomentsComments;
import app.witkey.live.fragments.dashboard.profile.moments.UserMomentsImageView;
import app.witkey.live.fragments.dashboard.profile.profilesetting.ProfileSettingFragment;
import app.witkey.live.fragments.dashboard.profile.systemsetting.SystemSettingFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.MomentBO;
import app.witkey.live.items.ProfileItemsBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.dialogs.EditDeleteMomentDialog;
import app.witkey.live.utils.dialogs.GoLiveBroadcastGiftPanelDialog;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edited by developer on 10/03/2017.
 */

public class UserProfileScrollViewFragment extends BaseFragment implements View.OnClickListener, MyProfileAdapter.ClickListeners {

    @BindView(R.id.userProfileScrollView)
    ScrollView userProfileScrollView;

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;

    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnSettings)
    ImageView btnSettings;

    @BindView(R.id.userLevelIV)
    ImageView userLevelIV;
    @BindView(R.id.artistLevelIV_)
    ImageView artistLevelIV_;

    @BindView(R.id.userProfileImageView)
    ImageView userProfileImageView;

    @BindView(R.id.titleBarBackground)
    ImageView titleBarBackground;

    @BindView(R.id.giftsCountTextView)
    CustomTextView giftsCountTextView;
    @BindView(R.id.fanBaseCountTextView)
    CustomTextView fanBaseCountTextView;
    @BindView(R.id.fanBaseCountView_)
    View fanBaseCountView_;
    @BindView(R.id.userStatusTextView)
    CustomTextView userStatusTextView;

    @BindView(R.id.userLevelBoxParentFrame)
    RelativeLayout userLevelBoxParentFrame;
    @BindView(R.id.userLevelBoxTextView)
    CustomTextView userLevelBoxTextView;

    @BindView(R.id.profile_image_parent_frame)
    RelativeLayout profile_image_parent_frame;

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
    @BindView(R.id.fabParent)
    LinearLayout fabParent;

    @BindView(R.id.systemSettingOption)
    CustomTextView systemSettingOption;

    boolean showFloating = true;
    private MyProfileAdapter myProfileAdapter;
    private List<ProfileItemsBO> profileItemsBOs;
    private Animations mProfileSettingAnim;
    private UserBO userBO;

    private boolean loadMoreStatus = true;
    @BindView(R.id.noMomentsParentFrame)
    RelativeLayout noMomentsParentFrame;
    @BindView(R.id.momentsRecyclerView)
    RecyclerView momentsRecyclerView;
    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.addPost)
//    FloatingActionButton addPost;

    private MomentsAdapter momentsAdapter;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;
    int isArtist = 0;
    int toShareMomentID = 0;
    int SHARE_MOMENT = 100;
    int userChips = 0;
    int userWitkeyDollars = 0;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    UserProgressDetailBO userProgressDetailBO_;
    UserLevelBO userLevelBO;
    UserLevelBO broadcasterLevelBO;
    MomentBO selectedMoment;
    private int selectedMomentPosition = 0;
    private int oldScrollY = 0;
    private int userCurrentLevel = 1;
    private int broadcasterCurrentLevel = 1;

    public static UserProfileScrollViewFragment newInstance() {
        Bundle args = new Bundle();
        UserProfileScrollViewFragment fragment = new UserProfileScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_profile_scroll_view, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        try {
            userProgressDetailBO_ = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userCurrentLevel = userProgressDetailBO_.getUser_level();
            broadcasterCurrentLevel = userProgressDetailBO_.getArtist_level();
            userLevelBO = Utils.getUserLevel(userCurrentLevel);
            broadcasterLevelBO = Utils.getBroadcasterLevel(broadcasterCurrentLevel);

            if (userBO != null) {
                isArtist = userBO.getIsArtist() != null ? userBO.getIsArtist() : 0;

                userChips = (int) userBO.getChips();
                userWitkeyDollars = userBO.getWitkeyDollar().intValue();
            }

            if (userProgressDetailBO_ != null) {
                userChips = (int) userProgressDetailBO_.getChips();
                userWitkeyDollars = userProgressDetailBO_.getWitkeyDollar().intValue();
            }

        } catch (Exception e) {
            LogUtils.e("UserProfileScrollViewFragment", "" + e.getMessage());
        }

        initViews();
        isAritst(isArtist);
        populateUserProfileData();
    }

    private void setUpData(boolean showData) {

        try {
            profileItemsBOs = new ArrayList<>();
            if (showData) {
                profileItemsBOs.add(new ProfileItemsBO(getString(R.string.witkey_dollars_earned), R.drawable.dollar, "" + userWitkeyDollars));
                profileItemsBOs.add(new ProfileItemsBO(getString(R.string.stored_chips_value), R.drawable.chip, "" + userChips));
                profileItemsBOs.add(new ProfileItemsBO(getString(R.string.user_level), userLevelBO != null ? userLevelBO.getLevelLocalImage() : R.drawable.user_level_1_9, userProgressDetailBO_ != null ? userProgressDetailBO_.getUser_level() + "" : "1"));
                profileItemsBOs.add(new ProfileItemsBO(getString(R.string.broadcaster_rank), broadcasterLevelBO != null ? broadcasterLevelBO.getLevelLocalImage() : R.drawable.pearl, userProgressDetailBO_ != null ? userProgressDetailBO_.getArtist_level() + "" : "1"));
            } else {
                profileItemsBOs.add(new ProfileItemsBO(getString(R.string.stored_chips_value), R.drawable.chip, "" + userChips));
                profileItemsBOs.add(new ProfileItemsBO(getString(R.string.user_level), userLevelBO != null ? userLevelBO.getLevelLocalImage() : R.drawable.user_level_1_9, userProgressDetailBO_ != null ? userProgressDetailBO_.getUser_level() + "" : "1"));
       /*userProgressDetailBO.getUser_level()*/
            }

            setUpProfileRecycler(profileItemsBOs);
        } catch (Exception e) {
            LogUtils.e("setUpData", "" + e.getMessage());

        }
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

        if (momentsAdapter != null) {
            momentsAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userBO != null) {
                    startIndex = 0;
                    loadMoreStatus = true;
                    loadMoreStatus = true;
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                }
            }
        });

        fabParent.setOnClickListener(this);
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

        userProfileScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = userProfileScrollView.getScrollY();

                if (userProfileScrollView != null && momentsAdapter != null) {
                    if (userProfileScrollView.getChildAt(0).getBottom() <= (userProfileScrollView.getHeight() + userProfileScrollView.getScrollY())) {
                        if (loadMoreStatus) {
                            if (momentsAdapter != null && momentsAdapter.getItems().size() >= maxItems
                                    && momentsAdapter.getItems().size() < totalRecords) {
                                loadMoreStatus = false;
                                momentsAdapter.addLoadingItem();
                                startIndex = startIndex + 1;
                                getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                            }
                        }
                    }

                    if (scrollY < profile_image_parent_frame.getHeight()) {

                        float heightRatio = (float) scrollY / (float) profile_image_parent_frame.getHeight();
                        titleBarBackground.setAlpha(heightRatio);
                    } else {
                        titleBarBackground.setAlpha(1f);
                    }
                }

                if (scrollY > (oldScrollY + Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET)) {
                    //up
                    MainFragment.showHideBottomTab(false);
                    Animations.hideFloatingView(fabParent);
                } else if (scrollY < (oldScrollY - Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET)) {
                    //down
                    MainFragment.showHideBottomTab(true);
                    Animations.showFloatingView(fabParent);
                }

                oldScrollY = scrollY;
            }
        });

        /*userProfileScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > 0) { // HIDE
//                    MainFragment.showHideBottomFloatingView(false);
                    Animations.showHideBottomFloatingView(getActivity(), fabParent, false);
                } else { // SHOW
//                    MainFragment.showHideBottomFloatingView(true);
                    Animations.showHideBottomFloatingView(getActivity(), fabParent, true);
                }
            }
        });*/

        /*userProfileScrollView.setOnTouchListener(new View.OnTouchListener() {
            float mInitialX;
            float mInitialY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mInitialX = event.getX();
                        mInitialY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        final float x = event.getX();
                        final float y = event.getY();
                        final float yDiff = y - mInitialY;
                        if (yDiff > 1.0) {
                            Log.e("Tag ", "Scroll down");
                            MainFragment.showHideBottomTab(true);
//                            Animations.showHideBottomFloatingView(getActivity(), fabParent, true);
                            break;

                        } else if (yDiff < 1.0) {
                            Log.e("Tag ", "Scroll up");
                            MainFragment.showHideBottomTab(false);
//                            Animations.showHideBottomFloatingView(getActivity(), fabParent, false);
                            break;
                        }
                        break;

                    default:
                        break;
                }
                return false;
            }
        });*/


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
                gotoNextFragment(BroadcastersFragment.newInstance());
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
            case R.id.fabParent:
                gotoNextFragment(UserMoments.newInstance(new MomentBO(), "CREATE"));
                break;
        }
    }

    private void momentsProfileToggle(boolean showMoments) {
        if (showMoments) {
            momentsParentFrame.setVisibility(View.VISIBLE);
            profileParentFrame.setVisibility(View.GONE);
            Animations.showFloatingView(fabParent);
        } else {
            momentsParentFrame.setVisibility(View.GONE);
            profileParentFrame.setVisibility(View.VISIBLE);
            Animations.hideFloatingView(fabParent);
        }
    }

    @Override
    public void onRowClick(int position) {
        if (userBO.getIsArtist() == 1) {
            if (position == 0) {
                gotoNextFragment(BroadcastersFragment.newInstance());
            } else if (position == 1) {
                gotoNextFragment(MyWalletFragment.newInstance());
            } else if (position == 2) {
                gotoNextFragment(UserLevelFragment.newInstance());
            } else if (position == 3) {
                startActivity(new Intent(getActivity(), BroadcastRankActivity.class));
            }
        } else {
            if (position == 0) {
                gotoNextFragment(MyWalletFragment.newInstance());
            } else if (position == 1) {
                gotoNextFragment(UserLevelFragment.newInstance());
            }
        }
    }

    // METHOD TO POPULATE ALL USER PROFILE DATA
    private void populateUserProfileData() {
        try {
            if (userBO != null) {
                profileNameTextView.setText(userBO.getUsername().toUpperCase());
                if (userBO.getIsArtist() == 1) {
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getUser_complete_id());
                } else {
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
                }
                userStatusTextView.setText(userBO.getStatusText());

                if (userProgressDetailBO_ != null) {
                    fanBaseCountTextView.setText(getString(R.string.fanbase) + " " + userProgressDetailBO_.getTotalFollowers());
                    giftsCountTextView.setText(getString(R.string.send) + " " + userProgressDetailBO_.getTotalGiftsSent() + " " + getString(R.string.gifts));
                }

                Utils.setImageSimple(userProfileImageView, userBO.getProfilePictureUrl(), getActivity());

                //startIndex = 0;
//                getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                userLevelBoxTextView.setText(userCurrentLevel + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                diamondsCountBoxTextView.setText(broadcasterCurrentLevel + "");
                artistLevelIV_.setImageResource(broadcasterLevelBO.getLevelLocalImage());
            }
        } catch (Exception e) {
            LogUtils.e("populateUserProfileData", "" + e.getMessage());
        }
    }

    private void setUpFeaturedRecycler(final List<MomentBO> momentBOs) {

        if (momentBOs != null && momentBOs.size() > 0) {
            if (momentsAdapter == null) {
                momentsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                momentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                momentsAdapter = new MomentsAdapter(momentBOs, context, momentsRecyclerView, true /*TRUE -> CAN EDIT*/);
                momentsRecyclerView.setAdapter(momentsAdapter);
                momentsRecyclerView.setNestedScrollingEnabled(false);

                momentsAdapter.setClickListener(new MomentsAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {
                            case R.id.heart_button:
                                try {
                                    String likeStatus = "0";
                                    if (((LikeButton) view).isLiked()) {
                                        likeStatus = EnumUtils.LIKE_DISLIKE_STATUSES.LIKE;
//                                        addRemoveLikeCount(true, position);
//                                    MomentBO momentBO = momentBOs.get(position);
//                                    momentBO.setTotal_likes(momentBOs.get(position).getTotal_comments() + 1);
                                        // momentsAdapter.updateItem(position, momentBO);
                                    } else {
                                        likeStatus = EnumUtils.LIKE_DISLIKE_STATUSES.DISLIKE; // DISLIKE HERE
//                                        addRemoveLikeCount(false, position);
//                                    MomentBO momentBO = momentBOs.get(position);
//                                    momentBO.setTotal_likes(momentBOs.get(position).getTotal_comments() > 0 ? momentBOs.get(position).getTotal_comments() - 1 : 0);
                                        // momentsAdapter.updateItem(position, momentBO);
                                    }
                                    setUserLikeDislikeNetworkCall(getActivity(), likeStatus, momentBOs.get(position).getMoment_id() + "", userBO.getId(), position);
                                } catch (Exception e) {
                                    LogUtils.e("setUpFeaturedRecycler", "" + e.getMessage());
                                }
                                break;

                            case R.id.postLV:
                            case R.id.imageParent:
                            case R.id.commentCountLV:
                                gotoNextFragment(UserMomentsComments.newInstance(momentBOs.get(position), true)); /*TRUE FOR TYPE COMMENTS, FALSE FOR LIKE*/
                                break;

                            case R.id.mEditMoment:
                                selectedMoment = momentBOs.get(position);
                                selectedMomentPosition = position;
                                new EditDeleteMomentDialog().show(getActivity().getSupportFragmentManager(), "EditMoments");
                                break;

                            case R.id.customTextView5:
                            case R.id.likeCountShow:
                                gotoNextFragment(UserMomentsComments.newInstance(momentBOs.get(position), false));/*TRUE FOR TYPE COMMENTS, FALSE FOR LIKE*/
                                break;
                            case R.id.commentCountShow:
                                gotoNextFragment(UserMomentsComments.newInstance(momentBOs.get(position), true));/*TRUE FOR TYPE COMMENTS, FALSE FOR LIKE*/
                                break;

                            case R.id.userProfileViewParent:
                            case R.id.userNameView:
                            case R.id.mUserProfileImage:
                               /* if (userBO != null && userBO.getId().equals(momentBOs.get(position).getUser_details().getId())) {
                                    *//*FROM HERE MOVE TO USER OWN PROFILE VIEW*//*
                                } else {*/
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(momentBOs.get(position), FanProfileScrollViewFragment.TYPE_MOMENTBO));
//                                }
                                break;

                            case R.id.shareCountLV:
                                toShareMomentID = momentBOs.get(position).getMoment_id();
//                                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, momentBOs.get(position).getMoment_id() + "", userBO.getId());
                                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                sharingIntent.setType("text/plain");
                                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.witkey_moment));
                                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, userBO.getUsername() + " " + getString(R.string.shared_this_moment) + " \n" + getString(R.string.moment_link) + toShareMomentID);
                                startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), SHARE_MOMENT);
                                break;
                        }
                    }

                    @Override
                    public void onRowClick(View view, int position, int child) {
                        switch (view.getId()) {
                            case R.id.im_slider:
                                gotoNextFragment(UserMomentsImageView.newInstance(momentBOs.get(position).getImage_arrsy()[child]));
                                break;
                        }
                    }
                });

               /* momentsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (momentsAdapter.getItems().size() >= maxItems
                                && momentsAdapter.getItems().size() < totalRecords) {
                            momentsAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                        }
                    }
                });*/

            } else {
                momentsAdapter.addItems(momentBOs);
                momentsAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    private void showNoResult(boolean isShow) {
        loadMoreStatus = true;
        swipeRefreshLayout.setRefreshing(false);
        if (isShow) {
            momentsRecyclerView.setVisibility(View.GONE);
            noMomentsParentFrame.setVisibility(View.VISIBLE);
        } else {
            noMomentsParentFrame.setVisibility(View.GONE);
            momentsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    private void getUserStreamsListNetworkCall(final Context context, int offset, int limit, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.all_moments,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
//                        if (getUserVisibleHint())
//                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        try {
                            if (momentsAdapter == null) {
                                showNoResult(true);
                            }
                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                if (jsonObject.has("user_progress_detail")) {
                                    UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                    ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);
                                    userProgressDetailBO_ = userProgressDetailBO;
                                    populateUserProfileData();
                                }
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("moments");
                                UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);
                                userProgressDetailBO_ = userProgressDetailBO;
                                populateUserProfileData();

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<MomentBO>>() {
                                }.getType();
                                List<MomentBO> newStreams = (List<MomentBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = 25;//jsonObject.getInt("total_records");

                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {

                                    if (momentsAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            momentsAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            momentsAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpFeaturedRecycler(newStreams);
                                }
                            }
                        } catch (Exception ex) {
                            showNoResult(true);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    //service name: moment-action
//    param: user_id, moment_id, type{50,:share, 60:like}
    private void setUserLikeDislikeNetworkCall(final Context context, final String type, String moment_id, String userID, final int position) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.TYPE, type);
        serviceParams.put(Keys.MOMENTS_ID, moment_id);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.moment_action,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        // DO SOMETHING
                        if (momentsAdapter != null)
                            momentsAdapter.notifyDataSetChanged();

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                if (type.equals(EnumUtils.LIKE_DISLIKE_STATUSES.DISLIKE)) {
                                    addRemoveLikeCount(false, position);
                                } else if (type.equals(EnumUtils.LIKE_DISLIKE_STATUSES.LIKE)) {
                                    addRemoveLikeCount(true, position);
                                }
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);
                                /*DO SOMETHING*/
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO DELETE USER MOMENT NETWORK CALL
    private void deleteUserMomentNetworkCall(final Context context, String _id, String userID, final int position) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.IS_DELETE, "1");/*1 for true*/
        serviceParams.put(Keys.ID, _id);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.add_moments,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        // DO SOMETHING

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                if (momentsAdapter != null && momentsAdapter.getItemCount() > 0) {
                                    momentsAdapter.removeItems(position);
                                }
                                /*DO SOMETHING*/
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isVisibleToUser) {
                if (swipeRefreshLayout != null && userBO != null) {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            startIndex = 0;
                            getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                        }
                    });
                }
            }
        } else {
            LogUtils.e(TAG, "IS NOT VISIBLE");
        }
    }

    private void isAritst(int artistStatus) {

        if (artistStatus == 1) {
            diamondBoxParentFrame.setVisibility(View.VISIBLE);
            fanBaseCountTextView.setVisibility(View.VISIBLE);
            fanBaseCountView_.setVisibility(View.VISIBLE);
            setUpData(true);
        } else {
            diamondBoxParentFrame.setVisibility(View.GONE);
            fanBaseCountTextView.setVisibility(View.GONE);
            fanBaseCountView_.setVisibility(View.GONE);
            setUpData(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SHARE_MOMENT) {
            if (resultCode == Activity.RESULT_OK) {
                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, toShareMomentID + "", userBO.getId(), 0);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*DO SOME THING HERE*/
            }
        }
    }

    private void addRemoveLikeCount(boolean statusAdd, int position) {
        int count = 0;
        try {
            if (momentsAdapter != null && momentsAdapter.getItemCount() > 0) {
                count = momentsAdapter.getItem(position).getTotal_likes();
                if (statusAdd) {
                    momentsAdapter.getItem(position).setTotal_likes(count + 1);
                    momentsAdapter.getItem(position).setIs_like(1);
                } else {
                    if (count > 0) {
                        momentsAdapter.getItem(position).setTotal_likes(count - 1);
                    }
                    momentsAdapter.getItem(position).setIs_like(0);
                }
                momentsAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            LogUtils.e("addRemoveLikeCount", "" + e.getMessage());
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

    @Subscribe // METHOD TO GET EVENT MESSAGE FOR SELECTION
    public void getMessage(Events.FragmentToUserProfileScrollViewMessage fragmentToUserProfileScrollViewMessage) {
        if (selectedMoment != null) {
            if (fragmentToUserProfileScrollViewMessage.getMessage().equals("EDIT")) {
                gotoNextFragment(UserMoments.newInstance(selectedMoment, "EDIT"));
            } else {
                deleteUserMomentNetworkCall(getActivity(), selectedMoment.getMoment_id() + "", userBO.getId(), selectedMomentPosition);
            }

        }
    }
}