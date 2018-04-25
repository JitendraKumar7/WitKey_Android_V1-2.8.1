package app.witkey.live.fragments.dashboard.stream;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.AccountSummaryAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.AccountSummaryBO;
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
import app.witkey.live.utils.animations.Animations;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edited by developer on 01/15/2017.
 */

public class AccountSummaryFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.accountSummaryRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.profileNameTextView)
    CustomTextView profileNameTextView;
    @BindView(R.id.profileIDTextView)
    CustomTextView profileIDTextView;

    @BindView(R.id.mProfileImageView)
    ImageView mProfileImageView;

    @BindView(R.id.userRankIV)
    ImageView userRankIV;
    @BindView(R.id.artistRankIV)
    ImageView artistRankIV;

    @BindView(R.id.userLevelBoxTextView)
    CustomTextView userLevelBoxTextView;
    @BindView(R.id.diamondsCountBoxTextView)
    CustomTextView diamondsCountBoxTextView;
    @BindView(R.id.artistViewParent)
    RelativeLayout artistViewParent;

    @BindView(R.id.noResultView)
    LinearLayout noResultView;
    @BindView(R.id.yearBTN)
    LinearLayout yearBTN;
    @BindView(R.id.todayBTN)
    LinearLayout todayBTN;
    @BindView(R.id.monthBTN)
    LinearLayout monthBTN;
    @BindView(R.id.bottomViewParent)
    LinearLayout bottomViewParent;
    @BindView(R.id.bottomViewCashParent)
    LinearLayout bottomViewCashParent;

    @BindView(R.id.cashOutNowCancelBTN)
    CustomTextView cashOutNowCancelBTN;
    @BindView(R.id.buyChipsBTN)
    CustomTextView buyChipsBTN;
    @BindView(R.id.cashOutBTN)
    CustomTextView cashOutBTN;
    @BindView(R.id.monthTV)
    CustomTextView monthTV;
    @BindView(R.id.todayTV)
    CustomTextView todayTV;
    @BindView(R.id.yearTV)
    CustomTextView yearTV;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;
    @BindView(R.id.detailTitleTV)
    CustomTextView detailTitleTV;
    @BindView(R.id.cashOutNowBTN)
    CustomTextView cashOutNowBTN;
    @BindView(R.id.witkeyDollarsTV)
    CustomTextView witkeyDollarsTV;
    @BindView(R.id.availableEarningTV)
    CustomTextView availableEarningTV;
    @BindView(R.id.withdrawLimitTV)
    CustomTextView withdrawLimitTV;
    @BindView(R.id.amountTV)
    CustomTextView amountTV;

    private AccountSummaryAdapter accountSummaryAdapter;

    UserBO userBO;
    UserLevelBO userLevelBO;
    UserLevelBO artistLevelBO;
    UserProgressDetailBO userProgressDetailBO;
    Animations animations;
    int userCurrentLevel = 1, artistCurrentLevel = 1;
    Dialog dialogSuccess;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;
    String selectionType = "2";
    String withdrawLimit = "0";
    boolean cashOutType = false;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static AccountSummaryFragment newInstance() {
        Bundle args = new Bundle();
        AccountSummaryFragment fragment = new AccountSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account_summary, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setTitleBarData();
        populateUserProfileData();
        initViews();
        showHideView();
    }

    private void initViews() {

        if (accountSummaryAdapter != null) {
            accountSummaryAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;

                getAccountSummaryListNetworkCall(getActivity(), startIndex, maxItems, selectionType);
            }
        });

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    startIndex = 0;

                    getAccountSummaryListNetworkCall(getActivity(), startIndex, maxItems, selectionType);
                }
            });
        }

        cashOutNowBTN.setOnClickListener(this);
        cashOutNowCancelBTN.setOnClickListener(this);
        buyChipsBTN.setOnClickListener(this);
        cashOutBTN.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        monthBTN.setOnClickListener(this);
        yearBTN.setOnClickListener(this);
        todayBTN.setOnClickListener(this);
        noResultRefreshTextView.setOnClickListener(this);
        animations = new Animations();
        selectionType(2);
    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.go_live_account_summary);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;

            case R.id.todayBTN:
                selectionType(1);/*TODAY*/
                break;

            case R.id.monthBTN:
                selectionType(2);/*MONTH*/
                break;

            case R.id.yearBTN:
                selectionType(3);/*YEAR*/
                break;

            case R.id.noResultRefreshTextView:
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            startIndex = 0;

                            getAccountSummaryListNetworkCall(getActivity(), startIndex, maxItems, selectionType);
                        }
                    });
                }
                break;
            case R.id.cashOutNowBTN:
                makeTransactionNetworkCall(getActivity(), cashOutType);
