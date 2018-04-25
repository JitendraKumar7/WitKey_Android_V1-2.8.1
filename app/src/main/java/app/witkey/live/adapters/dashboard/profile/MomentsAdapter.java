package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.MomentBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 9/23/2017.
 */

public class MomentsAdapter extends RecyclerView.Adapter {

    private List<MomentBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;
    boolean enableEdit = false;

    //constructor...
    public MomentsAdapter(List<MomentBO> data, Context context, RecyclerView mRecyclerView, boolean enableEdit_) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        enableEdit = enableEdit_;

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

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnLikeListener {


        @BindView(R.id.mUserProfileImage)
        ImageView mUserProfileImage;
        @BindView(R.id.mEditMoment)
        ImageView mEditMoment;
//        @BindView(R.id.mFeaturedCardImageView)
//        ImageView mFeaturedCardImageView;

        @BindView(R.id.momentVP)
        ViewPager momentVP;

        @BindView(R.id.mUserNameTextView)
        CustomTextView mUserNameTextView;

        @BindView(R.id.mLastSeenTextView)
        CustomTextView mLastSeenTextView;

        @BindView(R.id.mViewsTextView)
        CustomTextView mViewsTextView;

        @BindView(R.id.mFeaturedCardTextView)
        CustomTextView mFeaturedCardTextView;

        @BindView(R.id.likeCountLV)
        LinearLayout likeCountLV;
        @BindView(R.id.userNameView)
        LinearLayout userNameView;
        @BindView(R.id.pageIndicators)
        LinearLayout pageIndicators;
        @BindView(R.id.commentCountLV)
        LinearLayout commentCountLV;
        @BindView(R.id.postLV)
        LinearLayout postLV;
        @BindView(R.id.shareCountLV)
        LinearLayout shareCountLV;
        @BindView(R.id.userProfileViewParent)
        RelativeLayout userProfileViewParent;
        @BindView(R.id.imageParent)
        LinearLayout imageParent;
        @BindView(R.id.heart_button)
        LikeButton likeButton;
        @BindView(R.id.message_button)
        ImageView message_button;
        @BindView(R.id.share_button)
        ImageView share_button;
        @BindView(R.id.likeCountShow)
        CustomTextView likeCountShow;
        @BindView(R.id.customTextView5)
        CustomTextView customTextView5;
        @BindView(R.id.commentCountShow)
        CustomTextView commentCountShow;
        ImageView[] dots;
        MomentsSlideViewAdapter momentsSlideViewAdapter;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


            this.imageParent.setOnClickListener(this);
            this.userProfileViewParent.setOnClickListener(this);
            this.likeButton.setOnLikeListener(this);
            this.likeCountLV.setOnClickListener(this);
            this.commentCountLV.setOnClickListener(this);
            this.shareCountLV.setOnClickListener(this);
            this.momentVP.setOnClickListener(this);
            this.postLV.setOnClickListener(this);
            this.userNameView.setOnClickListener(this);
            this.mUserProfileImage.setOnClickListener(this);
            this.likeCountShow.setOnClickListener(this);
            this.customTextView5.setOnClickListener(this);
            this.commentCountShow.setOnClickListener(this);
            this.mEditMoment.setOnClickListener(this);
            usingCustomIcons();
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
                case R.id.likeCountLV:
                    if (clickListener != null)
                        likeButton.performClick();
                    break;
                case R.id.commentCountLV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.shareCountLV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.momentVP:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.likeButton:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.postLV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.userNameView:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.mUserProfileImage:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.customTextView5:
                case R.id.likeCountShow:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.commentCountShow:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.mEditMoment:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
            }
        }

        public void usingCustomIcons() {

            //shown when the button is in its default state or when unLiked.
           /* message_button.setUnlikeDrawable(new BitmapDrawable(mContext.getResources(), new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_message).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap()));

            //shown when the button is liked!
            message_button.setLikeDrawable(new BitmapDrawable(mContext.getResources(), new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_message).colorRes(R.color.witkey_orange).sizeDp(25).toBitmap()));
            //shown when the button is in its default state or when unLiked.
            share_button.setUnlikeDrawable(new BitmapDrawable(mContext.getResources(), new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_share).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap()));

            //shown when the button is liked!
            share_button.setLikeDrawable(new BitmapDrawable(mContext.getResources(), new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_share).colorRes(R.color.witkey_orange).sizeDp(25).toBitmap()));
*/
            likeButton.setUnlikeDrawable(new BitmapDrawable(mContext.getResources(), new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_thumb_up).colorRes(android.R.color.darker_gray).sizeDp(20).toBitmap()));

            //shown when the button is liked!
            likeButton.setLikeDrawable(new BitmapDrawable(mContext.getResources(), new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_thumb_up).colorRes(R.color.witkey_orange).sizeDp(20).toBitmap()));
        }

        @Override
        public void liked(LikeButton likeButton) {
            if (clickListener != null)
                clickListener.onRowClick(likeButton, getAdapterPosition());
        }

        @Override
        public void unLiked(LikeButton likeButton) {
            if (clickListener != null)
                clickListener.onRowClick(likeButton, getAdapterPosition());
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

    public void removeItems(int position) {
        dataSet.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, dataSet.size());
    }

    public void addItems(List<MomentBO> streamBOs) {
        dataSet.addAll(streamBOs);
        notifyDataSetChanged();
    }

    public void updateItem(int position, MomentBO momentBOs) {
        dataSet.set(position, momentBOs);
        notifyItemChanged(position);
    }

    public List<MomentBO> getItems() {
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

    public MomentBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_card_view_moments, parent, false);
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

        MomentBO momentBO = dataSet.get(listPosition);
        try {
            if (holder instanceof ItemTypeViewHolder) {
                final ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

                if (enableEdit) {
                    itemTypeViewHolder.mEditMoment.setVisibility(View.VISIBLE);
                } else {
                    itemTypeViewHolder.mEditMoment.setVisibility(View.GONE);
                }

                Utils.setImageRounded(itemTypeViewHolder.mUserProfileImage, momentBO.getUser_details().getProfilePictureUrl(), mContext);
                itemTypeViewHolder.mFeaturedCardTextView.setText(Utils.getShortString(momentBO.getUser_status_text(), 140));
                itemTypeViewHolder.mUserNameTextView.setText(Utils.getShortString(momentBO.getUser_details().getUsername(), 30));
                itemTypeViewHolder.likeCountShow.setText(momentBO.getTotal_likes() + " " + mContext.getString(R.string.people));
                itemTypeViewHolder.commentCountShow.setText(momentBO.getTotal_comments() + " " + mContext.getString(R.string.comments));
                itemTypeViewHolder.mLastSeenTextView.setText(Utils.getDifBtwn2Dates(mContext, momentBO.getCreated_at()));

//                itemTypeViewHolder.share_button.setImageBitmap(new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_share).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap());
//                itemTypeViewHolder.message_button.setImageBitmap(new IconicsDrawable(mContext, CommunityMaterial.Icon.cmd_message).colorRes(android.R.color.darker_gray).sizeDp(20).toBitmap());
                if (momentBO.getIs_like() == 1) {
                    itemTypeViewHolder.likeButton.setLiked(true);
                } else {
                    itemTypeViewHolder.likeButton.setLiked(false);
                }

                if (momentBO.getImage_arrsy() != null && momentBO.getImage_arrsy().length > 0) {
                    itemTypeViewHolder.momentVP.setVisibility(View.VISIBLE);
                    itemTypeViewHolder.pageIndicators.setVisibility(View.VISIBLE);

                    itemTypeViewHolder.momentVP.getLayoutParams().height = Utils.getScreenWidth(mContext);
                    itemTypeViewHolder.momentsSlideViewAdapter = new MomentsSlideViewAdapter(mContext, new ArrayList<>(Arrays.asList(momentBO.getImage_arrsy())));
                    itemTypeViewHolder.momentVP.setAdapter(itemTypeViewHolder.momentsSlideViewAdapter);
                    final int totalCount = itemTypeViewHolder.momentsSlideViewAdapter.getCount();

                    if (totalCount > 1) {
                        itemTypeViewHolder.dots = new ImageView[]{};
                        itemTypeViewHolder.dots = addIndicators(mContext, itemTypeViewHolder.pageIndicators, totalCount);

                        itemTypeViewHolder.momentVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                            Utils.showToast(mContext, "onPageScrolled " + totalCount + "-" + (1 + position));
                            }

                            @Override
                            public void onPageSelected(int position) {
                                moveIndicator(mContext, totalCount, itemTypeViewHolder.dots, position);
//                            Utils.showToast(mContext, "onPageSelected " + totalCount + "-" + (1 + position));
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                            }
                        });
                    }
                    itemTypeViewHolder.momentsSlideViewAdapter.setClickListener(new MomentsSlideViewAdapter.ClickListeners() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (clickListener != null)
                                clickListener.onRowClick(view, listPosition, position);
                        }
                    });


                } else {
                    itemTypeViewHolder.momentVP.setVisibility(View.GONE);
                    itemTypeViewHolder.pageIndicators.setVisibility(View.GONE);
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

        void onRowClick(View view, int position, int child);
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

    private static ImageView[] addIndicators(Context mContext, LinearLayout view, int count) {
        ImageView[] imageViews = new ImageView[count];
        view.removeAllViews();

        for (int i = 0; i < count; i++) {
            imageViews[i] = new ImageView(mContext);
            imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_un_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(4, 0, 4, 0);

            view.addView(imageViews[i], params);
        }

        imageViews[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_selected));
        return imageViews;
    }

    private static void moveIndicator(Context context, int totalCount, ImageView[] imageViews, int currentPosition) {
        try {
            for (int i = 0; i < totalCount; i++) {
                imageViews[i].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_un_selected));
            }
            imageViews[currentPosition].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_selected));
        } catch (Exception e) {
            LogUtils.e("moveIndicator", "" + e.getMessage());
        }
    }
}
