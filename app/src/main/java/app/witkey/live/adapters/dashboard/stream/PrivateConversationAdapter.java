package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import app.witkey.live.R;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/04/2017.
 */

public class PrivateConversationAdapter extends RecyclerView.Adapter {
    private ArrayList<ConversationBO> dataSet;
    Context mContext;
    int total_types;

    public void addItem(ConversationBO conversationBO) {
        dataSet.add(conversationBO);
        notifyDataSetChanged();
    }

    public ArrayList<ConversationBO> getItems() {
        return dataSet;
    }

    public void addItems(ArrayList<ConversationBO> list) {
        dataSet.addAll(list);
        notifyDataSetChanged();
    }

    public ConversationBO getItem(int position) {
        return dataSet.get(position);
    }

    class OthersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.messageText)
        CustomTextView messageText;
        @BindView(R.id.messageDate)
        CustomTextView messageDate;
        @BindView(R.id.messageImageView)
        ImageView messageImageView;
        @BindView(R.id.mMessageParentFrame)
        LinearLayout mMessageParentFrame;


        public OthersViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mMessageParentFrame.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mMessageParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.mParentFrame:
                    return clickListener.onRowLongPressClick(getAdapterPosition());
            }
            return false;
        }
    }

    class MeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.messageText)
        CustomTextView messageText;
        @BindView(R.id.messageDate)
        CustomTextView messageDate;
        @BindView(R.id.messageImageView)
        ImageView messageImageView;
        @BindView(R.id.mMessageParentFrame)
        LinearLayout mMessageParentFrame;


        public MeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            this.mMessageParentFrame.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mMessageParentFrame:
                    if (clickListener != null)
                        clickListener.onRowClick(getAdapterPosition());
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.mParentFrame:
                    return clickListener.onRowLongPressClick(getAdapterPosition());
            }
            return false;
        }
    }

    public PrivateConversationAdapter(ArrayList data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case EnumUtils.ConversationType.CHAT_MESSAGE_ME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_private_chat_other,
                        parent, false);
                return new MeViewHolder(view);
            case EnumUtils.ConversationType.CHAT_MESSAGE_OTHER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_private_chat_me,
                        parent, false);
                return new OthersViewHolder(view);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getmRowType();
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        ConversationBO object = dataSet.get(listPosition);
        if (object != null && mContext != null) {

            if (holder instanceof MeViewHolder) {
                ((MeViewHolder) holder).messageDate.setText(object.getDate() + "");
                ((MeViewHolder) holder).messageText.setText(object.getText());
                Utils.setImageRounded(((MeViewHolder) holder).messageImageView, object.getDpUrl(), mContext);
            } else if (holder instanceof OthersViewHolder) {
                ((OthersViewHolder) holder).messageDate.setText(object.getDate() + "");
                ((OthersViewHolder) holder).messageText.setText(object.getText());
                Utils.setImageRounded(((OthersViewHolder) holder).messageImageView, object.getDpUrl(), mContext);
            }
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

        boolean onRowLongPressClick(int position);
    }
}
