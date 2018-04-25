package app.witkey.live.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.Validation;
import app.witkey.live.utils.customviews.CustomEditText;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 9/20/2017.
 */

public class LoginUserActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.edtEmailAddress)
    CustomEditText edtEmailAddress;

    @BindView(R.id.edtPassword)
    CustomEditText edtPassword;

    @BindView(R.id.mBtnLogin)
    Button mBtnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnLogin:
                checkUserCredentials();
                break;
        }
    }

    private void checkUserCredentials() {

        String email = edtEmailAddress.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        boolean error = false;

        if (Validation.isEmpty(password)) {
            error = true;
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.this_field_is_required));
        } else if (password.length() < 6) {
            error = true;
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.must_be_at_least_six_characters_long));
        }
        if (Validation.isEmpty(email)) {
            error = true;
            edtEmailAddress.requestFocus();
            edtEmailAddress.setError(getString(R.string.this_field_is_required));

        } else if (!Validation.isValidEmail(email)) {
            error = true;
            edtEmailAddress.requestFocus();
            edtEmailAddress.setError(getString(R.string.please_enter_valid_email));
        }

        if (error == false) {
            KeyboardOp.hide(this);
            // TODO CALL TO ARTISTE SIGN IN API HERE
            Utils.showToast(this, getString(R.string.will_be_implemented_later));
        }

    }
}

