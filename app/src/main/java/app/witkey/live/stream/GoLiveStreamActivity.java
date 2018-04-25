package app.witkey.live.stream;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.ksyun.media.player.IMediaPlayer;
import com.ksyun.media.player.KSYMediaPlayer;
import com.ksyun.media.streamer.capture.camera.CameraTouchHelper;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyProFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgBeautyToneCurveFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterBase;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;
import com.ksyun.media.streamer.logstats.StatsLogReport;
import com.ksyun.media.streamer.util.gles.GLRender;
import com.lion.materialshowcaseview.IShowcaseSequenceListener;
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

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.WitKeyApplication;
import app.witkey.live.activities.DeepLinkActivity;
import app.witkey.live.activities.EventPosterDetailActivity;
import app.witkey.live.activities.ScanCodeActivity;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.activities.expansion.Config;
import app.witkey.live.activities.SplashActivity;
import app.witkey.live.adapters.dashboard.profile.FilterImagesAdapter;
import app.witkey.live.adapters.dashboard.profile.MyWalletAdapter;
import app.witkey.live.adapters.dashboard.stream.GoLiveSlideViewAdapter;
import app.witkey.live.adapters.dashboard.stream.GoLiveTopAdapter;
import app.witkey.live.adapters.dashboard.stream.StreamingConversationAdapter;
import app.witkey.live.fragments.dashboard.message.PrivateMessageFragment;
import app.witkey.live.fragments.dashboard.payment.WebviewFragment;
import app.witkey.live.fragments.dashboard.profile.BroadcastRankActivity;
import app.witkey.live.fragments.dashboard.stream.DailyMissionEnergyFragment;
import app.witkey.live.fragments.dashboard.stream.GoLiveStreamerStartFragment;
import app.witkey.live.fragments.dashboard.stream.LiveFeedEndedFragment;
import app.witkey.live.fragments.dashboard.stream.SetOfStickersFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.items.FilterBO;
import app.witkey.live.items.GiftItemBO;
import app.witkey.live.items.PackagesBO;
import app.witkey.live.items.ProgrammeBO;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.StreamEndSummaryBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.items.WalletBO;
import app.witkey.live.stream.kit.ImgFaceunityFilter;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.DateTimeOp;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.ScreenShotUtils;
import app.witkey.live.utils.StreamConfig;
import app.witkey.live.utils.StreamUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.R.attr.data;

public class GoLiveStreamActivity extends BaseActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback,
        View.OnClickListener, AnimationListener {

    private final static String TAG = "JKS";//GoLiveStreamActivity.class.getSimpleName();
    Context mContext;

    private static final String SHOWCASE_ID = "GoLive";

    @BindView(R.id.goLiveParentView)
    LinearLayout goLiveParentView;

    @BindView(R.id.oldChatView)
    LinearLayout oldChatView;

    @BindView(R.id.filterListView)
    LinearLayout filterListView;

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

    @BindView(R.id.userShareImageView)
    ImageView userShareImageView;

    @BindView(R.id.closeChatTab)
    ImageView closeChatTab;

    @BindView(R.id.screenShotCloseImageView)
    ImageView screenShotCloseImageView;

    @BindView(R.id.mRecyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.recyclerViewFilterImages)
    RecyclerView recyclerViewFilterImages;

    @BindView(R.id.cameraMessagesParentFrame)
    RelativeLayout cameraMessagesParentFrame;

    @BindView(R.id.cameraMessages)
    CustomTextView cameraMessages;
    @BindView(R.id.newUserEntered)
    CustomTextView newUserEntered;
    @BindView(R.id.micTextView)
    CustomTextView micTextView;

    @BindView(R.id.emojiToggle)
    CustomTextView emojiToggle;

    @BindView(R.id.filterToggle)
    CustomTextView filterToggle;

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
    @BindView(R.id.userLevelIV)
    ImageView userLevelIV;

    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.giftBulletView)
    GiftView giftBulletView;

    @BindView(R.id.viewTopLayer)
    View viewTopLayer;

    boolean mediaPlayerOnBack = false;

    /*PLAYER*/
    // Player object
    private KSYMediaPlayer ksyMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    /*PLAYER*/

    String giftJsonString = "";
    public static HashMap<Integer, GiftItemBO> giftCount;
    private List<WalletBO> walletBOs;
    private MyWalletAdapter myWalletAdapter;

    boolean fromPaymentPage = false;
    boolean fromEndSummary = false;
    boolean fromDailyMission = false;
    GoLiveTopAdapter goLiveTopAdapter;
    private UserBO userBO;
    GifDrawable gifDrawable;

    public static final int RECORD_AUDIO_REQUEST_CODE = 200;
    public static final int REQUEST_STORAGE_PERMISSION = 201;
    public static final int REQUEST_TO_READ_STORAGE_PERMISSION = 204;
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
    Bitmap bitmap;
    private Bitmap screenshotBitmap;
    StreamBO streamBO;
    ProgressDialog progressDialog;
    private SubscribeCallback subscribeCallback;

    private Dialog dialogEndBroadcast;
    private Dialog dialogPurchaseCoins;

    private boolean beautyModeOn = false;
    private boolean hdQualityOn = false;
    private boolean tutorials = false;

    public static long startTime = 0;
    public static int SHARE_MOMENT = 100;

    int progressCount = 0;
    boolean loopChatJoinAnimationStatus = true;
    Handler handler = new Handler();
    Handler handlerJoinChat = new Handler();

    private ArrayList<ProgrammeBO> programmeBOs;

    /*KSY STREAMER LIB*/
    @BindView(R.id.streamer_camera_preview)
    GLSurfaceView mCameraPreviewView;
    @BindView(R.id.camera_hint)
    CameraHintView mCameraHintView;
    @BindView(R.id.debuginfo)
    TextView mDebugInfoTextView;
    @BindView(R.id.beauty_choose)
    View mBeautyChooseView;
    @BindView(R.id.beauty_spin)
    AppCompatSpinner mBeautySpinner;
    @BindView(R.id.beauty_grind)
    LinearLayout mBeautyGrindLayout;

    @BindView(R.id.grind_seek_bar)
    AppCompatSeekBar mGrindSeekBar;
    @BindView(R.id.beauty_whiten)
    LinearLayout mBeautyWhitenLayout;
    @BindView(R.id.whiten_seek_bar)
    AppCompatSeekBar mWhitenSeekBar;
    @BindView(R.id.beauty_ruddy)
    LinearLayout mBeautyRuddyLayout;
    @BindView(R.id.ruddy_seek_bar)
    AppCompatSeekBar mRuddySeekBar;
    @BindView(R.id.video_surface_view)
    SurfaceView mVideoSurfaceView;

    private KSYStreamer mStreamer;
    private ImgFaceunityFilter mImgFaceunityFilter;
    private Handler mMainHandler;
    private Handler slideViewHandler;
    private Timer mTimer;

    private boolean mAutoStart;
    private boolean mIsLandscape;
    private boolean mPrintDebugInfo = false;
    private boolean mRecording = false;
    private boolean mIsFileRecording = false;
    private String mDebugInfo = "";
    private String mRecordUrl = "/sdcard/rec_test.mp4";

    private boolean mHWEncoderUnsupported;
    private boolean mSWEncoderUnsupported;

    FilterImagesAdapter filterImagesAdapter;

    private final static int PERMISSION_REQUEST_CAMERA_AUDIOREC = 1;
    /*KSY STREAMER LIB*/
    private int slideViewCounter = 0;
    private int count = 0;
    private int giftPanelUserLevel = 1;

    // todo
    //private MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

    /*PLAYER*/
    private IMediaPlayer.OnErrorListener mOnErrorListenerPlayer = new IMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
            AlertOP.showAlert(GoLiveStreamActivity.this, "",
                    getString(R.string.cant_play_video),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            stopPlayer();
            return false;
        }
    };

    private IMediaPlayer.OnCompletionListener mOnCompletionListener = new IMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(IMediaPlayer mp) {

            AlertOP.showAlert(GoLiveStreamActivity.this, "",
                    getString(R.string.video_is_ended),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            //Playback completed, the user can choose to release the player
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.stop();
                ksyMediaPlayer.release();
            }
        }
    };

    private final SurfaceHolder.Callback mSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(holder);
                ksyMediaPlayer.setScreenOnWhilePlaying(true);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder Holder) {
            // here is very important, you must call !!!
            if (ksyMediaPlayer != null) {
                ksyMediaPlayer.setDisplay(null);
            }
        }
    };

    private IMediaPlayer.OnPreparedListener mOnPreparedListener = new IMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(IMediaPlayer mp) {
            if (ksyMediaPlayer != null) {
                // Set the video scaling mode, which is crop mode
                ksyMediaPlayer.setVideoScalingMode(KSYMediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                // start playing video
                ksyMediaPlayer.start();
                storagePermissionCheck();
                try {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showHideUserBlurImage(false);
                        }
                    }, 3000);
                } catch (Exception e) {
                    Log.e(TAG, "" + e.getMessage());
                }


            }
        }
    };

    /*PLAYER*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live_stream);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        mContext = this;
        if (bundle != null) {
            String type = bundle.getString(ARG_TYPE);
            if (type.equals(ARG_PARAM_1)) {
                streamPlayStatus = false;
                streamBO = getIntent().getParcelableExtra(ARG_PARAM_3);
                goLive(streamBO);
            } else if (type.equals(ARG_PARAM_2)) {
                streamPlayStatus = true;
                streamBO = getIntent().getParcelableExtra(ARG_PARAM_3);

                Log.d(TAG, "Streaming Data " + new Gson().toJson(streamBO));
                playVideo(streamBO);
            } else if (type.equals(ARG_PARAM_5)) {
                initView();
                streamPlayStatus = true;
                tutorials = true;
                showHideViewOnPlayStream(true);
                startTutorials();
                initPubNub(false);
            }

            Log.d("JKS", "type " + type);
            Log.i("JKS", new Gson().toJson(streamBO));
        }
        startTime = System.currentTimeMillis();

        getEventPostersNetworkCall(GoLiveStreamActivity.this, streamPlayStatus, streamBO != null ? streamBO.getId() : "0");
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
//        playStreamVideo(streamBO);

        showHideViewOnPlayStream(true);
//        if (streamBO != null && streamBO.getStatus().equals(StreamUtils.STATUS_ENDED)) {
//            AlertOP.showAlert(GoLiveStreamActivity.this, "",
//                    getString(R.string.video_is_ended),
//                    new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//        } else {

        initPlayer(streamBO);/*KSYPLAYER*/
        if (userBO != null && streamBO != null) {
            liveViewersCountNetworkCall(this, StreamUtils.ACTION_VIEW, userBO.getId(), streamBO.getId(), UserSharedPreference.readUserToken());
            if (streamBO.getStatus().equals(StreamUtils.STATUS_LIVE)) {
                showChatJoinBubbleAnimation();/*IF STREAM LIVE START SHOWING*/
                initPubNub(true);
            } else {
                initPubNub(false);
            }
        }
        //}
    }

    // METHOD TO CREATE LIVE STREAM
    private void goLive(StreamBO streamBO) {
        initView();

        /*KSY STREAMER LIB*/
        mMainHandler = new Handler();
        showHideViewOnPlayStream(false);
        showProgress();
        if (!Constants.BUILD_TYPE_QA) { // TODO FOR TESTING ONLY
            initStreamerObject(streamBO);
        }
        initFaceunity(); // set face filters
        initBeautyUI();// set beauty filter
        initPubNub(false);

        //onBeautyChecked(true); /*BEAUTY FILTERS*/
        //onMuteChecked(isChecked); /*STREAM AUDIO SETTING*/
        //onFaceunityPropCheck(true); /*STICKERS*/
        //onBackoffClick(); /*EXIT STREAM*/
        //onSwitchCamera(); /*SWITCH CAMERA*/
        //onShootClick();/*STREAM START AND STOP*/
        //onRecordClick();/*STREAM RECORDING*/
        //onCaptureScreenShotClick(); /*SCREEN SHOT*/

        /*KSY STREAMER LIB*/
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
        userShareImageView.setOnClickListener(this);
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
        emojiToggle.setOnClickListener(this);
        filterToggle.setOnClickListener(this);

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

    // METHOD TO SHOW PROGRESS DAILOG
    private void showProgress() {

        progressDialog = new ProgressDialog(GoLiveStreamActivity.this);
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
                    getPackagesListNetworkCall(GoLiveStreamActivity.this, purchaseList, swipeRefreshLayout, noResultParent);
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPackagesListNetworkCall(GoLiveStreamActivity.this, purchaseList, swipeRefreshLayout, noResultParent);
            }
        });

        noResultRefreshTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRefreshLayout.setRefreshing(true);
                getPackagesListNetworkCall(GoLiveStreamActivity.this, purchaseList, swipeRefreshLayout, noResultParent);
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
                    fromPaymentPage = true;
                    mediaPlayerOnBack = true;
                    gotoNextFragment(WebviewFragment.newInstance(packagesBOList.get(position).getAmount(), packagesBOList.get(position).getId() + "", (packagesBOList.get(position).getWitky_chips() + packagesBOList.get(position).getFree_chips() + packagesBOList.get(position).getPromotion()) + ""));
                    /*if (packagesBOList.get(position).getAllow_promotion() == 1) {

                        gotoNextFragment(WebviewFragment.newInstance(packagesBOList.get(position).getAmount(), packagesBOList.get(position).getId() + "", (packagesBOList.get(position).getWitky_chips() + ((packagesBOList.get(position).getWitky_chips() * packagesBOList.get(position).getPromotion()) / 100)) + ""));
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

            case R.id.viewTopLayer:
                if (filterListView.getVisibility() == View.VISIBLE) {
                    filterListView.setVisibility(View.GONE);
                    viewTopLayer.setOnClickListener(null);
                }
                break;

            case R.id.goLiveBottomOpt1:
                new GoLiveMenuOptionsDialog().
                        show(getSupportFragmentManager(), "Option 1");
                break;
            case R.id.goLiveBottomOpt2:
                showHideChatCreator(true);
                break;
            case R.id.goLiveBottomOpt3:
                onSwitchCamera();
                break;
            case R.id.goLiveBottomOpt4:
                onCaptureScreenShotClick();
                break;
            case R.id.goLiveBottomOpt5:
                new GoLiveInviteOptionsDialog().
                        show(getSupportFragmentManager(), "Option 5");
                break;
            case R.id.goLiveBottomOpt6:
                startActivity(new Intent(GoLiveStreamActivity.this, ScanCodeActivity.class));
                break;
            case R.id.userViewImageView:
                new GoLiveViewersSummaryDialog().newInstance(new ArrayList<ConversationBO>(onlineConversationBOs)).show(getSupportFragmentManager(), "Option 6");
                break;
            case R.id.userCancelImageView:
                if (streamPlayStatus) {
//                    if (giftCount != null && giftCount.size() > 0) { /*IF WANT TO CHECK GIFTS EXIST*/
                    if (userBO != null && streamBO != null && streamBO.getUser_details() != null) {
                        sendGiftCountNetworkCall(this, userBO.getId(), streamBO.getUser_details().getId(), streamBO.getId(), UserSharedPreference.readUserToken(), giftCount, true);
                    }
                    stopPlayer();
