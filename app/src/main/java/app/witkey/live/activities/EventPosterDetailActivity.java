package app.witkey.live.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import app.witkey.live.R;
import app.witkey.live.activities.abstracts.BaseActivity;
import app.witkey.live.adapters.dashboard.stream.ProgrammeSlideViewAdapter;
import app.witkey.live.items.ProgrammeBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventPosterDetailActivity extends BaseActivity implements View.OnClickListener {

    public static final String PROGRAMME_LIST = "programmeList";
    public static final String INDEX = "listIndex";

    @BindView(R.id.rl_poster_container)
    RelativeLayout rl_poster_container;
    @BindView(R.id.imv_placeholder)
    ImageView imv_placeholder;

    @BindView(R.id.closeImageView)
    LinearLayout closeImageView;

    @BindView(R.id.programmeImagesVP)
    ViewPager programmeImagesVP;

    @BindView(R.id.pageIndicators)
    LinearLayout pageIndicators;
    private ImageView[] imageIndicators;

    private ArrayList<ProgrammeBO> programmeBOs;
    private int listIndex = -1;
    private ProgrammeSlideViewAdapter programmeSlideViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_poster_detail);

        ButterKnife.bind(this);

        try {
            programmeBOs = getIntent().getParcelableArrayListExtra(PROGRAMME_LIST);
            listIndex = getIntent().getIntExtra(INDEX, listIndex);
            setListeners();
            handleData();
        } catch (Exception e) {
            LogUtils.e("EventPosterDetailActivity", "" + e.getMessage());
        }
    }

    private void setListeners() {
        closeImageView.setOnClickListener(this);
    }

    private void setUpProgrammeViewPager() {
        programmeSlideViewAdapter = new ProgrammeSlideViewAdapter(this, programmeBOs);
        programmeImagesVP.setOffscreenPageLimit(4);
        programmeImagesVP.setAdapter(programmeSlideViewAdapter);

        imageIndicators = new ImageView[]{};
        imageIndicators = addIndicators(this, pageIndicators, programmeBOs.size());

        programmeImagesVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                // position_ = position;
                moveIndicator(EventPosterDetailActivity.this, programmeBOs.size(), imageIndicators, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (listIndex > -1 && listIndex < programmeBOs.size()) {
            programmeImagesVP.setCurrentItem(listIndex);
        }
    }

    private static void moveIndicator(Context context, int totalCount, ImageView[] imageViews, int currentPosition) {
        try {
            for (int i = 0; i < totalCount; i++) {
                imageViews[i].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_un_selected));
            }
            imageViews[currentPosition].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_selected));
        } catch (Exception e) {
            LogUtils.e("moveIndicator", "" + e.getMessage());
        }
    }

    private static ImageView[] addIndicators(Context mContext, LinearLayout view, int count) {
        ImageView[] imageViews = new ImageView[count];
        view.removeAllViews();

        for (int i = 0; i < count; i++) {
            imageViews[i] = new ImageView(mContext);
            imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_un_selected));

            int circleSize = Utils.dp2px(mContext, 7);
            int circleMargen = Utils.dp2px(mContext, 3);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(circleSize, circleSize);
            params.setMargins(circleMargen, 0, circleMargen, 0);

            view.addView(imageViews[i], params);
        }

        imageViews[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_selected));
        return imageViews;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.closeImageView:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe // METHOD TO GET EVENT MESSAGE
    public void getMessage(Events.EventPosterActivityMessage eventPosterActivityMessage) {

        if (programmeBOs == null) {
            programmeBOs = eventPosterActivityMessage.getProgrammeBOs();
            handleData();
        }
    }

    private void handleData() {
        if (programmeBOs != null && programmeBOs.size() > 0) {
            rl_poster_container.setVisibility(View.VISIBLE);
            imv_placeholder.setVisibility(View.GONE);

            setUpProgrammeViewPager();
        } else {
            rl_poster_container.setVisibility(View.GONE);
            imv_placeholder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


