package app.witkey.live.utils.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/02/2017.
 */

public class ShareStreamDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.btnShareBroadcast)
    Button btnShareBroadcast;
    @BindView(R.id.btnInviteFriends)
    Button btnInviteFriends;

    boolean liveStream = true;
    String streamURL;
    String userName;
    String userID;
    String streamID;
    String streamUUID;
    int SHARE_BROADCAST = 100;
    int SHARE_INVITE = 200;

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

    public ShareStreamDialog() {

    }

    public static ShareStreamDialog newInstance(String url, String userName, String userID, String streamID, String streamUUID, boolean liveStream_) {
        Bundle args = new Bundle();
        args.putString("URL", url);
        args.putString("USERNAME", userName);
        args.putString("USERID", userID);
        args.putString("STREAMID", streamID);
        args.putString("STREAMUUID", streamUUID);
        args.putBoolean("STREAMLIVE", liveStream_);
        ShareStreamDialog fragment = new ShareStreamDialog();
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

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_share_stream, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        setViewsPosition();
        initViews();

        if (getArguments() != null) {
            streamURL = getArguments().getString("URL");
            userName = getArguments().getString("USERNAME");
            userID = getArguments().getString("USERID");
            streamID = getArguments().getString("STREAMID");
            streamUUID = getArguments().getString("STREAMUUID");
            liveStream = getArguments().getBoolean("STREAMLIVE");
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
    }

    private void initViews() {
        btnInviteFriends.setOnClickListener(this);
        btnShareBroadcast.setOnClickListener(this);
    }

    private void setViewsPosition() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnInviteFriends:
//                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.INVITE_FRIEND, streamID, userID);
                Intent sharingIntentFriend = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntentFriend.setType("text/plain");
                sharingIntentFriend.putExtra(android.content.Intent.EXTRA_SUBJECT, "Witkey");
                sharingIntentFriend.putExtra(android.content.Intent.EXTRA_TEXT, userName + " " + getString(R.string.witkey_live_invite) + " \n" + getString(R.string.play_url));
//                startActivity(Intent.createChooser(sharingIntentFriend, "Share via"));
                startActivityForResult(Intent.createChooser(sharingIntentFriend, "Share via"), SHARE_INVITE);
//                dismiss();
                break;

            case R.id.btnShareBroadcast:
                if (liveStream) {
//                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, streamID, userID);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Witkey");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, userName + " " + getString(R.string.invite_live) + " \n" + getString(R.string.live_link) + streamUUID);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                    startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), SHARE_BROADCAST);
                } else {
                    Utils.showToast(getActivity(), getString(R.string.cant_share));
                }
//                dismiss();
                break;
        }
    }

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    //service name: moment-action
//    param: user_id, moment_id, type{50,:share, 60:like}
    private void setUserLikeDislikeNetworkCall(final Context context, String type, String stream_id, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.TYPE, type);
//        serviceParams.put(Keys.MOMENTS_ID, moment_id);
        serviceParams.put(Keys.STREAM_ID, stream_id);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.moment_action,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        // DO SOMETHING
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SHARE_BROADCAST) {
            if (resultCode == Activity.RESULT_OK) {
                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, streamID, userID);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*DO SOME THING HERE*/
            }
        } else if (requestCode == SHARE_INVITE) {
            if (resultCode == Activity.RESULT_OK) {
                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.INVITE_FRIEND, streamID, userID);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*DO SOME THING HERE*/
            }
        }
        dismiss();
    }
}

