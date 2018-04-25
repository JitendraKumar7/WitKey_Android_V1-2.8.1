package app.witkey.live.activities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Messenger;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.vending.expansion.zipfile.APKExpansionSupport;
import com.android.vending.expansion.zipfile.ZipResourceFile;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.zip.CRC32;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.expansion.Config;
import app.witkey.live.activities.expansion.SampleDownloaderService;
import app.witkey.live.activities.expansion.XAPKFile;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.AnimationListener;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.R.attr.data;

public class SplashActivity extends AppCompatActivity implements AnimationListener, View.OnClickListener, IDownloaderClient {

    @BindView(R.id.imageViewSplash)
    ImageView imageViewSplash;
    @BindView(R.id.gifImageView)
    GifImageView gifImageView;

    boolean loopSplash = true;
    MediaPlayer audio;
    String TAG = "JKS";
    String app_version = "NONE";
    GifDrawable gifDrawable;
    AppSettingsBO appSettingsBO;
    Dialog dialogForceUpdate;
    String actionType = "", jsonData = "";

    private ProgressBar mPB;
    private TextView mStatusText;
    private ImageView mImage;
    private int mState;
    private IStub mDownloaderClientStub;
    static private final float SMOOTHING_FACTOR = 0.005f;
    private boolean mCancelValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        imageViewSplash.setVisibility(View.GONE);

        try {

            //mDownloaderClientStub = DownloaderClientMarshaller.CreateStub
            // (this, SampleDownloaderService.class);
//            setUpViews();
//            readXAPKZipFiles();/*TEST*/

            /**
             * Before we do anything, are the files we expect already here and
             * delivered (presumably by Market)
             */
            /*if (!expansionFilesDelivered()) {
                try {
                    Intent launchIntent = SplashActivity.this.getIntent();
                    Intent intentToLaunchThisActivityFromNotification = new Intent(
                            SplashActivity.this, SplashActivity.this.getClass());
                    intentToLaunchThisActivityFromNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentToLaunchThisActivityFromNotification.setAction(launchIntent.getAction());

                    if (launchIntent.getCategories() != null) {
                        for (String category : launchIntent.getCategories()) {
                            intentToLaunchThisActivityFromNotification.addCategory(category);
                        }
                    }
                    // Build PendingIntent used to open this activity from
                    // Notification
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            SplashActivity.this,
                            0, intentToLaunchThisActivityFromNotification,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    // Request to start the download
                    DownloaderClientMarshaller.startDownloadServiceIfRequired(this,
                            pendingIntent, SampleDownloaderService.class);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }*/ /*else {////////////////////////////////////////////////////////////////////////////////////////////
                validateXAPKZipFiles();
            }*/

            if (getIntent() != null && getIntent().getExtras() != null) {
                actionType = getIntent().getExtras().getString("TYPE");
                jsonData = getIntent().getExtras().getString("JSON_DATA");
            }

            /*if (getIntent() != null) {

                String type = getIntent().getStringExtra("TYPE");
                LogUtils.e("getIntent", "" + type);
            }*/

            if (Constants.BUILD_TYPE_QA || Constants.HIDE_SPLASH) {
                loopSplash = false;
                proceed();
            } else {
                playGifs();
//            playAudio();

                if (Utils.hasInternetConnection(this)) {
                    String userID = "0";
                    UserBO userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                    if (userBO != null) {
                        userID = userBO.getId();
                    }
                    userAppSettingsNetworkCall(this, userID);
                    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2
                } else {
                    loopSplash = false;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "" + e.getMessage());
        }
//        LogUtils.e("DEVICE GCM TOKEN", "" + UserSharedPreference.readRegToken());
//        Utils.getProjectHashKey(this);
    }

    private void proceed() {

        try {
            //loopSplash = true;/*TODO FOR TESTING*/
            if (!loopSplash) {
                /*if (audio != null && audio.isPlaying()) {
                    audio.stop();
                }*/

                if (UserSharedPreference.readIsUserLoggedIn()) {
                    // IF USER ALREADY LOGGED IN THEN OPEN DASHBOARD
                    if (!TextUtils.isEmpty(actionType) && !TextUtils.isEmpty(jsonData)) {
                        startActivity(new Intent(SplashActivity.this, Dashboard.class)
                                .putExtra("TYPE", actionType)
                                .putExtra("JSON_DATA", jsonData));
                    } else {
                        startActivity(new Intent(SplashActivity.this, Dashboard.class));
                    }
                } else { // IF USER NOT ALREADY LOGGED IN THEN OPEN LOGIN
                    startActivity(new Intent(SplashActivity.this, LoginOptionsActivity.class));
                }
                finish();
            } else {
//                readXAPKZipFiles();
                playGifs();
            }
        } catch (Exception e) {
            Log.e(TAG, "proceed " + e.getMessage());
        }
    }

