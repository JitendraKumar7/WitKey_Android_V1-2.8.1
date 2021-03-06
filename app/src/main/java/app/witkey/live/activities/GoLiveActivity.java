package app.witkey.live.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arasthel.asyncjob.AsyncJob;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.lion.materialshowcaseview.IShowcaseListener;
import com.lion.materialshowcaseview.MaterialShowcaseSequence;
import com.lion.materialshowcaseview.MaterialShowcaseView;
import com.lion.materialshowcaseview.ShowcaseConfig;
import com.lion.materialshowcaseview.TutorialViewType;
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

import app.witkey.live.fragments.dashboard.profile.BroadcastRankActivity;
import app.witkey.live.fragments.dashboard.stream.LiveFeedEndedFragment;
import app.witkey.live.items.StreamEndSummaryBO;
import app.witkey.live.utils.WebServiceUtils;

import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.android.graphics.WZBitmap;
import com.wowza.gocoder.sdk.api.android.opengl.WZGLES;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.devices.WZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WZCamera;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.errors.WZError;
import com.wowza.gocoder.sdk.api.errors.WZStreamingError;
import com.wowza.gocoder.sdk.api.geometry.WZSize;
import com.wowza.gocoder.sdk.api.graphics.WZColor;
import com.wowza.gocoder.sdk.api.render.WZRenderAPI;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.adapters.dashboard.profile.MyWalletAdapter;
import app.witkey.live.adapters.dashboard.stream.GoLiveSlideViewAdapter;
import app.witkey.live.adapters.dashboard.stream.GoLiveTopAdapter;
import app.witkey.live.adapters.dashboard.stream.StreamingConversationAdapter;
import app.witkey.live.fragments.dashboard.message.PrivateMessageFragment;
import app.witkey.live.fragments.dashboard.payment.WebviewFragment;
import app.witkey.live.fragments.dashboard.stream.DailyMissionEnergyFragment;
import app.witkey.live.fragments.dashboard.stream.GoLiveStartPageFragment;
import app.witkey.live.fragments.dashboard.stream.SetOfStickersFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.items.GiftItemBO;
import app.witkey.live.items.PackagesBO;
import app.witkey.live.items.ProgrammeBO;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.items.WalletBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.DateTimeOp;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.ScreenShotUtils;
import app.witkey.live.utils.StreamUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.animations.BubbleViewAnimation;
import app.witkey.live.utils.animations.giftbullet.GiftSendModel;
import app.witkey.live.utils.animations.giftbullet.GiftView;
import app.witkey.live.utils.circleprogressbar.CircleProgressBar;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.customviews.FullScreenVideoView;
import app.witkey.live.utils.dialogs.GoLiveBroadcastGiftPanelDialog;
import app.witkey.live.utils.dialogs.GoLiveBroadcastGiftReceivedDialog;
import app.witkey.live.utils.dialogs.GoLiveDollarsDialog;
import app.witkey.live.utils.dialogs.GoLiveHomeProfileDialog;
import app.witkey.live.utils.dialogs.GoLiveInviteOptionsDialog;
import app.witkey.live.utils.dialogs.GoLiveMenuOptionsDialog;
import app.witkey.live.utils.dialogs.GoLiveShareDialog;
import app.witkey.live.utils.dialogs.GoLiveViewersProfilePageDialog;
import app.witkey.live.utils.dialogs.GoLiveViewersSummaryDialog;
import app.witkey.live.utils.dialogs.ShareStreamDialog;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.pubnub.PubnubUtils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import app.witkey.live.utils.wowza.WowzaUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static app.witkey.live.WitKeyApplication.context;

public class GoLiveActivity extends BaseActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        View.OnClickListener,
        WZStatusCallback, AnimationListener {

    private final static String TAG = GoLiveActivity.class.getSimpleName();
    Context mContext;

    @BindView(R.id.camera)
    WZCameraView mCameraView;

    private static final String SHOWCASE_ID = "GoLive";

    @BindView(R.id.goLiveParentView)
    LinearLayout goLiveParentView;

    @BindView(R.id.oldChatView)
    LinearLayout oldChatView;

    @BindView(R.id.goLiveChatViewer)
    LinearLayout goLiveChatViewer;

    @BindView(R.id.goLiveChatCreator)
    LinearLayout goLiveChatCreator;

    @BindView(R.id.page_two)
    LinearLayout page_two;

    @BindView(R.id.screenShotView)
    LinearLayout screenShotView;

    @BindView(R.id.screenShotPopupSave)
    CustomTextView screenShotPopupSave;

    @BindView(R.id.screenShotPopupShare)
    CustomTextView screenShotPopupShare;

    @BindView(R.id.goLiveChatEDT)
    CustomEditText goLiveChatEDT;

    @BindView(R.id.goLiveChatViewerEDT)
    CustomEditText goLiveChatViewerEDT;

    @BindView(R.id.screenShotImageView)
    ImageView screenShotImageView;

    @BindView(R.id.topAnimationImageView)
    ImageView topAnimationImageView;

    @BindView(R.id.gifImageView)
    GifImageView gifImageView;

    @BindView(R.id.userImageBlur)
    ImageView userImageBlur;

    @BindView(R.id.goLiveBottomOpt1)
    LinearLayout goLiveBottomOpt1;

    @BindView(R.id.goLiveBottomOpt2)
    LinearLayout goLiveBottomOpt2;

    @BindView(R.id.goLiveTopView)
    LinearLayout goLiveTopView;

    @BindView(R.id.goLiveCenterView)
    LinearLayout goLiveCenterView;

    @BindView(R.id.goLiveBottomOpt3)
    LinearLayout goLiveBottomOpt3;

    @BindView(R.id.goLiveBottomOpt4)
    LinearLayout goLiveBottomOpt4;

    @BindView(R.id.goLiveBottomTabParent)
    LinearLayout goLiveBottomTabParent;

    @BindView(R.id.goLiveBottomOpt5)
    LinearLayout goLiveBottomOpt5;

    @BindView(R.id.goLiveListParentLL)
    LinearLayout goLiveListParentLL;

    @BindView(R.id.goLiveBottomTab)
    LinearLayout goLiveBottomTab;

    @BindView(R.id.goLiveBottomOpt6)
    LinearLayout goLiveBottomOpt6;

    @BindView(R.id.animationViewParent)
    LinearLayout animationViewParent;

    @BindView(R.id.goLiveViewerChatCamera)
    LinearLayout goLiveViewerChatCamera;

    @BindView(R.id.goLiveViewerChatFolder)
    LinearLayout goLiveViewerChatFolder;

    @BindView(R.id.goLiveViewerChatGift)
    LinearLayout goLiveViewerChatGift;

    @BindView(R.id.goLiveViewerChatPost)
    LinearLayout goLiveViewerChatPost;

    @BindView(R.id.goLiveViewerChatMessage)
    LinearLayout goLiveViewerChatMessage;

    @BindView(R.id.userImageParentFrame)
    LinearLayout userImageParentFrame;

    @BindView(R.id.userImageIV)
    ImageView userImageIV;

    @BindView(R.id.userNameTextView)
    CustomTextView userNameTextView;

    @BindView(R.id.userPointsTextView)
    CustomTextView userPointsTextView;

    @BindView(R.id.userDiamondTextView)
    CustomTextView userDiamondTextView;

    @BindView(R.id.userCashTextView)
    CustomTextView userCashTextView;

    @BindView(R.id.userViewImageView)
    ImageView userViewImageView;

    @BindView(R.id.eventPosterIV)
    ImageView eventPosterIV;

    @BindView(R.id.userCancelImageView)
    ImageView userCancelImageView;

    @BindView(R.id.closeChatTab)
    ImageView closeChatTab;

    @BindView(R.id.screenShotCloseImageView)
    ImageView screenShotCloseImageView;

    @BindView(R.id.mRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.cameraMessagesParentFrame)
    RelativeLayout cameraMessagesParentFrame;

    @BindView(R.id.cameraMessages)
    CustomTextView cameraMessages;

    @BindView(R.id.userViewParent)
    LinearLayout userViewParent;

    @BindView(R.id.dailyMissionEnergyImageView)
    ImageView dailyMissionEnergyImageView;

    @BindView(R.id.giftSlideInView)
    LinearLayout giftSlideInView;

    @BindView(R.id.pointsTypeIV)
    ImageView pointsTypeIV;

    @BindView(R.id.userCashParentFrame)
    RelativeLayout userCashParentFrame;

    @BindView(R.id.progressBar1)
    ProgressBar progressBar;
    @BindView(R.id.video_view)
    FullScreenVideoView mVideoView;

    @BindView(R.id.bubbleViewGoLive)
    BubbleViewAnimation bubbleViewGoLive;

    @BindView(R.id.custom_progress)
    CircleProgressBar custom_progress;

    @BindView(R.id.userProfileView)
    RelativeLayout userProfileView;

    @BindView(R.id.userCoinsCountView)
    LinearLayout userCoinsCountView;

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.giftBulletView)
    GiftView giftBulletView;

    String giftJsonString = "";
    public static HashMap<Integer, GiftItemBO> giftCount;
    private List<WalletBO> walletBOs;
    private MyWalletAdapter myWalletAdapter;

    protected boolean mDetectingFaces = false;
    boolean fromEndSummary = false;
    boolean is_face_detected = false;
    GoLiveTopAdapter goLiveTopAdapter;
    private UserBO userBO;
    GifDrawable gifDrawable;

    public static final int RECORD_AUDIO_REQUEST_CODE = 200;
    public static final int REQUEST_STORAGE_PERMISSION = 201;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String FRAGMENT_DIALOG = "dialog";
    public static String ARG_TYPE = "TYPE";
    public static String ARG_PARAM_1 = "GOLIVE";
    public static String ARG_PARAM_2 = "PLAYSTREAM";
    public static String ARG_PARAM_3 = "STREAMBO";
    public static String ARG_PARAM_4 = "USER_IMAGE";
    public static String ARG_PARAM_5 = "TUTORIAL";
    boolean streamPlayStatus = false;
    private String chatChannel = "";//"1122-CHAT"; // FOR TESTING ONLY
    private String streamVideoURL = "";

    private AtomicBoolean mGrabFrame = new AtomicBoolean(false);
    private AtomicBoolean mSavingFrame = new AtomicBoolean(false);
    private Set<ConversationBO> onlineConversationBOs = new HashSet<>();
    StreamingConversationAdapter conversationAdapter;

    //wowza
    private WZBroadcastConfig goCoderBroadcastConfig;
    private WZBroadcast goCoderBroadcaster;
    private WZAudioDevice goCoderAudioDevice;
    WowzaGoCoder goCoder;
    Bitmap bitmap;
    StreamBO streamBO;
    ProgressDialog progressDialog;
    private SubscribeCallback subscribeCallback;

    private Dialog dialogEndBroadcast;
    private Dialog dialogPurchaseCoins;

    private boolean beautyModeOn = false;
    private boolean hdQualityOn = false;
    private boolean tutorials = false;

    // face detection
