package app.witkey.live.fragments.dashboard.profile.moments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.comments.CommentsAdapter;
import app.witkey.live.adapters.dashboard.message.PrivateChatAdapter;
import app.witkey.live.adapters.dashboard.profile.LikedUsersAdapter;
import app.witkey.live.adapters.dashboard.profile.MomentsSlideViewAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.message.MessageAdminFragment;
import app.witkey.live.fragments.dashboard.message.PrivateMessageFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.CommentsBO;
import app.witkey.live.items.LikedUsersBO;
import app.witkey.live.items.MomentBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.KeyboardOp;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edited by developer on 10/03/2017.
 */

public class UserMomentsComments extends BaseFragment implements View.OnClickListener, OnLikeListener {


    UserBO userBO = null;
    String userID = "";
    private Context context;


    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btnCheck)
    ImageView btnCheck;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.imageParent)
    LinearLayout imageParent;
    @BindView(R.id.momentsImagesVP)
    ViewPager momentsImagesVP;
    @BindView(R.id.pageIndicators)
    LinearLayout pageIndicators;
    @BindView(R.id.likeCountTV)
    CustomTextView likeCountTV;
    @BindView(R.id.commentCountTV)
    CustomTextView commentCountTV;
    @BindView(R.id.likeButton)
    LikeButton likeButton;
    @BindView(R.id.message_button)
    ImageView message_button;
    @BindView(R.id.share_button)
    ImageView share_button;
    @BindView(R.id.shareCountLV)
    LinearLayout shareCountLV;
    @BindView(R.id.mUserProfileImage)
    ImageView mUserProfileImage;
    @BindView(R.id.like_button)
    ImageView like_button;
    @BindView(R.id.mUserNameTextView)
    CustomTextView mUserNameTextView;
    @BindView(R.id.mLastSeenTextView)
    CustomTextView mLastSeenTextView;
    @BindView(R.id.mViewsTextView)
    CustomTextView mViewsTextView;
    @BindView(R.id.mFeaturedCardTextView)
    CustomTextView mFeaturedCardTextView;
    @BindView(R.id.recyclerViewComments)
    RecyclerView recyclerViewComments;
    @BindView(R.id.noResultCommentsFrame)
    LinearLayout noResultCommentsFrame;
    @BindView(R.id.commentCountLV)
    LinearLayout commentCountLV;
    @BindView(R.id.likeCountLV)
    LinearLayout likeCountLV;
    @BindView(R.id.likeParent)
    LinearLayout likeParent;
    @BindView(R.id.commentsChatEDT)
    EditText commentsChatEDT;
    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;
    @BindView(R.id.noResultTitle)
    CustomTextView noResultTitle;
    @BindView(R.id.noResultBody)
    CustomTextView noResultBody;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    LikedUsersAdapter likedUsersAdapter;
    CommentsAdapter commentsAdapter;
    MomentsSlideViewAdapter momentsSlideViewAdapter;
    MomentBO momentBO;

    ImageView[] imageIndicators;
    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 20;
    private int startIndex = 0;
    int totalRecords = 0;
    //    int position_ = 0;
    int likeCount = 0;
    int SHARE_MOMENT = 100;
    boolean commentStatus = true;


    private String TAG = this.getClass().getSimpleName();

    public static UserMomentsComments newInstance(MomentBO momentBO, boolean typeComments) {
        Bundle args = new Bundle();
        args.putParcelable("MOMENTBO", momentBO);
        args.putBoolean("TYPECOMMENTS", typeComments);
        UserMomentsComments fragment = new UserMomentsComments();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_user_moments_comments, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            if (getArguments() != null) {
                momentBO = getArguments().getParcelable("MOMENTBO");
                commentStatus = getArguments().getBoolean("TYPECOMMENTS");
            }
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userID = userBO.getId();
        } catch (Exception e) {
            LogUtils.e("UserMoments", "" + e.getMessage());
        }

        setTitleBarData();
        initView();
        initListeners();
        showHideToolBarView();
        makeFirstCall();
