package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;

import app.witkey.live.items.GiftPanelItemBO;
import app.witkey.live.utils.animations.BubbleViewAnimation;

/**
 * created by developer on 9/23/2017.
 */

public class GiftsSlideViewAdapter extends PagerAdapter {


    public static GiftPanelItemBO giftPanelBO;
    private LayoutInflater layoutInflater;
    Context context;
    List<GiftPanelItemBO> giftPanelBOList;
    List<List<GiftPanelItemBO>> giftPanelBOList_;
    GiftPanelAdapter giftPanelAdapter;
    BubbleViewAnimation bubbleViewAnimation;
    ArrayList<GiftPanelAdapter> giftPanelAdapters;
    Button btnSubmit;

    public GiftsSlideViewAdapter(Context context, List<GiftPanelItemBO> giftPanelBOList, BubbleViewAnimation bubbleViewAnimation, Button btnSubmit) {
        this.context = context;
        this.btnSubmit = btnSubmit;
        this.bubbleViewAnimation = bubbleViewAnimation;
        this.giftPanelBOList = giftPanelBOList;
        splitList(giftPanelBOList);
        giftPanelAdapters = new ArrayList<>();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_gift_slide, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.giftsSlideRecycler);

        recyclerView.setLayoutManager(new GridLayoutManager(context, 5, LinearLayoutManager.VERTICAL, false));

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        List<GiftPanelItemBO> giftItems = new ArrayList<>();
        giftItems.addAll(giftPanelBOList_.get(position));
        if (giftPanelAdapters.size() <= position) {
            giftPanelAdapters.add(new GiftPanelAdapter(giftItems, context, recyclerView, bubbleViewAnimation, btnSubmit));
        } else {
            giftPanelAdapters.set(position, new GiftPanelAdapter(giftItems, context, recyclerView, bubbleViewAnimation, btnSubmit));
        }
        recyclerView.setAdapter(giftPanelAdapters.get(position));

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return (int) Math.ceil((giftPanelBOList.size() / 10.0));
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    private void splitList(List<GiftPanelItemBO> giftPanelBOList) {

        giftPanelBOList_ = new ArrayList<>();
        if (giftPanelBOList != null && giftPanelBOList.size() > 0) {

            for (int start = 0; start < giftPanelBOList.size(); start += 10) {
                int end = Math.min(start + 10, giftPanelBOList.size());
                giftPanelBOList_.add(giftPanelBOList.subList(start, end));
            }
        }
    }

    public GiftPanelItemBO getGiftId() {
        return giftPanelBO;
    }

    public ArrayList<GiftPanelAdapter> getGiftPageList() {
        return this.giftPanelAdapters;
    }
}
