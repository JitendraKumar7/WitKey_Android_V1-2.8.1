package app.witkey.live.utils.wowza;

import android.content.Context;

import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WZMediaConfig;
import com.wowza.gocoder.sdk.api.logging.WZLog;

import app.witkey.live.Constants;
import app.witkey.live.items.StreamBO;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;


/**
 * created by developer on 9/27/2017.
 */

public class WowzaUtils {

    protected static WowzaGoCoder sGoCoderSDK = null;
    private static final String SDK_SAMPLE_APP_LICENSE_KEY = "GOSK-5844-0103-8778-96F4-6B37";//"GOSK-3944-0103-6A38-4060-5FC7";//"GOSK-1C44-0103-DC03-BEFC-28F9";//"GOSK-C443-0103-B318-A980-0C90";
    private static WZBroadcast mWZBroadcast = null;
    private static WZBroadcastConfig mWZBroadcastConfig = null;

    public WZBroadcast getBroadcast() {
        return mWZBroadcast;
    }

    public WZBroadcastConfig getBroadcastConfig() {
        return mWZBroadcastConfig;
    }

    public static WowzaGoCoder initWowzaGoCoderInstance(Context context) {
        if (sGoCoderSDK == null) {
            WZLog.LOGGING_ENABLED = true;

            // Initialize the GoCoder SDK
            sGoCoderSDK = WowzaGoCoder.init(context, UserSharedPreference.readUserWowzaKey());
//            sGoCoderSDK = WowzaGoCoder.init(context, SDK_SAMPLE_APP_LICENSE_KEY);

            if (sGoCoderSDK == null) {
                WZLog.error("WitKey", WowzaGoCoder.getLastError());
            }
        }

        return sGoCoderSDK;
    }

    public static WZBroadcastConfig getWowzaBroadcastConfigurations(StreamBO streamBO) {
        // Create a configuration instance for the broadcaster
        try {
//        mWZBroadcastConfig = new WZBroadcastConfig(sGoCoderSDK.getConfig());
            mWZBroadcastConfig = new WZBroadcastConfig(WZMediaConfig.FRAME_SIZE_1280x720);

            mWZBroadcastConfig.setHostAddress(streamBO.getStreamIp());
            mWZBroadcastConfig.setPortNumber(Integer.parseInt(streamBO.getStreamPort()));
            mWZBroadcastConfig.setApplicationName(streamBO.getStreamApp());
//            mWZBroadcastConfig.setStreamName(streamBO.getStream_name());
            mWZBroadcastConfig.setStreamName(streamBO.getUuid());
            mWZBroadcastConfig.setUsername(streamBO.getStream_username());
            mWZBroadcastConfig.setPassword(streamBO.getStream_password());

            mWZBroadcastConfig.setVideoEnabled(true);
            mWZBroadcastConfig.setVideoFrameWidth(WZMediaConfig.DEFAULT_VIDEO_FRAME_WIDTH);
            mWZBroadcastConfig.setVideoFrameHeight(WZMediaConfig.DEFAULT_VIDEO_FRAME_HEIGHT);
            mWZBroadcastConfig.setVideoFramerate(Integer.parseInt(String.valueOf(WZMediaConfig.DEFAULT_VIDEO_FRAME_RATE)));
            mWZBroadcastConfig.setVideoKeyFrameInterval(Integer.parseInt(String.valueOf(WZMediaConfig.DEFAULT_VIDEO_KEYFRAME_INTERVAL)));
            mWZBroadcastConfig.setVideoBitRate(280);
            mWZBroadcastConfig.setABREnabled(true);

            /*TODO HAVE TO REPLACE FALSE WITH TRUE*/
            // audio settings
            mWZBroadcastConfig.setAudioEnabled(Constants.ENABLE_STREAM_VOICE);

            mWZBroadcastConfig.setAudioSampleRate(Integer.parseInt(String.valueOf(WZMediaConfig.DEFAULT_AUDIO_SAMPLE_RATE)));
            mWZBroadcastConfig.setAudioChannels(WZMediaConfig.AUDIO_CHANNELS_STEREO);
            mWZBroadcastConfig.setAudioBitRate(Integer.parseInt(String.valueOf(WZMediaConfig.DEFAULT_AUDIO_BITRATE)));

        } catch (Exception e) {
            LogUtils.d("WowzaUtils ", "" + e.getMessage());
        }
        return mWZBroadcastConfig;
    }
}
