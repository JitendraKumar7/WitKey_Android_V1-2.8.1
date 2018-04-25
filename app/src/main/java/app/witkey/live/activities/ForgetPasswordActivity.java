package app.witkey.live.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomEditText;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/03/2017.
 */

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.mBtnSubmitForget)
    Button mBtnSubmitForget;

    @BindView(R.id.edtEmailAddressForget)
    CustomEditText edtEmailAddressForget;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
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
        mBtnSubmitForget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnSubmitForget:
                checkUserCredentials();
                break;
        }
    }

    // METHOD TO VALIDATE ALL USER FORGETPASSWORD FIELDS AND MAKE NETWORK CALL
    private void checkUserCredentials() {

        String email = edtEmailAddressForget.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(email)) {
            error = true;
            edtEmailAddressForget.requestFocus();
            edtEmailAddressForget.setError(getString(R.string.this_field_is_required));

        } else if (!Validation.isValidEmail(email)) {
            error = true;
            edtEmailAddressForget.requestFocus();
            edtEmailAddressForget.setError(getString(R.string.please_enter_valid_email));
        }

        if (error == false) {
            KeyboardOp.hide(this);
            userLogInNetworkCall(this, email);
        }
    }

    // METHOD FOR USER FORGOT PASSWORD NETWORK CALL
    private void userLogInNetworkCall(final Context context, String userEmail) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        serviceParams.put(Keys.USER_EMAIL, userEmail);

        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.forgot_password,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    try {
                        if (taskItem.isError()) {
                            JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                            JSONObject jsonError = jsonObject.getJSONObject("errors");

                            if (jsonError.has("email")) {
                                edtEmailAddressForget.requestFocus();
                                edtEmailAddressForget.setError(getString(R.string.email_not_found));
                            } else {
                                AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                            }
                        } else {

                            if (taskItem.getResponse() != null) {
                                // SnackBarUtil.showSnackbar(context, getString(R.string.email_sent), false);
                                clearAllFields();

                                AlertOP.showAlert(context, null, getString(R.string.email_sent), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        onBackPressed();
                                    }
                                });

                                //new ExitActiviyAsyncTask().execute();
                             /* Intent intent = new Intent(context, LoginArtisteActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();*/
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    // TASK EXIST ACTIVITY SLOWLY
    private class ExitActiviyAsyncTask extends AsyncTask<String, String, String> {

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
            onBackPressed();
        }
    }

    // METHOD TO CLEAR ALL SCREEN FIELDS
    private void clearAllFields() {
        edtEmailAddressForget.getText().clear();
    }
}


