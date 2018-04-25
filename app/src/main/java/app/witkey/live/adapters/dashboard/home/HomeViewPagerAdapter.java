package app.witkey.live.adapters.dashboard.home;

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
import android.widget.TextView;

import app.witkey.live.R;
import app.witkey.live.fragments.dashboard.home.HottestFragment;
import app.witkey.live.fragments.dashboard.home.NewGoddessMachoFragment;
import app.witkey.live.fragments.dashboard.home.TutorialFragment;
import app.witkey.live.utils.EnumUtils;


/**
 * created by developer on 9/21/2017.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;
    View tabView;
    TextView tabTextView;
    ImageView tabImageView;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    private HottestFragment hottestFragment;

    /**
     * Create pager adapter
     *
     * @param resources
     * @param fm
     */
    public HomeViewPagerAdapter(final Resources resources, FragmentManager fm) {
        super(fm);
        this.resources = resources;
    }

    public void setTabs(TabLayout tabs, Context context) {
        if (context == null) return;
//        tabs.getTabAt(0).setCustomView(setTabView(0, context));
        tabs.getTabAt(0).setCustomView(setTabView(0, context));
        tabs.getTabAt(1).setCustomView(setTabView(1, context));
        tabs.getTabAt(2).setCustomView(setTabView(2, context));
//        tabs.getTabAt(3).setCustomView(setTabView(3, context));
//        tabs.getTabAt(4).setCustomView(setTabView(4, context));
    }

    @Override
    public Fragment getItem(int position) {
        final Fragment result;
        switch (position) {
            /*case 0:
                result = new FeaturedFragment();
                break;*/
            case 0:
                hottestFragment = new HottestFragment();
                //result = NewGoddessMachoFragment.newInstance(EnumUtils.HomeScreenType.HOTTEST);
                result = hottestFragment;
                break;
//            case 1:
//                result = NewGoddessMachoFragment.newInstance(EnumUtils.HomeScreenType.NEW);
//                //result = new NewFragment();
//                break;
            case 1:
                result = NewGoddessMachoFragment.newInstance(EnumUtils.HomeScreenType.GODDESS);
                //result = new GoddessFragment();
                break;
            case 2:
                result = NewGoddessMachoFragment.newInstance(EnumUtils.HomeScreenType.MACHO);
                //result = new MachoFragment();
                break;
//            case 4:
//                result = new TutorialFragment();
//                break;
            default:
                result = new Fragment();
                break;
        }

        return result;
    }

    @Override
    public int getCount() {
        return 3;//6;
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

    public View setTabView(int position, Context context) {
        tabView = tabTextView = null;
        tabImageView = null;
        tabView = LayoutInflater.from(context).inflate(R.layout.custom_tab_home_top_new, null);
        tabTextView = (TextView) tabView.findViewById(R.id.customTabParent);
        //tabImageView = (ImageView) tabView.findViewById(R.id.customTabImage);
        tabTextView.setText(context.getString(getTabIcon(position, false)));
        //tabImageView.setImageResource(getTabIcon(position, true));
//        tabTextView.setCompoundDrawablesWithIntrinsicBounds(getTabIcon(position, true), 0, 0, 0);
        return tabView;
    }

    private int getTabIcon(int index, boolean type) {

        int[] iconsSelected = {
              //  R.drawable.featured,
                R.drawable.hotest,
                //R.drawable.new_,
                R.drawable.goddess,
                R.drawable.macho,
                //R.drawable.tutorial
        };
        int[] iconsUnselected = {
                R.drawable.inbox_off,
                R.drawable.inbox_off,
                R.drawable.inbox_off,
                R.drawable.inbox_off,
                R.drawable.inbox_off,
                R.drawable.inbox_off
        };

        int[] tabNames = {
                R.string.featured,
                //R.string.hottest,
                //R.string.new_,
                R.string.goddess,
                R.string.macho,
                //R.string.tutorial
        };

        if (type) {
            return iconsSelected[index];
        } else {
            return tabNames[index];
        }
    }

    public HottestFragment getHottestFragment() {
        return hottestFragment;
    }

    public void setHottestFragment(HottestFragment hottestFragment) {
        this.hottestFragment = hottestFragment;
    }
}
