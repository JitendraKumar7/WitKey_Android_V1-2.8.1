package app.witkey.live.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import app.witkey.live.BuildConfig;
import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.ScreenShotUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.Validation;
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
 * created by developer on 9/21/2017.
 */

public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.mProfileImageView)
    ImageView mProfileImageView;

    @BindView(R.id.edtUserName)
    CustomEditText edtUserName;

    @BindView(R.id.includedLayout)
    View includedLayout;

    @BindView(R.id.tv_take_photo)
    CustomTextView tv_take_photo;

    @BindView(R.id.tv_photo_gallery)
    CustomTextView tv_photo_gallery;

    @BindView(R.id.edtGender)
    CustomEditText edtGender;

    @BindView(R.id.edtEmail)
    CustomEditText edtEmail;

    @BindView(R.id.edtPassword)
    CustomEditText edtPassword;

    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    @BindView(R.id.signupParent)
    LinearLayout signupParent;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    boolean isIncludedLayoutVisible = false;
    Animations animations;
    private Uri outputUri;
    private Uri outputFileUri;
    private ProgressDialog pDialog = null;

    String filePath = "";
    public static final int IMAGE_CAPTURE = 101;
    public static final int IMAGE_LIBRARY = 102;

    public static final int IMAGE_CAPTURE_REQUEST_CODE = 202;
    public static final int CAMERA_REQUEST_CODE = 203;
    public static final int CHOOSE_PHOTO_VIDEO_REQUEST_CODE = 204;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        initToolBar();
        initView();
    }

    // TODO UPDATE NAVIGATION ICON
    private void initToolBar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow_left_white_36dp));
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.witkey_orange), PorterDuff.Mode.MULTIPLY);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        btnSignUp.setOnClickListener(this);
        mProfileImageView.setOnClickListener(this);
        tv_take_photo.setOnClickListener(this);
        tv_photo_gallery.setOnClickListener(this);
        signupParent.setOnClickListener(this);
        edtGender.setOnClickListener(this);
        animations = new Animations();
        Utils.setImageRounded(mProfileImageView, "", this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp:
                checkUserCredentials();
                break;

            case R.id.mProfileImageView:
                KeyboardOp.hide(this);
                isIncludedLayoutVisible = true;
                animations.performScaleAnimation(includedLayout, 0.5f, 0.0f);
                break;

            case R.id.tv_photo_gallery:
                choosePhotoFromLibrary();
                break;

            case R.id.tv_take_photo:
                capturePhoto();
                break;

            case R.id.signupParent:
                checkIfPopupVisible();
                break;

            case R.id.edtGender:
                new SignUpGenderDialog().newInstance("SIGNUP").show(getSupportFragmentManager(), "GenderDialog");
                break;
        }
    }

    // METHOD TO VALIDATE ALL USER SIGNUP FIELDS AND MAKE NETWORK CALL
    private void checkUserCredentials() {

        String userName = edtUserName.getText().toString().trim();
        String gender = edtGender.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(password)) {
            error = true;
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.this_field_is_required));

        } else if (password.length() < 6) {
            error = true;
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.password_length_short));
        }

        if (Validation.isEmpty(gender)) {
            error = true;
            edtGender.requestFocus();
            edtGender.setError(getString(R.string.this_field_is_required));

        } else if (gender.length() < 3) {
            error = true;
            edtGender.requestFocus();
            edtGender.setError(getString(R.string.this_field_is_required));
        }

        if (Validation.isEmpty(userName)) {
            error = true;
            edtUserName.requestFocus();
            edtUserName.setError(getString(R.string.this_field_is_required));

        } else if (userName.length() < 3) {
            error = true;
            edtUserName.requestFocus();
            edtUserName.setError(getString(R.string.must_be_at_least_three_characters_long));
        }

        if (Validation.isEmpty(email)) {
            error = true;
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.this_field_is_required));

        } else if (!Validation.isValidEmail(email)) {
            error = true;
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.please_enter_valid_email));
        }

        if (error == false) {
            KeyboardOp.hide(this);
            userSignUpNetworkCall(this, userName, email, gender, password);
        }
    }

    // METHOD TO CAPTURE IMAGE + CHECK CAMERA PERMISSION
    private void capturePhoto() {

        animations.performScaleAnimation(includedLayout, 0.5f, 0.0f);
        MarshMallowPermission permission = new MarshMallowPermission(this);
        if (permission.checkPermissionForExternalStorage() && permission.checkPermissionForCamera()) {
            if (hasCamera()) {
                String newFileName = "UserImage_" + Utils.getCurrentTimeStamp() + ".jpg";
                File newFile = new File(android.os.Environment.getExternalStorageDirectory(), newFileName);
                if (SDK_INT >= N) {
                    outputUri = FileProvider.getUriForFile(SignUpActivity.this,
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
                permission.requestPermissionForExternalStorage(IMAGE_CAPTURE_REQUEST_CODE, SignUpActivity.this);
            } else if (!permission.checkPermissionForCamera()) {
                permission.requestPermissionForCamera(CAMERA_REQUEST_CODE, SignUpActivity.this);
            }
        }
    }

    // METHOD TO GET IMAGE FROM GALLERY + CHECK EXTERNAL STORAGE PERMISSION
    private void choosePhotoFromLibrary() {
        animations.performScaleAnimation(includedLayout, 0.5f, 0.0f);
        MarshMallowPermission permission = new MarshMallowPermission(this);
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
            permission.requestPermissionForExternalStorage(CHOOSE_PHOTO_VIDEO_REQUEST_CODE, SignUpActivity.this);
        }
    }

    // METHOD TO CHECK CAMERA FROM PACKAGE
    private boolean hasCamera() {
        return this.getPackageManager().hasSystemFeature(
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
                        filePath = ScreenShotUtils.getRealPathFromURI_BelowAPI11(this, data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        filePath = ScreenShotUtils.getRealPathFromURI_API11to18(this, data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        filePath = ScreenShotUtils.getRealPathFromURI_API19(this, data.getData());

                    try {
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        this.getContentResolver().takePersistableUriPermission(uri, takeFlags);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Glide.with(SignUpActivity.this).load(uri).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(SignUpActivity.this.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mProfileImageView.setImageDrawable(circularBitmapDrawable);

                        }
                    });
                } else {
                }
                break;

            case IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {

                    Glide.with(SignUpActivity.this).load(outputFileUri).asBitmap().centerCrop().into(new BitmapImageViewTarget(mProfileImageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(SignUpActivity.this.getResources(), resource);
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

    // METHOD FOR USER SIGNUP NETWORK CALL
    private void userSignUpNetworkCall(final Context context, String userName, String userEmail,
                                       String userGender, String userPassword) {

        showProgress(context);

        String serverUrl = "";
        if (Constants.appDomain == EnumUtils.AppDomain.LIVE) {
            serverUrl = context.getResources().getString(R.string.base_url_live);
        } else if (Constants.appDomain == EnumUtils.AppDomain.QA) {
            serverUrl = context.getResources().getString(R.string.base_url_qa);
        } else if (Constants.appDomain == EnumUtils.AppDomain.DEV) {
            serverUrl = context.getResources().getString(R.string.base_url_dev);
        }

        serverUrl = serverUrl + WebServiceUtils.filterServiceName(EnumUtils.ServiceName.register.toString());


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
        builder.setMultipartParameter(Keys.USER_NAME, userName);
        builder.setMultipartParameter(Keys.USER_EMAIL, userEmail);
        builder.setMultipartParameter(Keys.USER_PASSWORD, userPassword);
        builder.setMultipartParameter(Keys.USER_GENDER, userGender);

//        serviceParams.put(Keys.DEVICE_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); // NOT USED YET
        builder.setMultipartParameter(Keys.DEVICE_NAME, Build.MANUFACTURER);
        builder.setMultipartParameter(Keys.DEVICE_OS_VERSION, Build.VERSION.RELEASE);
        builder.setMultipartParameter(Keys.DEVICE_REGISTERATION_ID, UserSharedPreference.readRegToken());
//        builder.setMultipartParameter(Keys.DEVICE_REGISTERATION_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        builder.setMultipartParameter(Keys.DEVICE_RESOLUTION, Utils.getScreenWidth(context) + " x " + Utils.getScreenHeight(context));
        builder.setMultipartParameter(Keys.DEVICE_TYPE, "2"); // (1 for IOS, 2 for andriod)
        builder.setMultipartParameter(Keys.DEVICE_USER_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


        builder.setMultipartContentType("multipart/form-data");
        if (file != null)
            builder.setMultipartFile(Keys.USER_PROFILE_PICTURE, "image/jpeg", file);

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
                                    EnumUtils.ServiceName.register);

                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = new JSONObject(taskItem.getResponse());

                                    if (jsonObject.has("errors")) {
                                        JSONObject jsonError = jsonObject.getJSONObject("errors");

                                        if (jsonError.has("username")) {
                                            edtUserName.setError(getString(R.string.user_name_exist));
                                            edtUserName.requestFocus();
                                        } else if (jsonError.has("email")) {
                                            edtEmail.requestFocus();
                                            edtEmail.setError(getString(R.string.email_exist));
                                        }

                                    } else if (jsonObject.has("user")) {
                                        Gson gson = new Gson();
                                        UserBO userBO = gson.fromJson(jsonObject.get("user").toString(), UserBO.class);

                                        if (userBO != null) {

                                            UserSharedPreference.saveIsUserLoggedIn(true);
                                            UserSharedPreference.saveUserToken(userBO.getToken());
                                            // TODO ADDING COINS BALANCE
                                            // userBO.setChips(25000);
                                            ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);// TODO ADD OBJECT HERE
                                            ObjectSharedPreference.saveObject(userBO.getUserProgressDetailBO(), Keys.USER_PROGRESS_DETAIL); /*NOT GETTING REQUIRED OBJECT HERE*/

                                            clearAllFields();

                                            if (userBO.getNotificationMessageBO() != null && userBO.getNotificationMessageBO().size() > 0) {
                                                Intent intent = new Intent(context, WelcomeActivity.class);
                                                intent.putExtra("MESSAGE", userBO.getNotificationMessageBO().get(0));
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(context, Dashboard.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }

                                            finish();
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
        edtEmail.getText().clear();
        edtPassword.getText().clear();
        edtUserName.getText().clear();
        edtGender.getText().clear();
        filePath = "";
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
    public void getMessage(Events.FragmentToSignUpActivityMessage fragmentToSignUpActivityMessage) {
        edtGender.setText(fragmentToSignUpActivityMessage.getMessage());
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
                    Utils.showToast(SignUpActivity.this, getString(R.string.camera_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;

            case CAMERA_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    capturePhoto();
                    checkIfPopupVisible();
                } else {
                    Utils.showToast(SignUpActivity.this, getString(R.string.camera_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;

            case CHOOSE_PHOTO_VIDEO_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromLibrary();
                    checkIfPopupVisible();
                } else {
                    Utils.showToast(SignUpActivity.this, getString(R.string.image_library_permission_not_granted));
                }
                // No need to start camera here; it is handled by onResume
                break;
        }
    }

    // METHOD TO CHECK IF POPUP VIEW VISIBLE THEN CLOSE IT
    private void checkIfPopupVisible() {
        if (isIncludedLayoutVisible) {
            isIncludedLayoutVisible = false;
            animations.performScaleAnimation(includedLayout, 0.5f, 0.0f);
        }
    }

}