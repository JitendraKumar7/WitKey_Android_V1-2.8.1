package app.witkey.live.fragments.dashboard.profile.systemsetting.aboutus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.CmsBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.txtPrivacy)
    TextView txtTerms;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    String pageTitle = "", pageType = "";
    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static PrivacyPolicyFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("TYPE", type);
        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            if (getArguments().getString("TYPE").equals(EnumUtils.AboutUs_Type.USER_AGREEMENT)) {
                pageTitle = getString(R.string.user_agreement);
                pageType = EnumUtils.AboutUs_Type.USER_AGREEMENT;
            } else if (getArguments().getString("TYPE").equals(EnumUtils.AboutUs_Type.ABOUT_OUR_COMPANY)) {
                pageType = EnumUtils.AboutUs_Type.ABOUT_OUR_COMPANY;
                pageTitle = getString(R.string.about_our_company);
            } else if (getArguments().getString("TYPE").equals(EnumUtils.AboutUs_Type.COMMUNITY_CONVENTION)) {
                pageTitle = getString(R.string.community_convention);
                pageType = EnumUtils.AboutUs_Type.COMMUNITY_CONVENTION;
            } else {
                pageTitle = getString(R.string.privacy_policy);
                pageType = EnumUtils.AboutUs_Type.PRIVACY_POLICY;
            }
        }

        setTitleBarData(pageTitle);
        showHideView();
        initViews(pageType);
    }

    private void initViews(final String pageType) {

        btnBack.setOnClickListener(this);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.witkey_orange,
                R.color.witkey_orange,
                R.color.witkey_orange);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPrivacyPolicyNetworkCall(getActivity(), pageType, UserSharedPreference.readUserToken());
            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getPrivacyPolicyNetworkCall(getActivity(), pageType, UserSharedPreference.readUserToken());
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY == 0) swipeRefreshLayout.setEnabled(true);
                else swipeRefreshLayout.setEnabled(false);

            }
        });
    }

    private void setTitleBarData(String pageTitle) {
        mToolbarTitle.setText(pageTitle);
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
        }
    }

    // METHOD TO GET PRIVACY POLICY NETWORK CALL
    private void getPrivacyPolicyNetworkCall(final Context context, String pageType, String userToken) {
        txtTerms.setText("");
        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        serviceParams.put(Keys.LIST_TYPE, pageType);
        tokenServiceHeaderParams.put(Keys.TOKEN, userToken);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.cms_pages,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {
                swipeRefreshLayout.setRefreshing(false);
                if (taskItem != null) {
                    if (taskItem.isError()) {
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {
                            if (taskItem.getResponse() != null && getActivity() != null) {
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());

                                Gson gson = new Gson();
                                Type listType = new TypeToken<List<CmsBO>>() {
                                }.getType();
                                List<CmsBO> cmsBOs = (List<CmsBO>) gson.fromJson(
                                        jsonObject.getJSONArray("cms").toString(),
                                        listType);
                                if (cmsBOs.size() > 0) {
                                    CmsBO privacyCmsBO = cmsBOs.get(0);
                                    txtTerms.setText(Html.fromHtml(privacyCmsBO.getBody()));
                                }

                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        // if response is successful then do something
                    }
                }
            }
        });
    }
}