package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.GoLiveDollarsAdapter;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by developer on 10/02/2017.
 */

public class GoLiveDollarsDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.goLiveDollerDialogRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.selectorOne)
    LinearLayout selectorOne;
    @BindView(R.id.selectorTwo)
    LinearLayout selectorTwo;
    @BindView(R.id.selectorThree)
    LinearLayout selectorThree;

    @BindView(R.id.goLiveDollerDialogMore)
    ImageView goLiveDollerDialogMore;
    @BindView(R.id.totalTodayTextView)
    CustomTextView totalTodayTextView;
    @BindView(R.id.dateTextView)
    CustomTextView dateTextView;
    @BindView(R.id.timeTextView)
    CustomTextView timeTextView;
    @BindView(R.id.thisHourTextView)
    CustomTextView thisHourTextView;
    @BindView(R.id.endedTextView)
    CustomTextView endedTextView;
    @BindView(R.id.lastHourTextView)
    CustomTextView lastHourTextView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.noResultView)
    LinearLayout noResultView;
    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;
    @BindView(R.id.goLiveDollerDialogTextView)
    CustomTextView goLiveDollerDialogTextView;
    private int maxItems = Constants.MAX_ITEMS_TO_LOAD;
    private int startIndex = 0;
    int totalRecords = 0;
    String selectionType = "5";

    private Dialog dialogWitkeyDollarHourlyRate;
    private GoLiveDollarsAdapter goLiveDollarsAdapter;

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

    public GoLiveDollarsDialog() {

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_dollars, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        initWitkeyDollarHourlyRateDialog();
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

        if (goLiveDollarsAdapter != null) {
            goLiveDollarsAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startIndex = 0;
                getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, selectionType, EnumUtils.ServiceName.top_spender);
            }
        });

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    startIndex = 0;
                    getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, selectionType, EnumUtils.ServiceName.top_spender);
                }
            });
        }


        String currentViewerText = "CURRENT VIEWERS ";
        String currentViewerNumber = "162";

        SpannableString spannableString = new SpannableString(currentViewerText + currentViewerNumber);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), currentViewerText.length(), currentViewerText.length() + currentViewerNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.witkey_orange)), currentViewerText.length(), currentViewerText.length() + currentViewerNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), currentViewerText.length(), currentViewerText.length() + currentViewerNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

