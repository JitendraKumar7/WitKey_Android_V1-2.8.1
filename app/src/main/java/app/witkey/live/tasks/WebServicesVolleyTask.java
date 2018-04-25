package app.witkey.live.tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.utils.EnumUtils.AppDomain;
import app.witkey.live.utils.EnumUtils.RequestMethod;
import app.witkey.live.utils.EnumUtils.ServiceName;
import app.witkey.live.utils.EnumUtils.ServiceResponseMessage;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.WebServiceUtils;

/**
 * created by developer on 5/30/2017.
 * <p>
 * This class contains new_ WebService architecture that is dependent on Volley library for calling services.
 * Currently this class is not using in the app but we will use this in future and replace this with
 * the current Services architecture.
 */
public class WebServicesVolleyTask {

    private Context context;
    private boolean isShowLoader, isExternalService = false;
    private ServiceName serviceName;
    private RequestMethod requestMethod;
    private HashMap<String, Object> serviceParams = new HashMap<String, Object>();
    private HashMap<String, Object> serviceHeaders;
    private String serverUrl;
    public AsyncResponseCallBack delegate = null;
    String dialogMessage, postData = null;
    ProgressDialog alertDialogFragment;
    private StringRequest request;
    private String TAG = "JKS";//this.getClass().getSimpleName();

