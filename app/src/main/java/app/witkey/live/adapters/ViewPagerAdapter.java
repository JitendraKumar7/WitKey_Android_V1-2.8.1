package app.witkey.live.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.fragments.dashboard.FavoritesFragment;
import app.witkey.live.fragments.dashboard.HomeFragment;
import app.witkey.live.fragments.dashboard.MessageFragment;
import app.witkey.live.fragments.dashboard.profile.UserProfileScrollViewFragment;
import app.witkey.live.utils.EnumUtils;


/**
 * created by developer on 9/21/2017.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;
    View tabView;
    ImageView tabIcon;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fm
     */
    public ViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            case 0:
                result = new HomeFragment();
                break;
            case 1:
                result = new FavoritesFragment();
                break;
            case 2:
                result = EnumUtils.getCurrentGoLiveFlow();
//                result = new GoLiveTokStartFragment();
//                    result = new GoLiveStreamerStartFragment();
//                    result = new GoLiveStartPageFragment();
                break;
            case 3:
                result = new MessageFragment();
                break;
            case 4:
                result = new UserProfileScrollViewFragment();//UserProfileAllInOneFragment();
                break;
            default:
                result = new Fragment();
                break;
        }
        return result;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        switch (position) {
            case 0:
                return "";//resources.getString(R.string.page_1);
            case 1:
                return "";//resources.getString(R.string.page_2);
            case 2:
                return "";//resources.getString(R.string.page_3);
            default:
                return "";
        }
    }

    /**
     * On each Fragment instantiation we are saving the reference of that Fragment in a Map
     * It will help us to retrieve the Fragment by position
     *
     * @param container
     * @param position
     * @return
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    /**
     * Remove the saved reference from our Map on the Fragment destroy
     *
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    /**
     * Get the Fragment by position
     *
     * @param position tab position of the fragment
     * @return
     */
    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    public void setTabs(TabLayout tabs, Context context, int height) {
        tabs.getTabAt(0).setCustomView(setTabView(0, context, height));
        tabs.getTabAt(1).setCustomView(setTabView(1, context, height));
        tabs.getTabAt(2).setCustomView(setTabView(2, context, height));
        tabs.getTabAt(3).setCustomView(setTabView(3, context, height));
    }

    public View setTabView(int position, Context context, int height) {
        tabView = tabIcon = null;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tabView = LayoutInflater.from(context).inflate(R.layout.custom_tab_home_bottom, null);
        tabIcon = (ImageView) tabView.findViewById(R.id.tabIcon);
        tabIcon.setLayoutParams(layoutParams);
        tabIcon.setImageResource(getTabIcon(position, true));
        return tabView;
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
