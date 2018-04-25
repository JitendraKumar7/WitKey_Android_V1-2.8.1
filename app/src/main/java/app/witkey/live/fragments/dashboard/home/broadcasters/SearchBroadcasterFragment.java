package app.witkey.live.fragments.dashboard.home.broadcasters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import app.witkey.live.activities.Activities;
import app.witkey.live.adapters.dashboard.home.MyBroadcasterAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchBroadcasterFragment extends BaseFragment implements View.OnClickListener {

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

    @BindView(R.id.artistRecyclerView)
    RecyclerView artistRecyclerView;

    @BindView(R.id.broadcasterSearchEditText)
    CustomEditText broadcasterSearchEditText;

    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;

    @BindView(R.id.noResultTextView_)
    CustomTextView noResultTextView_;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    private MyBroadcasterAdapter myBroadcasterAdapter;
    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 10;
    private int startIndex = 0;
    int totalRecords = 0;

    private Runnable runnable;
    private Handler handler;

    String userSearchString = "NONE";
    List<UserBO> userBOList;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static SearchBroadcasterFragment newInstance() {
        Bundle args = new Bundle();
        SearchBroadcasterFragment fragment = new SearchBroadcasterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_search_broadcaster, container, false);
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
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.search_text);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void setUpArtistRecycler(final List<UserBO> userBOList) {

        if (userBOList != null && userBOList.size() > 0) {
            if (myBroadcasterAdapter == null) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                artistRecyclerView.setItemAnimator(new DefaultItemAnimator());
                artistRecyclerView.setLayoutManager(linearLayoutManager);
                myBroadcasterAdapter = new MyBroadcasterAdapter(userBOList, getContext(), artistRecyclerView);
                artistRecyclerView.setAdapter(myBroadcasterAdapter);

                myBroadcasterAdapter.setClickListener(new MyBroadcasterAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        switch (view.getId()) {
//                            case R.id.fanNameCustomTextView:
//                            case R.id.fanImageView:
                            case R.id.fanParentFrame:
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
                                if (position < 0)
                                    return;

                                if (userBOList.get(position).getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                                    fanFollowNetworkCall(getActivity(), userBOList.get(position).getId(), false, UserSharedPreference.readUserToken(), position);
                                } else {
                                    fanFollowNetworkCall(getActivity(), userBOList.get(position).getId(), true, UserSharedPreference.readUserToken(), position);
                                }
                                break;
                        }
                    }
                });

                myBroadcasterAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (myBroadcasterAdapter != null && myBroadcasterAdapter.getItems().size() >= maxItems
                                && myBroadcasterAdapter.getItems().size() < totalRecords) {
                            myBroadcasterAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            String searchStr = broadcasterSearchEditText.getText().toString().trim();
                            if (!searchStr.isEmpty()) {
                                getArtistListNetworkCall(getActivity(), startIndex, maxItems, searchStr);
                            } else {
                                getArtistListNetworkCall(getActivity(), startIndex, maxItems, userSearchString);
                            }
                        }
                    }
                });
            } else {
                myBroadcasterAdapter.addItems(userBOList);
                myBroadcasterAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.VISIBLE);

            noResultTextView.setVisibility(View.VISIBLE);
            noResultTextView.setText(R.string.no_artist);
            noResultTextView_.setText(R.string.popular_artist_here);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            noResultTextView.setVisibility(View.GONE);
            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.GONE);
        }
    }

    private void initViews() {

        if (myBroadcasterAdapter != null) {
            myBroadcasterAdapter = null;
        }

        handler = new Handler();

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String searchStr = broadcasterSearchEditText.getText().toString().trim();
                if (!searchStr.isEmpty()) {
                    startIndex = 0;
                    getArtistListNetworkCall(getActivity(), startIndex, maxItems, searchStr);
                } else {
                    firstNetworkCall();
                }
            }
        });

        rv_swipe_refresh_tv.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        rv_swipe_refresh_tv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String searchStr = broadcasterSearchEditText.getText().toString().trim();
                if (!searchStr.isEmpty()) {
                    startIndex = 0;
                    getArtistListNetworkCall(getActivity(), startIndex, maxItems, searchStr);
                } else {
                    firstNetworkCall();
                }
            }
        });

        broadcasterSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchStr = broadcasterSearchEditText.getText().toString().trim();
                    if (!TextUtils.isEmpty(searchStr)) {
                        startIndex = 0;
                        KeyboardOp.hide(context, broadcasterSearchEditText);
                        getArtistListNetworkCall(getActivity(), startIndex, maxItems, searchStr);
                    }
                    return true;
                }
                return false;
            }
        });

        broadcasterSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String searchStr = s.toString().trim();

                if (searchStr.isEmpty()) {
                    firstNetworkCall();
                }

//                if (searchStr.length() > 2) { // TODO FOR NOW :D
                if (searchStr.length() > 0) {
                    if (runnable != null) {
                        handler.removeCallbacks(runnable);
                        runnable = null;
                    }
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (!searchStr.isEmpty()) {
                                startIndex = 0;
                                getArtistListNetworkCall(getActivity(), startIndex, maxItems, searchStr);
                            }
                        }
                    };
                    handler.postDelayed(runnable, 500);
                }
            }
        });

        btnBack.setOnClickListener(this);
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                if (getActivity() != null) {
                    Activities.goBackFragment(context, 1);
                }
                break;
            case R.id.noResultRefreshTextView:
                broadcasterSearchEditText.getText().clear();
                rv_swipe_refresh_tv.setRefreshing(true);
                firstNetworkCall();
                break;
        }
    }

    // METHOD TO GET ALL ARTIST DETAIL NETWORK CALL
    private void getArtistListNetworkCall(final Context context, int offset, int limit, String listType) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.artist,
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
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("artists");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<UserBO>>() {
                                }.getType();
                                List<UserBO> newStreams = (List<UserBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (myBroadcasterAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            myBroadcasterAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            myBroadcasterAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    /*userBOList = new ArrayList<>();
                                    userBOList.addAll(newStreams);*/
                                    setUpArtistRecycler(newStreams);
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

    // METHOD TO GET FIRST TIME VIEW DATA
    private void firstNetworkCall() {
        startIndex = 0;
        getArtistListNetworkCall(getActivity(), startIndex, maxItems, userSearchString);
    }

    // METHOD FOR FAN FOLLOW NETWORK CALL
    private void fanFollowNetworkCall(final Context context, String userID, final boolean followStatus, String userToken, final int index) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        if (followStatus) {
            serviceParams.put(Keys.FOLLOW_USER, userID);
        } else {
            serviceParams.put(Keys.UNFOLLOW_USER, userID);
        }
        tokenServiceHeaderParams.put(Keys.TOKEN, userToken);

        new WebServicesVolleyTask(context, true, "",
                followStatus ? EnumUtils.ServiceName.follow_user : EnumUtils.ServiceName.unfollow_user,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                if (myBroadcasterAdapter != null && myBroadcasterAdapter.getItemCount() > 0) {
                                    if (followStatus) {
                                        myBroadcasterAdapter.getItem(index).setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                    } else {
                                        myBroadcasterAdapter.getItem(index).setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                    }
                                    myBroadcasterAdapter.notifyDataSetChanged();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}