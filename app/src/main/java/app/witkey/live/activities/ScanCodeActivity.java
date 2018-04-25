package app.witkey.live.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.witkey.live.R;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ScanCodeActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.backBN)
    CustomTextView backBN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        backBN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBN:
                finish();
                break;
        }
    }
}


