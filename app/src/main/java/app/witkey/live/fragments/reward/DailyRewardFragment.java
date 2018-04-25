package app.witkey.live.fragments.reward;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.reward.DailyRewardAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.RewardBO;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyRewardFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.btnCollectRewards)
    Button btnCollectRewards;

    @BindView(R.id.dailyRewardParentLayout)
    LinearLayout dailyRewardParentLayout;

    @BindView(R.id.recyclerViewRewards)
    RecyclerView recyclerViewRewards;

    @BindView(R.id.customTextViewTitle)
    CustomTextView customTextViewTitle;

    private DailyRewardAdapter dailyRewardAdapter;
    private List<RewardBO> rewardBOList;

    public static DailyRewardFragment newInstance() {
        Bundle args = new Bundle();
        DailyRewardFragment fragment = new DailyRewardFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_daily_reward, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
        setViewSize();

        setUpData();
        setUpFeaturedRecycler(rewardBOList);

        //setUpAdapter();
    }

    private void initView() {
        btnCollectRewards.setOnClickListener(this);
    }

    private void setViewSize() {
        int screenHeight = Utils.getScreenHeight(getContext());
        int screenWidth = Utils.getScreenWidth(getContext());
        if (screenHeight > 0) {
            screenHeight = (screenHeight / 5);
            screenWidth = screenWidth - 60;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, (screenHeight * 3));
            dailyRewardParentLayout.setLayoutParams(layoutParams);
        }
    }

    private void setUpData() {

        rewardBOList = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            rewardBOList.add(new RewardBO());
        }
    }

    private void setUpFeaturedRecycler(final List<RewardBO> rewardBOList) {

        if (rewardBOList != null && rewardBOList.size() > 0) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
            recyclerViewRewards.setItemAnimator(new DefaultItemAnimator());
            recyclerViewRewards.setLayoutManager(gridLayoutManager);

            dailyRewardAdapter = new DailyRewardAdapter(rewardBOList, getContext(), recyclerViewRewards);
            recyclerViewRewards.setAdapter(dailyRewardAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCollectRewards:
                break;
        }
    }
}