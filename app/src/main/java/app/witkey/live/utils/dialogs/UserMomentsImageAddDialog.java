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
import android.widget.Button;

import app.witkey.live.R;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/02/2017.
 */

public class UserMomentsImageAddDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.btnPickFromGallery)
    Button btnPickFromGallery;
    @BindView(R.id.btnPickFromCamera)
    Button btnPickFromCamera;

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

    public UserMomentsImageAddDialog() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_user_moments_image_add, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        setViewsPosition();
        initViews();

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
        btnPickFromGallery.setOnClickListener(this);
        btnPickFromCamera.setOnClickListener(this);
    }

    private void setViewsPosition() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPickFromGallery:
                dismiss();
                GlobalBus.getBus().post(new Events.UserMomentsFragmentMessage("GALLERY"));
                break;
            case R.id.btnPickFromCamera:
                dismiss();
                GlobalBus.getBus().post(new Events.UserMomentsFragmentMessage("CAMERA"));
                break;
        }
    }
}

