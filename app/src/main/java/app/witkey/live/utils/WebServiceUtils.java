package app.witkey.live.utils;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.Constants;
import app.witkey.live.items.TaskItem;


/**
 * created by developer on 8/11/2015.
 */

public class WebServiceUtils {

    public static String getResponseMessage(TaskItem taskItem) {
        if (taskItem.getServiceError() != null) {
            EnumUtils.ServiceResponseMessage serviceErrors = taskItem.getServiceError();
            if (serviceErrors != null) {
                switch (serviceErrors) {
                    case InvalidResponse:
                        return taskItem.getServiceStatusMessage(); // "Invalid Response"
                    case NetworkError:
                        return "The internet connection seems to be offline.";
                    case ServerNotReachable:
                        return "Server not reachable.";
                    case ConnectionTimeOut:
                        return "Connection Timeout";
                    default:
                        return "Invalid Service";
                }
            }
        } else {
            return taskItem.getServiceStatusMessage();
        }

        return "";
    }

    public static HashMap<String, Object> getServiceParams(String[] keyArray, Object[] valueArray) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < keyArray.length; i++) {
            map.put(keyArray[i], valueArray[i]);
        }
        return map;
    }

    public static boolean isEncrypt(String key) {
        if (key.contains(":")) {
            if (key.split(":")[1].equals("Encrypt"))
                return true;
            else
                return false;
        } else {
            return false;
        }
    }

    public static void showResponseError(Context context, TaskItem taskItem) {
        AlertOP.showAlert(context, "", WebServiceUtils.getResponseMessage(taskItem));
    }


    //if service name contain '-' this function will change to '_'. because we are using enum for webservice name.
    public static String filterServiceName(String serviceName) {
        if (serviceName.contains("_id")) {
            serviceName = serviceName.toString().replace("_id", "");
        }else if (serviceName.contains("_")) {
            serviceName = serviceName.toString().replace("_", "-");
        }
        return serviceName;
    }

    //if service name contain '-' this function will change to '_'. because we are using enum for webservice name.
    public static boolean checkServiceNameID(EnumUtils.ServiceName serviceName) {
        return serviceName.toString().contains("_id");
    }

    public static TaskItem parseResponse(String response, EnumUtils.ServiceName serviceName) {

        TaskItem taskItem = new TaskItem();
        taskItem.setServiceName(serviceName);
        LogUtils.i("WebService", "parseResponse: " + response);
        try {
                // JSONObject jsonError = jsonObject.getJSONObject("Error");
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
                    taskItem.setServiceError(EnumUtils.ServiceResponseMessage.InvalidResponse);

                } else {
                    taskItem.setError(false);
                }




        } catch (JSONException e) {
            taskItem.setError(true);
            taskItem.setServiceError(EnumUtils.ServiceResponseMessage.InvalidResponse);
            e.printStackTrace();
        }

        return taskItem;
    }

}//main
