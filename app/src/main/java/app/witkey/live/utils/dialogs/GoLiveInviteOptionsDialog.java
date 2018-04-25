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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.GoLiveOptionInviteAdapter;
import app.witkey.live.items.FeaturedBO;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 9/29/2017.
 */

public class GoLiveInviteOptionsDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.goLiveInviteForLive)
    CustomTextView goLiveInviteForLive;

    GoLiveOptionInviteAdapter goLiveOptionInviteAdapter;
    List<FeaturedBO> featuredList;

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

    public GoLiveInviteOptionsDialog() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_invite_options, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        setListeners();
        setUpData();
        setUpFeaturedRecycler(featuredList);

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

    private void setListeners() {
        goLiveInviteForLive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.goLiveInviteForLive:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                dismiss();
                break;
        }
    }

    private void setUpData() {
        featuredList = new ArrayList<>();

        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 1", "11 hrs ago", "10K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 2", "12 hrs ago", "11K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 3", "13 hrs ago", "12K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 4", "14 hrs ago", "13K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 5", "15 hrs ago", "14K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 6", "16 hrs ago", "15K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
        featuredList.add(new FeaturedBO("https://www.w3schools.com/w3css/img_avatar3.png", "Name 7", "17 hrs ago", "16K views", getString(R.string.welcome_desc), "https://www.w3schools.com/w3css/img_avatar3.png"));
    }

    private void setUpFeaturedRecycler(List<FeaturedBO> featuredList) {

        if (featuredList != null && featuredList.size() > 0) {

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            goLiveOptionInviteAdapter = new GoLiveOptionInviteAdapter(featuredList, getActivity(), mRecyclerView);
            mRecyclerView.setAdapter(goLiveOptionInviteAdapter);
        }

    }
}

