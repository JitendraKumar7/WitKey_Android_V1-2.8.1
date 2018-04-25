package app.witkey.live.fragments.dashboard.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.home.TutorialViewPagerAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TutorialScreenViewFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.btn_skip)
    Button btn_skip;

    @BindView(R.id.btn_next)
    Button btn_next;

    boolean tutorialType = false;
    boolean nextButtonType = false;

    TutorialViewPagerAdapter tutorialViewPagerAdapter;

    public static TutorialScreenViewFragment newInstance(boolean type) {
        Bundle args = new Bundle();
        args.putBoolean("TYPE", type);
        TutorialScreenViewFragment fragment = new TutorialScreenViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tutorial_screen_view, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getBoolean("TYPE")) {
                tutorialType = true;
            } else {
                tutorialType = false;
            }
        }

        initView();
        initAdapter(tutorialType);
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        btn_skip.setOnClickListener(this);
        btn_next.setOnClickListener(this);
    }

    // METHOD TO INITIALIZE ADAPTER
    private void initAdapter(final boolean type) {
        tutorialViewPagerAdapter = new TutorialViewPagerAdapter(getActivity(), getImages(type));
        viewPager.setAdapter(tutorialViewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if ((position + 1) == getImages(type).length) {
                    updateNextButton(true);
                } else {
                    updateNextButton(false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // METHOD TO GET ALL IMAGES LIST
    private int[] getImages(boolean type) {
        int imagesAllApp[] = {R.drawable.car, R.drawable.car, R.drawable.car, R.drawable.car};
        int imagesGoLive[] = {R.drawable.car, R.drawable.car, R.drawable.car, R.drawable.car};

        if (type) {
            return imagesAllApp;
        } else {
            return imagesGoLive;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (nextButtonType) {
                    getActivity().onBackPressed();
                } else {
                    moveNext();
                }
                break;
            case R.id.btn_skip:
                getActivity().onBackPressed();
                break;
        }
    }

    // METHOD TO MOVE TO THE NEXT PAGE
    private void moveNext() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private void updateNextButton(boolean status) {
        if (status) {
            nextButtonType = true;
            btn_next.setText(R.string.finish);
        } else {
            nextButtonType = false;
            btn_next.setText(R.string.next);
        }
    }
}