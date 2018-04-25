package app.witkey.live.fragments.dashboard.profile;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.adapters.dashboard.profile.HowToIncreaseRankingAdapter;
import app.witkey.live.items.HowToIncreaseRankingBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BroadcastRankActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.smallIcon)
    ImageView smallIcon;
    @BindView(R.id.broadcasterRankImageView)
    ImageView broadcasterRankImageView;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.levelCurrentTextView)
    CustomTextView levelCurrentTextView;
    @BindView(R.id.broadcasterRankCustomTextView)
    CustomTextView broadcasterRankCustomTextView;
    @BindView(R.id.diamondsCountBoxTextView)
    CustomTextView diamondsCountBoxTextView;
    @BindView(R.id.levelPreviousTextView)
    CustomTextView levelPreviousTextView;
    @BindView(R.id.goldLevelNextTextView)
    CustomTextView goldLevelNextTextView;

    @BindView(R.id.broadcastRankingProgressBar)
    SeekBar broadcastRankingProgressBar;

    @BindView(R.id.howToIncreaseRankingRecyclerView)
    RecyclerView howToIncreaseRankingRecyclerView;

    private HowToIncreaseRankingAdapter howToIncreaseRankingAdapter;
    private List<HowToIncreaseRankingBO> howToIncreaseRankingBOs;

    UserProgressDetailBO userProgressDetailBO;
    int userCurrentLevel = 1;
    UserLevelBO userLevelBO;

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_ranking);

        ButterKnife.bind(this);

        try {
            userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
            userCurrentLevel = userProgressDetailBO.getArtist_level();
            userLevelBO = Utils.getBroadcasterLevel(userCurrentLevel);
        } catch (Exception e) {
            LogUtils.e("UserLevelFragment", "" + e.getMessage());
        }

        initProgressBar();
        setTitleBarData();
        showHideView();

        initViews();

        setUpHowToIncreaseRankingData();
        setUpHowToIncreaseRankingRecycler(howToIncreaseRankingBOs);
    }

    private void initProgressBar() {
        broadcastRankingProgressBar.setPadding(0, 0, 0, 0);
        broadcastRankingProgressBar.setEnabled(false);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.broadcaster_rank);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void setUpHowToIncreaseRankingData() {

        howToIncreaseRankingBOs = new ArrayList<>();

        howToIncreaseRankingBOs.add(new HowToIncreaseRankingBO(getString(R.string.how_to_increase_your_ranking)));
        howToIncreaseRankingBOs.add(new HowToIncreaseRankingBO(getString(R.string.every_hour_of_broadcast_earns_10)));
//        howToIncreaseRankingBOs.add(new HowToIncreaseRankingBO(getString(R.string.organise_an_event_to_earn_50)));
        howToIncreaseRankingBOs.add(new HowToIncreaseRankingBO(getString(R.string.every_fan_that_follows_you_earn_10)));
        howToIncreaseRankingBOs.add(new HowToIncreaseRankingBO(getString(R.string.each_popularity_award_recieved_earns_you_50)));
    }

    private void setUpHowToIncreaseRankingRecycler(final List<HowToIncreaseRankingBO> howToIncreaseRankingBOList) {

        if (howToIncreaseRankingBOList != null && howToIncreaseRankingBOList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            howToIncreaseRankingRecyclerView.setItemAnimator(new DefaultItemAnimator());
            howToIncreaseRankingRecyclerView.setLayoutManager(linearLayoutManager);
            howToIncreaseRankingAdapter = new HowToIncreaseRankingAdapter(howToIncreaseRankingBOList, this, howToIncreaseRankingRecyclerView);
            howToIncreaseRankingRecyclerView.setAdapter(howToIncreaseRankingAdapter);
        } else {
            //showNoResult(true);
        }
    }

    private void initViews() {

        try {
            String currentLevelTorandos = "520";
            String currentLevelTornadosFrom = " / 1000";
            String tornadosText = " TORNADOS";

            SpannableString spannableStringCurrentLevelTornados = new SpannableString(userProgressDetailBO.getArtist_current_tornados() + currentLevelTornadosFrom + tornadosText);
            spannableStringCurrentLevelTornados.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.witkey_orange)), 0, (userProgressDetailBO.getArtist_current_tornados() + "").length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            levelCurrentTextView.setText(spannableStringCurrentLevelTornados);

            broadcasterRankCustomTextView.setText(userProgressDetailBO.getArtist_level() + "");
            diamondsCountBoxTextView.setText(userProgressDetailBO.getArtist_level() + "");
            levelPreviousTextView.setText(getString(R.string.level) + " " + userProgressDetailBO.getArtist_level());
            goldLevelNextTextView.setText(getString(R.string.level) + " " + (userProgressDetailBO.getArtist_level() + 1));

            broadcastRankingProgressBar.setProgress(userProgressDetailBO.getArtist_current_tornados());

            broadcasterRankImageView.setImageResource(userLevelBO.getLevelLocalImage());
            smallIcon.setImageResource(userLevelBO.getLevelLocalImage());
        } catch (Exception e) {
            LogUtils.e("initViews", "" + e.getMessage());
        }
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                finish();
                break;
        }
    }
}