//                    }
                    finish();
                } else {
                    showEndBroadcastDialog();
                }
                break;
            case R.id.userShareImageView:
                DeepLinkActivity.share(this, streamBO.getUuid());
                break;
            case R.id.screenShotPopupSave:
                MarshMallowPermission permission = new MarshMallowPermission(this);
                if (permission.checkPermissionForExternalStorage()) {
                    saveCapturedBitmap();
                } else {
                    permission.requestPermissionForExternalStorage(REQUEST_STORAGE_PERMISSION, this);
                }
                break;
            case R.id.screenShotPopupShare:
                if (screenshotBitmap != null) {
                    ScreenShotUtils.shareBitmapScreenShot(screenshotBitmap, this);
                    screenShotView.setVisibility(View.GONE);
                } else {
                    Utils.showToast(GoLiveStreamActivity.this, getString(R.string.nothing_to_share));
                    screenShotView.setVisibility(View.GONE);
                }
                break;
            case R.id.screenShotCloseImageView:
                screenShotView.setVisibility(View.GONE);
                break;
            case R.id.userImageParentFrame:
                new GoLiveHomeProfileDialog().newInstance(onlineConversationBOs != null ? onlineConversationBOs.size() + "" : "0", streamBO, streamPlayStatus, userCashTextView.getText().toString()).show(getSupportFragmentManager(), "Option 8");//, userCashTextView.getText().toString()
