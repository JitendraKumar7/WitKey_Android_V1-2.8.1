package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;

import app.witkey.live.R;
import app.witkey.live.utils.GlideRoundTransform;

/**
 * created by developer on 9/23/2017.
 */

public class MomentsSlideViewAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    Context context;
    ArrayList<String> image_arraylist;
    int currentPosition = 0;

    public MomentsSlideViewAdapter(Context context, ArrayList<String> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.layout_moment_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);


        Glide.with(context.getApplicationContext())
                .load(image_arraylist.get(position))
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.place_holder_videos) // optional
                .error(R.drawable.place_holder_videos)         // optional
                .transform(new CenterCrop(context.getApplicationContext()), new GlideRoundTransform(context,15))
                .into(im_slider);

        //currentPosition = position;
        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null)
                    clickListener.onItemClick(view, position);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
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