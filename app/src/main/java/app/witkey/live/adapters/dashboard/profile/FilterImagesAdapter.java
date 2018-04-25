package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.FilterBO;
import app.witkey.live.utils.customviews.CustomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterImagesAdapter extends RecyclerView.Adapter {

    private List<FilterBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;
    RoundedBitmapDrawable circularBitmapDrawable;

    //constructor...
    public FilterImagesAdapter(List<FilterBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

        Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.place_holder_videos);

        circularBitmapDrawable =
                RoundedBitmapDrawableFactory.create(mContext.getResources(), icon);
        circularBitmapDrawable.setCornerRadius(50f);
    }

    public void refresh(List<FilterBO> data) {
        dataSet = data;
        notifyDataSetChanged();
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.filterImageParentFrame)
        RelativeLayout filterImageParentFrame;

        @BindView(R.id.filterImageView)
        ImageView filterImageView;

        @BindView(R.id.filterNumber)
        CustomTextView filterNumber;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.filterImageParentFrame.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.filterImageParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.filterImageParentFrame:
                    if (clickListener != null)
                        return clickListener.onRowLongPressClick(getAdapterPosition());
            }
            return false;
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

    public void addItems(List<FilterBO> momentImagesBOList) {
        dataSet.addAll(momentImagesBOList);
        notifyDataSetChanged();
    }

    public void removeItems(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataSet.size());
    }

    public List<FilterBO> getItems() {
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

    public FilterBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_filter_image_item, parent, false);
                return new ItemTypeViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        /*if (dataSet.size() == 0 && position == 0)
            return VIEW_TYPE_ITEM;*/
        return dataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {
        try {
            if (holder instanceof ItemTypeViewHolder) {
                final ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

                FilterBO filterBO = dataSet.get(listPosition);
                itemTypeViewHolder.filterImageView.setImageResource(filterBO.getPath());
                itemTypeViewHolder.filterNumber.setText((listPosition + 1) + "");

            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();// + 1;
    }


    /*Listeners*/
    ClickListeners clickListener;

    public void setClickListener(ClickListeners clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListeners {
        void onRowClick(View view, int position);

        boolean onRowLongPressClick(int position);
    }
}
