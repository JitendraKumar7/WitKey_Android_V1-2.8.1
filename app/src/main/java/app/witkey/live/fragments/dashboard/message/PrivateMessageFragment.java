package app.witkey.live.fragments.dashboard.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.PrivateConversationAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.DateTimeOp;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.pubnub.PubnubUtils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivateMessageFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.closeChatTab)
    CustomTextView closeChatTab;

    @BindView(R.id.mToolbarTitle)
    CustomTextView mToolbarTitle;

    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;

//    @BindView(R.id.swipe_refresh)
//    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;
    @BindView(R.id.privateChatEDT)
    CustomEditText privateChatEDT;

    private Set<ConversationBO> onlineConversationBOs = new HashSet<>();
    PrivateConversationAdapter conversationAdapter;

    private SubscribeCallback subscribeCallback;
    String targetChanel;
    String reciverName;
    UserBO userBO;
    int senderID = 0, receiverID = 0;
    boolean canCreate = false;

    public static PrivateMessageFragment newInstance(String userID, Boolean createChat, String targetChanel, String userName) {
        Bundle args = new Bundle();
        args.putString("USERID", userID);
        args.putBoolean("CREATECHANNEL", createChat);
        args.putString("TARGETCHANNEL", targetChanel);
        args.putString("USAERNAME", userName);
        PrivateMessageFragment fragment = new PrivateMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_private_message, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            if (getArguments() != null) {
                userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                receiverID = Integer.parseInt(getArguments().getString("USERID"));
                reciverName = getArguments().getString("USAERNAME");
                senderID = Integer.parseInt(userBO.getId());

                if (getArguments().getBoolean("CREATECHANNEL")) {
                    createChannel(senderID, receiverID);
                } else {
                    targetChanel = getArguments().getString("TARGETCHANNEL");
                }
            }
        } catch (Exception e) {
            LogUtils.e("PrivateMessageFragment", "" + e.getMessage());
        }

        initPubNub();
        showHideView();

        initView();