//        currentViewersTextView.setText(spannableString);

        selectorOne.setOnClickListener(this);
        selectorTwo.setOnClickListener(this);
        selectorThree.setOnClickListener(this);

        goLiveDollerDialogMore.setOnClickListener(this);
        noResultRefreshTextView.setOnClickListener(this);
        customViewToggle(1);
    }

    private void initWitkeyDollarHourlyRateDialog() {
        dialogWitkeyDollarHourlyRate = new Dialog(getContext());
        dialogWitkeyDollarHourlyRate.setContentView(R.layout.dialog_witkey_dollar_hourly_rate);
        dialogWitkeyDollarHourlyRate.setCanceledOnTouchOutside(true);
        if (dialogWitkeyDollarHourlyRate.getWindow() != null) {
            dialogWitkeyDollarHourlyRate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    private void showWitkeyDollarHourlyRateDialog() {
        if (dialogWitkeyDollarHourlyRate != null)
            dialogWitkeyDollarHourlyRate.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectorOne:
                customViewToggle(1);
                break;
            case R.id.selectorTwo:
                customViewToggle(2);
                break;
            case R.id.selectorThree:
                customViewToggle(3);
                break;
            case R.id.goLiveDollerDialogMore:
                showWitkeyDollarHourlyRateDialog();
                break;
            case R.id.noResultRefreshTextView:
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            startIndex = 0;
                            getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, selectionType, EnumUtils.ServiceName.top_spender);
                        }
                    });
                }
                break;
        }
    }

    private void setUpViewerRecycler(final List<UserBO> userBOList) {


        if (userBOList != null && userBOList.size() > 0) {

            if (goLiveDollarsAdapter == null) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

                goLiveDollarsAdapter = new GoLiveDollarsAdapter(userBOList, getActivity(), mRecyclerView);
                mRecyclerView.setAdapter(goLiveDollarsAdapter);

                goLiveDollarsAdapter.setClickListener(new GoLiveDollarsAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(int position) {
//                        Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                        /*switch (view.getId()) {
                            case R.id.IndexBottom:
                                Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
//                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(userBOList.size() < (position + 6) ? userBOList.get(position + 7) : position + 5, FanProfileScrollViewFragment.TYPE_USERBO));
                                break;
                        }*/

                        dismiss();
                    }
                });

                goLiveDollarsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        if (goLiveDollarsAdapter != null && goLiveDollarsAdapter.getItems().size() >= maxItems
                                && goLiveDollarsAdapter.getItems().size() < totalRecords) {
                            goLiveDollarsAdapter.addLoadingItem();
                            startIndex = startIndex + 1;

                            getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, selectionType, EnumUtils.ServiceName.top_spender);
                        }
                    }
                });


            } else {

                goLiveDollarsAdapter.addItems(userBOList);
                goLiveDollarsAdapter.setLoaded();
            }
        } else

        {
            showNoResult(true);
        }

    }


    // METHOD FOR CUSTOM VIEW TOGGLE
    private void customViewToggle(int position) {
        try {
            if (position == 1) { //POSITION 1
                selectorOne.setBackgroundColor(getResources().getColor(R.color.witkey_orange));
                selectorTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
                selectorThree.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));

                dateTextView.setTextColor(getResources().getColor(R.color.grey_dark));
                totalTodayTextView.setTextColor(getResources().getColor(R.color.witkey_orange));
                timeTextView.setTextColor(getResources().getColor(R.color.grey_dark));
                thisHourTextView.setTextColor(getResources().getColor(R.color.witkey_orange));
                endedTextView.setTextColor(getResources().getColor(R.color.witkey_dim_white));
                lastHourTextView.setTextColor(getResources().getColor(R.color.witkey_dim_white));
                goLiveDollerDialogTextView.setText(getString(R.string.top_spenders) + " " + getString(R.string.last_hour));

                selectionType = "5";
            } else if (position == 2) {//POSITION 2
                timeTextView.setText(Utils.getCurrentTime());
                selectorOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
                selectorTwo.setBackgroundColor(getResources().getColor(R.color.witkey_orange));
                selectorThree.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
                dateTextView.setTextColor(getResources().getColor(R.color.grey_dark));
                totalTodayTextView.setTextColor(getResources().getColor(R.color.witkey_orange));
                timeTextView.setTextColor(getResources().getColor(R.color.witkey_dim_white));
                thisHourTextView.setTextColor(getResources().getColor(R.color.witkey_dim_white));
                endedTextView.setTextColor(getResources().getColor(R.color.grey_dark));
                lastHourTextView.setTextColor(getResources().getColor(R.color.witkey_orange));
                goLiveDollerDialogTextView.setText(getString(R.string.top_spenders) + " " + getString(R.string.this_hour));
                selectionType = "4";
            } else if (position == 3) {//POSITION 3
                dateTextView.setText(Utils.getCurrentDate());
                selectorOne.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
                selectorTwo.setBackgroundDrawable(getResources().getDrawable(R.drawable.edittext_outerline));
                selectorThree.setBackgroundColor(getResources().getColor(R.color.witkey_orange));
                dateTextView.setTextColor(getResources().getColor(R.color.witkey_dim_white));
                totalTodayTextView.setTextColor(getResources().getColor(R.color.witkey_dim_white));
                timeTextView.setTextColor(getResources().getColor(R.color.grey_dark));
                thisHourTextView.setTextColor(getResources().getColor(R.color.witkey_orange));
                endedTextView.setTextColor(getResources().getColor(R.color.grey_dark));
                lastHourTextView.setTextColor(getResources().getColor(R.color.witkey_orange));
                goLiveDollerDialogTextView.setText(getString(R.string.top_spenders) + " " + getString(R.string.today));
                selectionType = "1";
            }
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        startIndex = 0;
                        getTopBroadcastersListNetworkCall(getActivity(), startIndex, maxItems, selectionType, EnumUtils.ServiceName.top_spender);
                    }
                });
            }
        } catch (Exception e) {
            LogUtils.e("customViewToggle", "" + e.getMessage());
        }
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

    // METHOD TO GET ALL USERS DETAIL NETWORK CALL
    private void getTopBroadcastersListNetworkCall(final Context context, int offset, int limit, String listType, EnumUtils.ServiceName serviceName) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                serviceName,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (goLiveDollarsAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = null;
                                favoritesJsonArray = jsonObject.getJSONArray("top-spender");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<UserBO>>() {
                                }.getType();
                                List<UserBO> userBOs = (List<UserBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {
                                    if (goLiveDollarsAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            goLiveDollarsAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            goLiveDollarsAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpViewerRecycler(userBOs);
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

}

