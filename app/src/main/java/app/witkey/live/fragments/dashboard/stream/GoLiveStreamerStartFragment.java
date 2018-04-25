package app.witkey.live.fragments.dashboard.stream;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.GoLiveActivity;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.message.PrivateMessageFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.stream.GoLiveStreamActivity;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.PixelsOp;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveStreamerStartFragment extends BaseFragment implements
        ActivityCompat.OnRequestPermissionsResultCallback, View.OnClickListener, SurfaceHolder.Callback {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.overlayView)
    View overlayView;

    @BindView(R.id.ll_bottom_icons)
    LinearLayout ll_bottom_icons;

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

    @BindView(R.id.accountSummaryBTN)
    CustomTextView accountSummaryBTN;

    @BindView(R.id.goLiveIDTV)
    CustomEditText goLiveIDTV;

    @BindView(R.id.goLiveTitleTV)
    CustomEditText goLiveTitleTV;
    @BindView(R.id.camerapreview)
    SurfaceView surfaceView;

    StreamBO streamBO;
    UserBO userBO;

    /*CAMERA VIEW*/
    Camera camera;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    /*CAMERA VIEW*/

    private int totalRecords = 0;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    public static GoLiveStreamerStartFragment newInstance() {
        Bundle args = new Bundle();
        GoLiveStreamerStartFragment fragment = new GoLiveStreamerStartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.activity_go_live_streamer_start, container, false);

        context = inflater.getContext();

        rootView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                layoutParams.topMargin = (int) (Utils.getScreenHeight(getActivity()) - ll_bottom_icons.getHeight() - PixelsOp.pxFromDp(getActivity(), 60f));
                layoutParams.topMargin = (Utils.getScreenHeight(getActivity()) - ll_bottom_icons.getHeight() - Utils.dp2px(getActivity(), 60));
                ll_bottom_icons.setLayoutParams(layoutParams);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        //checkUserType();
        initView();
    }

    private void initCameraPreview() {
        try {
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        } catch (Exception e) {
            LogUtils.e("initCameraPreview", "" + e.getMessage());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            try {
                camera.stopPreview();
                camera.release();
                camera = null;
                previewing = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            if (userBO != null) {
                goLiveIDTV.setText(userBO.getUser_complete_id());
                loginType(userBO.getSignup_type());
            }
            goLiveSignalBTN.setOnClickListener(this);
            goLiveScreenBTN.setOnClickListener(this);
            goLiveCalanderBTN.setOnClickListener(this);
            goLiveFacebookBTN.setOnClickListener(this);
            goLiveGoogleBTN.setOnClickListener(this);
            goLiveWechatBTN.setOnClickListener(this);
            goLiveEmailBTN.setOnClickListener(this);
            goLiveBTN.setOnClickListener(this);
            goLiveCancelBTN.setOnClickListener(this);

            if (userBO.getIsArtist() == 1) {
                accountSummaryBTN.setVisibility(View.VISIBLE);
                accountSummaryBTN.setOnClickListener(this);
            } else {
                accountSummaryBTN.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            LogUtils.e("initView", "" + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goLiveSignalBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.accountSummaryBTN:
                gotoNextFragment(AccountSummaryFragment.newInstance());
                break;
            case R.id.goLiveScreenBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveCalanderBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveFacebookBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveGoogleBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveWechatBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveEmailBTN:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.goLiveBTN:

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {
                    validateFieldsMakeNetworkCall();/*UN COMMENT FOR OLD FLOW*/
                } else {
                    checkForCameraPermission();
                }
                break;
            case R.id.goLiveCancelBTN:
                KeyboardOp.hide(getActivity());
                getActivity().onBackPressed();
                break;
        }
    }

    private void checkForCameraPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                initCameraPreview();
            } catch (Exception e) {
                LogUtils.e(TAG, e.getMessage() + "");
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            ConfirmationDialogFragment
                    .newInstance(R.string.camera_permission_confirmation,
                            new String[]{Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION,
                            R.string.camera_permission_not_granted)
                    .show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
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
            return new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
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
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // REGISTER EVENT BUS TO LISTEN EVENT.
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        // UNREGISTER EVENT BUS
        GlobalBus.getBus().unregister(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkForCameraPermission();
    }

    @Subscribe // METHOD TO GET EVENT MESSAGE FOR SELECTION
    public void getMessage(Events.MainFragmentToGoLiveFragmentMessage
                                   mainFragmentToGoLiveFragmentMessage) {

        if (mainFragmentToGoLiveFragmentMessage.getMessage().equals("OPEN_WOWZA_VIEW")) {
            if (!Constants.BUILD_TYPE_QA) { // TODO ADD !
//                checkUserType();
            }
        }
    }

    // METHOD TO CHECK USER OBJECT AND MAKE NETWORK CALL
    private void makeNetworkCall() {
        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        if (userBO != null) {
            goLiveNetworkCall(getActivity(), userBO.getId(), goLiveTitleTV.getText().toString(), "240p", 1, 1, 1, 1, userBO.getToken());
        }
    }

    // METHOD TO MAKE NETWORK CALL TO CREATE STREAM
    private void goLiveNetworkCall(final Context context, String userID, String
            nameCategory, String quality, int isPublic, int allowComments,
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
//        serviceParams.put(Keys.CATEGORY_ID, "0.0");
        tokenServiceHeaderParams.put(Keys.TOKEN, token);

        new WebServicesVolleyTask(context, true, "",
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
                                clearAllFields();
                                if (streamBO != null) {
                                    startActivity(new Intent(getActivity(), GoLiveStreamActivity.class)
                                            .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_1)
                                            .putExtra(GoLiveActivity.ARG_PARAM_3, streamBO));
                                    onBackPressed();
                                } else {
                                    SnackBarUtil.showSnackbar(context, getString(R.string.unable_to_create_stream), false);
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

    private void validateFieldsMakeNetworkCall() {

        if (userBO != null) {
            if (userBO.getIsArtist() == 0) {
//                checkUserType();
                return;
            }
        }

        String steamID = goLiveTitleTV.getText().toString().trim();
        String steamTitle = goLiveIDTV.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(steamTitle)) {
            error = true;
            goLiveIDTV.requestFocus();
            goLiveIDTV.setError(getString(R.string.this_field_is_required));

        }

        if (Validation.isEmpty(steamID)) {
            error = true;
            goLiveTitleTV.requestFocus();
            goLiveTitleTV.setError(getString(R.string.this_field_is_required));

        }

        if (!error) {
            KeyboardOp.hide(getActivity());
            if (Constants.BUILD_TYPE_QA) { // TODO FOR TESTING ONLY
                startActivity(new Intent(getActivity(), GoLiveActivity.class).putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_1));
            } else {
                makeNetworkCall();
            }
        }
    }

    // METHOD TO CLEAR ALL SCREEN FIELDS
    private void clearAllFields() {
        goLiveTitleTV.getText().clear();
//        goLiveIDTV.getText().clear();
    }

    // (1 for email, 2 for fb, 3 for google, 4 for wechat)
    private void loginType(int type) {
        goLiveEmailBTN.setColorFilter(ContextCompat.getColor(getActivity(), type == 1 ? R.color.witkey_orange : R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        goLiveFacebookBTN.setColorFilter(ContextCompat.getColor(getActivity(), type == 2 ? R.color.witkey_orange : R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        goLiveGoogleBTN.setColorFilter(ContextCompat.getColor(getActivity(), type == 3 ? R.color.witkey_orange : R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        goLiveWechatBTN.setColorFilter(ContextCompat.getColor(getActivity(), type == 4 ? R.color.witkey_orange : R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
}