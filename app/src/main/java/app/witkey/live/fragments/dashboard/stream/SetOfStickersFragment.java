package app.witkey.live.fragments.dashboard.stream;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.StickersAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.items.StickerBO;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SetOfStickersFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.userCancelImageView)
    ImageView userCancelImageView;

    @BindView(R.id.stickersRecyclerView)
    RecyclerView stickersRecyclerView;

    private StickersAdapter stickersAdapter;
    private List<StickerBO> stickerBOList;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static SetOfStickersFragment newInstance() {
        Bundle args = new Bundle();
        SetOfStickersFragment fragment = new SetOfStickersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_set_of_stickers, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initViews();

        setUpData();
        setUpStickersRecycler(stickerBOList);

    }

    private void initViews() {
        userCancelImageView.setOnClickListener(this);
    }

    private void setUpData() {
        stickerBOList = new ArrayList<>();

        stickerBOList.add(new StickerBO(R.drawable.stickers1));
        stickerBOList.add(new StickerBO(R.drawable.stickers2));
        stickerBOList.add(new StickerBO(R.drawable.stickers3));
        stickerBOList.add(new StickerBO(R.drawable.stickers4));
        stickerBOList.add(new StickerBO(R.drawable.stickers5));
        stickerBOList.add(new StickerBO(R.drawable.stickers6));
        stickerBOList.add(new StickerBO(R.drawable.stickers7));
    }

    private void setUpStickersRecycler(List<StickerBO> stickerBOList) {

        if (stickerBOList != null && stickerBOList.size() > 0) {
            stickersRecyclerView.setLayoutManager(new GridLayoutManager(context, 3,
                    LinearLayoutManager.VERTICAL, false));
            stickersRecyclerView.setItemAnimator(new DefaultItemAnimator());
            stickersAdapter = new StickersAdapter(stickerBOList, context, stickersRecyclerView);
            stickersRecyclerView.setAdapter(stickersAdapter);
            stickersAdapter.setClickListener(new StickersAdapter.ClickListeners() {
                @Override
                public void onRowClick(int position) {
                    goBack();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userCancelImageView:
                goBack();
                break;
        }
    }

    private void goBack() {
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}