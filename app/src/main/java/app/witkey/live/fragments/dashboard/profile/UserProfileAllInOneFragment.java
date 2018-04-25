package app.witkey.live.fragments.dashboard.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MyProfileAdapter;
import app.witkey.live.adapters.dashboard.profile.UserProfileAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.home.broadcasters.BroadcastersFragment;
import app.witkey.live.fragments.dashboard.profile.profilesetting.ProfileSettingFragment;
import app.witkey.live.fragments.dashboard.profile.systemsetting.SystemSettingFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.ProfileItemsBO;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edited by developer on 10/03/2017.
 */

public class UserProfileAllInOneFragment extends BaseFragment implements View.OnClickListener, MyProfileAdapter.ClickListeners {

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;

    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnSettings)
    ImageView btnSettings;

    @BindView(R.id.includedLayoutProfile)
    View includedLayoutProfile;

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

    private boolean loadMoreStatus = true;
    @BindView(R.id.noMomentsParentFrame)
    RelativeLayout noMomentsParentFrame;
    @BindView(R.id.momentsRecyclerView)
    RecyclerView momentsRecyclerView;
    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private UserProfileAdapter userProfileAdapter;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static UserProfileAllInOneFragment newInstance() {
        Bundle args = new Bundle();
        UserProfileAllInOneFragment fragment = new UserProfileAllInOneFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_profile_all_in_one, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);

        initViews();
        /*setUpData();
        setUpProfileRecycler(profileItemsBOs);
        populateUserProfileData();*/
    }

    private void setUpData() {

        profileItemsBOs = new ArrayList<>();

        profileItemsBOs.add(new ProfileItemsBO(getString(R.string.witkey_dollars_earned), R.drawable.dollar, "217,988"));
        profileItemsBOs.add(new ProfileItemsBO(getString(R.string.stored_chips_value), R.drawable.chip, "32,000"));
        profileItemsBOs.add(new ProfileItemsBO(getString(R.string.user_level), R.drawable.moment, "110"));
        profileItemsBOs.add(new ProfileItemsBO(getString(R.string.broadcaster_rank), R.drawable.diamond, "150"));
    }


    private void initViews() {

        if (userProfileAdapter != null) {
            userProfileAdapter = null;
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

        btnBack.setOnClickListener(this);
        btnSettings.setOnClickListener(this);

        mProfileSettingAnim = new Animations();
        systemSettingOption.setOnClickListener(this);
        profileSettingOption.setOnClickListener(this);
        optionView.setOnClickListener(this);
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

    private void setUpFeaturedRecycler(final List<Object> objectList) {

        if (objectList != null && objectList.size() > 0) {
            if (userProfileAdapter == null) {
                momentsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                momentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                // TOP ADD TOP INDEX
                objectList.add(0, getUserProfileData());
                userProfileAdapter = new UserProfileAdapter(objectList, context, momentsRecyclerView);
                momentsRecyclerView.setAdapter(userProfileAdapter);
                momentsRecyclerView.setNestedScrollingEnabled(false);

                userProfileAdapter.setClickListener(new UserProfileAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {
                            case R.id.imageParent:
                                /*startActivity(new Intent(getActivity(), GoLiveActivity.class)
                                        .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_2)
                                        .putExtra(GoLiveActivity.ARG_PARAM_3, streamBOs.get(position)));*/
                                break;
                            case R.id.userProfileViewParent:
//                                gotoNextFragment(FanProfileScrollViewFragment.newInstance());
                                break;
                        }
                    }
                });

                userProfileAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (userProfileAdapter != null && userProfileAdapter.getItems().size() >= maxItems
                                && userProfileAdapter.getItems().size() < totalRecords) {
                            userProfileAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                        }
                    }
                });

            } else {
                userProfileAdapter.addItems(objectList);
                userProfileAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    private void showNoResult(boolean isShow) {
        loadMoreStatus = true;
        swipeRefreshLayout.setRefreshing(false);
        if (isShow) {
            setUpFeaturedRecycler(getUserProfileData());
//            momentsRecyclerView.setVisibility(View.GONE);
//            noMomentsParentFrame.setVisibility(View.VISIBLE);
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
                EnumUtils.ServiceName.user_streams,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (userProfileAdapter == null) {
                            showNoResult(true);
                        }
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("streams");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<StreamBO>>() {
                                }.getType();
                                List<Object> objectList = (List<Object>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                totalRecords = 0;
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {

                                    if (userProfileAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            userProfileAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            userProfileAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpFeaturedRecycler(objectList);
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

    // METHOD TO CONVERT USER PROFILE DATA TO STREAMBO
    private List<Object> getUserProfileData() {
        List<Object> objectList = null;
        if (userBO != null) {
            objectList = new ArrayList<>();
            StreamBO streamBO = new StreamBO();
            UserDetailBO userDetailBO = new UserDetailBO();
            userDetailBO.setUsername(userBO.getUsername());
            userDetailBO.setId(userBO.getId());
            userDetailBO.setProfilePictureUrl(userBO.getProfilePictureUrl());
            userDetailBO.setStatusText(userBO.getStatusText());
            streamBO.setUser_details(userDetailBO);
            objectList.add(streamBO);
            return objectList;
        }
        return objectList;
    }
}