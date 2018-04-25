package app.witkey.live.adapters.dashboard.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.PackagesBO;
import app.witkey.live.utils.customviews.CustomTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletAdapter extends RecyclerView.Adapter {

    private List<PackagesBO> dataSet;
    Context mContext;
    int total_types;
    public final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int visibleThreshold = 2;
    private boolean isLoading;

    //constructor...
    public MyWalletAdapter(List<PackagesBO> data, Context context, RecyclerView mRecyclerView) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }

    class ItemTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.mWalletParentFrame)
        RelativeLayout mWalletParentFrame;
        @BindView(R.id.chipsCustomTextView)
        CustomTextView chipsCustomTextView;
        @BindView(R.id.promotionChips)
        CustomTextView promotionChips;
        @BindView(R.id.freeChips)
        CustomTextView freeChips;
        @BindView(R.id.promotion)
        LinearLayout promotion;
        @BindView(R.id.free)
        LinearLayout free;
        @BindView(R.id.btnChipPrice)
        Button btnChipPrice;

        public ItemTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mWalletParentFrame.setOnClickListener(this);
            this.btnChipPrice.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mWalletParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(v, getAdapterPosition());
                    break;
                case R.id.btnChipPrice:
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

    public void addItems(List<PackagesBO> walletBOList) {
        dataSet.addAll(walletBOList);
        notifyDataSetChanged();
    }

    public List<PackagesBO> getItems() {
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

    public PackagesBO getItem(int position) {
        return dataSet.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case VIEW_TYPE_ITEM:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_wallet_item, parent, false);
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

        PackagesBO walletBO = dataSet.get(listPosition);

        if (holder instanceof ItemTypeViewHolder) {
            ItemTypeViewHolder itemTypeViewHolder = (ItemTypeViewHolder) holder;

            itemTypeViewHolder.free.setVisibility(View.GONE);
            itemTypeViewHolder.promotion.setVisibility(View.GONE);

            itemTypeViewHolder.chipsCustomTextView.setText(walletBO.getWitky_chips() + "");


            if (walletBO.getFree_chips() > 0) {
                itemTypeViewHolder.free.setVisibility(View.VISIBLE);
                itemTypeViewHolder.freeChips.setText(walletBO.getFree_chips() + "");
            }
            if (walletBO.getPromotion() > 0) {
                itemTypeViewHolder.promotion.setVisibility(View.VISIBLE);
                itemTypeViewHolder.promotionChips.setText(walletBO.getPromotion() + "");
            }

            /*String chips = "<font color='#000000'>" + walletBO.getWitky_chips() + "</font>";
            if (walletBO.getFree_chips() > 0) {
                chips = chips + "<font color='#ec7c31'> + " + walletBO.getFree_chips() + "</font>";
//                itemTypeViewHolder.chipsCustomTextView.setText(Html.fromHtml("<font color='#000000'>" + walletBO.getWitky_chips() + " </font> <font color='#ec7c31'> + " + walletBO.getFree_chips() + "</font>"));
            }
            if (walletBO.getPromotion() > 0) {
                chips = chips + "<font color='#ec7c31'> + " + walletBO.getPromotion() + "</font>";
//                itemTypeViewHolder.chipsCustomTextView.setText(Html.fromHtml("<font color='#000000'>" + walletBO.getWitky_chips() + " </font> <font color='#ec7c31'> + " + walletBO.getFree_chips() + " + " + walletBO.getPromotion() + "</font>"));
            }
            itemTypeViewHolder.chipsCustomTextView.setText(Html.fromHtml(chips));
*/
            /*if (walletBO.getAllow_promotion() == 1) {
                itemTypeViewHolder.chipsCustomTextView.setText(Html.fromHtml("<font color='#000000'>" + walletBO.getWitky_chips() + " </font> <font color='#ec7c31'> + " + mContext.getString(R.string.free) + " " + ((walletBO.getWitky_chips() * walletBO.getPromotion()) / 100) + "</font>"));
            } else {
                itemTypeViewHolder.chipsCustomTextView.setText(walletBO.getWitky_chips() + "");
            }*/

            /*Spannable chipsSpannable = new SpannableString(walletBO.getChips() + freeChips);

            chipsSpannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.witkey_orange)), walletBO.getChips().length(), walletBO.getChips().length() + freeChips.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            itemTypeViewHolder.chipsCustomTextView.setText(chipsSpannable);*/

            itemTypeViewHolder.btnChipPrice.setText("$" + walletBO.getAmount());

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
