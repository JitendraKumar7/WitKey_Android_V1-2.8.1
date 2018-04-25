package app.witkey.live.adapters.dashboard.profile;

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
import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.Utils;
import butterknife.ButterKnife;

/**
 * created by developer on 9/23/2017.
 */

public class UserProfileAdapter extends RecyclerView.Adapter {

    private List<Object> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    public final int VIEW_TYPE_TOP_INDEX = 2;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public UserProfileAdapter(List<Object> data, Context context, RecyclerView mRecyclerView) {
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
        RelativeLayout userProfileViewParent;
        ImageView mFeaturedCardImageView;
        TextView mUserNameTextView;
        TextView mLastSeenTextView;
        TextView mViewsTextView;
        TextView mFeaturedCardTextView;
        View mParentFrameFeatured;


        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            this.mParentFrameFeatured = itemView.findViewById(R.id.mParentFrameFeatured);
            this.imageParent = (LinearLayout) itemView.findViewById(R.id.imageParent);
            this.userProfileViewParent = (RelativeLayout) itemView.findViewById(R.id.userProfileViewParent);
            this.mUserProfileImage = (ImageView) itemView.findViewById(R.id.mUserProfileImage);
            this.mUserNameTextView = (TextView) itemView.findViewById(R.id.mUserNameTextView);

            this.mLastSeenTextView = (TextView) itemView.findViewById(R.id.mLastSeenTextView);
            this.mViewsTextView = (TextView) itemView.findViewById(R.id.mViewsTextView);
            this.mFeaturedCardTextView = (TextView) itemView.findViewById(R.id.mFeaturedCardTextView);
            this.mFeaturedCardImageView = (ImageView) itemView.findViewById(R.id.mFeaturedCardImageView);

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

    class TopIndexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView mUserProfileImage;
        LinearLayout imageParent;
        RelativeLayout userProfileViewParent;
        ImageView mFeaturedCardImageView;
        TextView mUserNameTextView;
        TextView mLastSeenTextView;
        TextView mViewsTextView;
        TextView mFeaturedCardTextView;
        View mParentFrameFeatured;


        public TopIndexViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

//            this.imageParent.setOnClickListener(this);
//            this.userProfileViewParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
               /* case R.id.imageParent:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.userProfileViewParent:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;*/
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

    public void addItems(List<Object> streamBOs) {
        dataSet.addAll(streamBOs);
        notifyDataSetChanged();
    }

    public List<Object> getItems() {
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

    public Object getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_TOP_INDEX:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_user_profile_item_top, parent, false);
                return new TopIndexViewHolder(view);
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_view_featured, parent, false);
                return new ItemTypeViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_TOP_INDEX;
        } else {
            return dataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Object object = dataSet.get(listPosition);
        if (holder instanceof ItemTypeViewHolder) {
            StreamBO streamBO = (StreamBO) object;
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;
            if (Utils.getScreenHeight(mContext) > 0 && Utils.getScreenWidth(mContext) > 0) {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Utils.getScreenWidth(mContext),
                        (Utils.getScreenHeight(mContext) / 2));
                itemTypeViewHolder.imageParent.setLayoutParams(params);
            }
            Utils.setImageRounded(itemTypeViewHolder.mUserProfileImage, streamBO.getUser_details().getProfilePictureUrl(), mContext);
            Utils.setImageSimple(itemTypeViewHolder.mFeaturedCardImageView, streamBO.getUser_details().getProfilePictureUrl(), mContext);
            itemTypeViewHolder.mUserNameTextView.setText(streamBO.getUser_details().getUsername());
            itemTypeViewHolder.mLastSeenTextView.setText(R.string.last_seen);
            itemTypeViewHolder.mViewsTextView.setText(R.string.viewers_count);
            itemTypeViewHolder.mFeaturedCardTextView.setText(R.string.welcome_desc);

        } else if (holder instanceof TopIndexViewHolder) {



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
        void onRowClick(View view, int position);

    }
}
