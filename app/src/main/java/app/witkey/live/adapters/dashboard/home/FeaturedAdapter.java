package app.witkey.live.adapters.dashboard.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.StreamUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;

/**
 * created by developer on 9/23/2017.
 */

public class FeaturedAdapter extends RecyclerView.Adapter {

    private List<StreamBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public FeaturedAdapter(List<StreamBO> data, Context context, RecyclerView mRecyclerView) {
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

        ImageView mUserProfileImage;
        LinearLayout imageParent;
        LinearLayout userProfileViewParent;
        ImageView mFeaturedCardImageView;
        TextView mUserNameTextView;
        TextView mLastSeenTextView;
        TextView mViewsTextView;
        TextView mFeaturedCardTextView;
        TextView stream_name_TV;
        View mParentFrameFeatured;

        CustomTextView liveStatus;
        ImageView equalizer;
        ImageView liveStatusDot;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            this.mParentFrameFeatured = itemView.findViewById(R.id.mParentFrameFeatured);
            this.imageParent = (LinearLayout) itemView.findViewById(R.id.imageParent);
            this.userProfileViewParent = (LinearLayout) itemView.findViewById(R.id.userProfileViewParent);
            this.mUserProfileImage = (ImageView) itemView.findViewById(R.id.mUserProfileImage);
            this.liveStatusDot = (ImageView) itemView.findViewById(R.id.liveStatusDot);
            this.mUserNameTextView = (TextView) itemView.findViewById(R.id.mUserNameTextView);
            this.liveStatus = (CustomTextView) itemView.findViewById(R.id.liveStatus);

            this.stream_name_TV = (TextView) itemView.findViewById(R.id.stream_name_TV);
            this.mLastSeenTextView = (TextView) itemView.findViewById(R.id.mLastSeenTextView);
            this.mViewsTextView = (TextView) itemView.findViewById(R.id.mViewsTextView);
            this.mFeaturedCardTextView = (TextView) itemView.findViewById(R.id.mFeaturedCardTextView);
            this.mFeaturedCardImageView = (ImageView) itemView.findViewById(R.id.mFeaturedCardImageView);
            this.equalizer = (ImageView) itemView.findViewById(R.id.equalizer);

            this.imageParent.setOnClickListener(this);
            this.userProfileViewParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageParent:
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

    public void addItems(List<StreamBO> streamBOs) {
        dataSet.addAll(streamBOs);
        notifyDataSetChanged();
    }

    public List<StreamBO> getItems() {
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

    public StreamBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_view_featured_new, parent, false);
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

        StreamBO streamBO = dataSet.get(listPosition);
        try {
            if (holder instanceof ItemTypeViewHolder) {
                ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;
                if (Utils.getScreenHeight(mContext) > 0 && Utils.getScreenWidth(mContext) > 0) {

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getScreenWidth(mContext),
                            (Utils.getScreenHeight(mContext) / 2));
                    itemTypeViewHolder.imageParent.setLayoutParams(params);
                }
                Utils.setImageRounded(itemTypeViewHolder.mUserProfileImage, streamBO.getUser_details().getProfilePictureUrl(), mContext);
//            Utils.setImageSimple(itemTypeViewHolder.mFeaturedCardImageView, streamBO.getUser_details().getProfilePictureUrl(), mContext);
                Utils.setImageSimpleWitkey(itemTypeViewHolder.mFeaturedCardImageView, streamBO.getUser_details().getProfilePictureUrl(), mContext);
                itemTypeViewHolder.mUserNameTextView.setText(streamBO.getUser_details().getUsername());
                itemTypeViewHolder.stream_name_TV.setText(streamBO.getName());
                if (streamBO.getCreatedAt() != null && !streamBO.getCreatedAt().isEmpty()) {
                    itemTypeViewHolder.mLastSeenTextView.setText(Utils.getDifBtwn2Dates(mContext, streamBO.getCreatedAt()));
//                itemTypeViewHolder.mLastSeenTextView.setText(Utils.howMuchTimePastFromNow(mContext, streamBO.getCreatedAt(), ""));
                } else {
                    itemTypeViewHolder.mLastSeenTextView.setText("1 " + mContext.getString(R.string.last_seen));
                }

                itemTypeViewHolder.mViewsTextView.setText(getViewersCount(streamBO.getTotalViewers()));
                itemTypeViewHolder.mFeaturedCardTextView.setText(R.string.welcome_desc);

                if (streamBO.getStatus().equals(StreamUtils.STATUS_ENDED)) {
                    itemTypeViewHolder.liveStatus.setVisibility(View.VISIBLE);
                    itemTypeViewHolder.liveStatus.setText(R.string.ended);
                    itemTypeViewHolder.liveStatus.setTextColor(ContextCompat.getColor(mContext,
                            R.color.white));
                    itemTypeViewHolder.liveStatusDot.setVisibility(View.GONE);
                    itemTypeViewHolder.equalizer.setVisibility(View.GONE);
                } else {

                    /*itemTypeViewHolder.liveStatus.setText(R.string.live);
                    itemTypeViewHolder.liveStatus.setTextColor(ContextCompat.getColor(mContext,
                            R.color.witkey_orange));
                    itemTypeViewHolder.liveStatusDot.setVisibility(View.VISIBLE);
                    itemTypeViewHolder.liveStatusDot.startAnimation(Animations.getBlinkAnimationInstance());*/
                    itemTypeViewHolder.liveStatus.setVisibility(View.GONE);
                    itemTypeViewHolder.equalizer.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.raw.equalizer_1).asGif().into(itemTypeViewHolder.equalizer);

                }

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

    private String getViewersCount(String viewersCount) {
        if (viewersCount != null && !viewersCount.isEmpty()) {
            try {

                int count = Integer.parseInt(viewersCount);
                if (count < 1000) {
                    return (count < 2) ? count + " " + mContext.getString(R.string.viewer) : count + " " + mContext.getString(R.string.viewers);
                } else {
                    int count_ = count / 1000;
                    int count_hundred = count % 1000;
                    int count_hundred_points = count_hundred / 100;
                    return count_ + "." + count_hundred_points + " " + mContext.getString(R.string.viewers);
                }

            } catch (Exception e) {
                LogUtils.e("getViewersCount", "" + e.getMessage());
                return "0" + mContext.getString(R.string.viewer);
            }
        } else {
            return "0" + mContext.getString(R.string.viewer);
        }
    }

    private String getTimeAgo(String timeAgo) {
        if (timeAgo != null && !timeAgo.isEmpty()) {
            try {

                int count = Integer.parseInt(timeAgo);
                if (count < 1000) {
                    return (count < 2) ? count + " " + mContext.getString(R.string.viewer) : count + " " + mContext.getString(R.string.viewers);
                } else {
                    int count_ = count / 1000;
                    int count_hundred = count % 1000;
                    int count_hundred_points = count_hundred / 100;
                    return count_ + "." + count_hundred_points + " " + mContext.getString(R.string.viewers);
                }

            } catch (Exception e) {
                LogUtils.e("getViewersCount", "" + e.getMessage());
                return "0" + mContext.getString(R.string.viewer);
            }
        } else {
            return "1" + mContext.getString(R.string.viewer);
        }
    }
}