    public WebServicesVolleyTask(Context context, boolean isShowLoader,
                                 String dialogMessage, ServiceName serviceName,
                                 RequestMethod requestMethod, HashMap<String, Object> service_Params,
                                 HashMap<String, Object> serviceHeaders,
                                 AsyncResponseCallBack delegate) {
        this.context = context;
        this.isShowLoader = isShowLoader;
        this.dialogMessage = dialogMessage;
        this.serviceName = serviceName;
        this.requestMethod = requestMethod;
        this.serviceParams = service_Params;
        this.serviceHeaders = serviceHeaders;
        this.delegate = delegate;
        this.isExternalService = false;

        // ** Here we check which url is to execute **//
        if (context == null)
            return;

        if (Constants.appDomain == AppDomain.LIVE) {
            serverUrl = context.getResources().getString(R.string.base_url_live);
        } else if (Constants.appDomain == AppDomain.QA) {
            serverUrl = context.getResources().getString(R.string.base_url_qa);
        } else if (Constants.appDomain == AppDomain.DEV) {
            serverUrl = context.getResources().getString(R.string.base_url_dev);
        }

        serverUrl = serverUrl + WebServiceUtils.filterServiceName(serviceName.toString());

        if (serviceName != ServiceName.ExternalService) {

            // IF SERVICE METHOD IS END STREAM THEN APPEND STREAM_ID IN URL
            if (serviceName.equals(ServiceName.end_stream)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.STREAM_ID);
                serviceParams.remove(Keys.STREAM_ID);
            } else if (serviceName.equals(ServiceName.all_streams)) {
                // IF SERVICE METHOD IS ALL STREAMS THEN APPEND OFFSET AND LIMIT IN URL
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.LIST_TYPE);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (serviceName.equals(ServiceName.my_artist_streams)) {
                // IF SERVICE METHOD IS ALL STREAMS THEN APPEND OFFSET AND LIMIT IN URL
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
            } else if (serviceName.equals(ServiceName.user_streams)) {
                // IF SERVICE METHOD IS USER STREAMS THEN APPEND OFFSET AND LIMIT IN URL
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.USER_ID);
            } else if (serviceName.equals(ServiceName.artist)) {
                // IF SERVICE METHOD IS USER STREAMS THEN APPEND OFFSET AND LIMIT IN URL
                if (service_Params.get(Keys.LIST_TYPE).equals("NONE")) {
                    serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET);
                } else {
                    serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.LIST_TYPE);
                }
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (WebServiceUtils.checkServiceNameID(serviceName)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.ID);
                serviceParams.remove(Keys.ID);
            } else if (serviceName.equals(ServiceName.cms_pages)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_TYPE);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (serviceName.equals(ServiceName.all_moments)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.USER_ID);
            } else if (serviceName.equals(ServiceName.my_artists_moment)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.USER_ID);
            } else if (serviceName.equals(ServiceName.all_comment)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.MOMENTS_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.MOMENTS_ID);
            } else if (serviceName.equals(ServiceName.all_like)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.MOMENTS_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.MOMENTS_ID);
            } else if (serviceName.equals(ServiceName.top_broadcaster)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.LIST_TYPE);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (serviceName.equals(ServiceName.top_spender)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.LIST_TYPE);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (serviceName.equals(ServiceName.top_fan)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.LIST_TYPE);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (serviceName.equals(ServiceName.artist_stream)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.LIST_TYPE);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.LIST_TYPE);
            } else if (serviceName.equals(ServiceName.my_fan)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
            } else if (serviceName.equals(ServiceName.get_blocked_user)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
            } else if (serviceName.equals(ServiceName.get_banner)) {
                serverUrl = serverUrl + "/2/" + serviceParams.get(Keys.TYPE) + "/" + serviceParams.get(Keys.STREAM_ID);
                serviceParams.remove(Keys.TYPE);
                serviceParams.remove(Keys.STREAM_ID);
            } else if (serviceName.equals(ServiceName.get_chat)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_LIMIT) + "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.USER_ID);
            } else if (serviceName.equals(ServiceName.get_notifications)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.LIST_TYPE); //+ "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.LIST_OFFSET);
                serviceParams.remove(Keys.LIST_LIMIT);
                serviceParams.remove(Keys.USER_ID);
            } else if (serviceName.equals(ServiceName.is_friend)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.USER_ID) + "/" + serviceParams.get(Keys.FRIEND_USER_ID); //+ "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.USER_ID);
                serviceParams.remove(Keys.FRIEND_USER_ID);
            } else if (serviceName.equals(ServiceName.withdraw_wd)) {
                serverUrl = serverUrl + "/" + serviceParams.get(Keys.ID); //+ "/" + serviceParams.get(Keys.LIST_OFFSET) + "/" + serviceParams.get(Keys.USER_ID);
                serviceParams.remove(Keys.ID);
            }

            if (requestMethod == RequestMethod.GET) {
                callVolleyGetRequest();
            } else {
                callVolleyPostRequest();
            }
        }
    }

    // this constructor is used to handle some special service names that are not handled by enum
    // it only include the serviceName in string...
    public WebServicesVolleyTask(Context context, boolean isShowLoader,
                                 String dialogMessage, ServiceName serviceName, String strServiceName,
                                 RequestMethod requestMethod, HashMap<String, Object> service_Params,
                                 HashMap<String, Object> serviceHeaders,
                                 AsyncResponseCallBack delegate) {
        this.context = context;
        this.isShowLoader = isShowLoader;
        this.dialogMessage = dialogMessage;
        this.serviceName = serviceName;
        this.requestMethod = requestMethod;
        this.serviceParams = service_Params;
        this.serviceHeaders = serviceHeaders;
        this.delegate = delegate;
        this.isExternalService = false;

        // ** Here we check which url is to execute **//

        if (Constants.appDomain == AppDomain.LIVE) {
            serverUrl = context.getResources().getString(R.string.base_url_live);
        } else if (Constants.appDomain == AppDomain.QA) {
            serverUrl = context.getResources().getString(R.string.base_url_qa);
        }


        serverUrl = serverUrl + strServiceName;

        if (serviceName != ServiceName.ExternalService) {
            if (requestMethod == RequestMethod.GET) {
                callVolleyGetRequest();
            } else {
                callVolleyPostRequest();
            }
        }
    }


    //if user wants to send some specific url then call this constructor....
    public WebServicesVolleyTask(Context context, boolean isShowLoader,
                                 String dialogMessage, ServiceName serviceName,
                                 RequestMethod requestMethod, HashMap<String, Object> serviceParams,
                                 HashMap<String, Object> serviceHeaders, String serverUrl, String postBody,
                                 AsyncResponseCallBack delegate) {

        this(context, isShowLoader, dialogMessage, serviceName, requestMethod, serviceParams, serviceHeaders,
                delegate);
        this.isExternalService = true;
        this.serverUrl = serverUrl;


        if (requestMethod == RequestMethod.GET) {
            callVolleyGetRequest();
        } else {
            callVolleyPostRequest();
        }

    }


    private ServiceResponseMessage getServiceError(String strErrorCode) {
        ServiceResponseMessage serviceResponseMessage = ServiceResponseMessage.InvalidResponse;
        try {
            int errorCode = Integer.parseInt(strErrorCode);
            serviceResponseMessage = ServiceResponseMessage.values()[errorCode];
        } catch (Exception ex) {
        }
        return serviceResponseMessage;
    }


    private void callVolleyPostRequest() {
        try {
//        addParams();
            onPreExecute();
            Log.i(TAG, "Call Volley Post Request: " + serverUrl);
            //Log.i("WebServiceTask", "callVolleyPostRequest: " + serverUrl);

            StringRequest request = new StringRequest(
                    requestMethod.ordinal(),
                    serverUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            onPostExecute(parseResponse(response));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            TaskItem taskItem = new TaskItem();
                            taskItem.setError(true);
                            taskItem.setServiceError(ServiceResponseMessage.NetworkError);
                            taskItem.setServiceName(serviceName);
                            onPostExecute(taskItem);
                            error.printStackTrace();
                        }
                    }) {

                @Override
                public String getBodyContentType() {
                    return super.getBodyContentType();
                }


                @Override
                public byte[] getBody() {
                    try {
                        String postData = getPostData();
                        if (postData != null)
                            return postData.getBytes("UTF-8");
                        else {
                            return super.getBody();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return null;
                    }
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getServiceHeaders();
                }

            };

            request.setRetryPolicy(new DefaultRetryPolicy(Constants.WEBSERVICE_WAITTIME,
                    -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        } catch (Exception e) {
            Log.i(TAG,  "" + e.getMessage());
        }
    }


    private Map<String, String> getServiceParams() {
        Map<String, String> paramsMap = new HashMap<>();

        if (serviceParams != null && serviceParams.size() > 0) {
            Iterator it = serviceParams.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                String key = ((String) pair.getKey());
                Object value = pair.getValue();

//                if (WebServiceUtils.isEncrypt(((String) pair.getKey()))) {
//                    paramsMap.put(name.split(":")[0], EncryptionOp
//                            .encryptToB64String(value + ""));
//
//                    Log.i("RestClient", "Encrypted params: " + name.split(":")[0] +
//                            " value: " + EncryptionOp
//                            .encryptToB64String(value + ""));
//                } else
                if (!key.equals(Keys.Body))
                    paramsMap.put(key, value + "");

            }
        }
        return paramsMap;
    }


    @Nullable
    private String getPostData() {
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
                        String paramString = key + "="
                                + URLEncoder.encode(value, "UTF-8");
//                            String paramString = key + "="
//                                    + value;
                        if (postData.length() > 1) {
                            postData += "&" + paramString;
                        } else {
                            postData += paramString;
                        }
                    }
                }


