package app.witkey.live.fragments.dashboard.payment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.SnackBarUtil;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link WebviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebviewFragment extends BaseFragment implements View.OnClickListener {

    private Context context;
    private String authToken;

    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.messageOptions)
    LinearLayout messageOptions;
    @BindView(R.id.mToolbarTitle)
    TextView mToolbarTitle;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.web_view)
    WebView webView;
    UserBO userBO;
    double amount = 0;
    String paymentID = "0";
    String chipsCount = "0";
    AppSettingsBO appSettingsBO;

//    private String postBackUrl = "http://18.220.157.19/Witkey/api/payment-return?check_status=true";
//    private String paymentUrl = "http://test.gtpayment.com/gtpayment.do";

    public WebviewFragment() {
    }

    public static WebviewFragment newInstance(double amount, String paymentID, String chipsCount) {
        Bundle args = new Bundle();
        args.putDouble("AMOUNT", amount);
        args.putString("PAYMENTID", paymentID);
        args.putString("CHIPSCOUNT", chipsCount);
        WebviewFragment fragment = new WebviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = inflater.getContext();
        return inflater.inflate(R.layout.fragment_web_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setTitleBarData();
        showHideView();
        try {
            appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            if (getArguments() != null) {
                amount = getArguments().getDouble("AMOUNT");
                paymentID = getArguments().getString("PAYMENTID");
                chipsCount = getArguments().getString("CHIPSCOUNT");
                loadWebView(amount, paymentID, appSettingsBO.getPayment_email(), appSettingsBO.getPayment_currency(),
                        appSettingsBO.getPayment_language(), appSettingsBO.getPayment_email(), appSettingsBO.getPayment_ucancel(),
                        appSettingsBO.getPayment_ureturn(), appSettingsBO.getPayment_unotify(), appSettingsBO.getPayment_url(),
                        appSettingsBO.getPayment_post_back_url());
            } else {
                SnackBarUtil.showSnackbar(context, "Something went wrong please try again.", false);
            }
        } catch (Exception e) {
            LogUtils.e("WebviewFragment", "" + e.getMessage());
        }
    }

    private void loadWebView(double amount, String paymentID, String userEmail, String payment_currency, String payment_language,
                             String payment_email, String payment_ucancel, String payment_ureturn, String payment_unotify,
                             String payment_url, final String payment_post_back_url) {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        btnBack.setOnClickListener(this);

        //enable cookies...
        final CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        } else {
            cookieManager.setAcceptCookie(true);
        }

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                Log.i("", "Processing webview url click..." + request.getUrl());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                Log.i("", "Processing webview url click..." + url);
                if (url.contains(payment_post_back_url)) {
                    webView.setVisibility(View.GONE);
                    getData(url);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }

            /*
            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
              final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                String message = "SSL Certificate error.";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_UNTRUSTED:
                        message = "The certificate authority is not trusted.";
                        break;
                    case SslError.SSL_EXPIRED:
                        message = "The certificate has expired.";
                        break;
                    case SslError.SSL_IDMISMATCH:
                        message = "The certificate Hostname mismatch.";
                        break;
                    case SslError.SSL_NOTYETVALID:
                        message = "The certificate is not yet valid.";
                        break;
                }
                message += " Do you want to continue anyway?";

                builder.setTitle("SSL Certificate Error");
                builder.setMessage(message);
                builder.setCancelable(false);
                builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.proceed();
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.cancel();
                        showErrorAlert();
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            }*/

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                Log.i("", "onPageFinished: " + url);
                progressBar.setVisibility(View.GONE);
            }
        });

        //TODO: Set the dynamic data here...
        HashMap<String, Object> serviceParams = new HashMap<>();
        serviceParams.put("lang", payment_language);
        serviceParams.put("member", userEmail);
        serviceParams.put("productid", paymentID);
        serviceParams.put("product", "PRODUCT DETAIL");
        serviceParams.put("price", "" + amount);
        serviceParams.put("membercurrency", payment_currency);
        serviceParams.put("ucancel", payment_ucancel);
        serviceParams.put("ureturn", payment_ureturn);
        serviceParams.put("unotify", payment_unotify);
        serviceParams.put("trace_no", "abcedf");
        serviceParams.put("custom_email", payment_email);
        serviceParams.put("secret_key", UserSharedPreference.readUserPaymentKey());//"1d557a1e03126cd28ea442aba1c280a5a9b5d0eb");
        serviceParams.put("api_exclude", "");


        String postData = getPostData(serviceParams);

        webView.postUrl(payment_url, EncodingUtils.getBytes(postData, "utf-8"));

    }

    private void setTitleBarData() {
        mToolbarTitle.setText(R.string.gt_pay);
    }

    private void showHideView() {
        btnBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setVisibility(View.VISIBLE);
        messageOptions.setVisibility(View.GONE);
    }

    private void getData(String newUrl) {
        URL url = null;
        try {
            url = new URL(newUrl);
            String query = url.getQuery();
            Map<String, String> data = new HashMap<String, String>();
            for (String q : query.split("&")) {
                String[] qa = q.split("=");
                String name = URLDecoder.decode(qa[0]);
                String value = "";
                if (qa.length == 2) {
                    value = URLDecoder.decode(qa[1]);
                }

                data.put(name, value);
            }//loop
            boolean isPaymentSuccessful = false;
            if (data.size() > 0) {
                isPaymentSuccessful = true;
                if (data.containsKey("transaction_id") && data.get("transaction_id").equals("0")) {
                    isPaymentSuccessful = false;
                }

            }
            if (isPaymentSuccessful && data.containsKey("invoice") && data.containsKey("status") && data.get("status").equals("SUCCESS")) {
                String trancsactionID = data.get("transaction_id");
                String orderRefNumber = data.get("invoice");
//                paymentBO.setTrxID(orderRefNumber);
//                paymentBO.setPaymentMode(EnumUtils.PaymentMode.EASYPAY.getNumVal());
//                bookNowService(paymentBO);
                notifyPaymentSuccessNetworkCall(getActivity(), userBO.getId() != null ? userBO.getId() : "0", amount,
                        data.containsKey("checksign") ? data.get("checksign") : "0", data.containsKey("custom_email") ? data.get("custom_email") : "0",
                        data.get("invoice"), data.containsKey("productid") ? data.get("productid") : "0", data.get("status"),
                        data.containsKey("trace_no") ? data.get("trace_no") : "0", data.containsKey("transaction_id") ? data.get("transaction_id") : "0",
                        UserSharedPreference.readUserToken(), paymentID);
                //showSuccessAlert(trancsactionID, orderRefNumber);
            } else {
                showErrorAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorAlert();
        }
    }

    private void showErrorAlert() {
        AlertOP.showAlert(context, "Error", "Your transaction is not successful. Please try again."
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().onBackPressed();
                    }
                });
    }

    private void showSuccessAlert(String transactionID, String invoice) {
        AlertOP.showAlert(context, "Alert", "Your transaction is successful.\n" +
                        "Transaction ID: " + transactionID + "\n" +
                        "Invoice: " + invoice
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().onBackPressed();
                    }
                });
    }

    private String getPostData(HashMap<String, Object> serviceParams) {
        String postData = null;
        try {
            if (serviceParams != null && serviceParams.size() > 0) {
//                Map.Entry<String, Object> entry = serviceParams.entrySet().iterator().next();
//                String key = entry.getKey();

                postData = "";
                Iterator it = serviceParams.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    String key = ((String) pair.getKey());
                    String value = pair.getValue().toString();
                    if (!key.equals(Keys.Body)) {
//                        String paramString = key + "="
//                                + URLEncoder.encode(value, "UTF-8");
                        String paramString = key + "="
                                + value;
                        if (postData.length() > 1) {
                            postData += "&" + paramString;
                        } else {
                            postData += paramString;
                        }
                    }
                }
            }//10020161
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return postData;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                getActivity().onBackPressed();
                break;
        }
    }


    //service name: add-transaction'
    //Type: Post
    //Params: 'invoice','transaction_id','checksign', 'status','productid','custom_email', 'amount','user_id', 'trace_no'
    // METHOD TO MAKE NETWORK CALL TO NOTIFY SERVER ABOUT PAYMENT SUCCESS
    public void notifyPaymentSuccessNetworkCall(final Context context, String userID, double paymentAmount, String paymentCheckSign,
                                                String paymentCustomEmail, String paymentInvoice, String paymentProductID
            , String paymentStatus, String paymentTraceID, String paymentTransactionID, String token, String paymentID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        serviceParams.put(Keys.USER_ID, userID);

        serviceParams.put(Keys.PAYMENT_AMOUNT, paymentAmount);
        serviceParams.put(Keys.PAYMENT_CHECK_SIGN, paymentCheckSign);
//        serviceParams.put(Keys.PAYMENT_CUSTOM_EMAIL, paymentCustomEmail);
        serviceParams.put(Keys.PAYMENT_INVOICE, paymentInvoice);
        serviceParams.put(Keys.PAYMENT_PRODUCT_ID, paymentProductID);
//        serviceParams.put(Keys.PAYMENT_STATUS, paymentStatus);
        serviceParams.put(Keys.PAYMENT_TRACE_ID, paymentTraceID);
        serviceParams.put(Keys.PAYMENT_TRANSACTION_ID, paymentTransactionID);
        serviceParams.put(Keys.PAYMENT_PACKAGE_ID, paymentID);

        tokenServiceHeaderParams.put(Keys.TOKEN, token);


        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.add_transaction,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        // DO SOME THING HERE
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                // DO SOME THING HERE

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                UserBO userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
                                userBO.setChips(jsonObject.getInt("updated_chips"));
                                ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
                                AlertOP.showAlert(context, null, "" + chipsCount + " chips has been added in your wallet", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getActivity().onBackPressed();
                                    }
                                });
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