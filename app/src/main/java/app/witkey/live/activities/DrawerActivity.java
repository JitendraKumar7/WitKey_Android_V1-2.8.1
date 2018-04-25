package app.witkey.live.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.fragments.dashboard.HomeFragment;
import app.witkey.live.fragments.dashboard.MessageFragment;
import app.witkey.live.fragments.dashboard.StreamFragment;
import app.witkey.live.fragments.dashboard.ProfileFragment;


public class DrawerActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private DrawerLayout drawer;
    private FragmentManager fm;
    private Context context;

    private HomeFragment mainFragment1 = null;
    private StreamFragment mainFragment2 = null;
    private MessageFragment mainFragment3 = null;
    private ProfileFragment mainFragment4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        context = DrawerActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setNavigationHeader();

        //Set drawer menu click listeners...
        setDrawerMenuListener();

        //Set Home fragment
        if (mainFragment1 == null) {
            mainFragment1 = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_dashboard, mainFragment1).commit();
        }

    }

    private void setNavigationHeader() {

        LinearLayout llHeader = (LinearLayout) findViewById(R.id.ll_drawer_header);
        TextView tvUserName = (TextView) findViewById(R.id.tv_username);
        TextView tvAccount = (TextView) findViewById(R.id.tv_myaccount);

        llHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawers();

            }
        });
    }

    private void setDrawerMenuListener() {
        RadioButton rbHome = (RadioButton) findViewById(R.id.rb_home);
        RadioButton rbContact = (RadioButton) findViewById(R.id.rb_contact);
        RadioButton rbAboutUs = (RadioButton) findViewById(R.id.rb_about_us);
        RadioButton rbLogout = (RadioButton) findViewById(R.id.rb_logout);

        rbHome.setOnCheckedChangeListener(this);
        rbContact.setOnCheckedChangeListener(this);
        rbAboutUs.setOnCheckedChangeListener(this);
        rbLogout.setOnCheckedChangeListener(this);

        rbHome.setOnClickListener(this);
        rbContact.setOnClickListener(this);
        rbAboutUs.setOnClickListener(this);
        rbLogout.setOnClickListener(this);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        int id = compoundButton.getId();

        switch (id) {
            case R.id.rb_home:
                if (checked) {
                    if (mainFragment1 == null) {
                        mainFragment1 = new HomeFragment();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.content_dashboard, mainFragment1).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment1).commit();
                        hideOthersFragment(mainFragment1);
                    }
                }
                break;
            case R.id.rb_contact:
                if (checked) {
                    if (mainFragment2 == null) {
                        mainFragment2 = StreamFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.content_dashboard, mainFragment2).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment2).commit();
                        hideOthersFragment(mainFragment2);
                    }
                }
                break;
            case R.id.rb_about_us:
                if (checked) {
                    if (mainFragment3 == null) {
                        mainFragment3 = MessageFragment.newInstance();
                        getSupportFragmentManager().beginTransaction()
                                .add(R.id.content_dashboard, mainFragment3).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .show(mainFragment3).commit();
                        hideOthersFragment(mainFragment3);
                    }
                }
                break;
            case R.id.rb_logout:
                if (checked) {
                    //signOutFromGoogle();
                    Intent intent = new Intent(DrawerActivity.this, LoginArtisteActivity.class);
                    startActivity(intent);
                    finish();

                }
                break;
        }
    }

    private void hideFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragment).commit();
        }
    }

    private void hideOthersFragment(Fragment fragment) {

        if (fragment instanceof HomeFragment) {

            hideFragment(mainFragment2);
            hideFragment(mainFragment3);
            hideFragment(mainFragment4);

        } else if (fragment instanceof StreamFragment) {

            hideFragment(mainFragment3);
            hideFragment(mainFragment1);
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openLeftDrawer() {
        try {
            drawer.openDrawer(Gravity.LEFT);
        } catch (Exception e) {
        }
    }

    public void closeDrawers() {
        try {
            drawer.closeDrawers();
        } catch (Exception e) {
        }
    }


    public void lockDrawer() {
        try {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                    Gravity.LEFT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unlockDrawer() {
        try {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);
        } catch (Exception e) {
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
    public void onClick(View view) {
        closeDrawers();
    }


}
