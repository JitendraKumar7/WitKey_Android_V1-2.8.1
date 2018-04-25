package app.witkey.live.fragments.dashboard.profile.systemsetting;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.profilesetting.ProfileSettingFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;

public class FeedbackFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.natureOfFeedbackEditText)
    CustomTextView natureOfFeedbackEditText;
    @BindView(R.id.feedbackEditText)
    CustomEditText feedbackEditText;
    @BindView(R.id.emailContactEditText)
    CustomEditText emailContactEditText;
    @BindView(R.id.typeBroadcast)
    CustomTextView typeBroadcast;
    @BindView(R.id.typeGift)
    CustomTextView typeGift;
    @BindView(R.id.typeShare)
    CustomTextView typeShare;
    @BindView(R.id.typeValue)
    CustomTextView typeValue;
    @BindView(R.id.typeReward)
    CustomTextView typeReward;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    boolean typeBroadcast_ = false;
    boolean typeGift_ = false;
    boolean typeShare_ = false;
    boolean typeValue_ = false;
    boolean typeReward_ = false;

    private Context context;
    private String TAG = this.getClass().getSimpleName();
    UserBO userBO;

    public static FeedbackFragment newInstance() {
        Bundle args = new Bundle();
        FeedbackFragment fragment = new FeedbackFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setTitleBarData();
        showHideView();
        initViews();
        //setupUI(getView());
    }

    public void setupUI(View view) {

        if (!(view instanceof EditText) && view.getId() != btnSubmit.getId()) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    KeyboardOp.hide(getContext());
                }
            });
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    private void initViews() {

        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        typeBroadcast.setOnClickListener(this);
        typeGift.setOnClickListener(this);
        typeShare.setOnClickListener(this);
        typeValue.setOnClickListener(this);
        typeReward.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.feedback);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnSubmit:
                checkUserCredentials();
                break;
            case R.id.typeBroadcast:
                if (typeBroadcast_) {
                    typeBroadcast.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    typeBroadcast_ = false;
                } else {
                    typeBroadcast.setCompoundDrawablesWithIntrinsicBounds(null, null, getColorDrawable(true), null);
                    typeBroadcast_ = true;
                }
                break;
            case R.id.typeGift:
                if (typeGift_) {
                    typeGift.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    typeGift_ = false;
                } else {
                    typeGift.setCompoundDrawablesWithIntrinsicBounds(null, null, getColorDrawable(true), null);
                    typeGift_ = true;
                }
                break;
            case R.id.typeShare:
                if (typeShare_) {
                    typeShare.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    typeShare_ = false;
                } else {
                    typeShare.setCompoundDrawablesWithIntrinsicBounds(null, null, getColorDrawable(true), null);
                    typeShare_ = true;
                }
                break;
            case R.id.typeValue:
                if (typeValue_) {
                    typeValue.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    typeValue_ = false;
                } else {
                    typeValue.setCompoundDrawablesWithIntrinsicBounds(null, null, getColorDrawable(true), null);
                    typeValue_ = true;
                }
                break;
            case R.id.typeReward:
                if (typeReward_) {
                    typeReward.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    typeReward_ = false;
                } else {
                    typeReward.setCompoundDrawablesWithIntrinsicBounds(null, null, getColorDrawable(true), null);
                    typeReward_ = true;
                }
                break;

        }
    }

    private Drawable getColorDrawable(boolean type) {
        Drawable mDrawable = getActivity().getResources().getDrawable(R.drawable.ic_check_white_24dp);
        mDrawable.setColorFilter(ContextCompat.getColor(getActivity(), type ? R.color.witkey_orange : R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
        return mDrawable;
    }

    private String getTypeString() {
        String type = "";
        if (typeBroadcast_) {
            type = type + getString(R.string.broadcaster_problem) + ",";
        }
        if (typeGift_) {
            type = type + getString(R.string.gift_sending_problem) + ",";
        }
        if (typeShare_) {
            type = type + getString(R.string.unable_to_share) + ",";
        }
        if (typeValue_) {
            type = type + getString(R.string.stored_value_problem) + ",";
        }
        if (typeReward_) {
            type = type + getString(R.string.unable_to_collect_rewards);
        }
        return type;
    }

    // METHOD TO VALIDATE ALL USER SIGNUP FIELDS AND MAKE NETWORK CALL
    private void checkUserCredentials() {
        String userID = "";
        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userID = userBO.getId();
        } catch (Exception e) {

        }
        String feedback = feedbackEditText.getText().toString().trim();
        String email = emailContactEditText.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(feedback) && Validation.isEmpty(getTypeString())) {
            error = true;
            SnackBarUtil.showSnackbar(getActivity(), getString(R.string.feedback_error), false);
        }

        if (Validation.isEmpty(email)) {
            error = true;
            emailContactEditText.requestFocus();
            emailContactEditText.setError(getString(R.string.this_field_is_required));

        } else if (!Validation.isValidEmail(email)) {
            error = true;
            emailContactEditText.requestFocus();
            emailContactEditText.setError(getString(R.string.please_enter_valid_email));
        }

        if (error == false) {
            KeyboardOp.hide(getActivity());
            setUserFeedbackNetworkCall(getActivity(), getTypeString(), feedbackEditText.getText().toString(), emailContactEditText.getText().toString(), userID);
        }
    }


    //METHOD TO SEND USER FEEDBACK NETWORK CALL
    //Service name: feed-back
    //Type: Post
    //params: issues(all comma seprated issues), other_remarks{other issue}, user_id,email
    private void setUserFeedbackNetworkCall(final Context context, String issues, String other_remarks, String email, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.USER_EMAIL, email);
        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.ISSUEES, issues);
        serviceParams.put(Keys.OTHER_REMARKS, other_remarks);

        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.feed_back,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        // DO SOMETHING

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                SnackBarUtil.showSnackbar(context, "Feedback successfully sent", false);
                                new ExitFragmentAsyncTask().execute();
                                empyData();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // TASK EXIST ACTIVITY SLOWLY
    private class ExitFragmentAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            getActivity().onBackPressed();
        }
    }

    private void empyData() {
        feedbackEditText.getText().clear();
        emailContactEditText.getText().clear();
    }
}