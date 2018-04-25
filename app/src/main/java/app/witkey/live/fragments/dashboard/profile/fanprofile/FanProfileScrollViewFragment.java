package app.witkey.live.fragments.dashboard.profile.fanprofile;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MomentsAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.message.PrivateMessageFragment;
import app.witkey.live.fragments.dashboard.profile.moments.UserMomentsImageView;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.BlockUserBO;
import app.witkey.live.items.CommentsBO;
import app.witkey.live.items.LikedUsersBO;
import app.witkey.live.items.MomentBO;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserLevelBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.ImageOP;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.dialogs.BlockUserDialog;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FanProfileScrollViewFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.imageFrame)
    RelativeLayout imageFrame;

    @BindView(R.id.titleBarParentFrame)
    RelativeLayout titleBarParentFrame;

    @BindView(R.id.titleBarBackground)
    ImageView titleBarBackground;

    @BindView(R.id.fanProfileScrollView)
    ScrollView fanProfileScrollView;

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;

    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;

    @BindView(R.id.fanFollowStatus)
    CustomTextView fanFollowStatus;

    @BindView(R.id.giftsCountTextView)
    CustomTextView giftsCountTextView;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnFilter)
    ImageView btnFilter;

    @BindView(R.id.momentsParentFrame)
    RelativeLayout momentsParentFrame;
    @BindView(R.id.profileParentFrame)
    ScrollView profileParentFrame;

    @BindView(R.id.momentsProfileToggleParentFrame)
    RadioGroup momentsProfileToggleParentFrame;

    @BindView(R.id.noMomentsParentFrame)
    RelativeLayout noMomentsParentFrame;
    @BindView(R.id.momentsRecyclerView)
    RecyclerView momentsRecyclerView;

    @BindView(R.id.messageParentFrame)
    LinearLayout messageParentFrame;
    @BindView(R.id.followParentFrame)
    LinearLayout followParentFrame;

    @BindView(R.id.userProfileImageView)
    ImageView userProfileImageView;
    @BindView(R.id.userLevelBoxIV)
    ImageView userLevelBoxIV;
    @BindView(R.id.userLevelBoxTextView)
    CustomTextView userLevelBoxTextView;

    @BindView(R.id.imageView2)
    ImageView imageView2;

    @BindView(R.id.fanProfileMessage)
    ImageView fanProfileMessage;
    @BindView(R.id.userStatusTextView)
    CustomTextView userStatusTextView;
    @BindView(R.id.fanBaseCountTextView)
    CustomTextView fanBaseCountTextView;
    @BindView(R.id.userChipsTV)
    CustomTextView userChipsTV;
    @BindView(R.id.witkeyIdTV)
    CustomTextView witkeyIdTV;
    @BindView(R.id.ageTV)
    CustomTextView ageTV;
    @BindView(R.id.userLevelTV)
    CustomTextView userLevelTV;
    @BindView(R.id.birthdayTV)
    CustomTextView birthdayTV;
    @BindView(R.id.userCityTV)
    CustomTextView userCityTV;
    @BindView(R.id.userStarTV)
    CustomTextView userStarTV;
    @BindView(R.id.userLevelIV)
    ImageView userLevelIV;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    public static int TYPE_USERBO = 100;
    public static int TYPE_STREAMBO = 200;
    public static int TYPE_MOMENTBO = 300;
    public static int TYPE_COMMENTBO = 400;
    public static int TYPE_LIKEDBO = 500;
    public static int TYPE_BLOCK_USERBO = 600;
    int totalRecords = 0;
    StreamBO streamBO;
    UserBO userBO;
    MomentBO momentBO;
    CommentsBO commentsBO;
    LikedUsersBO likedUsersBO;
    BlockUserBO blockUserBO;
    int objectTypeUser = 0;
    private boolean loadMoreStatus = true;
    private MomentsAdapter momentsAdapter;
    private Context context;
    UserBO myUserBO;
    private int isBlock = 0;
    private String TAG = this.getClass().getSimpleName();

    public static FanProfileScrollViewFragment newInstance(Object object, int objectType) {
        Bundle args = new Bundle();
        args.putParcelable("OBJECT", (Parcelable) object);
        args.putInt("TYPE", objectType);
        FanProfileScrollViewFragment fragment = new FanProfileScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /*public static FanProfileScrollViewFragment newInstance(Object streamBOs, UserBO userBO, MomentBO momentBO, int objectType) {
        Bundle args = new Bundle();
        args.putParcelable("STREAMBO", (Parcelable) streamBOs);
        args.putParcelable("USERBO", userBO);
        args.putParcelable("MOMENTBO", momentBO);
        args.putInt("TYPE", objectType);
        FanProfileScrollViewFragment fragment = new FanProfileScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_fan_profile_scroll_view, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        try {
            if (getArguments() != null) {
                if (getArguments().getInt("TYPE") == TYPE_STREAMBO) {
                    streamBO = getArguments().getParcelable("OBJECT");
                    objectTypeUser = TYPE_STREAMBO;
                } else if (getArguments().getInt("TYPE") == TYPE_USERBO) {
                    userBO = getArguments().getParcelable("OBJECT");
                    objectTypeUser = TYPE_USERBO;
                } else if (getArguments().getInt("TYPE") == TYPE_MOMENTBO) {
                    momentBO = getArguments().getParcelable("OBJECT");
                    objectTypeUser = TYPE_MOMENTBO;
                } else if (getArguments().getInt("TYPE") == TYPE_COMMENTBO) {
                    commentsBO = getArguments().getParcelable("OBJECT");
                    objectTypeUser = TYPE_COMMENTBO;
                } else if (getArguments().getInt("TYPE") == TYPE_LIKEDBO) {
                    likedUsersBO = getArguments().getParcelable("OBJECT");
                    objectTypeUser = TYPE_LIKEDBO;
                } else if (getArguments().getInt("TYPE") == TYPE_BLOCK_USERBO) {
                    blockUserBO = getArguments().getParcelable("OBJECT");
                    objectTypeUser = TYPE_BLOCK_USERBO;
                }
            }
            myUserBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        } catch (Exception e) {
            LogUtils.e("FanProfileScrollViewFragment", "" + e.getMessage());
        }
            /*if (getArguments().getInt("TYPE") == TYPE_STREAMBO) {
                streamBO = getArguments().getParcelable("STREAMBO");
                objectTypeUser = TYPE_STREAMBO;
            } else if (getArguments().getInt("TYPE") == TYPE_USERBO) {
                userBO = getArguments().getParcelable("USERBO");
                objectTypeUser = TYPE_USERBO;
            }*/
        initViews();
        setUpUserProfile();
    }

    private void initViews() {

        if (momentsAdapter != null) {
            momentsAdapter = null;
        }
        if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {

            userStatusTextView.setText(streamBO.getUser_details().getStatusText());
            giftsCountTextView.setText(getString(R.string.sent) + " " + streamBO.getUser_details().getUserProgressDetailBO().getTotalGiftsSent() + " " + getString(R.string.gifts));
            fanBaseCountTextView.setText(getString(R.string.following) + " " + streamBO.getUser_details().getUserProgressDetailBO().getTotalFollowings());
            Utils.setImageSimple(userProfileImageView, streamBO.getUser_details().getProfilePictureUrl(), getActivity());
            profileNameTextView.setText(streamBO.getUser_details().getUsername().toUpperCase());
            if (streamBO.getUser_details().getIsArtist() == 1) {
                profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + streamBO.getUser_details().getUser_complete_id());
            } else {
                profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + streamBO.getUserId());
            }