//                new GoLiveHomeProfileDialog().newInstance(onlineConversationBOs != null ? onlineConversationBOs.size() + "" : "0", streamBO).show(getSupportFragmentManager(), "Option 8");
                break;
            case R.id.dailyMissionEnergyImageView:
                if (streamPlayStatus) {
                    fromPaymentPage = true;
                    mediaPlayerOnBack = true;
                    gotoNextFragment(DailyMissionEnergyFragment.newInstance());
                } else {
                    fromDailyMission = true;
                    startActivity(new Intent(GoLiveStreamActivity.this, BroadcastRankActivity.class));
                }
                break;
            case R.id.btnEndBroadcastOk:
                if (streamBO != null && userBO != null) {
                    if (!Constants.BUILD_TYPE_QA) {
                        onShootClick();/*STOP STREAM HERE*/
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
                Intent intent = new Intent(GoLiveStreamActivity.this, EventPosterDetailActivity.class);
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
                if (ksyMediaPlayer != null) {
                    if (ksyMediaPlayer.isPlaying()) {
                        screenshotBitmap = ksyMediaPlayer.getScreenShot();
                        if (screenshotBitmap != null) {
                            showBitmap();
                        }
                    }
                }
                break;

            case R.id.goLiveViewerChatFolder:
                new GoLiveBroadcastGiftReceivedDialog().show(getSupportFragmentManager(), "Gifts");
                break;
            case R.id.goLiveViewerChatGift:
                loopChatJoinAnimationStatus = false;
                new GoLiveBroadcastGiftPanelDialog().newInstance(giftJsonString, giftPanelUserLevel).show(getSupportFragmentManager(), "Gifts");
                showHideForGiftDialog(false);
                break;

            case R.id.goLiveViewerChatPost:
                if (streamBO != null && userBO != null) {
                    new ShareStreamDialog().newInstance(streamVideoURL, userBO.getUsername(), userBO.getId(), streamBO.getId(), streamBO.getUuid(), streamBO.getStatus().equals(StreamUtils.STATUS_LIVE)).show(getSupportFragmentManager(), "ShareStreamDialog");
                }/* else {
                    Utils.showToast(this, getString(R.string.cant_share));
                }*/
                break;
            case R.id.goLiveViewerChatMessage:
                fromPaymentPage = true;
                mediaPlayerOnBack = true;
                gotoNextFragment(PrivateMessageFragment.newInstance(streamBO.getUser_details().getId(), true, "", streamBO.getUser_details().getUsername()));
//                showShowerAnimation();
                break;

            case R.id.video_view:
                Utils.showToast(GoLiveStreamActivity.this, getString(R.string.will_be_implemented_later));
                break;

            case R.id.emojiToggle:
                setEmojiFilterToggle(true, false);
                break;
            case R.id.filterToggle:
                setEmojiFilterToggle(false, false);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        /*KSY STREAMER LIB*/
        if (mStreamer != null) {
            mStreamer.onPause();
            mStreamer.setUseDummyAudioCapture(true);
            mStreamer.stopCameraPreview();
        }
        if (streamPlayStatus) {
            if (ksyMediaPlayer != null) {
                if (ksyMediaPlayer.isPlaying()) {
                    mediaPlayerOnBack = true;
                    ksyMediaPlayer.pause();
                }
            }
        }
        /*KSY STREAMER LIB*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {*/

        /*KSY STREAMER LIB*/
        if (!streamPlayStatus) {
            startCameraPreviewWithPermCheck();
            if (mStreamer != null) {
                mStreamer.onResume();
                mStreamer.setUseDummyAudioCapture(false);
            }
            if (mCameraHintView != null) {
                mCameraHintView.hideAll();
            }
        }
        if (streamPlayStatus) {
//            storagePermissionCheck();
            if (ksyMediaPlayer != null) {
                if (mediaPlayerOnBack) {
                    ksyMediaPlayer.start();
                }
            }
        }
        /*KSY STREAMER LIB*/

        /*} else if (ActivityCompat.shouldShowRequestPermissionRationale(this,
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
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mStreamer != null) {
                        if (mStreamer.isRecording()) {
                            mStreamer.onResume();
                        } else {
                            mStreamer.startCameraPreview();
                        }
                    }
                } else {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case RECORD_AUDIO_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mStreamer != null) {
                        startRecord();
                    }
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
                    saveCapturedBitmap();

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

        public static GoLiveStreamerStartFragment.ConfirmationDialogFragment newInstance(@StringRes int message,
                                                                                         String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            GoLiveStreamerStartFragment.ConfirmationDialogFragment fragment = new GoLiveStreamerStartFragment.ConfirmationDialogFragment();
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

        try {
            if (streamPlayStatus) {
                if (streamBO != null) {
                    if (streamBO.getUser_details() != null) {
                        UserLevelBO userLevelBO = Utils.getBroadcasterLevel(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level());
                        userCashTextView.setText(streamBO.getUser_details().getWitkeyDollar().intValue() + "");
//                        updateUserDollars((streamBO.getUser_details().getWitkeyDollar().intValue()), 0);
                        userPointsTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getArtist_total_tornados() + "");
                        userDiamondTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "");
                        userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                        userImageIV.getLayoutParams().width = userImageIV.getLayoutParams().height;
                        Utils.setImageRounded(userImageIV, streamBO.getUser_details().getProfilePictureUrl(), this);
                        if (streamBO.getUser_details().getUsername().length() > 10) {
                            userNameTextView.setText(streamBO.getUser_details().getUsername().substring(0, 9) + "...");
                        } else {
                            userNameTextView.setText(streamBO.getUser_details().getUsername());
                        }
                    }
                }
            } else {

                if (userBO != null) {
                    UserProgressDetailBO userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
                    if (userProgressDetailBO != null) {

                        UserLevelBO userLevelBO = Utils.getBroadcasterLevel(userProgressDetailBO.getArtist_level());
                        userPointsTextView.setText(userProgressDetailBO.getArtist_total_tornados() + "");
                        userDiamondTextView.setText(userProgressDetailBO.getArtist_level() + "");
                        userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                    }

                    userCashTextView.setText(userBO.getWitkeyDollar().intValue() + "");
//                    updateUserDollars((userBO.getWitkeyDollar().intValue()), 0);
                    userImageIV.getLayoutParams().width = userImageIV.getLayoutParams().height;
                    Utils.setImageRounded(userImageIV, userBO.getProfilePictureUrl(), this);

                    if (userBO.getUsername().length() > 10) {
                        userNameTextView.setText(userBO.getUsername().substring(0, 9) + "...");
                    } else {
                        userNameTextView.setText(userBO.getUsername());
                    }
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "userCashTextView " + e.getMessage());
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

    // METHOD TO SHOW WOWZA BITMAP ON CUSTOM CARD VIEW
    private void showBitmap() {

        try {
            screenShotView.setVisibility(View.VISIBLE);
            screenShotImageView.setImageBitmap(Utils.getRoundedCornerOptionsBitmap(screenshotBitmap, 50, false, false, true, true));

            goLiveBottomOpt4.setEnabled(true);
            goLiveBottomOpt4.setClickable(true);

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());

        }

        mSavingFrame.set(false);
    }

    private void takeScreenshotOfStream() {

        try {
            bitmap = ScreenShotUtils.getScreenShot(goLiveParentView);
            screenShotView.setVisibility(View.VISIBLE);
            screenShotImageView.setImageBitmap(Utils.getRoundedCornerOptionsBitmap(bitmap, 50, false, false, true, true));

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());

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
                onSwitchCamera();
                break;
            case "BeautyMood":
                onBeautyChecked(true);
                /*if (beautyModeOn)
                    cameraMessages.setText(R.string.beauty_mode_turned_off);
                else
                    cameraMessages.setText(R.string.beauty_mode_turned_on);

                beautyModeOn = !beautyModeOn;
                cameraMessagesParentFrame.setVisibility(View.VISIBLE);*/
                break;
            case "ShareDialog":
                if (streamBO != null && userBO != null) {
                    new GoLiveShareDialog().newInstance(userBO.getUsername(), streamBO.getUuid(), userBO.getId(), streamBO.getId()).show(getSupportFragmentManager(), "Option 9");
                }
                break;
            case "FluentQuality":
                if (hdQualityOn) {
                    switchVideoResulotion();
                    cameraMessages.setText(getString(R.string.switched_to_fluent_quality));
                } else {
                    switchVideoResulotion();
                    cameraMessages.setText(getString(R.string.switched_to_hd_quality));
                }

                hdQualityOn = !hdQualityOn;

                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
                break;
            case "ShowStickers":
                onFaceunityPropCheck(true);
//                gotoNextFragment(SetOfStickersFragment.newInstance());
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
            case "ADD_FOLLOW":
                if (streamPlayStatus) {
                    if (streamBO != null) {
                        streamBO.getUser_details().setIs_follow("1");
                    }
                }
                break;
            case "ADD_UNFOLLOW":
                if (streamPlayStatus) {
                    if (streamBO != null) {
                        streamBO.getUser_details().setIs_follow("");
                    }
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
        Log.d(TAG, "Streaming URL " + streamVideoURL);
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
                if (mVideoView != null) {
                    mVideoView.stopPlayback();
                }
                return false;
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer vmp) {


                AlertOP.showAlert(GoLiveStreamActivity.this, "",
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
            emojiToggle.setVisibility(View.GONE);
            filterToggle.setVisibility(View.GONE);
            //progressBar.setVisibility(View.VISIBLE);
            showHideUserBlurImage(true);
            mVideoView.setVisibility(View.GONE);/*HERE*/
            mVideoSurfaceView.setVisibility(View.VISIBLE);
            goLiveListParentLL.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.GONE);

            /*KSY*/
            mCameraPreviewView.setVisibility(View.GONE);
            mCameraHintView.setVisibility(View.GONE);
            /*KSY*/
        } else {
            eventPosterIV.setImageResource(R.drawable.sporgram_nav);
            dailyMissionEnergyImageView.setImageResource(R.drawable.daily_mission);
            pointsTypeIV.setImageResource(R.drawable.tornado_new);
            oldChatView.setVisibility(View.VISIBLE);
            emojiToggle.setVisibility(View.GONE);
            filterToggle.setVisibility(View.GONE);
            goLiveChatViewer.setVisibility(View.GONE);
            goLiveChatCreator.setVisibility(View.GONE);
            userViewParent.setVisibility(View.VISIBLE);
            userViewImageView.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.VISIBLE);
            goLiveListParentLL.setVisibility(View.VISIBLE);
            //progressBar.setVisibility(View.GONE);
            showHideUserBlurImage(false);
            mVideoView.setVisibility(View.GONE);
            mVideoSurfaceView.setVisibility(View.GONE);
            /*KSY*/
            mCameraPreviewView.setVisibility(View.VISIBLE);
            mCameraHintView.setVisibility(View.VISIBLE);
            /*KSY*/
        }
    }

    // METHOD TO SHOW HIDE VIEW ACCORDING TO CHAT
    private void showHideChatCreator(boolean showChat) {

        if (showChat) {
//            oldChatView.setVisibility(View.VISIBLE);
            goLiveChatCreator.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.GONE);

        } else {
//            oldChatView.setVisibility(View.GONE);
            goLiveChatEDT.getText().clear();
            goLiveChatCreator.setVisibility(View.GONE);
            goLiveTopView.setVisibility(View.VISIBLE);
            goLiveBottomTab.setVisibility(View.VISIBLE);
            goLiveCenterView.setVisibility(View.VISIBLE);
        }
    }

    /*METHOD TO SHOW HIDE FILTERS LIST */
    private void showHideFiltersListView(boolean show, int filterType) {
        if (show) {
            filterListView.setVisibility(View.VISIBLE);
            viewTopLayer.setOnClickListener(this);
        } else {
            filterListView.setVisibility(View.GONE);
            viewTopLayer.setOnClickListener(null);
        }
        if (filterType == 1) { /*BEAUTY FILTER*/
            setUpFilterImagesRecycler(getFiltersList(true));
        } else if (filterType == 2) { /*STICKER FILTER*/
            setUpFilterImagesRecycler(getFiltersList(false));
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
                                                // TODO HAVE TO SHOW FOR SPECIFIC USERS ONLY
                                                micTextView.setText(conversationBO.getSenderId());
                                                newUserEntered.setText(conversationBO.getUsername() + ": " + conversationBO.getText());
                                                Animations.slideInRightViewAnimation(GoLiveStreamActivity.this, giftSlideInView);
                                            }
                                            // WHEN MESSAGE TYPE IS GIFT SENT MESSAGE
                                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
                                            addMessage(conversationBO);
                                            addBubble(conversationBO.getSocialGiftID());
//                                            checkForFullAnimation(conversationBO.getSocialGiftID());
                                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.LIVE_STATUS_REMOVE) {
                                            /*REMOVE OBJECT FROM LIVE LIST*/
                                            removeFromLiveStatus(conversationBO);
                                        }
                                    }
                                    if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.GIFT_SENT) {
//                                        addAnimationGiftBullet(conversationBO.getUsername(), EnumUtils.GiftResById.getGiftRes(conversationBO.getSocialGiftID()), conversationBO.getText()); // ALREADY USED IN METHOD BELOW
//                                        checkForFullAnimation(conversationBO.getSocialGiftID(), conversationBO.getText(), conversationBO.getGiftPrice(), conversationBO.getUsername());
                                        startFullAnimation(conversationBO.getSocialGiftID(), conversationBO.getText(), conversationBO.getGiftPrice(), conversationBO.getUsername(), conversationBO.getGiftURL(), conversationBO.getCount(), conversationBO.getDpUrl(), conversationBO.getType());
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
                Utils.dp2px(GoLiveStreamActivity.this, 30), Utils.dp2px(GoLiveStreamActivity.this, 30));

        bubbleViewGoLive.addBubble((int) goLiveBottomTabParent.getX() + (goLiveBottomTabParent.getWidth()) - Utils.dp2px(GoLiveStreamActivity.this, 30), goLiveBottomTabParent.getTop() - Utils.dp2px(GoLiveStreamActivity.this, 0), new Random().nextInt(9 - 5) + 5);
    }

    // METHOD TO CHECK IF MESSAGE ALREADY EXISTS THEN TRUE
    private boolean isAlreadyExist(ConversationBO conversationBO, Set<ConversationBO> conversationBOs) {
        try {
            if (conversationBOs != null && conversationBOs.size() > 0) {
                for (ConversationBO conversationBO1 : conversationBOs) {
                    if (conversationBO1.getSenderId().equals(conversationBO.getSenderId())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("isAlreadyExist", "" + e.getMessage());
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

            RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(WitKeyApplication.context) {
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
                        Utils.showToast(GoLiveStreamActivity.this, getString(R.string.will_be_implemented_later));
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
                            Toast.makeText(GoLiveStreamActivity.this, "Unable to fetch history", Toast.LENGTH_SHORT).show();
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
                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.CHAT_MESSAGE) {
                            if (!conversationBO.getText().toString().trim().isEmpty()) {
                                //for the case of message...
                                conversationBOs.add(conversationBO);
                            }
                        } else if (conversationBO.getConversationTypeFlag() == EnumUtils.ConversationType.LIVE_STATUS) {
                            String streamCreatorId = streamBO != null ? streamBO.getUserId() : "0";
                            if (!conversationBO.getSenderId().equals(userBO.getId()) && !conversationBO.getSenderId().equals(streamCreatorId)) {
                                //for the case of presence...
                                if (!isAlreadyExist(conversationBO, onlineConversationBOs)) {
                                    onlineConversationBOs.add(conversationBO);
                                    setUpFeaturedRecycler(new ArrayList<ConversationBO>(onlineConversationBOs));
                                }
                            }
                        }

                            /*if (!conversationBO.getText().toString().trim().isEmpty()) {
                                //for the case of message...
                                conversationBOs.add(conversationBO);
                            } else {
                                //for the case of presence...
                                if (!isAlreadyExist(conversationBO, onlineConversationBOs)) {
                                    onlineConversationBOs.add(conversationBO);
                                    setUpFeaturedRecycler(new ArrayList<ConversationBO>(onlineConversationBOs));
                                }
                            }*/

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
        String text = GoLiveStreamActivity.this.getString(Utils.getChatJoinedIndex());
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
        manageGiftCount(giftID, giftPrice, giftSendCount);

        /*TO SHOW GIFT COUNT TIMER AND SEND GIFT*/// TODO FOR NOW
        /*if (giftSendCount > 1) {
            showGiftTimer(conversationBO, giftSendCount);
        }*/

        /*CALL TO ADD GIFT*/
        if (userBO != null && streamBO != null && streamBO.getUser_details() != null) {
            sendGiftCountNetworkCall(this, userBO.getId(), streamBO.getUser_details().getId(), streamBO.getId(), UserSharedPreference.readUserToken(), giftCount, false);
        }
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
        Log.i(TAG, "sendMessage: " + message);
        JsonElement element = gson.fromJson(message, JsonElement.class);
        PubnubUtils.getPubNubInstance().publish()
                .message(element)
                .channel(chatChannel)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {

                        if (status.isError()) {
                            Toast.makeText(GoLiveStreamActivity.this, "Error on sending message", Toast.LENGTH_SHORT).show();
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
        Log.e(TAG, "onDestroy GO LIVE STREAM ");
        if (!streamPlayStatus) {
            /*KSY STREAMER LIB*/
            if (mMainHandler != null) {
                mMainHandler.removeCallbacksAndMessages(null);
                mMainHandler = null;
            }
            if (mTimer != null) {
                mTimer.cancel();
            }
            if (mStreamer != null) {
                stopStream();
                mStreamer.setOnLogEventListener(null);
                mStreamer.release();

            }
            /*KSY STREAMER LIB*/
        }
        clearPubNub();
        stopPlayer();

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (handlerJoinChat != null) {
            handlerJoinChat.removeCallbacksAndMessages(null);
        }
        if (slideViewHandler != null) {
            slideViewHandler.removeCallbacksAndMessages(null);
        }
    }

    // METHOD TO DESTROY PUBNUB OBJECT
    private void clearPubNub() {
        PubnubUtils.getPubNubInstance().unsubscribeAll();
        PubnubUtils.getPubNubInstance().removeListener(subscribeCallback);
//        PubnubUtils.getPubNubInstance().destroy();
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
        if (fromPaymentPage) {
            if (ksyMediaPlayer != null) {
                if (mediaPlayerOnBack) {
                    ksyMediaPlayer.start();
                    super.onBackPressed();
                }
            }
            fromPaymentPage = false;
        } else if (streamPlayStatus) {
//            if (giftCount != null && giftCount.size() > 0) {
            if (userBO != null && streamBO != null && streamBO.getUser_details() != null) {
                sendGiftCountNetworkCall(this, userBO.getId(), streamBO.getUser_details().getId(), streamBO.getId(), UserSharedPreference.readUserToken(), giftCount, true);
//            }
            }
            stopPlayer();
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
        if (tutorials) {
            chatChannel = "chat-123 Tutorials";
        } else {
            chatChannel = UserSharedPreference.readUserStreamChatKey() + "" + streamBO.getUuid();
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
                                giftPanelUserLevel = jsonObject.getInt("user_gift_level");
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
            Log.e(TAG, "showChatJoinBubbleAnimation " + e.getMessage());
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

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(GoLiveStreamActivity.this);
        sequence.setConfig(config);

        String[] goLiveAppTutorialArray = getResources().getStringArray(R.array.go_live_app_tutorial);
        int textIndex = 0;

        IShowcaseSequenceListener iShowcaseSequenceListener = new IShowcaseSequenceListener() {
            @Override
            public void onShowcaseSkipClicked(MaterialShowcaseView showcaseView) {
                finish();
            }

            @Override
            public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView.DismissedType dismissedType) {
                if (dismissedType == MaterialShowcaseView.DismissedType.SKIP_CLICKED) {
                    showHideWhiteBackground(false); // show button three.
                }
            }

            @Override
            public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                showHideWhiteBackground(true);
            }
        };


        MaterialShowcaseView userProfile = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.UserProfile)
                .setTarget(userProfileView)
                .setRadius(userProfileView.getHeight())
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userProfile.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userProfile);

        MaterialShowcaseView userCoinsCount = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.UserCoinsCount)
                .setTarget(userCoinsCountView)
                .setRadius(userCoinsCountView.getHeight())
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userCoinsCount.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userCoinsCount);

        MaterialShowcaseView followBroadcaster = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.FollowBroadcaster)
                .setTarget(userProfileView)
                .setRadius(userProfileView.getHeight())
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        followBroadcaster.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(followBroadcaster);

        MaterialShowcaseView userViewers = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveList)
                .setTarget(goLiveListParentLL)
                .setRadius(goLiveListParentLL.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userViewers.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userViewers);

        MaterialShowcaseView userEvents = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.EventPoster)
                .setTarget(eventPosterIV)
                .setRadius(eventPosterIV.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userEvents.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userEvents);

        MaterialShowcaseView userMission = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.DailyMissionEnergy)
                .setTarget(dailyMissionEnergyImageView)
                .setRadius(dailyMissionEnergyImageView.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userMission.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userMission);

        MaterialShowcaseView userChatView = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.OldChat)
                .setTarget(oldChatView)
                .setRadius(oldChatView.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userChatView.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userChatView);

        MaterialShowcaseView userChatField = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveChatViewerEDT)
                .setTarget(goLiveChatViewerEDT)
                .setRadius(goLiveChatViewerEDT.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userChatField.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userChatField);

        MaterialShowcaseView userCamera = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatCamera)
                .setTarget(goLiveViewerChatCamera)
                .setRadius(goLiveViewerChatCamera.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userCamera.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userCamera);

        MaterialShowcaseView userChatPost = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatPost)
                .setTarget(goLiveViewerChatPost)
                .setRadius(goLiveViewerChatPost.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userChatPost.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userChatPost);

        MaterialShowcaseView userChatMessage = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatMessage)
                .setTarget(goLiveViewerChatMessage)
                .setRadius(goLiveViewerChatMessage.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userChatMessage.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userChatMessage);

        MaterialShowcaseView userChatFolder = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatFolder)
                .setTarget(goLiveViewerChatFolder)
                .setRadius(goLiveViewerChatFolder.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userChatFolder.addShowcaseListener(iShowcaseSequenceListener);
        sequence.addSequenceItem(userChatFolder);

        MaterialShowcaseView userChatGift = new MaterialShowcaseView.Builder(GoLiveStreamActivity.this)
                .setTutorialViewType(TutorialViewType.GoLiveViewerChatGift)
                .setTarget(goLiveViewerChatGift)
                .setRadius(goLiveViewerChatGift.getHeight() / 2)
                .setUseAutoRadius(false)
                .setContentText(goLiveAppTutorialArray[textIndex++])
                .setDismissOnTouch(true)
                .build();
        userChatGift.addShowcaseListener(new IShowcaseSequenceListener() {
            @Override
            public void onShowcaseSkipClicked(MaterialShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {

            }

            @Override
            public void onShowcaseDismissed(MaterialShowcaseView showcaseView, MaterialShowcaseView.DismissedType dismissedType) {
                finish();
            }
        });
        sequence.addSequenceItem(userChatGift);

        sequence.start();

    }

    private void startTutorials() {
        MaterialShowcaseView.resetSingleUse(GoLiveStreamActivity.this, SHOWCASE_ID);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showTutoirial();
            }
        }, 100);
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
                    Utils.setImageSimpleBlur(userImageBlur, userBO.getProfilePictureUrl(), GoLiveStreamActivity.this);
                } else {
                    Utils.setImageSimpleBlur(userImageBlur, streamBO.getUser_details().profilePictureUrl, GoLiveStreamActivity.this);
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

    private void addAnimationGiftBullet(String giftName, int giftRes, String giftText, double giftPrice, String gifURL, String userDpURL) {
        GiftSendModel giftSendModel = new GiftSendModel(1);
        giftSendModel.setNickname(giftName);
        giftSendModel.setGiftURL(gifURL);
        giftSendModel.setUserImage(userDpURL);
        giftSendModel.setSig(giftText);
        giftBulletView.addGift(giftSendModel);
        updateUserDollars(giftPrice);
    }

    private void startFullAnimation(final int giftID, final String giftName, final double giftPrice, final String
            userName, final String gifURL, String giftCount, final String userDpURL, int giftLevel) {
        if (mContext == null)
            return;
        slideViewCounter = 1;

        try {
            count = 0;
            if (giftCount != null && !giftCount.isEmpty()) {
                try {
                    count = Integer.parseInt(giftCount);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            playGifs(giftLevel + "", giftID + ".gif");
            if (count > 1) {

                slideViewHandler = new Handler();
                slideViewHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addAnimationGiftBullet(userName, EnumUtils.GiftResById.getGiftRes(giftID), giftName, giftPrice, gifURL, userDpURL);

                        if (slideViewCounter < count) {
                            slideViewCounter++;
                            slideViewHandler.postDelayed(this, 500);
                        }
                    }
                }, 10);
            } else {
                addAnimationGiftBullet(userName, EnumUtils.GiftResById.getGiftRes(giftID), giftName, giftPrice, gifURL, userDpURL);
            }
        } catch (Exception e) {
            Log.e(TAG, "startFullAnimation " + e.getMessage());
        }
    }

    /*SHOWER ANIMATION*/
    private void showShowerAnimation() {

        new CountDownTimer(10000, 300) {
            public void onTick(long millisUntilFinished) {
                bubbleViewGoLive.setBubbleDrawable(getResources().getDrawable(EnumUtils.GiftResById.getCandyRes(new Random().nextInt(11))),
                        Utils.dp2px(GoLiveStreamActivity.this, 40), Utils.dp2px(GoLiveStreamActivity.this, 40));

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

    private void manageGiftCount(int giftID, int giftPrice, int giftSendCount) {

        if (giftCount != null && giftCount.size() > 0) { // HAVE VALUE

            if (giftCount.containsKey(giftID)) { // ALREADY HAVE
                giftCount.put(giftID, new GiftItemBO(giftID, giftCount.get(giftID).getItemCount() + giftSendCount, giftPrice));
            } else {// ADD FIRST TIME
                giftCount.put(giftID, new GiftItemBO(giftID, giftSendCount, giftPrice));
            }
        } else { // ADD FIRST TIME
            giftCount.put(giftID, new GiftItemBO(giftID, giftSendCount, giftPrice));
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
                Log.e(TAG, "getGiftCountJso " + e.getMessage());
            }
        }
        return new JSONArray(jsonObj).toString();
    }

    private void playGifs(String levelNumber, String gifName) {
        try {
//            File file = Utils.getFileFromAssets(this, gifName); /*FROM ASSETS*/
            /*File file = new File(Environment.getExternalStorageDirectory(), Constants.WITKEY_GIFTS_FOLDER_PATH + File.separator + levelNumber + File.separator + gifName); *//*FROM EXTERNAL MEMORY*//*
            if (file != null && file.exists()) {

                gifImageView.setVisibility(View.VISIBLE);

//                gifImageView.setImageURI(Uri.parse("file:///" + Utils.getFileFromAssets(this, gifName)));*//*FROM ASSETS*//*
                gifImageView.setImageURI(Uri.fromFile(file));*//*FROM EXTERNAL MEMORY*//*
                gifDrawable = (GifDrawable) gifImageView.getDrawable();*//*FROM ASSETS*//*
                gifDrawable.addAnimationListener(this);
                if (!gifDrawable.isPlaying()) {
                    resetAnimation();
                    gifDrawable.start();
                }
            }*/

            new ReadFromObbAsyncTask(levelNumber, gifName).execute();
        } catch (Exception e) {
            Log.e(TAG, "playGifs " + e.getMessage());
        }
    }

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
    private void getEventPostersNetworkCall(Context context, boolean streamPlayStatus, String streamID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.TYPE, streamPlayStatus ? "0" : "1");
        serviceParams.put(Keys.STREAM_ID, streamID);

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

        new WebServicesVolleyTask(context, true, "",
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

    /*--------------------------------------------------- KSY LIB-------------------------------------------------------------*/

    /*METHOD TO INIT STEAMER OBJECT*/
    private void initStreamerObject(StreamBO streamBO) {
        try {
            mStreamer = new KSYStreamer(this);
            mStreamer = StreamConfig.getStreamerObject(this, streamBO);

            mIsLandscape = false; /*ORIENTATION*/
            mAutoStart = true;/*AUTO START STREAM*/
            mPrintDebugInfo = false;/*SHOW DEBUG INFO*/

            mStreamer.setDisplayPreview(mCameraPreviewView);

            mStreamer.setOnInfoListener(mOnInfoListener);
            mStreamer.setOnErrorListener(mOnErrorListener);
            mStreamer.setOnLogEventListener(mOnLogEventListener);

            mStreamer.getImgTexFilterMgt().setOnErrorListener(new ImgTexFilterBase.OnErrorListener() {
                @Override
                public void onError(ImgTexFilterBase filter, int errno) {
                    Toast.makeText(GoLiveStreamActivity.this, "The current model does not support this filter",
                            Toast.LENGTH_SHORT).show();
                    mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                            ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
                }
            });

            // touch focus and zoom support
            CameraTouchHelper cameraTouchHelper = new CameraTouchHelper();
            cameraTouchHelper.setCameraCapture(mStreamer.getCameraCapture());
            mCameraPreviewView.setOnTouchListener(cameraTouchHelper);
            // set CameraHintView to show focus rect and zoom ratio
            cameraTouchHelper.setCameraHintView(mCameraHintView);

        } catch (Exception e) {
            Log.e(TAG, "initStreamerObject " + e.getMessage());
        }
    }

    /*METHOD TO INIT BEAUTY FILTER SETTING*/
    private void initBeautyUI() {
        String[] items = new String[]{"DISABLE", "BEAUTY_SOFT", "SKIN_WHITEN", "BEAUTY_ILLUSION",
                "BEAUTY_DENOISE", "BEAUTY_SMOOTH", "BEAUTY_PRO", "DEMO_FILTER", "GROUP_FILTER",
                "ToneCurve", "Retro", "film"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBeautySpinner.setAdapter(adapter);
        mBeautySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = ((TextView) parent.getChildAt(0));
                if (textView != null) {
                    textView.setTextColor(getResources().getColor(R.color.black));
                }
                if (position == 0) {
                    mStreamer.getImgTexFilterMgt().setFilter((ImgFilterBase) null);
                } else if (position <= 5) {
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), position + 15);
                } else if (position == 6) {
                    mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                            ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO);
                } else if (position == 7) {
                    mStreamer.getImgTexFilterMgt().setFilter(
                            new DemoFilter(mStreamer.getGLRender()));
                } else if (position == 8) {
                    List<ImgFilterBase> groupFilter = new LinkedList<>();
                    groupFilter.add(new DemoFilter2(mStreamer.getGLRender()));
                    groupFilter.add(new DemoFilter3(mStreamer.getGLRender()));
                    groupFilter.add(new DemoFilter4(mStreamer.getGLRender()));
                    mStreamer.getImgTexFilterMgt().setFilter(groupFilter);
                } else if (position == 9) {
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mStreamer.getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            GoLiveStreamActivity.this.getResources().openRawResource(R.raw.tone_cuver_sample));

                    mStreamer.getImgTexFilterMgt().setFilter(acvFilter);
                } else if (position == 10) {
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mStreamer.getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            GoLiveStreamActivity.this.getResources().openRawResource(R.raw.fugu));

                    mStreamer.getImgTexFilterMgt().setFilter(acvFilter);
                } else if (position == 11) {
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mStreamer.getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            GoLiveStreamActivity.this.getResources().openRawResource(R.raw.jiaopian));

                    mStreamer.getImgTexFilterMgt().setFilter(acvFilter);
                }
                List<ImgFilterBase> filters = mStreamer.getImgTexFilterMgt().getFilter();
                if (filters != null && !filters.isEmpty()) {
                    final ImgFilterBase filter = filters.get(0);
                    mBeautyGrindLayout.setVisibility(filter.isGrindRatioSupported() ?
                            View.VISIBLE : View.GONE);
                    mBeautyWhitenLayout.setVisibility(filter.isWhitenRatioSupported() ?
                            View.VISIBLE : View.GONE);
                    mBeautyRuddyLayout.setVisibility(filter.isRuddyRatioSupported() ?
                            View.VISIBLE : View.GONE);
                    SeekBar.OnSeekBarChangeListener seekBarChangeListener =
                            new SeekBar.OnSeekBarChangeListener() {
                                @Override
                                public void onProgressChanged(SeekBar seekBar, int progress,
                                                              boolean fromUser) {
                                    if (!fromUser) {
                                        return;
                                    }
                                    float val = progress / 100.f;
                                    if (seekBar == mGrindSeekBar) {
                                        filter.setGrindRatio(val);
                                    } else if (seekBar == mWhitenSeekBar) {
                                        filter.setWhitenRatio(val);
                                    } else if (seekBar == mRuddySeekBar) {
                                        if (filter instanceof ImgBeautyProFilter) {
                                            val = progress / 50.f - 1.0f;
                                        }
                                        filter.setRuddyRatio(val);
                                    }
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {
                                }

                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {
                                }
                            };
                    mGrindSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mWhitenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mRuddySeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
                    mGrindSeekBar.setProgress((int) (filter.getGrindRatio() * 100));
                    mWhitenSeekBar.setProgress((int) (filter.getWhitenRatio() * 100));
                    int ruddyVal = (int) (filter.getRuddyRatio() * 100);
                    if (filter instanceof ImgBeautyProFilter) {
                        ruddyVal = (int) (filter.getRuddyRatio() * 50 + 50);
                    }
                    mRuddySeekBar.setProgress(ruddyVal);
                } else {
                    mBeautyGrindLayout.setVisibility(View.GONE);
                    mBeautyWhitenLayout.setVisibility(View.GONE);
                    mBeautyRuddyLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });
        mBeautySpinner.setPopupBackgroundResource(R.color.white);
        mBeautySpinner.setSelection(4);
    }

    /*METHOD TO START LIVE STREAM*/
    private void startStream() {
        try {
            mStreamer.startStream();
            Log.e(TAG, "startStream-> " + mStreamer.getIFrameInterval());
        } catch (Exception e) {
            Log.e(TAG, "startStream " + e.getMessage());
        }
        mRecording = true;
    }

    /*METHOD TO START STREAM RECORDING*/
    private void startRecord() {
        mStreamer.startRecord(mRecordUrl);
        mIsFileRecording = true;
    }

    /*METHOD TO STOP STREAM RECORDING*/
    private void stopRecord() {
        mStreamer.stopRecord();
        mIsFileRecording = false;
    }

    /*METHOD TO STOP LIVE STREAM*/
    private void stopStream() {
        mStreamer.stopStream();
        mRecording = false;
        if (!Constants.BUILD_TYPE_QA) {
            if (streamBO != null && userBO != null) {
                endCurrentStreamNetworkCall(GoLiveStreamActivity.this,
                        streamBO.getId(), userBO.getToken(), (System.currentTimeMillis() - startTime) + "");
            }
        }
    }

    /*METHOD TO SHOW DEBUG INFO*/
    private void beginInfoUploadTimer() {
        if (mPrintDebugInfo && mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    updateDebugInfo();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDebugInfoTextView.setText(mDebugInfo);
                        }
                    });
                }
            }, 100, 1000);
        }
    }

    /*METHOD TO UPDATE DEBUG INFO*/
    private void updateDebugInfo() {
        if (mStreamer == null) return;
        mDebugInfo = String.format(Locale.getDefault(),
                "RtmpHostIP()=%s DroppedFrameCount()=%d \n " +
                        "ConnectTime()=%d DnsParseTime()=%d \n " +
                        "UploadedKB()=%d EncodedFrames()=%d \n" +
                        "CurrentKBitrate=%d Version()=%s",
                mStreamer.getRtmpHostIP(), mStreamer.getDroppedFrameCount(),
                mStreamer.getConnectTime(), mStreamer.getDnsParseTime(),
                mStreamer.getUploadedKBytes(), mStreamer.getEncodedFrames(),
                mStreamer.getCurrentUploadKBitrate(), KSYStreamer.getVersion());
    }

    /*METHOD TO HANDLE CAMERA RELATED OPERATION*/
    private void setCameraAntiBanding50Hz() {
        Camera.Parameters parameters = mStreamer.getCameraCapture().getCameraParameters();
        if (parameters != null) {
            parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
            mStreamer.getCameraCapture().setCameraParameters(parameters);
        }
    }

    /*METHOD TO LISTEN STREAM INFO*/
    private KSYStreamer.OnInfoListener mOnInfoListener = new KSYStreamer.OnInfoListener() {
        @Override
        public void onInfo(int what, int msg1, int msg2) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_INIT_DONE:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_INIT_DONE");
                    setCameraAntiBanding50Hz();
                    updateFaceunitParams();
                    if (mAutoStart) {
                        startStream();
                    }
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_STREAM_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_STREAM_SUCCESS");
                    beginInfoUploadTimer();
                    break;
                case StreamerConstants.KSY_STREAMER_OPEN_FILE_SUCCESS:
                    Log.d(TAG, "KSY_STREAMER_OPEN_FILE_SUCCESS");
                    break;
                case StreamerConstants.KSY_STREAMER_FRAME_SEND_SLOW:
                    Log.d(TAG, "KSY_STREAMER_FRAME_SEND_SLOW " + msg1 + "ms");
//                    Toast.makeText(GoLiveStreamActivity.this, "Network not good!",Toast.LENGTH_SHORT).show();
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_RAISE:
                    Log.d(TAG, "BW raise to " + msg1 / 1000 + "kbps");
                    break;
                case StreamerConstants.KSY_STREAMER_EST_BW_DROP:
                    Log.d(TAG, "BW drop to " + msg1 / 1000 + "kpbs");
                    break;
                default:
                    Log.d(TAG, "OnInfo: " + what + " msg1: " + msg1 + " msg2: " + msg2);
                    break;
            }
        }
    };

    /*METHOD TO HANDLE ENCODE ERRORS*/
    private void handleEncodeError() {
        int encodeMethod = mStreamer.getVideoEncodeMethod();
        if (encodeMethod == StreamerConstants.ENCODE_METHOD_HARDWARE) {
            mHWEncoderUnsupported = true;
            if (mSWEncoderUnsupported) {
                mStreamer.setEncodeMethod(
                        StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE_COMPAT mode");
            } else {
                mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_SOFTWARE);
                Log.e(TAG, "Got HW encoder error, switch to SOFTWARE mode");
            }
        } else if (encodeMethod == StreamerConstants.ENCODE_METHOD_SOFTWARE) {
            mSWEncoderUnsupported = true;
            if (mHWEncoderUnsupported) {
                Log.e(TAG, "Got SW encoder error, can not streamer");
            } else {
                mStreamer.setEncodeMethod(StreamerConstants.ENCODE_METHOD_HARDWARE);
                Log.e(TAG, "Got SW encoder error, switch to HARDWARE mode");
            }
        }
    }

    /*METHOD TO LISTEN STREAM ERROR*/
    private KSYStreamer.OnErrorListener mOnErrorListener = new KSYStreamer.OnErrorListener() {
        @Override
        public void onError(int what, int msg1, int msg2) {
            switch (what) {
                case StreamerConstants.KSY_STREAMER_ERROR_DNS_PARSE_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_DNS_PARSE_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_PUBLISH_FAILED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_PUBLISH_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_CONNECT_BREAKED:
                    Log.d(TAG, "KSY_STREAMER_ERROR_CONNECT_BREAKED");
                    break;
                case StreamerConstants.KSY_STREAMER_ERROR_AV_ASYNC:
                    Log.d(TAG, "KSY_STREAMER_ERROR_AV_ASYNC " + msg1 + "ms");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNSUPPORTED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_ENCODER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_UNKNOWN");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_START_FAILED");
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_SERVER_DIED");
                    break;
                //Camera was disconnected due to use by higher priority user.
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_EVICTED:
                    Log.d(TAG, "KSY_STREAMER_CAMERA_ERROR_EVICTED");
                    break;
                default:
                    Log.d(TAG, "what=" + what + " msg1=" + msg1 + " msg2=" + msg2);
                    break;
            }
            switch (what) {
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_START_FAILED:
                case StreamerConstants.KSY_STREAMER_AUDIO_RECORDER_ERROR_UNKNOWN:
                    break;
                case StreamerConstants.KSY_STREAMER_CAMERA_ERROR_SERVER_DIED:
                    mStreamer.stopCameraPreview();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startCameraPreviewWithPermCheck();
                        }
                    }, 5000);
                    break;
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_CLOSE_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_ERROR_UNKNOWN:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_OPEN_FAILED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_FORMAT_NOT_SUPPORTED:
                case StreamerConstants.KSY_STREAMER_FILE_PUBLISHER_WRITE_FAILED:
                    stopRecord();
                    break;
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNSUPPORTED:
                case StreamerConstants.KSY_STREAMER_VIDEO_ENCODER_ERROR_UNKNOWN: {
                    handleEncodeError();
                    stopStream();
                    mMainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startStream();
                        }
                    }, 3000);
                }
                break;
                default:
                    if (mStreamer.getEnableAutoRestart()) {
                        mRecording = false;
                    } else {
                        stopStream();
                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startStream();
                            }
                        }, 3000);
                    }
                    break;
            }
        }
    };

    /*METHOD TO LOG ERROR*/
    private StatsLogReport.OnLogEventListener mOnLogEventListener =
            new StatsLogReport.OnLogEventListener() {
                @Override
                public void onLogEvent(StringBuilder singleLogContent) {
                    Log.i(TAG, "***onLogEvent : " + singleLogContent.toString());
                }
            };


    /*SWITCH CAMERA*/
    private void onSwitchCamera() {
        if (mStreamer != null) {

            mStreamer.switchCamera();
            mCameraHintView.hideAll();

            boolean isFrontCamera = !mStreamer.isFrontCamera();
            if (isFrontCamera) {
                mImgFaceunityFilter.setMirror(true);
            } else {
                mImgFaceunityFilter.setMirror(false);
            }

            /*if (isFrontCamera) {
                cameraMessages.setText(getString(R.string.what_viewers_see_is_the_opposite_of_what_you_see_now));
                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
            } else {
                cameraMessages.setText(getString(R.string.what_viewers_see_is_the_same_as_what_you_see_now));
                cameraMessagesParentFrame.setVisibility(View.VISIBLE);
            }*/
        }
    }

    /*STREAM START AND STOP*/
    private void onShootClick() {
        if (mRecording) {
            stopStream();
        } else {
            startStream();
        }// FOR TESTING
    }

    /*STREAM RECORDING*/
    private void onRecordClick() {
        if (mIsFileRecording) {
            stopRecord();
        } else {
            startRecord();
        }
    }

    /*BEAUTY FILTERS*/
    private void onBeautyChecked(boolean isChecked) {
        if (isChecked) {
            if (mStreamer.getVideoEncodeMethod() == StreamerConstants.ENCODE_METHOD_SOFTWARE_COMPAT) {
                mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(), isChecked ?
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DENOISE :
                        ImgTexFilterMgt.KSY_FILTER_BEAUTY_DISABLE);
                mStreamer.setEnableImgBufBeauty(isChecked);
                showHideFiltersListView(true, 1);
            } else {
                showHideFiltersListView(true, 1);
//            mBeautyChooseView.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
            }
        } else {
            mStreamer.getImgTexFilterMgt().setFilter((ImgFilterBase) null);
            showHideFiltersListView(false, 0);
        }
    }

    /*STREAM AUDIO SETTING*/
    private void onMuteChecked(boolean isChecked) {
        mStreamer.setMuteAudio(isChecked);
    }

    /*SCREEN SHOT*/
    private void onCaptureScreenShotClick() {

        /*goLiveBottomOpt4.setEnabled(false);
        goLiveBottomOpt4.setClickable(false);*/

        mStreamer.requestScreenShot(new GLRender.ScreenShotListener() {

            @Override
            public void onBitmapAvailable(Bitmap bitmap) {

                try {
                    screenshotBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            showBitmap();
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "onCaptureScreenShotClick " + e.getMessage());
                }
                /*BufferedOutputStream bos = null;
                try {
                    Date date = new Date();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                    final String filename = "/sdcard/screenshot" + dateFormat.format(date) + ".jpg";

                    bos = new BufferedOutputStream(new FileOutputStream(filename));
                    if (screenshotBitmap != null) {
                        screenshotBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos);

                        runOnUiThread(new Runnable() {
                            public void run() {
                                showBitmap();
                                Toast.makeText(GoLiveStreamActivity.this, "Save the screenshot to " + filename, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    if (bos != null) try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });

    }

    /*METHOD TO  CHECK CAMERA PERMISSION*/

    private void startCameraPreviewWithPermCheck() {
        int cameraPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int audioPerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int storagePerm = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (cameraPerm != PackageManager.PERMISSION_GRANTED ||
                storagePerm != PackageManager.PERMISSION_GRANTED ||
                audioPerm != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Log.e(TAG, "No CAMERA or AudioRecord permission, please check");
                Toast.makeText(this, "No CAMERA or AudioRecord permission, please check",
                        Toast.LENGTH_LONG).show();
            } else {
                String[] permissions = {Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions,
                        PERMISSION_REQUEST_CAMERA_AUDIOREC);
            }
        } else {
            if (mStreamer != null) {
                mStreamer.startCameraPreview();
            }
        }
    }

    private void storagePermissionCheck() {

        MarshMallowPermission permission = new MarshMallowPermission(this);
        if (permission.checkPermissionForReadStorage()) {
            /*DO NOTHING*/
        } else {
            permission.requestPermissionForReadStorage(REQUEST_TO_READ_STORAGE_PERMISSION, this);
        }
    }


    /*STICKERS*/
    private void onFaceunityPropCheck(boolean isCheck) {
        initFaceunity();

        if (isCheck) {
            mStreamer.getCameraCapture().mImgBufSrcPin.connect(mImgFaceunityFilter.getBufSinkPin());
            showHideFiltersListView(true, 2);
            //showFaceunityPropChoose();
        } else {
            showHideFiltersListView(false, 0);
            mImgFaceunityFilter.setPropType(-1);
        }
    }

    /*METHOD TO SET STREAM STICKERS*/
    private void showFaceunityPropChoose() {
        initFaceunity();

        android.app.AlertDialog alertDialog;
        alertDialog = new android.app.AlertDialog.Builder(this)
                .setTitle("Please select the sticker")
                .setSingleChoiceItems(
                        new String[]{"BEAGLEDOG", "COLORCROWN", "DEER",
                                "HAPPYRABBI", "HARTSHORN", "ITEM0204", "ITEM0208",
                                "ITEM0210", "ITEM0501", "MOOD", "PRINCESSCROWN", "TIARA", "YELLOWEAR"}, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mImgFaceunityFilter.setPropType(which);
                                dialog.dismiss();
                            }
                        })
                .create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    /*METHOD TO INIT BEAUTY FILTER*/
    private void initFaceunity() {
        if (mImgFaceunityFilter == null) {
            //add faceunity filter
            mImgFaceunityFilter = new ImgFaceunityFilter(this, mStreamer.getGLRender());
            mStreamer.getImgTexFilterMgt().setExtraFilter(mImgFaceunityFilter);
        }

        updateFaceunitParams();
    }

    /*METHOD TO UPDATE BEAUTY FILTER*/
    private void updateFaceunitParams() {
        mImgFaceunityFilter.setTargetSize(mStreamer.getTargetWidth(),
                mStreamer.getTargetHeight());

        if (mStreamer.isFrontCamera()) {
            mImgFaceunityFilter.setMirror(true);
        } else {
            mImgFaceunityFilter.setMirror(false);
        }
    }


    private void setUpFilterImagesRecycler(final List<FilterBO> filterBOList) {
        filterImagesAdapter = null;
        if (filterBOList != null) {
            if (filterBOList.size() > 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                recyclerViewFilterImages.setItemAnimator(new DefaultItemAnimator());
                recyclerViewFilterImages.setLayoutManager(linearLayoutManager);
                filterImagesAdapter = new FilterImagesAdapter(filterBOList, this, recyclerViewFilterImages);
                recyclerViewFilterImages.setAdapter(filterImagesAdapter);
                filterImagesAdapter.setClickListener(new FilterImagesAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        if (position > filterBOList.size()) return;

                        filterListView.setVisibility(View.GONE);
                        viewTopLayer.setOnClickListener(null);

                        if (filterBOList.get(position).getType() == 1) { /*BEAUY*/
                            setBeautyFilter(position, true);
                        } else if (filterBOList.get(position).getType() == 2) {/*STICKERS*/
                            setBeautyFilter(position, false);
                        }
                    }

                    @Override
                    public boolean onRowLongPressClick(int position) {
                        return false;
                    }
                });
            }
        } else {
            //showNoResult(true);
        }
    }

    /*METHOD TO GET FILTER LIST*/
    private List<FilterBO> getFiltersList(boolean typeBeauty) { /*1 FOR BEAUTY, 2 FOR STICKERS*/
        List<FilterBO> filterBOList = new ArrayList<>();

        if (typeBeauty) {

            filterBOList.add(new FilterBO("BEAUTY_SOFT", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("SKIN_WHITEN", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("BEAUTY_ILLUSION", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("BEAUTY_DENOISE", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("BEAUTY_SMOOTH", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("BEAUTY_PRO", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("DEMO_FILTER", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("GROUP_FILTER", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("ToneCurve", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("Retro", R.drawable.place_holder_videos, 1));
            filterBOList.add(new FilterBO("film", R.drawable.place_holder_videos, 1));
        } else {

            filterBOList.add(new FilterBO("TIARA", R.drawable.place_holder_videos, 2));
            filterBOList.add(new FilterBO("YELLOWEAR", R.drawable.place_holder_videos, 2));
            filterBOList.add(new FilterBO("PRINCESSCROWN", R.drawable.place_holder_videos, 2));
            filterBOList.add(new FilterBO("MOOD", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("BEAGLEDOG", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("COLORCROWN", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("DEER", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("HAPPYRABBI", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("HARTSHORN", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("ITEM0204", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("ITEM0208", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("ITEM0210", R.drawable.place_holder_videos, 2));
//            filterBOList.add(new FilterBO("ITEM0501", R.drawable.place_holder_videos, 2));

        }

        return filterBOList;
    }

    private void setBeautyFilter(int filterNumber, boolean beautyFilter) {

        if (beautyFilter) {

            /*KSY_FILTER_BEAUTY_DISABLE = 0;
            KSY_FILTER_BEAUTY_SOFT = 16;
            KSY_FILTER_BEAUTY_SKINWHITEN = 17;
            KSY_FILTER_BEAUTY_ILLUSION = 18;
            KSY_FILTER_BEAUTY_DENOISE = 19;
            KSY_FILTER_BEAUTY_SMOOTH = 20;
            KSY_FILTER_BEAUTY_SOFT_EXT = 21;
            KSY_FILTER_BEAUTY_SOFT_SHARPEN = 22;
            KSY_FILTER_BEAUTY_PRO = 23;*/


            switch (filterNumber) {
                case 0:
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_SOFT);
                    break;
                case 1:
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), 18);
                    break;
                case 2:
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), 20);
                    break;
                case 3:
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), 22);
                    break;
                case 4:
                    List<ImgFilterBase> groupFilter = new LinkedList<>();
                    groupFilter.add(new DemoFilter2(mStreamer.getGLRender()));
                    groupFilter.add(new DemoFilter3(mStreamer.getGLRender()));
                    mStreamer.getImgTexFilterMgt().setFilter(groupFilter);
                    break;
                case 5:
                    mStreamer.getImgTexFilterMgt().setFilter(
                            mStreamer.getGLRender(), ImgTexFilterMgt.KSY_FILTER_BEAUTY_SKINWHITEN);
                    break;
                case 6:
                    mStreamer.getImgTexFilterMgt().setFilter(mStreamer.getGLRender(),
                            ImgTexFilterMgt.KSY_FILTER_BEAUTY_PRO);
                    break;
                case 7:
                    mStreamer.getImgTexFilterMgt().setFilter(
                            new DemoFilter(mStreamer.getGLRender()));
                    break;
                case 8:
                    List<ImgFilterBase> groupFilter1 = new LinkedList<>();
                    groupFilter1.add(new DemoFilter2(mStreamer.getGLRender()));
                    groupFilter1.add(new DemoFilter3(mStreamer.getGLRender()));
                    groupFilter1.add(new DemoFilter4(mStreamer.getGLRender()));
                    mStreamer.getImgTexFilterMgt().setFilter(groupFilter1);
                    break;
                case 9:
                    ImgBeautyToneCurveFilter acvFilter = new ImgBeautyToneCurveFilter(mStreamer.getGLRender());
                    acvFilter.setFromCurveFileInputStream(
                            GoLiveStreamActivity.this.getResources().openRawResource(R.raw.tone_cuver_sample));

                    mStreamer.getImgTexFilterMgt().setFilter(acvFilter);
                    break;
                case 10:
                    ImgBeautyToneCurveFilter acvFilter1 = new ImgBeautyToneCurveFilter(mStreamer.getGLRender());
                    acvFilter1.setFromCurveFileInputStream(
                            GoLiveStreamActivity.this.getResources().openRawResource(R.raw.fugu));

                    mStreamer.getImgTexFilterMgt().setFilter(acvFilter1);
                    break;
                case 11:
                    ImgBeautyToneCurveFilter acvFilter2 = new ImgBeautyToneCurveFilter(mStreamer.getGLRender());
                    acvFilter2.setFromCurveFileInputStream(
                            GoLiveStreamActivity.this.getResources().openRawResource(R.raw.jiaopian));

                    mStreamer.getImgTexFilterMgt().setFilter(acvFilter2);
                    break;
                default:
                    mStreamer.getImgTexFilterMgt().setFilter((ImgFilterBase) null);
                    break;
            }
            setEmojiFilterToggle(false, true);
        } else {
            mImgFaceunityFilter.setPropType(filterNumber);
            setEmojiFilterToggle(true, true);
        }
    }

    private void setEmojiFilterToggle(boolean typeEmoji, boolean show) {
        if (typeEmoji && show) {
            emojiToggle.setVisibility(View.VISIBLE);
            emojiToggle.setText(R.string.emoji_off);
        } else if (typeEmoji && (!show)) {
            emojiToggle.setVisibility(View.GONE);
            emojiToggle.setText(R.string.emoji_on);
            onFaceunityPropCheck(false);
        } else if (!typeEmoji && show) {
            filterToggle.setVisibility(View.VISIBLE);
            filterToggle.setText(R.string.filter_off);
        } else if (!typeEmoji && (!show)) {
            filterToggle.setVisibility(View.GONE);
            filterToggle.setText(R.string.filter_on);
            onBeautyChecked(false);
        }
    }

    /*--------------------------------------------------- KSY LIB-------------------------------------------------------------*/

    private void updateUserDollars(double giftPrice) {
        try {
            double userDollar = 0;
            if (giftPrice > 0) {

                userDollar = (giftPrice - ((giftPrice * 20) / 100));
//                DecimalFormat formater = new DecimalFormat("#.##");

                try {
                    userDollar = userDollar + Integer.parseInt(userCashTextView.getText().toString());
                } catch (Exception e) {
                    e.getStackTrace();
                }
//                userCashTextView.setText(Utils.getShortString((formater.format(userDollar) + ""), 9));
                userCashTextView.setText(Utils.getShortString(((int) Math.ceil(userDollar) + ""), 9));
            } else {
                userCashTextView.setText(Utils.getShortString(((int) Math.ceil(userDollar) + ""), 9));
            }
        } catch (Exception e) {
            Log.e("updateUserDollars", "" + e.getMessage());
        }
    }

    private void switchVideoResulotion() {
        if (mStreamer != null && (!hdQualityOn)) {
            mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_720P);
            mStreamer.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_720P);
//            mStreamer.startCameraPreview();
        } else if (mStreamer != null && (hdQualityOn)) {
            mStreamer.setPreviewResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
            mStreamer.setTargetResolution(StreamerConstants.VIDEO_RESOLUTION_480P);
//            mStreamer.startCameraPreview();
        }
    }

    private void initPlayer(StreamBO streamBO) {
        ksyMediaPlayer = new KSYMediaPlayer.Builder(this.getApplicationContext()).build();
//        ksyMediaPlayer.setDecodeMode(KSYMediaPlayer.KSYDecodeMode.KSY_DECODE_MODE_SOFTWARE);
        ksyMediaPlayer.setOnCompletionListener(mOnCompletionListener);
        ksyMediaPlayer.setOnPreparedListener(mOnPreparedListener);
        ksyMediaPlayer.setOnErrorListener(mOnErrorListenerPlayer);

//        ksyMediaPlayer.setBufferTimeMax(0.4f);
//        ksyMediaPlayer.setBufferSize(2);

//        ksyMediaPlayer.enbaleFastPlayMode(true);/*NEW*/

        mSurfaceHolder = mVideoSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceCallback);

        try {
            // ksyMediaPlayer.setDataSource("rtmp://live.hkstv.hk.lxdns.com/live/hks");
            //rtmp://18.217.153.224:1935/liveedge/witkey-live-fina
            String streamVideoURL = StreamUtils.getVideoUrl(streamBO);
            Log.e(TAG, "Media Player Streaming URL " + streamVideoURL);
            ksyMediaPlayer.setDataSource(streamVideoURL);
            ksyMediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlayer() {
        if (ksyMediaPlayer != null) {
            ksyMediaPlayer.stop();
            ksyMediaPlayer.release();
            ksyMediaPlayer = null;
        }
    }

    private void removeFromLiveStatus(ConversationBO conversationBO) {
        try {
            for (ConversationBO conversationBO1 : onlineConversationBOs) {
                if (conversationBO1.getSenderId().equals(conversationBO.getSenderId())) {
                    onlineConversationBOs.remove(conversationBO1);
                    break;
                }
            }
            setUpFeaturedRecycler(new ArrayList<ConversationBO>(onlineConversationBOs));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveCapturedBitmap() {
        if (screenshotBitmap != null) {/*SAVE HERE PERMISSION*/
            ScreenShotUtils.SaveScreenShot(screenshotBitmap, this);
            screenShotView.setVisibility(View.GONE);
        } else {
            Utils.showToast(GoLiveStreamActivity.this, getString(R.string.nothing_to_save));
            screenShotView.setVisibility(View.GONE);
        }
    }

    private class ReadFromObbAsyncTask extends AsyncTask<String, Void, String> {

        String levelNumber = "", gifName = "";

        public ReadFromObbAsyncTask(String levelNumber_, String gifName_) {
            this.levelNumber = levelNumber_;
            this.gifName = gifName_;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            Utils.showToast(GoLiveStreamActivity.this, "" + getString(R.string.text_reading_start));
//                mStatusText.setText(R.string.text_reading_start);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                // Get a ZipResourceFile representing a merger of both the main and patch files
                ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(GoLiveStreamActivity.this, Config.EXPANSION_MAIN_VERSION, 0);

                if (expansionFile != null) {
                    // Get an input stream for a known file inside the expansion file ZIPs
                    InputStream fileStream = expansionFile.getInputStream(Config.EX_MAIN_FOLDER_NAME + "/" + gifName);//Config.EX_MAIN_FILE_NAME);
                    if (fileStream != null) {
//                        return getFilePath(GoLiveStreamActivity.this, Config.EX_MAIN_FILE_NAME, Config.EX_MAIN_FILE_EXTN, fileStream);
                        return getFilePath(GoLiveStreamActivity.this, gifName, Config.EX_MAIN_FILE_EXTN, fileStream);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path); /*TODO TRY CATCH HERE */
            if (!TextUtils.isEmpty(path)) {
                File file = new File(path);
                if (file.exists()) {
                    playGifs(levelNumber, gifName, file);
//                    Utils.showToast(GoLiveStreamActivity.this, "OBB-> " + file.getAbsolutePath());
                } else {
                    File file_ = new File(Environment.getExternalStorageDirectory(), Constants.WITKEY_GIFTS_FOLDER_PATH + File.separator + levelNumber + File.separator + gifName);
//                    Utils.showToast(GoLiveStreamActivity.this, "FILES-> " + file_.getAbsolutePath());
                    playGifs(levelNumber, gifName, file_);
                }
            } else {
                File file_ = new File(Environment.getExternalStorageDirectory(), Constants.WITKEY_GIFTS_FOLDER_PATH + File.separator + levelNumber + File.separator + gifName);
//                Utils.showToast(GoLiveStreamActivity.this, "FILES-> " + file_.getAbsolutePath());
                playGifs(levelNumber, gifName, file_);
            }
        }

        private void playGifs(String levelNumber, String gifName, File file) {
//            File file = new File(Environment.getExternalStorageDirectory(), Constants.WITKEY_GIFTS_FOLDER_PATH + File.separator + levelNumber + File.separator + gifName); *//*FROM EXTERNAL MEMORY*//*
            try {
                if (file != null && file.exists()) {

                    gifImageView.setVisibility(View.VISIBLE);

//                gifImageView.setImageURI(Uri.parse("file:///" + Utils.getFileFromAssets(this, gifName)));*//*FROM ASSETS*//*
                    gifImageView.setImageURI(Uri.fromFile(file));/*FROM EXTERNAL MEMORY*/
                    gifDrawable = (GifDrawable) gifImageView.getDrawable();/*FROM ASSETS*/
                    gifDrawable.addAnimationListener(GoLiveStreamActivity.this);
                    if (!gifDrawable.isPlaying()) {
                        resetAnimation();
                        gifDrawable.start();
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "playGifs " + e.getMessage());
            }
        }
    }

    /*Create a path where we will place our private file on external
    storage.*/
    public String getFilePath(Context context, String name,
                              String extension, InputStream is) throws Exception {
        File file = new File(context.getExternalFilesDir(null), name); //+ "." + extension);
        OutputStream os = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        os.write(data);
        is.close();
        os.close();
        return file.getPath();
    }


}
