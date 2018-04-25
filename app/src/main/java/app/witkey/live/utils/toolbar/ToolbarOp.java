package app.witkey.live.utils.toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import app.witkey.live.R;
import app.witkey.live.activities.DrawerActivity;
import app.witkey.live.activities.TabActivity;
import app.witkey.live.utils.ColorOp;
import app.witkey.live.utils.PixelsOp;

public class ToolbarOp {

    public static int ToolbarContentHeight = 45;


    public static enum Theme {
        Dark, Light;
    }

    public static void hideToolbar(final View view, final Context context) {
        if (view != null) {
            view.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        final AppCompatActivity activity = (AppCompatActivity) context;
                        Toolbar tb = (Toolbar) activity
                                .findViewById(R.id.toolbar);
                        activity.setSupportActionBar(tb);
                        tb.removeAllViews();
                        tb.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public static void hideToolbarRetainViews(final View view, final Context context) {
        if (view != null) {
            view.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        final AppCompatActivity activity = (AppCompatActivity) context;
                        Toolbar tb = (Toolbar) activity
                                .findViewById(R.id.toolbar);
//                        activity.setSupportActionBar(tb);
                        tb.setVisibility(View.GONE);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public static void showToolbar(final View view, final Context context) {
        if (view != null) {
            view.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        final AppCompatActivity activity = (AppCompatActivity) context;
                        Toolbar tb = (Toolbar) activity
                                .findViewById(R.id.toolbar);
//                        activity.setSupportActionBar(tb);
                        tb.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public static void refresh(final View view, final Context context, final String title, final String subTitle,
                               final Theme theme, final int navResId,
                               final View.OnClickListener navIconListener,
                               final MenuItem... menuItems) {
        if (view != null) {
            view.post(new Runnable() {

                @Override
                public void run() {
                    try {
                        final AppCompatActivity activity = (AppCompatActivity) context;
                        View vForLightTheme = (View) activity
                                .findViewById(R.id.v_for_light_theme);
                        Toolbar tb = (Toolbar) activity
                                .findViewById(R.id.toolbar);
                        TextView tvTitle = new TextView(context);
                        tvTitle.setSingleLine(true);
                        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                        activity.setSupportActionBar(tb);
                        tb.removeAllViews();
                        tb.addView(tvTitle);

                        tb.setVisibility(View.VISIBLE);

                        getNavIcon(activity, tb, navResId, navIconListener);

                        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                                20);
                        tvTitle.setSingleLine(true);
//                        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
//                                "fonts/" + Constants.DEFAULT_FONT_NAME);
//                        tvTitle.setTypeface(typeface);
                        tvTitle.setText(title);

                        if (theme == Theme.Dark) {
                            vForLightTheme.setVisibility(View.GONE);

                            tvTitle.setTextColor(ColorOp.getColor(context,
                                    R.color.white));

                            tb.setBackgroundResource(R.color.colorPrimaryDark);
                        } else {
                            vForLightTheme.setVisibility(View.VISIBLE);
                            tb.setBackgroundResource(R.color.white);
                            tvTitle.setTextColor(ColorOp.getColor(context,
                                    R.color.black));
                        }

                        try {
                            if (menuItems.length > 0) {
                                for (int i = menuItems.length - 1; i >= 0; i--) {
                                    MenuItem menuItem = menuItems[i];
                                    if (menuItem instanceof MenuItemImgOrStr) {
                                        MenuItemImgOrStr mi = (MenuItemImgOrStr) menuItem;
                                        if (mi.getTitle() != null) {
                                            addMenuItemString(activity, tb, mi,
                                                    theme);
                                        } else if (isResTypeDrawable(context,
                                                mi.getResourceId())) {
                                            addMenuItemImage(activity, tb, mi);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    @SuppressLint("InflateParams")
    private static void addMenuItemString(AppCompatActivity activity,
                                          Toolbar tb, MenuItemImgOrStr menuItem, Theme theme) {
        TextView tv = (TextView) activity.getLayoutInflater().inflate(
                R.layout.menuitem_string, null);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, PixelsOp.convertToDensity(
                activity, ToolbarContentHeight));
        lp.gravity = Gravity.RIGHT;
        tv.setMinWidth(PixelsOp
                .convertToDensity(activity, ToolbarContentHeight));
        if (menuItem.getResourceId() == -1) {
            tv.setText(menuItem.getTitle());
        } else {
            tv.setText(activity.getResources().getString(menuItem.getResourceId()));
        }

        if (theme == Theme.Dark) {
            tv.setTextColor(ColorOp.getColor((Context) activity, R.color.white));
        } else {
            tv.setTextColor(ColorOp.getColor((Context) activity, R.color.black));
//            tv.setTextColor(ColorOp.getColor((Context) activity,
//                    Module.getColor(module)));
        }
        tv.setOnClickListener(menuItem.getOnClickListener());
        tb.addView(tv, lp);
    }

    @SuppressLint("InflateParams")
    private static void addMenuItemImage(AppCompatActivity activity,
                                         Toolbar tb, MenuItemImgOrStr menuItem) {
        ImageView iv = (ImageView) activity.getLayoutInflater().inflate(
                R.layout.menuitem_image, null);
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                PixelsOp.convertToDensity(activity, ToolbarContentHeight),
                PixelsOp.convertToDensity(activity, ToolbarContentHeight));
        lp.gravity = Gravity.RIGHT;
        iv.setImageResource(menuItem.getResourceId());
        iv.setOnClickListener(menuItem.getOnClickListener());
        tb.addView(iv, lp);
    }



    private static boolean isResTypeString(Context context, int resID) {
        String type = context.getResources().getResourceTypeName(resID);
        if (type.toLowerCase(Locale.getDefault()).equals("string")) {
            return true;
        }
        return false;
    }

    private static boolean isResTypeDrawable(Context context, int resID) {
        if(resID != -1){
            String type = context.getResources().getResourceTypeName(resID);
            if (type.toLowerCase(Locale.getDefault()).equals("drawable")) {
                return true;
            }
        }

        return false;
    }

    private static void getNavIcon(final AppCompatActivity activity,
                                   Toolbar tb, final int navResId,
                                   final View.OnClickListener navIconListener) {

        int count = activity.getSupportFragmentManager()
                .getBackStackEntryCount();

        if (activity instanceof DrawerActivity) {
            if(navResId == 0) {
                if (count == 0) {
                    tb.setNavigationIcon(R.drawable.ic_menu);
                    tb.setNavigationOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ((DrawerActivity) activity).openLeftDrawer();
                        }
                    });
                } else {
                    tb.setNavigationIcon(R.drawable.ic_arrow_back_white);
                    if (navIconListener == null) {
                        tb.setNavigationOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                ((DrawerActivity) activity).goBackFragment(1);
                            }
                        });
                    } else
                        tb.setNavigationOnClickListener(navIconListener);
                }
            } else {
                tb.setNavigationIcon(navResId);
                if (navIconListener == null) {
                    if (count == 0) {
                        tb.setNavigationOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((DrawerActivity) activity).onBackPressed();
                            }
                        });
                    } else {
                        tb.setNavigationOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((DrawerActivity) activity).goBackFragment(1);
                            }
                        });
                    }
                } else
                    tb.setNavigationOnClickListener(navIconListener);
            }
        }else if (activity instanceof TabActivity) {
            if(navResId == 0) {
                if (count == 0) {
                    tb.setNavigationIcon(null);
//                    tb.setNavigationIcon(R.drawable.ic_menu_48);
//                    tb.setNavigationOnClickListener(new_ View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            ((TabActivity) activity).openLeftDrawer();
//                        }
//                    });
                } else {
                    tb.setNavigationIcon(R.drawable.ic_arrow_back_white);
                    if (navIconListener == null) {
                        tb.setNavigationOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((TabActivity) activity).goBackFragment(1);

                            }
                        });
                    } else
                        tb.setNavigationOnClickListener(navIconListener);
                }
            } else {
                tb.setNavigationIcon(navResId);
                if (navIconListener == null) {
                    if (count == 0) {
                        tb.setNavigationOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((TabActivity) activity).onBackPressed();
                            }
                        });
                    } else {
                        tb.setNavigationOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((TabActivity) activity).goBackFragment(1);
                            }
                        });
                    }
                } else
                    tb.setNavigationOnClickListener(navIconListener);
            }
        }

    }

}
