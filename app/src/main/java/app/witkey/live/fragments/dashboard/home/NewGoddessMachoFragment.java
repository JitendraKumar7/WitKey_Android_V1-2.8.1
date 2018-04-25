package app.witkey.live.fragments.dashboard.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.EventPosterDetailActivity;
import app.witkey.live.activities.GoLiveActivity;
import app.witkey.live.adapters.dashboard.home.NewGoddessMachoAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.stream.GoLiveStreamActivity;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGoddessMachoFragment extends BaseFragment implements View.OnClickListener {

    public static final String FRAGMENT_TYPE = "fragmentType";

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    private String fragmentType;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout rvSwipeRefreshLayout;

    @BindView(R.id.rv_swipe_refresh_TV)
    SwipeRefreshLayout rv_swipe_refresh_TV;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 5;
    private int startIndex = 0;
    int totalRecords = 0;

    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;

    private NewGoddessMachoAdapter newGoddessMachoAdapter;

    public static NewGoddessMachoFragment newInstance(String fragmentType) {
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TYPE, fragmentType);
        NewGoddessMachoFragment fragment = new NewGoddessMachoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentType = getArguments().getString(FRAGMENT_TYPE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_new_goddess_macho, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

//        showNoResult(true);/*ON QA REQUEST*/
        initView();
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        if (newGoddessMachoAdapter != null) {
            newGoddessMachoAdapter = null;
        }
        rvSwipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        rvSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getAllStreamsNetworkCall(getActivity(), startIndex, maxItems, fragmentType);
            }
        });

        rv_swipe_refresh_TV.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        rv_swipe_refresh_TV.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getAllStreamsNetworkCall(getActivity(), startIndex, maxItems, fragmentType);
            }
        });

        noResultRefreshTextView.setOnClickListener(this);
    }

    private void setUpNewGoddessMachoRecycler(final List<StreamBO> streamBOList) {

        if (streamBOList != null && streamBOList.size() > 0) {

            if (newGoddessMachoAdapter == null) {

                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setLayoutManager(gridLayoutManager);
                newGoddessMachoAdapter = new NewGoddessMachoAdapter(streamBOList, getContext(), mRecyclerView, rvSwipeRefreshLayout.getHeight());
                mRecyclerView.setAdapter(newGoddessMachoAdapter);

                newGoddessMachoAdapter.setClickListener(new NewGoddessMachoAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {
                            case R.id.mNewGoddessMachoCardImageView:
                                //TODO HAVE TO REPLACE THIS WITH STATUS ENDED
//                    if (streamBOList.get(position).getStatus().equals(StreamUtils.STATUS_LIVE)) {
                                startActivity(new Intent(getActivity(), EnumUtils.getCurrentGoLiveViewType())
                                        .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_2)
                                        .putExtra(GoLiveActivity.ARG_PARAM_3, streamBOList.get(position)));
                                //Log.d("JKS", new Gson().toJson(streamBOList.get(position)));
//                    }
                                break;
                            case R.id.imageProfileView:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(streamBOList.get(position), FanProfileScrollViewFragment.TYPE_STREAMBO));
                                break;
                        }
                    }
                });

                newGoddessMachoAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (newGoddessMachoAdapter != null && newGoddessMachoAdapter.getItems().size() >= maxItems
                                && newGoddessMachoAdapter.getItems().size() < totalRecords) {
                            newGoddessMachoAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getAllStreamsNetworkCall(getActivity(), startIndex, maxItems, fragmentType);
                        }
                    }
                });

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
                newGoddessMachoAdapter.addItems(streamBOList);
                newGoddessMachoAdapter.setLoaded();
            }

        } else {
            showNoResult(true);
        }
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            rvSwipeRefreshLayout.setRefreshing(false);
            rv_swipe_refresh_TV.setRefreshing(false);
            rvSwipeRefreshLayout.setVisibility(View.GONE);
            rv_swipe_refresh_TV.setVisibility(View.VISIBLE);
            noResultTextView.setVisibility(View.VISIBLE);
        } else {
            rvSwipeRefreshLayout.setRefreshing(false);
            rv_swipe_refresh_TV.setRefreshing(false);
            rvSwipeRefreshLayout.setVisibility(View.VISIBLE);
            rv_swipe_refresh_TV.setVisibility(View.GONE);
            noResultTextView.setVisibility(View.GONE);
        }
    }

    // METHOD TO GET ALL STREAMS NETWORK CALL
    private void getAllStreamsNetworkCall(final Context context, int offset, int limit, String listType) {

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
                        if (newGoddessMachoAdapter == null) {
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
                                List<StreamBO> newStreams = (List<StreamBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");

                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {

                                    if (newGoddessMachoAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            newGoddessMachoAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            newGoddessMachoAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpNewGoddessMachoRecycler(newStreams);
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
            if (rvSwipeRefreshLayout != null) {
                rvSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        rvSwipeRefreshLayout.setRefreshing(true);
                        startIndex = 0;
                        getAllStreamsNetworkCall(getActivity(), startIndex, maxItems, fragmentType);
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
                rv_swipe_refresh_TV.setRefreshing(true);
                startIndex = 0;
                getAllStreamsNetworkCall(getActivity(), startIndex, maxItems, fragmentType);
                break;
        }
    }
}