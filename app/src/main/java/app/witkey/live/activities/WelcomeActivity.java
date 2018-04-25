package app.witkey.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.items.NotificationMessageBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnProceed)
    Button btnProceed;

    @BindView(R.id.welcomeParentLayout)
    LinearLayout welcomeParentLayout;
    @BindView(R.id.customTextView)
    CustomTextView customTextView;
    @BindView(R.id.customTextView2)
    CustomTextView customTextView2;
    NotificationMessageBO notificationMessageBO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        try {
            if (getIntent() != null) {
                notificationMessageBO = (NotificationMessageBO) getIntent().getParcelableExtra("MESSAGE");
            }
        } catch (Exception e) {
            LogUtils.e("WelcomeActivity", "" + e.getMessage());
        }

        initView();
        setViewSize();
    }

    private void initView() {
        if (notificationMessageBO != null) {
            customTextView.setText(notificationMessageBO.getName());
            customTextView2.setText(notificationMessageBO.getNotification_text());
        }
        btnProceed.setOnClickListener(this);
    }

    private void setViewSize() {
        int screenHeight = Utils.getScreenHeight(this);
        int screenWidth = Utils.getScreenWidth(this);
        if (screenHeight > 0) {
            screenHeight = (screenHeight / 5);
            screenWidth = screenWidth - 60;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, (screenHeight * 3));
            welcomeParentLayout.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnProceed:
                startActivity(new Intent(this, Dashboard.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
