package app.witkey.live.fragments.dashboard.home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.EventPosterDetailActivity;
import app.witkey.live.activities.GoLiveActivity;
import app.witkey.live.adapters.dashboard.home.HottestAdapter;
import app.witkey.live.fragments.MainFragment;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.profile.fanprofile.FanProfileScrollViewFragment;
import app.witkey.live.fragments.dashboard.stream.GoLiveStreamerStartFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.interfaces.OnLoadMoreListener;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.items.ProgrammeBO;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.stream.GoLiveStreamActivity;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.GiftsUpdateUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.dialogs.RewardPopup;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HottestFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;

    @BindView(R.id.rv_swipe_refresh_tv)
    SwipeRefreshLayout rv_swipe_refresh_tv;

    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    private HottestAdapter hottestAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<ProgrammeBO> programmeBOs;

    private int maxItems = Constants.MAX_ITEMS_TO_LOAD + 5;
    public static final int REQUEST_STORAGE_PERMISSION = 200;
    private int startIndex = 0;
    int totalRecords = 0;
    public static boolean callServerForResult = true;

    public static HottestFragment newInstance() {
        Bundle args = new Bundle();
        HottestFragment fragment = new HottestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_hottest, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        /*FILE DOWNLOAD */
//        checkForGiftUpdate();
        checkForPromotionAvail();
        /*FILE DOWNLOAD */

//      showNoResult(true);/*ON QA REQUEST*/
        initView();
    }

    // METHOD TO INITIALIZE VIEW
    private void initView() {
        if (hottestAdapter != null) {
            hottestAdapter = null;
        }

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (hottestAdapter != null) {
                    hottestAdapter = null;
                }
                startIndex = 0;
                getHottestListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.HOTTEST);

            }
        });

        rv_swipe_refresh_tv.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        rv_swipe_refresh_tv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (hottestAdapter != null) {
                    hottestAdapter = null;
                }
                startIndex = 0;
                getHottestListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.HOTTEST);
            }
        });

        noResultRefreshTextView.setOnClickListener(this);
    }

    private void setUpHottestRecycler(final List<StreamBO> streamBOs, JSONArray jsonArray) {

        if (streamBOs != null && streamBOs.size() > 0) {
            if (hottestAdapter == null) {
                streamBOs.add(0, new StreamBO());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.setLayoutManager(gridLayoutManager);
                //streamBOs.add(0, new StreamBO());
                hottestAdapter = new HottestAdapter(streamBOs, getContext(), mRecyclerView, swipeRefreshLayout.getHeight(), jsonArray);
                mRecyclerView.setAdapter(hottestAdapter);
                hottestAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {

                        try {
                            if (hottestAdapter != null && hottestAdapter.getItems() != null && hottestAdapter.getItems().size() >= maxItems
                                    && hottestAdapter.getItems().size() < totalRecords) {
                                hottestAdapter.addLoadingItem();
                                startIndex = startIndex + 1;
                                getHottestListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.HOTTEST);
                            }
                        } catch (Exception e) {
                            LogUtils.e("setUpHottestRecycler", "" + e.getMessage());
                        }
                    }
                });
                hottestAdapter.setClickListener(new HottestAdapter.ClickListeners() {
                    @Override
                    public void onRowClick(View view, int position) {

                        switch (view.getId()) {
                            case R.id.mParentFrameHottest:
                                // TODO HAVE TO REPLACE THIS FLOW
//                              gotoNextFragment(FanProfileScrollViewFragment.newInstance());
                                startActivity(new Intent(getActivity(), EnumUtils.getCurrentGoLiveViewType())
                                        .putExtra(GoLiveActivity.ARG_TYPE, GoLiveActivity.ARG_PARAM_2)
                                        .putExtra(GoLiveActivity.ARG_PARAM_3, streamBOs.get(position)));
                                break;
                            case R.id.im_slider:
                                if (programmeBOs != null && programmeBOs.size() > 0) {
                                    startActivity(new Intent(getActivity(), EventPosterDetailActivity.class)
                                            .putExtra(EventPosterDetailActivity.INDEX, position)
                                            .putExtra(EventPosterDetailActivity.PROGRAMME_LIST, programmeBOs));
                                }
                                break;
                            case R.id.imageProfileView:
                                gotoNextFragment(FanProfileScrollViewFragment.newInstance(streamBOs.get(position), FanProfileScrollViewFragment.TYPE_STREAMBO));
                                break;
                        }
                    }
                });
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

            } else {
                hottestAdapter.addItems(streamBOs);
                hottestAdapter.setLoaded();
            }
        } else {
            showNoResult(true);
        }
    }

    // METHOD TO SHOW NO RESULT VIEW
    private void showNoResult(boolean isShow) {
        if (isShow) {
            rv_swipe_refresh_tv.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            noResultTextView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
        } else {
            noResultTextView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            rv_swipe_refresh_tv.setVisibility(View.GONE);
            rv_swipe_refresh_tv.setRefreshing(false);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }


    // METHOD TO GET ALL USERS DETAIL NETWORK CALL
    private void getHottestListNetworkCall(final Context context, int offset, int limit, String listType) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.LIST_OFFSET, offset);
        serviceParams.put(Keys.LIST_LIMIT, limit);
        serviceParams.put(Keys.LIST_TYPE, listType);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.all_streams,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        if (getUserVisibleHint())
                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (hottestAdapter == null) {
                            showNoResult(true);
                        }

                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("streams");
                                JSONArray imagesJsonArray = jsonObject.getJSONArray("images_array");
                                JSONArray largeImagesJsonArray = jsonObject.getJSONArray("_large_images_array");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<StreamBO>>() {
                                }.getType();
                                List<StreamBO> newStreams = (List<StreamBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {

                                    if (hottestAdapter != null) {
                                        if (startIndex != 0) {
                                            //for the case of load more...
                                            hottestAdapter.removeLoadingItem();
                                        } else {
                                            //for the case of pulltoRefresh...
                                            hottestAdapter.clearItems();
                                        }
                                    }

                                    showNoResult(false);
                                    setUpHottestRecycler(newStreams, imagesJsonArray);
                                    setLargeImagesJsonData(largeImagesJsonArray);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && callServerForResult) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        if (hottestAdapter != null) {
                            hottestAdapter = null;
                        }
                        startIndex = 0;
                        getHottestListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.HOTTEST);
                    }
                });
            }
        } else {
            LogUtils.e(TAG, "IS NOT VISIBLE");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noResultRefreshTextView:
                rv_swipe_refresh_tv.setRefreshing(true);
                if (hottestAdapter != null) {
                    hottestAdapter = null;
                }
                startIndex = 0;
                getHottestListNetworkCall(getActivity(), startIndex, maxItems, EnumUtils.HomeScreenType.HOTTEST);
                break;
        }
    }

    private void updateGifts() {

        MarshMallowPermission permission = new MarshMallowPermission(getActivity());
        if (permission.checkPermissionForExternalStorage()) {
            GiftsUpdateUtils.checkForGiftUpdated(getActivity());
        } else {
            permission.requestPermissionForExternalStorage(REQUEST_STORAGE_PERMISSION, this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGifts();
                }
                break;
        }
    }

    private void setLargeImagesJsonData(JSONArray jsonImagesArray) {

        programmeBOs = new ArrayList<>();
        try {
            if (jsonImagesArray != null && jsonImagesArray.length() > 0) {
                for (int index = 0; index < jsonImagesArray.length(); index++) {
                    programmeBOs.add(new ProgrammeBO(-1, jsonImagesArray.optString(index)));
                }
            }
        } catch (Exception e) {
            LogUtils.e("setLargeImagesJsonData", "" + e.getMessage());
        }
    }

    private void checkForGiftUpdate() {
        try {
            boolean status = false;
            if (Utils.hasInternetConnection(context)) {

                MarshMallowPermission permission = new MarshMallowPermission(getActivity());
                if (permission.checkPermissionForExternalStorage()) {
                    File giftsFolder = new File(Environment.getExternalStorageDirectory(), GiftsUpdateUtils.WITKEY_GIFTS_FOLDER_PATH);
                    if (!giftsFolder.exists()) {
                        status = true;
                    }
                }

                AppSettingsBO appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
                String userGiftVersion = UserSharedPreference.readUserCurrentGiftVersion();/*TO SAVE*/
                if (appSettingsBO != null && !appSettingsBO.getGift_version().isEmpty() && userGiftVersion != null && !userGiftVersion.isEmpty()
                        && !appSettingsBO.getGift_version().equals(userGiftVersion)) {
                    status = true;
                }

                if (status) {
                    onUpdateDialog(getActivity());
                }
            }
        } catch (Exception e) {
            LogUtils.e("checkForGiftUpdate", "" + e.getMessage());
        }
    }

    private void onUpdateDialog(final Context context) {

        AlertOP.showAlert(context, getString(R.string.download), getString(R.string.cancel), "",
                getString(R.string.download_social_gifts),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateGifts();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertOP.showAlert(context, "", getString(R.string.gift_animations_desc_2), null);
                    }
                });
    }

    private void checkForPromotionAvail() {
        try {
            AppSettingsBO appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
            if (appSettingsBO != null && appSettingsBO.getAllow_initial_promotion() != null && appSettingsBO.getAllow_initial_promotion() == 1) {
                UserBO userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                if (userBO != null) {
                    if (userBO.getPromotion_avail() == 0) { /*0 NOT AVAILED, 1 AVAILED*/
                        RewardPopup rewardPopup = new RewardPopup(getActivity());
                        rewardPopup.setCancelable(true);
                        rewardPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        rewardPopup.show();
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("checkForPromotionAvail", "" + e.getMessage());
        }
    }
}