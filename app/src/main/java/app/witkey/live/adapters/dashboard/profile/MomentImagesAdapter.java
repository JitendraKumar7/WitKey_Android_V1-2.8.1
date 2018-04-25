package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.MomentImagesBO;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MomentImagesAdapter extends RecyclerView.Adapter {

    private List<MomentImagesBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public MomentImagesAdapter(List<MomentImagesBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    public void refresh(List<MomentImagesBO> data) {
        dataSet = data;
        notifyDataSetChanged();
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.mMomentImageParentFrame)
        RelativeLayout mMomentImageParentFrame;

        @BindView(R.id.momentImageView)
        ImageView momentImageView;

        @BindView(R.id.removeMoment)
        LinearLayout removeMoment;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mMomentImageParentFrame.setOnClickListener(this);
            this.removeMoment.setOnClickListener(this);
            this.mMomentImageParentFrame.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mMomentImageParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.removeMoment:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.mMomentImageParentFrame:
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

    public void addItems(List<MomentImagesBO> momentImagesBOList) {
        dataSet.addAll(momentImagesBOList);
        notifyDataSetChanged();
    }

    public void removeItems(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataSet.size());
    }

    public List<MomentImagesBO> getItems() {
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

    public MomentImagesBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_moment_image_item, parent, false);
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

        if (holder instanceof ItemTypeViewHolder) {
            final ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

            MomentImagesBO momentImagesBO = dataSet.get(listPosition);

            if (momentImagesBO.getMomentImageUri() == null) {

                itemTypeViewHolder.momentImageView.setImageResource(R.drawable.ic_add_image);
                itemTypeViewHolder.removeMoment.setVisibility(View.GONE);

            } else {

                Glide.with(mContext)
                        .load(momentImagesBO.getMomentImageUri())
                        .asBitmap()
                        .centerCrop()
                        .into(new BitmapImageViewTarget(itemTypeViewHolder.momentImageView) {
                            @Override
                            protected void setResource(Bitmap resource) {

                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                circularBitmapDrawable.setCornerRadius(50f);
                                itemTypeViewHolder.momentImageView.setImageDrawable(circularBitmapDrawable);
                            }
                        });

                if (momentImagesBO.isSelected()) {
                    itemTypeViewHolder.removeMoment.setVisibility(View.VISIBLE);
                } else {
                    itemTypeViewHolder.removeMoment.setVisibility(View.GONE);
                }
            }
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
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
