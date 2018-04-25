package app.witkey.live.stream.kit;

import com.faceunity.wrapper.faceunity;
import com.ksyun.media.streamer.filter.imgbuf.ImgBufScaleFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgFilterBase;
import com.ksyun.media.streamer.framework.ImgBufFrame;
import com.ksyun.media.streamer.framework.ImgTexFrame;
import com.ksyun.media.streamer.framework.SinkPin;
import com.ksyun.media.streamer.framework.SrcPin;
import com.ksyun.media.streamer.util.gles.GLRender;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

/**
 * draw face unity prop&beauty
 */

public class ImgFaceunityFilter extends ImgFilterBase {
    private final static String TAG = "ImgFaceunityFilter";

    private final static String VERSION = "1.0.2.1";

    private final static String PROP_TYPE_BEAGLEDOG = "Faceunity/BeagleDog.mp3";
    private final static String PROP_TYPE_COLORGROWN = "Faceunity/ColorCrown.mp3";
    private final static String PROP_TYPE_DEER = "Faceunity/Deer.mp3";

    private final static String PROP_TYPE_EINSTEIN = "Faceunity/einstein.mp3";
    private final static String PROP_TYPE_HAPPYRABBI = "Faceunity/HappyRabbi.mp3";
    private final static String PROP_TYPE_HARTSHORN = "Faceunity/hartshorn.mp3";
    private final static String PROP_TYPE_ITEM0204 = "Faceunity/item0204.mp3";
    private final static String PROP_TYPE_ITEM0208 = "Faceunity/item0208.mp3";
    private final static String PROP_TYPE_ITEM0210 = "Faceunity/item0210.mp3";
    private final static String PROP_TYPE_ITEM0501 = "Faceunity/item0501.mp3";
    private final static String PROP_TYPE_MOOD = "Faceunity/Mood.mp3";
    private final static String PROP_TYPE_PINCESSCROWN = "Faceunity/PrincessCrown.mp3";
    private final static String PROP_TYPE_TIARA = "Faceunity/tiara.mp3";
    private final static String PROP_TYPE_YELLLOWEAR = "Faceunity/YellowEar.mp3";

    private final static String GESTURE_TYPE_HEART = "Faceunity/heart.mp3";

    private final static String BEAUTY_TYPE_NATURE = "nature";
    private final static String BEAUTY_TYPE_DELTA = "delta";
    private final static String BEAUTY_TYPE_ELECTRIC = "electric";
    private final static String BEAUTY_TYPE_SLOWLIVED = "slowlived";
    private final static String BEAUTY_TYPE_TOKYO = "tokyo";
    private final static String BEAUTY_TYPE_WARM = "warm";

    private final static String[] PROPS = {
            PROP_TYPE_TIARA,
            PROP_TYPE_YELLLOWEAR,
            PROP_TYPE_PINCESSCROWN,
            PROP_TYPE_MOOD
//            PROP_TYPE_DEER,
//            PROP_TYPE_BEAGLEDOG,
//            PROP_TYPE_HAPPYRABBI,
//            PROP_TYPE_HARTSHORN,
//            PROP_TYPE_ITEM0204,
//            PROP_TYPE_ITEM0208,
//            PROP_TYPE_ITEM0210,
//            PROP_TYPE_ITEM0501,
//            PROP_TYPE_COLORGROWN,
    };

    private final static String[] GESTURES = {
            GESTURE_TYPE_HEART
    };

    private final static String[] BEAUTYS = {
            BEAUTY_TYPE_NATURE,
            BEAUTY_TYPE_DELTA,
            BEAUTY_TYPE_ELECTRIC,
            BEAUTY_TYPE_SLOWLIVED,
            BEAUTY_TYPE_TOKYO,
            BEAUTY_TYPE_WARM
    };

    private static final int SINK_NUM = 2;
    private GLRender mGLRender;
    private SinkPin<ImgTexFrame> mTexSinkPin;
    private SinkPin<ImgBufFrame> mBufSinkPin;
    private SrcPin<ImgTexFrame> mSrcPin;

    private ImgYFlipFilter mImgYFlipFilter;
    private ImgBufScaleFilter mFaceunityScale;

    private boolean mInited = false;
    private int mFrameID;
    private int[] m_items = new int[3];
    private byte[] mInputBufArray = null;
    private float[] mTexMatrix; // flip vertical matrix
    private Object BUF_LOCK = new Object();

