package app.witkey.live.fragments.dashboard.profile;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.HowToLevelUpAdapter;
import app.witkey.live.adapters.dashboard.profile.UserLevelAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.HowToLevelUpBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class UserLevelFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.userLevelImageView)
    ImageView userLevelImageView;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.userLevelRecyclerView)
    RecyclerView userLevelRecyclerView;

    @BindView(R.id.userLevelCustomTextView)
    CustomTextView userLevelCustomTextView;
    @BindView(R.id.userLevelCurrentProgressTextView)
    CustomTextView userLevelCurrentProgressTextView;
    @BindView(R.id.goldLevelPreviousTextView)
    CustomTextView goldLevelPreviousTextView;
    @BindView(R.id.goldLevelNextTextView)
    CustomTextView goldLevelNextTextView;

    @BindView(R.id.userLevelProgressBar)
    SeekBar userLevelProgressBar;

    private UserLevelAdapter userLevelAdapter;
    private List<UserLevelBO> userLevelBOs;

    @BindView(R.id.howToLevelUpRecyclerView)
    RecyclerView howToLevelUpRecyclerView;

    UserProgressDetailBO userProgressDetailBO;

    private HowToLevelUpAdapter howToLevelUpAdapter;
    private List<HowToLevelUpBO> howToLevelUpBOs;

    private Context context;
    private String TAG = this.getClass().getSimpleName();
    int userCurrentLevel = 1;
    UserLevelBO userLevelBO;

    public static UserLevelFragment newInstance() {
        Bundle args = new Bundle();
        UserLevelFragment fragment = new UserLevelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_level, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
            userCurrentLevel = userProgressDetailBO.getUser_level();
            userLevelBO = Utils.getUserLevel(userCurrentLevel);
        } catch (Exception e) {
            LogUtils.e("UserLevelFragment", "" + e.getMessage());
        }

        initProgressBar();
        setTitleBarData();
        showHideView();

        initViews();
        setUpUserLevelData();
        setUpUserLevelRecycler(userLevelBOs);

        setUpHowToLevelUpData();
        setUpHowToLevelUpRecycler(howToLevelUpBOs);
    }

    private void initProgressBar() {
        userLevelProgressBar.setPadding(0, 0, 0, 0);
        userLevelProgressBar.setEnabled(false);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.user_level);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void setUpUserLevelData() {

        userLevelBOs = new ArrayList<>();

        userLevelBOs.add(new UserLevelBO("1 - 9", R.drawable.user_level_1_9));
        userLevelBOs.add(new UserLevelBO("10 - 29", R.drawable.user_level_10_29));
        userLevelBOs.add(new UserLevelBO("30 - 49", R.drawable.user_level_30_49));
        userLevelBOs.add(new UserLevelBO("50 - 99", R.drawable.user_level_50_99));
        userLevelBOs.add(new UserLevelBO("100 - 149", R.drawable.user_level_100_149));
        userLevelBOs.add(new UserLevelBO("150 - 199", R.drawable.user_level_150_199));
        userLevelBOs.add(new UserLevelBO("200 - 499", R.drawable.user_level_200_499));
    }

    private void setUpUserLevelRecycler(final List<UserLevelBO> userLevelBOList) {

        if (userLevelBOList != null && userLevelBOList.size() > 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 7, GridLayoutManager.VERTICAL, false);

            //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

            userLevelRecyclerView.setItemAnimator(new DefaultItemAnimator());
            userLevelRecyclerView.setLayoutManager(gridLayoutManager);
            userLevelAdapter = new UserLevelAdapter(userLevelBOList, getContext(), userLevelRecyclerView);
            userLevelRecyclerView.setAdapter(userLevelAdapter);
        } else {
            //showNoResult(true);
        }
    }

    private void setUpHowToLevelUpData() {

        howToLevelUpBOs = new ArrayList<>();

        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.how_to_level_up), ""));
        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.just_login_to_view), getString(R.string.earn_5_tornados_for_every_5_min_viewing)));
        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.just_share_the_broadcaster_to_friends), getString(R.string.earn_5_tornados_for_3_shares)));
        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.simply_send_gifts_to_your), getString(R.string.earn_10_tornados_for_every_1000_gifts)));
        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.just_invite_friends_to_view_broadcasters), getString(R.string.earn_10_tornados_for_every_3_invites)));
        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.recharge_your_chips_to_top_up), getString(R.string.earn_20_tornados_for_Every_50_purchase)));
//        howToLevelUpBOs.add(new HowToLevelUpBO(getString(R.string.participate_in_a_current_event), getString(R.string.earn_5_tornados_for_every_event)));
    }

    private void setUpHowToLevelUpRecycler(final List<HowToLevelUpBO> howToLevelUpBOList) {

        if (howToLevelUpBOList != null && howToLevelUpBOList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            howToLevelUpRecyclerView.setItemAnimator(new DefaultItemAnimator());
            howToLevelUpRecyclerView.setLayoutManager(linearLayoutManager);
            howToLevelUpAdapter = new HowToLevelUpAdapter(howToLevelUpBOList, getContext(), howToLevelUpRecyclerView);
            howToLevelUpRecyclerView.setAdapter(howToLevelUpAdapter);
        } else {
            //showNoResult(true);
        }
    }

    private void initViews() {

        SpannableString spannableStringGoldLevel = new SpannableString(userLevelBO.getLevelTitle() + " " + getString(R.string.level) + " " + userCurrentLevel);
        spannableStringGoldLevel.setSpan(new StyleSpan(Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringGoldLevel.setSpan(new StyleSpan(Typeface.BOLD), userLevelBO.getLevelTitle().length() + 1, spannableStringGoldLevel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        userLevelCustomTextView.setText(spannableStringGoldLevel);

        SpannableString spannableStringCurrentLevel = new SpannableString(userProgressDetailBO.getUser_current_tornados() + " / 100");
        spannableStringCurrentLevel.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), (userProgressDetailBO.getUser_current_tornados() + "").length() + 1, spannableStringCurrentLevel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userLevelCurrentProgressTextView.setText(spannableStringCurrentLevel);

        goldLevelPreviousTextView.setText(userLevelBO.getLevelTitle() + " " + getString(R.string.level) + " " + (userCurrentLevel));
        goldLevelNextTextView.setText(userLevelBO.getLevelTitle() + " " + getString(R.string.level) + " " + (userCurrentLevel + 1));

        userLevelImageView.setImageResource(userLevelBO.getLevelLocalImage());
        userLevelProgressBar.setProgress(userProgressDetailBO.getUser_current_tornados());
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
        }
    }
}