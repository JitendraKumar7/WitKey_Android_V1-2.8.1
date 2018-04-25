package app.witkey.live.activities.abstracts;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import app.witkey.live.R;

public abstract class BaseActivity extends AppCompatActivity {

    // REPLACE FRAGMENT IN CONTAINER
    public void gotoNextFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left, R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
        ft.replace(R.id.fragmentContainer, fragment);
        ft.addToBackStack("");
        ft.commitAllowingStateLoss();
    }
}