//    private AtomicBoolean mGrabFrame = new AtomicBoolean(false);
    private Bitmap wowzaFramebitmap;
    private SparseArray<Face> mFaces;
    private Face chosenFace = null;
    private FaceDetector detector;
    private WZBitmap mNinjaFace;
    public static long startTime = 0;
    public static int SHARE_MOMENT = 100;

    int progressCount = 0;
    boolean loopChatJoinAnimationStatus = true;
    Handler handler = new Handler();
    Handler handlerJoinChat = new Handler();

    private ArrayList<ProgrammeBO> programmeBOs;

    // todo
    //private MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        mContext = this;
        if (bundle != null) {
            String type = bundle.getString(ARG_TYPE);
            if (type.equals(ARG_PARAM_1)) {
                streamPlayStatus = false;
                streamBO = getIntent().getParcelableExtra(ARG_PARAM_3);

                // face detection
               /* initFaceDetector();
                initVideoFrameRenderer();*/

                goLive(streamBO);
            } else if (type.equals(ARG_PARAM_2)) {
                streamPlayStatus = true;
                streamBO = getIntent().getParcelableExtra(ARG_PARAM_3);
                playVideo(streamBO);
            } else if (type.equals(ARG_PARAM_5)) {
                initView();
                streamPlayStatus = true;
                tutorials = true;
                showHideViewOnPlayStream(true);
                startTutorials();
                initPubNub(false);
            }
        }
        startTime = System.currentTimeMillis();

        getEventPostersNetworkCall(GoLiveActivity.this);
    }

    private void initFaceDetector() {
        detector = new FaceDetector.Builder(this)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .build();
    }

    // METHOD TO PLAY SAVED STREAM VIDEOS
    private void playVideo(StreamBO streamBO) {
        initView();
        giftCount = new HashMap<>();

//        showChatJoinBubbleAnimation();
//        checkForFullAnimation(500);
//        showGiftTimer();
//        startTutorials();
//        playGifsWithGlide("");

        showHideViewOnPlayStream(true);
        playStreamVideo(streamBO);
        if (userBO != null) {
            liveViewersCountNetworkCall(this, StreamUtils.ACTION_VIEW, userBO.getId(), streamBO.getId(), UserSharedPreference.readUserToken());
            if (streamBO.getStatus().equals(StreamUtils.STATUS_LIVE)) {
                showChatJoinBubbleAnimation();/*IF STREAM LIVE START SHOWING*/
                initPubNub(true);
            } else {
                initPubNub(false);
            }
        }
    }

    // METHOD TO CREATE LIVE STREAM
    private void goLive(StreamBO streamBO) {
        initView();
        initWowzaSDk();
        showHideViewOnPlayStream(false);
        showProgress();
        if (!Constants.BUILD_TYPE_QA) { // TODO FOR TESTING ONLY
            setWowzaConfig(streamBO);
        }
        captureWowzaScreen();
        initPubNub(true);
    }

    // METHOD TO INITIALIZE PUBNUB
    private void initPubNub(boolean showEmptyMessage) {
        initSocialAndChatChannel();
        sendFirstTestMsg(getString(R.string.be_courteous_in_your_decorum));
        addPubNubListener();
        subscribeChannel();
        getChatHistory();
        if (showEmptyMessage) {
            sendMsgForPresence(); //TO SEND EMPTY MESSAGES FOR LIVE USERS
        }
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {


        pager.setAdapter(new GoLiveSlideViewAdapter());
        pager.setCurrentItem(1);

        giftBulletView.setViewCount(2);
        giftBulletView.init();

        mVideoView.setOnClickListener(this);

        goLiveBottomOpt1.setOnClickListener(this);
        goLiveBottomOpt2.setOnClickListener(this);
        goLiveBottomOpt3.setOnClickListener(this);
        goLiveBottomOpt4.setOnClickListener(this);
        goLiveBottomOpt5.setOnClickListener(this);
        goLiveBottomOpt6.setOnClickListener(this);
        userViewImageView.setOnClickListener(this);
        userCancelImageView.setOnClickListener(this);
        screenShotPopupSave.setOnClickListener(this);
        screenShotPopupShare.setOnClickListener(this);
        screenShotCloseImageView.setOnClickListener(this);
        eventPosterIV.setOnClickListener(this);
        closeChatTab.setOnClickListener(this);

        goLiveViewerChatCamera.setOnClickListener(this);
        goLiveViewerChatFolder.setOnClickListener(this);
        goLiveViewerChatGift.setOnClickListener(this);
        goLiveViewerChatPost.setOnClickListener(this);
        goLiveViewerChatMessage.setOnClickListener(this);

        userImageParentFrame.setOnClickListener(this);
        dailyMissionEnergyImageView.setOnClickListener(this);
        userCashParentFrame.setOnClickListener(this);

        cameraMessagesParentFrame.setOnClickListener(this);

        goLiveChatEDT.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    sendTextMsg(true);
                }
                return false;
            }
        });

        goLiveChatViewerEDT.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    sendTextMsg(false);
                }
                return false;
            }
        });

        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        initEndBroadcastDialog();
        initPurchaseCoinsDialog();
        populateView();
    }

    // METHOD TO INITIALIZE WOWZA SDK
    private void initWowzaSDk() {
        goCoder = WowzaUtils.initWowzaGoCoderInstance(getApplicationContext());
    }

    // METHOD TO SHOW PROGRESS DAILOG
    private void showProgress() {

        progressDialog = new ProgressDialog(GoLiveActivity.this);
        progressDialog.setMessage("Streaming server initializing. Please wait.");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }

    // INITIALIZE END BROADCAST DIALOG
    private void initEndBroadcastDialog() {
        dialogEndBroadcast = new Dialog(this);
        dialogEndBroadcast.setContentView(R.layout.dialog_end_broadcast);
        dialogEndBroadcast.setCanceledOnTouchOutside(true);
        if (dialogEndBroadcast.getWindow() != null) {
            dialogEndBroadcast.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        Button btnEndBroadcastOk = (Button) dialogEndBroadcast.findViewById(R.id.btnEndBroadcastOk);
        Button btnEndBroadcastLater = (Button) dialogEndBroadcast.findViewById(R.id.btnEndBroadcastLater);

        btnEndBroadcastOk.setOnClickListener(this);
        btnEndBroadcastLater.setOnClickListener(this);
    }

    // INITIALIZE END BROADCAST DIALOG
    private void initPurchaseCoinsDialog() {
        dialogPurchaseCoins = new Dialog(this);
        dialogPurchaseCoins.setContentView(R.layout.dialog_purchase_coins);
        dialogPurchaseCoins.setCanceledOnTouchOutside(true);
        if (dialogPurchaseCoins.getWindow() != null) {
            dialogPurchaseCoins.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        RecyclerView purchaseList = (RecyclerView) dialogPurchaseCoins.findViewById(R.id.purchaseList);
        LinearLayout noResultParent = (LinearLayout) dialogPurchaseCoins.findViewById(R.id.noResultParent);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) dialogPurchaseCoins.findViewById(R.id.swipe_refresh);
        CustomTextView noResultRefreshTextView = (CustomTextView) dialogPurchaseCoins.findViewById(R.id.noResultRefreshTextView);
        setUpCoinsData(purchaseList, swipeRefreshLayout, noResultParent, noResultRefreshTextView);
//        setUpWalletRecycler(walletBOs, purchaseList);
    }

    private void setUpCoinsData(final RecyclerView purchaseList, final SwipeRefreshLayout swipeRefreshLayout, final LinearLayout noResultParent, CustomTextView noResultRefreshTextView) {

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);


        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    getPackagesListNetworkCall(GoLiveActivity.this, purchaseList, swipeRefreshLayout, noResultParent);
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPackagesListNetworkCall(GoLiveActivity.this, purchaseList, swipeRefreshLayout, noResultParent);
            }
        });

        noResultRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                getPackagesListNetworkCall(GoLiveActivity.this, purchaseList, swipeRefreshLayout, noResultParent);
            }
        });
    }

    private void setUpWalletRecycler(final List<PackagesBO> packagesBOList, final RecyclerView purchaseList) {

        if (packagesBOList != null && packagesBOList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            purchaseList.setItemAnimator(new DefaultItemAnimator());
            purchaseList.setLayoutManager(linearLayoutManager);
            myWalletAdapter = new MyWalletAdapter(packagesBOList, this, purchaseList);
            purchaseList.setAdapter(myWalletAdapter);
            myWalletAdapter.setClickListener(new MyWalletAdapter.ClickListeners() {
                @Override
                public void onRowClick(View v, int position) {
                    gotoNextFragment(WebviewFragment.newInstance(packagesBOList.get(position).getAmount(), packagesBOList.get(position).getId() + "", (packagesBOList.get(position).getWitky_chips() + packagesBOList.get(position).getFree_chips() + packagesBOList.get(position).getPromotion()) + ""));
                    /*if (packagesBOList.get(position).getAllow_promotion() == 1) {

                    } else {
                        gotoNextFragment(WebviewFragment.newInstance(packagesBOList.get(position).getAmount(), packagesBOList.get(position).getId() + "", packagesBOList.get(position).getWitky_chips() + ""));
                    }*/
                    if (dialogPurchaseCoins != null) {
                        dialogPurchaseCoins.dismiss();
                    }
                }
            });

        } else {
            //showNoResult(true);
        }

    }

    // SHOW ENG BROADCAST DIALOG
    private void showEndBroadcastDialog() {
        if (dialogEndBroadcast != null)
            dialogEndBroadcast.show();
    }

    // SHOW PURCHASE COINS DIALOG
    private void showPurchaseCoinsDialog() {
        if (dialogPurchaseCoins != null)
            dialogPurchaseCoins.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goLiveBottomOpt1:
                new GoLiveMenuOptionsDialog().
                        show(getSupportFragmentManager(), "Option 1");
                break;
            case R.id.goLiveBottomOpt2:
                showHideChatCreator(true);
                break;
            case R.id.goLiveBottomOpt3:
                switchWowzaCamera();
                break;
            case R.id.goLiveBottomOpt4:
                takeScreenShot();
                break;
            case R.id.goLiveBottomOpt5:
                new GoLiveInviteOptionsDialog().
                        show(getSupportFragmentManager(), "Option 5");
                break;
            case R.id.goLiveBottomOpt6:
                startActivity(new Intent(GoLiveActivity.this, ScanCodeActivity.class));
                break;
            case R.id.userViewImageView:
                new GoLiveViewersSummaryDialog().newInstance(new ArrayList<ConversationBO>(onlineConversationBOs)).show(getSupportFragmentManager(), "Option 6");
                break;
            case R.id.userCancelImageView:
                if (streamPlayStatus) {
//                    if (giftCount != null && giftCount.size() > 0) { /*IF WANT TO CHECK GIFTS EXIST*/
                    sendGiftCountNetworkCall(this, userBO.getId(), streamBO.getUser_details().getId(), streamBO.getId(), UserSharedPreference.readUserToken(), giftCount, true);
//                    }
                    finish();
                } else {
                    showEndBroadcastDialog();
                }
                break;
            case R.id.screenShotPopupSave:
                if (bitmap != null) {
                    ScreenShotUtils.SaveScreenShot(bitmap, this);
                    screenShotView.setVisibility(View.GONE);
                } else {
                    Utils.showToast(GoLiveActivity.this, getString(R.string.nothing_to_save));
                    screenShotView.setVisibility(View.GONE);
                }
                break;
            case R.id.screenShotPopupShare:
                if (bitmap != null) {
                    ScreenShotUtils.shareBitmapScreenShot(bitmap, this);
                    screenShotView.setVisibility(View.GONE);
                } else {
                    Utils.showToast(GoLiveActivity.this, getString(R.string.nothing_to_share));
                    screenShotView.setVisibility(View.GONE);
                }
                break;
            case R.id.screenShotCloseImageView:
                screenShotView.setVisibility(View.GONE);
                break;
            case R.id.userImageParentFrame:
                new GoLiveHomeProfileDialog().newInstance(onlineConversationBOs != null ? onlineConversationBOs.size() + "" : "0", streamBO, streamPlayStatus, userCashTextView.getText().toString()).show(getSupportFragmentManager(), "Option 8");
                break;
            case R.id.dailyMissionEnergyImageView:
                if (streamPlayStatus) {
                    gotoNextFragment(DailyMissionEnergyFragment.newInstance());
                } else {
                    startActivity(new Intent(GoLiveActivity.this, BroadcastRankActivity.class));
                }
                break;
            case R.id.btnEndBroadcastOk:
                if (streamBO != null && userBO != null) {
                    if (!Constants.BUILD_TYPE_QA) {
                        stopVideoStreaming();
                    }
                    if (dialogEndBroadcast != null) {
                        dialogEndBroadcast.dismiss();
                    }
                } else {
//                    startActivity(new Intent(GoLiveActivity.this, LiveFeedEndedActivity.class));
                    finish();
                }
                break;
            case R.id.btnEndBroadcastLater:
                if (dialogEndBroadcast != null)
                    dialogEndBroadcast.dismiss();
                break;
            case R.id.userCashParentFrame:
                new GoLiveDollarsDialog().show(getSupportFragmentManager(), "Option 9");
                break;
            case R.id.eventPosterIV:
                Intent intent = new Intent(GoLiveActivity.this, EventPosterDetailActivity.class);
                intent.putExtra(EventPosterDetailActivity.INDEX, 0);
                intent.putExtra(EventPosterDetailActivity.PROGRAMME_LIST, programmeBOs);
                startActivity(intent);
                break;
            case R.id.closeChatTab:
                showHideChatCreator(false);
                break;

            case R.id.cameraMessagesParentFrame:
                cameraMessagesParentFrame.setVisibility(View.GONE);
                break;

            case R.id.goLiveViewerChatCamera:

                takeScreenshotOfStream();

                /*if (mVideoView.isPlaying()) {
                    // todo
                    //Bitmap currentFrame = mediaMetadataRetriever.getFrameAtTime(mVideoView.getCurrentPosition() * 1000);

                    takeScreenshotOfStream();
                }*/
                break;

            case R.id.goLiveViewerChatFolder:
                new GoLiveBroadcastGiftReceivedDialog().show(getSupportFragmentManager(), "Gifts");
                break;
            case R.id.goLiveViewerChatGift:
                loopChatJoinAnimationStatus = false;
                new GoLiveBroadcastGiftPanelDialog().newInstance(giftJsonString, /*USER LEVEL HERE*/1).show(getSupportFragmentManager(), "Gifts");
                showHideForGiftDialog(false);
                break;

            case R.id.goLiveViewerChatPost:
                new ShareStreamDialog().newInstance(streamVideoURL, userBO.getUsername(), userBO.getId(), streamBO.getId(), streamBO.getUuid(), true).show(getSupportFragmentManager(), "ShareStreamDialog");
                break;

            case R.id.goLiveViewerChatMessage:
                gotoNextFragment(PrivateMessageFragment.newInstance(streamBO.getUser_details().getId(), true, "", streamBO.getUser_details().getUsername()));
//                showShowerAnimation();
                break;

            case R.id.video_view:
                Utils.showToast(GoLiveActivity.this, getString(R.string.will_be_implemented_later));
                break;
        }
    }

    @Override // METHOD FOR WOWZA SERVER CALLBACK STATUS
    public void onWZStatus(final WZStatus goCoderStatus) {
        // A successful status transition has been reported by the GoCoder SDK
        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");
        if (goCoderStatus != null) {
            switch (goCoderStatus.getState()) {
                case WZState.STARTING:
                    statusMessage.append("Broadcast initialization");
                    break;

                case WZState.READY:
                    statusMessage.append("Ready to begin streaming");
                    break;

                case WZState.RUNNING:
                    statusMessage.append("Streaming is active");
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;

                case WZState.STOPPING:
                    statusMessage.append("Broadcast shutting down");
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    if (!Constants.BUILD_TYPE_QA) {
                        endCurrentStreamNetworkCall(GoLiveActivity.this,
                                streamBO.getId(), userBO.getToken(), (System.currentTimeMillis() - startTime) + "");

                    }
                    break;

                case WZState.IDLE:
                    statusMessage.append("The broadcast is stopped");
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    break;

                default:
                    return;
            }
        }
        // Display the status message using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Utils.showToast(GoLiveActivity.this, statusMessage.toString());
            }
        });
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        // If an error is reported by the GoCoder SDK, display a message
        // containing the error details using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Utils.showToast(GoLiveActivity.this,
                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription());
            }
        });
    }

    void setWowzaConfig(StreamBO streamBO) {
        goCoderAudioDevice = new WZAudioDevice();
        goCoderBroadcaster = new WZBroadcast();
        // Create a configuration instance for the broadcaster
        goCoderBroadcastConfig = WowzaUtils.getWowzaBroadcastConfigurations(streamBO);
        if (mCameraView != null) {
            WZCamera availableCameras[] = mCameraView.getCameras();
            // Ensure we can access to at least one camera
            if (availableCameras.length > 0) {
                // Set the video broadcaster in the broadcast config
                goCoderBroadcastConfig.setVideoBroadcaster(mCameraView);
            } else {
                goCoderBroadcastConfig.setVideoEnabled(false);
            }
        } else {
            goCoderBroadcastConfig.setVideoEnabled(false);
        }
        // Initialize the audio input device interface
        goCoderAudioDevice = new WZAudioDevice();
        //Set the audio broadcaster in the broadcast config
        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);

        mCameraView.setCameraConfig(goCoderBroadcastConfig);
