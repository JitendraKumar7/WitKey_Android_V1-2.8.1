package app.witkey.live.activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.devices.WZCameraView;
import com.wowza.gocoder.sdk.api.status.WZState;
import com.wowza.gocoder.sdk.api.status.WZStatus;
import com.wowza.gocoder.sdk.api.status.WZStatusCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.wowza.WowzaUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveStartPageActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback, WZStatusCallback, View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();
    // TODO HAVE TO REMOVE THIS CLASS
    @BindView(R.id.camera)
    WZCameraView mCameraView;

    @BindView(R.id.goLiveSignalBTN)
    ImageView goLiveSignalBTN;

    @BindView(R.id.goLiveScreenBTN)
    ImageView goLiveScreenBTN;

    @BindView(R.id.goLiveCalanderBTN)
    ImageView goLiveCalanderBTN;

    @BindView(R.id.goLiveFacebookBTN)
    ImageView goLiveFacebookBTN;

    @BindView(R.id.goLiveGoogleBTN)
    ImageView goLiveGoogleBTN;

    @BindView(R.id.goLiveWechatBTN)
    ImageView goLiveWechatBTN;

    @BindView(R.id.goLiveEmailBTN)
    ImageView goLiveEmailBTN;

    @BindView(R.id.goLiveCancelBTN)
    ImageView goLiveCancelBTN;

    @BindView(R.id.goLiveBTN)
    CustomTextView goLiveBTN;

    WowzaGoCoder goCoder;
    ProgressDialog progressDialog;

    StreamBO streamBO;
    UserBO userBO;
    private int totalRecords = 0;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_live_start);
        ButterKnife.bind(this);

        initView();
        new InitWowozaAsyncTask().execute();
    }

    private void initWowzaSDK() {
        goCoder = WowzaUtils.initWowzaGoCoderInstance(getApplicationContext());
    }

    private void initView() {
        goLiveSignalBTN.setOnClickListener(this);
        goLiveScreenBTN.setOnClickListener(this);
        goLiveCalanderBTN.setOnClickListener(this);
        goLiveFacebookBTN.setOnClickListener(this);
        goLiveGoogleBTN.setOnClickListener(this);
        goLiveWechatBTN.setOnClickListener(this);
        goLiveEmailBTN.setOnClickListener(this);
//        goLiveBTN.setOnClickListener(this);
        goLiveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNetworkCall();
            }
        });
        goLiveCancelBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goLiveSignalBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveScreenBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveCalanderBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveFacebookBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveGoogleBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveWechatBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveEmailBTN:
                Utils.showToast(this, getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveBTN:
                makeNetworkCall();
//                startActivity(new Intent(this, GoLiveActivity.class).putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_1));
//                finish();
                break;
            case R.id.goLiveCancelBTN:
                finish();
                break;
        }
    }

    @Override
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
                Utils.showToast(GoLiveStartPageActivity.this, statusMessage.toString());
            }
        });
    }

    @Override
    public void onWZError(final WZStatus goCoderStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Utils.showToast(GoLiveStartPageActivity.this,
                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription());
            }
        });
    }

    private void checkForCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            if (mCameraView != null) {

                if (mCameraView.isPreviewPaused()) {
                    mCameraView.onResume();
                } else {
                    mCameraView.startPreview();
                }
            }

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

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkForCameraPermission();
                }
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    private class InitWowozaAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            //showProgress();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            initWowzaSDK();
            checkForCameraPermission();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
          /*if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        checkForCameraPermission();
    }

    private void showProgress() {

        progressDialog = new ProgressDialog(GoLiveStartPageActivity.this);
        progressDialog.setMessage("Streaming server initializing. Please wait.");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    // METHOD TO CHECK USER OBJECT AND MAKE NETWORK CALL
    private void makeNetworkCall() {

        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        if (userBO != null) {
            goLiveNetworkCall(this, userBO.getId(), "STREAM_NAME_CATEGORY", "240p", 1, 1, 1, 1, userBO.getToken());
        }
    }

    private void goLiveNetworkCall(final Context context, String userID, String nameCategory, String quality, int isPublic, int allowComments,
                                   int allowTagRequest, int availableLater, String token) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        if (!quality.isEmpty())
            quality = quality.replace("p", "");
        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.NAME, nameCategory);
        serviceParams.put(Keys.QUALITY, quality);
        serviceParams.put(Keys.IS_PUBLIC, isPublic);
        serviceParams.put(Keys.ALLOW_COMMENTS, allowComments);
        serviceParams.put(Keys.ALLOW_TAG_REQUEST, allowTagRequest);
        serviceParams.put(Keys.AVAILABLE_LATER, availableLater);
        serviceParams.put(Keys.LONGITUDE, "0.0");
        serviceParams.put(Keys.LATITUDE, "0.0");
        serviceParams.put(Keys.CATEGORY_ID, "0.0");
        tokenServiceHeaderParams.put(Keys.TOKEN, token);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.streams,
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
                                JSONArray streamsJsonArray = jsonObject.getJSONArray("streams");
                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<StreamBO>>() {
                                }.getType();
                                List<StreamBO> newStreamBOs = (List<StreamBO>) gson.fromJson(streamsJsonArray.toString(),
                                        listType);
                                totalRecords = jsonObject.getInt("total_records");

                                streamBO = newStreamBOs.get(0);
                                streamBO.setStreaming(true);
                                //setWowzaConfig(streamBO);


                                // TODO CHANGE FLOW CALL HERE
                               /* Intent intent = new Intent(getActivity(), BroadCastActivity.class);
                                intent.putExtra(BroadCastActivity.ARG_PARAM1, streamBO);*/

                                // NEW FLOW
//                                Intent intent = new Intent(getActivity(), BroadCastToManyActivity.class);
//                                intent.putExtra(BroadCastToManyActivity.ARG_PARAM1, streamBO);
//                                startActivity(intent);
                                //((BaseActivity) (context)).replaceFragment(FindContacts.getInstance());
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

