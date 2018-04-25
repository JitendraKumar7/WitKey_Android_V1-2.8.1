package app.witkey.live.adapters.dashboard.home;

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
import app.witkey.live.items.UserBO;
import app.witkey.live.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class BroadcasterAdapter extends RecyclerView.Adapter {

    private List<UserBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 1;
    public final int VIEW_TYPE_ITEM_TOP = 0;
    private final int VIEW_TYPE_LOADING = 2;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    int TOP_SPENDER = 0, TOP_BROADCASTER = 1, TOP_FAN = 2;
    private boolean isLoading;
    int backImage;
    int topIndexCount = 6;

    private int margin;

    //constructor...
    public BroadcasterAdapter(List<UserBO> data, Context context, RecyclerView mRecyclerView, int backImageType) {
        this.dataSet = data;
        this.mContext = context;
        this.backImage = backImageType;
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

    class TopItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.firstIndex)
        LinearLayout firstIndex;

        @BindView(R.id.broadcasterTopParent)
        LinearLayout broadcasterTopParent;

        @BindView(R.id.secondIndex)
        LinearLayout secondIndex;

        @BindView(R.id.thirdIndex)
        LinearLayout thirdIndex;

        @BindView(R.id.fourthIndex)
        LinearLayout fourthIndex;

        @BindView(R.id.fifthIndex)
        LinearLayout fifthIndex;

        @BindView(R.id.additionalView)
        LinearLayout additionalView;

        @BindView(R.id.additionalViewNew)
        LinearLayout additionalViewNew;


        @BindView(R.id.broadcasterImageFirst_IV)
        ImageView broadcasterImageFirst_IV;

        @BindView(R.id.backImage)
        ImageView backImage;

        @BindView(R.id.broadcasterImageSecond_IV)
        ImageView broadcasterImageSecond_IV;

        @BindView(R.id.broadcasterImageThird_IV)
        ImageView broadcasterImageThird_IV;

        @BindView(R.id.broadcasterImageFouth_IV)
        ImageView broadcasterImageFouth_IV;

        @BindView(R.id.broadcasterImageFifth_IV)
        ImageView broadcasterImageFifth_IV;

        @BindView(R.id.broadcasterImageBottom_IV)
        ImageView broadcasterImageBottom_IV;

        @BindView(R.id.broadcasterImageBottom_IV_new)
        ImageView broadcasterImageBottom_IV_new;

        @BindView(R.id.broadcasterNameFirst_TV)
        TextView broadcasterNameFirst_TV;

        @BindView(R.id.broadcasterNameSecond_TV)
        TextView broadcasterNameSecond_TV;

        @BindView(R.id.broadcasterNameThird_TV)
        TextView broadcasterNameThird_TV;

        @BindView(R.id.broadcasterNameFourth_TV)
        TextView broadcasterNameFourth_TV;

        @BindView(R.id.broadcasterNameFifth_TV)
        TextView broadcasterNameFifth_TV;

        @BindView(R.id.broadcasterName_TV)
        TextView broadcasterName_TV;

        @BindView(R.id.broadcasterName_TV_new)
        TextView broadcasterName_TV_new;

        @BindView(R.id.broadcasterPosition_TV)
        TextView broadcasterPosition_TV;

        @BindView(R.id.broadcasterPosition_TV_new)
        TextView broadcasterPosition_TV_new;

        @BindView(R.id.broadcasterPositionFirst_TV)
        TextView broadcasterPositionFirst_TV;

        @BindView(R.id.broadcasterPositionSecond_TV)
        TextView broadcasterPositionSecond_TV;

        @BindView(R.id.broadcasterPositionThird_TV)
        TextView broadcasterPositionThird_TV;

        @BindView(R.id.broadcasterPositionFourth_TV)
        TextView broadcasterPositionFourth_TV;

        @BindView(R.id.broadcasterPositionFifth_TV)
        TextView broadcasterPositionFifth_TV;
        @BindView(R.id.IndexBottom_new)
        LinearLayout IndexBottom_new;
        @BindView(R.id.IndexBottom)
        LinearLayout IndexBottom;

        public TopItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.firstIndex.setOnClickListener(this);
            this.secondIndex.setOnClickListener(this);
            this.thirdIndex.setOnClickListener(this);
            this.fourthIndex.setOnClickListener(this);
            this.fifthIndex.setOnClickListener(this);
            this.additionalView.setOnClickListener(this);
            this.additionalViewNew.setOnClickListener(this);

            this.broadcasterImageFirst_IV.setOnClickListener(this);
            this.broadcasterImageSecond_IV.setOnClickListener(this);
            this.broadcasterImageThird_IV.setOnClickListener(this);
            this.broadcasterImageFouth_IV.setOnClickListener(this);
            this.broadcasterImageFifth_IV.setOnClickListener(this);
            this.IndexBottom_new.setOnClickListener(this);
            this.IndexBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.firstIndex:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.secondIndex:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.broadcasterImageFirst_IV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.broadcasterImageSecond_IV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.broadcasterImageThird_IV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.broadcasterImageFouth_IV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.broadcasterImageFifth_IV:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.IndexBottom_new:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.additionalView:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.additionalViewNew:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
            }
        }
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.IndexBottom)
        LinearLayout IndexBottom;

        @BindView(R.id.broadcasterPosition_TV)
        TextView broadcasterPosition_TV;

        @BindView(R.id.broadcasterImageBottom_IV)
        ImageView broadcasterImageBottom_IV;

        @BindView(R.id.broadcasterName_TV)
        TextView broadcasterName_TV;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.IndexBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.IndexBottom:
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

    public void addItems(List<UserBO> BroadcasterBOList) {
        dataSet.addAll(BroadcasterBOList);
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
            case VIEW_TYPE_ITEM_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_boradcaster_top_with_crown, parent, false);
                return new TopItemTypeViewHolder(view);
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_boradcaster_bottom, parent, false);
                return new ItemTypeViewHolder(view);
            case VIEW_TYPE_LOADING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_loading_item, parent, false);
                return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) == null) {
            return VIEW_TYPE_LOADING;
//        } else if (dataSet.get(position).isIndexTop()) {
        } else if (position == 0) {
            return VIEW_TYPE_ITEM_TOP;
        } else {
            return VIEW_TYPE_ITEM;
        }
        //        return dataSet.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        UserBO broadcasterBO = dataSet.get(listPosition);
        if (holder instanceof TopItemTypeViewHolder) {
            TopItemTypeViewHolder topItemTypeViewHolder = (TopItemTypeViewHolder) holder;

            if (listPosition == 0) {

                if (Utils.getScreenHeight(mContext) > 0) {

                    int screenHeight = Utils.getScreenHeight(mContext);
                    int newScreenHeight = (screenHeight / 5) - Utils.dp2px(mContext, 0);
                    int newScreenWidth = (Utils.getScreenWidth(mContext) / 3);
                  /*topItemTypeViewHolder.firstIndex.getLayoutParams().height = newScreenHeight;
                    topItemTypeViewHolder.firstIndex.getLayoutParams().width = newScreenWidth;
                    topItemTypeViewHolder.secondIndex.getLayoutParams().height = newScreenHeight;
                    topItemTypeViewHolder.secondIndex.getLayoutParams().width = newScreenWidth;
                    topItemTypeViewHolder.thirdIndex.getLayoutParams().height = newScreenHeight;
                    topItemTypeViewHolder.thirdIndex.getLayoutParams().width = newScreenWidth;
                    topItemTypeViewHolder.fourthIndex.getLayoutParams().height = newScreenHeight;
                    topItemTypeViewHolder.fourthIndex.getLayoutParams().width = newScreenWidth;
                    topItemTypeViewHolder.fifthIndex.getLayoutParams().height = newScreenHeight;
                    topItemTypeViewHolder.fifthIndex.getLayoutParams().width = newScreenWidth;*/
                    topItemTypeViewHolder.additionalView.getLayoutParams().height = screenHeight / 6 - Utils.dp2px(mContext, 10);
                    topItemTypeViewHolder.additionalViewNew.getLayoutParams().height = screenHeight / 6 - Utils.dp2px(mContext, 10);
                    ((RelativeLayout.LayoutParams) topItemTypeViewHolder.fifthIndex.getLayoutParams()).setMarginEnd((topItemTypeViewHolder.firstIndex.getLayoutParams().width / 3));
                    ((RelativeLayout.LayoutParams) topItemTypeViewHolder.fourthIndex.getLayoutParams()).setMarginStart((topItemTypeViewHolder.firstIndex.getLayoutParams().width / 3));
//                  topItemTypeViewHolder.broadcasterTopParent.getLayoutParams().height = (newScreenHeight * 3) + (screenHeight / 6) + (screenHeight / 6) - Utils.dp2px(mContext, 20);//50;
                }

                setBackImage(topItemTypeViewHolder.backImage);

                switch (dataSet.size()) {
                    case 1:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");

                        topItemTypeViewHolder.secondIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.thirdIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.fourthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.fifthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalView.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalViewNew.setVisibility(View.INVISIBLE);

                        break;
                    case 2:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getProfilePictureUrl(), mContext);
                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUsername());
                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText("2nd");
                        topItemTypeViewHolder.thirdIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.fourthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.fifthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalView.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalViewNew.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, dataSet.get(2).getProfilePictureUrl(), mContext);
                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUsername());
                        topItemTypeViewHolder.broadcasterNameThird_TV.setText(dataSet.get(2).getUsername());
                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText("2nd");
                        topItemTypeViewHolder.broadcasterPositionThird_TV.setText("3rd");
                        topItemTypeViewHolder.fourthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.fifthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalView.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalViewNew.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, dataSet.get(2).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFouth_IV, dataSet.get(3).getProfilePictureUrl(), mContext);
                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUsername());
                        topItemTypeViewHolder.broadcasterNameThird_TV.setText(dataSet.get(2).getUsername());
                        topItemTypeViewHolder.broadcasterNameFourth_TV.setText(dataSet.get(3).getUsername());
                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText("2nd");
                        topItemTypeViewHolder.broadcasterPositionThird_TV.setText("3rd");
                        topItemTypeViewHolder.broadcasterPositionFourth_TV.setText("4th");
                        topItemTypeViewHolder.fifthIndex.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalView.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalViewNew.setVisibility(View.INVISIBLE);

                        break;
                    case 5:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, dataSet.get(2).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFouth_IV, dataSet.get(3).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFifth_IV, dataSet.get(4).getProfilePictureUrl(), mContext);
                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUsername());
                        topItemTypeViewHolder.broadcasterNameThird_TV.setText(dataSet.get(2).getUsername());
                        topItemTypeViewHolder.broadcasterNameFourth_TV.setText(dataSet.get(3).getUsername());
                        topItemTypeViewHolder.broadcasterNameFifth_TV.setText(dataSet.get(4).getUsername());
                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText("2nd");
                        topItemTypeViewHolder.broadcasterPositionThird_TV.setText("3rd");
                        topItemTypeViewHolder.broadcasterPositionFourth_TV.setText("4th");
                        topItemTypeViewHolder.broadcasterPositionFifth_TV.setText("5th");
                        topItemTypeViewHolder.additionalView.setVisibility(View.INVISIBLE);
                        topItemTypeViewHolder.additionalViewNew.setVisibility(View.INVISIBLE);
                        break;
                    case 6:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, dataSet.get(2).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFouth_IV, dataSet.get(3).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFifth_IV, dataSet.get(4).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV, dataSet.get(5).getProfilePictureUrl(), mContext);

                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUsername());
                        topItemTypeViewHolder.broadcasterNameThird_TV.setText(dataSet.get(2).getUsername());
                        topItemTypeViewHolder.broadcasterNameFourth_TV.setText(dataSet.get(3).getUsername());
                        topItemTypeViewHolder.broadcasterNameFifth_TV.setText(dataSet.get(4).getUsername());
                        topItemTypeViewHolder.broadcasterName_TV.setText(dataSet.get(5).getUsername());

                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText("2nd");
                        topItemTypeViewHolder.broadcasterPositionThird_TV.setText("3rd");
                        topItemTypeViewHolder.broadcasterPositionFourth_TV.setText("4th");
                        topItemTypeViewHolder.broadcasterPositionFifth_TV.setText("5th");