    // METHOD FOR USER APP SETTINGS NETWORK CALL
    // Service Name: app-setting/{user id(optional)}
    // Type: POST
    // params: device_name, device_id, device_type

    private void userAppSettingsNetworkCall(final Context context, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            app_version = pInfo.versionName;
        } catch (Exception e) {
            Log.e(TAG, "userAppSettingsNetworkCall " + e.getMessage());
        }

//$request->user_id,
//        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.DEVICE_NAME, Build.MANUFACTURER);
        serviceParams.put(Keys.DEVICE_USER_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        serviceParams.put(Keys.DEVICE_TYPE, "2"); // (1 for IOS, 2 for andriod)
        serviceParams.put(Keys.DEVICE_OS_VERSION, Build.VERSION.RELEASE);
        serviceParams.put(Keys.DEVICE_RESOLUTION, Utils.getScreenWidth(context) + " x " + Utils.getScreenHeight(context));
//        serviceParams.put(Keys.APP_VERSION, app_version);
        serviceParams.put(Keys.DEVICE_REGISTERATION_ID, UserSharedPreference.readRegToken());
        if (UserSharedPreference.readIsUserLoggedIn()) {
            try {
                UserBO userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                if (userBO != null) {
                    serviceParams.put(Keys.USER_ID, userBO.getId());
                }
            } catch (Exception e) {
                Log.e(TAG, "userAppSettingsNetworkCall " + e.getMessage());
            }
        }

        /*IF LOGIN THEN SEND GCM OKEN IN THIS*/
        if (UserSharedPreference.readIsUserLoggedIn()) {
            /*THEN SEND USER ID AND DEVICE REGISTRATION ID*/
        }

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.app_setting,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        // DO SOME THING HERE
                        if (ObjectSharedPreference.getObject(Keys.APP_SETTINGS_OBJECT) != null) {
                            loopSplash = false;
                        } else {
                            AlertOP.showAlert(context, null, getString(R.string.an_error));
                        }

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                Gson gson = new Gson();
                                appSettingsBO = gson.fromJson(jsonObject.get("app-setting").toString(), AppSettingsBO.class);
                                ObjectSharedPreference.saveObject(appSettingsBO, Keys.APP_SETTINGS_OBJECT);
                                UserSharedPreference.saveUserWowzaKey(appSettingsBO.getWowza_android_app_license_key());
                                UserSharedPreference.saveUserPubNubKey(appSettingsBO.getPubnub_publish_key());
                                UserSharedPreference.saveUserPubNubSubKey(appSettingsBO.getPubnub_subs_key());
                                UserSharedPreference.saveUserPaymentKey(appSettingsBO.getPayment_api_key());
                                UserSharedPreference.saveUserPrivateChatKey(appSettingsBO.getChat_signature());
                                UserSharedPreference.saveUserStreamChatKey(appSettingsBO.getStrea_group_chat());
//                                UserSharedPreference.saveUserCurrentGiftVersion(appSettingsBO.getGift_version());

                                if (appSettingsBO != null) {

                                    if (app_version.equals(appSettingsBO.getAndroid_version())) {/*! FOR TESTING TO BE REMOVE*/
                                        goToNextActivity();
                                    } else {
                                        if (appSettingsBO.getIs_force_update() == 1) {/*0 FOR TESTING TO BE REMOVE*/
                                            cleanUserData();
                                            showForceUpdatePopup(true);
                                        } else {
                                            showForceUpdatePopup(false);
                                        }
                                    }
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

    private void cleanUserData() {
        UserSharedPreference.saveIsUserLoggedIn(false);
        UserSharedPreference.saveUserFirstVisit(false);
        UserSharedPreference.saveUserToken("");
        ObjectSharedPreference.saveObject(new UserBO(), Keys.USER_OBJECT);
    }

    /*private void playAudio() {
        try {
            audio = MediaPlayer.create(this, R.raw.splash);
            audio.start();
        } catch (Exception e) {
            LogUtils.e("playAudio", "" + e.getMessage());
        }
    }*/

    private void playGifs() {
        try { ///storage/emulated/0/Android/data/app.witkey.live/files/6.gif.obb
            String gifName = "splash.gif";
            File file = Utils.getFileFromAssets(this, gifName);
            if (file != null && file.exists()) {
                gifImageView.setImageURI(Uri.parse("file:///" + Utils.getFileFromAssets(this, gifName)));
                gifDrawable = (GifDrawable) gifImageView.getDrawable();
                gifDrawable.addAnimationListener(this);
                if (!gifDrawable.isPlaying()) {
                    resetAnimation();
                    gifDrawable.start();
                }
            }
        } catch (Exception e) {
            /*HANDEL HERE*/
//            gifImageView.setVisibility(View.GONE);
//            imageViewSplash.setVisibility(View.VISIBLE);
            Log.e(TAG, "playGifs" + e.getMessage());
        }
    }


    private void resetAnimation() {
        gifDrawable.stop();
        gifDrawable.setLoopCount(1);
    }

    @Override
    public void onAnimationCompleted(int loopNumber) {
        proceed();

        LogUtils.e(TAG, " on Animation Completed " + loopNumber);
    }

    private void showForceUpdatePopup(final boolean forceUpdateStatus) {

        dialogForceUpdate = new Dialog(this);
        dialogForceUpdate.setContentView(R.layout.popup_force_update);
        dialogForceUpdate.setCancelable(!forceUpdateStatus);
        dialogForceUpdate.setCanceledOnTouchOutside(!forceUpdateStatus);
        if (dialogForceUpdate.getWindow() != null) {
            dialogForceUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        Button btnupdate = (Button) dialogForceUpdate.findViewById(R.id.btnupdate);
        CustomTextView textTV = (CustomTextView) dialogForceUpdate.findViewById(R.id.textTV);
        btnupdate.setOnClickListener(this);

        dialogForceUpdate.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!forceUpdateStatus) {
                    goToNextActivity();
                }
            }
        });

