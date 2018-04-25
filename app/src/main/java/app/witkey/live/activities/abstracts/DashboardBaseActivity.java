package app.witkey.live.activities.abstracts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import app.witkey.live.R;

/**
 * created by developer on 9/21/2017.
 */

public abstract class DashboardBaseActivity extends AppCompatActivity {

    public void gotoNextFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left, R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
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