//        showHideNoResultView(false);
    }

    private void initView() {

        if (commentsAdapter != null) {
            commentsAdapter = null;
        }
        if (likedUsersAdapter != null) {
            likedUsersAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (commentStatus) {
//                    message_button.setImageBitmap(new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_message).colorRes(R.color.witkey_orange).sizeDp(25).toBitmap());
//                    like_button.setImageBitmap(new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_thumb_up).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap());
                    message_button.setColorFilter(getContext().getResources().getColor(R.color.witkey_orange));
                    like_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));

                    startIndex = 0;
                    getCommentsListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getMoment_id() + "");
                } else {
//                    message_button.setImageBitmap(new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_message).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap());
//                    like_button.setImageBitmap(new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_thumb_up).colorRes(R.color.witkey_orange).sizeDp(25).toBitmap());

                    like_button.setColorFilter(getContext().getResources().getColor(R.color.witkey_orange));
                    message_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));

                    startIndex = 0;
                    getLikedUsersListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getMoment_id() + "");
                }
            }
        });

        likeButton.setUnlikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_thumb_up).colorRes(android.R.color.darker_gray).sizeDp(20).toBitmap()));
        likeButton.setLikeDrawable(new BitmapDrawable(getResources(), new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_thumb_up).colorRes(R.color.witkey_orange).sizeDp(20).toBitmap()));
