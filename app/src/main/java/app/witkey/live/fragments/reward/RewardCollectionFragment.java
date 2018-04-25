package app.witkey.live.fragments.reward;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardCollectionFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.btnCollectRewards)
    Button btnCollectRewards;

    @BindView(R.id.rewardCollectionParentLayout)
    LinearLayout rewardCollectionParentLayout;

    public static RewardCollectionFragment newInstance() {
        Bundle args = new Bundle();
        RewardCollectionFragment fragment = new RewardCollectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_rewards_collection, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initView();
        setViewSize();
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
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth, (screenHeight * 3));
            rewardCollectionParentLayout.getLayoutParams().height = (screenHeight * 3);
            rewardCollectionParentLayout.getLayoutParams().width = screenWidth;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCollectRewards:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
        }
    }
}