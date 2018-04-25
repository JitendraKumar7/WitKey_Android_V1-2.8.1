package app.witkey.live.fragments.dashboard;

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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

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
import app.witkey.live.adapters.dashboard.message.PrivateChatAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.message.MessageAdminFragment;
import app.witkey.live.fragments.dashboard.message.MessageYourLittleHelperFragment;
import app.witkey.live.fragments.dashboard.message.PrivateMessageFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.PrivateChatBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;

    @BindView(R.id.messageFriends_LL)
    LinearLayout messageFriends_LL;

    @BindView(R.id.messageUnread_LL)
    LinearLayout messageUnread_LL;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.messageSelection)
    RadioGroup messageSelection;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipe_refresh;
    @BindView(R.id.rv_swipe_refresh_tv)
    SwipeRefreshLayout rv_swipe_refresh_tv;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    PrivateChatAdapter privateChatAdapter;
    UserBO userBO;
    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;
    private int oldScrollY = 0;
    private boolean loadMoreStatus = true;

    public static MessageFragment newInstance() {
        Bundle args = new Bundle();
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            if(userBO==null) userBO=new UserBO();
        } catch (Exception e) {
            LogUtils.e("MessageFragment", "" + e.getMessage());
        }

        initView();
        showHideView();
    }

    private void showHideView() {
        btnBack.setVisibility(View.GONE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
        mToolbarTitle.setText(R.string.inbox);
    }

    private void initView() {
        if (privateChatAdapter != null) {
            privateChatAdapter = null;
        }
        btnBack.setOnClickListener(this);
        noResultRefreshTextView.setOnClickListener(this);

        swipe_refresh.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getChatListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
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
                getChatListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
            }
        });


        if (swipe_refresh != null) {
            swipe_refresh.post(new Runnable() {
                @Override
                public void run() {
                    swipe_refresh.setRefreshing(true);
                    startIndex = 0;
                    getChatListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                }
            });
        }

        messageSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.friends_RB:
                        friendUnreadToggle(true);
                        break;
                    case R.id.unread_RB:
                        friendUnreadToggle(false);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noResultRefreshTextView:
                if (rv_swipe_refresh_tv != null) {
                    rv_swipe_refresh_tv.post(new Runnable() {
                        @Override
                        public void run() {
                            rv_swipe_refresh_tv.setRefreshing(true);
                            startIndex = 0;
                            getChatListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                        }
                    });
                }
                break;
        }
    }

    private void friendUnreadToggle(boolean showFriends) {
        if (showFriends) {
            messageUnread_LL.setVisibility(View.GONE);
            messageFriends_LL.setVisibility(View.VISIBLE);
        } else {
            messageUnread_LL.setVisibility(View.VISIBLE);
            messageFriends_LL.setVisibility(View.GONE);
        }
    }

    private void setUpChatViewRecycler(final List<PrivateChatBO> streamBOs) {

        if (streamBOs != null && streamBOs.size() > 0) {
            if (privateChatAdapter == null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                //streamBOs.add(0, new StreamBO());
                privateChatAdapter = new PrivateChatAdapter(streamBOs, getContext(), mRecyclerView);
                mRecyclerView.setAdapter(privateChatAdapter);
                mRecyclerView.setNestedScrollingEnabled(false);
                privateChatAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (privateChatAdapter != null && privateChatAdapter.getItems().size() >= maxItems
                                && privateChatAdapter.getItems().size() < totalRecords) {
                            privateChatAdapter.addLoadingItem();
                            startIndex = startIndex + 1;
                            getChatListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                        }
                    }
                });
                privateChatAdapter.setClickListener(new PrivateChatAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {
                            case R.id.fanNameCustomTextView:
                            case R.id.fanParentFrame:
                            case R.id.fanImageView:
                            case R.id.decTextView:
                                try {
                                    if (streamBOs.get(position).isShowDesc()) {
                                        if (streamBOs.get(position).getChat_id().equals("-1")) {
                                            gotoNextFragment(PrivateMessageFragment.newInstance("-1", true, streamBOs.get(position).getChat_id(), streamBOs.get(position).getAdminTitle()));
                                        } else {
                                            gotoNextFragment(MessageAdminFragment.newInstance(streamBOs.get(position).getAdminURL(), streamBOs.get(position).getChat_id(), streamBOs.get(position).getAdminTitle()));
                                        }
                                    } else {
                                        gotoNextFragment(PrivateMessageFragment.newInstance("0", false, streamBOs.get(position).getChat_id(), streamBOs.get(position).getUser_details().getUsername()));
                                    }
                                } catch (Exception e) {
                                    LogUtils.e("setUpChatViewRecycler", "" + e.getMessage());
                                }
                                break;
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
                privateChatAdapter.addItems(streamBOs);
                privateChatAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    //service name: moment-action
//    http://18.220.157.19/witkey/api/get-chat/10/0/463
    private void getChatListNetworkCall(final Context context, final int offset, int limit, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.get_chat,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        // DO SOMETHING
                        showNoResult(true);
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                List<PrivateChatBO> chatBOList = new ArrayList<>();
                                chatBOList.add(new PrivateChatBO(true, "Admin", "Chat with admin", "http://10.100.28.38:8080/Witkey/public/images-notifications/admin.png", "-1"));
                                if (jsonObject.has("adminNotification") && offset == 0) {
                                    JSONArray adminChatJsonArray = jsonObject.getJSONArray("adminNotification");
                                    if (adminChatJsonArray != null && adminChatJsonArray.length() > 0) {
                                        for (int index = 0; index < adminChatJsonArray.length(); index++) {
                                            try {
                                                JSONObject adminChatObject = adminChatJsonArray.getJSONObject(index);
                                                chatBOList.add(new PrivateChatBO(true, adminChatObject.getString("name"), adminChatObject.getString("notification_text"),
                                                        adminChatObject.getString("icon_url"), adminChatObject.getString("id")));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                if (jsonObject.has("chat")) {
                                    JSONArray chatJsonArray = jsonObject.getJSONArray("chat");

                                    Gson gson = new Gson();
                                    Type listType = new TypeToken<List<PrivateChatBO>>() {
                                    }.getType();
                                    List<PrivateChatBO> chatList = (List<PrivateChatBO>) gson.fromJson(chatJsonArray.toString(),
                                            listType);
                                    chatBOList.addAll(chatList);

                                    totalRecords = jsonObject.getInt("total_records");
                                }
                                if (chatBOList == null || chatBOList.size() == 0) {
                                    showNoResult(true);
                                } else {

                                    if (privateChatAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            privateChatAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            privateChatAdapter.clearItems();
                                        }
                                    }
                                    showNoResult(false);
                                    setUpChatViewRecycler(chatBOList);
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            showNoResult(true);
                        }
                    }
                }
            }
        });
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipe_refresh.setRefreshing(false);
            swipe_refresh.setVisibility(View.GONE);
            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.VISIBLE);
        } else {
            rv_swipe_refresh_tv.setVisibility(View.GONE);
            swipe_refresh.setVisibility(View.VISIBLE);
            rv_swipe_refresh_tv.setRefreshing(false);
            swipe_refresh.setRefreshing(false);
        }
    }


}