//        share_button.setImageBitmap(new IconicsDrawable(getActivity(), CommunityMaterial.Icon.cmd_share).colorRes(android.R.color.darker_gray).sizeDp(25).toBitmap());

        share_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));
        message_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));
        like_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));

        final String[] imagesArray = momentBO.getImage_arrsy();
        imageIndicators = new ImageView[]{};

        if (imagesArray != null && imagesArray.length > 0) {

            imageParent.setVisibility(View.VISIBLE);
            momentsSlideViewAdapter = new MomentsSlideViewAdapter(getActivity(), new ArrayList<>(Arrays.asList(momentBO.getImage_arrsy())));
            momentsImagesVP.setAdapter(momentsSlideViewAdapter);
            if (imagesArray.length > 1) {
                imageIndicators = addIndicators(getActivity(), pageIndicators, imagesArray.length);
                momentsImagesVP.getLayoutParams().height = Utils.getScreenWidth(getActivity());
                momentsImagesVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        // position_ = position;
                        moveIndicator(getActivity(), imagesArray.length, imageIndicators, position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            }
            momentsSlideViewAdapter.setClickListener(new MomentsSlideViewAdapter.ClickListeners() {
                @Override
                public void onItemClick(View view, int position) {
                    switch (view.getId()) {
                        case R.id.im_slider:
                            gotoNextFragment(UserMomentsImageView.newInstance(momentBO.getImage_arrsy()[position]));
                            break;
                    }
                }
            });

        } else {
            imageParent.setVisibility(View.GONE);
        }

        // POPULATING MOMENT DATA
        Utils.setImageRounded(mUserProfileImage, momentBO.getUser_details().getProfilePictureUrl(), getActivity());
        likeButton.setLiked(momentBO.getIs_like() == 1);
        likeCount = momentBO.getTotal_likes();
        mUserNameTextView.setText(momentBO.getUser_details().getUsername());
        likeCountTV.setText(likeCount + "");
        commentCountTV.setText(momentBO.getTotal_comments() + "");
        mFeaturedCardTextView.setText(momentBO.getUser_status_text());
        mLastSeenTextView.setText(Utils.getDifBtwn2Dates(getActivity(), momentBO.getCreated_at()));

        commentsChatEDT.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    String text = commentsChatEDT.getText().toString();
                    if (!TextUtils.isEmpty(text.trim())) {
                        addCommentsNetworkCall(getActivity(), userID, momentBO.getMoment_id() + "", momentBO.getUser_id() + "", text);

                        List<CommentsBO> commentsBOs = new ArrayList<CommentsBO>();
                        commentsBOs.add(new CommentsBO(12, -1, momentBO.getMoment_id() + 2, momentBO.getUser_id() + 1, text, Utils.getCurrentTimeStamp_Streams(), momentBO.getUser_details()));
                        setUpCommentsRecycler(commentsBOs);
                        showHideNoResultView(false);

                        commentsChatEDT.getText().clear();
                        KeyboardOp.hide(getActivity());
                    } else {
                        Utils.showToast(getActivity(), getString(R.string.enter_valid_comment));
                    }
                }
                return false;
            }
        });

        commentsChatEDT.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                commentCountLV.performClick();
            }
        });
    }

    private void initListeners() {
        shareCountLV.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        commentCountLV.setOnClickListener(this);
        likeCountLV.setOnClickListener(this);
        likeButton.setOnLikeListener(this);
        likeParent.setOnClickListener(this);
        noResultRefreshTextView.setOnClickListener(this);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.moments_content);
    }

    private void showHideToolBarView() {
        btnBack.setVisibility(View.VISIBLE);
        btnCheck.setVisibility(View.GONE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void setUpCommentsRecycler(final List<CommentsBO> commentsBOList) {

        if (commentsBOList != null && commentsBOList.size() > 0) {

            if (commentsAdapter == null) {
                recyclerViewComments.setLayoutManager(new LinearLayoutManager(context,
                        LinearLayoutManager.VERTICAL, false));
                recyclerViewComments.setItemAnimator(new DefaultItemAnimator());
                commentsAdapter = new CommentsAdapter(commentsBOList, context, recyclerViewComments);
                recyclerViewComments.setNestedScrollingEnabled(false);
                recyclerViewComments.setAdapter(commentsAdapter);
                commentCountTV.setText(commentsBOList.size() + "");
                recyclerViewComments.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET) {// HIDE
                            MainFragment.showHideBottomTab(false);
                        } else if (dy < -Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET) { // SHOW
                            MainFragment.showHideBottomTab(true);
                        }

                        super.onScrolled(recyclerView, dx, dy);
                    }
                });

                commentsAdapter.setClickListener(new CommentsAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {
                            case R.id.userProfileViewParent:
                                try {
                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(commentsBOList.get(position), FanProfileScrollViewFragment.TYPE_COMMENTBO));
                                } catch (Exception e) {
                                    LogUtils.e("setUpChatViewRecycler", "" + e.getMessage());
                                }
                                break;
                        }
                    }
                });
            } else {
                commentsAdapter.addItems(commentsBOList);
                commentsAdapter.setLoaded();
                commentCountTV.setText(commentsAdapter.getItemCount() + "");
            }
        } else {
            showHideNoResultView(true);
        }
    }

    private void setUpLikedUserRecycler(final List<LikedUsersBO> likedUsersBOList) {

        if (likedUsersBOList != null && likedUsersBOList.size() > 0) {
            if (likedUsersAdapter == null) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerViewComments.setItemAnimator(new DefaultItemAnimator());
                recyclerViewComments.setLayoutManager(linearLayoutManager);
                likeCountTV.setText(likedUsersBOList.size() + "");
                recyclerViewComments.setNestedScrollingEnabled(false);
                likedUsersAdapter = new LikedUsersAdapter(likedUsersBOList, getContext(), recyclerViewComments);
                recyclerViewComments.setAdapter(likedUsersAdapter);

                likedUsersAdapter.setClickListener(new LikedUsersAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {
                        switch (view.getId()) {
                            case R.id.fanNameCustomTextView:
                            case R.id.fanParentFrame:
                            case R.id.fanImageView:
                                try {
                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(likedUsersBOList.get(position), FanProfileScrollViewFragment.TYPE_LIKEDBO));
                                } catch (Exception e) {
                                    LogUtils.e("setUpLikedUserRecycler", "" + e.getMessage());
                                }
//                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(new StreamBO(), userBOList.get(position), FanProfileScrollViewFragment.TYPE_USERBO));
//                                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                               /* try {
                                    gotoNextFragment(FanProfileScrollViewFragment.newInstance(new StreamBO(), userBOList.get(position), FanProfileScrollViewFragment.TYPE_USERBO));
                                    if (broadcasterSearchEditText != null) {
                                        broadcasterSearchEditText.getText().clear();
                                    }
                                    KeyboardOp.hide(getActivity());
                                } catch (Exception e) {
                                    LogUtils.e("SearchBroadcasterFragment", "" + e.getMessage());
                                }*/
                                break;
                            case R.id.btnFanFollow:
                                if (likedUsersBOList.get(position).getUser_details().getIs_follow().equals(EnumUtils.IsFollow.FOLLOWING)) {
                                    fanFollowNetworkCall(getActivity(), likedUsersBOList.get(position).getUser_id() + "", false, UserSharedPreference.readUserToken(), position);
                                } else {
                                    fanFollowNetworkCall(getActivity(), likedUsersBOList.get(position).getUser_id() + "", true, UserSharedPreference.readUserToken(), position);
                                }
                                break;
                        }
                    }
                });

            } else {
                likedUsersAdapter.addItems(likedUsersBOList);
                likedUsersAdapter.setLoaded();
                likeCountTV.setText(likedUsersAdapter.getItemCount() + "");
            }
        } else {
            showHideNoResultView(true);
        }
    }

    private static ImageView[] addIndicators(Context mContext, LinearLayout view, int count) {
        ImageView[] imageViews = new ImageView[count];
        view.removeAllViews();

        for (int i = 0; i < count; i++) {
            imageViews[i] = new ImageView(mContext);
            imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_un_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.setMargins(4, 0, 4, 0);

            view.addView(imageViews[i], params);
        }

        imageViews[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_selected));
        return imageViews;
    }

    private static void moveIndicator(Context context, int totalCount, ImageView[] imageViews, int currentPosition) {
        try {
            for (int i = 0; i < totalCount; i++) {
                imageViews[i].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_un_selected));
            }
            imageViews[currentPosition].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_selected));
        } catch (Exception e) {
            LogUtils.e("moveIndicator", "" + e.getMessage());
        }
    }

    private void showHideNoResultView(boolean showStatus) {
        try {
            if (showStatus) {
                if (commentStatus) {

                    noResultTitle.setText(getString(R.string.no_comments));
                    noResultBody.setText(getString(R.string.be_first_comment));
                } else {
                    noResultTitle.setText(getString(R.string.no_like));
                    noResultBody.setText(getString(R.string.be_first_like));
                }
                noResultCommentsFrame.setVisibility(View.VISIBLE);
                recyclerViewComments.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                noResultCommentsFrame.setVisibility(View.GONE);
                recyclerViewComments.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        } catch (Exception e) {
            LogUtils.e("showHideNoResultView", "" + e.getMessage());
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBack:
                if (getActivity() != null) {
                    KeyboardOp.hide(getActivity());
                    getActivity().onBackPressed();
                }
                break;
            case R.id.shareCountLV:
                if (momentBO != null) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.witkey_moment));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, userBO.getUsername() + " " + getString(R.string.shared_this_moment) + " \n" + getString(R.string.moment_link) + momentBO.getMoment_id());
                    startActivityForResult(Intent.createChooser(sharingIntent, "Share via"), SHARE_MOMENT);
                }
                break;
            /*case R.id.likeParent:
            case R.id.likeButton:
                String likeStatus = "0";
//                likeButton.performClick();
                if (likeButton.isLiked()) {
                    likeStatus = EnumUtils.LIKE_DISLIKE_STATUSES.LIKE;
                } else {
                    likeStatus = EnumUtils.LIKE_DISLIKE_STATUSES.DISLIKE; // DISLIKE HERE
                }
                setUserLikeDislikeNetworkCall(getActivity(), likeStatus, momentBO.getMoment_id() + "", userBO.getId());
                break;*/
            case R.id.commentCountLV:
                KeyboardOp.hide(getActivity());
                commentStatus = true;
                commentsAdapter = null;
                makeFirstCall();
                break;
            case R.id.likeCountLV:
                KeyboardOp.hide(getActivity());
                commentStatus = false;
                likedUsersAdapter = null;
                makeFirstCall();
                break;
            case R.id.noResultRefreshTextView:
                commentsAdapter = null;
                likedUsersAdapter = null;
                makeFirstCall();
                break;
        }
    }

    // METHOD TO ADD USERS COMMENTS NETWORK CALL