//            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
            if (streamBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                fanFollowStatus.setText(R.string.un_follow);
            } else {
                fanFollowStatus.setText(R.string.follow);
            }

        } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
            userStatusTextView.setText(userBO.getStatusText());
            giftsCountTextView.setText(getString(R.string.sent) + " " + userBO.getUserProgressDetailBO().getTotalGiftsSent() + " " + getString(R.string.gifts));
            fanBaseCountTextView.setText(getString(R.string.following) + " " + userBO.getUserProgressDetailBO().getTotalFollowings());
            Utils.setImageSimple(userProfileImageView, userBO.getProfilePictureUrl(), getActivity());
            profileNameTextView.setText(userBO.getUsername().toUpperCase());
            if (userBO.getIsArtist() == 1) {
                profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getUser_complete_id());
            } else {
                profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
            }
//            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
            if (userBO.getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                fanFollowStatus.setText(R.string.un_follow);
            } else {
                fanFollowStatus.setText(R.string.follow);
            }
        } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
            userStatusTextView.setText(momentBO.getUser_details().getStatusText());
            giftsCountTextView.setText(getString(R.string.sent) + " " + momentBO.getUser_details().getUserProgressDetailBO().getTotalGiftsSent() + " " + getString(R.string.gifts)); // NOT GETTING VALUE HERE
            fanBaseCountTextView.setText(getString(R.string.following) + " " + momentBO.getUser_details().getUserProgressDetailBO().getTotalFollowings());
            Utils.setImageSimple(userProfileImageView, momentBO.getUser_details().getProfilePictureUrl(), getActivity());
            profileNameTextView.setText(momentBO.getUser_details().getUsername().toUpperCase());
            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + momentBO.getUser_details().getUser_complete_id());
            if (momentBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                fanFollowStatus.setText(R.string.un_follow);
            } else {
                fanFollowStatus.setText(R.string.follow);
            }
        } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
            userStatusTextView.setText(commentsBO.getUser_details().getStatusText());
            giftsCountTextView.setText(getString(R.string.sent) + " " + commentsBO.getUser_details().getUserProgressDetailBO().getTotalGiftsSent() + " " + getString(R.string.gifts)); // NOT GETTING VALUE HERE
            fanBaseCountTextView.setText(getString(R.string.following) + " " + commentsBO.getUser_details().getUserProgressDetailBO().getTotalFollowings());
            Utils.setImageSimple(userProfileImageView, commentsBO.getUser_details().getProfilePictureUrl(), getActivity());
            profileNameTextView.setText(commentsBO.getUser_details().getUsername().toUpperCase());
            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + commentsBO.getUser_details().getUser_complete_id());
            if (commentsBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                fanFollowStatus.setText(R.string.un_follow);
            } else {
                fanFollowStatus.setText(R.string.follow);
            }
        } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
            userStatusTextView.setText(likedUsersBO.getUser_details().getStatusText());
            giftsCountTextView.setText(getString(R.string.sent) + " " + likedUsersBO.getUser_details().getUserProgressDetailBO().getTotalGiftsSent() + " " + getString(R.string.gifts)); // NOT GETTING VALUE HERE
            fanBaseCountTextView.setText(getString(R.string.following) + " " + likedUsersBO.getUser_details().getUserProgressDetailBO().getTotalFollowings());
            Utils.setImageSimple(userProfileImageView, likedUsersBO.getUser_details().getProfilePictureUrl(), getActivity());
            profileNameTextView.setText(likedUsersBO.getUser_details().getUsername().toUpperCase());
            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + likedUsersBO.getUser_details().getUser_complete_id());
            if (likedUsersBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                fanFollowStatus.setText(R.string.un_follow);
            } else {
                fanFollowStatus.setText(R.string.follow);
            }
        } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
            userStatusTextView.setText(blockUserBO.getUser_details().getStatusText());
            giftsCountTextView.setText(getString(R.string.sent) + " " + blockUserBO.getUser_details().getUserProgressDetailBO().getTotalGiftsSent() + " " + getString(R.string.gifts)); // NOT GETTING VALUE HERE
            fanBaseCountTextView.setText(getString(R.string.following) + " " + blockUserBO.getUser_details().getUserProgressDetailBO().getTotalFollowings());
            Utils.setImageSimple(userProfileImageView, blockUserBO.getUser_details().getProfilePictureUrl(), getActivity());
            profileNameTextView.setText(blockUserBO.getUser_details().getUsername().toUpperCase());
            profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + blockUserBO.getUser_details().getUser_complete_id());
            if (blockUserBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                fanFollowStatus.setText(R.string.un_follow);
            } else {
                fanFollowStatus.setText(R.string.follow);
            }
        }

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    startIndex = 0;
                    if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                        swipeRefreshLayout.setRefreshing(true);
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, streamBO.getUserId());
                    } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                        swipeRefreshLayout.setRefreshing(true);
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                    } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                        swipeRefreshLayout.setRefreshing(true);
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getUser_details().getId());
                    } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                        swipeRefreshLayout.setRefreshing(true);
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, commentsBO.getUser_details().getId());
                    } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                        swipeRefreshLayout.setRefreshing(true);
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, likedUsersBO.getUser_details().getId());
                    } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                        swipeRefreshLayout.setRefreshing(true);
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, blockUserBO.getUser_details().getId());
                    }
                }
            });
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                // TODO REPLACE HERE EnumUtils.HomeScreenType.FEATURED WITH MY STREAMS
                if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, streamBO.getUserId());
                } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getUser_details().getId());
                } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, commentsBO.getUser_details().getId());
                } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, likedUsersBO.getUser_details().getId());
                } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, blockUserBO.getUser_details().getId());
                }

            }
        });

        btnBack.setOnClickListener(this);
        btnFilter.setOnClickListener(this);

        messageParentFrame.setOnClickListener(this);
        followParentFrame.setOnClickListener(this);

        messageParentFrame.setOnClickListener(this);

        momentsProfileToggleParentFrame.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {

                    case R.id.myMomentsRB:
                        momentsProfileToggle(true);
                        break;
                    case R.id.myProfileRB:
                        momentsProfileToggle(false);
                        break;
                }
            }
        });

        fanProfileScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = fanProfileScrollView.getScrollY();

                if (fanProfileScrollView != null && momentsAdapter != null) {
                    if (fanProfileScrollView.getChildAt(0).getBottom() <= (fanProfileScrollView.getHeight() + fanProfileScrollView.getScrollY())) {
                        if (loadMoreStatus) {
                            if (momentsAdapter != null && momentsAdapter.getItems().size() >= maxItems
                                    && momentsAdapter.getItems().size() < totalRecords) {
                                loadMoreStatus = false;
                                momentsAdapter.addLoadingItem();
                                startIndex = startIndex + 1;
                                if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, streamBO.getUserId());
                                } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, userBO.getId());
                                } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getUser_details().getId());
                                } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, commentsBO.getUser_details().getId());
                                } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, likedUsersBO.getUser_details().getId());
                                } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                                    getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, blockUserBO.getUser_details().getId());
                                }
                            }
                        }
                    }

                    if (scrollY < imageFrame.getHeight()) {

                        float heightRatio = (float) scrollY / (float) imageFrame.getHeight();
                        titleBarBackground.setAlpha(heightRatio);
                    } else {
                        titleBarBackground.setAlpha(1f);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.btnFilter:
                if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                    if (!myUserBO.getId().equals(streamBO.getUserId())) {
                        isBlock = streamBO.getUser_details().getIs_block();
                        BlockUserDialog.newInstance(isBlock).show(getActivity().getSupportFragmentManager(), "BlockUser");
                    } else {
                        SnackBarUtil.showSnackbar(context, "You can't block your self", false);
                    }
                } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                    if (!myUserBO.getId().equals(userBO.getId())) {
                        isBlock = userBO.getIs_block();
                        BlockUserDialog.newInstance(isBlock).show(getActivity().getSupportFragmentManager(), "BlockUser");
                    } else {
                        SnackBarUtil.showSnackbar(context, "You can't block your self", false);
                    }
                } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                    if (!myUserBO.getId().equals(momentBO.getUser_id() + "")) {
                        isBlock = momentBO.getUser_details().getIs_block();
                        BlockUserDialog.newInstance(isBlock).show(getActivity().getSupportFragmentManager(), "BlockUser");
                    } else {
                        SnackBarUtil.showSnackbar(context, "You can't block your self", false);
                    }
                } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                    if (!myUserBO.getId().equals(commentsBO.getUser_id() + "")) {
                        isBlock = commentsBO.getUser_details().getIs_block();
                        BlockUserDialog.newInstance(isBlock).show(getActivity().getSupportFragmentManager(), "BlockUser");
                    } else {
                        SnackBarUtil.showSnackbar(context, "You can't block your self", false);
                    }
                } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                    if (!myUserBO.getId().equals(likedUsersBO.getUser_id() + "")) {
                        isBlock = likedUsersBO.getUser_details().getIs_block();
                        BlockUserDialog.newInstance(isBlock).show(getActivity().getSupportFragmentManager(), "BlockUser");
                    } else {
                        SnackBarUtil.showSnackbar(context, "You can't block your self", false);
                    }
                } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                    if (!myUserBO.getId().equals(blockUserBO.getUser_details().getId() + "")) {
                        isBlock = 1;/*1 BECAUSE WE ARE IN BLOCK LIST*///blockUserBO.getUser_details().getIs_block();
                        BlockUserDialog.newInstance(isBlock).show(getActivity().getSupportFragmentManager(), "BlockUser");
                    } else {
                        SnackBarUtil.showSnackbar(context, "You can't block your self", false);
                    }
                }

                break;
            case R.id.followParentFrame:
                Animations.buttonBounceAnimation(getActivity(), imageView2);
                if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                    if (streamBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        fanFollowNetworkCall(getActivity(), streamBO.getUserId(), false, UserSharedPreference.readUserToken());
                    } else {
                        fanFollowNetworkCall(getActivity(), streamBO.getUserId(), true, UserSharedPreference.readUserToken());
                    }
                } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                    if (userBO.getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        fanFollowNetworkCall(getActivity(), userBO.getId(), false, UserSharedPreference.readUserToken());
                    } else {
                        fanFollowNetworkCall(getActivity(), userBO.getId(), true, UserSharedPreference.readUserToken());
                    }
                } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                    if (momentBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        fanFollowNetworkCall(getActivity(), momentBO.getUser_details().getId(), false, UserSharedPreference.readUserToken());
                    } else {
                        fanFollowNetworkCall(getActivity(), momentBO.getUser_details().getId(), true, UserSharedPreference.readUserToken());
                    }
                } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                    if (commentsBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        fanFollowNetworkCall(getActivity(), commentsBO.getUser_details().getId(), false, UserSharedPreference.readUserToken());
                    } else {
                        fanFollowNetworkCall(getActivity(), commentsBO.getUser_details().getId(), true, UserSharedPreference.readUserToken());
                    }
                } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                    if (likedUsersBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        fanFollowNetworkCall(getActivity(), likedUsersBO.getUser_details().getId(), false, UserSharedPreference.readUserToken());
                    } else {
                        fanFollowNetworkCall(getActivity(), likedUsersBO.getUser_details().getId(), true, UserSharedPreference.readUserToken());
                    }
                } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                    if (blockUserBO.getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                        fanFollowNetworkCall(getActivity(), blockUserBO.getUser_details().getId(), false, UserSharedPreference.readUserToken());
                    } else {
                        fanFollowNetworkCall(getActivity(), blockUserBO.getUser_details().getId(), true, UserSharedPreference.readUserToken());
                    }
                }
                break;
            case R.id.messageParentFrame:
                Animations.buttonBounceAnimation(getActivity(), fanProfileMessage);

                if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                    gotoNextFragment(PrivateMessageFragment.newInstance(streamBO.getUser_details().getId(), true, "", streamBO.getUser_details().getUsername()));
                } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                    gotoNextFragment(PrivateMessageFragment.newInstance(userBO.getId(), true, "", userBO.getUsername()));
                } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                    gotoNextFragment(PrivateMessageFragment.newInstance(momentBO.getUser_details().getId(), true, "", momentBO.getUser_details().getUsername()));
                } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                    gotoNextFragment(PrivateMessageFragment.newInstance(commentsBO.getUser_details().getId(), true, "", commentsBO.getUser_details().getUsername()));
                } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                    gotoNextFragment(PrivateMessageFragment.newInstance(likedUsersBO.getUser_details().getId(), true, "", likedUsersBO.getUser_details().getUsername()));
                } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                    gotoNextFragment(PrivateMessageFragment.newInstance(blockUserBO.getUser_details().getId(), true, "", blockUserBO.getUser_details().getUsername()));
                }
