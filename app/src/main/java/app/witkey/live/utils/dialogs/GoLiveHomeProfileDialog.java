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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.activities.GoLiveActivity;
import app.witkey.live.fragments.dashboard.home.broadcasters.BroadcastersFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveHomeProfileDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.mProfileImageView)
    ImageView mProfileImageView;

    @BindView(R.id.topSpenderIV)
    ImageView topSpenderIV;
    @BindView(R.id.artistLevelIV)
    ImageView artistLevelIV;
    @BindView(R.id.userLevelIV)
    ImageView userLevelIV;
    @BindView(R.id.userLevelBoxTextView)
    CustomTextView userLevelBoxTextView;
    @BindView(R.id.diamondsCountBoxTextView)
    CustomTextView diamondsCountBoxTextView;
    @BindView(R.id.userDollarsText)
    CustomTextView userDollarsText;
    @BindView(R.id.userChips)
    CustomTextView userChips;
    @BindView(R.id.userDollarsLast)
    CustomTextView userDollarsLast;
    @BindView(R.id.userViewersCount)
    CustomTextView userViewersCount;

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;
    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;
    @BindView(R.id.userDollars)
    LinearLayout userDollars;
    @BindView(R.id.userDollarLast)
    LinearLayout userDollarLast;
    @BindView(R.id.userLevel)
    RelativeLayout userLevel;
    @BindView(R.id.artistLevel)
    RelativeLayout artistLevel;
    @BindView(R.id.topSpenderParent)
    RelativeLayout topSpenderParent;
    @BindView(R.id.followArtistRL)
    RelativeLayout followArtistRL;

    @BindView(R.id.followArtistParent)
    LinearLayout followArtistParent;
    @BindView(R.id.followIV)
    ImageView followIV;
    @BindView(R.id.followTV)
    CustomTextView followTV;

    boolean typeStream = false;
    UserLevelBO userLevelBO;
    UserLevelBO artistLevelBO;
    StreamBO streamBO;
    UserBO userBO;
    String userCount;
    String dollarValue = "0";

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

    public GoLiveHomeProfileDialog() {

    }

    public static GoLiveHomeProfileDialog newInstance(String viewerCount, StreamBO streamBO, boolean typeStream, String dollarValue) {
        Bundle args = new Bundle();
        args.putParcelable("STREAMBO", streamBO);
        args.putString("COUNT", viewerCount);
        args.putBoolean("TYPE", typeStream);
        args.putString("DOLLARS", dollarValue);
        GoLiveHomeProfileDialog fragment = new GoLiveHomeProfileDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_home_profile, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        initViews();

        if (getArguments() != null) {
            userCount = getArguments().getString("COUNT");
            streamBO = getArguments().getParcelable("STREAMBO");
            typeStream = getArguments().getBoolean("TYPE");
            dollarValue = getArguments().getString("DOLLARS");
        }

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

        populateUserProfileData();

    }

    private void initViews() {
        topSpenderIV.setOnClickListener(this);
        followArtistParent.setOnClickListener(this);
    }

    private void populateUserProfileData() {
        try {

            if (typeStream) {

                if (streamBO != null) {
//                    UserProgressDetailBO userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
//                    if (userProgressDetailBO != null) {
                    followArtistRL.setVisibility(View.VISIBLE);
                    if (streamBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        followTV.setText(R.string.un_follow);
                        followIV.setImageResource(R.drawable.ic_follow_on);
                    } else {
                        followIV.setImageResource(R.drawable.ic_follow_off);
                        followIV.setColorFilter(getActivity().getResources().getColor(R.color.witkey_orange));
                        followTV.setText(R.string.follow);
                    }

                    userLevelBO = Utils.getUserLevel(streamBO.getUser_details().getUserProgressDetailBO().getUser_level());
                    artistLevelBO = Utils.getBroadcasterLevel(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level());
                    userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                    artistLevelIV.setImageResource(artistLevelBO.getLevelLocalImage());
                    userLevelBoxTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");
                    diamondsCountBoxTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getArtist_level() + "");
//                    }

                    profileNameTextView.setText(streamBO.getUser_details().getUsername().toUpperCase());
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + streamBO.getUser_details().getUser_complete_id());
                    Utils.setImageRounded(mProfileImageView, streamBO.getUser_details().getProfilePictureUrl(), getActivity());

            /*if (isArist) {
                userDollars.setVisibility(View.VISIBLE);
                userDollarLast.setVisibility(View.VISIBLE);
                artistLevel.setVisibility(View.VISIBLE);
            } else {
                userDollars.setVisibility(View.INVISIBLE);
                userDollarLast.setVisibility(View.INVISIBLE);
                artistLevel.setVisibility(View.GONE);
            }*/
                    if (TextUtils.isEmpty(dollarValue)) {
                        dollarValue = (streamBO.getUser_details().getWitkeyDollar().intValue()) + "";
                    }
//                    userDollarsText.setText((streamBO.getUser_details().getWitkeyDollar().intValue()) + "");
                    userDollarsText.setText(dollarValue);
                    userChips.setText((streamBO.getUser_details().getChips().intValue()) + "");
                    userDollarsLast.setText(dollarValue);
//                    userDollarsLast.setText((streamBO.getUser_details().getWitkeyDollar().intValue()) + "");
                    userViewersCount.setText(userCount);
                }
            } else {
                userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                if (userBO != null) {

                    followIV.setImageResource(R.drawable.ic_follow_off);
                    followIV.setColorFilter(getActivity().getResources().getColor(R.color.witkey_orange));
                    followTV.setText(R.string.follow);
                    followArtistRL.setVisibility(View.GONE);
//                    ((LinearLayout.LayoutParams) topSpenderParent.getLayoutParams()).gravity = Gravity.CENTER;
                    topSpenderParent.setGravity(Gravity.CENTER);

                    UserProgressDetailBO userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
                    if (userProgressDetailBO != null) {

                        userLevelBO = Utils.getUserLevel(userProgressDetailBO.getUser_level());
                        artistLevelBO = Utils.getBroadcasterLevel(userProgressDetailBO.getArtist_level());
                        userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                        artistLevelIV.setImageResource(artistLevelBO.getLevelLocalImage());
                        userLevelBoxTextView.setText(userProgressDetailBO.getUser_level() + "");
                        diamondsCountBoxTextView.setText(userProgressDetailBO.getArtist_level() + "");
                    }

                    profileNameTextView.setText(userBO.getUsername().toUpperCase());
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getUser_complete_id());
                    Utils.setImageRounded(mProfileImageView, userBO.getProfilePictureUrl(), getActivity());

            /*if (isArist) {
                userDollars.setVisibility(View.VISIBLE);
                userDollarLast.setVisibility(View.VISIBLE);
                artistLevel.setVisibility(View.VISIBLE);
            } else {
                userDollars.setVisibility(View.INVISIBLE);
                userDollarLast.setVisibility(View.INVISIBLE);
                artistLevel.setVisibility(View.GONE);
            }*/

                    if (TextUtils.isEmpty(dollarValue)) {
                        dollarValue = (userBO.getWitkeyDollar().intValue()) + "";
                    }
//                    userDollarsText.setText((userBO.getWitkeyDollar().intValue()) + "");
                    userDollarsText.setText(dollarValue);
                    userChips.setText(((int) userBO.getChips()) + "");
                    userDollarsLast.setText(dollarValue);
//                    userDollarsLast.setText((userBO.getWitkeyDollar().intValue()) + "");
                    userViewersCount.setText(userCount);

                }
            }
        } catch (Exception e) {
            LogUtils.e("populateUserProfileData", "" + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.topSpenderIV:
                dismiss();
                if (getActivity() instanceof GoLiveActivity) {
                    ((GoLiveActivity) getActivity()).gotoNextFragment(BroadcastersFragment.newInstance());
                }
                break;
            case R.id.followArtistParent:
                if (typeStream) {
                    if (streamBO != null) {
                        if (streamBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                            fanFollowNetworkCall(getActivity(), streamBO.getUser_details().getId(), false, UserSharedPreference.readUserToken());
                        } else {
                            fanFollowNetworkCall(getActivity(), streamBO.getUser_details().getId(), true, UserSharedPreference.readUserToken());
                        }
                    }
                } else {
                    SnackBarUtil.showSnackbar(getActivity(), getString(R.string.cant_follow_self), false);
                }
                break;
        }
    }

    // METHOD FOR FAN FOLLOW NETWORK CALL
    private void fanFollowNetworkCall(final Context context, String userID, final boolean followStatus, String userToken) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<>();

        if (followStatus) {
            serviceParams.put(Keys.FOLLOW_USER, userID);
        } else {
            serviceParams.put(Keys.UNFOLLOW_USER, userID);
        }
        tokenServiceHeaderParams.put(Keys.TOKEN, userToken);

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
                                    GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("ADD_FOLLOW"));
                                    followTV.setText(R.string.un_follow);
                                    followIV.setImageResource(R.drawable.ic_follow_on);
                                } else {
                                    GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("ADD_UNFOLLOW"));
                                    followIV.setImageResource(R.drawable.ic_follow_off);
                                    followIV.setColorFilter(getActivity().getResources().getColor(R.color.witkey_orange));
                                    followTV.setText(R.string.follow);
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

