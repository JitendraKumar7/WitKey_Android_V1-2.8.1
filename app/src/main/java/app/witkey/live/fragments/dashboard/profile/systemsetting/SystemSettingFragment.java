package app.witkey.live.fragments.dashboard.profile.systemsetting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.activities.LoginOptionsActivity;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.stream.LiveFeedEndedFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.StreamEndSummaryBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SystemSettingFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.blockListParentFrame)
    LinearLayout blockListParentFrame;
    @BindView(R.id.notificationsParentFrame)
    LinearLayout notificationsParentFrame;
    @BindView(R.id.clearCacheParentFrame)
    LinearLayout clearCacheParentFrame;
    @BindView(R.id.feedbackParentFrame)
    LinearLayout feedbackParentFrame;
    @BindView(R.id.aboutUsParentFrame)
    LinearLayout aboutUsParentFrame;

    @BindView(R.id.btnLogOut)
    Button btnLogOut;

    @BindView(R.id.blockListIV)
    ImageView blockListIV;
    @BindView(R.id.notificationsIV)
    ImageView notificationsIV;
    @BindView(R.id.clearCacheIV)
    ImageView clearCacheIV;
    @BindView(R.id.feedbackIV)
    ImageView feedbackIV;
    @BindView(R.id.aboutUsIV)
    ImageView aboutUsIV;
    UserBO userBO;

    private Dialog dialogLogout;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static SystemSettingFragment newInstance() {
        Bundle args = new Bundle();
        SystemSettingFragment fragment = new SystemSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_system_setting, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            initLogoutDialog();
            setTitleBarData();
            showHideView();
            initViews();
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        } catch (Exception e) {
            LogUtils.e("onViewCreated", "" + e.getMessage());
        }
    }

    private void initLogoutDialog() {
        dialogLogout = new Dialog(getContext());
        dialogLogout.setContentView(R.layout.dialog_system_setting_logout);
        dialogLogout.setCanceledOnTouchOutside(true);
        if (dialogLogout.getWindow() != null) {
            dialogLogout.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        Button btnLogOutYes = (Button) dialogLogout.findViewById(R.id.btnLogOutYes);
        Button btnLogOutLater = (Button) dialogLogout.findViewById(R.id.btnLogOutLater);

        btnLogOutYes.setOnClickListener(this);
        btnLogOutLater.setOnClickListener(this);
    }

    private void initViews() {

        btnBack.setOnClickListener(this);

        blockListParentFrame.setOnClickListener(this);
        notificationsParentFrame.setOnClickListener(this);
        clearCacheParentFrame.setOnClickListener(this);
        feedbackParentFrame.setOnClickListener(this);
        aboutUsParentFrame.setOnClickListener(this);

        btnLogOut.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.system_setting);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void showLogoutDialog() {
        if (dialogLogout != null)
            dialogLogout.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.blockListParentFrame:
                Animations.buttonBounceAnimation(getActivity(), blockListIV);
                gotoNextFragment(BlockListFragment.newInstance());
                break;
            case R.id.notificationsParentFrame:
                Animations.buttonBounceAnimation(getActivity(), notificationsIV);
                gotoNextFragment(NotificationsFragment.newInstance());
                break;
            case R.id.clearCacheParentFrame:
                Animations.buttonBounceAnimation(getActivity(), clearCacheIV);
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.feedbackParentFrame:
                Animations.buttonBounceAnimation(getActivity(), feedbackIV);
                gotoNextFragment(FeedbackFragment.newInstance());
                break;
            case R.id.aboutUsParentFrame:
                Animations.buttonBounceAnimation(getActivity(), aboutUsIV);
                gotoNextFragment(AboutUsFragment.newInstance());
                break;

            case R.id.btnLogOut:
                showLogoutDialog();
                break;
            case R.id.btnLogOutYes:
                if (userBO != null) {
                    logOutNetworkCall(getActivity(), userBO.getId(), UserSharedPreference.readRegToken());
                    clearUserData();
                    startActivity(new Intent(getActivity(), LoginOptionsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    getActivity().finish();
                }
                break;
            case R.id.btnLogOutLater:
                if (dialogLogout != null)
                    dialogLogout.dismiss();
                break;
        }
    }

    private void clearUserData() {
        if (LoginManager.getInstance() != null) {
            LoginManager.getInstance().logOut();
        }
        UserSharedPreference.saveIsUserLoggedIn(false);
    }

    // METHOD TO MAKE NETWORK CALL TO END CURRENT STREAM
    //service name: logout, type : Post
    private void logOutNetworkCall(final Context context, String userID, String regideractionID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        serviceParams.put(Keys.DEVICE_REGISTERATION_ID, regideractionID);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.logout,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        /* DO SOME THING*/
                        //AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                               /* DO SOME THING*/
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        // if response is successful then do something
                    }
                }
            }
        });
    }
}