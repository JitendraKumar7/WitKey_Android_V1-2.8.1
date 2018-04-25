package app.witkey.live.adapters.dashboard.stream;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.witkey.live.R;

public class GoLiveSlideViewAdapter extends PagerAdapter {


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public void destroyItem(ViewGroup collection,int position,Object view){
        collection.removeView((View)view);
    }


    @Override
    public Object instantiateItem(View collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:
                resId = R.id.page_one;
                break;
            case 1:
                resId = R.id.page_two;
                break;
        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 2;
    }
}