//                showSuccessPopup(getActivity(), cashOutType, "$" + withdrawLimit);
                break;

            case R.id.cashOutBTN:
                if (!withdrawLimit.equals("0") && !withdrawLimit.equals("0.00")) {
                    cashOutType = true;
                    bottomViewCashParent.setVisibility(View.VISIBLE);
                    Animations.hideBottomView(bottomViewParent);
                    Animations.showBottomView(bottomViewCashParent);
                    detailTitleTV.setText(getString(R.string.you_have_the_following_available_for_cash_out));
                    amountTV.setText("$" + withdrawLimit);
                } else {
                    SnackBarUtil.showSnackbar(context, getString(R.string.not_enough_coins_to_withdraw), false);
                }
                break;

            case R.id.buyChipsBTN:
                if (!withdrawLimit.equals("0") && !withdrawLimit.equals("0.00")) {
                    cashOutType = false;
                    bottomViewCashParent.setVisibility(View.VISIBLE);
                    Animations.hideBottomView(bottomViewParent);
                    Animations.showBottomView(bottomViewCashParent);
                    detailTitleTV.setText(R.string.convert_cash_to_chips);
                    amountTV.setText("$" + withdrawLimit);
                } else {
                    SnackBarUtil.showSnackbar(context, getString(R.string.not_enough_to_buy_chips), false);
                }
                break;

            case R.id.cashOutNowCancelBTN:
                Animations.hideBottomView(bottomViewCashParent);
                Animations.showBottomView(bottomViewParent);
                bottomViewCashParent.setVisibility(View.GONE);

                break;
        }
    }

    // METHOD TO POPULATE ALL USER PROFILE DATA
    private void populateUserProfileData() {
        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
            userCurrentLevel = userProgressDetailBO.getUser_level();

            artistCurrentLevel = userProgressDetailBO.getArtist_level();
            userLevelBO = Utils.getUserLevel(userCurrentLevel);
            artistLevelBO = Utils.getBroadcasterLevel(artistCurrentLevel);
            if (userBO != null) {
                profileNameTextView.setText(userBO.getUsername().toUpperCase());
                if (userBO.getIsArtist() == 1) {
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getUser_complete_id());
                } else {
                    profileIDTextView.setText(getString(R.string.witkey_id_no) + " " + userBO.getId());
                }
                Utils.setImageRounded(mProfileImageView, userBO.getProfilePictureUrl(), getActivity());

                userLevelBoxTextView.setText(userCurrentLevel + "");
                diamondsCountBoxTextView.setText(artistCurrentLevel + "");

                userRankIV.setImageResource(userLevelBO.getLevelLocalImage());
                artistRankIV.setImageResource(artistLevelBO.getLevelLocalImage());

                if (userBO.getIsArtist() == 1) {
                    artistViewParent.setVisibility(View.VISIBLE);
                } else {
                    artistViewParent.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            LogUtils.e("populateUserProfileData", "" + e.getMessage());
        }
    }

    private void setUpSummaryRecycler(final List<AccountSummaryBO> userBOList) {

        if (userBOList != null && userBOList.size() > 0) {

            if (accountSummaryAdapter == null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                accountSummaryAdapter = new AccountSummaryAdapter(userBOList, getActivity(), mRecyclerView);
                mRecyclerView.setAdapter(accountSummaryAdapter);

                accountSummaryAdapter.setClickListener(new AccountSummaryAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(int position) {
//                        Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                    }
                });

                accountSummaryAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (accountSummaryAdapter != null && accountSummaryAdapter.getItems().size() >= maxItems
                                && accountSummaryAdapter.getItems().size() < totalRecords) {
                            accountSummaryAdapter.addLoadingItem();
                            startIndex = startIndex + 1;

                            getAccountSummaryListNetworkCall(getActivity(), startIndex, maxItems, selectionType);
                        }
                    }
                });

                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dy > Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET) {// HIDE
                            Animations.hideBottomView(bottomViewParent);
                        } else if (dy < -Constants.SHOW_BOTTOM_TAB_SCROLL_OFFSET) { // SHOW
                            Animations.showBottomView(bottomViewParent);
                        }
                        super.onScrolled(recyclerView, dx, dy);
                    }
                });

            } else {

                accountSummaryAdapter.addItems(userBOList);
                accountSummaryAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }

    }

    // METHOD TO GET ACCOUNT SUMMARY DETAIL NETWORK CALL
    //http://18.220.157.19/witkey_dev/api/artist-stream/10/0/3
    private void getAccountSummaryListNetworkCall(final Context context, int offset, int limit, String listType) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.artist_stream,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            showNoResult(true);
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("fans");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<AccountSummaryBO>>() {
                                }.getType();
                                List<AccountSummaryBO> accountSummaryBOList = (List<AccountSummaryBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                populateBottomView(jsonObject);
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (accountSummaryAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            accountSummaryAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            accountSummaryAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpSummaryRecycler(accountSummaryBOList);
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

    // METHOD TO GET TRANSACTION DETAIL NETWORK CALL
    //http://18.220.157.19/witkey_dev/api/withdraw-wd/{id} 1-> chips, 2-> cash out
    private void makeTransactionNetworkCall(final Context context, final boolean cashOutType) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.ID, cashOutType ? "2" : "1");

        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.withdraw_wd,
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
                                if (jsonObject != null) {
                                    if (jsonObject.has("withdrawal_amount")) {
                                        String amount = jsonObject.getString("withdrawal_amount");
                                        showSuccessPopup(context, cashOutType, "$" + amount);
                                    }
                                    if (jsonObject.has("user_progress_detail")) {
                                        UserProgressDetailBO userProgressDetailBO = new Gson().fromJson(jsonObject.get("user_progress_detail").toString(), UserProgressDetailBO.class);
                                        ObjectSharedPreference.saveObject(userProgressDetailBO, Keys.USER_PROGRESS_DETAIL);
                                    }
                                    populateBottomView(jsonObject);
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

    private void selectionType(int type) {
        todayBTN.setBackgroundColor(getResources().getColor(type == 1 ? R.color.witkey_orange : R.color.white));
        monthBTN.setBackgroundColor(getResources().getColor(type == 2 ? R.color.witkey_orange : R.color.white));
        yearBTN.setBackgroundColor(getResources().getColor(type == 3 ? R.color.witkey_orange : R.color.white));

        todayTV.setTextColor(getResources().getColor(type == 1 ? R.color.white : R.color.witkey_orange));
        monthTV.setTextColor(getResources().getColor(type == 2 ? R.color.white : R.color.witkey_orange));
        yearTV.setTextColor(getResources().getColor(type == 3 ? R.color.white : R.color.witkey_orange));

        todayTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
        monthTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
        yearTV.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));

        selectionType = type + "";
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    startIndex = 0;
                    getAccountSummaryListNetworkCall(getActivity(), startIndex, maxItems, selectionType);
                }
            });
        }
        Animations.showBottomView(bottomViewParent);
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            noResultView.setVisibility(View.VISIBLE);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            noResultView.setVisibility(View.GONE);
        }
    }

    private void showSuccessPopup(Context context, final boolean cashOutStatus, String amount) {

        dialogSuccess = new Dialog(context);
        dialogSuccess.setContentView(R.layout.popup_success);
        dialogSuccess.setCancelable(true);
        dialogSuccess.setCanceledOnTouchOutside(true);
        if (dialogSuccess.getWindow() != null) {
            dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        Button btnCancel = (Button) dialogSuccess.findViewById(R.id.btnCancel);
        CustomTextView detailTV = (CustomTextView) dialogSuccess.findViewById(R.id.detailTV);
        CustomTextView dollarTV = (CustomTextView) dialogSuccess.findViewById(R.id.dollarTV);

        dollarTV.setText(amount);
        detailTV.setText(cashOutStatus ? getString(R.string.credited_to_account) : getString(R.string.chips_added_to_wallet));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animations.hideBottomView(bottomViewCashParent);
                Animations.showBottomView(bottomViewParent);
                dialogSuccess.dismiss();
            }
        });

        if (dialogSuccess != null)
            dialogSuccess.show();
    }

    private void populateBottomView(JSONObject jsonObject) {
        try {
            if (jsonObject.has("total_earning")) {
                witkeyDollarsTV.setText("$" + jsonObject.getString("total_earning"));
            }
            if (jsonObject.has("available_earning")) {
                availableEarningTV.setText("$" + jsonObject.getString("available_earning"));
            }
            if (jsonObject.has("withdrawal_limit")) {
                withdrawLimit = jsonObject.getString("withdrawal_limit");
                withdrawLimitTV.setText("$" + jsonObject.getString("withdrawal_limit"));
            }
        } catch (Exception e) {
            LogUtils.e("populateBottomView", "" + e.getMessage());
        }

    }
}