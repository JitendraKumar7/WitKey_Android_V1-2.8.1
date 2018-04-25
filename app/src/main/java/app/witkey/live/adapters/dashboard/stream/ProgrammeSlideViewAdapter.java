package app.witkey.live.adapters.dashboard.stream;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.witkey.live.R;
import app.witkey.live.items.ProgrammeBO;
import app.witkey.live.utils.Utils;

public class ProgrammeSlideViewAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    ArrayList<ProgrammeBO> programmeBOs;
    int currentPosition = 0;

    public ProgrammeSlideViewAdapter(Context context, ArrayList<ProgrammeBO> programmeBOs) {
        this.context = context;
        this.programmeBOs = programmeBOs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_programme_slider, container, false);
        //todo set scaletype properly
        final ImageView im_slider = view.findViewById(R.id.im_slider);

//        Utils.setImageSimpleWitkey(im_slider, programmeBOs.get(position).getImageUrl(), context);
        Glide.with(context)
                .load(programmeBOs.get(position).getImageUrl())
                .into(im_slider);

        //currentPosition = position;
        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onItemClick(view, position);
            }
        });

        if (position == programmeBOs.size() - 1) {
            im_slider.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        ((Activity) context).onBackPressed();
                        return super.onDoubleTap(e);
                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return programmeBOs.size();
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

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_slider:
                if (clickListener != null)
                    clickListener.onItemClick(v, currentPosition);
                break;
        }
    }*/

    /*Listeners*/
    ClickListeners clickListener;

    public void setClickListener(ClickListeners clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListeners {
        void onItemClick(View view, int position);
    }
}