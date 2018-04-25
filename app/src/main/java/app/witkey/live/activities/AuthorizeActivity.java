package app.witkey.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import app.witkey.live.R;
import app.witkey.live.utils.EnumUtils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthorizeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SocialLogin";
    @BindView(R.id.twitter_login_btn)
    TwitterLoginButton twitterLoginButton;

    @BindView(R.id.fb_login_button)
    LoginButton fbLoginButton;

    @BindView(R.id.google_login_button)
    Button googleLoginButton;

    //fb callback
    private CallbackManager callbackManager;
    //google
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    EnumUtils.Authentications authentications;
    public static final String ARG_PARAM = "authenticationFrom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            int id = getIntent().getIntExtra(ARG_PARAM, 0);
            authentications = EnumUtils.Authentications.getAuthentication(id);
        }
        checkAuthenticationType();
    }//onCreate


    private void checkAuthenticationType() {
        switch (authentications) {
            case TWITTER:
                setTwitterLoginCallBack();
                twitterLoginButton.performClick();
                break;
            case FACEBOOK:
                setFacebookLoginCallBack();
                fbLoginButton.performClick();
                break;
            case GMAIL:
                setGoogleLoginCallBack();
                googleLoginButton.performClick();
                break;
        }
    }

    //************************************************************Google Login***********************************************//

    //set google login  config
    private void setGoogleLoginCallBack() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    //call intent for google sign in
    @OnClick(R.id.google_login_button)
    void callGoogleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String token = acct.getIdToken();
            showToast("Google success: " + token);
            Log.i("", "Google success: " + token);
            signOut();
            finish();
        } else {
            finish();
        }
    }

    //call google logout
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Log.i("status", "" + status);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    //************************************************************Facebook***********************************************//

    private void setFacebookLoginCallBack() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        callbackManager = CallbackManager.Factory.create();

        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                String userId = loginResult.getAccessToken().getUserId();
                showToast("Facebook success: " + token);
                Log.i("", "Facebook success: " + token);
                LogOutFromFB();
                finish();
            }

            @Override
            public void onCancel() {
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                finish();
            }
        });

    }

    public void LogOutFromFB() {
        LoginManager.getInstance().logOut();
    }

    //************************************************************Twitter***********************************************//

    private void setTwitterLoginCallBack() {
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = result.data;
//              TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                showToast("Twitter success: " + token + "Twitter secret: " + secret);
                Log.i("", "Twitter success: " + token + "Twitter secret: " + secret);
                finish();
            }

            @Override
            public void failure(TwitterException exception) {
                finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        } else {
            if (callbackManager != null) {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, "" + message, Toast.LENGTH_LONG).show();
    }


}//main
