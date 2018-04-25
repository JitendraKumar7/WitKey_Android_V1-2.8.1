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
import android.widget.Button;

import app.witkey.live.R;
import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/02/2017.
 */

public class BlockUserDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.btnBlockUser)
    Button btnBlockUser;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    int isBlock = 0;

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

    public BlockUserDialog() {

    }

    public static BlockUserDialog newInstance(int blockStatus) {
        Bundle args = new Bundle();
        args.putInt("ISBLOCK", blockStatus);
        BlockUserDialog fragment = new BlockUserDialog();
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

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_block_user, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        if (getArguments() != null) {
            isBlock = getArguments().getInt("ISBLOCK");
        }

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

        if (isBlock == 1) {
            btnBlockUser.setText(getString(R.string.un_block));
        } else {
            btnBlockUser.setText(getString(R.string.block_user));
        }
        btnBlockUser.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void setViewsPosition() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBlockUser:
                if (isBlock == 1) {
                    GlobalBus.getBus().post(new Events.BlockUserFragmentMessage("UnBlockUser"));
                } else {
                    GlobalBus.getBus().post(new Events.BlockUserFragmentMessage("BlockUser"));
                }
                dismiss();

                break;
            case R.id.btnCancel:
                dismiss();
                break;
        }
    }
}

