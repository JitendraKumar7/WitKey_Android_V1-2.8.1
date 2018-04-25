package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.UserBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter {

    private List<UserBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;
    boolean viewTypeDisable = false;
    UserBO userBO;

    //constructor...
    public NotificationsAdapter(List<UserBO> data, Context context, RecyclerView mRecyclerView, boolean viewTypeDisable) {
        this.dataSet = data;
        this.viewTypeDisable = viewTypeDisable;
        this.mContext = context;
        total_types = dataSet.size();

        userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);

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

        @BindView(R.id.notificationParentFrame)
        RelativeLayout notificationParentFrame;

        @BindView(R.id.notificationPersonImageView)
        ImageView notificationPersonImageView;

        @BindView(R.id.notificationNameCustomTextView)
        CustomTextView notificationNameCustomTextView;

        @BindView(R.id.notificationSwitch)
        SwitchButton notificationSwitch;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.notificationParentFrame.setOnClickListener(this);
            this.notificationPersonImageView.setOnClickListener(this);
            this.notificationNameCustomTextView.setOnClickListener(this);
            this.notificationSwitch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.notificationSwitch:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.notificationParentFrame:
                case R.id.notificationPersonImageView:
                case R.id.notificationNameCustomTextView:
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

    public void viewDisable(boolean viewDisable) {
        viewTypeDisable = viewDisable;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
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

            Utils.setImageRounded(itemTypeViewHolder.notificationPersonImageView, fanBO.getProfilePictureUrl(), mContext);
            itemTypeViewHolder.notificationNameCustomTextView.setText(Utils.getShortString(fanBO.getUsername(), 15));
//            if (fanBO.getAllow_notification() == 1) { /*1 FOR ALLOW 2 FOR NO ALLOW*/

            itemTypeViewHolder.notificationSwitch.setEnabled(!viewTypeDisable);
            itemTypeViewHolder.notificationSwitch.setChecked(fanBO.getAllow_notification() == 1);

//            } else {
//                itemTypeViewHolder.btnFanFollow.setText(R.string.follow);
//            }

            if (userBO != null) {
                if (fanBO.getId().equals(userBO.getId())) {
                    itemTypeViewHolder.notificationSwitch.setVisibility(View.GONE);
                } else {
                    itemTypeViewHolder.notificationSwitch.setVisibility(View.VISIBLE);
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
