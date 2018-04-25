package app.witkey.live.activities;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class Activities {

    public static void goBackFragment(Context context, int count) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).goBackFragment(count);
        } else if (activity instanceof Dashboard) {
            ((Dashboard) activity).goBackFragment(count);
        }
    }

    public static Fragment getVisibleFragment(Context context) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            return ((TabActivity) activity).getVisibleFragment(activity);
        } else if (activity instanceof DrawerActivity) {
            return ((DrawerActivity) activity).getVisibleFragment(activity);
        }
        return null;
    }

    public static Fragment getCurrentFragment(Context context) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            return ((TabActivity) activity).getCurrentFragment();
        } else if (activity instanceof Dashboard) {
            return ((Dashboard) activity).getCurrentFragment();
        }
        return null;
    }

    public static void goBackFragmentWithAnimation(Context context, int count) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).goBackFragmentWithAnimation(count);
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).goBackFragmentWithAnimation(count);
        }
    }

    public static void removeAllFragments(Context context) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).removeAllFragments();
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).removeAllFragments();
        }
    }

    public static void removeAllFragmentsImmediate(Context context) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).removeAllFragmentsImmediate();
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).removeAllFragmentsImmediate();
        }
    }

    public static void gotoNextFragment(Context context, Fragment fragment) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).hideBottomNavigation();
            ((TabActivity) activity).gotoNextFragment(fragment);
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).lockDrawer();
            ((DrawerActivity) activity).gotoNextFragment(fragment);
        }
    }

    public static void gotoNextFragmentNoAnimation(Context context, Fragment fragment) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).gotoNextFragmentNoAnimation(fragment);
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).gotoNextFragmentNoAnimation(fragment);
        }
    }

    public static void gotoNextFragmentWithAnimation(Context context, Fragment fragment,
                                                     int enterAnim, int exitAnim, int popEnter,
                                                     int popExit) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).gotoNextFragmentWithAnimation(fragment, enterAnim, exitAnim, popEnter, popExit);
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).gotoNextFragmentWithAnimation(fragment, enterAnim, exitAnim, popEnter, popExit);
        }
    }

    public static void gotoSkipFragment(Context context, Fragment fragment) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).gotoSkipFragment(fragment);
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).gotoSkipFragment(fragment);
        }
    }

    public static void removeFragment(Context context, Fragment fragment) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).removeFragment(fragment);
        } else if (activity instanceof DrawerActivity) {
            ((DrawerActivity) activity).removeFragment(fragment);
        }
    }


    // this method is used to change status bar color and navigation bar color
    public static void changeThemeColor(Context context, int navigationBarColor, int statusBarColor) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(navigationBarColor);
            window.setStatusBarColor(statusBarColor);
        }
    }

    public static void hideBottomNavigation(Context context, boolean isHide) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            if (isHide) {
                ((TabActivity) activity).hideBottomNavigation();
            } else {
                ((TabActivity) activity).showBottomNavigation();
            }
        }
    }

    public static void lockDrawer(Context context, boolean lock) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof DrawerActivity) {
            if (lock) {
                ((DrawerActivity) activity).lockDrawer();
            } else {
                ((DrawerActivity) activity).unlockDrawer();
            }
        }
    }

    public static void setSelectedTab(Context context, int pageIndex) {
        AppCompatActivity activity = ((AppCompatActivity) context);
        if (activity instanceof TabActivity) {
            ((TabActivity) activity).setSelectedTab(pageIndex);
        }
    }
}
