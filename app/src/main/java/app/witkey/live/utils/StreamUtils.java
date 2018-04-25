package app.witkey.live.utils;

import android.content.Context;

import com.google.gson.Gson;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.tasks.WebServicesVolleyTask;

/**
 * created by developer on 12/06/2017.
 */

public class StreamUtils {

    public static final String STATUS_ENDED = "20";
    public static final String STATUS_LIVE = "10";

    //    0=Block, 10=Report, 20=Favorite, 30=Watch Later, 40=Save, 50=Share, 60=Like, 70=Dislike, 80=View
    public static final int ACTION_BLOCK = 0;
    public static final int ACTION_REPORT = 10;
    public static final int ACTION_FAVORITE = 20;
    public static final int ACTION_WATCH_LATER = 30;
    public static final int ACTION_SAVE = 40;
    public static final int ACTION_SHARE = 50;
    public static final int ACTION_LIKE = 60;
    public static final int ACTION_DISLIKE = 70;
    public static final int ACTION_VIEW = 80;

    public static String getVideoUrl(StreamBO streamBO) {

        // rtmp://witkeyapp.com:1935/liveorigin/(video unique ID)
        // rtmp://witkeyapp.com:1935/vod/(video unique ID)
        // rtmp://witkeyapp.com:1935/vod/92371247-fb95-11e7-9834-024fa0bb48fa

        if (streamBO.getStatus().equals("20")) {
            return "rtmp://" + streamBO.getStreamIp() + ":" + streamBO.getStreamPort() + "/vod/" + streamBO.getUuid();
        } else {
            return "rtmp://" + streamBO.getStreamIp() + ":" + streamBO.getStreamPort() + "/" + streamBO.getStreamApp() + "/" + streamBO.getUuid();
        }

    }

    public static String getServiceSucessMsgs(Context context, int action) {
        String msg = "";
        switch (action) {
            case ACTION_FAVORITE:
                msg = context.getString(R.string.add_to_favorite);
                break;
            case ACTION_WATCH_LATER:
                msg = context.getString(R.string.add_to_watch_later);
                break;
            case ACTION_SAVE:
                msg = context.getString(R.string.saved_sucessfully);
                break;
            case ACTION_REPORT:
                msg = context.getString(R.string.reported_successfully);
                break;
            case ACTION_BLOCK:
                msg = context.getString(R.string.blocked_successfully);
                break;
        }
        return msg;
    }

    // METHOD TO MAKE NETWORK CALL TO END CURRENT STREAM
    public static void endCurrentStreamNetworkCall2(final Context context, String streamID, String token, String timeDuration) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();
        serviceParams.put(Keys.ID, streamID);
//            String dateTime = "0000-00-00 " + Utils.getTimeInHrMinSec(timeInMilliseconds);
        String dateTime = "0000-00-00";
        serviceParams.put(Keys.STREAM_TIME, timeDuration);
        serviceParams.put(Keys.TIME_DURATION, timeDuration);

        tokenServiceHeaderParams.put(Keys.TOKEN, token);


        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.streams_id,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
                        AlertOP.showAlert(context, null, WebServiceUtils.getResponseMessage(taskItem));
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                // if response is successful then do something
                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                JSONArray streamsJsonArray = jsonObject.getJSONArray("streams");
                                Gson gson = new Gson();

                                //((BaseActivity) (context)).replaceFragment(FindContacts.getInstance());
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

    // METHOD TO MAKE NETWORK CALL TO END CURRENT STREAM
    public static void endCurrentStreamNetworkCall(final Context context, String streamID, String token) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        serviceParams.put(Keys.STREAM_ID, streamID);
        tokenServiceHeaderParams.put(Keys.TOKEN, token);

        new WebServicesVolleyTask(context, false, "",
                EnumUtils.ServiceName.end_stream,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {
                    if (taskItem.isError()) {
//                      // DO NOTHING
                    } else {
                        try {

                            if (taskItem.getResponse() != null) {
                                // DO NOTHING
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }


}