//    Service name: add-comment
//    params: user_id, moment_id, moment_user_id, comment
//    Typs : Post
    private void addCommentsNetworkCall(final Context context, String user_id, String moment_id, String moment_user_id, String comment) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.USER_ID, user_id);
        serviceParams.put(Keys.MOMENTS_ID, moment_id);
        serviceParams.put(Keys.MOMENTS_USER_ID, moment_user_id);
        serviceParams.put(Keys.COMMENT_TEXT, comment);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.add_comment,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        /*DO SOMETHING HERE*/
//                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
//                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                /*DO SOMETHING HERE*/
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO GET USERS COMMENTS NETWORK CALL
//    Service Name: all-comment/{per page}/{page no}/{moment_id}
//    Type : get
    private void getCommentsListNetworkCall(final Context context, int offset, int limit, String moment_id) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.MOMENTS_ID, moment_id);

        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.all_comment,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        showHideNoResultView(true);
//                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("Comments");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<CommentsBO>>() {
                                }.getType();
                                List<CommentsBO> newStreams = (List<CommentsBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_record");
                                if (totalRecords == 0) {
                                    showHideNoResultView(true);
                                } else {
                                    if (commentsAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            commentsAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            commentsAdapter.clearItems();
                                        }
                                    }

                                    showHideNoResultView(false);
                                    setUpCommentsRecycler(newStreams);
                                }

                            }
                        } catch (Exception ex) {
                            showHideNoResultView(true);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // METHOD TO GET ALL LIKED USER DETAIL NETWORK CALL
    //http://18.220.157.19/witkey_dev/api/all-like/10/0/14
    private void getLikedUsersListNetworkCall(final Context context, int offset, int limit, String momentID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.MOMENTS_ID, momentID);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.all_like,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        showHideNoResultView(true);
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("Like");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<LikedUsersBO>>() {
                                }.getType();
                                List<LikedUsersBO> newStreams = (List<LikedUsersBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_record");
                                if (totalRecords == 0) {
                                    showHideNoResultView(true);
                                } else {
                                    if (likedUsersAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            likedUsersAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            likedUsersAdapter.clearItems();
                                        }
                                    }

                                    showHideNoResultView(false);
                                    setUpLikedUserRecycler(newStreams);
                                }
                            }
                        } catch (Exception ex) {
                            showHideNoResultView(true);
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void makeFirstCall() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    if (commentStatus) {
                        message_button.setColorFilter(getContext().getResources().getColor(R.color.witkey_orange));
                        like_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));

                        startIndex = 0;
                        getCommentsListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getMoment_id() + "");
                    } else {

                        message_button.setColorFilter(getContext().getResources().getColor(android.R.color.darker_gray));
                        like_button.setColorFilter(getContext().getResources().getColor(R.color.witkey_orange));

                        startIndex = 0;
                        getLikedUsersListNetworkCall(getActivity(), startIndex, maxItems, momentBO.getMoment_id() + "");
                    }
                }
            });

        }
    }

    // METHOD TO GET ALL USER STREAMS NETWORK CALL
    //service name: moment-action
