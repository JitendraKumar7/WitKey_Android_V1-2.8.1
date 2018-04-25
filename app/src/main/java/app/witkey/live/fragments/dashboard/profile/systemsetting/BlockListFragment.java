package app.witkey.live.fragments.dashboard.profile.systemsetting;

import android.content.Context;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.BlockListAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.BlockUserBO;
import app.witkey.live.items.LikedUsersBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BlockListFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.rv_swipe_refresh_tv)
    SwipeRefreshLayout rv_swipe_refresh_tv;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;

    @BindView(R.id.noResultTextView_)
    CustomTextView noResultTextView_;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    @BindView(R.id.blockListRecyclerView)
    RecyclerView blockListRecyclerView;

    private BlockListAdapter blockListAdapter;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 2;
    private int startIndex = 0;
    int totalRecords = 0;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static BlockListFragment newInstance() {
        Bundle args = new Bundle();
        BlockListFragment fragment = new BlockListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_block_list, container, false);
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
        /*setUpData();
        setUpBlockListRecycler(blockListBOs);*/
    }

    // METHOD TO INITIALIZE VIEW
    private void initViews() {

        if (blockListAdapter != null) {
            blockListAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firstNetworkCall();
            }
        });

        rv_swipe_refresh_tv.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        rv_swipe_refresh_tv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                firstNetworkCall();
            }
        });

        noResultRefreshTextView.setOnClickListener(this);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    firstNetworkCall();
                }
            });
        }

        btnBack.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.block_list);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    /*private void setUpData() {

        blockListBOs = new ArrayList<>();

        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Smith", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Jake", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Connor", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Wyatt", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Cody", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Scott", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Logan", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Jacob", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Maxwell", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Emma", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Carly", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Heather", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Katherine", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Allison", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Troy", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Frank", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Marc", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Dominic", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Isaac", true));
        blockListBOs.add(new BlockListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Gregory", true));
    }

    private void setUpBlockListRecycler(final List<BlockListBO> blockListBOList) {

        if (blockListBOList != null && blockListBOList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            blockListRecyclerView.setItemAnimator(new DefaultItemAnimator());
            blockListRecyclerView.setLayoutManager(linearLayoutManager);
            blockListAdapter = new BlockListAdapter(blockListBOList, getContext(), blockListRecyclerView);
            blockListRecyclerView.setAdapter(blockListAdapter);
            showNoResult(false);
        } else {
            showNoResult(true);
        }
    }*/

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.VISIBLE);

            noResultTextView.setVisibility(View.VISIBLE);
            noResultTextView.setText(R.string.no_blocked_user);
            noResultTextView_.setText(R.string.blocked_user_here);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            noResultTextView.setVisibility(View.GONE);
            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.noResultRefreshTextView:
                rv_swipe_refresh_tv.setRefreshing(true);
                firstNetworkCall();
                break;
        }
    }

    private void setUpBlockListRecycler(final List<BlockUserBO> userBOList) {

        if (userBOList != null && userBOList.size() > 0) {
            if (blockListAdapter == null) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                blockListRecyclerView.setItemAnimator(new DefaultItemAnimator());
                blockListRecyclerView.setLayoutManager(linearLayoutManager);
                blockListAdapter = new BlockListAdapter(userBOList, getContext(), blockListRecyclerView);
                blockListRecyclerView.setAdapter(blockListAdapter);

                blockListAdapter.setClickListener(new BlockListAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.blockListNameCustomTextView:
                            case R.id.blockListImageView:
                                try {
                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(position), FanProfileScrollViewFragment.TYPE_BLOCK_USERBO));
                                } catch (Exception e) {
                                    LogUtils.e("SearchBroadcasterFragment", "" + e.getMessage());
                                }
                                break;
                            case R.id.btnUserBlock:
                                setUserBlockNetworkCall(getActivity(), userBOList.get(position).getUser_details().getId(), position);
                                break;
                        }
                    }
                });

                /*blockListAdapter.setClickListener(new MyFansAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.fanNameCustomTextView:
                            case R.id.fanImageView:
                                try {
                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(position), FanProfileScrollViewFragment.TYPE_USERBO));
//                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(new StreamBO(), userBOList.get(position), new MomentBO(), FanProfileScrollViewFragment.TYPE_USERBO));
                                    if (broadcasterSearchEditText != null) {
                                        broadcasterSearchEditText.getText().clear();
                                    }
                                    KeyboardOp.hide(getActivity());
                                } catch (Exception e) {
                                    LogUtils.e("SearchBroadcasterFragment", "" + e.getMessage());
                                }
                                break;
                            case R.id.btnFanFollow:
                                if (userBOList.get(position).getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                                    fanFollowNetworkCall(getActivity(), userBOList.get(position).getId(), false, UserSharedPreference.readUserToken(), position);
                                } else {
                                    fanFollowNetworkCall(getActivity(), userBOList.get(position).getId(), true, UserSharedPreference.readUserToken(), position);
                                }
                                break;
                        }
                    }
                });*/

                blockListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (blockListAdapter != null && blockListAdapter.getItems().size() >= maxItems
                                && blockListAdapter.getItems().size() < totalRecords) {
                            blockListAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getBlockListNetworkCall(getActivity(), startIndex, maxItems);
                        }
                    }
                });
            } else {
                blockListAdapter.addItems(userBOList);
                blockListAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    // METHOD TO GET FIRST TIME VIEW DATA
    private void firstNetworkCall() {
        startIndex = 0;
        getBlockListNetworkCall(getActivity(), startIndex, maxItems);
    }

    // METHOD TO GET ALL ARTIST DETAIL NETWORK CALL
    private void getBlockListNetworkCall(final Context context, int offset, int limit) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.get_blocked_user,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            showNoResult(true);
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());

                                // todo parse actual model
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("users");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<BlockUserBO>>() {
                                }.getType();
                                List<BlockUserBO> newStreams = (List<BlockUserBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (blockListAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            blockListAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            blockListAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpBlockListRecycler(newStreams);
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

    // METHOD TO GET ALL ARTIST DETAIL NETWORK CALL
//    Service Name: unblock_user
//    Type Post
//    Params: id{the id of user which u want to unbllock }
    private void setUserBlockNetworkCall(final Context context, String iD, final int position) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.ID, iD);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.unblock_user,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            showNoResult(true);
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                SnackBarUtil.showSnackbar(context, "This user is now un blocked.", false);
                                if (blockListAdapter != null && position > -1) {
                                    blockListAdapter.removeItem(position);
                                }

                                /*JSONObject jsonObject = new JSONObject(taskItem.getResponse());

                                // todo parse actual model
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("users");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<BlockUserBO>>() {
                                }.getType();
                                List<BlockUserBO> newStreams = (List<BlockUserBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (blockListAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            blockListAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            blockListAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpBlockListRecycler(newStreams);
                                }*/
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
}