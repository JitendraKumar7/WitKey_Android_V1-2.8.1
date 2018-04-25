package app.witkey.live.adapters.dashboard.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.UserBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyBroadcasterAdapter extends RecyclerView.Adapter {

    private List<UserBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;
    UserBO userBO;

    //constructor...
    public MyBroadcasterAdapter(List<UserBO> data, Context context, RecyclerView mRecyclerView) {
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

        @BindView(R.id.mFanParentFrame)
        RelativeLayout mFanParentFrame;

        @BindView(R.id.fanImageView)
        ImageView fanImageView;

        @BindView(R.id.fanNameCustomTextView)
        CustomTextView fanNameCustomTextView;

        @BindView(R.id.btnFanFollow)
        Button btnFanFollow;
        @BindView(R.id.fanParentFrame)
        RelativeLayout fanParentFrame;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.btnFanFollow.setOnClickListener(this);
//            this.fanImageView.setOnClickListener(this);
//            this.fanNameCustomTextView.setOnClickListener(this);
            this.fanParentFrame.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnFanFollow:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
//                case R.id.fanNameCustomTextView:
//                case R.id.fanImageView:
                case R.id.fanParentFrame:
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

    public void addItems(List<UserBO> fanBOList) {
        dataSet.addAll(fanBOList);
        notifyDataSetChanged();
    }

    public List<UserBO> getItems() {
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

    public UserBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fan_item, parent, false);
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

        UserBO fanBO = dataSet.get(listPosition);

        if (holder instanceof ItemTypeViewHolder) {
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            Utils.setImageRounded(itemTypeViewHolder.fanImageView, fanBO.getProfilePictureUrl(), mContext);
            itemTypeViewHolder.fanNameCustomTextView.setText(Utils.getShortString(fanBO.getUsername(), 15));
            if (fanBO.getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                itemTypeViewHolder.btnFanFollow.setText(R.string.un_follow);
            } else {
                itemTypeViewHolder.btnFanFollow.setText(R.string.follow);
            }

            if (userBO != null) {
                if (fanBO.getId().equals(userBO.getId())) {
                    itemTypeViewHolder.btnFanFollow.setVisibility(View.GONE);
                } else {
                    itemTypeViewHolder.btnFanFollow.setVisibility(View.VISIBLE);
                }
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
        void onRowClick(View view, int position);

    }

}