//        mCameraView.setScaleMode(WZMediaConfig.RESIZE_TO_ASPECT);
        mCameraView.setVideoBackgroundColor(WZColor.LIGHTGREY);

        if (goCoderBroadcastConfig.isVideoEnabled()) {
            if (mCameraView.isPreviewPaused())
                mCameraView.onResume();
            else
                mCameraView.startPreview();
        }

        startVideoStreaming();
    }

    //Start live video stream to WOwza server
    private void startVideoStreaming() {

        MarshMallowPermission permission = new MarshMallowPermission(GoLiveActivity.this);

        if (permission.checkPermissionForMicrophone()) {

            WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

            if (configValidationError != null) {
                Toast.makeText(getApplicationContext(), configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
            } else if (goCoderBroadcaster.getStatus().isRunning()) {
                // Stop the broadcast that is currently running
                goCoderBroadcaster.endBroadcast();
            } else {
                // Start streaming
                goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
            }

        } else {
            permission.requestPermissionForMicrophone(RECORD_AUDIO_REQUEST_CODE, GoLiveActivity.this);
        }
    }

    @Override
    protected void onPause() {
        if (mCameraView != null) {
            mCameraView.stopPreview();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (goCoder != null && mCameraView != null) {
            if (mNinjaFace == null) {
                // Read in the bitmap for the face detection
                Bitmap faceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ninja_emoji);
                mNinjaFace = new WZBitmap(faceBitmap);
                mNinjaFace.setVisible(false);
                mCameraView.registerFrameRenderer(mNinjaFace);
            }
        }*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            if (mCameraView.isPreviewPaused())
                mCameraView.onResume();
            else
                mCameraView.startPreview();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mCameraView.isPreviewPaused())
                        mCameraView.onResume();
                    else
                        mCameraView.startPreview();

                } else {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case RECORD_AUDIO_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVideoStreaming();
                } else {
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(this, getString(R.string.record_audio_permission_not_granted),
                            Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takeScreenShot();

                } else {
                    Toast.makeText(this, R.string.storage_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static GoLiveStartPageFragment.ConfirmationDialogFragment newInstance(@StringRes int message,
                                                                                     String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            GoLiveStartPageFragment.ConfirmationDialogFragment fragment = new GoLiveStartPageFragment.ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();
        }
    }

    // METHOD TO POPULATE VIEW DATA
    private void populateView() {
        if (userBO != null) {
            if (userBO.getUsername().length() > 10) {
                userNameTextView.setText(userBO.getUsername().substring(0, 9) + "...");
            } else {
                userNameTextView.setText(userBO.getUsername());
            }
        }
        userImageIV.getLayoutParams().width = userImageIV.getLayoutParams().height;
        Utils.setImageRounded(userImageIV, userBO.getProfilePictureUrl(), this);
        try {
            if (streamBO != null) {
                if (streamBO.getUser_details() != null) {
                    userCashTextView.setText((streamBO.getUser_details().getWitkeyDollar().intValue()) + "");
                    userPointsTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getArtist_total_tornados() + "");
                    userDiamondTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "");

//                double coins = streamBO.getUser_details().getWitkeyDollar();
//                userCashTextView.setText((int) coins + "");
                    Utils.setImageRounded(userImageIV, streamBO.getUser_details().getProfilePictureUrl(), this);
                    if (streamBO.getUser_details().getUsername().length() > 10) {
                        userNameTextView.setText(streamBO.getUser_details().getUsername().substring(0, 9) + "...");
                    } else {
                        userNameTextView.setText(streamBO.getUser_details().getUsername());
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("userCashTextView", "" + e.getMessage());
        }

    }

    // METHOD TO POPULATE DATA INTO THE LIST
    private void setUpFeaturedRecycler(final List<ConversationBO> conversationBOs) {

        if (conversationBOs != null && conversationBOs.size() > 0) {

            //if (goLiveTopAdapter == null) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            goLiveTopAdapter = new GoLiveTopAdapter(conversationBOs, this, mRecyclerView);
            mRecyclerView.setAdapter(goLiveTopAdapter);

            goLiveTopAdapter.setClickListener(new GoLiveTopAdapter.ClickListeners() {
                @Override
                public void onRowClick(int position) {
                    new GoLiveViewersProfilePageDialog().newInstance(conversationBOs.get(position)).show(getSupportFragmentManager(), "Option 9");
                }
            });
            /*} else {
                goLiveTopAdapter.addItems(conversationBOs);
            }*/
        }
    }

    // METHOD TO SE FLAG TRUE FOR WOWZA FRAME LISTENER
    private void takeScreenShot() {

        MarshMallowPermission permission = new MarshMallowPermission(this);
        if (permission.checkPermissionForExternalStorage()) {

            // Setting mGrabFrame to true will trigger the video frame listener to become active
            if (!mGrabFrame.get() && !mSavingFrame.get()) {
                goLiveBottomOpt4.setEnabled(false);
                goLiveBottomOpt4.setClickable(false);
                mGrabFrame.set(true);
            }

//            ScreenShotUtils.takeScreenShot(goLiveParentView, this);
        } else {
            permission.requestPermissionForExternalStorage(REQUEST_STORAGE_PERMISSION, this);
        }
    }

    // METHOD TO START WOWZA VIDEO FRAME LISTENER
    private void captureWowzaScreen() {
        // Create an register a video frame listener with WZCameraPreview
        WZRenderAPI.VideoFrameListener videoFrameListener = new WZRenderAPI.VideoFrameListener() {

            @Override
            // onWZVideoFrameListenerFrameAvailable() will only be called nce isWZVideoFrameListenerActive() returns true
            public boolean isWZVideoFrameListenerActive() {
                // Only indicate the frame listener once the screenshot button has been pressed
                // and when we're not in the process of saving a previous screenshot
                return (mGrabFrame.get() && !mSavingFrame.get());
            }

            @Override
            public void onWZVideoFrameListenerInit(WZGLES.EglEnv eglEnv) {
                // nothing needed for this example
            }

            @Override
            // onWZVideoFrameListenerFrameAvailable() is called when isWZVideoFrameListenerActive() = true
            // and a new frame has been rendered on the camera preview display surface
            public void onWZVideoFrameListenerFrameAvailable(WZGLES.EglEnv eglEnv, WZSize frameSize, int frameRotation, long timecodeNanos) {
                // set these flags so that this doesn't get called numerous times in parallel
                mSavingFrame.set(true);
                mGrabFrame.set(false);

                // create a pixel buffer and read the pixels from the camera preview display surface using GLES
                final WZSize bitmapSize = new WZSize(frameSize);
                final ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(bitmapSize.getWidth() * bitmapSize.getHeight() * 4);
                pixelBuffer.order(ByteOrder.LITTLE_ENDIAN);
                GLES20.glReadPixels(0, 0, bitmapSize.getWidth(), bitmapSize.getHeight(),
                        GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
                final int eglError = WZGLES.checkForEglError(TAG + "(glReadPixels)");
                if (eglError != EGL14.EGL_SUCCESS) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goLiveBottomOpt4.setEnabled(true);
                            goLiveBottomOpt4.setClickable(true);
                            Toast.makeText(getApplicationContext(), WZGLES.getEglErrorString(eglError), Toast.LENGTH_LONG).show();
                        }
                    });

                    mSavingFrame.set(false);
                    return;
                }
                pixelBuffer.rewind();

                // now that we have the pixels, create a new thread for transforming and saving the bitmap
                // so that we don't block the camera preview display renderer

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showBitmap(bitmapSize, pixelBuffer);
                    }
                });
            }

            @Override
            public void onWZVideoFrameListenerRelease(WZGLES.EglEnv eglEnv) {
                // nothing needed for this example
            }
        };

        // register our newly created video frame listener wth the camera preview display view
        mCameraView.registerFrameListener(videoFrameListener);

        if (true) {
            // start the camera preview display and enable the screenshot button when it is ready
            mCameraView.startPreview(new WZCameraView.PreviewStatusListener() {
                @Override
                public void onWZCameraPreviewStarted(WZCamera camera, WZSize frameSize, int frameRate) {
                    goLiveBottomOpt4.setEnabled(true);
                    goLiveBottomOpt4.setClickable(true);

                    /*if (cameraSupportsFaceDetection(camera))
                        mDetectingFaces = setFaceDetectionState(camera, true);
                    else
                        Toast.makeText(GoLiveActivity.this, "The selected camera does not support facial recognition", Toast.LENGTH_LONG).show();*/


                }

                @Override
                public void onWZCameraPreviewStopped(int cameraId) {
                    goLiveBottomOpt4.setEnabled(false);
                    goLiveBottomOpt4.setClickable(false);
                }

                @Override
                public void onWZCameraPreviewError(WZCamera camera, WZError error) {
                    goLiveBottomOpt4.setEnabled(false);
                    goLiveBottomOpt4.setClickable(false);
                }
            });
        }
    }

    // METHOD TO SHOW WOWZA BITMAP ON CUSTOM CARD VIEW
    private void showBitmap(WZSize bitmapSize, ByteBuffer pixelBuffer) {

        try {

            bitmap = Bitmap.createBitmap(bitmapSize.getWidth(), bitmapSize.getHeight(), Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(pixelBuffer);
            Matrix xformMatrix = new Matrix();
            xformMatrix.setScale(-1, 1);  // flip horiz
            xformMatrix.preRotate(180);  // flip vert
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapSize.getWidth(), bitmapSize.getHeight(), xformMatrix, false);
            screenShotView.setVisibility(View.VISIBLE);
            screenShotImageView.setImageBitmap(Utils.getRoundedCornerOptionsBitmap(bitmap, 50, false, false, true, true));

        } catch (Exception e) {
            LogUtils.i(TAG, e.getMessage());

        } finally {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goLiveBottomOpt4.setEnabled(true);
                    goLiveBottomOpt4.setClickable(true);
                }
            });
        }

        mSavingFrame.set(false);
    }

    private void takeScreenshotOfStream() {

        try {
            bitmap = ScreenShotUtils.getScreenShot(goLiveParentView);
            screenShotView.setVisibility(View.VISIBLE);
            screenShotImageView.setImageBitmap(Utils.getRoundedCornerOptionsBitmap(bitmap, 50, false, false, true, true));

        } catch (Exception e) {
            LogUtils.i(TAG, e.getMessage());

        } finally {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goLiveBottomOpt4.setEnabled(true);
                    goLiveBottomOpt4.setClickable(true);
                }
            });
        }

        //mSavingFrame.set(false);
    }

    // METHOD TO SWITCH WOWZA CAMERA VIEW
    private void switchWowzaCamera() {
        WZCamera newCamera = mCameraView.switchCamera();
        if (newCamera != null) {

            if (newCamera.isFront()) {
                cameraMessages.setText(getString(R.string.what_viewers_see_is_the_opposite_of_what_you_see_now));
                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
            } else if (newCamera.isBack()) {
                cameraMessages.setText(getString(R.string.what_viewers_see_is_the_same_as_what_you_see_now));
                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
            }

            if (newCamera.hasCapability(WZCamera.FOCUS_MODE_CONTINUOUS))
                newCamera.setFocusMode(WZCamera.FOCUS_MODE_CONTINUOUS);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // REGISTER EVENT BUS TO LISTEN EVENT.
        GlobalBus.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // UNREGISTER EVENT BUS
        GlobalBus.getBus().unregister(this);
    }


    @Subscribe // METHOD TO GET EVENT MESSAGE FOR SELECTION
    public void getMessage(Events.FragmentToGoLiveUpActivityMessage fragmentToGoLiveUpActivityMessage) {

        switch (fragmentToGoLiveUpActivityMessage.getMessage()) {
            case "Switch":
                switchWowzaCamera();
                break;
            case "BeautyMood":
                if (beautyModeOn)
                    cameraMessages.setText(R.string.beauty_mode_turned_off);
                else
                    cameraMessages.setText(R.string.beauty_mode_turned_on);

                beautyModeOn = !beautyModeOn;

                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
                break;
            case "ShareDialog":
                new GoLiveShareDialog().newInstance(userBO.getUsername(), streamBO.getUuid(), userBO.getId(), streamBO.getId()).show(getSupportFragmentManager(), "Option 9");
                break;
            case "FluentQuality":
                if (hdQualityOn)
                    cameraMessages.setText(getString(R.string.switched_to_fluent_quality));
                else
                    cameraMessages.setText(getString(R.string.switched_to_hd_quality));

                hdQualityOn = !hdQualityOn;

                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
                break;
            case "ShowStickers":
                gotoNextFragment(SetOfStickersFragment.newInstance());
                break;
            case "Gifts_ID": /*SEND ADD GIFTS CALL ON EVERY SEND PRESS HERE*/
                loopChatJoinAnimationStatus = true;
                showChatJoinBubbleAnimation();
                if (streamPlayStatus) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showHideForGiftDialog(true);
                        }
                    });
                }
                sendSocialMsg(fragmentToGoLiveUpActivityMessage.getId(), fragmentToGoLiveUpActivityMessage.getGiftName(), fragmentToGoLiveUpActivityMessage.getGiftPrice(), fragmentToGoLiveUpActivityMessage.getGiftCount(), fragmentToGoLiveUpActivityMessage.getGiftURL(), fragmentToGoLiveUpActivityMessage.getGiftLevel());
                break;
            case "GT_Pay":
                if (streamPlayStatus) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showHideForGiftDialog(true);
                        }
                    });
                }
                gotoNextFragment(WebviewFragment.newInstance(0, "0", "0"));
                break;
            case "SHOW_PURCHASE_COINS":
                if (streamPlayStatus) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showHideForGiftDialog(true);
                        }
                    });
                }
                showPurchaseCoinsDialog();
                break;
            case "SHOW_VIEWER_CHAT_VIEW":
                loopChatJoinAnimationStatus = true;
                showChatJoinBubbleAnimation();
                if (streamPlayStatus) {
                    showHideForGiftDialog(true);
                }
                break;
        }
    }

    // METHOD TO PLAY STREAM VIDEO

    private void playStreamVideo(StreamBO streamBO) {

        //progressBar.setVisibility(View.VISIBLE);
        showHideUserBlurImage(true);
        streamVideoURL = StreamUtils.getVideoUrl(streamBO);
        //used in streamix...
        Uri video = Uri.parse(streamVideoURL);

        // todo , issue in m3u8 format
        //mediaMetadataRetriever.setDataSource(StreamUtils.getVideoUrl(streamBO), new HashMap<String, String>());

//        Uri video = Uri.parse(streamBO.getStream_playback_url());
        mVideoView.setVideoURI(video);

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(false);
                //progressBar.setVisibility(View.GONE);
                showHideUserBlurImage(false);
                mVideoView.start();
            }
        });

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //progressBar.setVisibility(View.GONE);
                showHideUserBlurImage(false);

                return false;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer vmp) {


                AlertOP.showAlert(GoLiveActivity.this, "",
                        getString(R.string.video_is_ended),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // finish();
                            }
                        });
                if (mVideoView != null) {
                    mVideoView.stopPlayback();
                }
            }
        });
    }

    // METHOD TO SHOW HIDE VIEW ACCORDING TO STREAM TYPE
    private void showHideViewOnPlayStream(boolean streamPlayStatus) {

        if (streamPlayStatus) {
            eventPosterIV.setImageResource(R.drawable.sporgram_nav);//leaderboard_new);
            ((LinearLayout.LayoutParams) eventPosterIV.getLayoutParams()).setMargins(20, 0, 0, 0);
            dailyMissionEnergyImageView.setImageResource(R.drawable.daily_mission);//witkey_new);
            pointsTypeIV.setImageResource(R.drawable.tornado_new);//diamond);
            oldChatView.setVisibility(View.VISIBLE);
            goLiveChatViewer.setVisibility(View.VISIBLE);
            goLiveChatCreator.setVisibility(View.GONE);
            userViewParent.setVisibility(View.VISIBLE);
            userViewImageView.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.VISIBLE);
            showHideUserBlurImage(true);
            mVideoView.setVisibility(View.VISIBLE);
            goLiveListParentLL.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.GONE);
            mCameraView.setVisibility(View.GONE);

        } else {
            eventPosterIV.setImageResource(R.drawable.sporgram_nav);
            dailyMissionEnergyImageView.setImageResource(R.drawable.daily_mission);
            pointsTypeIV.setImageResource(R.drawable.tornado_new);
            oldChatView.setVisibility(View.GONE);
            goLiveChatViewer.setVisibility(View.GONE);
            goLiveChatCreator.setVisibility(View.GONE);
            mCameraView.setVisibility(View.VISIBLE);
            userViewParent.setVisibility(View.VISIBLE);
            userViewImageView.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.VISIBLE);
            goLiveListParentLL.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.GONE);
            showHideUserBlurImage(false);
            mVideoView.setVisibility(View.GONE);
        }
    }

    // METHOD TO SHOW HIDE VIEW ACCORDING TO CHAT
    private void showHideChatCreator(boolean showChat) {

        if (showChat) {
            oldChatView.setVisibility(View.VISIBLE);
            goLiveChatCreator.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.GONE);

        } else {
            oldChatView.setVisibility(View.GONE);
            goLiveChatEDT.getText().clear();
            goLiveChatCreator.setVisibility(View.GONE);
            goLiveTopView.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.VISIBLE);
            goLiveCenterView.setVisibility(View.VISIBLE);
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
                if (message.getChannel().equals(chatChannel)) {
                    try {
                        //on receive new message...
                        JsonElement jsonElement = message.getMessage();
                        final ConversationBO conversationBO = gson.fromJson(jsonElement, ConversationBO.class);
                        if (conversationBO != null) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (conversationBO.getSenderId() != null && !conversationBO.getSenderId().equals(userBO.getId())) {

                                        // WHEN MESSAGE TYPE IS CHAT MESSAGE
                                        if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.CHAT_MESSAGE) {
                                            if (!conversationBO.getText().isEmpty()) {
                                                addMessage(conversationBO);
                                            }

                                            // WHEN MESSAGE TYPE IS USER LIVE STATUS MESSAGE
                                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.LIVE_STATUS) {
                                            if (!isAlreadyExist(conversationBO, onlineConversationBOs)) {
                                                onlineConversationBOs.add(conversationBO);
                                                setUpFeaturedRecycler(new ArrayList<ConversationBO>(onlineConversationBOs));
                                                addMessage(conversationBO); // FOR CHAT JOIN MESSAGE
//                                                showChatJoinBubbleAnimation(); // PLAY ALWAYS
//                                                checkForFullAnimation(500, "", 0, "");
                                                Animations.slideInRightViewAnimation(GoLiveActivity.this, giftSlideInView);
                                            }
                                            // WHEN MESSAGE TYPE IS GIFT SENT MESSAGE
                                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
                                            addMessage(conversationBO);
                                            addBubble(conversationBO.getSocialGiftID());
//                                            checkForFullAnimation(conversationBO.getSocialGiftID());
                                        }
                                    }
                                    if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
//                                        addAnimationGiftBullet(conversationBO.getUsername(), EnumUtils.GiftResById.getGiftRes(conversationBO.getSocialGiftID()), conversationBO.getText()); // ALREADY USED IN METHOD BELOW
//                                        checkForFullAnimation(conversationBO.getSocialGiftID(), conversationBO.getText(), conversationBO.getGiftPrice(), conversationBO.getUsername());
                                        startFullAnimation(conversationBO.getSocialGiftID(), conversationBO.getText(), conversationBO.getGiftPrice(), conversationBO.getUsername(), conversationBO.getGiftURL());
                                    }
                                }
                            });

                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
