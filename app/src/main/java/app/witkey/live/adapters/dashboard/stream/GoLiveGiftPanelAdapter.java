package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Random;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.GiftPanelBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.animations.BubbleViewAnimation;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveGiftPanelAdapter extends RecyclerView.Adapter {

    private List<GiftPanelBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;
    private BubbleViewAnimation bubbleViewAnimation;

    private RecyclerView mRecyclerView;

    private int selectedIndex = -1;

    Button btnSubmit;

    //constructor...
    public GoLiveGiftPanelAdapter(List<GiftPanelBO> data, Context context, RecyclerView mRecyclerView, BubbleViewAnimation _bubbleViewAnimation, Button btnSubmit) {
        this.dataSet = data;
        this.mContext = context;
        this.bubbleViewAnimation = _bubbleViewAnimation;
        total_types = dataSet.size();
        this.mRecyclerView = mRecyclerView;
        this.btnSubmit = btnSubmit;
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mGiftPanelParentFrame)
        RelativeLayout mGiftPanelParentFrame;

        @BindView(R.id.giftPanelParentFrame)
        RelativeLayout giftPanelParentFrame;

        @BindView(R.id.giftPanelQuantity)
        CustomTextView giftPanelQuantity;

        @BindView(R.id.giftPanelImageView)
        ImageView giftPanelImageView;
        @BindView(R.id.coinsIV)
        ImageView coinsIV;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mGiftPanelParentFrame.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mGiftPanelParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(getAdapterPosition());
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

    public void addItems(List<GiftPanelBO> giftPanelBOList) {
        dataSet.addAll(giftPanelBOList);
        notifyDataSetChanged();
    }

    public List<GiftPanelBO> getItems() {
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

    public GiftPanelBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_go_live_gift_panel_item, parent, false);
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

        final GiftPanelBO giftPanelBO = dataSet.get(listPosition);

        if (holder instanceof ItemTypeViewHolder) {
            final ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

            itemTypeViewHolder.giftPanelParentFrame.getLayoutParams().width = Utils.getScreenWidth(mContext) / 5;
            itemTypeViewHolder.giftPanelParentFrame.getLayoutParams().height = Utils.getScreenWidth(mContext) / 5;

            itemTypeViewHolder.giftPanelQuantity.setText(giftPanelBO.getPrice() + "");
            itemTypeViewHolder.giftPanelImageView.setImageResource(giftPanelBO.getLocalImage());

            if (giftPanelBO.isSelected()) {
                itemTypeViewHolder.giftPanelParentFrame.setBackground(mContext.getResources().getDrawable(R.drawable.view_border));
                if (giftPanelBO.getStatus() == EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK) {
                    Animations.giftBounceAnimation(mContext, itemTypeViewHolder.giftPanelImageView);
                }
            } else {
                itemTypeViewHolder.giftPanelParentFrame.setBackground(null);
                if (itemTypeViewHolder.giftPanelImageView.getAnimation() != null) {
                    itemTypeViewHolder.giftPanelImageView.getAnimation().setAnimationListener(null);
                    itemTypeViewHolder.giftPanelImageView.getAnimation().cancel();
                    itemTypeViewHolder.giftPanelImageView.getAnimation().reset();
                    itemTypeViewHolder.giftPanelImageView.clearAnimation();
                }
            }

            if (giftPanelBO.getStatus() == EnumUtils.GiftLevel.GIFT_STATUS_LOCK) {
                itemTypeViewHolder.coinsIV.setVisibility(View.GONE);
                itemTypeViewHolder.giftPanelQuantity.setText(Utils.getShortString(giftPanelBO.getName(), 9));
            } else {
                itemTypeViewHolder.coinsIV.setVisibility(View.VISIBLE);
            }

            itemTypeViewHolder.mGiftPanelParentFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    for (int i = 0; i < dataSet.size(); i++) {
                        dataSet.get(i).setSelected(false);
                    }

                    int firstVisibleIndex = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    int lastVisibleIndex = ((GridLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();

                    for (int i = firstVisibleIndex; i <= lastVisibleIndex; i++) {
                        try {
                            View child = mRecyclerView.getLayoutManager().findViewByPosition(i);
                            if (child != null) {
                                child.findViewById(itemTypeViewHolder.giftPanelParentFrame.getId()).setBackground(null);
                                if (child.findViewById(itemTypeViewHolder.giftPanelImageView.getId()).getAnimation() != null) {
                                    child.findViewById(itemTypeViewHolder.giftPanelImageView.getId()).getAnimation().setAnimationListener(null);
                                    child.findViewById(itemTypeViewHolder.giftPanelImageView.getId()).getAnimation().cancel();
                                    child.findViewById(itemTypeViewHolder.giftPanelImageView.getId()).getAnimation().reset();
                                    child.findViewById(itemTypeViewHolder.giftPanelImageView.getId()).clearAnimation();
                                }
                            }
                        } catch (Exception e) {
                            LogUtils.e("giftPanelParentFrame", "" + e.getMessage());
                        }
                    }

                    giftPanelBO.setSelected(true);
                    btnSubmit.setEnabled(true);
                    if (giftPanelBO.getStatus() == EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK) {
                        Animations.giftBounceAnimation(mContext, itemTypeViewHolder.giftPanelImageView);
                    }
                    itemTypeViewHolder.giftPanelParentFrame.setBackground(mContext.getResources().getDrawable(R.drawable.view_border));
                    selectedIndex = listPosition;

                    //bubbleViewAnimation.setBubbleDrawable(mContext.getResources().getDrawable(giftPanelBO.getLocalImage()), Utils.dp2px(mContext, 40), Utils.dp2px(mContext, 40));
                    //addBubbleView(view, giftPanelBO);
                }
            });

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
        void onRowClick(int position);
    }

    public void addBubbleView(View view, final GiftPanelBO selectedGiftPanelBO) {
        int left = view.getLeft();
        int top = view.getTop();
        int right = view.getRight();
        int bottom = view.getBottom();
        int height = view.getHeight();
        int width = view.getWidth();
        float x = view.getX();
        float y = view.getY();
        Log.d("BubbleView", "height= " + height);
//        bubbleViewAnimation.addBubble(right /*- Utils.dp2px(mContext, 15)*/, top + 200);
        bubbleViewAnimation.addBubble((int) x + (width / 2) - Utils.dp2px(mContext, 20)/*- Utils.dp2px(mContext, 15)*/, top + Utils.dp2px(mContext, 290), new Random().nextInt(5));

        new CallEventBusAsyncTask().execute(selectedGiftPanelBO.getId(), selectedGiftPanelBO.getName(), selectedGiftPanelBO.getPrice());
//        GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("Gifts_ID", selectedGiftPanelBO.getId()));
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    private class CallEventBusAsyncTask extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... params) {
            GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("Gifts_ID", (String) params[1], (int) params[0], (int) params[2], (int) params[2], "NOT USED", 0));
            return null;
        }
    }
}