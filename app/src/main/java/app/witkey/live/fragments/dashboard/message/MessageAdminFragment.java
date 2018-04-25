package app.witkey.live.fragments.dashboard.message;

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
import app.witkey.live.adapters.dashboard.message.MessageAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.AdminNotificationBO;
import app.witkey.live.items.MessageBO;
import app.witkey.live.items.MomentBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdminFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.mToolbarTitle)
    CustomTextView mToolbarTitle;

    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    MessageAdapter messageAdapter;
    List<MessageBO> messageBOList;
    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;

    String selectionType;
    String user_img;
    String user_id;
    String title;

    public static MessageAdminFragment newInstance(String type_URL, String type_ID, String title) {
        Bundle args = new Bundle();
        args.putString("IMG_URL", type_URL);
        args.putString("TYPE_ID", type_ID);
        args.putString("TYPE_TITLE", title);
        MessageAdminFragment fragment = new MessageAdminFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message_admin, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            user_img = bundle.getString("IMG_URL");
            user_id = bundle.getString("TYPE_ID");
            selectionType = bundle.getString("TYPE_TITLE");
        }

        showHideView();
        initView();
    }

    private void setUpFeaturedRecycler(List<AdminNotificationBO> messageBOList) {

        if (messageBOList != null && messageBOList.size() > 0) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            messageAdapter = new MessageAdapter(messageBOList, context, mRecyclerView, user_img);
            mRecyclerView.setAdapter(messageAdapter);
        } else {
            showNoResult(true);
        }

    }

    private void showNoResult(boolean isShow) {
        swipeRefreshLayout.setRefreshing(false);
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void initView() {

        if (messageAdapter != null) {
            messageAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getAdminChatListNetworkCall(getActivity(), startIndex, maxItems, user_id);
            }
        });

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    startIndex = 0;
                    getAdminChatListNetworkCall(getActivity(), startIndex, maxItems, user_id);
                }
            });
        }
        mToolbarTitle.setText(selectionType);
        btnBack.setOnClickListener(this);
        noResultRefreshTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.noResultRefreshTextView:
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            startIndex = 0;
                            getAdminChatListNetworkCall(getActivity(), startIndex, maxItems, user_id);
                        }
                    });
                }
                break;
        }
    }

    // METHOD TO GET ALL ADMIN NOTIFICATION NETWORK CALL
    //http://18.220.157.19/witkey/api/get-notifications/1
    private void getAdminChatListNetworkCall(final Context context, int offset, int limit, String listType) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
//        serviceParams.put(Keys.LIST_OFFSET, offset);
//        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.get_notifications,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (messageAdapter == null) {
                            showNoResult(true);
                        }
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("notification");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<AdminNotificationBO>>() {
                                }.getType();
                                List<AdminNotificationBO> newStreams = (List<AdminNotificationBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

//                                totalRecords = jsonObject.getInt("total_records");
//                                if (totalRecords == 0) {
//                                    showNoResult(true);
//                                } else {
                                if (messageAdapter != null) {
                                    if (startIndex != 0) {
                                        //for the case of load more...
                                        messageAdapter.removeLoadingItem();
                                    } else {
                                        //for the case of pulltoRefresh...
                                        messageAdapter.clearItems();
                                    }
                                }

                                showNoResult(false);
                                setUpFeaturedRecycler(newStreams);
//                                }
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