package app.witkey.live.fragments.dashboard.profile.profilesetting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.heetch.countrypicker.Country;
import com.heetch.countrypicker.CountryPickerCallbacks;
import com.heetch.countrypicker.CountryPickerDialog;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import app.witkey.live.BuildConfig;
import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.ScreenShotUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.dialogs.SignUpGenderDialog;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.N;

/**
 * Edited by developer on 10/03/2017.
 */

public class ProfileSettingFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnCheck)
    ImageView btnCheck;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;
    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;

    @BindView(R.id.userDobProfile)
    CustomTextView userDobProfile;

    @BindView(R.id.userSloganProfile)
    CustomEditText userSloganProfile;

    @BindView(R.id.userGenderProfile)
    CustomTextView userGenderProfile;

    @BindView(R.id.userCountryProfile)
    CustomTextView userCountryProfile;

    @BindView(R.id.mProfileImageView)
    ImageView mProfileImageView;

    @BindView(R.id.tv_take_photo)
    CustomTextView tv_take_photo;

    @BindView(R.id.tv_photo_gallery)
    CustomTextView tv_photo_gallery;

    @BindView(R.id.includedLayoutPopup)
    View includedLayoutPopup;

    @BindView(R.id.profileSettingParentView)
    LinearLayout profileSettingParentView;

    @BindView(R.id.connectedAccountParentFrame)
    LinearLayout connectedAccountParentFrame;

    @BindView(R.id.editProfileGender)
    ImageView editProfileGender;

    @BindView(R.id.editProfileSlogan)
    ImageView editProfileSlogan;

    @BindView(R.id.editProfileDOB)
    ImageView editProfileDOB;

    @BindView(R.id.editProfileCountry)
    ImageView editProfileCountry;

    @BindView(R.id.editProfileAccount)
    ImageView editProfileAccount;
    @BindView(R.id.userRankIV)
    ImageView userRankIV;
    @BindView(R.id.artistRankIV)
    ImageView artistRankIV;

    @BindView(R.id.editProfileResetPass)
    ImageView editProfileResetPass;
    @BindView(R.id.resetPasswordView)
    View resetPasswordView;

    @BindView(R.id.userSlogan)
    CustomTextView userSlogan;
    @BindView(R.id.userLevelBoxTextView)
    CustomTextView userLevelBoxTextView;
    @BindView(R.id.diamondsCountBoxTextView)
    CustomTextView diamondsCountBoxTextView;
    @BindView(R.id.artistViewParent)
    RelativeLayout artistViewParent;

    @BindView(R.id.resetPasswordParentFrame)
    LinearLayout resetPasswordParentFrame;
    boolean isIncludedLayoutVisible = false;
    private ProgressDialog pDialog = null;
    public static final int IMAGE_CAPTURE = 101;
    public static final int IMAGE_LIBRARY = 102;

    public static final int IMAGE_CAPTURE_REQUEST_CODE = 202;
    public static final int CAMERA_REQUEST_CODE = 203;
    public static final int CHOOSE_PHOTO_VIDEO_REQUEST_CODE = 204;

    UserBO userBO;
    UserLevelBO userLevelBO;
    UserLevelBO artistLevelBO;
    UserProgressDetailBO userProgressDetailBO;
    boolean isChangeMade = false;
    Animations animations;
    private Uri outputUri;
    private Uri outputFileUri;
    String filePath = "";
    private String userDOB = "";
    int userCurrentLevel = 1, artistCurrentLevel = 1;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static ProfileSettingFragment newInstance() {
        Bundle args = new Bundle();
        ProfileSettingFragment fragment = new ProfileSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_profile_setting, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setTitleBarData();
        populateUserProfileData();
        initViews();
        showHideView();
    }

    private void initViews() {
        btnBack.setOnClickListener(this);
        userSlogan.setOnClickListener(this);
        connectedAccountParentFrame.setOnClickListener(this);
        userGenderProfile.setOnClickListener(this);
        userDobProfile.setOnClickListener(this);
        userCountryProfile.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        resetPasswordParentFrame.setOnClickListener(this);
        mProfileImageView.setOnClickListener(this);
        profileSettingParentView.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        tv_photo_gallery.setOnClickListener(this);
        animations = new Animations();
//        Utils.setImageRounded(mProfileImageView, "", getActivity());

        userSloganProfile.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                isChangeMade = true;
            }
        });

        userSloganProfile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus)
                    Animations.buttonBounceAnimation(getActivity(), editProfileSlogan);
            }
        });

    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.profile_setting);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        btnCheck.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;

            case R.id.userSlogan:
                Animations.buttonBounceAnimation(getActivity(), editProfileSlogan);
                break;

            case R.id.connectedAccountParentFrame:
                Animations.buttonBounceAnimation(getActivity(), editProfileAccount);
                gotoNextFragment(PhoneConnectedFragment.newInstance());
                break;

            case R.id.userGenderProfile:
                Animations.buttonBounceAnimation(getActivity(), editProfileGender);
                isChangeMade = true;
                new SignUpGenderDialog().newInstance("PROFILE").show(getActivity().getSupportFragmentManager(), "GenderDialog");
                break;

            case R.id.userDobProfile:
                Animations.buttonBounceAnimation(getActivity(), editProfileDOB);
                isChangeMade = true;
                showDatePicker();
                break;

            case R.id.userCountryProfile:
                if (userBO.getIsArtist() == 0) {
                    Animations.buttonBounceAnimation(getActivity(), editProfileCountry);
                    isChangeMade = true;
                    showCountryPickerDialog();
                }
                break;

            case R.id.resetPasswordParentFrame:
                Animations.buttonBounceAnimation(getActivity(), editProfileResetPass);
                gotoNextFragment(ChangePasswordFragment.newInstance());
                break;

            case R.id.mProfileImageView:
                KeyboardOp.hide(context);
                isIncludedLayoutVisible = true;
                animations.performScaleAnimation(includedLayoutPopup, 0.5f, 0.0f);
                break;

            case R.id.profileSettingParentView:
                checkIfPopupVisible();
                break;

            case R.id.tv_photo_gallery:
                isChangeMade = true;
                choosePhotoFromLibrary();
                break;

            case R.id.tv_take_photo:
                isChangeMade = true;
                capturePhoto();
                break;

            case R.id.btnCheck:
                KeyboardOp.hide(getActivity());
                if (isChangeMade) {
                    if (!userGenderProfile.getText().toString().equals(getString(R.string.user_gender))) {/*FOR GOOGLE+ LOGIN*/
                        userSignUpNetworkCall(getActivity(), userBO.getEmail(),
                                userGenderProfile.getText().toString(), userBO.getId(),
                                userSloganProfile.getText().toString(), userDOB,
                                userCountryProfile.getText().toString(), UserSharedPreference.readUserToken());
                    } else {
                        SnackBarUtil.showSnackbar(context, getString(R.string.gender_required), false);
                    }
                } else {
                    SnackBarUtil.showSnackbar(context, getString(R.string.no_change_to_save), false);
                }
                break;
        }
    }

    // METHOD TO POPULATE ALL USER PROFILE DATA
    private void populateUserProfileData() {
        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
            userCurrentLevel = userProgressDetailBO.getUser_level();

            if (userBO.getSignup_type() == 1) {
                resetPasswordParentFrame.setVisibility(View.VISIBLE);
                resetPasswordView.setVisibility(View.VISIBLE);
            } else {
                resetPasswordView.setVisibility(View.GONE);
                resetPasswordParentFrame.setVisibility(View.GONE);
            }

            artistCurrentLevel = userProgressDetailBO.getArtist_level();
            userLevelBO = Utils.getUserLevel(userCurrentLevel);
            artistLevelBO = Utils.getBroadcasterLevel(artistCurrentLevel);
            if (userBO != null) {
                profileNameTextView.setText(userBO.getUsername().toUpperCase());
                if (userBO.getIsArtist() == 1) {
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getUser_complete_id());
                } else {
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
                }
                Utils.setImageRounded(mProfileImageView, userBO.getProfilePictureUrl(), getActivity());
                userGenderProfile.setText(Utils.firstAlphabetCap(userBO.getGender()));
                userSloganProfile.setText(userBO.getStatusText());
                String star = (Utils.getStarDetail(userBO.getDateOfBirth()).equals("N/A") ? "" : Utils.getStarDetail(userBO.getDateOfBirth()));
                userDOB = userBO.getDateOfBirth();
                userDobProfile.setText(userDOB + " " + star);
                userCountryProfile.setText(userBO.getCountry());
                userLevelBoxTextView.setText(userCurrentLevel + "");
                diamondsCountBoxTextView.setText(artistCurrentLevel + "");

                userRankIV.setImageResource(userLevelBO.getLevelLocalImage());
                artistRankIV.setImageResource(artistLevelBO.getLevelLocalImage());

                if (userBO.getIsArtist() == 1) {
                    artistViewParent.setVisibility(View.VISIBLE);
                } else {
                    artistViewParent.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
            LogUtils.e("populateUserProfileData", "" + e.getMessage());
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

    @Subscribe // METHOD TO GET EVENT MESSAGE FOR SELECTION
    public void getMessage(Events.FragmentToProfileSettingMessage fragmentToProfileSettingMessage) {
        userGenderProfile.setText(fragmentToProfileSettingMessage.getMessage());
    }

    private void showDatePicker() {
        final String star = (Utils.getStarDetail(userBO.getDateOfBirth()).equals("N/A") ? "" : Utils.getStarDetail(userBO.getDateOfBirth()));
        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                userDOB = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                userDobProfile.setText(userDOB + " " + star);
            }
        }, 1970, 0, 1);
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis() - 1000);
        datePickerDialog.show();
    }

    public void showCountryPickerDialog() {
        new CountryPickerDialog(getActivity(), new CountryPickerCallbacks() {
            @Override
            public void onCountrySelected(Country country, int flagResId) {

                userCountryProfile.setText((new Locale(getActivity().getResources().getConfiguration().locale.getLanguage(),
                        country.getIsoCode()).getDisplayCountry()));
            }
        }).show();
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

    // METHOD TO CHECK IF POPUP VIEW VISIBLE THEN CLOSE IT
    private void checkIfPopupVisible() {
        if (isIncludedLayoutVisible) {
            isIncludedLayoutVisible = false;
            animations.performScaleAnimation(includedLayoutPopup, 0.5f, 0.0f);
        }
    }

    // METHOD TO GET IMAGE FROM GALLERY + CHECK EXTERNAL STORAGE PERMISSION
    private void choosePhotoFromLibrary() {
        animations.performScaleAnimation(includedLayoutPopup, 0.5f, 0.0f);
        MarshMallowPermission permission = new MarshMallowPermission(getActivity());
        if (permission.checkPermissionForExternalStorage()) {
            if (SDK_INT < 19) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, IMAGE_LIBRARY);
            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                photoPickerIntent.setType("image/*");
                String[] mimetypes = {"image/*"};
                photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                startActivityForResult(photoPickerIntent, IMAGE_LIBRARY);
            }
        } else {
            permission.requestPermissionForExternalStorage(CHOOSE_PHOTO_VIDEO_REQUEST_CODE, getActivity());
        }
    }

    // METHOD TO CAPTURE IMAGE + CHECK CAMERA PERMISSION
    private void capturePhoto() {

        animations.performScaleAnimation(includedLayoutPopup, 0.5f, 0.0f);
        MarshMallowPermission permission = new MarshMallowPermission(getActivity());
        if (permission.checkPermissionForExternalStorage() && permission.checkPermissionForCamera()) {
            if (hasCamera()) {
                String newFileName = "UserImage_" + Utils.getCurrentTimeStamp() + ".jpg";
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

    // METHOD TO CHECK CAMERA FROM PACKAGE
    private boolean hasCamera() {
        return getActivity().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        isIncludedLayoutVisible = false;
        switch (requestCode) {

            case IMAGE_LIBRARY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
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

                    Glide.with(getActivity()).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mProfileImageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                } else {
                }
                break;

            case IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {

                    Glide.with(getActivity()).load(outputFileUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getActivity().getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mProfileImageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });

                } else {
                    filePath = "";
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case IMAGE_CAPTURE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    capturePhoto();
                    checkIfPopupVisible();
                } else {
                    Utils.showToast(getActivity(), getString(R.string.camera_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;

            case CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    capturePhoto();
                    checkIfPopupVisible();
                } else {
                    Utils.showToast(getActivity(), getString(R.string.camera_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;

            case CHOOSE_PHOTO_VIDEO_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromLibrary();
                    checkIfPopupVisible();
                } else {
                    Utils.showToast(getActivity(), getString(R.string.image_library_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    // METHOD FOR USER SIGNUP NETWORK CALL
    private void userSignUpNetworkCall(final Context context, String userFullName, String userGender, String userID, String userSlogan,
                                       String userDob, String userCountry, final String userToken) {

        showProgress(context);

        String serverUrl = "";
        if (Constants.appDomain == EnumUtils.AppDomain.LIVE) {
            serverUrl = context.getResources().getString(R.string.base_url_live);
        } else if (Constants.appDomain == EnumUtils.AppDomain.QA) {
            serverUrl = context.getResources().getString(R.string.base_url_qa);
        } else if (Constants.appDomain == EnumUtils.AppDomain.DEV) {
            serverUrl = context.getResources().getString(R.string.base_url_dev);
        }

        serverUrl = serverUrl + WebServiceUtils.filterServiceName(EnumUtils.ServiceName.user.toString());

        File file = null;
        try {
            if (!TextUtils.isEmpty(filePath)) {
                File imageFile = new File(filePath);
                file = new Compressor(context).compressToFile(imageFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Builders.Any.B builder = Ion.with(context).load("POST", serverUrl);

        builder.setHeader(Keys.TOKEN, userToken);
        builder.setMultipartParameter(Keys.METHOD, "PATCH");

//      builder.setMultipartParameter(Keys.USER_FULL_NAME, userFullName);
        builder.setMultipartParameter(Keys.USER_ID, userID);
        builder.setMultipartParameter(Keys.USER_GENDER, userGender);
        builder.setMultipartParameter(Keys.USER_DOB, userDob);
        builder.setMultipartParameter(Keys.USER_COUNTRY, userCountry);
        builder.setMultipartParameter(Keys.USER_STATUS_TEXT, userSlogan.isEmpty() ? getString(R.string.i_m_the_reason_someone_smiles_today) : userSlogan);
        builder.setMultipartContentType("multipart/form-data");
        if (file != null)
            builder.setMultipartFile(Keys.USER_PROFILE_PICTURE, "image/jpeg", file);

        builder.asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (result == null) {
                            hideProgress();
//                            Utils.showToast(context, getString(R.string.error_occured));

                        } else {
                            hideProgress();

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
                                            // TODO ADDING COINS BALANCE
                                            //  userBO.setChips(25000);
                                            ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);// TODO NEED OBJECT HERE
//                                            ObjectSharedPreference.saveObject(userBO.getUserProgressDetailBO(), Keys.USER_PROGRESS_DETAIL); /*NOT GETTING REQUIRED OBJECT HERE*/

                                            SnackBarUtil.showSnackbar(context, getString(R.string.profile_update), false);
                                            populateUserProfileData();
                                            addDelayOnBack();
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