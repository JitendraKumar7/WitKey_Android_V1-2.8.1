package app.witkey.live.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.fragments.dashboard.HomeFragment;
import app.witkey.live.fragments.dashboard.MessageFragment;
import app.witkey.live.fragments.dashboard.StreamFragment;
import app.witkey.live.fragments.dashboard.ProfileFragment;

public class TabActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private FragmentManager fm;
    private HomeFragment mainFragment1 = null;
    private StreamFragment mainFragment2 = null;
    private MessageFragment mainFragment3 = null;
    private ProfileFragment mainFragment4 = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    if (mainFragment1 == null) {
                        mainFragment1 = HomeFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, mainFragment1).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment1).commit();
                        hideOthersFragment(mainFragment1);
                    }
                    return true;
                case R.id.navigation_dashboard:

                    if (mainFragment2 == null) {
                        mainFragment2 = StreamFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, mainFragment2).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment2).commit();
                        hideOthersFragment(mainFragment2);
                    }
                    return true;
                case R.id.navigation_notifications:

                    if (mainFragment3 == null) {
                        mainFragment3 = MessageFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, mainFragment3).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment3).commit();
                        hideOthersFragment(mainFragment3);
                    }
                    return true;
                case R.id.ic_settings:

                    if (mainFragment4 == null) {
                        mainFragment4 = ProfileFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.container, mainFragment4).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment4).commit();
                        hideOthersFragment(mainFragment4);
                    }
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
//        mTextMessage = (TextView) findViewById(R.id.message);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (mainFragment1 == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, HomeFragment.newInstance()).commit();
        }

    }//onCreate

    private void hideOthersFragment(Fragment fragment) {

        if (fragment instanceof HomeFragment) {
            hideFragment(mainFragment2);
            hideFragment(mainFragment3);
            hideFragment(mainFragment4);
        } else if (fragment instanceof StreamFragment) {
            hideFragment(mainFragment1);
            hideFragment(mainFragment3);
            hideFragment(mainFragment4);
        } else if (fragment instanceof MessageFragment) {
            hideFragment(mainFragment1);
            hideFragment(mainFragment2);
            hideFragment(mainFragment4);

        } else if (fragment instanceof ProfileFragment) {
            hideFragment(mainFragment1);
            hideFragment(mainFragment2);
            hideFragment(mainFragment3);
        }
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragment).commit();
        }
    }

    public void goBackFragmentWithAnimation(int count) {
        // if count greater than zero popbackstack
        try {
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_out_to_left, R.anim.slide_out_to_right
                    , R.anim.slide_out_to_left, R.anim.slide_out_to_right);
            if (count > 0) {
                for (int i = 0; i < count; i++)
                    fm.popBackStack();
            }

            ft.commit();
        } catch (Exception e) {
        }
    }

    public Fragment getCurrentFragment() {
        Fragment fragment = (Fragment) fm.findFragmentById(R.id.container);
        return fragment;
    }

    public Fragment getVisibleFragment(Activity activity) {
        FragmentManager fragmentManager = (FragmentManager) fm;
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public void removeAllFragments() {
        try {
            fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeAllFragmentsImmediate() {
        try {
            fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void gotoNextFragment(Fragment fragment) {
        navigation.setVisibility(View.GONE);
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left, R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
        ft.replace(R.id.container, fragment);
        ft.addToBackStack("");
        ft.commitAllowingStateLoss();
    }

    public void gotoNextFragmentNoAnimation(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(0, 0);
        ft.add(R.id.container, fragment);
        ft.addToBackStack("");
        ft.commit();
    }

    public void goBackFragment(int count) {
        // if count greater than zero popbackstack
        try {
            if (count > 0) {
                for (int i = 0; i < count; i++)
                    fm.popBackStack();
            }
        } catch (Exception e) {
        }
    }

    public void gotoNextFragmentWithAnimation(Fragment fragment, int enterAnim, int exitAnim, int popEnter, int popExit) {
        try {
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(enterAnim, exitAnim
                    , popEnter, popExit);
            ft.replace(R.id.container, fragment);
            ft.addToBackStack("");
            ft.commit();
            //ft.commitAllowingStateLoss();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void gotoSkipFragment(Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left, R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
        ft.replace(R.id.container, fragment);
        //ft.addToBackStack(null);
        ft.commit();
    }

    public void removeFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.remove(fragment);
            fm.popBackStack();
        } catch (IllegalStateException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        //check if home page is not selected...
        if (fm.getBackStackEntryCount() == 0 && (navigation.getSelectedItemId() != R.id.navigation_home)) {
            setSelectedTab(0);
        } else {
            super.onBackPressed();
        }

    }

    public void hideBottomNavigation() {
        navigation.setVisibility(View.GONE);
    }

    public void showBottomNavigation() {
        navigation.setVisibility(View.VISIBLE);
    }

    public void setSelectedTab(int selectedTab) {
        switch (selectedTab) {
            case 0:
                navigation.setSelectedItemId(R.id.navigation_home);
                break;
            case 1:
                break;
        }

    }
}//main
