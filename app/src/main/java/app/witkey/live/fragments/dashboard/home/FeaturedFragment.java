package app.witkey.live.fragments.dashboard.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MomentsAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.fragments.dashboard.profile.moments.UserMomentsComments;
import app.witkey.live.fragments.dashboard.profile.moments.UserMomentsImageView;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.MomentBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.rv_swipe_refresh_tv)
    SwipeRefreshLayout rv_swipe_refresh_tv;

    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    MomentsAdapter featuredAdapter;
    UserBO userBO;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;
    int toShareMomentID = 0;
    int SHARE_MOMENT = 100;

    public static FeaturedFragment newInstance() {
        Bundle args = new Bundle();
        FeaturedFragment fragment = new FeaturedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        context = inflater.getContext();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
//            showNoResult(true);/*ON QA REQUEST*/
            initView();
        } catch (Exception e) {
            LogUtils.e("onViewCreated", "" + e.getMessage());
        }
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        if (featuredAdapter != null) {
            featuredAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getFeaturedListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.FEATURED);
            }
        });

        rv_swipe_refresh_tv.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        rv_swipe_refresh_tv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getFeaturedListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.FEATURED);
            }
        });

        noResultRefreshTextView.setOnClickListener(this);
    }

    private void setUpFeaturedRecycler(final List<MomentBO> streamBOs) {

        if (streamBOs != null && streamBOs.size() > 0) {

            if (featuredAdapter == null) {

                mRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                featuredAdapter = new MomentsAdapter(streamBOs, context, mRecyclerView, false /*CANT EDIT*/);
                mRecyclerView.setAdapter(featuredAdapter);
                mRecyclerView.setNestedScrollingEnabled(false);

                featuredAdapter.setClickListener(new MomentsAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {

                            case R.id.heart_button:
                                String likeStatus = "0";
                                try {
                                    //((LikeButton) view).performClick();

                                    if (((LikeButton) view).isLiked()) {
                                        likeStatus = EnumUtils.LIKE_DISLIKE_STATUSES.LIKE;
                                    } else {
                                        likeStatus = EnumUtils.LIKE_DISLIKE_STATUSES.DISLIKE; // DISLIKE HERE
                                    }
                                    setUserLikeDislikeNetworkCall(getActivity(), likeStatus, streamBOs.get(position).getMoment_id() + "", userBO.getId(), position);

                                } catch (Exception e) {
                                    LogUtils.e("setUpFeaturedRecycler", "" + e.getMessage());
                                }
                                break;


                            case R.id.momentVP:
//                                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                                break;


                            case R.id.postLV:
                            case R.id.imageParent:
                            case R.id.commentCountLV:
                                gotoNextFragment(UserMomentsComments.newInstance(streamBOs.get(position), true)); /*TRUE FOR TYPE COMMENTS, FALSE FOR LIKE*/
                                break;

                            case R.id.customTextView5:
                            case R.id.likeCountShow:
                                gotoNextFragment(UserMomentsComments.newInstance(streamBOs.get(position), false));/*TRUE FOR TYPE COMMENTS, FALSE FOR LIKE*/
                                break;
                            case R.id.commentCountShow:
                                gotoNextFragment(UserMomentsComments.newInstance(streamBOs.get(position), true));/*TRUE FOR TYPE COMMENTS, FALSE FOR LIKE*/
                                break;

                            case R.id.userProfileViewParent:
                            case R.id.userNameView:
                            case R.id.mUserProfileImage:
                               /* if (userBO != null && userBO.getId().equals(momentBOs.get(position).getUser_details().getId())) {
                                    *//*FROM HERE MOVE TO USER OWN PROFILE VIEW*//*
                                } else {*/
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(streamBOs.get(position), FanProfileScrollViewFragment.TYPE_MOMENTBO));
//                                }
                                break;

                            case R.id.shareCountLV:
                                toShareMomentID = streamBOs.get(position).getMoment_id();
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
                                gotoNextFragment(UserMomentsImageView.newInstance(streamBOs.get(position).getImage_arrsy()[child]));
                                break;
                        }
                    }
                });

                featuredAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (featuredAdapter != null && featuredAdapter.getItems().size() >= maxItems
                                && featuredAdapter.getItems().size() < totalRecords) {
                            featuredAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getFeaturedListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.FEATURED);
                        }
                    }
                });


               /* mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                    boolean isIdle;
                    int mScrollY;

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        isIdle = newState == RecyclerView.SCROLL_STATE_IDLE;
                        if (isIdle) {
                            mScrollY = 0;
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        mScrollY += dy;
                        if (mScrollY > 12) {
                            MainFragment.showHideBottomTab(false);
                        } else {
                            MainFragment.showHideBottomTab(true);
                        }
                    }
                });*/


                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET) {// HIDE
                            MainFragment.showHideBottomTab(false);
                        } else if (dy < -Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET) { // SHOW
                            MainFragment.showHideBottomTab(true);
                        }

                        super.onScrolled(recyclerView, dx, dy);
                    }
                });


            } else {
                featuredAdapter.addItems(streamBOs);
                featuredAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.GONE);
            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.VISIBLE);
            noResultTextView.setVisibility(View.VISIBLE);
        } else {
            rv_swipe_refresh_tv.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            noResultTextView.setVisibility(View.GONE);
            rv_swipe_refresh_tv.setRefreshing(false);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    // METHOD TO GET ALL USERS DETAIL NETWORK CALL
    private void getFeaturedListNetworkCall(final Context context, int offset, int limit, String listType) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.all_streams,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (featuredAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("moment");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<MomentBO>>() {
                                }.getType();
                                List<MomentBO> newStreams = (List<MomentBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (featuredAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            featuredAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            featuredAdapter.clearItems();
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
                        if (featuredAdapter != null)
                            featuredAdapter.notifyDataSetChanged();
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


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        startIndex = 0;
                        getFeaturedListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.FEATURED);
                    }
                });
            }
        } else {
            LogUtils.e(TAG, "IS NOT VISIBLE");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noResultRefreshTextView:
                rv_swipe_refresh_tv.setRefreshing(true);
                startIndex = 0;
                getFeaturedListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.FEATURED);
                break;
        }
    }

    private void addRemoveLikeCount(boolean statusAdd, int position) {
        int count = 0;
        try {
            if (featuredAdapter != null && featuredAdapter.getItemCount() > 0) {
                count = featuredAdapter.getItem(position).getTotal_likes();
                if (statusAdd) {
                    featuredAdapter.getItem(position).setTotal_likes(count + 1);
                    featuredAdapter.getItem(position).setIs_like(1);
                } else {
                    if (count > 0) {
                        featuredAdapter.getItem(position).setTotal_likes(count - 1);
                    }
                    featuredAdapter.getItem(position).setIs_like(0);
                }
                featuredAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            LogUtils.e("addRemoveLikeCount", "" + e.getMessage());
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

}