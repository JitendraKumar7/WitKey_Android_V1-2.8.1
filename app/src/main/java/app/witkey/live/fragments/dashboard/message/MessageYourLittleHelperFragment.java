package app.witkey.live.fragments.dashboard.message;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.message.MessageAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.MessageBO;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageYourLittleHelperFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.btnBack)
    ImageView btnBack;

    @BindView(R.id.mToolbarTitle)
    CustomTextView mToolbarTitle;

    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noMomentsParentFrame)
    LinearLayout noMomentsParentFrame;

    MessageAdapter messageAdapter;
    List<MessageBO> messageBOList;

    String selectionType;

    public static MessageYourLittleHelperFragment newInstance() {
        Bundle args = new Bundle();
        MessageYourLittleHelperFragment fragment = new MessageYourLittleHelperFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_message_your_little_helper, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        showHideView();
        initView();
        setUpData();
//        setUpFeaturedRecycler(messageBOList);
    }

    private void setUpData() {
        messageBOList = new ArrayList<>();

        /*messageBOList.add(new MessageBO("11:20", "Sep 20", getString(R.string.welcome_desc) + getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("14:30", "Sep 21", getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("11:40", "Sep 22", getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("04:50", "Sep 23", getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("01:60", "Sep 24", getString(R.string.welcome_desc) + getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("10:20", "Sep 25", getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("12:40", "Sep 26", getString(R.string.welcome_desc) + getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("13:30", "Sep 27", getString(R.string.welcome_desc), selectionType));
        messageBOList.add(new MessageBO("14:20", "Sep 28", getString(R.string.welcome_desc) + getString(R.string.welcome_desc), selectionType));*/
    }

   /* private void setUpFeaturedRecycler(List<MessageBO> messageBOList) {

        if (messageBOList != null && messageBOList.size() > 0) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            messageAdapter = new MessageAdapter(usermessageBOList, context, mRecyclerView);
            mRecyclerView.setAdapter(messageAdapter);
        } else {
            showNoResult(true);
        }

    }*/

    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
            noMomentsParentFrame.setVisibility(View.VISIBLE);
        } else {
            noMomentsParentFrame.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void initView() {

        selectionType = getString(R.string.your_little_helper);
        mToolbarTitle.setText(selectionType);
        btnBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
        }
    }
}