//                if (key.equals(Keys.Body)) {
//                    postData = (String) entry.getValue();
//                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Log.d(TAG,"postData " + postData);
        return postData;
    }


    //multipart
    private void callMultiPart() {

    }

    //------------------------ For volley Get service implementation -----------------------------//
    private void callVolleyGetRequest() {
        try {
            addParams();
            onPreExecute();
            Log.i(TAG, "Call Volley Get Request: " + serverUrl);

            request = new StringRequest(
                    requestMethod.ordinal(),
                    serverUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            onPostExecute(parseResponse(response));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                            TaskItem taskItem = new TaskItem();
                            taskItem.setError(true);
                            if (error instanceof NetworkError || error instanceof NoConnectionError) {

                                taskItem.setServiceError(ServiceResponseMessage.NetworkError);

                            } else if (error instanceof TimeoutError) {
                                taskItem.setServiceError(ServiceResponseMessage.ConnectionTimeOut);
                            } else {

                                taskItem.setServiceStatusMessage("Invalid Response");
                                taskItem.setServiceError(ServiceResponseMessage.InvalidResponse);
                            }

                            taskItem.setServiceName(serviceName);
                            onPostExecute(taskItem);
                        }
                    }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return getServiceHeaders();
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(Constants.WEBSERVICE_WAITTIME,
                    -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            request.setShouldCache(true);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);

        } catch (Exception e) {
            Log.i(TAG,  "" + e.getMessage());
        }
    }

    public void cancelTask() {
        if (request != null) {
            request.cancel();
        }
    }

    // add params is used to append params in service url for GET Request
    private void addParams() {

        try {
            String combinedParams = "";
            if (serviceParams != null && serviceParams.size() > 0) {
                combinedParams += "?";

                Iterator it = serviceParams.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    String key = ((String) pair.getKey());
                    String value = pair.getValue().toString();

                    // if encryption is required to any value then do it...
                    if (WebServiceUtils.isEncrypt(key)) {
//                        key = key.split(":")[0];
//                        value = EncryptionOp
//                                .encryptToB64String(value + "");
                    }
                    if (!key.equals(Keys.Body)) {
                        String paramString = key + "="
                                + URLEncoder.encode(value, "UTF-8");
                        if (combinedParams.length() > 1) {
                            combinedParams += "&" + paramString;
                        } else {
                            combinedParams += paramString;
                        }
                    }
                }
                serverUrl = serverUrl + combinedParams;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //---------------------- General Helping Methods used in both Get/Post requests --------------------//
    private void onPreExecute() {
        try {
            if (isShowLoader) {
                if (TextUtils.isEmpty(dialogMessage)) {
                    dialogMessage = "Loading...";
                }
//                AppCompatActivity activity = ((AppCompatActivity) context);
                alertDialogFragment = ProgressDialog.show(context, "Please wait", dialogMessage);
//                alertDialogFragment.show(activity.getSupportFragmentManager(), "");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void onPostExecute(TaskItem taskItem) {
        try {
            if (isShowLoader && alertDialogFragment != null && alertDialogFragment.isShowing()) {
                alertDialogFragment.dismiss();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (delegate != null)
            delegate.onTaskComplete(taskItem);
    }


    private Map<String, String> getServiceHeaders() {
        Map<String, String> headerMap = new HashMap<>();

        if (serviceHeaders != null) {
            Iterator it = serviceHeaders.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                String name = ((String) pair.getKey());
                Object value = pair.getValue();

                if (WebServiceUtils.isEncrypt(((String) pair.getKey()))) {
                    //used for the case of encrypted parameters...
//                    headerMap.put(name.split(":")[0], EncryptionOp
//                            .encryptToB64String(value + ""));
//
//                    Log.i("RestClient", "Encrypted params: " + name.split(":")[0] +
//                            " value: " + EncryptionOp
//                            .encryptToB64String(value + ""));
                } else
                    headerMap.put(name, value + "");
//				it.remove();
            }
        }

        Log.i(TAG,  "headerMap " + headerMap);
        return headerMap;
    }

    private TaskItem parseResponse(String response) {

        TaskItem taskItem = new TaskItem();
        taskItem.setServiceName(serviceName);
        Log.i(TAG, "parseResponse: " + response);
        try {

            if (isExternalService) {
                taskItem.setError(false);
                taskItem.setResponse(response);
            } else {
                JSONObject jsonObject = new JSONObject(response);
                //first set
                taskItem.setResponse(response);
                taskItem.setServiceStatusMessage(jsonObject
                        .getString("Message"));
                String responseCode = jsonObject
                        .getString("Response");
                taskItem.setErrorCode(responseCode);

//                JSONObject resultJsonObject1 = jsonObject.getJSONObject("Result");

                if (!responseCode.equals(Constants.VALID_RESPONSE_CODE) &&
                        !responseCode.equals(Constants.CREATED_RESPONSE_CODE) &&
                        !responseCode.equals(Constants.UPDATED_RESPONSE_CODE)) {

//                    JSONObject errorObject = resultJsonObject1.getJSONObject("errors");
                    //else in case some other error...
                    taskItem.setError(true);
                    taskItem.setServiceError(ServiceResponseMessage.InvalidResponse);

                } else {
                    taskItem.setError(false);
                }


            }

        } catch (JSONException e) {
            taskItem.setError(true);
            taskItem.setServiceError(ServiceResponseMessage.InvalidResponse);
            e.printStackTrace();
        }

        return taskItem;
    }


    /*
    private void getAuthToken(final ServiceName prevServiceName,
                              final RequestMethod prevRequestMethod,
                              final HashMap<String, Object> prevServiceParams,
                              final HashMap<String, Object> prevServiceHeaders,
                              final AsyncResponseCallBack prevDelegate) {

//		DeviceID as string
//		Source as AppSource (1 for ios and 2 for Android)
//		ID as integer ( 0 on first time token generation)

        JSONObject jObj = null;
        try {
            jObj = new_ JSONObject();
            jObj.put(Keys.DeviceID, DeviceOp.getDeviceID(context));
            jObj.put(Keys.Source, Constants.APP_SOURCE);
            jObj.put(Keys.Token_ID, "0");

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        String[] keyArray = {Keys.Body};
        Object[] valuesArray = {jObj.toString()};

        HashMap<String, Object> tokenServiceParams = WebServiceUtils
                .getServiceParams(keyArray, valuesArray);

        String value = Constants.CONST_HEADER + DateTimeOp.getCurrentDateTimeInUTC(Constants.dateFormat12);

        String[] headerKeyArray = {Keys.Key + Constants.Encrypt};
        Object[] headerValuesArray = {value};

        HashMap<String, Object> tokenServiceHeaderParams = WebServiceUtils
                .getServiceParams(headerKeyArray, headerValuesArray);

        boolean loaderActive = false;
        if (!serviceName.equals(ServiceName.Setting)
                || !serviceName.equals(ServiceName.States))
            loaderActive = true;
        WebServiceAsyncTask task = new_ WebServiceAsyncTask(context,
                loaderActive, null,
                ServiceName.GenerateToken,
                RequestMethod.POST, tokenServiceParams, tokenServiceHeaderParams,
                new_ AsyncResponseCallBack() {

                    @Override
                    public void onTaskComplete(TaskItem taskItem) {

//						updateHeader();
                        //call the previous service in which token is expired or invalid...
                        new_ WebServicesVolleyTask(context, true,
                                null, prevServiceName,
                                prevRequestMethod,
                                prevServiceParams, prevServiceHeaders,
                                prevDelegate);
                    }
                });
        task.execute();
    }
        */

}// mian
