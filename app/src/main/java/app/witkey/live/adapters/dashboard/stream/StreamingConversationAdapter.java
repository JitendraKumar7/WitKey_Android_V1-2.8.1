package app.witkey.live.adapters.dashboard.stream;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

import app.witkey.live.R;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.customviews.CustomTextView;

/**
 * created by developer on 10/04/2017.
 */

public class StreamingConversationAdapter extends RecyclerView.Adapter {
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

        TextView mUsername;
        TextView mMessage;
        TextView micTextView;
        View mParentFrame;
        ImageView mUserProfileImage;
        ImageView conversationGiftsIV;
        ImageView imvLike;
        ImageView imvDislike;
        RelativeLayout rlUser;

        public OthersViewHolder(View itemView) {
            super(itemView);
            this.mParentFrame = itemView.findViewById(R.id.mParentFrame);
            this.mUsername = (TextView) itemView.findViewById(R.id.mUserName);
            this.micTextView = (TextView) itemView.findViewById(R.id.micTextView);
            this.mMessage = (TextView) itemView.findViewById(R.id.mUserMessage);
            this.mUserProfileImage = (ImageView) itemView.findViewById(R.id.mUserProfileImage);
            this.conversationGiftsIV = (ImageView) itemView.findViewById(R.id.conversationGiftsIV);
//            this.imvLike = (ImageView) itemView.findViewById(R.id.imv_like);
//            this.imvDislike = (ImageView) itemView.findViewById(R.id.imv_dislike);
            // this.rlUser = (RelativeLayout) itemView.findViewById(R.id.rl_user);

            // this.rlUser.setOnClickListener(this);
            this.mParentFrame.setOnLongClickListener(this);
//            this.imvLike.setOnClickListener(this);
//            this.imvDislike.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                /*case R.id.rl_user:
                    if (clickListener != null)
                        clickListener.onRowClick(getAdapterPosition());
                    break;*/

               /* case R.id.imv_like:
                    if (clickListener != null)
                        clickListener.onLikeClick(getAdapterPosition());
                    break;

                case R.id.imv_dislike:
                    if (clickListener != null)
                        clickListener.onDislikeClick(getAdapterPosition());
                    break;*/
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

    class GiftViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mUsername;
        TextView mMessage;
        TextView micTextView;
        CustomTextView giftCountTV;
        View mParentFrame;
        ImageView mUserProfileImage;
        ImageView conversationGiftsIV;

        public GiftViewHolder(View itemView) {
            super(itemView);
            this.mParentFrame = itemView.findViewById(R.id.mParentFrame);
            this.mUsername = (TextView) itemView.findViewById(R.id.mUserName);
            this.mMessage = (TextView) itemView.findViewById(R.id.mUserMessage);
            this.micTextView = (TextView) itemView.findViewById(R.id.micTextView);
            this.mUserProfileImage = (ImageView) itemView.findViewById(R.id.mUserProfileImage);
            this.conversationGiftsIV = (ImageView) itemView.findViewById(R.id.conversationGiftsIV);
            this.giftCountTV = (CustomTextView) itemView.findViewById(R.id.giftCountTV);

            this.mParentFrame.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.mParentFrame:
                    if (clickListener != null)
                        clickListener.onDislikeClick(getAdapterPosition());
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

    class FirstMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView mUsername;
        TextView mMessage;
        View mParentFrame;
        ImageView mUserProfileImage;
        RelativeLayout rlUser;

        public FirstMessageViewHolder(View itemView) {
            super(itemView);
            this.mParentFrame = itemView.findViewById(R.id.mParentFrame);
            this.mUsername = (TextView) itemView.findViewById(R.id.mUserName);
            this.mMessage = (TextView) itemView.findViewById(R.id.mUserMessage);
            this.mUserProfileImage = (ImageView) itemView.findViewById(R.id.mUserProfileImage);
            this.rlUser = (RelativeLayout) itemView.findViewById(R.id.rl_user);

            this.rlUser.setOnClickListener(this);
            this.mParentFrame.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_user:
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

    public StreamingConversationAdapter(ArrayList data, Context context) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case EnumUtils.ConversationType.CHAT_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_streaming_conversation_other_new,
                        parent, false);
                return new OthersViewHolder(view);
            case EnumUtils.ConversationType.FIRST_MESSAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_streaming_conversation_first,
                        parent, false);
                return new FirstMessageViewHolder(view);
            case EnumUtils.ConversationType.LIVE_STATUS:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_streaming_conversation_gift_new,
                        parent, false);
                return new GiftViewHolder(view);
            case EnumUtils.ConversationType.GIFT_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowview_streaming_conversation_gift_new,
                        parent, false);
                return new GiftViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return dataSet.get(position).getConversationTypeFlag();
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        ConversationBO object = dataSet.get(listPosition);
        if (object != null && mContext != null) {

            switch (object.getConversationTypeFlag()) {
                case EnumUtils.ConversationType.CHAT_MESSAGE:
//                    ((OthersViewHolder) holder).mUsername.setText(object.getSenderName());
                    ((OthersViewHolder) holder).micTextView.setText(object.getSenderId() + "");
                    ((OthersViewHolder) holder).mMessage.setText(Html.fromHtml("<font color='#fed9b3'>" + object.getUsername() + ":</font>" + " " + object.getText()));
//                    Utils.setImageRounded(((OthersViewHolder) holder).mUserProfileImage, object.getDpUrl(), mContext);
                    ((OthersViewHolder) holder).conversationGiftsIV.setVisibility(View.GONE);
                    break;

                case EnumUtils.ConversationType.FIRST_MESSAGE:
                    ((FirstMessageViewHolder) holder).mMessage.setText(object.getText());
                    break;

                case EnumUtils.ConversationType.GIFT_SENT:
                    ((GiftViewHolder) holder).mMessage.setText(Html.fromHtml("<font color='#fed9b3'>" + object.getUsername() + ":</font> <font color='#fdcb03'> Sent </font>"));
//                    ((GiftViewHolder) holder).mMessage.setText(Html.fromHtml("<font color='#fed9b3'>" + object.getUsername() + ":</font> <font color='#fdcb03'> Sent "+ object.getText()+" </font>"));
                    ((GiftViewHolder) holder).micTextView.setText(object.getSenderId() + "");
                    ((GiftViewHolder) holder).conversationGiftsIV.setVisibility(View.VISIBLE);
                    if (object.getCount() != null && !object.getCount().isEmpty() && !object.getCount().equals("1")) {/*INSTEAD TO CONVERT getCount IN TO INT FOR JUST TO CHECK OTHER THEN 1*/
                        ((GiftViewHolder) holder).giftCountTV.setText("X" + object.getCount());
                    } else {
                        ((GiftViewHolder) holder).giftCountTV.setText("");
                    }
                    Glide.with(mContext)
                            .load(object.getGiftURL())
                            .centerCrop()
                            .into(((GiftViewHolder) holder).conversationGiftsIV);
//                    Utils.setImageRounded(((GiftViewHolder) holder).mUserProfileImage, object.getDpUrl(), mContext);
//                    ((GiftViewHolder) holder).conversationGiftsIV.setImageResource(EnumUtils.GiftResById.getGiftRes(object.getSocialGiftID()));
                    break;

                case EnumUtils.ConversationType.LIVE_STATUS:
                    ((GiftViewHolder) holder).mMessage.setText(Html.fromHtml("<font color='#fed9b3'>" + object.getUsername() + ":</font> <font color='#ffffff'>" + object.getText() + "</font>"));
                    ((GiftViewHolder) holder).micTextView.setText(object.getSenderId() + "");
                    ((GiftViewHolder) holder).conversationGiftsIV.setVisibility(View.GONE);
                    //  Utils.setImageRounded(((GiftViewHolder) holder).mUserProfileImage, object.getDpUrl(), mContext);
//                    ((GiftViewHolder) holder).conversationGiftsIV.setImageResource(getImage(object.getSocialGiftID()));
                    break;
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

        void onLikeClick(int position);

        void onDislikeClick(int position);

        boolean onRowLongPressClick(int position);
    }
}
