package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.GoLiveGiftReceivedAdapter;
import app.witkey.live.items.GiftReceivedBO;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveBroadcastGiftReceivedDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.giftsRecyclerView)
    RecyclerView giftsRecyclerView;

    @BindView(R.id.noGiftImageView)
    ImageView noGiftImageView;

    @BindView(R.id.noGiftMessageTextView)
    CustomTextView noGiftMessageTextView;

    @BindView(R.id.btnUse)
    Button btnUse;

    @BindView(R.id.giftsRecyclerViewParentFrame)
    RelativeLayout giftsRecyclerViewParentFrame;

    @BindView(R.id.giftTextView)
    CustomTextView giftTextView;
    @BindView(R.id.rewardsTextView)
    CustomTextView rewardsTextView;
    @BindView(R.id.bagsTextView)
    CustomTextView bagsTextView;

    private GoLiveGiftReceivedAdapter goLiveGiftReceivedAdapter;
    private List<GiftReceivedBO> giftReceivedBOs;

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            switch (newState) {
                case BottomSheetBehavior.STATE_DRAGGING:
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;
                case BottomSheetBehavior.STATE_SETTLING:
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    public GoLiveBroadcastGiftReceivedDialog() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_broadcast_gift_received, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        initViews();

        onClick(giftTextView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        ((View) bottomSheetView.getParent()).setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getDialog().getWindow();
            if (window != null)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initViews() {

        /*giftsRecyclerViewParentFrame.post(new Runnable() {
            @Override
            public void run() {
                giftsRecyclerViewParentFrame.getLayoutParams().height = (Utils.getScreenHeight(getContext()) / 5) * 2;
            }
        });*/

        giftTextView.setOnClickListener(this);
        rewardsTextView.setOnClickListener(this);
        bagsTextView.setOnClickListener(this);

        btnUse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.giftTextView:
                selectionToggle(1);
                setUpGiftData();
                setUpGiftReceivedRecycler(giftReceivedBOs);
                break;
            case R.id.rewardsTextView:
                selectionToggle(2);
                setUpRewardsData();
                setUpGiftReceivedRecycler(giftReceivedBOs);
                break;
            case R.id.bagsTextView:
                selectionToggle(3);
                setUpBagData();
                setUpGiftReceivedRecycler(giftReceivedBOs);
                break;
            case R.id.btnUse:
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
        }
    }

    private void setUpGiftData() {

        giftReceivedBOs = new ArrayList<>();

        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.guiter, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.stars, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.gift_new, ""));
    }

    private void setUpRewardsData() {

        giftReceivedBOs = new ArrayList<>();

        /*giftReceivedBOs.add(new GiftReceivedBO(R.drawable.guiter, "2 DAYS\nLEFT"));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.stars, "2 HOURS\nLEFT"));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));
        giftReceivedBOs.add(new GiftReceivedBO(R.drawable.bag, ""));*/
    }

    private void setUpBagData() {

        giftReceivedBOs = new ArrayList<>();
    }

    private void setUpGiftReceivedRecycler(List<GiftReceivedBO> giftReceivedBOList) {

        if (giftReceivedBOList != null && giftReceivedBOList.size() > 0) {

            showNoResult(false);

            giftsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5, LinearLayoutManager.VERTICAL, false));

            giftsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            goLiveGiftReceivedAdapter = new GoLiveGiftReceivedAdapter(giftReceivedBOList, getActivity(), giftsRecyclerView);
            giftsRecyclerView.setAdapter(goLiveGiftReceivedAdapter);
        } else {
            showNoResult(true);
        }
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            giftsRecyclerView.setVisibility(View.INVISIBLE);
            btnUse.setVisibility(View.INVISIBLE);
            noGiftImageView.setVisibility(View.VISIBLE);
            noGiftMessageTextView.setVisibility(View.VISIBLE);
        } else {
            giftsRecyclerView.setVisibility(View.VISIBLE);
            btnUse.setVisibility(View.VISIBLE);
            noGiftImageView.setVisibility(View.INVISIBLE);
            noGiftMessageTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void selectionToggle(int indexType) {
        giftTextView.setTextColor(indexType == 1 ? getResources().getColor(R.color.witkey_orange) : getResources().getColor(R.color.white));
        rewardsTextView.setTextColor(indexType == 2 ? getResources().getColor(R.color.witkey_orange) : getResources().getColor(R.color.white));
        bagsTextView.setTextColor(indexType == 3 ? getResources().getColor(R.color.witkey_orange) : getResources().getColor(R.color.white));
    }
}

