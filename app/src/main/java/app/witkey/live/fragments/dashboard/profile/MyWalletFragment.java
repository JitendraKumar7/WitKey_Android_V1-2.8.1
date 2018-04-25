package app.witkey.live.fragments.dashboard.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.profile.MyWalletAdapter;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.fragments.dashboard.payment.WebviewFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.PackagesBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyWalletFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.noResultParent)
    LinearLayout noResultParent;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.rv_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.walletRecyclerView)
    RecyclerView walletRecyclerView;
    @BindView(R.id.currentChipsTextView)
    CustomTextView currentChipsTextView;
    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;
    @BindView(R.id.noResultRefreshTextView)
    CustomTextView noResultRefreshTextView;

    int totalRecords = 0;
    private int startIndex = 0;
    UserBO userBO;

    private MyWalletAdapter myWalletAdapter;

    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static MyWalletFragment newInstance() {
        Bundle args = new Bundle();
        MyWalletFragment fragment = new MyWalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_wallet, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        } catch (Exception e) {
            LogUtils.e("MyWalletFragment", "" + e.getMessage());
        }
        setTitleBarData();
        showHideView();

        initViews();

    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.wallet);
        currentChipsTextView.setText(((int) userBO.getChips()) + "");

    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void setUpWalletRecycler(final List<PackagesBO> walletBOList) {

        if (walletBOList != null && walletBOList.size() > 0) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            walletRecyclerView.setItemAnimator(new DefaultItemAnimator());
            walletRecyclerView.setLayoutManager(linearLayoutManager);
            myWalletAdapter = new MyWalletAdapter(walletBOList, getContext(), walletRecyclerView);
            walletRecyclerView.setAdapter(myWalletAdapter);

            myWalletAdapter.setClickListener(new MyWalletAdapter.ClickListeners() {
                @Override
                public void onRowClick(View view, int position) {

                    switch (view.getId()) {

//                            gotoNextFragment(WebviewFragment.newInstance(walletBOList.get(position).getAmount(), walletBOList.get(position).getId() + ""));
                        case R.id.mWalletParentFrame:
                        case R.id.btnChipPrice:
                            gotoNextFragment(WebviewFragment.newInstance(walletBOList.get(position).getAmount(), walletBOList.get(position).getId() + "", (walletBOList.get(position).getWitky_chips() + walletBOList.get(position).getFree_chips() + walletBOList.get(position).getPromotion()) + ""));

                            /*if (walletBOList.get(position).getAllow_promotion() == 1) {

                                gotoNextFragment(WebviewFragment.newInstance(walletBOList.get(position).getAmount(), walletBOList.get(position).getId() + "", (walletBOList.get(position).getWitky_chips() + ((walletBOList.get(position).getWitky_chips() * walletBOList.get(position).getPromotion()) / 100)) + ""));
                            } else {
                                gotoNextFragment(WebviewFragment.newInstance(walletBOList.get(position).getAmount(), walletBOList.get(position).getId() + "", walletBOList.get(position).getWitky_chips() + ""));
                            }*/

                            break;
                    }
                }
            });
        } else {
            showNoResult(true);
        }
    }

    private void showNoResult(boolean isShow) {
        if (isShow) {
            swipeRefreshLayout.setVisibility(View.GONE);
            noResultTextView.setVisibility(View.VISIBLE);
            noResultParent.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            noResultParent.setVisibility(View.GONE);
            noResultTextView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initViews() {

        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        btnBack.setOnClickListener(this);

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    getPackagesListNetworkCall(getActivity());
                }
            });
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPackagesListNetworkCall(getActivity());
            }
        });

        noResultRefreshTextView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
            case R.id.noResultRefreshTextView:
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            getPackagesListNetworkCall(getActivity());
                        }
                    });
                }
                break;
        }
    }

    // METHOD TO GET PACKAGES DETAIL NETWORK CALL
    // http://18.220.157.19/witkey_dev/api/get-packages
    private void getPackagesListNetworkCall(final Context context) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.get_packages,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
//                        if (getUserVisibleHint())
//                            AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                        if (myWalletAdapter == null) {
                            showNoResult(true);
                        } else {
                            showNoResult(false);
                        }
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray favoritesJsonArray = jsonObject.getJSONArray("Packages");

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<PackagesBO>>() {
                                }.getType();
                                List<PackagesBO> newStreams = (List<PackagesBO>) gson.fromJson(favoritesJsonArray.toString(),
                                        listType);

                                /*totalRecords = jsonObject.getInt("total_records");
                                if (totalRecords == 0) {
                                    showNoResult(true);
                                } else {*/
                                if (myWalletAdapter != null) {
                                    if (startIndex != 0) {
                                        //for the case of load more...
                                        myWalletAdapter.removeLoadingItem();
                                    } else {
                                        //for the case of pulltoRefresh...
                                        myWalletAdapter.clearItems();
                                    }
                                }

                                showNoResult(false);
                                setUpWalletRecycler(newStreams);
//                                }
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