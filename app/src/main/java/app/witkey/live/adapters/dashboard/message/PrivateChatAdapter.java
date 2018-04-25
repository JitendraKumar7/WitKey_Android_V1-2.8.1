package app.witkey.live.adapters.dashboard.message;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.PrivateChatBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivateChatAdapter extends RecyclerView.Adapter {

    private List<PrivateChatBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 1;
    private boolean isLoading;

    //constructor...
    public PrivateChatAdapter(List<PrivateChatBO> data, Context context, RecyclerView mRecyclerView) {
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

        @BindView(R.id.mFanParentFrame)
        RelativeLayout mFanParentFrame;

        @BindView(R.id.fanImageView)
        ImageView fanImageView;

        @BindView(R.id.fanNameCustomTextView)
        CustomTextView fanNameCustomTextView;
        @BindView(R.id.decTextView)
        CustomTextView decTextView;

        @BindView(R.id.btnFanFollow)
        Button btnFanFollow;
        @BindView(R.id.fanParentFrame)
        RelativeLayout fanParentFrame;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.btnFanFollow.setOnClickListener(this);
            this.fanImageView.setOnClickListener(this);
            this.fanNameCustomTextView.setOnClickListener(this);
            this.fanParentFrame.setOnClickListener(this);
            this.decTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnFanFollow:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.fanNameCustomTextView:
                case R.id.fanParentFrame:
                case R.id.decTextView:
                case R.id.fanImageView:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
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

    public void addItems(List<PrivateChatBO> fanBOList) {
        dataSet.addAll(fanBOList);
        notifyDataSetChanged();
    }

    public List<PrivateChatBO> getItems() {
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

    public PrivateChatBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_item, parent, false);
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

        PrivateChatBO fanBO = dataSet.get(listPosition);
        try {
            if (holder instanceof ItemTypeViewHolder) {
                ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;
                if (fanBO.isShowDesc()) {
                    Utils.setImageSimple(itemTypeViewHolder.fanImageView, fanBO.adminURL, mContext);
                    itemTypeViewHolder.decTextView.setVisibility(View.VISIBLE);
                    itemTypeViewHolder.decTextView.setText("" + fanBO.adminDesc);
                    itemTypeViewHolder.fanNameCustomTextView.setText(Utils.getShortString(fanBO.adminTitle, 25));
                } else {
                    Utils.setImageRounded(itemTypeViewHolder.fanImageView, fanBO.getUser_details().getProfilePictureUrl(), mContext);
                    itemTypeViewHolder.decTextView.setVisibility(View.GONE);
                    itemTypeViewHolder.fanNameCustomTextView.setText(Utils.getShortString(fanBO.getUser_details().getUsername(), 15));
                }
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
        void onRowClick(View view, int position);

    }

}
