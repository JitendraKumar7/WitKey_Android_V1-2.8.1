package app.witkey.live.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 9/20/2017.
 */

public class LoginArtisteActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edtArtisteID)
    CustomEditText edtArtisteID;

    @BindView(R.id.edtArtistePassword)
    CustomEditText edtArtistePassword;

    @BindView(R.id.btnLoginArtiste)
    Button btnLoginArtiste;

    @BindView(R.id.btnRegisterArtiste)
    CustomTextView btnRegisterArtiste;

    @BindView(R.id.btnForgetPassword)
    CustomTextView btnForgetPassword;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_artiste);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        edtArtisteID.setOnClickListener(this);
        edtArtistePassword.setOnClickListener(this);
        btnLoginArtiste.setOnClickListener(this);
        btnRegisterArtiste.setOnClickListener(this);
        btnForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginArtiste:
                checkUserCredentials();
                break;

            case R.id.btnRegisterArtiste:
                //AppSettingsBO appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
                //if (appSettingsBO != null && appSettingsBO.getAllow_signup().equals(Constants.IS_ALLOW_SIGNUP_TRUE)) {
                    startActivity(new Intent(LoginArtisteActivity.this, SignUpActivity.class));
                //}
                break;

            case R.id.btnForgetPassword:
                startActivity(new Intent(LoginArtisteActivity.this, ForgetPasswordActivity.class));
                break;
        }
    }

    // METHOD TO VALIDATE ALL USER LOGIN FIELDS AND MAKE NETWORK CALL
    private void checkUserCredentials() {

        String artisteID = edtArtisteID.getText().toString().trim();
        String artistePassword = edtArtistePassword.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(artistePassword)) {
            error = true;
            edtArtistePassword.requestFocus();
            edtArtistePassword.setError(getString(R.string.this_field_is_required));

        } else if (artistePassword.length() < 6) {
            error = true;
            edtArtistePassword.requestFocus();
            edtArtistePassword.setError(getString(R.string.password_length_short));
        }

        if (Validation.isEmpty(artisteID)) {
            error = true;
            edtArtisteID.requestFocus();
            edtArtisteID.setError(getString(R.string.this_field_is_required));
        } else if (artisteID.length() < 3) {
            error = true;
            edtArtisteID.requestFocus();
            edtArtisteID.setError(getString(R.string.must_be_at_least_three_characters_long));
        } else if (!Validation.isValidEmail(artisteID)) {
            error = true;
            edtArtisteID.requestFocus();
            edtArtisteID.setError(getString(R.string.error_invalid_email));
        }

        if (error == false) {
            KeyboardOp.hide(this);
            userlogInNetworkCall(this, artisteID, artistePassword);
        }
    }

    // METHOD FOR USER LOGIN NETWORK CALL
    private void userlogInNetworkCall(final Context context, String userEmail, String userPassword) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        serviceParams.put(Keys.USER_EMAIL, userEmail);
        serviceParams.put(Keys.USER_PASSWORD, userPassword);

//        serviceParams.put(Keys.DEVICE_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); // NOT USED YET
        serviceParams.put(Keys.DEVICE_NAME, Build.MANUFACTURER);
        serviceParams.put(Keys.DEVICE_OS_VERSION, Build.VERSION.RELEASE);
//        serviceParams.put(Keys.DEVICE_REGISTERATION_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        serviceParams.put(Keys.DEVICE_REGISTERATION_ID, UserSharedPreference.readRegToken());
        serviceParams.put(Keys.DEVICE_RESOLUTION, Utils.getScreenWidth(context) + " x " + Utils.getScreenHeight(context));
        serviceParams.put(Keys.DEVICE_TYPE, "2"); // (1 for IOS, 2 for andriod)
        serviceParams.put(Keys.DEVICE_USER_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.login,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        // REPLACED BY QA
                        AlertOP.showAlert(context, null, getString(R.string.invalid_email_pass));
//                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                Gson gson = new Gson();
                                UserBO userBO = gson.fromJson(jsonObject.get("user").toString(), UserBO.class);

                                if (userBO != null) {
                                    UserSharedPreference.saveIsUserLoggedIn(true);
                                    UserSharedPreference.saveUserToken(userBO.getToken());
                                    // TODO ADDING COINS BALANCE
                                    // userBO.setChips(25000);
                                    ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
                                    ObjectSharedPreference.saveObject(userBO.getUserProgressDetailBO(), Keys.USER_PROGRESS_DETAIL);

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
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO CLEAR ALL SCREEN FIELDS
    private void clearAllFields() {
        edtArtisteID.getText().clear();
        edtArtistePassword.getText().clear();
    }

}

