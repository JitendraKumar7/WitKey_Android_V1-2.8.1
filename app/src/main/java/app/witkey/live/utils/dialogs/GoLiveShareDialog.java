package app.witkey.live.utils.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.fragments.dashboard.payment.WebviewFragment;
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

public class GoLiveShareDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.starImageView)
    ImageView starImageView;

    @BindView(R.id.facebookShareImageView)
    ImageView facebookShareImageView;
    @BindView(R.id.googleShareImageView)
    ImageView googleShareImageView;
    @BindView(R.id.weChatShareImageView)
    ImageView weChatShareImageView;
    @BindView(R.id.profileShareImageView)
    ImageView profileShareImageView;
    @BindView(R.id.phoneShareImageView)
    ImageView phoneShareImageView;
    int SHARE_MOMENT = 100;

    String userName;
    String uuid;
    String streamID;
    String userID;
    boolean facebookShare = false;

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

    public static GoLiveShareDialog newInstance(String userName, String streamUUID, String userID, String streamID) {
        Bundle args = new Bundle();
        args.putString("NAME", userName);
        args.putString("UUID", streamUUID);
        args.putString("USERID", userID);
        args.putString("STREAMID", streamID);
        GoLiveShareDialog fragment = new GoLiveShareDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public GoLiveShareDialog() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_share, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        if (getArguments() != null) {
            userName = getArguments().getString("NAME");
            uuid = getArguments().getString("UUID");
            userID = getArguments().getString("USERID");
            streamID = getArguments().getString("STREAMID");
        }

        setViewsPosition();
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

    private void initViews() {
        facebookShareImageView.setOnClickListener(this);
        googleShareImageView.setOnClickListener(this);
        weChatShareImageView.setOnClickListener(this);
        profileShareImageView.setOnClickListener(this);
        phoneShareImageView.setOnClickListener(this);
    }

    private void setViewsPosition() {
        starImageView.post(new Runnable() {
            @Override
            public void run() {

                int starWidth = starImageView.getWidth();
                int starHeight = starImageView.getHeight();

                float star2ndRowY = 0.39f * starHeight;

                googleShareImageView.setX(starImageView.getX() - googleShareImageView.getWidth());
                googleShareImageView.setY(star2ndRowY + (googleShareImageView.getHeight() / 2));

                weChatShareImageView.setX(starImageView.getX() + starWidth);
                weChatShareImageView.setY(star2ndRowY + (weChatShareImageView.getHeight() / 2));

                profileShareImageView.setX(starImageView.getX() + (starWidth / 4) - profileShareImageView.getWidth());
                phoneShareImageView.setX((starImageView.getX() + starWidth) - (starWidth / 4));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebookShareImageView:
                postOnFacebookWall();
                break;
            case R.id.googleShareImageView:
            case R.id.weChatShareImageView:
            case R.id.profileShareImageView:
            case R.id.phoneShareImageView:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Witkey");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, userName + " " + getString(R.string.invite_live) + " \n" + getString(R.string.live_link) + uuid);
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.will_be_implemented_later));
                startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), SHARE_MOMENT);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SHARE_MOMENT) {
            if (resultCode == Activity.RESULT_OK) {
                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, streamID, userID);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*DO SOME THING HERE*/
            }
        } else if (facebookShare) {
            if (resultCode == Activity.RESULT_OK) {
                Utils.showToast(getActivity(), getString(R.string.posted_successfully));
                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, streamID, userID);
            }
        }
        dismiss();
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

    public void postOnFacebookWall() {
        try {

            facebookShare = true;
            CallbackManager callbackManager = CallbackManager.Factory.create();
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://developers.facebook.com"))
                    .build();

            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Log.e("shareDialog", "onSuccess");
                }

                @Override
                public void onCancel() {
                    Log.e("shareDialog", "onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e("shareDialog", "onError" + error.getMessage());
                }
            });

            /*ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Log.d("onSuccess", "Testing graph API wall post");
                }

                @Override
                public void onCancel() {
                    Log.d("onCancel", "Testing graph API wall post");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.d("onError", "Testing graph API wall post");
                }
            });*/

            //ShareDialog shareDialog = new ShareDialog(this);
            //shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

