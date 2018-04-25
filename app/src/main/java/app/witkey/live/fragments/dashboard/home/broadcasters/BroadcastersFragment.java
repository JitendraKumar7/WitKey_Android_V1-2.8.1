package app.witkey.live.fragments.dashboard.home.broadcasters;

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
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.home.BroadcasterAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BroadcastersFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.broadcasterTopSelection)
    RadioGroup broadcasterTopSelection;
    @BindView(R.id.broadcasterRankingSelection)
    RadioGroup broadcasterRankingSelection;
    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;
    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;
    @BindView(R.id.noResultParent)
    LinearLayout noResultParent;

    private String TAG = this.getClass().getSimpleName();
    BroadcasterAdapter broadcasterAdapter;
    private Context context;

    int TOP_SPENDER = 0, TOP_BROADCASTER = 1, TOP_FAN = 2, CURRENT_TYPE = 0;
    int DAILY = 1, WEEKLY = 2, MONTHLY = 3, CURRENT_RANKING_TYPE = 1;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 5;
    private int startIndex = 0;
    int totalRecords = 0;

    public static BroadcastersFragment newInstance() {
        Bundle args = new Bundle();
        BroadcastersFragment fragment = new BroadcastersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_broadcaster, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
        setSelectionListener();
    }

    private void initView() {
        btnBack.setOnClickListener(this);
        noResultRefreshTextView.setOnClickListener(this);
    }

    private void setUpFeaturedRecycler(final List<UserBO> userBOList, int backImageType) {


        if (userBOList != null && userBOList.size() > 0) {

            if (broadcasterAdapter == null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                broadcasterAdapter = new BroadcasterAdapter(userBOList, context, mRecyclerView, backImageType);
                mRecyclerView.setAdapter(broadcasterAdapter);

                broadcasterAdapter.setClickListener(new BroadcasterAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.IndexBottom:
//                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.size() < (position + 6) ? userBOList.get(position + 7) : position + 5, FanProfileScrollViewFragment.TYPE_USERBO));
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(position + 6), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.broadcasterImageFirst_IV:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(0), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.broadcasterImageSecond_IV:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(1), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.broadcasterImageThird_IV:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(2), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.broadcasterImageFouth_IV:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(3), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.broadcasterImageFifth_IV:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(4), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.IndexBottom_new:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(5), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.additionalView:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(6), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                            case R.id.additionalViewNew:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(7), FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                        }
                    }
                });

                broadcasterAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (broadcasterAdapter != null && broadcasterAdapter.getItems().size() >= maxItems
                                && broadcasterAdapter.getItems().size() < totalRecords) {
                            broadcasterAdapter.addLoadingItem();
                            startIndex = startIndex + 1;

                            if (CURRENT_TYPE == TOP_BROADCASTER) {
//                                getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, "", EnumUtils.ServiceName.top_broadcaster);
                                getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_broadcaster);
                            } else if (CURRENT_TYPE == TOP_SPENDER) {
                                getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_spender);
                            } else if (CURRENT_TYPE == TOP_FAN) {
                                getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_fan);
                            }
                        }
                    }
                });


            } else {

                broadcasterAdapter.addItems(userBOList);
                broadcasterAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    private void showNoResult(boolean isShow) {
        if (isShow) {
            noResultParent.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            noResultParent.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void setSelectionListener() {

        broadcasterTopSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.topSpender:
                        CURRENT_TYPE = TOP_SPENDER;
                        makeFirstTopBroadcasterCall();
                        break;
                    case R.id.topBroadcaster:
                        CURRENT_TYPE = TOP_BROADCASTER;
                        makeFirstTopBroadcasterCall();
                        break;
                    case R.id.topFan:
                        CURRENT_TYPE = TOP_FAN;
                        makeFirstTopBroadcasterCall();
                        break;
                }
            }
        });

        broadcasterRankingSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.dailyRanking:
                        CURRENT_RANKING_TYPE = DAILY;
                        makeFirstTopBroadcasterCall();
                        break;
                    case R.id.weeklyRanking:
                        CURRENT_RANKING_TYPE = WEEKLY;
                        makeFirstTopBroadcasterCall();
                        break;
                    case R.id.monthlyRanking:
                        CURRENT_RANKING_TYPE = MONTHLY;
                        makeFirstTopBroadcasterCall();
                        break;
                }
            }
        });

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                if (broadcasterAdapter != null) {
                    broadcasterAdapter = null;
                }

                if (CURRENT_TYPE == TOP_BROADCASTER) {
//                    getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, "", EnumUtils.ServiceName.top_broadcaster);
                    getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_broadcaster);
                } else if (CURRENT_TYPE == TOP_SPENDER) {
                    getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_spender);
                } else if (CURRENT_TYPE == TOP_FAN) {
                    getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_fan);
                }
            }
        });
        makeFirstTopBroadcasterCall();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.noResultRefreshTextView:
                makeFirstTopBroadcasterCall();
                break;
        }
    }

    // METHOD TO GET ALL USERS DETAIL NETWORK CALL
    private void getTopBroadcastersListNetworkCall(final Context context, int offset, int limit, String listType, EnumUtils.ServiceName serviceName) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                serviceName,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (broadcasterAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = null;
                                if (jsonObject.has("top-spender")) {
                                    favoritesJsonArray = jsonObject.getJSONArray("top-spender");
                                } else if (jsonObject.has("artists")) {
                                    favoritesJsonArray = jsonObject.getJSONArray("artists");
                                } else if (jsonObject.has("top-fan")) {
                                    favoritesJsonArray = jsonObject.getJSONArray("top-fan");
                                }

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<UserBO>>() {
                                }.getType();
                                List<UserBO> newStreams = (List<UserBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (broadcasterAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            broadcasterAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            broadcasterAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpFeaturedRecycler(newStreams, CURRENT_TYPE);
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

    private void makeFirstTopBroadcasterCall() {
        if (broadcasterAdapter != null) {
            broadcasterAdapter = null;
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    startIndex = 0;
                    if (CURRENT_TYPE == TOP_BROADCASTER) {
//                        getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, "", EnumUtils.ServiceName.top_broadcaster);
                        getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_broadcaster);
                    } else if (CURRENT_TYPE == TOP_SPENDER) {
                        getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_spender);
                    } else if (CURRENT_TYPE == TOP_FAN) {
                        getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, CURRENT_RANKING_TYPE + "", EnumUtils.ServiceName.top_fan);
                    }
                }
            });
        }
    }
}