//    param: user_id, moment_id, type{50,:share, 60:like}
    private void setUserLikeDislikeNetworkCall(final Context context, String type, String moment_id, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.TYPE, type);
        serviceParams.put(Keys.MOMENTS_ID, moment_id);
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
    public void liked(LikeButton likeButton) {
        if (momentBO != null) {
            likeCount = likeCount + 1;
            likeCountTV.setText(likeCount + "");
        }
        setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.LIKE, momentBO.getMoment_id() + "", userBO.getId());
    }

    @Override
    public void unLiked(LikeButton likeButton) {
        if (momentBO != null) {
            likeCount = likeCount - 1;
            likeCountTV.setText((likeCount > 0 ? likeCount : 0) + "");
        }
        setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.DISLIKE, momentBO.getMoment_id() + "", userBO.getId());
    }

    // METHOD FOR FAN FOLLOW NETWORK CALL
    private void fanFollowNetworkCall(final Context context, final String userID, final boolean followStatus, String userToken, final int index) {

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

                                if (taskItem.getResponse() != null) {
                                    if (likedUsersAdapter != null && likedUsersAdapter.getItemCount() > 0) {
                                        if (followStatus) {
                                            likedUsersAdapter.getItem(index).getUser_details().setIs_follow(EnumUtils.IsFollow.FOLLOWING);
                                        } else {
                                            likedUsersAdapter.getItem(index).getUser_details().setIs_follow(EnumUtils.IsFollow.NOT_FOLLOWING);
                                        }
                                        likedUsersAdapter.notifyDataSetChanged();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SHARE_MOMENT) {
            if (resultCode == Activity.RESULT_OK) {
                setUserLikeDislikeNetworkCall(getActivity(), EnumUtils.LIKE_DISLIKE_STATUSES.SHARE, momentBO.getMoment_id() + "", userBO.getId());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                /*DO SOME THING HERE*/
            }
        }
    }


}