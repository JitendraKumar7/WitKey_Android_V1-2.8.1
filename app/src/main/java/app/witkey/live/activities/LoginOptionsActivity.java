package app.witkey.live.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
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
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.circleprogressbar.CircleProgressBar;
import app.witkey.live.utils.dialogs.PromoPopup;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

/**
 * created by developer on 9/21/2017.
 */

public class LoginOptionsActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "JKS";
    private static final int RC_SIGN_IN = 007;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;


    @BindView(R.id.btn_login_email)
    ImageView btn_login_email;

    @BindView(R.id.btn_login_facebook)
    ImageView btn_login_facebook;

    @BindView(R.id.img_login_option_back)
    ImageView img_login_option_back;

    @BindView(R.id.textImage)
    ImageView textImage;

    @BindView(R.id.btn_login_wechat)
    ImageView btn_login_wechat;

    @BindView(R.id.btn_login_google)
    ImageView btn_login_google;

    @BindView(R.id.custom_progressHour)
    CircleProgressBar custom_progressHour;

    @BindView(R.id.custom_progressDay)
    CircleProgressBar custom_progressDay;

    @BindView(R.id.custom_progressMin)
    CircleProgressBar custom_progressMin;

    @BindView(R.id.custom_progressSec)
    CircleProgressBar custom_progressSec;
    @BindView(R.id.timerParent)
    LinearLayout timerParent;
    @BindView(R.id.fb_login_button)
    LoginButton fbLoginButton;

    /*FACEBOOK CALLBACK*/
    private CallbackManager mCallbackManager;

    boolean animationStatus = true;
    long different_ = 0;
    int currentIndex = 0;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_options);
        ButterKnife.bind(this);

        initView();
        // initAnimation();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //signInButton.setVisibility(View.GONE);
                //signOutButton.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (user.getDisplayName() != null) {

                        Log.d(TAG, "Firebase User Id:" + user.getUid());
                        Log.d(TAG, "Firebase User Email:" + user.getEmail());
                        Log.d(TAG, "Firebase User Photo Url:" + user.getPhotoUrl());
                        Log.d(TAG, "Firebase User Display Name:" + user.getDisplayName());

                        userlogInNetworkCall(user.getUid());
                        userSignUpNetworkCall(LoginOptionsActivity.this, user.getDisplayName(), user.getEmail(), "userPassword");
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        mCallbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // ...
            }
        });

    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {

        AppSettingsBO appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
        if (appSettingsBO != null) {
            if (!appSettingsBO.getIs_live().equals(Constants.IS_LIVE_TRUE)) { // TODO ADD !
                timerParent.setVisibility(View.VISIBLE);
//                showTimer(appSettingsBO.getGo_live_date().isEmpty() ? "0000-00-00 00:00:00" : appSettingsBO.getGo_live_date());
                showTimer(appSettingsBO.getMiliseconds().isEmpty() ? "00" : appSettingsBO.getMiliseconds());

                /*if (!appSettingsBO.getPromo_code().isEmpty())
                    showPromoAlert();*/

                setControlsEnable(false);
            }
            /*PROMOTION LOGIC UPDATED*/ // TODO TO BE DISCUSS
            if (appSettingsBO.getAllow_initial_promotion() != null && appSettingsBO.getAllow_initial_promotion() == 1) {
                showPromoAlert();
            }
        }
        btn_login_email.setOnClickListener(this);
        btn_login_facebook.setOnClickListener(this);
        btn_login_google.setOnClickListener(this);
        btn_login_wechat.setOnClickListener(this);
    }

    private void showPromoAlert() {
        PromoPopup promoPopup = new PromoPopup(LoginOptionsActivity.this);
        promoPopup.setCancelable(true);
        promoPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        promoPopup.show();

    }

    // METHOD TO INITIALIZE ANIMATION
    private void initAnimation() {

        final int[] imageBackground = {
                R.drawable.bg2,
                R.drawable.bg3,
                R.drawable.bg4,
                R.drawable.bg5,
                R.drawable.bg6
        };
        final int[] imageBackgroundText = {
                R.drawable.gold,
                R.drawable.wood,
                R.drawable.water,
                R.drawable.fire,
                R.drawable.earth
        };
        img_login_option_back.postDelayed(
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {

                            BitmapFactory.Options opts = new BitmapFactory.Options();
                            opts.inDensity = DisplayMetrics.DENSITY_DEFAULT;
                            opts.inSampleSize = 4;

                            Bitmap bmpText = BitmapFactory.decodeResource(getResources(), imageBackgroundText[currentIndex], opts);
                            Bitmap bmp = BitmapFactory.decodeResource(getResources(), imageBackground[currentIndex++], opts);

                            img_login_option_back.setImageBitmap(bmp);
                            textImage.setImageBitmap(bmpText);

                            if (currentIndex == (imageBackground.length)) {
                                currentIndex = 0;
                            }
                            img_login_option_back.postDelayed(runnable, 4000);

                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 0);
    }

    @Override
    public void onClick(View v) {
//        Intent intentAuthorize = new Intent(LoginOptionsActivity.this, AuthorizeActivity.class);
        AppSettingsBO appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
        boolean isAllowSignup = false;
        if (appSettingsBO != null && appSettingsBO.getAllow_signup().equals(Constants.IS_ALLOW_SIGNUP_TRUE)) {
            isAllowSignup = true;
        }

        switch (v.getId()) {
            case R.id.btn_login_email:
                startActivity(new Intent(LoginOptionsActivity.this, LoginArtisteActivity.class));
                break;

            case R.id.btn_login_facebook:
                //if (isAllowSignup) {
                // setFacebookLoginCallBack();
                // }
                fbLoginButton.performClick();
//                intentAuthorize.putExtra(AuthorizeActivity.ARG_PARAM, EnumUtils.Authentications.FACEBOOK.ordinal());
//                startActivity(intentAuthorize);
//                Utils.showToast(LoginOptionsActivity.this, getString(R.string.will_be_implemented_later));
                break;

            case R.id.btn_login_google:
                //if (isAllowSignup) {

                Log.d(TAG, "login click");
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                //setGoogleLoginCallBack();
                //}
//                intentAuthorize.putExtra(AuthorizeActivity.ARG_PARAM, EnumUtils.Authentications.GMAIL.ordinal());
//                startActivity(intentAuthorize);
//                Utils.showToast(LoginOptionsActivity.this, getString(R.string.will_be_implemented_later));
                break;

            case R.id.btn_login_wechat:
                Utils.showToast(LoginOptionsActivity.this, getString(R.string.will_be_implemented_later));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        img_login_option_back.removeCallbacks(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initAnimation();
    }

    private void showTimer(String startDate) {
        try {

            final long newStartDate = Long.parseLong(startDate);
            different_ = newStartDate;
            new CountDownTimer(newStartDate, 1000) {

                public void onTick(long millisUntilFinished) {
                    long different = different_;
                    long secondsInMilli = 1000;
                    long minutesInMilli = secondsInMilli * 60;
                    long hoursInMilli = minutesInMilli * 60;
                    long daysInMilli = hoursInMilli * 24;
                    long elapsedDays = different / daysInMilli;
                    different = different % daysInMilli;

                    long elapsedHours = different / hoursInMilli;
                    different = different % hoursInMilli;

                    long elapsedMinutes = different / minutesInMilli;
                    different = different % minutesInMilli;

                    long elapsedSeconds = different / secondsInMilli;
                    custom_progressDay.setMax(31);
                    custom_progressDay.setProgress((int) elapsedDays);
                    custom_progressHour.setMax(24);
                    custom_progressHour.setProgress((int) elapsedHours);
                    custom_progressMin.setMax(60);
                    custom_progressMin.setProgress((int) elapsedMinutes);
                    custom_progressSec.setMax(60);
                    custom_progressSec.setProgress((int) elapsedSeconds);
                    different_ = different_ - 1000;

                }

                public void onFinish() {
                    timerParent.setVisibility(View.GONE);
                    setControlsEnable(true);

                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setControlsEnable(boolean status) {
        btn_login_email.setEnabled(status);
        btn_login_facebook.setEnabled(status);
        btn_login_google.setEnabled(status);
        btn_login_wechat.setEnabled(status);
    }

    public void logOutFromFB() {
        LoginManager.getInstance().logOut();
    }

    public void logOutFromGmail() {
        FirebaseAuth.getInstance().signOut();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }

        } else if (mCallbackManager != null) {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse response) {
                        Log.d(TAG, "Facebook jsonObject: " + jsonObject);
                        Log.d(TAG, "Facebook response: " + response);
                        // Application code
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,cover,email");
        request.setParameters(parameters);
        request.executeAsync();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            logOutFromFB();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginOptionsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "Google Id:" + acct.getId());
        Log.d(TAG, "Google Email:" + acct.getEmail());
        Log.d(TAG, "Google Photo Url:" + acct.getPhotoUrl());
        Log.d(TAG, "Google Given Name:" + acct.getGivenName());
        Log.d(TAG, "Google Display Name:" + acct.getDisplayName());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginOptionsActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // METHOD FOR USER LOGIN NETWORK CALL
    private void userlogInNetworkCall(String id) {

        String url = "http://witkeyapp.com/witkey/api/wpgatewaay/" + id;
        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        });
        RequestQueue ExampleRequestQueue = Volley.newRequestQueue(this);
        ExampleRequestQueue.add(ExampleStringRequest);

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
                Log.d(TAG, "Result l " + taskItem.getResponse());
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


    // METHOD FOR// USER SIGNUP NETWORK CALL
    private void userSignUpNetworkCall(final Context context, final String userName, final String userEmail, final String userPassword) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        serviceParams.put(Keys.USER_NAME, userName);
        serviceParams.put(Keys.USER_EMAIL, userEmail);
        serviceParams.put(Keys.USER_PASSWORD, userPassword);
        serviceParams.put(Keys.USER_GENDER, "Male");

//        serviceParams.put(Keys.DEVICE_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)); // NOT USED YET
        serviceParams.put(Keys.DEVICE_NAME, Build.MANUFACTURER);
        serviceParams.put(Keys.DEVICE_OS_VERSION, Build.VERSION.RELEASE);
//        serviceParams.put(Keys.DEVICE_REGISTERATION_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        serviceParams.put(Keys.DEVICE_REGISTERATION_ID, UserSharedPreference.readRegToken());
        serviceParams.put(Keys.DEVICE_RESOLUTION, Utils.getScreenWidth(context) + " x " + Utils.getScreenHeight(context));
        serviceParams.put(Keys.DEVICE_TYPE, "2"); // (1 for IOS, 2 for andriod)
        serviceParams.put(Keys.DEVICE_USER_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));


        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.register,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {
                Log.d(TAG, "Result " + taskItem.getResponse());
                if (taskItem != null) {

                    if (taskItem.getResponse() != null) {
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(taskItem.getResponse());

                            if (jsonObject.has("errors")) {

                                userlogInNetworkCall(context, userEmail, userPassword);

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

                                    //clearAllFields();

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


}