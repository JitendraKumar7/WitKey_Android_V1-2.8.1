package app.witkey.live.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import app.witkey.live.utils.KeyboardOp;

public abstract class ToolbarFragment extends Fragment {

    private static boolean isActive;

    public abstract void refreshToolbar();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshToolbar();
        isActive = true;
    }

    @Override
    public void onPause() {
        isActive = false;
        super.onPause();
        KeyboardOp.hide(getActivity());
    }

    public static boolean isFragmentActive() {
        return isActive;
    }

}
