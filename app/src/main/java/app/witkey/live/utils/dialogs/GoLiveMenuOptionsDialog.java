package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import app.witkey.live.R;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 9/29/2017.
 */

public class GoLiveMenuOptionsDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.option1DialogSwitchScreen)
    ImageView option1DialogSwitchScreen;

    @BindView(R.id.option1DialogStickers)
    ImageView option1DialogStickers;

    @BindView(R.id.option1DialogShare)
    ImageView option1DialogShare;

    @BindView(R.id.option1DialogBeautyMood)
    ImageView option1DialogBeautyMood;

    @BindView(R.id.option1DialogFluentQuality)
    ImageView option1DialogFluentQuality;

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

    public GoLiveMenuOptionsDialog() {
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_menu_options, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        setListeners();

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
        option1DialogSwitchScreen.setOnClickListener(this);
        option1DialogStickers.setOnClickListener(this);
        option1DialogShare.setOnClickListener(this);
        option1DialogBeautyMood.setOnClickListener(this);
        option1DialogFluentQuality.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.option1DialogSwitchScreen:
                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("Switch"));
                dismiss();
                break;
            case R.id.option1DialogStickers:
//                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("ShowStickers"));/*FOR NOW OTHER WISE WORKING*/
                dismiss();
                break;
            case R.id.option1DialogShare:
                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("ShareDialog"));
                dismiss();
                break;
            case R.id.option1DialogBeautyMood:
                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("BeautyMood"));
                dismiss();
                break;
            case R.id.option1DialogFluentQuality:
                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("FluentQuality"));
                dismiss();
                break;
        }
    }
}

