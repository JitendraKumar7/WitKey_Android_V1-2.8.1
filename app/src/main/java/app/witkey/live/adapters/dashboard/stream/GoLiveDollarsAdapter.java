package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveDollarsAdapter extends RecyclerView.Adapter {

    private List<UserBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public GoLiveDollarsAdapter(List<UserBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItem, totalItemCount;
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mViewerSummaryParentFrame)
        RelativeLayout mViewerSummaryParentFrame;

        @BindView(R.id.viewerImageView)
        ImageView viewerImageView;
        @BindView(R.id.viewerTextView)
        CustomTextView viewerTextView;

        @BindView(R.id.viewerRankImageView)
        ImageView viewerRankImageView;
        @BindView(R.id.viewerRankTextView)
        CustomTextView viewerRankTextView;

        @BindView(R.id.viewerCashImageView)
        ImageView viewerCashImageView;
        @BindView(R.id.viewerCashTextView)
        CustomTextView viewerCashTextView;
        @BindView(R.id.userChipsCount)
        CustomTextView userChipsCount;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mViewerSummaryParentFrame.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mViewerSummaryParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(getAdapterPosition());
                    break;
            }
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


    public void setLoaded() {
        isLoading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void addItems(List<UserBO> viewersSummaryBOs) {
        dataSet.addAll(viewersSummaryBOs);
        notifyDataSetChanged();
    }

    public List<UserBO> getItems() {
        return dataSet;
    }

    public void removeLoadingItem() {
        dataSet.remove(dataSet.size() - 1);
        notifyItemRemoved(dataSet.size());
    }

    public void addLoadingItem() {
        dataSet.add(null);
        notifyItemInserted(dataSet.size() - 1);
    }

    public void clearItems() {
        dataSet.clear();
    }

    public UserBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_go_live_top_spenders, parent, false);
                return new ItemTypeViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        try {
            UserBO userBO = dataSet.get(listPosition);

            if (holder instanceof ItemTypeViewHolder) {
                ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

                if (userBO.getUserProgressDetailBO() != null) {
                    UserLevelBO userLevelBO = Utils.getUserLevel(userBO.getUserProgressDetailBO().getUser_level());
                    itemTypeViewHolder.viewerRankImageView.setImageResource(userLevelBO.getLevelLocalImage());
                    itemTypeViewHolder.viewerRankTextView.setText(userLevelBO.getLevelTitle() + " " + userBO.getUserProgressDetailBO().getUser_level());
                }

                Utils.setImageRounded(itemTypeViewHolder.viewerImageView, userBO.getProfilePictureUrl(), mContext);
                itemTypeViewHolder.viewerTextView.setText(userBO.getUsername());

//            itemTypeViewHolder.viewerCashImageView.setImageResource(userBO.getCashLocalImage());
                itemTypeViewHolder.viewerCashTextView.setText((userBO.getWitkeyDollar().intValue()) + "");
                itemTypeViewHolder.userChipsCount.setText(((int)userBO.getChips()) + "");

            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        } catch (Exception e) {
            LogUtils.e("onBindViewHolder", "" + e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    /*Listeners*/
    ClickListeners clickListener;

    public void setClickListener(ClickListeners clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListeners {
        void onRowClick(int position);

    }

}
