package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveViewersProfilePageDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.mProfileImageView)
    ImageView mProfileImageView;
    @BindView(R.id.userLevelIV)
    ImageView userLevelIV;

    @BindView(R.id.btnFollow)
    Button btnFollow;
    @BindView(R.id.btnGuestLive)
    Button btnGuestLive;
    @BindView(R.id.userProfileIDTV)
    CustomTextView userProfileIDTV;
    @BindView(R.id.userChips)
    CustomTextView userChips;
    @BindView(R.id.userDollar)
    CustomTextView userDollar;
    @BindView(R.id.userProfileNameTV)
    CustomTextView userProfileNameTV;
    @BindView(R.id.profileRankTextView)
    CustomTextView profileRankTextView;
    @BindView(R.id.userDollarParent)
    LinearLayout userDollarParent;
    @BindView(R.id.social_options)
    LinearLayout social_options;
    ConversationBO conversationBO;
    UserLevelBO userLevelBO;
    boolean alreadyFollow = false;
    UserBO userBO;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            switch (newState) {
                case BottomSheetBehavior.STATE_DRAGGING:
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;
                case BottomSheetBehavior.STATE_SETTLING:
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public static GoLiveViewersProfilePageDialog newInstance(ConversationBO conversationBO) {
        Bundle args = new Bundle();
        args.putParcelable("USER", conversationBO);
        GoLiveViewersProfilePageDialog fragment = new GoLiveViewersProfilePageDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public GoLiveViewersProfilePageDialog() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_viewers_profile_page, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);


        setRoundPic(Constants.DUMMY_IMAGE_URL);
        try {
            if (getArguments() != null) {
                conversationBO = getArguments().getParcelable("USER");
                if (conversationBO.getUser_details() != null && conversationBO.getUser_details().getUserProgressDetailBO() != null) {
                    userLevelBO = Utils.getUserLevel(conversationBO.getUser_details().getUserProgressDetailBO().getUser_level());
                }
                populateViewData(conversationBO);
            }
        } catch (Exception e) {
            LogUtils.e("setupDialog", "" + e.getMessage());
        }
        initViews();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        ((View) bottomSheetView.getParent()).setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getDialog().getWindow();
            if (window != null)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void setRoundPic(String imgURL) {
        Utils.setImageRounded(mProfileImageView, imgURL, getContext());
    }

    private void populateViewData(ConversationBO conversationBO) {
        try {
            userProfileIDTV.setText(getString(R.string.witkey_id) + ": " + conversationBO.getSenderId());
            userProfileNameTV.setText(conversationBO.getUsername());
            if (userLevelBO != null) {
                profileRankTextView.setText(userLevelBO.getLevelTitle() + " " + conversationBO.getUser_details().getUserProgressDetailBO().getUser_level());
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
            }
            if (conversationBO.getUser_details().isArtist == 1) {
                social_options.setVisibility(View.VISIBLE);
                userDollarParent.setVisibility(View.VISIBLE);
            } else {
                social_options.setVisibility(View.INVISIBLE);
                userDollarParent.setVisibility(View.INVISIBLE);
            }

            userDollar.setText((conversationBO.getUser_details().getWitkeyDollar().intValue()) + "");
            userChips.setText(((int) conversationBO.getUser_details().getChips()) + "");
            setRoundPic(conversationBO.getDpUrl());
        } catch (Exception e) {
            LogUtils.e("populateViewData", "" + e.getMessage());
        }
    }

    private void initViews() {
        try {
            if (conversationBO != null) {
                userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                checkFanFollowNetworkCall(getActivity(), userBO.getId(), conversationBO.getSenderId());
            }
        } catch (Exception e) {
            LogUtils.e("initViews", "" + e.getMessage());
        }
        btnFollow.setOnClickListener(this);
        btnGuestLive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFollow:
                if (conversationBO != null) {
                    fanFollowNetworkCall(getActivity(), conversationBO.getSenderId(), !alreadyFollow);
                }
//                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
            case R.id.btnGuestLive:
                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                break;
        }
    }

    // METHOD FOR FOLLOW / uNFOLLOW NETWORK CALL
//    is-friend/42/43
//    is-friend/{user_id}/{friend_user_id}
//    {"Message":"Friend status!","Response":200,"Result":{"FriendsStatus":0,"last_24_earning_wd":0,"viewers_24_last":"3"}}
    private void checkFanFollowNetworkCall(final Context context, final String userID, String friend_user_id) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        serviceParams.put(Keys.USER_ID, userID);
        serviceParams.put(Keys.FRIEND_USER_ID, friend_user_id);
        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.is_friend,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                if (jsonObject.has("FriendsStatus")) {

                                    if (jsonObject.getString("FriendsStatus").equals("1")) {
                                        alreadyFollow = true;
                                        btnFollow.setText(getString(R.string.un_follow));
                                    } else {
                                        alreadyFollow = false;
                                        btnFollow.setText(getString(R.string.follow));
                                    }
                                }
                               /*DO SOMETHING*/
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void fanFollowNetworkCall(final Context context, final String userID, final boolean followStatus) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        if (followStatus) {
            serviceParams.put(Keys.FOLLOW_USER, userID);
        } else {
            serviceParams.put(Keys.UNFOLLOW_USER, userID);
        }
        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, true, "",
                followStatus ? EnumUtils.ServiceName.follow_user : EnumUtils.ServiceName.unfollow_user,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                if (followStatus) {
                                    alreadyFollow = true;
                                    btnFollow.setText(getString(R.string.un_follow));
                                } else {
                                    alreadyFollow = false;
                                    btnFollow.setText(getString(R.string.follow));
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }
}

