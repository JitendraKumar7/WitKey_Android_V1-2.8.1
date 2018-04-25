package app.witkey.live.adapters.dashboard.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.PixelsOp;
import app.witkey.live.utils.StreamUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewGoddessMachoAdapter extends RecyclerView.Adapter {

    private List<StreamBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 1;
    private boolean isLoading;
    String tempURL = "https://www.w3schools.com/w3css/img_avatar3.png";
    private int recyclerViewHeight;
//    private int margin;

    //constructor...
    public NewGoddessMachoAdapter(List<StreamBO> data, Context context, RecyclerView mRecyclerView, int recyclerViewHeight) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        this.recyclerViewHeight = recyclerViewHeight;

//        margin = (int) PixelsOp.pxFromDp(mContext, 10);


        final GridLayoutManager linearLayoutManager = (GridLayoutManager) mRecyclerView.getLayoutManager();
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

        @BindView(R.id.mParentFrameNewGoddessMacho)
        LinearLayout mParentFrameNewGoddessMacho;
        @BindView(R.id.imageProfileView)
        LinearLayout imageProfileView;

        @BindView(R.id.mNewGoddessMachoCardImageView)
        ImageView mNewGoddessMachoCardImageView;

        @BindView(R.id.micTextView)
        TextView micTextView;

        @BindView(R.id.liveStatus)
        CustomTextView liveStatus;

        @BindView(R.id.hottestTextView)
        TextView hottestTextView;

        @BindView(R.id.mViewsTextView)
        TextView mViewsTextView;

        @BindView(R.id.mLastSeenTextView)
        TextView mLastSeenTextView;

        @BindView(R.id.liveStatusDot)
        ImageView liveStatusDot;
        @BindView(R.id.equalizer)
        ImageView equalizer;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mNewGoddessMachoCardImageView.setOnClickListener(this);
            this.imageProfileView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mNewGoddessMachoCardImageView:
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

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public RelativeLayout loadMoreParent;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            loadMoreParent = (RelativeLayout) itemView.findViewById(R.id.loadMoreParent);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
        }
    }


    public void setLoaded() {
        isLoading = false;
    }


    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public void addItems(List<StreamBO> StreamBOList) {
        dataSet.addAll(StreamBOList);
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_new_goddess_macho_item, parent, false);
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
        if (holder instanceof ItemTypeViewHolder) {
            final ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;


            itemTypeViewHolder.mNewGoddessMachoCardImageView.getLayoutParams().height = Utils.getScreenHeight(mContext) / 4;
            itemTypeViewHolder.mParentFrameNewGoddessMacho.getLayoutParams().height = Utils.getScreenHeight(mContext) / 4+100;
//            itemTypeViewHolder.micTextView.setText(listPosition + "");
            if (streamBO.getUser_details().getUserProgressDetailBO() != null && !TextUtils.isEmpty(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "")) {
                itemTypeViewHolder.micTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "");//(hottestBO.getId()); //listPosition
            } else {
                itemTypeViewHolder.micTextView.setText(listPosition + "");//(hottestBO.getId()); //listPosition
            }
//            itemTypeViewHolder.hottestTextView.setText(Utils.getShortString(streamBO.getUser_details().getId() + " " + streamBO.getUser_details().username, 15));

            if (streamBO.getUser_details().getUser_complete_id().contains("-")) {
                itemTypeViewHolder.hottestTextView.setText(streamBO.getUser_details().getUser_complete_id().split("-")[0] + " " + streamBO.getUser_details().getUsername());
            } else {
                itemTypeViewHolder.hottestTextView.setText(streamBO.getUser_details().getUser_complete_id() + " " + streamBO.getUser_details().getUsername());
            }

//            Utils.setImageCornersRound(itemTypeViewHolder.mNewGoddessMachoCardImageView, streamBO.getUser_details().getProfilePictureUrl(), mContext);
            Utils.setImageCornersRoundWitkey(itemTypeViewHolder.mNewGoddessMachoCardImageView, streamBO.getUser_details().getProfilePictureUrl(), mContext, 10f);
            ((RelativeLayout.LayoutParams) itemTypeViewHolder.mNewGoddessMachoCardImageView.getLayoutParams()).setMargins(11, 11, 11, 11);

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
                itemTypeViewHolder.equalizer.setVisibility(View.VISIBLE);
                itemTypeViewHolder.liveStatus.setVisibility(View.GONE);
                Glide.with(mContext).load(R.raw.equalizer_1).asGif().into(itemTypeViewHolder.equalizer);
            }

            if (streamBO.getCreatedAt() != null && !streamBO.getCreatedAt().isEmpty()) {
                itemTypeViewHolder.mLastSeenTextView.setText(Utils.getDifBtwn2Dates(mContext, streamBO.getCreatedAt()));
//                itemTypeViewHolder.mLastSeenTextView.setText(Utils.howMuchTimePastFromNow(mContext, streamBO.getCreatedAt(), ""));
            } else {
                itemTypeViewHolder.mLastSeenTextView.setText("1 " + mContext.getString(R.string.hr_ago));
            }
            itemTypeViewHolder.mViewsTextView.setText(getViewersCount(streamBO.getTotalViewers()));

          /*  if (listPosition % 2 == 0) {
                ((RelativeLayout.LayoutParams) itemTypeViewHolder.ivModel.getLayoutParams()).setMargins(margin, margin, margin / 2, margin);
            } else {
                ((RelativeLayout.LayoutParams) itemTypeViewHolder.ivModel.getLayoutParams()).setMargins(margin / 2, margin, margin, margin);
            }*/

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.loadMoreParent.getLayoutParams().width = Utils.getScreenWidth(mContext);
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
}
