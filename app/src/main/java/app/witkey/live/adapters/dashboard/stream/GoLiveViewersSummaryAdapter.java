package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveViewersSummaryAdapter extends RecyclerView.Adapter {

    private List<ConversationBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public GoLiveViewersSummaryAdapter(ArrayList<ConversationBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
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

    public void addItems(List<ConversationBO> conversationBOs) {
        dataSet.addAll(conversationBOs);
        notifyDataSetChanged();
    }

    public List<ConversationBO> getItems() {
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

    public ConversationBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_go_live_viewer_summary_item, parent, false);
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

        ConversationBO conversationBO = dataSet.get(listPosition);
        if (holder instanceof ItemTypeViewHolder) {
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;
            try {
                UserBO userDetail = conversationBO.getUser_details();
                int userLevel = 1;
                if (userDetail != null) {
                    userLevel = userDetail.getUserProgressDetailBO().getUser_level();

                    UserLevelBO userLevelBO = Utils.getUserLevel(userLevel);

                    Utils.setImageRounded(itemTypeViewHolder.viewerImageView, userDetail.getProfilePictureUrl(), mContext);
                    itemTypeViewHolder.viewerTextView.setText(userDetail.getUsername());

                    itemTypeViewHolder.viewerRankImageView.setImageResource(userLevelBO.getLevelLocalImage());
                    itemTypeViewHolder.viewerRankTextView.setText(userLevelBO.getLevelTitle() + " " + userLevel);

                    itemTypeViewHolder.viewerCashImageView.setImageResource((userDetail.getIsArtist() == 1) ? R.drawable.dollar : R.drawable.chip);
                    itemTypeViewHolder.viewerCashTextView.setText((userDetail.getIsArtist() == 1) ? (userDetail.getWitkeyDollar().intValue()) + "" : ((int)userDetail.getChips()) + "");
                } else {

                    UserLevelBO userLevelBO = Utils.getUserLevel(userLevel);

                    Utils.setImageRounded(itemTypeViewHolder.viewerImageView, conversationBO.getDpUrl(), mContext);
                    itemTypeViewHolder.viewerTextView.setText(conversationBO.getSenderName());

                    itemTypeViewHolder.viewerRankImageView.setImageResource(userLevelBO.getLevelLocalImage());
                    itemTypeViewHolder.viewerRankTextView.setText(userLevelBO.getLevelTitle() + " " + userLevel);

                    itemTypeViewHolder.viewerCashImageView.setImageResource(R.drawable.dollar);
                    itemTypeViewHolder.viewerCashTextView.setText("0");
                }
            } catch (Exception e) {
                LogUtils.e("GoLiveViewersSummaryAdapter", "" + e.getMessage());
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