    private String mPropPath;
    private String mCurrentPropPath;
    private String mGesturePath;
    private String mCurrentGesturePath;
    private String mBeautyType;
    private String mCurrentBeautyType;
    public double mBeautyColorLevel = 1.0;
    public int mBeautyBlurLevel = 3;
    public double mBeautyCheekLevel = 0;
    public double mBeautyEyeLevel = 0;

    private Context mContext;
    private int mOutTexture = ImgTexFrame.NO_TEXTURE;
    private int mOutFrameBuffer = -1;
    private int[] mViewPort = new int[4];

    public ImgFaceunityFilter(Context context, GLRender glRender) {
        mContext = context;
        mGLRender = glRender;
        mFrameID = 0;

        mTexSinkPin = new FaceunityTexSinkPin();
        mBufSinkPin = new FaceunityBufSinkPin();
        mSrcPin = new SrcPin<>();
        mGLRender.addListener(mGLRenderListener);

        mImgYFlipFilter = new ImgYFlipFilter(mGLRender);
        mImgYFlipFilter.getSrcPin().connect(mTexSinkPin);
        mFaceunityScale = new ImgBufScaleFilter();
        mFaceunityScale.getSrcPin().connect(mBufSinkPin);

        mTexMatrix = new float[16];
        Matrix.setIdentityM(mTexMatrix, 0);
        Matrix.translateM(mTexMatrix, 0, 0, 1, 0);
        Matrix.scaleM(mTexMatrix, 0, 1, -1, 1);


    }

    public void onPause() {
        mGLRender.queueDrawFrameAppends(new Runnable() {
            public void run() {
                if (m_items[2] != 0) {
                    faceunity.fuDestroyItem(m_items[2]);
                    m_items[2] = 0;
                }
                faceunity.fuOnDeviceLost();
                mFrameID = 0;
            }
        });
    }

    public SinkPin<ImgTexFrame> getTexSinkPin() {
        return mImgYFlipFilter.getSinkPin();
    }

    public SinkPin<ImgBufFrame> getBufSinkPin() {
        return mFaceunityScale.getSinkPin();
    }

    @Override
    public int getSinkPinNum() {
        return SINK_NUM;
    }

    @Override
    public SinkPin<ImgTexFrame> getSinkPin(int i) {
        return mImgYFlipFilter.getSinkPin();
    }

    public SrcPin<ImgTexFrame> getSrcPin() {
        return mSrcPin;
    }

    public void setMirror(boolean isMirror) {
        mFaceunityScale.setMirror(isMirror);
    }

    public void setTargetSize(int targetWith, int targetHeight) {
        mFaceunityScale.setTargetSize(targetWith, targetHeight);
    }

    /**
     * 0~12 Use stickers, others do not use stickers
     *
     * @param index
     */
    public void setPropType(int index) {
        if (index >= PROPS.length || index < 0) {
            mPropPath = null;
            return;
        }

        mPropPath = PROPS[index];
    }

    public void setGestureType(int index) {
        if (index >= GESTURES.length || index < 0) {
            mGesturePath = null;
            return;
        }

        mGesturePath = GESTURES[index];
    }

    /**
     * 0~5 Use beauty, others do not use beauty
     *
     * @param type
     */
    public void setBeautyType(int type) {
        if (type > 5 || type < 0) {
            mBeautyType = null;
            return;
        }

        mBeautyType = BEAUTYS[type];
    }

    public double getBeautyColorLevel() {
        return mBeautyColorLevel;
    }

    /**
     * Whitening level 1.0 is the default
     *
     * @param colorLevel
     */
    public void setBeautyColorLevel(double colorLevel) {
        mBeautyColorLevel = colorLevel;
    }

    /**
     * Debris level 3.0 is the default, in the range of 0-5
     *      * Medium dermabrasion can be set to 3.0 and heavy dermabrasion can be set to 5.0
     *
     * @param blurLevel
     */
    public void setBeautyBlurLevel(int blurLevel) {
        mBeautyBlurLevel = blurLevel;
    }

    /**
     * Set the face-lift level, the default is 0
     *      * 0 is off effect, 1 is the default effect, greater than 1 to further enhance the effect
     *
     * @param cheekLevel
     */
    public void setBeautyCheekLevel(double cheekLevel) {
        mBeautyCheekLevel = cheekLevel;
    }

