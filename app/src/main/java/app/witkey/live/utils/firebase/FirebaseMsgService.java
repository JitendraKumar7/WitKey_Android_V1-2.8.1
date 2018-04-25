package app.witkey.live.utils.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import app.witkey.live.Constants;
import app.witkey.live.R;
import app.witkey.live.activities.Dashboard;
import app.witkey.live.activities.SplashActivity;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

/**
 * created by developer on 6/15/2017.
 */

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        LogUtils.e("onMessageReceived", "" + Constants.APP_IS_LIVE);
        // Check if message contains a data payload.
        try {
            if (UserSharedPreference.readIsUserLoggedIn()) {
                if (remoteMessage.getData().size() > 0) {
                    checkNotificationType(remoteMessage);
                }
            }
        } catch (Exception e) {
            LogUtils.e("onMessageReceived", "" + e.getMessage());
        }
        // Check if message contains a notification payload.
        /*if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "checkNotificationType: " + remoteMessage.getNotification().getBody());
//            Log.d(TAG, "From DATA: " + remoteMessage.getData().toString());
//            checkNotificationType(remoteMessage.getNotification().getBody());
        }*/
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    // [END receive_message]
    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotificationSimpleText(String messageTitle, String messageBody, String type, String jsonData) {
        Intent intent = new Intent(this, Constants.APP_IS_LIVE ? Dashboard.class : SplashActivity.class);
        intent.putExtra("TYPE", type);
        intent.putExtra("JSON_DATA", jsonData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon_new)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }

    private void checkNotificationType(RemoteMessage remoteMessage) {

        String body = "", type = "", title = "", jsonData = "";
        try {
            if (remoteMessage.getData() != null) {
                Map<String, String> data = remoteMessage.getData();
                type = data.get("type");
                body = data.get("body");
                title = data.get("title");
                jsonData = data.get("id");
                sendNotificationSimpleText(title, body, type, jsonData);
            }
        } catch (Exception e) {
            LogUtils.e("checkNotificationType", "" + e.getMessage());
        }
    }
}
