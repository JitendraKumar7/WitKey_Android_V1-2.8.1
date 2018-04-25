package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import app.witkey.live.R;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/03/2017.
 */

public class SignUpGenderDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.maleTextView)
    CustomTextView maleTextView;

    @BindView(R.id.femaleTextView)
    CustomTextView femaleTextView;
    String type;

    public SignUpGenderDialog() {

    }

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


    public static SignUpGenderDialog newInstance(String from) {
        Bundle args = new Bundle();
        args.putString("FROM", from);
        SignUpGenderDialog fragment = new SignUpGenderDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_gender, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);
        type = getArguments().getString("FROM");
        initView();

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


    // METHOD TO INITIALIZE VIEW
    private void initView() {
        maleTextView.setOnClickListener(this);
        femaleTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.maleTextView:
                if (type.equals("SIGNUP")) {
                    GlobalBus.getBus().post(new Events.FragmentToSignUpActivityMessage("Male"));
                } else {
                    GlobalBus.getBus().post(new Events.FragmentToProfileSettingMessage("Male"));
                }
                dismiss();
                break;
            case R.id.femaleTextView:
                if (type.equals("SIGNUP")) {
                    GlobalBus.getBus().post(new Events.FragmentToSignUpActivityMessage("Female"));
                } else {
                    GlobalBus.getBus().post(new Events.FragmentToProfileSettingMessage("Female"));
                }
                dismiss();
                break;
        }
    }
}

