package app.witkey.live.adapters.dashboard.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MomentsSlideViewAdapter;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.StreamUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HottestAdapter extends RecyclerView.Adapter {

    private List<StreamBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private final int VIEW_TYPE_ONE_ITEM = 2;
    private final int VIEW_TYPE_TOP_ITEM = 3;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;
    JSONArray jsonArray;
    ArrayList<String> arrayList;
    int showIndexLimit = 10;

    private int recyclerViewHeight;

    //constructor...
    public HottestAdapter(List<StreamBO> data, Context context, RecyclerView mRecyclerView, int recyclerViewHeight, JSONArray jsonArray) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        this.recyclerViewHeight = recyclerViewHeight;
        this.jsonArray = jsonArray;
        this.arrayList = new ArrayList<>();

        if (jsonArray != null && jsonArray.length() > 0) {
            for (int index = 0; index < jsonArray.length(); index++) {
                try {
                    arrayList.add(jsonArray.get(index).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            arrayList.add("NONE");
        }

        GridLayoutManager gridLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position > showIndexLimit || position == 0 ? 2 : 1;
            }
        });

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

    class ItemTypeOneIndexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mParentFrameHottest)
        LinearLayout mParentFrameHottest;
        @BindView(R.id.imageProfileView)
        LinearLayout imageProfileView;

        @BindView(R.id.hottestCardTopView)
        RelativeLayout hottestCardTopView;

        @BindView(R.id.micParent)
        LinearLayout micParent;

        @BindView(R.id.mHottestCardImageView)
        ImageView mHottestCardImageView;

        @BindView(R.id.micTextView)
        TextView micTextView;

        @BindView(R.id.mLastSeenTextView)
        TextView mLastSeenTextView;
        @BindView(R.id.mViewsTextView)
        TextView mViewsTextView;
        @BindView(R.id.liveStatus)
        CustomTextView liveStatus;
        @BindView(R.id.liveStatusDot)
        ImageView liveStatusDot;

        @BindView(R.id.hottestTextView)
        TextView hottestTextView;
        @BindView(R.id.equalizer)
        ImageView equalizer;

        public ItemTypeOneIndexViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mParentFrameHottest.setOnClickListener(this);
            this.imageProfileView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mParentFrameHottest:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.imageProfileView:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
            }
        }
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mParentFrameHottest)
        LinearLayout mParentFrameHottest;
        @BindView(R.id.imageProfileView)
        LinearLayout imageProfileView;

        @BindView(R.id.hottestCardTopView)
        RelativeLayout hottestCardTopView;

        @BindView(R.id.micParent)
        LinearLayout micParent;

        @BindView(R.id.mHottestCardImageView)
        ImageView mHottestCardImageView;

        @BindView(R.id.micTextView)
        TextView micTextView;

        @BindView(R.id.mLastSeenTextView)
        TextView mLastSeenTextView;
        @BindView(R.id.mViewsTextView)
        TextView mViewsTextView;
        @BindView(R.id.liveStatus)
        CustomTextView liveStatus;
        @BindView(R.id.liveStatusDot)
        ImageView liveStatusDot;
        @BindView(R.id.equalizer)
        ImageView equalizer;

        @BindView(R.id.hottestTextView)
        TextView hottestTextView;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mParentFrameHottest.setOnClickListener(this);
            this.imageProfileView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mParentFrameHottest:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.imageProfileView:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
            }
        }
    }

    class ItemTypeTopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageParent)
        LinearLayout imageParent;

        @BindView(R.id.addImagesVP)
        ViewPager addImagesVP;

        @BindView(R.id.pageIndicators)
        LinearLayout pageIndicators;
        MomentsSlideViewAdapter momentsSlideViewAdapter;
        ImageView[] dots;

        public ItemTypeTopViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parentLayout:
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

    public void addItems(List<StreamBO> hottestBOList) {
        dataSet.addAll(hottestBOList);
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hottest_item, parent, false);
                return new ItemTypeViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
            case VIEW_TYPE_ONE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hottest_item, parent, false);
                return new ItemTypeOneIndexViewHolder(view);
            case VIEW_TYPE_TOP_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hottest_top, parent, false);
                return new ItemTypeTopViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position) == null ? VIEW_TYPE_LOADING : position == 0 ? VIEW_TYPE_TOP_ITEM : position > showIndexLimit ? VIEW_TYPE_ONE_ITEM : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        StreamBO hottestBO = dataSet.get(listPosition);
        if (holder instanceof ItemTypeTopViewHolder) {
            final ItemTypeTopViewHolder itemTypeTopViewHolder = (ItemTypeTopViewHolder) holder;


            int otherVewHeight = ((Utils.getScreenHeight(mContext) / 4) * 2) + Utils.dp2px(mContext, 135) + Utils.getStatusBarHeight(mContext);
            int topViewHeight = (Utils.getScreenHeight(mContext) - otherVewHeight);
            if (topViewHeight > Utils.dp2px(mContext, 100)) {
                itemTypeTopViewHolder.imageParent.getLayoutParams().height = topViewHeight;
            } else {
                itemTypeTopViewHolder.imageParent.getLayoutParams().height = Utils.dp2px(mContext, 100);
            }

            itemTypeTopViewHolder.momentsSlideViewAdapter = new MomentsSlideViewAdapter(mContext, arrayList);
//          itemTypeTopViewHolder.momentsSlideViewAdapter = new MomentsSlideViewAdapter(mContext, new ArrayList<>(Arrays.asList(new String[]{"http://www.wickedclubcrawl.com.au/images/events/birthdays/clubbing-selfie-girl-party-dj-dancefloor-1200.jpg", "http://www.lujure.ca/2015/photos/PuertoRico_Nightlife.jpg", "https://www.galavantier.com/sites/default/files/wp-content/uploads/2014/10/Screen-Shot-2014-10-16-at-9.54.58-AM.png", "https://pbs.twimg.com/media/C2LiPAcUQAAlM-T.jpg"})));
            itemTypeTopViewHolder.addImagesVP.setAdapter(itemTypeTopViewHolder.momentsSlideViewAdapter);
            final int totalCount = itemTypeTopViewHolder.momentsSlideViewAdapter.getCount();

            if (totalCount > 1) {
                itemTypeTopViewHolder.dots = new ImageView[]{};
                itemTypeTopViewHolder.dots = addIndicators(mContext, itemTypeTopViewHolder.pageIndicators, totalCount);

                itemTypeTopViewHolder.addImagesVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        moveIndicator(mContext, totalCount, itemTypeTopViewHolder.dots, position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
            itemTypeTopViewHolder.momentsSlideViewAdapter.setClickListener(new MomentsSlideViewAdapter.ClickListeners() {
                @Override
                public void onItemClick(View view, int position) {
                    if (clickListener != null)
                        clickListener.onRowClick(view, position);
                }
            });

        } else if (holder instanceof ItemTypeViewHolder) {
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

            itemTypeViewHolder.hottestCardTopView.setVisibility(View.VISIBLE);
            itemTypeViewHolder.micParent.setVisibility(View.VISIBLE);
            itemTypeViewHolder.micTextView.setVisibility(View.VISIBLE);
            itemTypeViewHolder.hottestTextView.setVisibility(View.VISIBLE);
            if (hottestBO.getUser_details().getUserProgressDetailBO() != null && !TextUtils.isEmpty(hottestBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "")) {
                itemTypeViewHolder.micTextView.setText(hottestBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "");//(hottestBO.getId()); //listPosition
            } else {
                itemTypeViewHolder.micTextView.setText(listPosition + "");//(hottestBO.getId()); //listPosition
            }
            if (hottestBO.getUser_details().getUser_complete_id().contains("-")) {
                itemTypeViewHolder.hottestTextView.setText(hottestBO.getUser_details().getUser_complete_id().split("-")[0] + " " + hottestBO.getUser_details().getUsername());
            } else {
                itemTypeViewHolder.hottestTextView.setText(hottestBO.getUser_details().getUser_complete_id() + " " + hottestBO.getUser_details().getUsername());

            }
//                Utils.setImageSimple(itemTypeViewHolder.mHottestCardImageView, hottestBO.getUser_details().profilePictureUrl, mContext);
            //Utils.setImageSimpleWitkey(itemTypeViewHolder.mHottestCardImageView, hottestBO.getUser_details().getProfilePictureUrl(), mContext);
            Utils.setImageCornersRoundWitkey(itemTypeViewHolder.mHottestCardImageView, hottestBO.getUser_details().getProfilePictureUrl(), mContext, 10f);
            ((RelativeLayout.LayoutParams) itemTypeViewHolder.mHottestCardImageView.getLayoutParams()).setMargins(20, 11, 20, 11);

            itemTypeViewHolder.mHottestCardImageView.getLayoutParams().height = Utils.getScreenHeight(mContext) / 4;
            itemTypeViewHolder.mParentFrameHottest.getLayoutParams().height = Utils.getScreenHeight(mContext) / 4 + 100;

            if (hottestBO.getCreatedAt() != null && !hottestBO.getCreatedAt().isEmpty()) {
                itemTypeViewHolder.mLastSeenTextView.setText(Utils.getDifBtwn2Dates(mContext, hottestBO.getCreatedAt()));
//                itemTypeViewHolder.mLastSeenTextView.setText(Utils.howMuchTimePastFromNow(mContext, streamBO.getCreatedAt(), ""));
            } else {
                itemTypeViewHolder.mLastSeenTextView.setText("1 " + mContext.getString(R.string.hr_ago));
            }

            if (hottestBO.getStatus().equals(StreamUtils.STATUS_ENDED)) {
                itemTypeViewHolder.liveStatus.setVisibility(View.VISIBLE);
                itemTypeViewHolder.liveStatus.setText(R.string.ended);
                itemTypeViewHolder.liveStatus.setTextColor(ContextCompat.getColor(mContext,
                        R.color.white));
                itemTypeViewHolder.liveStatusDot.setVisibility(View.GONE);
                itemTypeViewHolder.equalizer.setVisibility(View.GONE);
            } else {

//                itemTypeViewHolder.liveStatus.setText(R.string.live);
//                itemTypeViewHolder.liveStatus.setTextColor(ContextCompat.getColor(mContext,  R.color.witkey_orange));
//                itemTypeViewHolder.liveStatusDot.setVisibility(View.VISIBLE);
//                itemTypeViewHolder.liveStatusDot.startAnimation(Animations.getBlinkAnimationInstance());
                itemTypeViewHolder.equalizer.setVisibility(View.VISIBLE);
                itemTypeViewHolder.liveStatus.setVisibility(View.GONE);
                Glide.with(mContext).load(R.raw.equalizer_1).asGif().into(itemTypeViewHolder.equalizer);
            }

            itemTypeViewHolder.mViewsTextView.setText(getViewersCount(hottestBO.getTotalViewers()));


               /* if (listPosition % 2 == 0) {
                    ((LinearLayout.LayoutParams) itemTypeViewHolder.mHottestCardImageView.getLayoutParams()).setMargins(margin, margin, margin, 0);
                } else {
                    ((LinearLayout.LayoutParams) itemTypeViewHolder.mHottestCardImageView.getLayoutParams()).setMargins(0, margin, 0, 0);
                }*/

        } else if (holder instanceof ItemTypeOneIndexViewHolder) {
            ItemTypeOneIndexViewHolder itemTypeOneIndexViewHolder = (ItemTypeOneIndexViewHolder) holder;

            itemTypeOneIndexViewHolder.hottestCardTopView.setVisibility(View.VISIBLE);
            itemTypeOneIndexViewHolder.micParent.setVisibility(View.VISIBLE);
            itemTypeOneIndexViewHolder.micTextView.setVisibility(View.VISIBLE);
            itemTypeOneIndexViewHolder.hottestTextView.setVisibility(View.VISIBLE);
            if (hottestBO.getUser_details().getUserProgressDetailBO() != null && !TextUtils.isEmpty(hottestBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "")) {
                itemTypeOneIndexViewHolder.micTextView.setText(hottestBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "");//(hottestBO.getId()); //listPosition
            } else {
                itemTypeOneIndexViewHolder.micTextView.setText(listPosition + "");//(hottestBO.getId()); //listPosition
            }
            if (hottestBO.getUser_details().getUser_complete_id().contains("-")) {
                itemTypeOneIndexViewHolder.hottestTextView.setText(hottestBO.getUser_details().getUser_complete_id().split("-")[0] + " " + hottestBO.getUser_details().getUsername());
            } else {
                itemTypeOneIndexViewHolder.hottestTextView.setText(hottestBO.getUser_details().getUser_complete_id() + " " + hottestBO.getUser_details().getUsername());
            }
//            itemTypeOneIndexViewHolder.hottestTextView.setText(Utils.getShortString(hottestBO.getUser_details().getId() + " " + hottestBO.getUser_details().getUsername(), 15));
//                Utils.setImageSimple(itemTypeOneIndexViewHolder.mHottestCardImageView, hottestBO.getUser_details().profilePictureUrl, mContext);
            //Utils.setImageSimpleWitkey(itemTypeOneIndexViewHolder.mHottestCardImageView, hottestBO.getUser_details().getProfilePictureUrl(), mContext);
            Utils.setImageCornersRoundWitkey(itemTypeOneIndexViewHolder.mHottestCardImageView, hottestBO.getUser_details().getProfilePictureUrl(), mContext, 10f);
            ((RelativeLayout.LayoutParams) itemTypeOneIndexViewHolder.mHottestCardImageView.getLayoutParams()).setMargins(10, 5, 10, 5);

            itemTypeOneIndexViewHolder.mHottestCardImageView.getLayoutParams().height = Utils.getScreenHeight(mContext) / 2;
            itemTypeOneIndexViewHolder.mParentFrameHottest.getLayoutParams().height = Utils.getScreenHeight(mContext) / 2+100;
            if (hottestBO.getCreatedAt() != null && !hottestBO.getCreatedAt().isEmpty()) {
                itemTypeOneIndexViewHolder.mLastSeenTextView.setText(Utils.getDifBtwn2Dates(mContext, hottestBO.getCreatedAt()));
//                itemTypeOneIndexViewHolder.mLastSeenTextView.setText(Utils.howMuchTimePastFromNow(mContext, streamBO.getCreatedAt(), ""));
            } else {
                itemTypeOneIndexViewHolder.mLastSeenTextView.setText("1 " + mContext.getString(R.string.hr_ago));
            }

            if (hottestBO.getStatus().equals(StreamUtils.STATUS_ENDED)) {

                itemTypeOneIndexViewHolder.liveStatus.setVisibility(View.VISIBLE);
                itemTypeOneIndexViewHolder.liveStatus.setText(R.string.ended);
                itemTypeOneIndexViewHolder.liveStatus.setTextColor(ContextCompat.getColor(mContext,
                        R.color.white));
                itemTypeOneIndexViewHolder.liveStatusDot.setVisibility(View.GONE);
                itemTypeOneIndexViewHolder.equalizer.setVisibility(View.GONE);
            } else {

                /*itemTypeOneIndexViewHolder.liveStatus.setText(R.string.live);
                itemTypeOneIndexViewHolder.liveStatus.setTextColor(ContextCompat.getColor(mContext,
                        R.color.witkey_orange));
                itemTypeOneIndexViewHolder.liveStatusDot.setVisibility(View.VISIBLE);
                itemTypeOneIndexViewHolder.liveStatusDot.startAnimation(Animations.getBlinkAnimationInstance());*/
                itemTypeOneIndexViewHolder.equalizer.setVisibility(View.VISIBLE);
                itemTypeOneIndexViewHolder.liveStatus.setVisibility(View.GONE);
                Glide.with(mContext).load(R.raw.equalizer_1).asGif().into(itemTypeOneIndexViewHolder.equalizer);
            }

            itemTypeOneIndexViewHolder.mViewsTextView.setText(getViewersCount(hottestBO.getTotalViewers()));
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
                    return count_ + "." + count_hundred_points + "K " + mContext.getString(R.string.viewers);
                }

            } catch (Exception e) {
                LogUtils.e("getViewersCount", "" + e.getMessage());
                return "0 " + mContext.getString(R.string.viewer);
            }
        } else {
            return "0 " + mContext.getString(R.string.viewer);
        }
    }

    private static ImageView[] addIndicators(Context mContext, LinearLayout view, int count) {
        ImageView[] imageViews = new ImageView[count];
        view.removeAllViews();

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                imageViews[i] = new ImageView(mContext);
                imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_un_selected));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(18, 18);
                params.setMargins(4, 0, 4, 0);

                view.addView(imageViews[i], params);
            }
            imageViews[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_selected));
        }
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
