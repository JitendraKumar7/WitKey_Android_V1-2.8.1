package app.witkey.live.fragments.abstracts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import app.witkey.live.R;
import app.witkey.live.fragments.dashboard.stream.NonArtistGoLiveOverlayFragment;
import app.witkey.live.interfaces.OnBackPressListener;


public abstract class BaseFragment extends Fragment implements OnBackPressListener {
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("JKS", this.getClass().getSimpleName());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * All listeners must be initialized here
     * <p/>
     * Will be call when in onViewCreated()
     **/
//    protected abstract void setListeners();
    protected void setActionBar(ActionBar titleBar) {

    }

    protected void showLoader(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
        }

        progressDialog.setMessage("Loading please wait... ");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    protected void hideLoader() {
        if (progressDialog != null) {
            progressDialog.hide();

            progressDialog = null;
        }

    }

    @Override
    public boolean onBackPressed() {
       /* return new FragmentBackPressImpl(this) {
            @Override
            public void onChildFragmentPop() {

            }
        }.onBackPressed();*/
        return true;
    }

    public void gotoNextFragment(Fragment fragment) {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

        if (fragment instanceof NonArtistGoLiveOverlayFragment) {
            ft.setCustomAnimations(R.anim.slide_in_from_bottom,
                    0, 0, R.anim.slide_out_to_bottom);

            if (getActivity().findViewById(R.id.main_layout) != null)
                getActivity().findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.grey_dark));
        } else {
            ft.setCustomAnimations(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left, R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right);

            if (getActivity().findViewById(R.id.main_layout) != null)
                getActivity().findViewById(R.id.main_layout).setBackgroundColor(getResources().getColor(R.color.witkey_dim_white));
        }

        ft.replace(R.id.fragmentContainer, fragment);
        ft.addToBackStack("");
        ft.commitAllowingStateLoss();
    }

    protected int getTabIcon(int index, boolean type) {

        int[] iconsSelected = {
                R.drawable.home,
                R.drawable.mic,
                R.drawable.inbox,
                R.drawable.profile
        };

        int[] iconsUnselected = {
                R.drawable.inbox_off,
                R.drawable.inbox_off,
                R.drawable.inbox_off,
                R.drawable.inbox_off
        };

        if (type) {
            return iconsSelected[index];
        } else {
            return iconsUnselected[index];

        }

    }


}

