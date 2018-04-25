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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.NotificationsAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.switchLiveNotification)
    SwitchButton switchLiveNotification;
    @BindView(R.id.switchMessageNotification)
    SwitchButton switchMessageNotification;
    @BindView(R.id.notificationsRecyclerView)
    RecyclerView notificationsRecyclerView;

    private NotificationsAdapter notificationsAdapter;
//    private List<NotificationListBO> notificationListBOs;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 2;
    private int startIndex = 0;
    int totalRecords = 0;
    boolean viewDisable = false;
    UserBO userBO;

    public static NotificationsFragment newInstance() {
        Bundle args = new Bundle();
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notifications, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setTitleBarData();
        showHideView();
        updateNotificationChecks();
        initViews();
//        setUpData();
    }


    private void updateNotificationChecks() {
        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            if (userBO != null) {
                switchLiveNotification.setChecked(userBO.getLive_notification_status() == 1);
                switchMessageNotification.setChecked(userBO.getMessage_notification_status() == 1);
                viewDisable = userBO.getLive_notification_status() == 2;
                if (notificationsAdapter != null) {
                    notificationsAdapter.viewDisable(viewDisable);
                }
            }
        } catch (Exception e) {
            LogUtils.e("UserProfileScrollViewFragment", "" + e.getMessage());
        }
    }

    private void initViews() {

        if (notificationsAdapter != null) {
            notificationsAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getArtistListNetworkCall(getActivity(), startIndex, maxItems, "NONE", viewDisable);
            }
        });

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    startIndex = 0;
                    getArtistListNetworkCall(getActivity(), startIndex, maxItems, "NONE", viewDisable);
                }
            });
        }

        btnBack.setOnClickListener(this);
        switchLiveNotification.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.notifications);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    /*private void setUpData() {

        notificationListBOs = new ArrayList<>();

        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Smith", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Jake", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Connor", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Wyatt", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Cody", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Scott", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Logan", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Jacob", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Maxwell", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Emma", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Carly", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Heather", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Katherine", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Allison", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Troy", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Frank", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Marc", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Dominic", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Isaac", true));
        notificationListBOs.add(new NotificationListBO("https://www.w3schools.com/w3css/img_avatar3.png", "Gregory", true));
    }*/

    private void setUpArtistRecycler(final List<UserBO> userBOList, final boolean viewDisable) {

        if (userBOList != null && userBOList.size() > 0) {
            if (notificationsAdapter == null) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                notificationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                notificationsRecyclerView.setLayoutManager(linearLayoutManager);
                notificationsAdapter = new NotificationsAdapter(userBOList, getContext(), notificationsRecyclerView, viewDisable);
                notificationsRecyclerView.setAdapter(notificationsAdapter);

                notificationsAdapter.setClickListener(new NotificationsAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.notificationSwitch:
                                if (position < 0 || position > userBOList.size())
                                    return;

                                boolean status = ((SwitchButton) view).isChecked();
                                setNotificationParamNetworkCall(getActivity(), userBO.getId(), userBOList.get(position).getId(), status ? EnumUtils.Notification.ALLOW : EnumUtils.Notification.DISALLOW);
                                break;
                            case R.id.notificationParentFrame:
                            case R.id.notificationPersonImageView:
                            case R.id.notificationNameCustomTextView:
                                if (position < 0 || position > userBOList.size())
                                    return;
                                try {
                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.get(position), FanProfileScrollViewFragment.TYPE_USERBO));
                                } catch (Exception e) {
                                    LogUtils.e("SearchBroadcasterFragment", "" + e.getMessage());
                                }
                                break;
                        }
                    }
                });

                notificationsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (notificationsAdapter != null && notificationsAdapter.getItems().size() >= maxItems
                                && notificationsAdapter.getItems().size() < totalRecords) {
                            notificationsAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getArtistListNetworkCall(getActivity(), startIndex, maxItems, "NONE", viewDisable);
                        }
                    }
                });
            } else {
                notificationsAdapter.addItems(userBOList);
                notificationsAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }


    /*private void setUpNotificationsRecycler(final List<NotificationListBO> notificationListBOList) {

        if (notificationListBOList != null && notificationListBOList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            notificationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            notificationsRecyclerView.setLayoutManager(linearLayoutManager);
            notificationsAdapter = new NotificationsAdapter(notificationListBOList, getContext(), notificationsRecyclerView);
            notificationsRecyclerView.setAdapter(notificationsAdapter);
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                if (getActivity() != null)
                    getActivity().onBackPressed();
                break;
            case R.id.switchMessageNotification:
                userSignUpNetworkCall(getActivity(), userBO.getId(), true, switchMessageNotification.isChecked() ? 1 : 2, UserSharedPreference.readUserToken());
                break;
            case R.id.switchLiveNotification:
                viewDisable = !switchLiveNotification.isChecked();
                startIndex = 0;
                if (notificationsAdapter != null) {
                    notificationsAdapter.viewDisable(viewDisable);
                }

                swipeRefreshLayout.setRefreshing(true);
                getArtistListNetworkCall(getActivity(), startIndex, maxItems, "NONE", viewDisable);

                userSignUpNetworkCall(getActivity(), userBO.getId(), false, switchLiveNotification.isChecked() ? 1 : 2, UserSharedPreference.readUserToken());
                /*CALL API HERE*/
                break;
        }
    }

    // METHOD TO GET ALL ARTIST DETAIL NETWORK CALL
    private void getArtistListNetworkCall(final Context context, int offset, int limit, String listType, final boolean viewDisable) {

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
                        if (getUserVisibleHint()) {
                            showNoResult(true);
                        }
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("artists");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<UserBO>>() {
                                }.getType();
                                List<UserBO> newArtist = (List<UserBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (notificationsAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            notificationsAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            notificationsAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpArtistRecycler(newArtist, viewDisable);
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

    // METHOD TO SET NOTIFICATION PARAMS NETWORK CALL
    //Service Name: allow_notification
    //user_id, friend_user_id, status{20: allow, 10: not allow}
    //type post
    private void setNotificationParamNetworkCall(final Context context, String user_id, String userFriendID, int allowStatus) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.USER_ID, user_id);
        serviceParams.put(Keys.FRIEND_USER_ID, userFriendID);
        serviceParams.put(Keys.STATUS, allowStatus);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.allow_notification,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint()) {
                            showNoResult(true);
                        }
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {


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

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        } else {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    // METHOD FOR NOTIFICATION SETTING NETWORK CALL THIS METHOD (setMultipartParameter) TO CALL SERVER USED BCZ SERVER SIDE WANT SAME REQUEST AS IN UPDATE PROFILE
    //message_notification_status
//    live_notification_status
    // 1 for , 2 for off
    private void userSignUpNetworkCall(final Context context, String userID, boolean typeMessage, int status, final String userToken) {
        String serverUrl = "";
        if (Constants.appDomain == EnumUtils.AppDomain.LIVE) {
            serverUrl = context.getResources().getString(R.string.base_url_live);
        } else if (Constants.appDomain == EnumUtils.AppDomain.QA) {
            serverUrl = context.getResources().getString(R.string.base_url_qa);
        } else if (Constants.appDomain == EnumUtils.AppDomain.DEV) {
            serverUrl = context.getResources().getString(R.string.base_url_dev);
        }

        serverUrl = serverUrl + WebServiceUtils.filterServiceName(EnumUtils.ServiceName.user.toString());

        Builders.Any.B builder = Ion.with(context).load("POST", serverUrl);

        builder.setHeader(Keys.TOKEN, userToken);
        builder.setMultipartParameter(Keys.METHOD, "PATCH");
        builder.setMultipartParameter(Keys.USER_ID, userID);
        if (typeMessage) {
            builder.setMultipartParameter(Keys.MESSAGE_NOTIFICATION, status + "");
        } else {
            builder.setMultipartParameter(Keys.LIVE_NOTIFICATION, status + "");
        }
        builder.setMultipartContentType("multipart/form-data");

        builder.asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            Utils.showToast(context, "Error");

                        } else {

                            TaskItem taskItem = WebServiceUtils.parseResponse(result.toString(),
                                    EnumUtils.ServiceName.user);

                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = new JSONObject(taskItem.getResponse());

                                    if (jsonObject.has("errors")) {
                                        JSONObject jsonError = jsonObject.getJSONObject("errors");
                                        // CAN SHOW ERROR HERE

                                    } else if (jsonObject.has("user")) {
                                        Gson gson = new Gson();
                                        UserBO userBO = gson.fromJson(jsonObject.get("user").toString(), UserBO.class);

                                        if (userBO != null) {

                                            UserSharedPreference.saveUserToken(userToken);
                                            userBO.setToken(userToken);
                                            ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
                                            updateNotificationChecks();
                                        }
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }
}