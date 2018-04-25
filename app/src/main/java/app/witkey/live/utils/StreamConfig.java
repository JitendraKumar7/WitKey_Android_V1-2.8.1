package app.witkey.live.utils;

import android.content.Context;

import com.ksyun.media.streamer.encoder.VideoEncodeFormat;
import com.ksyun.media.streamer.framework.AVConst;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.kit.StreamerConstants;

import app.witkey.live.Constants;
import app.witkey.live.items.StreamBO;

/**
 * created by developer on 10/04/2017.
 * KSY LIB
 */

public class StreamConfig {

    private static KSYStreamer mStreamer = null;

    /*THINGS NEEDED*/
    private static int frameRate = 15; /*Acquisition frame rate : 15*/
    private static int videoBitrate = 800; /*Video bit rate(Max) : 800*/
    private static int audioBitRate = 48; /*Audio rate : 48*/
    private static int videoResolution = StreamerConstants.VIDEO_RESOLUTION_480P; /*StreamerConstants.VIDEO_RESOLUTION_360P, VIDEO_RESOLUTION_480P, VIDEO_RESOLUTION_540P, VIDEO_RESOLUTION_720P*/
    private static int encodeType = AVConst.CODEC_ID_AVC;/* AVConst.CODEC_ID_HEVC, AVConst.CODEC_ID_AVC*/
    private static int encodeMethod = StreamerConstants.ENCODE_METHOD_HARDWARE;/*StreamerConstants.ENCODE_METHOD_HARDWARE, StreamerConstants.ENCODE_METHOD_SOFTWARE, StreamerConstants.ENCODE_METHOD_SOFTWARE*/
    private static int encodeScene = VideoEncodeFormat.ENCODE_SCENE_DEFAULT;/* VideoEncodeFormat.ENCODE_SCENE_DEFAULT, VideoEncodeFormat.ENCODE_SCENE_SHOWSELF*/
    private static int encodeProfile = VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER;/*VideoEncodeFormat.ENCODE_PROFILE_LOW_POWER, VideoEncodeFormat.ENCODE_PROFILE_BALANCE, VideoEncodeFormat.ENCODE_PROFILE_HIGH_PERFORMANCE*/
    private static int orientation = 0;/*ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : 90, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT : 0, ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR : 180*/

    private static String createStreamURl(StreamBO streamBO) {
        if (streamBO != null) {
            // SAMPLE rtmp://18.220.157.19:1935/live/myStream
            return "rtmp://" + streamBO.getStreamIp() + ":" + streamBO.getStreamPort() + "/" + streamBO.getStreamApp() + "/" + streamBO.getUuid();
        }
        return null;
    }

    public static KSYStreamer getStreamerObject(Context context, StreamBO streamBO) {

        mStreamer = new KSYStreamer(context);
        try {

            mStreamer.setUrl(createStreamURl(streamBO));
            mStreamer.setPreviewFps(frameRate);
            mStreamer.setTargetFps(frameRate);

            mStreamer.setVideoKBitrate(videoBitrate * 3 / 4, videoBitrate, videoBitrate / 4);

            mStreamer.setAudioKBitrate(audioBitRate);
            mStreamer.setPreviewResolution(videoResolution);
            mStreamer.setTargetResolution(videoResolution);
            mStreamer.setVideoCodecId(encodeType);
            mStreamer.setEncodeMethod(encodeMethod);
            mStreamer.setVideoEncodeScene(encodeScene);
            mStreamer.setVideoEncodeProfile(encodeProfile);
            mStreamer.setRotateDegrees(orientation);
            mStreamer.setCameraFacing(0);

            mStreamer.setIFrameInterval(1f);
//            mStreamer.setVideoBitrate(videoBitrate);
//            mStreamer.setVideoBitrate(10,10,10);

            mStreamer.setEnableAutoRestart(true, 3000);
            mStreamer.setFrontCameraMirror(false);
            mStreamer.setMuteAudio(!Constants.ENABLE_STREAM_VOICE);
            mStreamer.setEnableAudioPreview(false);
            mStreamer.setEnableAudioMix(false);
        } catch (Exception e) {
            LogUtils.e("getStreamerObject", "" + e.getMessage());
        }
        return mStreamer;
    }

}
