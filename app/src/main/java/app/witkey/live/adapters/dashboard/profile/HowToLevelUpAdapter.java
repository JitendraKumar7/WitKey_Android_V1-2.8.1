package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.HowToLevelUpBO;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HowToLevelUpAdapter extends RecyclerView.Adapter {

    private List<HowToLevelUpBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public HowToLevelUpAdapter(List<HowToLevelUpBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mHowToLevelUpParentFrame)
        RelativeLayout mHowToLevelUpParentFrame;

        @BindView(R.id.levelUpProcedureCustomTextView)
        CustomTextView levelUpProcedureCustomTextView;

        @BindView(R.id.levelUpRewardCustomTextView)
        CustomTextView levelUpRewardCustomTextView;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mHowToLevelUpParentFrame.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parentLayout:
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

    public void addItems(List<HowToLevelUpBO> howToLevelUpBOList) {
        dataSet.addAll(howToLevelUpBOList);
        notifyDataSetChanged();
    }

    public List<HowToLevelUpBO> getItems() {
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

    public HowToLevelUpBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_how_to_level_up_item, parent, false);
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

        HowToLevelUpBO howToLevelUpBO = dataSet.get(listPosition);

        if (holder instanceof ItemTypeViewHolder) {
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

            if (listPosition == 0) {
                itemTypeViewHolder.levelUpProcedureCustomTextView.setTextColor(mContext.getResources().getColor(R.color.witkey_orange));
                itemTypeViewHolder.levelUpRewardCustomTextView.setTextColor(mContext.getResources().getColor(R.color.witkey_orange));

                itemTypeViewHolder.levelUpProcedureCustomTextView.setText(howToLevelUpBO.getLevelUpProcedure());
                itemTypeViewHolder.levelUpRewardCustomTextView.setVisibility(View.GONE);
            } else {
                itemTypeViewHolder.levelUpProcedureCustomTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                itemTypeViewHolder.levelUpRewardCustomTextView.setTextColor(mContext.getResources().getColor(R.color.witkey_orange));
                itemTypeViewHolder.levelUpProcedureCustomTextView.setText(howToLevelUpBO.getLevelUpProcedure());
                itemTypeViewHolder.levelUpRewardCustomTextView.setText(howToLevelUpBO.getLevelUpReward());
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
