package app.witkey.live.fragments.dashboard.profile.moments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import org.apache.commons.io.FileUtils;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.witkey.live.BuildConfig;
import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MomentImagesAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.GiftPanelItemBO;
import app.witkey.live.items.MomentBO;
import app.witkey.live.items.MomentImagesBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.ScreenShotUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.dialogs.UserMomentsImageAddDialog;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

import static android.app.Activity.RESULT_OK;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.N;

/**
 * Edited by developer on 10/03/2017.
 */

public class UserMoments extends BaseFragment implements View.OnClickListener, MomentImagesAdapter.ClickListeners {

    public static final int IMAGE_CAPTURE = 301;
    public static final int IMAGE_LIBRARY = 302;
    public static final int IMAGE_CAPTURE_REQUEST_CODE = 303;
    public static final int CHOOSE_PHOTO_VIDEO_REQUEST_CODE = 304;
    public static final int CAMERA_REQUEST_CODE = 305;

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnCheck)
    ImageView btnCheck;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.momentsText)
    CustomEditText momentsText;
    @BindView(R.id.alphabetCount)
    CustomTextView alphabetCount;

    @BindView(R.id.recyclerViewMomentsImages)
    RecyclerView recyclerViewMomentsImages;
    List<MomentImagesBO> momentImagesBOs;

    private ProgressDialog pDialog = null;
    MomentImagesAdapter momentImagesAdapter;
    int maxSelectionCount = 6;

    private Uri outputUri;
    private Uri outputFileUri;
    String filePath = "";

    UserBO userBO = null;
    MomentBO momentBO = null;
    String userID = "";
    String type;
    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static UserMoments newInstance(MomentBO momentBO, String type) {
        Bundle args = new Bundle();
        args.putParcelable("USERMOMENT", momentBO);
        args.putString("TYPE", type);
        UserMoments fragment = new UserMoments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_moments, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initViews();
        showHideView();

        try {
            if (getArguments() != null) {
                type = getArguments().getString("TYPE");
                momentBO = getArguments().getParcelable("USERMOMENT");

                if (type.equals("EDIT")) {
                    setTitleBarData(R.string.edit_moments_);
                    setUpDataArray(momentBO.getImage_arrsy());
                    if (!TextUtils.isEmpty(momentBO.getUser_status_text())) {
                        momentsText.setText(momentBO.getUser_status_text() + "");
                    }
                } else {
                    setTitleBarData(R.string.add_moments_);
                    setUpData();
                }
            }

            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userID = userBO.getId();
        } catch (Exception e) {
            LogUtils.e("UserMoments", "" + e.getMessage());
        }
        setUpMomentsImagesRecycler(momentImagesBOs);
    }


    private void setUpData() {
        momentImagesBOs = new ArrayList<>();
        momentImagesBOs.add(new MomentImagesBO(null, null));
    }

    private void setUpDataArray(String[] imageArray) {
        momentImagesBOs = new ArrayList<>();
        for (int i = 0; i < imageArray.length; i++) {
            momentImagesBOs.add(new MomentImagesBO(Uri.parse(imageArray[i]), imageArray[i]));
        }
        momentImagesBOs.add(new MomentImagesBO(null, null));
    }

    private void setUpMomentsImagesRecycler(List<MomentImagesBO> momentImagesBOs) {
        if (momentImagesBOs != null) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewMomentsImages.setItemAnimator(new DefaultItemAnimator());
            recyclerViewMomentsImages.setLayoutManager(linearLayoutManager);
            momentImagesAdapter = new MomentImagesAdapter(momentImagesBOs, getContext(), recyclerViewMomentsImages);
            recyclerViewMomentsImages.setAdapter(momentImagesAdapter);
            momentImagesAdapter.setClickListener(this);
        } else {
            //showNoResult(true);
        }
    }


    private void initViews() {

        btnBack.setOnClickListener(this);
        btnCheck.setOnClickListener(this);

        momentsText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                alphabetCount.setText(s.toString().length() + "/140");
//                alphabetCount.setText(140 - s.toString().length() + "/140");
            }
        });
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        btnCheck.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void setTitleBarData(int type) {
        mToolbarTitle.setText(getString(type));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                KeyboardOp.hide(getActivity());
                getActivity().onBackPressed();
                break;
            case R.id.btnCheck:
                validateFields();
                break;
        }
    }

    @Override
    public void onRowClick(View view, int position) {

        switch (view.getId()) {

            case R.id.mMomentImageParentFrame:
                if (position == momentImagesAdapter.getItems().size() - 1) {
                    // add image icon clicked
                    if (momentImagesAdapter != null && momentImagesAdapter.getItemCount() < maxSelectionCount) {

                        new UserMomentsImageAddDialog().show(getActivity().getSupportFragmentManager(), "UserMomentsImageAdd");
                    } else {
                        SnackBarUtil.showSnackbar(context, getString(R.string.no_more_then_5), false);
                    }
                } else {/*DO SOMETHING HERE*/}
                break;
            case R.id.removeMoment:
                if (momentImagesAdapter != null && momentImagesAdapter.getItemCount() > 0) {
                    momentImagesAdapter.removeItems(position);
                }
                break;
        }
    }

    @Override
    public boolean onRowLongPressClick(int position) {
        if (position != momentImagesAdapter.getItems().size() - 1) {
            for (int i = 0; i < momentImagesAdapter.getItemCount(); i++) {
                momentImagesAdapter.getItem(i).setSelected(false);
            }
            momentImagesAdapter.getItem(position).setSelected(true);
            momentImagesAdapter.notifyDataSetChanged();

            return true;
        }
        return false;
    }

    private boolean hasCamera() {
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT);
    }

    private void openCameraToPickImage() {

        MarshMallowPermission permission = new MarshMallowPermission(getActivity());
        if (permission.checkPermissionForExternalStorage() && permission.checkPermissionForCamera()) {
            if (hasCamera()) {
                String newFileName = "MomentImage_" + Utils.getCurrentTimeStamp() + ".jpg";
                File newFile = new File(android.os.Environment.getExternalStorageDirectory(), newFileName);
                if (SDK_INT >= N) {
                    outputUri = FileProvider.getUriForFile(getActivity(),
                            BuildConfig.APPLICATION_ID + ".provider",
                            newFile);
                } else {
                    outputUri = Uri.fromFile(newFile);
                }
                outputFileUri = Uri.fromFile(newFile);
                filePath = outputFileUri.getPath();

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                startActivityForResult(intent, IMAGE_CAPTURE);
            }
        } else {
            if (!permission.checkPermissionForExternalStorage()) {
                permission.requestPermissionForExternalStorage(IMAGE_CAPTURE_REQUEST_CODE, getActivity());
            } else if (!permission.checkPermissionForCamera()) {
                permission.requestPermissionForCamera(CAMERA_REQUEST_CODE, getActivity());
            }
        }
    }

    private void openGalleryToPickMultipleImages() {

        MarshMallowPermission permission = new MarshMallowPermission(getActivity());
        if (permission.checkPermissionForExternalStorage()) {

            FishBun.with(this)
                    .MultiPageMode()
                    .setPickerSpanCount(4)
                    .setActionBarColor(getResources().getColor(R.color.witkey_orange), getResources().getColor(R.color.witkey_orange), true)
                    .setActionBarTitleColor(getResources().getColor(R.color.witkey_dim_white))
                    .setAlbumSpanCount(1, 2)
                    .setMaxCount(momentImagesAdapter != null ? (maxSelectionCount - momentImagesAdapter.getItemCount()) : maxSelectionCount)
                    .setButtonInAlbumActivity(true)
                    .setCamera(false)
                    .setReachLimitAutomaticClose(false)
                    .setAllViewTitle("All")
                    .setActionBarTitle("Witkey")
                    .textOnImagesSelectionLimitReached(getString(R.string.more_then_five_images))
                    .textOnNothingSelected("I need a photo!")
                    .startAlbum();

            /*Intent intent = new Intent();
            intent.setType("image*//*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            String[] mimetypes = {"image*//*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_LIBRARY);*/

        } else {
            permission.requestPermissionForExternalStorage(CHOOSE_PHOTO_VIDEO_REQUEST_CODE, getActivity());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {

                    ArrayList<Uri> uris = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                    for (int i = 0; i < uris.size(); i++) {
                        Uri uri = uris.get(i);

                        String filePath = ScreenShotUtils.getRealPathFromContentURI(getActivity(), uri);

                        try {
                            final int takeFlags = data.getFlags()
                                    & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (momentImagesAdapter != null) {
                            momentImagesBOs.add(0, new MomentImagesBO(uri, filePath));
                        }
                    }
                }
                break;

            case IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    if (momentImagesAdapter != null) {
                        momentImagesBOs.add(0, new MomentImagesBO(outputFileUri, filePath));
                    }
                } else {
                    filePath = "";
                }
                break;
        }

        if (momentImagesAdapter != null) {
           /* for (int i = 0; i < momentImagesAdapter.getItemCount(); i++) {
                momentImagesAdapter.getItem(i).setSelected(false);
            }*/
            momentImagesAdapter.notifyDataSetChanged();
        }

        /*if (requestCode == IMAGE_LIBRARY && resultCode == Activity.RESULT_OK && data != null && data.getClipData() != null) {

            ClipData clipData = data.getClipData();

            for (int i = 0; i < clipData.getItemCount(); i++) {

                Uri uri = clipData.getItemAt(i).getUri();

                String filePath;

                if (Build.VERSION.SDK_INT < 11)
                    filePath = ScreenShotUtils.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    filePath = ScreenShotUtils.getRealPathFromURI_API11to18(getActivity(), data.getData());

                    // SDK > 19 (Android 4.4)
                else
                    filePath = ScreenShotUtils.getRealPathFromURI_API19(getActivity(), data.getData());

                try {
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getActivity().getContentResolver().takePersistableUriPermission(uri, takeFlags);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (momentImagesAdapter != null)
                    momentImagesAdapter.getItems().add(new MomentImagesBO(uri, filePath));
            }

            if (momentImagesAdapter != null)
                momentImagesAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CHOOSE_PHOTO_VIDEO_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGalleryToPickMultipleImages();
                } else {
                    Utils.showToast(getActivity(), getString(R.string.image_library_permission_not_granted));
                }
                break;

            case CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCameraToPickImage();
                } else {
                    Utils.showToast(getActivity(), getString(R.string.camera_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe // METHOD TO GET EVENT MESSAGE
    public void getMessage(Events.UserMomentsFragmentMessage userMomentsFragmentMessage) {

        if (userMomentsFragmentMessage.getMessage().equals("GALLERY")) {
            openGalleryToPickMultipleImages();
        } else if (userMomentsFragmentMessage.getMessage().equals("CAMERA")) {
            openCameraToPickImage();
        }
    }

    private void validateFields() {
        String momentsText_ = momentsText.getText().toString().trim();
        boolean error = false;

        /*if (Validation.isEmpty(momentsText_)) {
            error = true;
            momentsText.requestFocus();
            momentsText.setError(getString(R.string.this_field_is_required));
        }*/

        if (momentImagesAdapter == null) {
            if (Validation.isEmpty(momentsText_)) {
                error = true;
                SnackBarUtil.showSnackbar(getActivity(), getString(R.string.add_moment), false);
            }
        } else {
            if (Validation.isEmpty(momentsText_) && momentImagesAdapter.getItemCount() < 2) {
                error = true;
                SnackBarUtil.showSnackbar(getActivity(), getString(R.string.add_moment), false);
            }
        }

        /*if (Validation.isEmpty(momentsText_) && momentImagesAdapter != null && momentImagesAdapter.getItemCount() < 2) {
            error = true;
            SnackBarUtil.showSnackbar(getActivity(), getString(R.string.add_moment), false);
        }*/

        if (!error) {
            KeyboardOp.hide(getActivity());
            putUserMomentNetworkCall(getActivity(), userID, momentsText.getText().toString(), UserSharedPreference.readUserToken(), type);
        }
    }

    // METHOD FOR USER SIGNUP NETWORK CALL
    private void putUserMomentNetworkCall(final Context context, String userID, String userMoments, String userToken, final String callType) {

        showProgress(context);

        String serverUrl = "";
        if (Constants.appDomain == EnumUtils.AppDomain.LIVE) {
            serverUrl = context.getResources().getString(R.string.base_url_live);
        } else if (Constants.appDomain == EnumUtils.AppDomain.QA) {
            serverUrl = context.getResources().getString(R.string.base_url_qa);
        } else if (Constants.appDomain == EnumUtils.AppDomain.DEV) {
            serverUrl = context.getResources().getString(R.string.base_url_dev);
        }

        serverUrl = serverUrl + WebServiceUtils.filterServiceName(EnumUtils.ServiceName.add_moments.toString());

        ArrayList<File> file = new ArrayList<>();
        ArrayList<String> imageURL = new ArrayList<>();
        try {
            if (momentImagesAdapter != null && momentImagesAdapter.getItemCount() > 0) {
                for (int i = 0; i < momentImagesAdapter.getItemCount(); i++) {
                    if (momentImagesAdapter.getItem(i).getFilePath() != null && !TextUtils.isEmpty(momentImagesAdapter.getItem(i).getFilePath())) {
                        File imageFile = new File(momentImagesAdapter.getItem(i).getFilePath());
                        if (imageFile != null && imageFile.exists()) {
                            file.add(new Compressor(context).compressToFile(imageFile));
                        } else {
                            String url = momentImagesAdapter.getItem(i).getFilePath();
                            if (!TextUtils.isEmpty(url) && url.contains("/")) {
                                String[] parts = url.split("/");
                                if (parts.length > 0) {
                                    imageURL.add(parts[parts.length - 1]);
                                }
                            }
                        }
                        /*if (momentImagesAdapter.getItem(i).getFilePath().contains("")) {
                            FileUtils.copyURLToFile(url, f);
                        } else {*/
//                        File imageFile = new File(momentImagesAdapter.getItem(i).getFilePath());
//                        file.add(new Compressor(context).compressToFile(imageFile));
//                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("putUserMomentNetworkCall", "" + e.getMessage());
        }
        Builders.Any.B builder = Ion.with(context).load("POST", serverUrl);
        builder.setHeader(Keys.TOKEN, userToken);
//      builder.setMultipartParameter(Keys.METHOD, "PATCH");

        builder.setMultipartParameter(Keys.MOMENTS_TEXT, userMoments);
        builder.setMultipartParameter(Keys.USER_ID, userID);

        if (callType.equals("EDIT")) {
            builder.setMultipartParameter(Keys.ID, momentBO.getMoment_id().toString());
        }

        builder.setMultipartContentType("multipart/form-data");
        if (file != null && file.size() > 0) {
            for (int count = 0; count < file.size(); count++) {
                builder.setMultipartFile(Keys.MOMENTS_IMAGE + (count + 1), "image/jpeg", file.get(count));
            }
            /*switch (file.size()) {
                case 1:
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_1, "image/jpeg", file.get(0));
                    break;
                case 2:
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_1, "image/jpeg", file.get(0));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_2, "image/jpeg", file.get(1));
                    break;
                case 3:
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_1, "image/jpeg", file.get(0));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_2, "image/jpeg", file.get(1));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_3, "image/jpeg", file.get(2));
                    break;
                case 4:
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_1, "image/jpeg", file.get(0));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_2, "image/jpeg", file.get(1));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_3, "image/jpeg", file.get(2));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_4, "image/jpeg", file.get(3));
                    break;
                case 5:
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_1, "image/jpeg", file.get(0));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_2, "image/jpeg", file.get(1));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_3, "image/jpeg", file.get(2));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_4, "image/jpeg", file.get(3));
                    builder.setMultipartFile(Keys.MOMENTS_IMAGE_5, "image/jpeg", file.get(4));
                    break;
            }*/
        }
        if (imageURL != null && imageURL.size() > 0) {
            int count = file != null ? file.size() : 0;
            for (int i = 0; i < imageURL.size(); i++) {
                count++;
                builder.setMultipartParameter(Keys.MOMENTS_IMAGE + count, imageURL.get(i));
            }
        }

        builder.asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            hideProgress();
                            Utils.showToast(context, "Error");

                        } else {
                            hideProgress();

                            TaskItem taskItem = WebServiceUtils.parseResponse(result.toString(),
                                    EnumUtils.ServiceName.add_moments);

                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = new JSONObject(taskItem.getResponse());

                                    if (jsonObject.has("errors")) {
//                                        JSONObject jsonError = jsonObject.getJSONObject("errors");
                                        /*DO SOMETHING HERE*/
                                    } else if (jsonObject.has("moments")) {

                                       /* JSONObject favoritesJsonArray = jsonObject.getJSONObject("movement");
                                        String temp = favoritesJsonArray.getString("user_id");*/

                                        SnackBarUtil.showSnackbar(context, callType.equals("EDIT") ? getString(R.string.moment_updated) : getString(R.string.moment_added), false);
                                        clearAllFields();
//                                        new ExitFragmentAsyncTask().execute();
                                        addDelayOnBack();
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }

    // METHOD TO SHOW PROGRESS DIALOG
    private void showProgress(Context context) {

        if (pDialog == null) {
            pDialog = new ProgressDialog(context);
        }

        pDialog.setMessage("Loading please wait... ");
        pDialog.setIndeterminate(true);
        pDialog.show();
    }

    // METHOD TO HIDE PROGRESS DIALOG
    private void hideProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

    // METHOD TO CLEAR ALL SCREEN FIELDS
    private void clearAllFields() {
        momentsText.getText().clear();
        filePath = "";
    }

    // TASK EXIST ACTIVITY SLOWLY
    /*private class ExitFragmentAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }*/


    private void addDelayOnBack() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getActivity() != null)
                    getActivity().onBackPressed();
            }
        }, 1000);
    }


}