//                    presence.get
            }
        };
        PubnubUtils.getPubNubInstance().addListener(subscribeCallback);
    }

    private void addBubble(int giftID) {

        if (giftID < 0)
            return;

        bubbleViewGoLive.setBubbleDrawable(getResources().getDrawable(EnumUtils.GiftResById.getGiftRes(giftID)),
                Utils.dp2px(GoLiveActivity.this, 30), Utils.dp2px(GoLiveActivity.this, 30));

        bubbleViewGoLive.addBubble((int) goLiveBottomTabParent.getX() + (goLiveBottomTabParent.getWidth()) - Utils.dp2px(GoLiveActivity.this, 30), goLiveBottomTabParent.getTop() - Utils.dp2px(GoLiveActivity.this, 0), new Random().nextInt(9 - 5) + 5);
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

    // METHOD TO POPULATE DATA ON CHAT LIST
    private void setUpConversationAdapter(final ArrayList<ConversationBO> list) {

        if (conversationAdapter != null) {
            conversationAdapter.addItems(list);
        } else {
            conversationAdapter = new StreamingConversationAdapter(list, this);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
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
            conversationAdapter.setClickListener(new StreamingConversationAdapter.ClickListeners() {
                @Override
                public void onRowClick(int position) {
                    ConversationBO conversationBO = conversationAdapter.getItem(position);
                    if (!conversationBO.getSenderId().equals(userBO.getId())) {
                        //if not the login user...
                        Utils.showToast(GoLiveActivity.this, getString(R.string.will_be_implemented_later));
                    }

                }

                @Override
                public void onLikeClick(int position) {
/*
                    int newLikes = likes + 1;
                    SocialDataBO socialDataBO = new SocialDataBO(newLikes, dislikes);

                    sendSocialData(socialDataBO, true);*/
                }

                @Override
                public void onDislikeClick(int position) {
                 /*   int newDisLikes = dislikes + 1;
                    SocialDataBO socialDataBO = new SocialDataBO(likes, newDisLikes);
                    sendSocialData(socialDataBO, false);*/
                }

                @Override
                public boolean onRowLongPressClick(int position) {
                    return false;
                }
            });
        }
    }

    // METHOD TO CREATE PUBNUB CHANNEL
    private void subscribeChannel() {
        PubnubUtils.getPubNubInstance().subscribe()
                .channels(Arrays.asList(chatChannel))// subscribe to channels
//                .channels(Arrays.asList(chatChannel, socialChannel))// subscribe to channels
                .execute();
    }

    // METHOD TO GET ALL CHAT MESSAGES HISTORY
    private void getChatHistory() {
        PubnubUtils.getPubNubInstance().history()
                .channel(chatChannel) // where to fetch history from chat channel
                .count(100) // how many items to fetch
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if (status.isError()) {
                            Toast.makeText(GoLiveActivity.this, "Unable to fetch history", Toast.LENGTH_SHORT).show();
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
                            conversationBO.setmRowType(ConversationBO.OTHER);
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
                                    setUpFeaturedRecycler(new ArrayList<ConversationBO>(onlineConversationBOs));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (conversationBOs != null && conversationBOs.size() > 0)
                    setUpConversationAdapter(conversationBOs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // METHOD TO CREATE MESSAGE OBJECT FOR PUBNUB SERVER
    private void sendMsgForPresence() {
        String text = GoLiveActivity.this.getString(Utils.getChatJoinedIndex());
        ConversationBO conversationBO = new ConversationBO(userBO.getProfilePictureUrl(),
                Utils.getCurrentTimeStamp_Streams(), userBO.getFullName(), userBO.getId(), text, userBO.getUsername(), EnumUtils.ConversationType.LIVE_STATUS, 0, 0, userBO, 1);
        sendMessage(conversationBO);
    }

    // METHOD TO CREATE SOCIAL MESSAGE OBJECT FOR PUBNUB SERVER
    private void sendSocialMsg(int giftID, String giftName, int giftPrice, int giftSendCount, String gifURL, int giftLevel) {
        String text = giftName;
        ConversationBO conversationBO = new ConversationBO(userBO.getProfilePictureUrl(),
                Utils.getCurrentTimeStamp_Streams(), userBO.getFullName(), userBO.getId(), text, userBO.getUsername(), EnumUtils.ConversationType.GIFT_SENT, giftID, giftPrice, giftSendCount, gifURL, giftLevel);
        sendMessage(conversationBO);
        manageGiftCount(giftID, giftPrice);

         /*TO SHOW GIFT COUNT TIMER AND SEND GIFT*/// TODO FOR NOW
        /*if (giftSendCount > 1) {
            showGiftTimer(conversationBO, giftSendCount);
        }*/

        /*CALL TO ADD GIFT*/
        sendGiftCountNetworkCall(this, userBO.getId(), streamBO.getUser_details().getId(), streamBO.getId(), UserSharedPreference.readUserToken(), giftCount, false);
    }

    // METHOD TO CREATE FIRST MESSAGE OBJECT FOR PUBNUB SERVER
    private void sendFirstTestMsg(String text) {
        ConversationBO conversationBO = new ConversationBO(userBO.getProfilePictureUrl(),
                Utils.getCurrentTimeStamp_Streams(), userBO.getFullName(), userBO.getId(), text, userBO.getUsername(), EnumUtils.ConversationType.FIRST_MESSAGE, 0, 0, 1, "TEMP URL", 0);
        addMessage(conversationBO);
    }

    // METHOD TO SEND TEXT MESSAGE TO PUBNUB SERVER
    private void sendMessage(final ConversationBO conversationBO) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String message = gson.toJson(conversationBO);
        LogUtils.i(TAG, "sendMessage: " + message);
        JsonElement element = gson.fromJson(message, JsonElement.class);
        PubnubUtils.getPubNubInstance().publish()
                .message(element)
                .channel(chatChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {

                        if (status.isError()) {
                            Toast.makeText(GoLiveActivity.this, "Error on sending message", Toast.LENGTH_SHORT).show();
                        } else {
                            // WHEN MESSAGE TYPE IS GIFT MESSAGE
                            if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
                                if (conversationBO.getSenderId() != null && !conversationBO.getSenderId().equals(userBO.getId())) {
                                    addBubble(conversationBO.getSocialGiftID());
                                }
                                addMessage(conversationBO);

                                // WHEN MESSAGE TYPE IS CHAT MESSAGE
                            } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.CHAT_MESSAGE) {
                                if (!conversationBO.getText().isEmpty()) {
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
    private void sendTextMsg(boolean typeStreamCreator) {

        String text;
        if (typeStreamCreator) {
            text = goLiveChatEDT.getText().toString().trim();
        } else {
            text = goLiveChatViewerEDT.getText().toString().trim();
        }

        if (!TextUtils.isEmpty(text)) {

            String date = DateTimeOp.getCurrentDateTime(Constants.dateFormat3) +
                    " at " + DateTimeOp.getCurrentDateTime(Constants.dateFormat29);

            ConversationBO conversationBO = new ConversationBO(userBO.getProfilePictureUrl(),
                    Utils.getCurrentTimeStamp_Streams(), userBO.getFullName(), userBO.getId(), Utils.checkForProfanity(this, text), userBO.getUsername(), EnumUtils.ConversationType.CHAT_MESSAGE, 0, 0, 1, "TEMP URL", 0);

            goLiveChatEDT.setText("");
            goLiveChatViewerEDT.setText("");
            sendMessage(conversationBO);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearPubNub();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (handlerJoinChat != null) {
            handlerJoinChat.removeCallbacksAndMessages(null);
        }
    }

    // METHOD TO DESTROY PUBNUB OBJECT
    private void clearPubNub() {
        PubnubUtils.getPubNubInstance().unsubscribeAll();
        PubnubUtils.getPubNubInstance().removeListener(subscribeCallback);
//        PubnubUtils.getPubNubInstance().destroy();
    }

    // METHOD TO END VIDEO STREAM
    private void stopVideoStreaming() {
        WZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

        if (configValidationError != null) {
            Toast.makeText(GoLiveActivity.this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
        } else if (goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast();
        }
    }

    @Override
    public void onBackPressed() {

        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

        if (fragmentList != null) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof SetOfStickersFragment) {
                    super.onBackPressed();
                    return;
                }
            }
        }

        if (streamPlayStatus) {
//            if (giftCount != null && giftCount.size() > 0) {
            sendGiftCountNetworkCall(this, userBO.getId(), streamBO.getUser_details().getId(), streamBO.getId(), UserSharedPreference.readUserToken(), giftCount, true);
//            }
            super.onBackPressed();
        } else if (fromEndSummary) {
            finish();
        } else {
            showEndBroadcastDialog();
        }
    }

    // METHOD TO INITIATE SOCIAL AND CHAT CHANNEL FOR PUBNUB
    private void initSocialAndChatChannel() {
        // create channel name used for chat data
        if (Constants.BUILD_TYPE_QA) { // TODO FOR TESTING ONLY
            chatChannel = "chat-123-QA";
        } else {
            if (tutorials) {
                chatChannel = "chat-123 Tutorials";
            } else {
                chatChannel = "chat-123" + streamBO.getUuid();
            }
        }
    }

    // METHOD FOR LIVE VIEWERS COUNT NETWORK CALL
    private void liveViewersCountNetworkCall(final Context context, int streamActionType, String userID, String streamID, String userToken) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.STREAM_ID, streamID);
        serviceParams.put(Keys.TYPE, streamActionType);

        tokenServiceHeaderParams.put(Keys.TOKEN, userToken);

        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.streamsactions,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
//                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray gift_detail = jsonObject.getJSONArray("gift_detail");
                                giftJsonString = gift_detail.toString();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO SEND BUBBLE ANIMATION WHEN ANY NEW USER JOINS CHAT
    private void showChatJoinBubbleAnimation() {

        try {
            handlerJoinChat = new Handler();
            handlerJoinChat.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (loopChatJoinAnimationStatus) {
                        addBubble(new Random().nextInt(1022 - 1000) + 1000);
                        handlerJoinChat.postDelayed(this, 200);
                    }

                }
            }, 1000);
        } catch (Exception e) {
            LogUtils.e("showChatJoinBubbleAnimation", "" + e.getMessage());
        }

        /*new CountDownTimer(4000, 200) {

            public void onTick(long millisUntilFinished) {
                if (!loopChatJoinAnimation && loopChatJoinAnimationStatus)
                    addBubble(new Random().nextInt(1022 - 1000) + 1000);
            }

            public void onFinish() {
                if (loopChatJoinAnimation) {
                    showChatJoinBubbleAnimation();
                }
            }
        }.start();*/
    }

    // METHOD TO SHOW TIMER FOR GIFT ANIMATION
    private void showGiftTimer(final ConversationBO conversationBO, final int giftSendCount) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                custom_progress.setVisibility(View.VISIBLE);
                progressCount = 0;
                custom_progress.setProgress(0);
                custom_progress.setMax(giftSendCount);
            }
        });

        try {
            handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (progressCount == -1) {
                        custom_progress.setVisibility(View.GONE);
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        progressCount++;
                        custom_progress.setProgress(progressCount);
                        if (progressCount < giftSendCount) {
                            sendMessage(conversationBO);
                            handler.postDelayed(this, 1000);
                        } else {
                            progressCount = -1;
                            handler.postDelayed(this, 200);
                        }
                    }
                }
            }, 1000);
        } catch (Exception e) {
            custom_progress.setProgress(0);
            custom_progress.setVisibility(View.GONE);
        }
    }

    private void showTutoirial() {
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(150); // half second between each showcase view
        config.setMaskColor(getResources().getColor(R.color.grey_transparent));

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(GoLiveActivity.this);
        sequence.setConfig(config);

        String[] goLiveAppTutorialArray = getResources().getStringArray(R.array.go_live_app_tutorial);
        int textIndex = 0;

        MaterialShowcaseView userProfile = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.UserProfile)
                .setTarget(userProfileView)
                .setRadius(userProfileView.getHeight())
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                        showHideWhiteBackground(true);
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userProfile);
        MaterialShowcaseView userCoinsCount = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.UserCoinsCount)
                .setTarget(userCoinsCountView)
                .setRadius(userCoinsCountView.getHeight())
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userCoinsCount);
        MaterialShowcaseView followBroadcaster = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.FollowBroadcaster)
                .setTarget(userProfileView)
                .setRadius(userProfileView.getHeight())
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                        showHideWhiteBackground(true);
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(followBroadcaster);
        MaterialShowcaseView userViewers = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveList)
                .setTarget(goLiveListParentLL)
                .setRadius(goLiveListParentLL.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userViewers);
        MaterialShowcaseView userEvents = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.EventPoster)
                .setTarget(eventPosterIV)
                .setRadius(eventPosterIV.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userEvents);
        MaterialShowcaseView userMission = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.DailyMissionEnergy)
                .setTarget(dailyMissionEnergyImageView)
                .setRadius(dailyMissionEnergyImageView.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userMission);
        MaterialShowcaseView userChatView = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.OldChat)
                .setTarget(oldChatView)
                .setRadius(oldChatView.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userChatView);
        MaterialShowcaseView userChatField = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveChatViewerEDT)
                .setTarget(goLiveChatViewerEDT)
                .setRadius(goLiveChatViewerEDT.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userChatField);
        MaterialShowcaseView userCamera = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatCamera)
                .setTarget(goLiveViewerChatCamera)
                .setRadius(goLiveViewerChatCamera.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userCamera);
        MaterialShowcaseView userChatPost = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatPost)
                .setTarget(goLiveViewerChatPost)
                .setRadius(goLiveViewerChatPost.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userChatPost);
        MaterialShowcaseView userChatMessage = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatMessage)
                .setTarget(goLiveViewerChatMessage)
                .setRadius(goLiveViewerChatMessage.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userChatMessage);
        MaterialShowcaseView userChatFolder = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatFolder)
                .setTarget(goLiveViewerChatFolder)
                .setRadius(goLiveViewerChatFolder.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                            showHideWhiteBackground(false); // show button three.
                        }
                    }
                })
                .build();
        sequence.addSequenceItem(userChatFolder);
        MaterialShowcaseView userChatGift = new MaterialShowcaseView.Builder(GoLiveActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatGift)
                .setTarget(goLiveViewerChatGift)
                .setRadius(goLiveViewerChatGift.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView
                            .DismissedType dismissedType) {
                        showHideWhiteBackground(false); // show button three.
                    }
                })
                .build();
        sequence.addSequenceItem(userChatGift);

        sequence.start();

    }

    private void startTutorials() {
        MaterialShowcaseView.resetSingleUse(GoLiveActivity.this, SHOWCASE_ID);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showTutoirial();
            }
        }, 1000);
    }

    private void showHideWhiteBackground(boolean showStatus) {

        if (showStatus) {
            page_two.setBackgroundColor(getResources().getColor(R.color.witkey_dim_white));
        } else {
            page_two.setBackgroundColor(0);
        }
    }

    private void showHideUserBlurImage(boolean showStatus) {

        if (streamBO != null) {
            if (showStatus) {
                userImageBlur.setVisibility(View.VISIBLE);
                if (tutorials) {
                    Utils.setImageSimpleBlur(userImageBlur, userBO.getProfilePictureUrl(), GoLiveActivity.this);
                } else {
                    Utils.setImageSimpleBlur(userImageBlur, streamBO.getUser_details().profilePictureUrl, GoLiveActivity.this);
                }
            } else {
                userImageBlur.setVisibility(View.GONE);
            }
        }
    }

    private void addAnimationGiftBullet() {
        GiftSendModel giftSendModel = new GiftSendModel(1);
        giftSendModel.setNickname("User 1");
        giftSendModel.setGiftRes(R.drawable.lips);
        giftSendModel.setSig("User 1 Text Here ");
        giftBulletView.addGift(giftSendModel);
    }

    private void addAnimationGiftBullet_() {
        GiftSendModel giftSendModel = new GiftSendModel(1);
        giftSendModel.setNickname("User 2");
        giftSendModel.setGiftRes(R.drawable.car);
        giftSendModel.setSig("User 2 Text Here ");
        giftBulletView.addGift(giftSendModel);
    }

    private void addAnimationGiftBullet(String giftName, int giftRes, String giftText) {
        GiftSendModel giftSendModel = new GiftSendModel(1);
        giftSendModel.setNickname(giftName);
        giftSendModel.setGiftRes(giftRes);
        giftSendModel.setSig(giftText);
        giftBulletView.addGift(giftSendModel);
    }

    private void checkForFullAnimation(int giftID, String giftName, double giftPrice, String
            userName) {
        if (mContext == null)
            return;

        /*try {
            double count = Double.parseDouble(userCashTextView.getText().toString());
            int coins = (int) (count + giftPrice);
            userCashTextView.setText(coins + "");
        } catch (Exception e) {
            LogUtils.e("checkForFullAnimation", "" + e.getMessage());
        }*/
//        playGifs(giftID + ".gif");
//        playGifsWithGlide(giftID + ".gif");
        addAnimationGiftBullet(userName, EnumUtils.GiftResById.getGiftRes(giftID), giftName);

        switch (giftID) {
            case 1005:
                /*if (topAnimationImageView.getVisibility() == View.VISIBLE) {
                    topAnimationImageView.setVisibility(View.GONE);
                }
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                topAnimationImageView.setLayoutParams(layoutParams);
                Animations.heartViewAnimation(mContext, topAnimationImageView);*/
                break;
            case 1019:
//                addAnimationGiftBullet_();
                break;
            case 1004:
//                addAnimationGiftBullet(userName, EnumUtils.GiftResById.getGiftRes(giftID), giftName);
//                addAnimationGiftBullet();
                break;
            case 500:
                Animations.slideInRightViewAnimation(GoLiveActivity.this, giftSlideInView);
                break;
            case 1014:
                /*if (topAnimationImageView.getVisibility() == View.VISIBLE) {
                    topAnimationImageView.setVisibility(View.GONE);
                }
                RelativeLayout.LayoutParams layoutParamsStart = new RelativeLayout.LayoutParams(Utils.dp2px(this, 250), Utils.dp2px(this, 250));
                layoutParamsStart.addRule(RelativeLayout.CENTER_IN_PARENT);
                topAnimationImageView.setLayoutParams(layoutParamsStart);
                Animations.smallToLargeStarViewAnimation(GoLiveActivity.this, topAnimationImageView);*/
                break;
            case 1011: // OUT OF MEMORY
//                Animations.smallToLargeGuiterViewAnimation(GoLiveActivity.this, topAnimationImageView);
                break;
        }
    }

    private void startFullAnimation(int giftID, String giftName, double giftPrice, String
            userName, String gifURL) {
        if (mContext == null)
            return;


        playGifs("level_0", giftID + ".gif");
        addAnimationGiftBullet(userName, EnumUtils.GiftResById.getGiftRes(giftID), giftName);
    }


    private void showShowerAnimation() {

        new CountDownTimer(10000, 300) {
            public void onTick(long millisUntilFinished) {
                bubbleViewGoLive.setBubbleDrawable(getResources().getDrawable(EnumUtils.GiftResById.getCandyRes(new Random().nextInt(11))),
                        Utils.dp2px(GoLiveActivity.this, 40), Utils.dp2px(GoLiveActivity.this, 40));

                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
                bubbleViewGoLive.addBubble(new Random().nextInt(goLiveBottomTabParent.getWidth()), goLiveBottomTabParent.getTop() + 50, 5);
            }

            public void onFinish() {

            }
        }.start();
    }

    private void manageGiftCount(int giftID, int giftPrice) {

        if (giftCount != null && giftCount.size() > 0) { // HAVE VALUE

            if (giftCount.containsKey(giftID)) { // ALREADY HAVE
                giftCount.put(giftID, new GiftItemBO(giftID, giftCount.get(giftID).getItemCount() + 1, giftPrice));
            } else {// ADD FIRST TIME
                giftCount.put(giftID, new GiftItemBO(giftID, 1, giftPrice));
            }
        } else { // ADD FIRST TIME
            giftCount.put(giftID, new GiftItemBO(giftID, 1, giftPrice));
        }
    }

    //service name: add-gift'
    //Type: Post
    //Params: 'user_id','stream_user_id','stream_id', 'gift_detail'(object or array or jason )
    // METHOD TO MAKE NETWORK CALL TO SEND GIFT COUNT
    public static void sendGiftCountNetworkCall(final Context context, String userID, String
            streamUserID, String streamID, String token, HashMap<Integer, GiftItemBO> giftCount_,
                                                boolean withDuration) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.STREAM_USER_ID, streamUserID);
        serviceParams.put(Keys.STREAM_ID, streamID);
        if (withDuration) {
            serviceParams.put(Keys.TIME_DURATION, (System.currentTimeMillis() - startTime));
        } else {
            serviceParams.put(Keys.TIME_DURATION, 0);
        }
        serviceParams.put(Keys.GIFT_DETAIL, getGiftCountJson(giftCount_));

        tokenServiceHeaderParams.put(Keys.TOKEN, token);


        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.add_gift,
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

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                UserBO userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                                userBO.setChips(jsonObject.getInt("updated_chips"));
                                ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
                                UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);
                                giftCount = new HashMap<>();
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

    private static String getGiftCountJson(HashMap<Integer, GiftItemBO> giftCount) {
        List<JSONObject> jsonObj = new ArrayList<JSONObject>();

        for (Integer key : giftCount.keySet()) {
            try {
                JSONObject obj = new JSONObject(new Gson().toJson(giftCount.get(key)));
                jsonObj.add(obj);
            } catch (Exception e) {
                LogUtils.e("getGiftCountJson", "" + e.getMessage());
            }
        }
        return new JSONArray(jsonObj).toString();
    }

    // face detection
    private void initVideoFrameRenderer() {

        WZRenderAPI.VideoFrameRenderer videoFrameRenderer = new WZRenderAPI.VideoFrameRenderer() {
            @Override
            public boolean isWZVideoFrameRendererActive() {
                return mGrabFrame.get();
            }

            @Override
            public void onWZVideoFrameRendererInit(WZGLES.EglEnv eglEnv) {

            }

            @Override
            public void onWZVideoFrameRendererDraw(final WZGLES.EglEnv eglEnv, final WZSize wzSize, final int i) {

                mGrabFrame.set(false);

                final WZSize bitmapSize = new WZSize(wzSize);
                final ByteBuffer pixelBuffer = ByteBuffer.allocateDirect(bitmapSize.getWidth() * bitmapSize.getHeight() * 4);

                pixelBuffer.order(ByteOrder.LITTLE_ENDIAN);
                GLES20.glReadPixels(0, 0, bitmapSize.getWidth(), bitmapSize.getHeight(), GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
                final int eglError = WZGLES.checkForEglError(TAG + "(glReadPixels)");
                if (eglError != EGL14.EGL_SUCCESS) {
                    return;
                }
                pixelBuffer.rewind();

                AsyncJob asyncJob = new AsyncJob.AsyncJobBuilder<Boolean>().doInBackground(new AsyncJob.AsyncAction<Boolean>() {
                    @Override
                    public Boolean doAsync() {

                        bitmap = Bitmap.createBitmap(bitmapSize.getWidth(), bitmapSize.getHeight(), Bitmap.Config.ARGB_8888);
                        bitmap.copyPixelsFromBuffer(pixelBuffer);

                        Matrix xformMatrix = new Matrix();
                        xformMatrix.setScale(-1, 1);  // flip horiz
                        xformMatrix.preRotate(180);  // flip vert
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapSize.getWidth(), bitmapSize.getHeight(), xformMatrix, false);

                        if (detector.isOperational()) {
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                            mFaces = detector.detect(frame);
//                            Log.d("testing", "faces: " + mFaces.size());
                        }

                        if (mFaces.size() > 0) {

                            chosenFace = mFaces.get(mFaces.keyAt(0));

                            if (chosenFace != null && mCameraView != null) {
                                float faceScale = chosenFace.getWidth() / (float) mCameraView.getWidth();

                                mNinjaFace.setPosition((int) (chosenFace.getPosition().x + (chosenFace.getWidth() / 2)), (int) (mCameraView.getHeight() - chosenFace.getPosition().y - (chosenFace.getHeight() / 2)));
                                mNinjaFace.setScale(faceScale, WZBitmap.FRAME_WIDTH);
                            }
                        }
                        if (mFaces.size() == 0) {
                            chosenFace = null;
                        }

                        return true;
                    }
                }).doWhenFinished(new AsyncJob.AsyncResultAction<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        mNinjaFace.setVisible(chosenFace != null);
                        mGrabFrame.set(true);
                    }
                }).create();
                asyncJob.start();
            }

            @Override
            public void onWZVideoFrameRendererRelease(WZGLES.EglEnv eglEnv) {

            }
        };

        mCameraView.registerFrameRenderer(videoFrameRenderer);
        mGrabFrame.set(true);
    }

    private void playGifs(String levelNumber, String gifName) {
        try {
            File file = Utils.getFileFromAssets(this, gifName); /*FROM ASSETS*/
//            File file = new File(Environment.getExternalStorageDirectory(), Constants.WITKEY_GIFTS_FOLDER_PATH + File.separator + levelNumber + File.separator + gifName); /*FROM EXTERNAL MEMORY*/
            if (file != null && file.exists()) {

                gifImageView.setVisibility(View.VISIBLE);

                gifImageView.setImageURI(Uri.parse("file:///" + Utils.getFileFromAssets(this, gifName)));/*FROM ASSETS*/
//                gifImageView.setImageURI(Uri.fromFile(file));/*FROM EXTERNAL MEMORY*/
                gifDrawable = (GifDrawable) gifImageView.getDrawable();/*FROM ASSETS*/
                gifDrawable.addAnimationListener(this);
                if (!gifDrawable.isPlaying()) {
                    resetAnimation();
                    gifDrawable.start();
                }
            }
        } catch (Exception e) {
            LogUtils.e("playGifs", "" + e.getMessage());
        }

    }

    /*PLAY WITH URL*/
    /*private void playGifsWithGlide(String giftURL) {
        try {
            topAnimationImageView.setVisibility(View.VISIBLE);

            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(topAnimationImageView, 1);
            Glide.with(this)
                    .load(giftURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {

                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            topAnimationImageView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                            GifDrawable gifDrawable = null;
                            Handler handler = new Handler();
                            if (resource instanceof GifDrawable) {
                                gifDrawable = (GifDrawable) resource;

                                int duration = 0;
                                GifDecoder decoder = gifDrawable.getDecoder();
                                for (int i = 0; i < gifDrawable.getFrameCount(); i++) {
                                    duration += decoder.getDelay(i);
                                }

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        topAnimationImageView.setVisibility(View.GONE);
                                    }
                                }, (duration) + 3000);

                            }

                            return false;
                        }

                    })
                    .into(imageViewTarget);
        } catch (Exception e) {
            LogUtils.e("playGifs", "" + e.getMessage());
        }

    }*/

    private void resetAnimation() {
        gifDrawable.stop();
        gifDrawable.setLoopCount(1);
    }

    @Override
    public void onAnimationCompleted(int loopNumber) {
        gifImageView.setVisibility(View.GONE);
    }


    // METHOD TO GET PACKAGES DETAIL NETWORK CALL
    // http://18.220.157.19/witkey_dev/api/get-packages
    private void getPackagesListNetworkCall(final Context context,
                                            final RecyclerView purchaseList, final SwipeRefreshLayout swipeRefreshLayout,
                                            final LinearLayout noResultParent) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.get_packages,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        swipeRefreshLayout.setRefreshing(false);

                        if (myWalletAdapter == null) {
                            noResultParent.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                        }
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("Packages");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<PackagesBO>>() {
                                }.getType();
                                List<PackagesBO> newStreams = (List<PackagesBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                if (myWalletAdapter != null && myWalletAdapter.getItemCount() > 0) {
                                    //for the case of pulltoRefresh...
                                    myWalletAdapter.clearItems();
                                }

                                noResultParent.setVisibility(View.GONE);
                                swipeRefreshLayout.setVisibility(View.VISIBLE);
                                swipeRefreshLayout.setRefreshing(false);
                                setUpWalletRecycler(newStreams, purchaseList);
                            }
                        } catch (Exception ex) {
                            swipeRefreshLayout.setRefreshing(false);
                            noResultParent.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setVisibility(View.GONE);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void showHideForGiftDialog(boolean show) {

        if (show) {
            goLiveChatViewer.setVisibility(View.VISIBLE);
            oldChatView.setVisibility(View.VISIBLE);
            animationViewParent.setVisibility(View.VISIBLE);
        } else {
            goLiveChatViewer.setVisibility(View.GONE);
            oldChatView.setVisibility(View.GONE);
            animationViewParent.setVisibility(View.GONE);
        }
    }

    // METHOD TO GET POSTERS
    //http://18.220.157.19/witkey_dev/api/get_banner/{1: for home banner, 2 : for live screen banner}
    // http://18.220.157.19/witkey_dev/api/get_banner
    private void getEventPostersNetworkCall(Context context) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.get_banner,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        GlobalBus.getBus().post(new Events.EventPosterActivityMessage(null));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                programmeBOs = new ArrayList<>();

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("images_array");

                                for (int i = 0; i < favoritesJsonArray.length(); i++) {
                                    programmeBOs.add(new ProgrammeBO(-1, favoritesJsonArray.optString(i)));
                                }
                                GlobalBus.getBus().post(new Events.EventPosterActivityMessage(programmeBOs));
                            }
                        } catch (Exception ex) {
                            GlobalBus.getBus().post(new Events.EventPosterActivityMessage(null));
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    // METHOD TO MAKE NETWORK CALL TO END CURRENT STREAM
    private void endCurrentStreamNetworkCall(final Context context, String streamID, String token, final String timeDuration) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        serviceParams.put(Keys.ID, streamID);
//            String dateTime = "0000-00-00 " + Utils.getTimeInHrMinSec(timeInMilliseconds);
        serviceParams.put(Keys.STREAM_TIME, timeDuration);
        serviceParams.put(Keys.TIME_DURATION, timeDuration);

        tokenServiceHeaderParams.put(Keys.TOKEN, token);


        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.streams_id,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                UserBO userBO_ = new Gson().fromJson(jsonObject.get("user_detail").toString(), UserBO.class);
                                userBO_.setToken(userBO.getToken());
                                UserSharedPreference.saveUserToken(userBO.getToken());
                                ObjectSharedPreference.saveObject(userBO_, Keys.USER_OBJECT);
                                UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);
                                StreamEndSummaryBO streamEndSummaryBO = new Gson().fromJson(jsonObject.get("stream_summery").toString(), StreamEndSummaryBO.class);
                                streamEndSummaryBO.setEndStreamSummary(Utils.getTimeFormatedFromMiliSec(timeDuration));
                                gotoNextFragment(LiveFeedEndedFragment.newInstance(streamEndSummaryBO));
                                fromEndSummary = true;
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
