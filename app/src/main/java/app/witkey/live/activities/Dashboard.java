package app.witkey.live.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.google.gson.Gson;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.abstracts.DashboardBaseActivity;
import app.witkey.live.fragments.MainFragment;

import app.witkey.live.fragments.dashboard.profile.MyFansFragment;
import app.witkey.live.items.StreamBO;
import app.witkey.live.stream.GoLiveStreamActivity;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.LogUtils;
import butterknife.ButterKnife;

/**
 * created by developer on 9/21/2017.
 */

public class Dashboard extends DashboardBaseActivity {

    //private SoftInputAssist softInputAssist;
    private FragmentManager fm;
    String actionType = "", jsonData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        try {
            fm = getSupportFragmentManager();
            //softInputAssist = new SoftInputAssist(this);

            ButterKnife.bind(this);
            Constants.APP_IS_LIVE = true;
//            addMainFragment();

            if (getIntent() != null && getIntent().getExtras() != null) {
                actionType = getIntent().getExtras().getString("TYPE");
                jsonData = getIntent().getExtras().getString("JSON_DATA");

                if (!TextUtils.isEmpty(actionType)) {
                    if (actionType.equals(EnumUtils.NOTIFICATION_TYPES.TYPE_FOLLOW)) {
                        addMainFragment(2);
                    } else if (actionType.equals(EnumUtils.NOTIFICATION_TYPES.TYPE_STREAM_CREATED) && !TextUtils.isEmpty(jsonData)) {
                        addMainFragment(1);
                    } else {
                        addMainFragment(0);
                    }
                } else {
                    addMainFragment(0);
                }
            } else {
                addMainFragment(0);
            }
        } catch (Exception e) {
            LogUtils.e("Dashboard", "" + e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        //softInputAssist.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //softInputAssist.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //softInputAssist.onDestroy();
        LogUtils.e("onDestroy", "DASHBOARD");
        super.onDestroy();
    }

    private void addMainFragment(int index) {

        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer, MainFragment.newInstance());
        ft.commit();
        /*HANDEL STREAM CREATION TIME HERE*/
        if (index == 1) {
            try {
                StreamBO streamBO = new Gson().fromJson(jsonData, StreamBO.class);
                startActivity(new Intent(Dashboard.this, GoLiveStreamActivity.class)
                        .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_2)
                        .putExtra(GoLiveActivity.ARG_PARAM_3, streamBO));
            } catch (Exception e) {
                LogUtils.e("proceed-", "" + e.getMessage());
            }
        } else if (index == 2) {
            gotoNextFragment(MyFansFragment.newInstance());
        }
    }

    public void goBackFragment(int count) {
        // if count greater than zero popbackstack
        try {
            if (count > 0) {
                KeyboardOp.hide(Dashboard.this);
                for (int i = 0; i < count; i++)
                    fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        if (getCurrentFragment() instanceof MainFragment) {
            MainFragment mainFragment = (MainFragment) getCurrentFragment();
            if (mainFragment.getSelectedPage() != 0) {
                mainFragment.setSelectedPage(0);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

    public Fragment getCurrentFragment() {
        Fragment fragment = (Fragment) fm.findFragmentById(R.id.fragmentContainer);
        return fragment;
    }
}
