package app.witkey.live.fragments.dashboard.stream;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.PixelsOp;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyMissionEnergyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.dailyEnergyMissionLinearLayout)
    LinearLayout dailyEnergyMissionLinearLayout;

    @BindView(R.id.energyProgressBar)
    SeekBar energyProgressBar;

    @BindView(R.id.complete100EnergyTextView)
    CustomTextView complete100EnergyTextView;
    @BindView(R.id.energyValueTextView)
    CustomTextView energyValueTextView;
    @BindView(R.id.levelPreviousTextView)
    CustomTextView levelPreviousTextView;

    @BindView(R.id.complete500EnergyTextView)
    CustomTextView complete500EnergyTextView;
    @BindView(R.id.boxIV)
    ImageView boxIV;

    @BindView(R.id.tornadoIV)
    ImageView tornadoIV;
    int top = 100, down = 0;

    UserProgressDetailBO userProgressDetailBO;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static DailyMissionEnergyFragment newInstance() {
        Bundle args = new Bundle();
        DailyMissionEnergyFragment fragment = new DailyMissionEnergyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily_mission_energy, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
        } catch (Exception e) {
            LogUtils.e("DailyMissionEnergyFragment", "" + e.getMessage());
        }

        initProgressBar();
        initTextViews();
        initViews();
    }

    private void initViews() {
        dailyEnergyMissionLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                if (dailyEnergyMissionLinearLayout.getHeight() > Utils.getScreenHeight(getActivity())) {
//                    dailyEnergyMissionLinearLayout.setPadding(0, 0, 0, (int) PixelsOp.pxFromDp(getActivity(), 25f));
                    dailyEnergyMissionLinearLayout.setPadding(0, 0, 0, Utils.dp2px(getActivity(), 25));
                } else {
                    dailyEnergyMissionLinearLayout.setPadding(0, 0, 0, 0);
                }
            }
        });

        boxIV.setOnClickListener(this);
        tornadoIV.setOnClickListener(this);
    }

    // CHANGE TEXT SIZE OF SOME PART OF TEXT
    private void initTextViews() {
        SpannableString spannableString = SpannableString.valueOf(getString(R.string.complete_all_to_earn_your_100_energy_and_claim_10_tornados));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 26, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), 26, 36, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString spannableString1 = SpannableString.valueOf(getString(R.string.achieve_500_energy_to_unlock_your_mystery_gift));
        spannableString1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 8, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new RelativeSizeSpan(1.2f), 8, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        complete100EnergyTextView.setText(spannableString);
        complete500EnergyTextView.setText(spannableString1);

        energyValueTextView.setText(userProgressDetailBO.getDaily_mission_progress() + "");
        energyProgressBar.setProgress(userProgressDetailBO.getDaily_mission_progress());
        levelPreviousTextView.setText(userProgressDetailBO.getDaily_mission_progress() + "");

    }

    // REMOVE PADDING AND MAKE READ ONLY PROGRESS BAR
    private void initProgressBar() {
        try {
            energyProgressBar.setPadding(0, 0, 0, 0);
            energyProgressBar.setEnabled(false);
            tornadoIV.setColorFilter(Color.argb((int) Math.ceil(255.0 - ((255.0 / 100.0) * (double) userProgressDetailBO.getDaily_mission_progress())), 128, 128, 128));
            boxIV.setColorFilter(Color.argb((int) Math.ceil(255.0 - ((255.0 / 100.0) * (double) userProgressDetailBO.getDaily_mission_progress())), 128, 128, 128));

        } catch (Exception e) {
            LogUtils.e("DailyMissionEnergyFragment", "" + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tornadoIV:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.boxIV:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
        }
    }
}