    public double getBeautyCheekLevel() {
        return mBeautyCheekLevel;
    }

    public double getBeautyEyeLevel() {
        return mBeautyEyeLevel;
    }

    /**
     * Set the big eye level
     *      * 0 is off effect, 1 is the default effect, greater than 1 to further enhance the effect
     *
     * @param eyeLevel
     */
    public void setBeautyEyeLevel(double eyeLevel) {
        mBeautyEyeLevel = eyeLevel;
    }

    public double getBeautyBlurLvevl() {
        return mBeautyBlurLevel;
    }

    public void release() {
        mSrcPin.disconnect(true);
        if (mOutTexture != ImgTexFrame.NO_TEXTURE) {
            mGLRender.getFboManager().unlock(mOutTexture);
            mOutTexture = ImgTexFrame.NO_TEXTURE;
        }
        mInputBufArray = null;
        mGLRender.removeListener(mGLRenderListener);
    }


    private void onGLContextReady() {
        mGLRender.queueDrawFrameAppends(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream is = mContext.getAssets().open("Faceunity/v3.mp3");
                    byte[] v3data = new byte[is.available()];
                    is.read(v3data);
                    is.close();
                    /**
                     * fuSetup parameter explanation
                     * @param v3data
                     * @param null, old parameter, consider removed in the future
                     * @param authpack.A(), auth key byte array content
                     **/
                    faceunity.fuSetup(v3data, null, authpack.A());
                    mInited = true;
                } catch (IOException e) {
                    Log.e(TAG, "IOException: " + e);
                }
            }
        });
    }

    private GLRender.GLRenderListener mGLRenderListener = new GLRender.GLRenderListener() {
        @Override
        public void onReady() {
            mInited = false;
            mOutTexture = ImgTexFrame.NO_TEXTURE;
            ImgFaceunityFilter.this.onGLContextReady();
        }

        @Override
        public void onSizeChanged(int width, int height) {
            //do nothing
        }

        @Override
        public void onDrawFrame() {
            //do nothing
        }

        @Override
        public void onReleased() {
            faceunity.fuOnDeviceLost();
            mFrameID = 0;
        }
    };

    /**
     * Receive video data and add faceunity stickers or beauty
     */
    private class FaceunityTexSinkPin extends SinkPin<ImgTexFrame> {
        @Override
        public void onFormatChanged(Object format) {
            mSrcPin.onFormatChanged(format);
        }

        @Override
        public void onFrameAvailable(ImgTexFrame frame) {
            if (mSrcPin.isConnected()) {
                int isTracking = faceunity.fuIsTracking();
                if (isTracking == 0) {
                    // Face detection status, 0 indicates that no face is detected in the current frame
                }

                // no raw video buf data, or no need to display the sticker, return directly to the input textureid
                if (mInputBufArray != null) {
                    // set system android
                    faceunity.fuItemSetParam(m_items[0], "isAndroid", 1.0);
                    if (!TextUtils.isEmpty(mPropPath)) {
                        createPropItem();
                    } else {
                        if (m_items[0] != 0) {
                            faceunity.fuDestroyItem(m_items[0]);
                            m_items[0] = 0;
                        }
                    }

                    if (!TextUtils.isEmpty(mBeautyType)) {
                        createBeautyItem();
                    } else {
                        if (m_items[1] != 0) {
                            faceunity.fuDestroyItem(m_items[1]);
                            m_items[1] = 0;
                        }
                    }

                    if (!TextUtils.isEmpty(mGesturePath)) {
                        createGestureItem();
                    } else {
                        if (m_items[2] != 0) {
                            faceunity.fuDestroyItem(m_items[2]);
                            m_items[2] = 0;
                        }
                    }

                    if (m_items[0] != 0 || m_items[1] != 0 || m_items[2] != 0) {
                        if (mOutTexture == ImgTexFrame.NO_TEXTURE) {
                            mOutTexture = mGLRender.getFboManager()
                                    .getTextureAndLock(frame.format.width, frame.format.height);
                            mOutFrameBuffer = mGLRender.getFboManager().getFramebuffer(mOutTexture);
                        }
                        GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, mViewPort, 0);
                        GLES20.glViewport(0, 0, frame.format.width, frame.format.height);
                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mOutFrameBuffer);
                        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

                        synchronized (BUF_LOCK) {
                            mOutTexture = faceunity.fuDualInputToTexture(mInputBufArray, frame.textureId, 0,
                                    frame.format.width, frame.format.height, mFrameID++, m_items);
                        }

                        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
                        GLES20.glViewport(mViewPort[0], mViewPort[1], mViewPort[2], mViewPort[3]);

                        GLES20.glEnable(GL10.GL_BLEND);
                        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
                    } else {
                        mOutTexture = frame.textureId;
                    }

                } else {
                    mOutTexture = frame.textureId;
                }

                ImgTexFrame outFrame = new ImgTexFrame(frame.format, mOutTexture,
                        mTexMatrix,
                        frame.pts);

                mSrcPin.onFrameAvailable(outFrame);
            }
        }

        @Override
        public void onDisconnect(boolean recursive) {
            if (recursive) {
                release();
            }
        }
    }

    private class FaceunityBufSinkPin extends SinkPin<ImgBufFrame> {

        @Override
        public void onFormatChanged(Object format) {

        }

        @Override
        public void onFrameAvailable(ImgBufFrame frame) {
            if (frame.buf.limit() > 0) {
                synchronized (BUF_LOCK) {
                    if (mInputBufArray == null) {
                        mInputBufArray = new byte[frame.buf.limit()];
                    }
                    frame.buf.get(mInputBufArray);
                }
            }
        }

        @Override
        public void onDisconnect(boolean recursive) {

        }
    }

    private void createPropItem() {
        //The first time the sticker is displayed, the mCurrentPropPath is assigned
        if (TextUtils.isEmpty(mCurrentPropPath)) {
            mCurrentPropPath = mPropPath;
        }
        //When switching stickers, release the last sticker
        if (!mCurrentPropPath.equals(mPropPath) && m_items[0] != 0) {
            faceunity.fuDestroyItem(m_items[0]);
            m_items[0] = 0;
        }
        mCurrentPropPath = mPropPath;
        //Create a sticker
        if (m_items[0] == 0) {
            try {
                InputStream is = ImgFaceunityFilter.this.mContext.getAssets().open
                        (mCurrentPropPath);
                byte[] item_data = new byte[is.available()];
                is.read(item_data);
                is.close();
                m_items[0] = faceunity.fuCreateItemFromPackage(item_data);
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e);
            }
        }
    }

    private void createGestureItem() {
        //The first time the gesture is shown, the mCurrentGesturePath assignment
        if (TextUtils.isEmpty(mCurrentGesturePath)) {
            mCurrentGesturePath = mGesturePath;
        }
        //When switching stickers, release the last sticker
        if (!mCurrentGesturePath.equals(mGesturePath) && m_items[2] != 0) {
            faceunity.fuDestroyItem(m_items[2]);
            m_items[2] = 0;
        }
        mCurrentGesturePath = mGesturePath;
        //Create a sticker
        if (m_items[2] == 0) {
            try {
                InputStream is = ImgFaceunityFilter.this.mContext.getAssets().open
                        (mCurrentGesturePath);
                byte[] item_data = new byte[is.available()];
                is.read(item_data);
                is.close();
                m_items[2] = faceunity.fuCreateItemFromPackage(item_data);
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e);
            }
        }
    }

    private void createBeautyItem() {
        //Create beauty
        if (m_items[1] == 0) {
            try {
                InputStream is = ImgFaceunityFilter.this.mContext.getAssets().open
                        ("Faceunity/face_beautification.mp3");
                byte[] item_data = new byte[is.available()];
                is.read(item_data);
                is.close();
                m_items[1] = faceunity.fuCreateItemFromPackage(item_data);
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e);
            }
        }

        //Beauty for the first time, mCurrentBeautyType
        mCurrentBeautyType = mBeautyType;
        faceunity.fuItemSetParam(m_items[1], "filter_name", mCurrentBeautyType);
        faceunity.fuItemSetParam(m_items[1], "blur_level", mBeautyBlurLevel);
        faceunity.fuItemSetParam(m_items[1], "color_level", mBeautyColorLevel);
        faceunity.fuItemSetParam(m_items[1], "cheek_thinning", mBeautyCheekLevel);
        faceunity.fuItemSetParam(m_items[1], "eye_enlarging", mBeautyEyeLevel);
    }
}