//        setUpFeaturedRecycler(messageBOList);
    }

    // METHOD TO INITIALIZE PUBNUB
    private void initPubNub() {
        addPubNubListener();
        subscribeChannel();
        getChatHistory();
    }


    // METHOD TO CREATE PUBNUB CHANNEL
    private void subscribeChannel() {
        PubnubUtils.getPubNubInstance().subscribe()
                .channels(Arrays.asList(targetChanel))// subscribe to channels
//                .channels(Arrays.asList(chatChannel, socialChannel))// subscribe to channels
                .execute();
    }

    // METHOD TO GET ALL CHAT MESSAGES HISTORY
    private void getChatHistory() {
        PubnubUtils.getPubNubInstance().history()
                .channel(targetChanel) // where to fetch history from chat channel
                .count(100) // how many items to fetch
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if (status.isError()) {
                            Toast.makeText(getActivity(), "Unable to fetch history", Toast.LENGTH_SHORT).show();
                        } else {
                            parseMessages(result.getMessages());
                        }
                    }
                });
    }

    // METHOD TO RETRIEVE AND POPULATE ALREADY SAVED MESSAGES
    private void parseMessages(List<PNHistoryItemResult> messages) {
        try {
            Gson gson = new Gson();
            ArrayList<ConversationBO> conversationBOs = new ArrayList<>();
            if (messages != null && messages.size() > 0) {
                for (PNHistoryItemResult pnHistoryItemResult : messages) {
                    try {

                        JsonElement json = pnHistoryItemResult.getEntry();
                        ConversationBO conversationBO = gson.fromJson(json, ConversationBO.class);

                        if (conversationBO.getSenderId().equals(userBO.getId())) {
                            conversationBO.setmRowType(ConversationBO.ME);
                        } else {
                            conversationBO.setmRowType(ConversationBO.OTHER);
                        }

                        if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
                            conversationBOs.add(conversationBO);
                        } else {
                            if (!conversationBO.getText().toString().trim().isEmpty()) {
                                //for the case of message...
                                conversationBOs.add(conversationBO);
                            } else {
                                //for the case of presence...
                                if (!isAlreadyExist(conversationBO, onlineConversationBOs)) {
                                    onlineConversationBOs.add(conversationBO);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (conversationBOs != null && conversationBOs.size() > 0)
                    setUpConversationAdapter(conversationBOs);
            } else {
                canCreate = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // METHOD TO ADD PUBNUB LISTENER THAT WILL RECEIVE ALL MESSAGES
    private void addPubNubListener() {
        subscribeCallback = new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
                    // internet got lost, do some magic and call reconnect when ready
                    pubnub.reconnect();
                } else if (status.getCategory() == PNStatusCategory.PNTimeoutCategory) {
                    // do some magic and call reconnect when ready
                    pubnub.reconnect();
                } else {
//                    log.error(status);
                }
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                Gson gson = new Gson();
                if (message.getChannel().equals(targetChanel)) {
                    try {
                        //on receive new message...
                        JsonElement jsonElement = message.getMessage();
                        final ConversationBO conversationBO = gson.fromJson(jsonElement, ConversationBO.class);
                        if (conversationBO != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (conversationBO.getSenderId() != null && !conversationBO.getSenderId().equals(userBO.getId())) {

                                        // WHEN MESSAGE TYPE IS CHAT MESSAGE
                                        if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.CHAT_MESSAGE) {
                                            if (!conversationBO.getText().isEmpty()) {
                                                conversationBO.setmRowType(ConversationBO.OTHER);
                                                addMessage(conversationBO);
                                            }

                                            // WHEN MESSAGE TYPE IS USER LIVE STATUS MESSAGE
                                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.LIVE_STATUS) {
                                            if (!isAlreadyExist(conversationBO, onlineConversationBOs)) {
                                                onlineConversationBOs.add(conversationBO);
                                                addMessage(conversationBO); // FOR CHAT JOIN MESSAGE
                                            }
                                            // WHEN MESSAGE TYPE IS GIFT SENT MESSAGE
                                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
                                            addMessage(conversationBO);
                                        }
                                    }
                                }
                            });

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    canCreate = true;
//                    Utils.showToast(getActivity(), "Channel not created");
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
//                    presence.get
            }
        };
        PubnubUtils.getPubNubInstance().addListener(subscribeCallback);
    }

    // METHOD TO ADD MESSAGE IN THE LIST
    private void addMessage(final ConversationBO conversationBO) {
        try {
            if (conversationAdapter != null) {
                conversationAdapter.addItem(conversationBO);
                recyclerView.smoothScrollToPosition(conversationAdapter.getItems().size() - 1);
            } else {
                ArrayList<ConversationBO> list = new ArrayList<ConversationBO>();
                list.add(conversationBO);
                setUpConversationAdapter(list);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // METHOD TO CHECK IF MESSAGE ALREADY EXISTS THEN TRUE
    private boolean isAlreadyExist(ConversationBO conversationBO, Set<ConversationBO> conversationBOs) {
        if (conversationBOs != null && conversationBOs.size() > 0) {
            for (ConversationBO conversationBO1 : conversationBOs) {
                if (conversationBO1.getSenderId().equals(conversationBO.getSenderId())) {
                    return true;
                }
            }
        }
        return false;
    }

    // METHOD TO POPULATE DATA ON CHAT LIST
    private void setUpConversationAdapter(final ArrayList<ConversationBO> list) {

        if (conversationAdapter != null) {
            conversationAdapter.addItems(list);
        } else {
            conversationAdapter = new PrivateConversationAdapter(list, getActivity());
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(conversationAdapter);

            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(context) {
                @Override
                protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }
            };

            smoothScroller.setTargetPosition(list.size() - 1);
            linearLayoutManager.startSmoothScroll(smoothScroller);

            //set adapter click listner...
            conversationAdapter.setClickListener(new PrivateConversationAdapter.ClickListeners() {
                @Override
                public void onRowClick(int position) {
                    ConversationBO conversationBO = conversationAdapter.getItem(position);
                    if (!conversationBO.getSenderId().equals(userBO.getId())) {
                        //if not the login user...
                        Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                    }

                }

                @Override
                public boolean onRowLongPressClick(int position) {
                    return false;
                }
            });
        }
    }

    private void showNoResult(boolean isShow) {
        if (isShow) {
//            swipeRefreshLayout.setVisibility(View.GONE);
            noResultTextView.setVisibility(View.VISIBLE);
            noResultTextView.setText(R.string.no_featured_result);
        } else {
            noResultTextView.setVisibility(View.GONE);
//            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void initView() {
        mToolbarTitle.setText(reciverName);
//        mToolbarTitle.setText(targetChanel);
        btnBack.setOnClickListener(this);
        closeChatTab.setOnClickListener(this);

        /*privateChatEDT.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    sendTextMsg();
                }
                return false;
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.closeChatTab:
                sendTextMsg();
                break;
        }
    }

    private void createChannel(int senderID, int receiverID) {

        String receiver = receiverID + "";
        if (receiver.equals("-1")) {
            targetChanel = "admin-chat-with-" + senderID;
        } else {
            if (senderID > receiverID) {
                targetChanel = "(" + senderID + ")" + UserSharedPreference.readUserPrivateChatKey() + "(" + receiverID + ")";
            } else {
                targetChanel = "(" + receiverID + ")" + UserSharedPreference.readUserPrivateChatKey() + "(" + senderID + ")";
            }
        }
        /*if (senderID > receiverID) {
            targetChanel = "(" + senderID + ")" + Constants.PRIVATE_CHAT_STRING + "(" + receiverID + ")";
        } else {
            targetChanel = "(" + receiverID + ")" + Constants.PRIVATE_CHAT_STRING + "(" + senderID + ")";
        }*/

        /*CALL SERVER HERE TO SAVE CHAT CHANNEL ID*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearPubNub();
    }

    // METHOD TO DESTROY PUBNUB OBJECT
    private void clearPubNub() {
        PubnubUtils.getPubNubInstance().unsubscribeAll();
        PubnubUtils.getPubNubInstance().removeListener(subscribeCallback);
//        PubnubUtils.getPubNubInstance().destroy();
    }

    // METHOD TO SEND TEXT MESSAGE TO PUBNUB SERVER
    private void sendMessage(final ConversationBO conversationBO) {
//        createChatChannelNetworkCall(getActivity(), senderID + "", receiverID + "", targetChanel);
        if (canCreate) {
            createChatChannelNetworkCall(getActivity(), senderID + "", receiverID + "", targetChanel);
            canCreate = false;
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String message = gson.toJson(conversationBO);
        LogUtils.i(TAG, "sendMessage: " + message);
        JsonElement element = gson.fromJson(message, JsonElement.class);
        PubnubUtils.getPubNubInstance().publish()
                .message(element)
                .channel(targetChanel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {

                        if (status.isError()) {
                            Toast.makeText(getActivity(), "Error on sending message", Toast.LENGTH_SHORT).show();
                        } else {
                            // WHEN MESSAGE TYPE IS GIFT MESSAGE
                            if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
                                if (conversationBO.getSenderId() != null && !conversationBO.getSenderId().equals(userBO.getId())) {
//                                    addBubble(conversationBO.getSocialGiftID());
                                }
                                addMessage(conversationBO);

                                // WHEN MESSAGE TYPE IS CHAT MESSAGE
                            } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.CHAT_MESSAGE) {
                                if (!conversationBO.getText().isEmpty()) {
                                    conversationBO.setmRowType(ConversationBO.ME);
                                    addMessage(conversationBO);
                                }

                                // WHEN MESSAGE TYPE IS USER LIVE STATUS MESSAGE
                            } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.LIVE_STATUS) {
                                if (conversationBO.getSenderId() != null && !conversationBO.getSenderId().equals(userBO.getId())) {
                                    if (!isAlreadyExist(conversationBO, onlineConversationBOs)) {
                                        onlineConversationBOs.add(conversationBO);
                                    }
                                }
                            }
                        }
                    }
                });
    }

    // METHOD TO SEND TEXT MESSAGE TO PUBNUB SERVER
    private void sendTextMsg() {

        String text = privateChatEDT.getText().toString().trim();

        if (!TextUtils.isEmpty(text)) {

            String date = DateTimeOp.getCurrentDateTime(Constants.dateFormat3) +
                    " at " + DateTimeOp.getCurrentDateTime(Constants.dateFormat29);

            ConversationBO conversationBO = new ConversationBO(userBO.getProfilePictureUrl(),
                    Utils.getCurrentTimeStamp_Streams(), userBO.getFullName(), userBO.getId(), Utils.checkForProfanity(getActivity(), text), userBO.getUsername(), EnumUtils.ConversationType.CHAT_MESSAGE, 0, 0, 1, "TEMP URL", 0);

            privateChatEDT.setText("");
            sendMessage(conversationBO);
            KeyboardOp.hide(getActivity());
        }
    }

    //service name: add-chat'
    //Type: Post
    //Params: params: user_id, friend_user_id, chat_id
    // METHOD TO MAKE NETWORK CALL TO CREATE CHAT CHANNEL
    public static void createChatChannelNetworkCall(final Context context, String userID, String friendUserID, String targetChanel) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.FRIEND_USER_ID, friendUserID);
        serviceParams.put(Keys.PRIVATE_CHAT_ID, targetChanel);

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.add_chat,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        // DO SOME THING HERE
//                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                // DO SOME THING HERE


                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        // if response is successful then do something
                    }
                }
            }
        });
    }
}