package app.witkey.live.update;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import app.witkey.live.R;

/**
 * Created by HP on 2018/3/12.
 */

public class NotificationFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "NOTIFICATION_FB";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, remoteMessage.getFrom());

        if (remoteMessage.getData().size()>0){
            Log.d(TAG+" data",remoteMessage.getData().toString());
        }

        if(remoteMessage.getNotification()!= null){
            Log.d(TAG+" body",remoteMessage.getNotification().getBody());
        }
    }
}