//                        topItemTypeViewHolder.broadcasterPosition_TV.setText(dataSet.get(5).getUser_details().getId() + ".");
                        topItemTypeViewHolder.broadcasterPosition_TV.setText("6.");
                        topItemTypeViewHolder.additionalViewNew.setVisibility(View.INVISIBLE);
                        break;
                    /*case 7:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getUser_details().getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getUser_details().getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, dataSet.get(2).getUser_details().getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFouth_IV, dataSet.get(3).getUser_details().getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFifth_IV, dataSet.get(4).getUser_details().getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV, dataSet.get(5).getUser_details().getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV_new, dataSet.get(6).getUser_details().getProfilePictureUrl(), mContext);

                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUser_details().getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUser_details().getUsername());
                        topItemTypeViewHolder.broadcasterNameThird_TV.setText(dataSet.get(2).getUser_details().getUsername());
                        topItemTypeViewHolder.broadcasterNameFourth_TV.setText(dataSet.get(3).getUser_details().getUsername());
                        topItemTypeViewHolder.broadcasterNameFifth_TV.setText(dataSet.get(4).getUser_details().getUsername());
                        topItemTypeViewHolder.broadcasterName_TV.setText(dataSet.get(5).getUser_details().getUsername());
                        topItemTypeViewHolder.broadcasterName_TV_new.setText(dataSet.get(6).getUser_details().getUsername());

                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText(dataSet.get(0).getUser_details().getId() + "st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText(dataSet.get(1).getUser_details().getId() + "nd");
                        topItemTypeViewHolder.broadcasterPositionThird_TV.setText(dataSet.get(2).getUser_details().getId() + "rd");
                        topItemTypeViewHolder.broadcasterPositionFourth_TV.setText(dataSet.get(3).getUser_details().getId() + "th");
                        topItemTypeViewHolder.broadcasterPositionFifth_TV.setText(dataSet.get(4).getUser_details().getId() + "th");
                        topItemTypeViewHolder.broadcasterPosition_TV.setText(dataSet.get(5).getUser_details().getId() + ".");
                        topItemTypeViewHolder.broadcasterPosition_TV_new.setText(dataSet.get(6).getUser_details().getId() + ".");

                        break;*/
                    default:
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, dataSet.get(0).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, dataSet.get(1).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, dataSet.get(2).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFouth_IV, dataSet.get(3).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFifth_IV, dataSet.get(4).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV, dataSet.get(5).getProfilePictureUrl(), mContext);
                        Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV_new, dataSet.get(6).getProfilePictureUrl(), mContext);

                        topItemTypeViewHolder.broadcasterNameFirst_TV.setText(dataSet.get(0).getUsername());
                        topItemTypeViewHolder.broadcasterNameSecond_TV.setText(dataSet.get(1).getUsername());
                        topItemTypeViewHolder.broadcasterNameThird_TV.setText(dataSet.get(2).getUsername());
                        topItemTypeViewHolder.broadcasterNameFourth_TV.setText(dataSet.get(3).getUsername());
                        topItemTypeViewHolder.broadcasterNameFifth_TV.setText(dataSet.get(4).getUsername());
                        topItemTypeViewHolder.broadcasterName_TV.setText(dataSet.get(5).getUsername());
                        topItemTypeViewHolder.broadcasterName_TV_new.setText(dataSet.get(6).getUsername());

                        topItemTypeViewHolder.broadcasterPositionFirst_TV.setText("1st");
                        topItemTypeViewHolder.broadcasterPositionSecond_TV.setText("2nd");
                        topItemTypeViewHolder.broadcasterPositionThird_TV.setText("3rd");
                        topItemTypeViewHolder.broadcasterPositionFourth_TV.setText("4th");
                        topItemTypeViewHolder.broadcasterPositionFifth_TV.setText("5th");
                        topItemTypeViewHolder.broadcasterPosition_TV.setText("6.");
