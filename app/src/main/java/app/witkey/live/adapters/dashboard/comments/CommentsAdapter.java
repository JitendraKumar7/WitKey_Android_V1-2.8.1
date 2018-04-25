package app.witkey.live.adapters.dashboard.comments;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.CommentsBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 9/23/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter {

    private List<CommentsBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public CommentsAdapter(List<CommentsBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                try {
                    int lastVisibleItem, totalItemCount;

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                } catch (Exception e) {
                    LogUtils.e("CommentsAdapter", "" + e.getMessage());
                }
            }
        });
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mUserProfileImage)
        ImageView mUserProfileImage;
        @BindView(R.id.mUserNameTextView)
        CustomTextView mUserNameTextView;
        @BindView(R.id.mLastSeenTextView)
        TextView mLastSeenTextView;
        @BindView(R.id.mViewsTextView)
        TextView mViewsTextView;
        @BindView(R.id.mFeaturedCardTextView)
        TextView mFeaturedCardTextView;
        @BindView(R.id.userProfileViewParent)
        RelativeLayout userProfileViewParent;
        @BindView(R.id.mParentFrameFeatured)
        LinearLayout mParentFrameFeatured;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.userProfileViewParent.setOnClickListener(this);
            this.mParentFrameFeatured.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mParentFrameFeatured:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.userProfileViewParent:
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

    public void addItems(List<CommentsBO> streamBOs) {
        dataSet.addAll(streamBOs);
        notifyDataSetChanged();
    }

    public List<CommentsBO> getItems() {
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

    public CommentsBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_view_comments, parent, false);
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

        CommentsBO commentsBO = dataSet.get(listPosition);
        try {
            if (holder instanceof ItemTypeViewHolder) {
                ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

                Utils.setImageRounded(itemTypeViewHolder.mUserProfileImage, commentsBO.getUser_details().getProfilePictureUrl(), mContext);
                itemTypeViewHolder.mUserNameTextView.setText(commentsBO.getUser_details().getUsername());
                itemTypeViewHolder.mFeaturedCardTextView.setText(commentsBO.getComment());
//                itemTypeViewHolder.mLastSeenTextView.setText(commentsBO.getMoment_id() > 0 ? commentsBO.getMoment_id() + " " + mContext.getString(R.string.hr_ago) : mContext.getString(R.string.just_now));
                itemTypeViewHolder.mLastSeenTextView.setText(Utils.getDifBtwn2Dates(mContext, commentsBO.getCreated_at()));

            } else if (holder instanceof LoadingViewHolder) {
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        } catch (Exception e) {
            LogUtils.e("FeaturedAdapter", "" + e.getMessage());
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