        dialogForceUpdate.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (!forceUpdateStatus) {
                    goToNextActivity();
                }
            }
        });

        if (forceUpdateStatus) {
            btnupdate.setText(R.string.to_the_play_store);
            textTV.setText(getString(R.string.witkey_version_update_force));
        } else {
            btnupdate.setText(getString(R.string.update_));
            textTV.setText(getString(R.string.witkey_version_update));
        }

        if (dialogForceUpdate != null)
            dialogForceUpdate.show();

        /*ForceUpdatePopup forceUpdatePopup = new ForceUpdatePopup(this, forceUpdateStatus);
        forceUpdatePopup.setCancelable(!forceUpdateStatus);
        forceUpdatePopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        forceUpdatePopup.show();*/
    }

    private void goToNextActivity() {
        if (appSettingsBO.getIs_live().equals(Constants.IS_LIVE_TRUE)) {
//          ObjectSharedPreference.saveObject(appSettingsBO, Keys.APP_SETTINGS_OBJECT);
            if (appSettingsBO.getApp_allow().equals(Constants.IS_ALLOW_APP_TRUE)) {
                loopSplash = false;
            } else {
                cleanUserData();
                //startActivity(new Intent(SplashActivity.this, LoginOptionsActivity.class));
//                                            finish();
                loopSplash = false;
            }
        } else {
            cleanUserData();
            loopSplash = false;
            // SHOW COUNT DOWN TIMER HERE
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnupdate:
                Utils.openPlaystore(SplashActivity.this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*APK EXPANSION*/
    private void setUpViews() {
        mPB = (ProgressBar) findViewById(R.id.progressBar);
        mStatusText = (TextView) findViewById(R.id.statusText);
        mImage = (ImageView) findViewById(R.id.image);
    }

    /**
     * Go through each of the APK Expansion files defined in the structure above
     * and determine if the files are present and match the required size.
     *
     * @return true if they are present.
     */
    boolean expansionFilesDelivered() {
        for (XAPKFile xf : Config.xAPKS) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain, xf.mFileVersion);
            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false))
                return false;
        }
        return true;
    }

    /**
     * Connect the stub to our service on start.
     */
    @Override
    protected void onStart() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.connect(this);
        }
        super.onStart();
    }

    /**
     * Disconnect the stub from our service on stop
     */
    @Override
    protected void onStop() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.disconnect(this);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        this.mCancelValidation = true;
        super.onDestroy();
    }

    /**
     * Critical implementation detail. In onServiceConnected we create the
     * remote service and marshaler. This is how we pass the client information
     * back to the service so the client can be properly notified of changes. We
     * must do this every time we reconnect to the service.
     */
    @Override
    public void onServiceConnected(Messenger m) {
        //IDownloaderService mRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
        //mRemoteService.onClientUpdated(mDownloaderClientStub.getMessenger());
    }
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * The download state should trigger changes in the UI --- it may be useful
     * to show the state as being indeterminate at times. This sample can be
     * considered a guideline.
     */
    @Override
    public void onDownloadStateChanged(int newState) {
        setState(newState);
        boolean indeterminate;
        switch (newState) {
            case IDownloaderClient.STATE_IDLE:
                // STATE_IDLE means the service is listening, so it's
                // safe to start making calls via mRemoteService.
                indeterminate = true;
                break;
            case IDownloaderClient.STATE_CONNECTING:
            case IDownloaderClient.STATE_FETCHING_URL:
                indeterminate = true;
                break;
            case IDownloaderClient.STATE_DOWNLOADING:
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_FAILED_CANCELED:
            case IDownloaderClient.STATE_FAILED:
            case IDownloaderClient.STATE_FAILED_FETCHING_URL:
            case IDownloaderClient.STATE_FAILED_UNLICENSED:
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_NEED_CELLULAR_PERMISSION:
            case IDownloaderClient.STATE_PAUSED_WIFI_DISABLED_NEED_CELLULAR_PERMISSION:
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_BY_REQUEST:
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_ROAMING:
            case IDownloaderClient.STATE_PAUSED_SDCARD_UNAVAILABLE:
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_COMPLETED:
//                validateXAPKZipFiles();
                return;
            default:
                indeterminate = true;
        }
//        mPB.setIndeterminate(indeterminate);
    }

    /**
     * Sets the state of the various controls based on the progressinfo object
     * sent from the downloader service.
     */
    @Override
    public void onDownloadProgress(DownloadProgressInfo progress) {
//        mPB.setMax((int) (progress.mOverallTotal >> 8));
//        mPB.setProgress((int) (progress.mOverallProgress >> 8));
    }

    private void setState(int newState) {
        if (mState != newState) {
            mState = newState;
//            Utils.showToast(SplashActivity.this, "" + getString(Helpers.getDownloaderStringResourceIDFromState(newState)));
//            mStatusText.setText(Helpers.getDownloaderStringResourceIDFromState(newState));
        }
    }

    /**
     * Go through each of the Expansion APK files and open each as a zip file.
     * Calculate the CRC for each file and return false if any fail to match.
     *
     * @return true if XAPKZipFile is successful
     */
    void validateXAPKZipFiles() {
        AsyncTask<Object, DownloadProgressInfo, Boolean> validationTask = new AsyncTask<Object, DownloadProgressInfo, Boolean>() {

            @Override
            protected void onPreExecute() {
                Utils.showToast(SplashActivity.this, "" + getString(R.string.text_verifying_download));
//                mStatusText.setText(R.string.text_verifying_download);
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Object... params) {
                for (XAPKFile xf : Config.xAPKS) {
                    String fileName = Helpers.getExpansionAPKFileName(
                            SplashActivity.this,
                            xf.mIsMain, xf.mFileVersion);
                    if (!Helpers.doesFileExist(SplashActivity.this, fileName,
                            xf.mFileSize, false))
                        return false;
                    fileName = Helpers
                            .generateSaveFileName(SplashActivity.this, fileName);
                    ZipResourceFile zrf;
                    byte[] buf = new byte[1024 * 256];
                    try {
                        zrf = new ZipResourceFile(fileName);
                        ZipResourceFile.ZipEntryRO[] entries = zrf.getAllEntries();
                        /**
                         * First calculate the total compressed length
                         */
                        long totalCompressedLength = 0;
                        for (ZipResourceFile.ZipEntryRO entry : entries) {
                            totalCompressedLength += entry.mCompressedLength;
                        }
                        float averageVerifySpeed = 0;
                        long totalBytesRemaining = totalCompressedLength;
                        long timeRemaining;
                        /**
                         * Then calculate a CRC for every file in the Zip file,
                         * comparing it to what is stored in the Zip directory.
                         * Note that for compressed Zip files we must extract
                         * the contents to do this comparison.
                         */
                        for (ZipResourceFile.ZipEntryRO entry : entries) {
                            if (-1 != entry.mCRC32) {
                                long length = entry.mUncompressedLength;
                                CRC32 crc = new CRC32();
                                DataInputStream dis = null;
                                try {
                                    dis = new DataInputStream(
                                            zrf.getInputStream(entry.mFileName));

                                    long startTime = SystemClock.uptimeMillis();
                                    while (length > 0) {
                                        int seek = (int) (length > buf.length ? buf.length
                                                : length);
                                        dis.readFully(buf, 0, seek);
                                        crc.update(buf, 0, seek);
                                        length -= seek;
                                        long currentTime = SystemClock.uptimeMillis();
                                        long timePassed = currentTime - startTime;
                                        if (timePassed > 0) {
                                            float currentSpeedSample = (float) seek
                                                    / (float) timePassed;
                                            if (0 != averageVerifySpeed) {
                                                averageVerifySpeed = SMOOTHING_FACTOR
                                                        * currentSpeedSample
                                                        + (1 - SMOOTHING_FACTOR)
                                                        * averageVerifySpeed;
                                            } else {
                                                averageVerifySpeed = currentSpeedSample;
                                            }
                                            totalBytesRemaining -= seek;
                                            timeRemaining = (long) (totalBytesRemaining / averageVerifySpeed);
                                            this.publishProgress(
                                                    new DownloadProgressInfo(
                                                            totalCompressedLength,
                                                            totalCompressedLength
                                                                    - totalBytesRemaining,
                                                            timeRemaining,
                                                            averageVerifySpeed)
                                            );
                                        }
                                        startTime = currentTime;
                                        if (mCancelValidation)
                                            return true;
                                    }
                                    if (crc.getValue() != entry.mCRC32) {
                                        Log.e(com.google.android.vending.expansion.downloader.Constants.TAG,
                                                "CRC does not match for entry: "
                                                        + entry.mFileName);
                                        Log.e(com.google.android.vending.expansion.downloader.Constants.TAG,
                                                "In file: " + entry.getZipFileName());
                                        return false;
                                    }
                                } finally {
                                    if (null != dis) {
                                        dis.close();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(DownloadProgressInfo... values) {
                onDownloadProgress(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Utils.showToast(SplashActivity.this, "" + getString(R.string.text_validation_complete));
//                    mStatusText.setText(R.string.text_validation_complete);
                    readXAPKZipFiles();
                } else {
                    Utils.showToast(SplashActivity.this, "" + getString(R.string.text_validation_failed));
//                    mStatusText.setText(R.string.text_validation_failed);
                }
                super.onPostExecute(result);
            }

        };
        validationTask.execute(new Object());
    }

    void readXAPKZipFiles() {
        AsyncTask<Void, Void, String> diskWriteTask = new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Utils.showToast(SplashActivity.this, "" + getString(R.string.text_reading_start));
//                mStatusText.setText(R.string.text_reading_start);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    // Get a ZipResourceFile representing a merger of both the main and patch files
                    ZipResourceFile expansionFile = APKExpansionSupport.getAPKExpansionZipFile(SplashActivity.this, Config.EXPANSION_MAIN_VERSION, 0);

                    if (expansionFile != null) {
                        // Get an input stream for a known file inside the expansion file ZIPs
                        InputStream fileStream = expansionFile.getInputStream(Config.EX_MAIN_FOLDER_NAME + "/" + Config.EX_MAIN_FILE_NAME);
                        if (fileStream != null) {
                            return getFilePath(SplashActivity.this, Config.EX_MAIN_FILE_NAME, Config.EX_MAIN_FILE_EXTN, fileStream);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String path) {
                // hide loader with/out  error message
                super.onPostExecute(path);
                if (!TextUtils.isEmpty(path)) {
                    File file = new File(path);
                    if (file.exists()) {
                        Utils.showToast(SplashActivity.this, "" + getString(R.string.text_reading_complete));
//                        mStatusText.setText(R.string.text_reading_complete);
//                        mPB.setVisibility(View.GONE);


                        try {
                            if (file != null && file.exists()) {
                                gifImageView.setImageURI(Uri.fromFile(file));
                                gifDrawable = (GifDrawable) gifImageView.getDrawable();
                                if (!gifDrawable.isPlaying()) {
                                    resetAnimation();
                                    gifDrawable.start();
                                }
                            }
                        } catch (Exception e) {
                            /*HANDEL HERE*/
//            gifImageView.setVisibility(View.GONE);
//            imageViewSplash.setVisibility(View.VISIBLE);
                            Log.e(TAG, "playGifs" + e.getMessage());
                        }


                        Utils.showToast(SplashActivity.this, "" + file.getAbsolutePath());
//                        mImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    } else {
                        Utils.showToast(SplashActivity.this, "" + getString(R.string.text_reading_failed));
//                        mStatusText.setText(R.string.text_reading_failed);
                    }
                }
            }
        };
        diskWriteTask.execute();
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
    /*APK EXPANSION*/
}
// ANDROID SIDE AND IOS SIDE DONE: 3, 4, 5, 6, 8, 12,