//                        topItemTypeViewHolder.broadcasterPosition_TV.setText(dataSet.get(5).getUser_details().getId() + ".");
                        topItemTypeViewHolder.broadcasterPosition_TV_new.setText("7.");
//                        topItemTypeViewHolder.broadcasterPosition_TV_new.setText(dataSet.get(6).getUser_details().getId() + ".");

                        break;
                }
                /*Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFirst_IV, broadcasterBO.getBroadcasterTopBO().get(0).getUserImageURL(), mContext);
                Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageSecond_IV, broadcasterBO.getBroadcasterTopBO().get(1).getUserImageURL(), mContext);
                Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageThird_IV, broadcasterBO.getBroadcasterTopBO().get(2).getUserImageURL(), mContext);
                Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFouth_IV, broadcasterBO.getBroadcasterTopBO().get(3).getUserImageURL(), mContext);
                Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageFifth_IV, broadcasterBO.getBroadcasterTopBO().get(4).getUserImageURL(), mContext);
                Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV, broadcasterBO.getBroadcasterTopBO().get(5).getUserImageURL(), mContext);
                Utils.setImageRounded(topItemTypeViewHolder.broadcasterImageBottom_IV_new, broadcasterBO.getBroadcasterTopBO().get(6).getUserImageURL(), mContext);

                topItemTypeViewHolder.broadcasterNameFirst_TV.setText(broadcasterBO.getBroadcasterTopBO().get(0).getUserName());
                topItemTypeViewHolder.broadcasterNameSecond_TV.setText(broadcasterBO.getBroadcasterTopBO().get(1).getUserName());
                topItemTypeViewHolder.broadcasterNameThird_TV.setText(broadcasterBO.getBroadcasterTopBO().get(2).getUserName());
                topItemTypeViewHolder.broadcasterNameFourth_TV.setText(broadcasterBO.getBroadcasterTopBO().get(3).getUserName());
                topItemTypeViewHolder.broadcasterNameFifth_TV.setText(broadcasterBO.getBroadcasterTopBO().get(4).getUserName());
                topItemTypeViewHolder.broadcasterName_TV.setText(broadcasterBO.getBroadcasterTopBO().get(5).getUserName());
                topItemTypeViewHolder.broadcasterName_TV_new.setText(broadcasterBO.getBroadcasterTopBO().get(6).getUserName());

                topItemTypeViewHolder.broadcasterPositionFirst_TV.setText(broadcasterBO.getBroadcasterTopBO().get(0).getUserPosition() + "st");
                topItemTypeViewHolder.broadcasterPositionSecond_TV.setText(broadcasterBO.getBroadcasterTopBO().get(1).getUserPosition() + "nd");
                topItemTypeViewHolder.broadcasterPositionThird_TV.setText(broadcasterBO.getBroadcasterTopBO().get(2).getUserPosition() + "rd");
                topItemTypeViewHolder.broadcasterPositionFourth_TV.setText(broadcasterBO.getBroadcasterTopBO().get(3).getUserPosition() + "th");
                topItemTypeViewHolder.broadcasterPositionFifth_TV.setText(broadcasterBO.getBroadcasterTopBO().get(4).getUserPosition() + "th");
                topItemTypeViewHolder.broadcasterPosition_TV.setText(broadcasterBO.getBroadcasterTopBO().get(5).getUserPosition() + ".");
                topItemTypeViewHolder.broadcasterPosition_TV_new.setText(broadcasterBO.getBroadcasterTopBO().get(6).getUserPosition() + ".");*/
            }

        } else if (holder instanceof ItemTypeViewHolder) {
            if (dataSet.size() > listPosition + topIndexCount) {

                UserBO streamBO = dataSet.get(listPosition + topIndexCount);
                if (streamBO != null) {

                    ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

                    if (Utils.getScreenHeight(mContext) > 0) {

//                itemTypeViewHolder.IndexBottom.getLayoutParams().height = (Utils.getScreenHeight(mContext) / 6) - Utils.dp2px(mContext, 10);
                    }
                    Utils.setImageRounded(itemTypeViewHolder.broadcasterImageBottom_IV, streamBO.getProfilePictureUrl(), mContext);
                    itemTypeViewHolder.broadcasterName_TV.setText(streamBO.getUsername());
                    itemTypeViewHolder.broadcasterPosition_TV.setText((listPosition + 7) + ".");
                }
            }
        } else if (holder instanceof LoadingViewHolder)

        {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
//        return dataSet.size();// - topIndexCount;
        if (dataSet.size() > topIndexCount) {
            return dataSet.size() - topIndexCount;
        } else {
            return dataSet.size();
        }
    }

    /*Listeners*/
    ClickListeners clickListener;

    public void setClickListener(ClickListeners clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListeners {
        void onRowClick(View view, int position);
    }

    private void setBackImage(ImageView imageView) {
        if (backImage == TOP_SPENDER) {
            imageView.setBackgroundResource(R.drawable.bg7);
        } else if (backImage == TOP_BROADCASTER) {
            imageView.setBackgroundResource(R.drawable.bg8);
        } else if (backImage == TOP_FAN) {
            imageView.setBackgroundResource(R.drawable.bg9);
        }

    }

}