//                MainFragment.newInstance().setSelectedPage(2);
//                getActivity().onBackPressed();
                break;

        }
    }

    private void momentsProfileToggle(boolean showMoments) {
        if (showMoments) {
            momentsParentFrame.setVisibility(View.VISIBLE);
            profileParentFrame.setVisibility(View.GONE);
        } else {
            momentsParentFrame.setVisibility(View.GONE);
            profileParentFrame.setVisibility(View.VISIBLE);
        }
    }

    private void setUpFeaturedRecycler(final List<MomentBO> streamBOList) {

        if (streamBOList != null && streamBOList.size() > 0) {
            if (momentsAdapter == null) {
                momentsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                momentsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                momentsAdapter = new MomentsAdapter(streamBOList, context, momentsRecyclerView, false /*CANT EDIT*/);
                momentsRecyclerView.setAdapter(momentsAdapter);
                momentsRecyclerView.setNestedScrollingEnabled(false);

                momentsAdapter.setClickListener(new MomentsAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        //gotoNextFragment(FanProfileFragment.newInstance());
                    }

                    @Override
                    public void onRowClick(View view, int position, int child) {
                        switch (view.getId()) {
                            case R.id.im_slider:
                                gotoNextFragment(UserMomentsImageView.newInstance(streamBOList.get(position).getImage_arrsy()[child]));
                                break;
                        }
                    }
                });
            } else {
                momentsAdapter.addItems(streamBOList);
                momentsAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }

    }

    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setRefreshing(false);
            momentsRecyclerView.setVisibility(View.GONE);
            noMomentsParentFrame.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            noMomentsParentFrame.setVisibility(View.GONE);
            momentsRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    /*private void getUserStreamsListNetworkCall(final Context context, int offset, int limit, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.user_streams,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                loadMoreStatus = true;
                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (momentsAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("streams");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<StreamBO>>() {
                                }.getType();
                                List<StreamBO> newStreams = (List<StreamBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {

                                    if (momentsAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            momentsAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            momentsAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpFeaturedRecycler(newStreams);
                                }
                            }
                        } catch (Exception ex) {
                            showNoResult(true);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }*/



    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        startIndex = 0;
                        getUserStreamsListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.FEATURED);
                    }
                });
            }
        } else {
            LogUtils.e(TAG, "IS NOT VISIBLE");
        }
    }*/

    // METHOD FOR FAN FOLLOW NETWORK CALL
    private void fanFollowNetworkCall(final Context context, final String userID, final boolean followStatus, String userToken) {

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
                                if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                                    if (followStatus) {
                                        streamBO.getUser_details().setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        fanFollowStatus.setText(R.string.un_follow);
                                    } else {
                                        streamBO.getUser_details().setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        fanFollowStatus.setText(R.string.follow);
                                    }
                                } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                                    if (followStatus) {
                                        userBO.setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        fanFollowStatus.setText(R.string.un_follow);
                                    } else {
                                        userBO.setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        fanFollowStatus.setText(R.string.follow);
                                    }
                                } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                                    if (followStatus) {
                                        momentBO.getUser_details().setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        fanFollowStatus.setText(R.string.un_follow);
                                    } else {
                                        momentBO.getUser_details().setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        fanFollowStatus.setText(R.string.follow);
                                    }
                                } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                                    if (followStatus) {
                                        commentsBO.getUser_details().setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        fanFollowStatus.setText(R.string.un_follow);
                                    } else {
                                        commentsBO.getUser_details().setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        fanFollowStatus.setText(R.string.follow);
                                    }
                                } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                                    if (followStatus) {
                                        likedUsersBO.getUser_details().setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        fanFollowStatus.setText(R.string.un_follow);
                                    } else {
                                        likedUsersBO.getUser_details().setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        fanFollowStatus.setText(R.string.follow);
                                    }
                                } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                                    if (followStatus) {
                                        blockUserBO.getUser_details().setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        fanFollowStatus.setText(R.string.un_follow);
                                    } else {
                                        blockUserBO.getUser_details().setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        fanFollowStatus.setText(R.string.follow);
                                    }
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

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    private void getUserStreamsListNetworkCall(final Context context, int offset, int limit, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.all_moments,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
//                        if (getUserVisibleHint())
//                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (momentsAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("moments");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<MomentBO>>() {
                                }.getType();
                                List<MomentBO> newStreams = (List<MomentBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_record");

                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {

                                    if (momentsAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            momentsAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            momentsAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpFeaturedRecycler(newStreams);
                                }
                            }
                        } catch (Exception ex) {
                            showNoResult(true);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO POST USER BLOCK NETWORK CALL
//    Service Name: block_user
//    Type: Post
//    Params:  block_user{ the id of the user which your are going to block }
    private void setUserBlockNetworkCall(final Context context, String userID, final boolean isAlreadyBlock) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        EnumUtils.ServiceName serviceName;

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        if (isAlreadyBlock) {
            //do unblock
            serviceParams.put(Keys.ID, userID);
            serviceName = EnumUtils.ServiceName.unblock_user;
        } else {
            //do block
            serviceParams.put(Keys.BLOCK_USER, userID);
            serviceName = EnumUtils.ServiceName.block_user;
        }


        new WebServicesVolleyTask(context, false, "",
                serviceName,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
//                        if (getUserVisibleHint())
//                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (momentsAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                if (isAlreadyBlock)
                                    SnackBarUtil.showSnackbar(context, "This user is now unblocked.", false);
                                else
                                    SnackBarUtil.showSnackbar(context, "This user is now blocked.", false);

                                new ExitFragmentAsyncTask().execute();
                            }
                        } catch (Exception ex) {
                            showNoResult(true);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GlobalBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GlobalBus.getBus().unregister(this);
    }

    @Subscribe // METHOD TO GET EVENT MESSAGE
    public void getMessage(Events.BlockUserFragmentMessage blockUserFragmentMessage) {

        boolean isAlreadyBlock;
        if (blockUserFragmentMessage.getMessage().equals("BlockUser")) {
            //block the user...
            isAlreadyBlock = false;
            if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                setUserBlockNetworkCall(getActivity(), streamBO.getUserId(), isAlreadyBlock);
            } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                setUserBlockNetworkCall(getActivity(), userBO.getId(), isAlreadyBlock);
            } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                setUserBlockNetworkCall(getActivity(), momentBO.getUser_id() + "", isAlreadyBlock);
            } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                setUserBlockNetworkCall(getActivity(), commentsBO.getUser_id() + "", isAlreadyBlock);
            } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                setUserBlockNetworkCall(getActivity(), likedUsersBO.getUser_id() + "", isAlreadyBlock);
            } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                setUserBlockNetworkCall(getActivity(), blockUserBO.getUser_details().getId() + "", isAlreadyBlock);
            }
        } else {
            //unblock the user...
            isAlreadyBlock = true;
            if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {
                setUserBlockNetworkCall(getActivity(), streamBO.getUserId(), isAlreadyBlock);
            } else if (userBO != null && objectTypeUser == TYPE_USERBO) {
                setUserBlockNetworkCall(getActivity(), userBO.getId(), isAlreadyBlock);
            } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                setUserBlockNetworkCall(getActivity(), momentBO.getUser_id() + "", isAlreadyBlock);
            } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                setUserBlockNetworkCall(getActivity(), commentsBO.getUser_id() + "", isAlreadyBlock);
            } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                setUserBlockNetworkCall(getActivity(), likedUsersBO.getUser_id() + "", isAlreadyBlock);
            } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                setUserBlockNetworkCall(getActivity(), blockUserBO.getUser_details().getId() + "", isAlreadyBlock);
            }
        }
    }

    // TASK EXIST ACTIVITY SLOWLY
    private class ExitFragmentAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }

    private void setUpUserProfile() {
        try {
            if (userBO != null && objectTypeUser == TYPE_USERBO) {
                userChipsTV.setText(((int) userBO.getUserProgressDetailBO().getChips_spend()) + "");
                witkeyIdTV.setText(userBO.getUser_complete_id());
                ageTV.setText(Utils.calculateUserDOB(userBO.getDateOfBirth()));
                UserLevelBO userLevelBO = Utils.getUserLevel(userBO.getUserProgressDetailBO().getUser_level());
                userLevelTV.setText(userBO.getUserProgressDetailBO().getUser_level() + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                birthdayTV.setText(userBO.getDateOfBirth());
                userStarTV.setText(Utils.getStarDetail(userBO.getDateOfBirth()));
                userCityTV.setText(userBO.getCountry());
                userLevelBoxIV.setImageResource(userLevelBO.getLevelLocalImage());
                userLevelBoxTextView.setText(userBO.getUserProgressDetailBO().getUser_level() + "");

                if (userBO.getIsArtist() == 1) {
                    followParentFrame.setVisibility(View.VISIBLE);
                } else {
                    followParentFrame.setVisibility(View.GONE);
                }

            } else if (streamBO != null && objectTypeUser == TYPE_STREAMBO) {

                userChipsTV.setText(((int) streamBO.getUser_details().getUserProgressDetailBO().getChips_spend()) + "");
                witkeyIdTV.setText(streamBO.getUser_details().getUser_complete_id());
                ageTV.setText(Utils.calculateUserDOB(streamBO.getUser_details().getDateOfBirth()));
                UserLevelBO userLevelBO = Utils.getUserLevel(streamBO.getUser_details().getUserProgressDetailBO().getUser_level());
                userLevelTV.setText(streamBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                birthdayTV.setText(streamBO.getUser_details().getDateOfBirth());
                userCityTV.setText("N/A");// TODO HAVE TO ADD
                userStarTV.setText(Utils.getStarDetail(streamBO.getUser_details().getDateOfBirth()));
//                birthdayTV.setText(streamBO.getUser_details().getDateOfBirth());
//                userCityTV.setText(streamBO.getUser_details().getCountry());
                userLevelBoxIV.setImageResource(userLevelBO.getLevelLocalImage());
                userLevelBoxTextView.setText(streamBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");

                if (streamBO.getUser_details().getIsArtist() == 1) {
                    followParentFrame.setVisibility(View.VISIBLE);
                } else {
                    followParentFrame.setVisibility(View.GONE);
                }

            } else if (momentBO != null && objectTypeUser == TYPE_MOMENTBO) {
                userChipsTV.setText(((int) momentBO.getUser_details().getUserProgressDetailBO().getChips_spend()) + "");
                witkeyIdTV.setText(momentBO.getUser_details().getUser_complete_id());
                ageTV.setText(Utils.calculateUserDOB(momentBO.getUser_details().getDateOfBirth()));
                UserLevelBO userLevelBO = Utils.getUserLevel(momentBO.getUser_details().getUserProgressDetailBO().getUser_level());
                userLevelTV.setText(momentBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                birthdayTV.setText(momentBO.getUser_details().getDateOfBirth());
                userCityTV.setText("N/A");
                userStarTV.setText(Utils.getStarDetail(momentBO.getUser_details().getDateOfBirth()));
//                birthdayTV.setText(momentBO.getUser_details().getDateOfBirth());
//                userCityTV.setText(momentBO.getUser_details().getCountry());
                userLevelBoxIV.setImageResource(userLevelBO.getLevelLocalImage());
                userLevelBoxTextView.setText(momentBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");

                if (momentBO.getUser_details().getIsArtist() == 1) {
                    followParentFrame.setVisibility(View.VISIBLE);
                } else {
                    followParentFrame.setVisibility(View.GONE);
                }

            } else if (commentsBO != null && objectTypeUser == TYPE_COMMENTBO) {
                userChipsTV.setText(((int) commentsBO.getUser_details().getUserProgressDetailBO().getChips_spend()) + "");
                witkeyIdTV.setText(commentsBO.getUser_details().getUser_complete_id());
                ageTV.setText(Utils.calculateUserDOB(commentsBO.getUser_details().getDateOfBirth()));
                UserLevelBO userLevelBO = Utils.getUserLevel(commentsBO.getUser_details().getUserProgressDetailBO().getUser_level());
                userLevelTV.setText(commentsBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                birthdayTV.setText(commentsBO.getUser_details().getDateOfBirth());
                userCityTV.setText("N/A");
                userStarTV.setText(Utils.getStarDetail(commentsBO.getUser_details().getDateOfBirth()));
//                birthdayTV.setText(commentsBO.getUser_details().getDateOfBirth());
//                userCityTV.setText(commentsBO.getUser_details().getCountry());
                userLevelBoxIV.setImageResource(userLevelBO.getLevelLocalImage());
                userLevelBoxTextView.setText(commentsBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");

                if (commentsBO.getUser_details().getIsArtist() == 1) {
                    followParentFrame.setVisibility(View.VISIBLE);
                } else {
                    followParentFrame.setVisibility(View.GONE);
                }

            } else if (likedUsersBO != null && objectTypeUser == TYPE_LIKEDBO) {
                userChipsTV.setText(((int) likedUsersBO.getUser_details().getUserProgressDetailBO().getChips_spend()) + "");
                witkeyIdTV.setText(likedUsersBO.getUser_details().getUser_complete_id());
                ageTV.setText(Utils.calculateUserDOB(likedUsersBO.getUser_details().getDateOfBirth()));
                UserLevelBO userLevelBO = Utils.getUserLevel(likedUsersBO.getUser_details().getUserProgressDetailBO().getUser_level());
                userLevelTV.setText(likedUsersBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                birthdayTV.setText(likedUsersBO.getUser_details().getDateOfBirth());
                userCityTV.setText("N/A");
                userStarTV.setText(Utils.getStarDetail(likedUsersBO.getUser_details().getDateOfBirth()));
//                birthdayTV.setText(likedUsersBO.getUser_details().getDateOfBirth());
//                userCityTV.setText(likedUsersBO.getUser_details().getCountry());
                userLevelBoxIV.setImageResource(userLevelBO.getLevelLocalImage());
                userLevelBoxTextView.setText(likedUsersBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");

                if (likedUsersBO.getUser_details().getIsArtist() == 1) {
                    followParentFrame.setVisibility(View.VISIBLE);
                } else {
                    followParentFrame.setVisibility(View.GONE);
                }
            } else if (blockUserBO != null && objectTypeUser == TYPE_BLOCK_USERBO) {
                userChipsTV.setText(((int) blockUserBO.getUser_details().getUserProgressDetailBO().getChips_spend()) + "");
                witkeyIdTV.setText(blockUserBO.getUser_details().getUser_complete_id());
                ageTV.setText(Utils.calculateUserDOB(blockUserBO.getUser_details().getDateOfBirth()));
                UserLevelBO userLevelBO = Utils.getUserLevel(blockUserBO.getUser_details().getUserProgressDetailBO().getUser_level());
                userLevelTV.setText(blockUserBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");
                userLevelIV.setImageResource(userLevelBO.getLevelLocalImage());
                birthdayTV.setText(blockUserBO.getUser_details().getDateOfBirth());
                userCityTV.setText("N/A");
                userStarTV.setText(Utils.getStarDetail(blockUserBO.getUser_details().getDateOfBirth()));
//                birthdayTV.setText(blockUserBO.getUser_details().getDateOfBirth());
//                userCityTV.setText(blockUserBO.getUser_details().getCountry());
                userLevelBoxIV.setImageResource(userLevelBO.getLevelLocalImage());
                userLevelBoxTextView.setText(blockUserBO.getUser_details().getUserProgressDetailBO().getUser_level() + "");

                if (blockUserBO.getUser_details().getIsArtist() == 1) {
                    followParentFrame.setVisibility(View.VISIBLE);
                } else {
                    followParentFrame.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            LogUtils.e("setUpUserProfile", "" + e.getMessage());
        }


    }
}