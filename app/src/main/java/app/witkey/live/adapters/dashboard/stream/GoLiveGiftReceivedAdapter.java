package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
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
import app.witkey.live.items.GiftReceivedBO;
import app.witkey.live.utils.PixelsOp;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveGiftReceivedAdapter extends RecyclerView.Adapter {

    private List<GiftReceivedBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public GoLiveGiftReceivedAdapter(List<GiftReceivedBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mGiftReceivedParentFrame)
        RelativeLayout mGiftReceivedParentFrame;

        @BindView(R.id.giftReceivedParentFrame)
        RelativeLayout giftReceivedParentFrame;

        @BindView(R.id.giftReceivedTimeLeft)
        CustomTextView giftReceivedTimeLeft;

        @BindView(R.id.giftReceivedImageView)
        ImageView giftReceivedImageView;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mGiftReceivedParentFrame.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mGiftReceivedParentFrame:
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

    public void addItems(List<GiftReceivedBO> giftReceivedBOList) {
        dataSet.addAll(giftReceivedBOList);
        notifyDataSetChanged();
    }

    public List<GiftReceivedBO> getItems() {
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

    public GiftReceivedBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_go_live_gift_received_item, parent, false);
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

        GiftReceivedBO giftReceivedBO = dataSet.get(listPosition);

        if (holder instanceof ItemTypeViewHolder) {
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

            itemTypeViewHolder.giftReceivedParentFrame.getLayoutParams().width = Utils.getScreenWidth(mContext) / 5;
            itemTypeViewHolder.giftReceivedParentFrame.getLayoutParams().height = Utils.getScreenWidth(mContext) / 5;

            itemTypeViewHolder.giftReceivedTimeLeft.setText(giftReceivedBO.getTimeLeft());
            itemTypeViewHolder.giftReceivedImageView.setImageResource(giftReceivedBO.getLocalImage());

            if (listPosition > 1) {
                int padding = Utils.dp2px(mContext, 20);
                itemTypeViewHolder.giftReceivedImageView.setPadding(padding, padding, padding, padding);
                itemTypeViewHolder.giftReceivedTimeLeft.setVisibility(View.GONE);
            } else {
                int padding = Utils.dp2px(mContext, 5);
                itemTypeViewHolder.giftReceivedImageView.setPadding(padding, padding, padding, padding);
                itemTypeViewHolder.giftReceivedTimeLeft.setVisibility(View.VISIBLE);
            }

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
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
