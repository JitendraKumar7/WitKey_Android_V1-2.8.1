package app.witkey.live.fragments.dashboard.profile.profilesetting;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.customviews.CustomEditText;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnCheck)
    ImageView btnCheck;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.edtCurrentPassword)
    CustomEditText edtCurrentPassword;
    @BindView(R.id.edtAddNewPassword)
    CustomEditText edtAddNewPassword;
    @BindView(R.id.edtConfirmNewPassword)
    CustomEditText edtConfirmNewPassword;

    UserBO userBO;
    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
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
    }

    private void initViews() {
        btnBack.setOnClickListener(this);
        btnCheck.setOnClickListener(this);
        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.change_password);
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
                KeyboardOp.hide(getActivity());
                getActivity().onBackPressed();
                break;
            case R.id.btnCheck:
                checkUserCredentials();
                break;
        }
    }

    // METHOD TO VALIDATE ALL USER CHANGE PASS FIELDS AND MAKE NETWORK CALL
    private void checkUserCredentials() {

        String userNewPass = edtAddNewPassword.getText().toString().trim();
        String userCurrentPass = edtCurrentPassword.getText().toString().trim();
        String userConfirmNewPass = edtConfirmNewPassword.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(userConfirmNewPass)) {
            error = true;
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.setError(getString(R.string.this_field_is_required));

        } else if (userConfirmNewPass.length() < 6) {
            error = true;
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.setError(getString(R.string.password_length_short));
        }

        if (Validation.isEmpty(userNewPass)) {
            error = true;
            edtAddNewPassword.requestFocus();
            edtAddNewPassword.setError(getString(R.string.this_field_is_required));

        } else if (userNewPass.length() < 6) {
            error = true;
            edtAddNewPassword.requestFocus();
            edtAddNewPassword.setError(getString(R.string.password_length_short));
        }

        if (Validation.isEmpty(userCurrentPass)) {
            error = true;
            edtCurrentPassword.requestFocus();
            edtCurrentPassword.setError(getString(R.string.this_field_is_required));

        } else if (userCurrentPass.length() < 6) {
            error = true;
            edtCurrentPassword.requestFocus();
            edtCurrentPassword.setError(getString(R.string.password_length_short));
        }

        if (!userCurrentPass.isEmpty() && !userNewPass.isEmpty() && userCurrentPass.equals(userNewPass)) {
            error = true;
            edtAddNewPassword.requestFocus();
            edtAddNewPassword.setError(getString(R.string.old_pass_cant_new_pass));
            edtCurrentPassword.requestFocus();
            edtCurrentPassword.setError(getString(R.string.old_pass_cant_new_pass));
        }

        if (!userConfirmNewPass.isEmpty() && !userNewPass.isEmpty() && !userConfirmNewPass.equals(userNewPass)) {
            error = true;
            edtConfirmNewPassword.requestFocus();
            edtConfirmNewPassword.setError(getString(R.string.notifier_password_match));
        }

        if (error == false) {
            KeyboardOp.hide(getActivity());
            // TODO NETWORK CALL HERE
            changePasswordNetworkCall(getActivity(), userBO.getId(), userCurrentPass, userNewPass,
                    userConfirmNewPass, UserSharedPreference.readUserToken());
        }
    }

    // METHOD FOR USER CHANGE PASSWORD NETWORK CALL
    private void changePasswordNetworkCall(final Context context, String user_id, String oldPassword, String password,
                                           String password_confirmation, String token) {
        KeyboardOp.hide(context);
        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        serviceParams.put(Keys.USER_ID, user_id);
        serviceParams.put(Keys.OLD_PASSWORD, oldPassword);
        serviceParams.put(Keys.USER_PASSWORD, password);
        serviceParams.put(Keys.PASSWORD_CONFIRMATION, password_confirmation);

        tokenServiceHeaderParams.put(Keys.TOKEN, token);


        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.change_password,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
//                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        AlertOP.showAlert(context, null, getString(R.string.invalid_old_pass));
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                /*AlertOP.showAlert(context, "Alert", taskItem.getServiceStatusMessage(), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().onBackPressed();
                                        clearAllFields();
                                    }
                                });*/
                                SnackBarUtil.showSnackbar(context, getString(R.string.password_changes_success), false);
                                clearAllFields();
                                new ExitFragmentAsyncTask().execute();
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
        edtConfirmNewPassword.getText().clear();
        edtAddNewPassword.getText().clear();
        edtCurrentPassword.getText().clear();
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

}