package com.lion.materialshowcaseview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MaterialShowcaseView extends FrameLayout implements View.OnTouchListener, View.OnClickListener {

    List<IShowcaseListener> mListeners; // external listeners who want to observe when we show and dismiss
    private int mOldHeight;
    private int mOldWidth;
    private Bitmap mBitmap;// = new WeakReference<>(null);
    private Canvas mCanvas;
    private Paint mEraser;
    private Paint boxPaint;
    private Target mTarget;
    private int mXPosition;
    private int mYPosition;
    private int mRadius = ShowcaseConfig.DEFAULT_RADIUS;
    private boolean mUseAutoRadius = true;
    private double mAutoRadiusScaleFactor = ShowcaseConfig.DEFAULT_AUTO_RADIUS_SCALE_FACTOR;
    private View mContentBox;
    private View mButtonBox;
    private TextView mContentTextView;
    private TextView mNextButton;
    private TextView mSkipButton;
    private int mGravity;
    private int mContentBottomMargin;
    private int mContentTopMargin;
    private boolean mDismissOnTouch = false;
    private boolean mShouldRedraw = true;
    private boolean mShouldRender = false; // flag to decide when we should actually render
    private int mMaskColour;
    private AnimationFactory mAnimationFactory;
    private boolean mShouldAnimate = true;
    private long mFadeDurationInMillis = ShowcaseConfig.DEFAULT_FADE_TIME;
    private Handler mHandler;
    private long mDelayInMillis = ShowcaseConfig.DEFAULT_DELAY;
    private int mBottomMargin = 0;
    private boolean mSingleUse = false; // should display only once
    private PrefsManager mPrefsManager; // used to store state doe single use mode
    private UpdateOnGlobalLayout mLayoutListener;

    private Bitmap mArraw;
    private Matrix mArrawMatrix = new Matrix();
    private ViewLocation mViewLocation = ViewLocation.OTHER; // targetView location

    private boolean mHasSubSequence = false;

    private IShowcaseListener mOrientationListener;
    private boolean mOrientationFixed = false; // in manifest activity define orientation
    private DismissedType mDismissedType = DismissedType.OTHER;

    private TutorialViewType tutorialViewType;

    public MaterialShowcaseView(Context context) {
        super(context);
        init(context);
    }

    public MaterialShowcaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaterialShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MaterialShowcaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Static helper method for resetting single use flag
     *
     * @param context
     * @param showcaseID
     */
    public static void resetSingleUse(Context context, String showcaseID) {
        PrefsManager.resetShowcase(context, showcaseID);
    }

    /**
     * Static helper method for resetting all single use flags
     *
     * @param context
     */
    public static void resetAll(Context context) {
        PrefsManager.resetAll(context);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftButtonsBarSizePort(Activity activity) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private void init(Context context) {
        setWillNotDraw(false);

        // create our animation factory
        mAnimationFactory = new AnimationFactory();

        mListeners = new ArrayList<>();

        // make sure we add a global layout listener so we can adapt to changes
        mLayoutListener = new UpdateOnGlobalLayout();
        getViewTreeObserver().addOnGlobalLayoutListener(mLayoutListener);

        // consume touch events
        setOnTouchListener(this);

        mMaskColour = Color.parseColor(ShowcaseConfig.DEFAULT_MASK_COLOUR);
        setVisibility(INVISIBLE);

        if (context instanceof Activity && !mOrientationFixed) {
            addLockOrientationListener((Activity) context);
        }

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.default_showcase_content, this, true);
        mContentBox = contentView.findViewById(R.id.content_box);
        mButtonBox = contentView.findViewById(R.id.button_box);
        mContentTextView = (TextView) contentView.findViewById(R.id.tv_content);
        mNextButton = (TextView) contentView.findViewById(R.id.tv_next_or_finish);
        mNextButton.setOnClickListener(this);
        mSkipButton = (TextView) contentView.findViewById(R.id.tv_skip);
        mSkipButton.setOnClickListener(this);

        mContentTextView.getPaint().setFakeBoldText(true);
        mNextButton.getPaint().setFakeBoldText(true);
        mSkipButton.getPaint().setFakeBoldText(true);
    }

    /**
     * Interesting drawing stuff.
     * We draw a block of semi transparent colour to fill the whole screen then we draw of transparency
     * to create a circular "viewport" through to the underlying content
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // don't bother drawing if we're not ready
        if (!mShouldRender) return;

        // get current dimensions
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        // build a new canvas if needed i.e first pass or new dimensions
        if (mBitmap == null || mCanvas == null || mOldHeight != height || mOldWidth != width) {

            if (mBitmap != null) mBitmap.recycle();

            mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        // save our 'old' dimensions
        mOldWidth = width;
        mOldHeight = height;

        // clear canvas
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        // draw solid background
        mCanvas.drawColor(mMaskColour);

        // Erase a circle
        if (mEraser == null) {
            mEraser = new Paint();
            mEraser.setColor(0xFFFFFFFF);
            mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mEraser.setFlags(Paint.ANTI_ALIAS_FLAG);
        }

        if (boxPaint == null) {
            boxPaint = new Paint();
            boxPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            boxPaint.setStrokeWidth(5f);
            boxPaint.setColor(Color.TRANSPARENT);
            boxPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            boxPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        }

        /*Paint paint = new Paint();
        paint.setColor(Color.RED);*/

        //int[] locationOnScreen = new int[2];
        //mTarget.getmView().getLocationOnScreen(locationOnScreen);

        //mCanvas.drawRect(locationOnScreen[0], locationOnScreen[1], locationOnScreen[0] + mTarget.getmView().getWidth(), locationOnScreen[1] + mTarget.getmView().getHeight(), boxPaint);
        mCanvas.drawCircle(mXPosition, mYPosition, mRadius, mEraser);

        mArrawMatrix.reset();

        if (tutorialViewType == TutorialViewType.NewTab) {
            mArrawMatrix.postTranslate(mXPosition + mRadius, (mYPosition + mRadius / 3.0f));
        } else if (tutorialViewType == TutorialViewType.GoddessTab) {
            mArrawMatrix.postTranslate(mXPosition - mRadius - mArraw.getWidth(), (mYPosition + mRadius / 3.0f));
        } else if (tutorialViewType == TutorialViewType.HottestFirstIndex) {
            mArrawMatrix.postTranslate(mXPosition - mRadius, (mYPosition + mRadius / 3.0f) + mArraw.getHeight());
        } else if (tutorialViewType == TutorialViewType.HottestFourthIndex) {
            mArrawMatrix.postTranslate(mXPosition - mRadius - mArraw.getWidth(), (mYPosition - mRadius * 4 / 3) - dp2px(getContext(), 5));
        } else if (tutorialViewType == TutorialViewType.BottomTabThree)
            mArrawMatrix.postTranslate(mXPosition - mRadius - mArraw.getWidth(), (mYPosition - mRadius * 4 / 3) - dp2px(getContext(), 35));
        else if (tutorialViewType != null && tutorialViewType == TutorialViewType.OldChat) {
            mArrawMatrix.postTranslate(mXPosition - mRadius - dp2px(getContext(), 10), (mYPosition - mRadius * 4 / 3) - dp2px(getContext(), 10));
        } else {
            if (mViewLocation == ViewLocation.LT) {
                mArrawMatrix.postTranslate(mXPosition + mRadius, mYPosition + mRadius / 3.0f);
            } else if (mViewLocation == ViewLocation.LB) {
                mArrawMatrix.postTranslate(mXPosition + mRadius, (mYPosition - mRadius * 4 / 3.0f) - dp2px(getContext(), 35));
            } else if (mViewLocation == ViewLocation.RT) {
                mArrawMatrix.postTranslate(mXPosition - mRadius - mArraw.getWidth(), (mYPosition + mRadius / 3.0f));
            } else if (mViewLocation == ViewLocation.RB) {
                mArrawMatrix.postTranslate(mXPosition - mRadius - mArraw.getWidth(), (mYPosition - mRadius * 4 / 3) - dp2px(getContext(), 35));
            }
        }

        mCanvas.drawBitmap(mArraw, mArrawMatrix, null);

        // Draw the bitmap on our views  canvas.
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mDismissOnTouch) {
            mDismissedType = DismissedType.NOF_CLICKED;
            hide();
        }
        float xDelta = Math.abs(event.getRawX() - mXPosition);
        float yDelta = Math.abs(event.getRawY() - mYPosition);
        double distanceFromFocus = Math.sqrt(Math.pow(xDelta, 2)
                + Math.pow(yDelta, 2));
        if (distanceFromFocus < mRadius) {
            mDismissedType = DismissedType.NOF_CLICKED;
            hide();
        }
        return true;
    }

    private void notifyOnDisplayed() {
        for (IShowcaseListener listener : mListeners) {
            listener.onShowcaseDisplayed(this);
        }
    }

    private void notifyOnDismissed() {
        if (mListeners != null) {
            for (IShowcaseListener listener : mListeners) {
                listener.onShowcaseDismissed(this, mDismissedType);
            }

            mListeners.clear();
            mListeners = null;
        }
    }

    /**
     * Dismiss button clicked
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_skip) {
            mDismissedType = DismissedType.SKIP_CLICKED;
            if (mListeners != null) {
                for (IShowcaseListener listener : mListeners) {
                    if (listener instanceof IShowcaseSequenceListener) {
                        ((IShowcaseSequenceListener) listener).onShowcaseSkipClicked(this);
                    }
                }
            }
        } else if (v.getId() == R.id.tv_next_or_finish) {
            mDismissedType = DismissedType.NOF_CLICKED;
        }
        hide();
    }

    /**
     * Tells us about the "Target" which is the view we want to anchor to.
     * We figure out where it is on screen and (optionally) how big it is.
     * We also figure out whether to place our content and dismiss button above or below it.
     *
     * @param target
     */
    public void setTarget(Target target) {
        mTarget = target;

        if (mTarget != null) {
            // apply the target position
            Point targetPoint = mTarget.getPoint();
            setPosition(targetPoint);

            // apply auto radius
            if (mUseAutoRadius) {
                setRadius((int) (mTarget.getRadius() * mAutoRadiusScaleFactor));
                setPosition(mTarget.getFinalPoint(mRadius));
            }

            mArrawMatrix.reset();
            // now figure out whether to put content above or below it
            int height = getMeasuredHeight();
            int midHeight = height / 2;
            int yPos = mYPosition;

            int width = getMeasuredWidth();
            int midWidth = width / 2;
            int xPos = mXPosition;

            if (height != 0 && width != 0) {
                if (yPos > midHeight) {
                    // target is in lower half of screen, we'll sit above it
                    mContentTopMargin = 0;
                    mContentBottomMargin = (height - yPos) + mRadius;
                    mGravity = Gravity.BOTTOM;

                    if (tutorialViewType != null && tutorialViewType == TutorialViewType.BottomTabThree) {
                        mArrawMatrix.postRotate(270);
                    } else if (tutorialViewType != null && tutorialViewType == TutorialViewType.OldChat) {
                        mArrawMatrix.postRotate(-115);
                    } else {
                        if (xPos > midWidth) {
                            mViewLocation = ViewLocation.RB;
                            mArrawMatrix.postRotate(-115);
                        } else {
                            mViewLocation = ViewLocation.LB;
                            mArrawMatrix.postRotate(-15);
                        }
                    }
                } else {
                    // target is in upper half of screen, we'll sit below it
                    mContentTopMargin = yPos + mRadius;
                    mContentBottomMargin = 0;
                    mGravity = Gravity.TOP;

                    if (tutorialViewType != null && tutorialViewType == TutorialViewType.NewTab) {
                        mArrawMatrix.postRotate(55);
                    } else if (tutorialViewType == TutorialViewType.GoddessTab) {
                        mArrawMatrix.postRotate(255);
                        mArrawMatrix.postScale(1, -1);
                    } else if (tutorialViewType == TutorialViewType.HottestFirstIndex) {
                        mArrawMatrix.postRotate(255);
                        mArrawMatrix.postScale(1, -1);
                    } else {
                        if (xPos > midWidth) {
                            mViewLocation = ViewLocation.RT;
                            mArrawMatrix.postRotate(255);
                            mArrawMatrix.postScale(1, -1);
                        } else {
                            mViewLocation = ViewLocation.LT;
                            mArrawMatrix.postRotate(55);
                        }
                    }
                }

                if (mArraw == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.arraw);
                    mArrawMatrix.postScale(0.3f, 0.3f);
                    mArraw = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mArrawMatrix,
                            false);
                }
            }
        }

        applyLayoutParams();
    }

    private void applyLayoutParams() {
        int msWidth = getMeasuredWidth();
        int contentMargin = (int) getResources().getDimension(R.dimen.tv_content_margin);
        int contentBoxMarginBottom = (int) getResources().getDimension(R.dimen.content_box_margin_bottom);
        int buttonBoxLeftMargin = 0;
        boolean needUpForContentBox = false;
        if (mButtonBox != null && mArraw != null && mButtonBox.getLayoutParams() != null) {
            RelativeLayout.LayoutParams buttonBoxLP = (android.widget.RelativeLayout.LayoutParams) mButtonBox
                    .getLayoutParams();
            if (mViewLocation == ViewLocation.LB || mViewLocation == ViewLocation.LT) {
                int tempButtonBoxLeftMargin = mXPosition + mRadius + mArraw.getWidth() + ShowcaseConfig.ARROW_BUTTON_MARGIN;
                if (msWidth - tempButtonBoxLeftMargin < (mButtonBox.getWidth() + ShowcaseConfig.ARROW_BUTTON_MARGIN + contentMargin)) {
                    buttonBoxLeftMargin = msWidth - (mButtonBox.getWidth() + ShowcaseConfig.ARROW_BUTTON_MARGIN);
                    needUpForContentBox = true;
                } else {
                    buttonBoxLeftMargin = tempButtonBoxLeftMargin;
                }
            } else if (mViewLocation == ViewLocation.RT || mViewLocation == ViewLocation.RB) {
                int tempButtonBoxLeftMargin = mXPosition - mRadius - mArraw.getWidth() - mButtonBox.getWidth() -
                        ShowcaseConfig.ARROW_BUTTON_MARGIN;
                if (tempButtonBoxLeftMargin < (ShowcaseConfig.ARROW_BUTTON_MARGIN + contentMargin)) {
                    buttonBoxLeftMargin = contentMargin;
                    needUpForContentBox = true;
                } else {
                    buttonBoxLeftMargin = tempButtonBoxLeftMargin;
                }
            }
            buttonBoxLP.leftMargin = buttonBoxLeftMargin;

            if (tutorialViewType == TutorialViewType.ButtonSearch)
                buttonBoxLP.leftMargin = mXPosition + mRadius + mContentTextView.getWidth() / 2;
            else if (tutorialViewType == TutorialViewType.FeaturedTab) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + mContentTextView.getWidth() / 2;
            } else if (tutorialViewType == TutorialViewType.HottestTab) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + mContentTextView.getWidth() / 2 - mButtonBox.getWidth() / 2;
            } else if (tutorialViewType == TutorialViewType.NewTab) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + (mContentTextView.getWidth() / 2) - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.GoddessTab) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth();
            } else if (tutorialViewType == TutorialViewType.MachoTab) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - (mContentTextView.getWidth()) - (mSkipButton.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.HottestFirstIndex) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - (mContentTextView.getWidth() / 2) + (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.HottestFourthIndex) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mContentTextView.getWidth() + (mContentTextView.getWidth() / 2) - (mNextButton.getWidth() / 2);
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getWidth() / 2;
            } else if (tutorialViewType == TutorialViewType.ButtonMore) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - (mContentTextView.getWidth()) - (mSkipButton.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.BottomTabOne) {
                buttonBoxLP.leftMargin = mXPosition + mRadius - (mSkipButton.getWidth() / 2);
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mRadius;
            } else if (tutorialViewType == TutorialViewType.BottomTabTwo) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + (mContentTextView.getWidth() / 2) - (mButtonBox.getWidth() / 2);
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mRadius;
            } else if (tutorialViewType == TutorialViewType.BottomTabThree) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getHeight();
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mRadius / 2;
            } else if (tutorialViewType == TutorialViewType.BottomTabFour) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mContentTextView.getWidth() - (mSkipButton.getWidth() / 2);
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mRadius;
            } else if (tutorialViewType == TutorialViewType.BottomTabFive) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mContentTextView.getWidth() + (mContentTextView.getWidth() / 2) - (mNextButton.getWidth() / 2);
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mRadius;
            }

            // Go Live Check
            else if (tutorialViewType == TutorialViewType.UserProfile) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + mArraw.getWidth();
            } else if (tutorialViewType == TutorialViewType.UserCoinsCount) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + mArraw.getWidth();
            } else if (tutorialViewType == TutorialViewType.FollowBroadcaster) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + mArraw.getWidth();
            } else if (tutorialViewType == TutorialViewType.GoLiveList) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth() - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.EventPoster) {
                buttonBoxLP.leftMargin = mXPosition + mRadius + mArraw.getWidth();
            } else if (tutorialViewType == TutorialViewType.DailyMissionEnergy) {
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth() - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.OldChat) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 3;
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth();
            } else if (tutorialViewType == TutorialViewType.GoLiveChatViewerEDT) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 2;
                buttonBoxLP.leftMargin = mXPosition + mRadius + mArraw.getWidth() - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatCamera) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 3;
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth() - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatPost) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 4;
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth() - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatMessage) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 4;
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth() - (mButtonBox.getWidth() / 2);
            } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatFolder) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 3;
                buttonBoxLP.leftMargin = mXPosition - mRadius - mArraw.getWidth() - (mButtonBox.getWidth());
            } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatGift) {
                buttonBoxLP.removeRule(RelativeLayout.BELOW);
                buttonBoxLP.bottomMargin = mArraw.getHeight() / 3;
                buttonBoxLP.leftMargin = (int) (mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 1.3) + (mContentTextView.getWidth() / 2) - (mNextButton.getWidth() / 2));
            }

            mButtonBox.setLayoutParams(buttonBoxLP);
        }

        if (mContentBox != null && mContentBox.getLayoutParams() != null && mArraw != null) {
            FrameLayout.LayoutParams contentLP = (LayoutParams) mContentBox.getLayoutParams();
            contentLP.bottomMargin = mContentBottomMargin;
            contentLP.topMargin = mContentTopMargin;
            contentLP.gravity = mGravity;
            if (mViewLocation == ViewLocation.LB || mViewLocation == ViewLocation.RB) {
                contentLP.bottomMargin -= (mButtonBox.getHeight() / 5);//- dp2px(getContext(), 50);
                if (needUpForContentBox) {
                    contentLP.bottomMargin += contentBoxMarginBottom;
                }
            } else if (mViewLocation == ViewLocation.LT || mViewLocation == ViewLocation.RT) {
                if (mArraw.getHeight() > mRadius * 2 / 3) {
                    contentLP.topMargin = mContentTopMargin - mRadius * 2 / 3 + mArraw.getHeight();
                }
            }
            mContentBox.setLayoutParams(contentLP);
        }

        if (mContentTextView != null) {
            int width = mContentTextView.getWidth();
            TextPaint paint = mContentTextView.getPaint();
            int textOneLineWidth = (int) paint.measureText(mContentTextView.getText().toString());

            if (width != 0) {
                /*if (mViewLocation == ViewLocation.LB || mViewLocation == ViewLocation.LT) {
                    int leaveWidth = msWidth - buttonBoxLeftMargin;
                    if (textOneLineWidth < leaveWidth) {
                        *//*RelativeLayout.LayoutParams contentTextViewLP = (android.widget.RelativeLayout.LayoutParams) mContentTextView.getLayoutParams();
                        contentTextViewLP.setMargins(buttonBoxLeftMargin - contentMargin, 0, 0, 0);

                        if (tutorialViewType == TutorialViewType.ButtonSearch)
                            contentTextViewLP.setMargins(0, 0, 0, 0);

                        mContentTextView.setLayoutParams(contentTextViewLP);*//*
                        // Leave space enough.
                    } else if (textOneLineWidth >= leaveWidth && textOneLineWidth < (msWidth - 2 * contentMargin)) {
                        // Less than one line, more than leave space.
                        // Disable gravity
                        //mContentTextView.setGravity(Gravity.RIGHT);
                    }

                } else if (mViewLocation == ViewLocation.RB || mViewLocation == ViewLocation.RT) {
                    int leaveWidth = buttonBoxLeftMargin + mButtonBox.getWidth();
                    if (textOneLineWidth < leaveWidth) {
                        //mContentTextView.setGravity(Gravity.RIGHT);
                        RelativeLayout.LayoutParams contentTextViewLP = (android.widget.RelativeLayout.LayoutParams) mContentTextView.getLayoutParams();
                        contentTextViewLP.setMargins(0, 0, msWidth - leaveWidth - contentMargin, 0);
                        mContentTextView.setLayoutParams(contentTextViewLP);
                    }
                }*/

                RelativeLayout.LayoutParams contentTextViewLP = (android.widget.RelativeLayout.LayoutParams) mContentTextView.getLayoutParams();

                if (tutorialViewType == TutorialViewType.ButtonSearch)
                    contentTextViewLP.setMargins(mXPosition + mRadius, 0, 0, 0);
                else if (tutorialViewType == TutorialViewType.FeaturedTab) {
                    contentTextViewLP.setMargins(mXPosition + mRadius, 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.HottestTab) {
                    contentTextViewLP.setMargins(mXPosition + mRadius, 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.NewTab) {
                    contentTextViewLP.setMargins(mXPosition + mRadius, mArraw.getWidth(), 0, 0);
                } else if (tutorialViewType == TutorialViewType.GoddessTab) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 2), mArraw.getHeight() / 2, 0, 0);
                } else if (tutorialViewType == TutorialViewType.MachoTab) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - (mContentTextView.getWidth()), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.HottestFirstIndex) {
                    contentTextViewLP.setMargins(mXPosition - mContentTextView.getWidth(), mArraw.getHeight(), 0, 0);
                } else if (tutorialViewType == TutorialViewType.HottestFourthIndex) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - (mContentTextView.getWidth()), 0, 0, mArraw.getWidth()/2);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.ButtonMore) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - (mContentTextView.getWidth()), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.BottomTabOne) {
                    contentTextViewLP.setMargins(mXPosition + mRadius, 0, 0, mRadius * 3);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.BottomTabTwo) {
                    contentTextViewLP.setMargins(mXPosition + mRadius, 0, 0, mRadius);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.BottomTabThree) {
                    contentTextViewLP.setMargins(mXPosition - (mContentTextView.getWidth() / 2) - mArraw.getHeight(), 0, 0, (int) (mRadius * 1.5));
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.BottomTabFour) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - (mContentTextView.getWidth()), 0, 0, mRadius);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.BottomTabFive) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - (mContentTextView.getWidth()), 0, 0, (int) (mRadius * 2.5));
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                }

                // Go Live Check
                else if (tutorialViewType == TutorialViewType.UserProfile) {
                    contentTextViewLP.setMargins(mXPosition + mRadius + mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.UserCoinsCount) {
                    contentTextViewLP.setMargins(mXPosition + mRadius + mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.FollowBroadcaster) {
                    contentTextViewLP.setMargins(mXPosition + mRadius + mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.GoLiveList) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.EventPoster) {
                    contentTextViewLP.setMargins(mXPosition + mRadius + mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.DailyMissionEnergy) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, 0);
                } else if (tutorialViewType == TutorialViewType.OldChat) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 5), 0, 0, (int) (mArraw.getHeight() / 1.5));
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.GoLiveChatViewerEDT) {
                    contentTextViewLP.setMargins(mXPosition + mRadius + mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, mArraw.getHeight());
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatCamera) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, mArraw.getHeight() / 2);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatPost) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, mArraw.getHeight() / 2);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatMessage) {
                    contentTextViewLP.setMargins(mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 2), 0, 0, mArraw.getHeight() / 2);
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatFolder) {
                    contentTextViewLP.setMargins((int) (mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 1.5)), 0, 0, mArraw.getHeight());
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                } else if (tutorialViewType == TutorialViewType.GoLiveViewerChatGift) {
                    contentTextViewLP.setMargins((int) (mXPosition - mRadius - mArraw.getWidth() - (mContentTextView.getWidth() / 1.3)), 0, 0, mArraw.getHeight());
                    contentTextViewLP.addRule(RelativeLayout.BELOW, mButtonBox.getId());
                }

                mContentTextView.setLayoutParams(contentTextViewLP);

            }
        }
    }

    @Override
    public void invalidate() {
        mShouldRedraw = true;
        super.invalidate();
    }

    /**
     * SETTERS
     */

    void setPosition(Point point) {
        setPosition(point.x, point.y);
    }

    void setPosition(int x, int y) {
        mXPosition = x;
        mYPosition = y;
    }

    private void setContentText(CharSequence contentText) {
        if (mContentTextView != null) {
            mContentTextView.setText(contentText);
        }
    }

    String getDismissText() {
        if (mNextButton != null) {
            return mNextButton.getText().toString();
        } else {
            return null;
        }
    }

    void setDismissText(CharSequence dismissText) {
        if (mNextButton != null) {
            mNextButton.setText(dismissText);
        }
    }

    private void setContentTextColor(int textColour) {
        if (mContentTextView != null) {
            mContentTextView.setTextColor(textColour);
        }
    }

    private void setDismissTextColor(int textColour) {
        if (mNextButton != null) {
            mNextButton.setTextColor(textColour);
        }
    }

    private void setDismissOnTouch(boolean dismissOnTouch) {
        mDismissOnTouch = dismissOnTouch;
    }

    private void setUseAutoRadius(boolean useAutoRadius) {
        mUseAutoRadius = useAutoRadius;
    }

    private void setRadius(int radius) {
        mRadius = radius;
    }

    public void setAutoRadiusScaleFactor(double radiusScaleFactor) {
        mAutoRadiusScaleFactor = radiusScaleFactor;
        setUseAutoRadius(true);
    }

    private void setShouldRender(boolean shouldRender) {
        mShouldRender = shouldRender;
    }

    private void setMaskColour(int maskColour) {
        mMaskColour = maskColour;
    }

    private void setDelay(long delayInMillis) {
        if (mDelayInMillis == 0) {
            mDelayInMillis = delayInMillis;
        }
    }

    private void setFadeDuration(long fadeDurationInMillis) {
        mFadeDurationInMillis = fadeDurationInMillis;
    }

    private void setTypefaceToTextView(Typeface tf) {
        if (tf != null) {
            mContentTextView.setTypeface(tf);
            mNextButton.setTypeface(tf);
            mSkipButton.setTypeface(tf);
        }
    }

    void setSkipGuideVisibility(int visibility) {
        mSkipButton.setVisibility(visibility);
    }

    public void setHasSubSequence(boolean hasSubSequence) {
        mHasSubSequence = hasSubSequence;
    }

    public boolean needConnectToSubSequence() {
        return mHasSubSequence;
    }

    public void setOrientationFixed(boolean orientationFixed) {
        mOrientationFixed = orientationFixed;
    }

    public void setContentTextSize(float size) {
        if (size > 1) {
            mContentTextView.setTextSize(size);
        }
    }

    public void setButtonTextSize(float size) {
        if (size > 1) {
            mNextButton.setTextSize(size);
            mSkipButton.setTextSize(size);
        }
    }

    public void addShowcaseListener(IShowcaseListener showcaseListener) {
        mListeners.add(showcaseListener);
    }

    public void removeShowcaseListener(MaterialShowcaseSequence showcaseListener) {
        if (mListeners.contains(showcaseListener)) {
            mListeners.remove(showcaseListener);
        }
    }

    /**
     * Set properties based on a config object
     *
     * @param config
     */
    public void setConfig(ShowcaseConfig config) {
        setDelay(config.getDelay());
        setFadeDuration(config.getFadeDuration());
        setContentTextColor(config.getContentTextColor());
        setDismissTextColor(config.getDismissTextColor());
        setMaskColour(config.getMaskColor());
        setTypefaceToTextView(config.getTypeface());
        setContentTextSize(config.getContentTextSize());
        setButtonTextSize(config.getButtonTextSize());
    }

    private void singleUse(String showcaseID) {
        mSingleUse = true;
        mPrefsManager = new PrefsManager(getContext(), showcaseID);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void removeFromWindow() {
        if (getParent() != null && getParent() instanceof ViewGroup) {
            ((ViewGroup) getParent()).removeView(this);
        }

        notifyOnDismissed();

        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (mArraw != null) {
            mArraw.recycle();
            mArraw = null;
        }

        mEraser = null;
        mAnimationFactory = null;
        mCanvas = null;
        mHandler = null;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            getViewTreeObserver().removeGlobalOnLayoutListener(mLayoutListener);
        } else {
            getViewTreeObserver().removeOnGlobalLayoutListener(mLayoutListener);
        }
        mLayoutListener = null;

        if (mPrefsManager != null)
            mPrefsManager.close();

        mPrefsManager = null;


    }

    /**
     * Reveal the showcaseview. Returns a boolean telling us whether we actually did show anything
     *
     * @param activity
     * @return
     */
    public boolean show(final Activity activity) {

        /**
         * if we're in single use mode and have already shot our bolt then do nothing
         */
        if (mSingleUse) {
            if (mPrefsManager.hasFired()) {
                return false;
            } else {
                mPrefsManager.setFired();
            }
        }

        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);

        setShouldRender(true);


        /**
         * If we're on lollipop then make sure we don't draw over the nav bar
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomMargin = getSoftButtonsBarSizePort(activity);
            FrameLayout.LayoutParams contentLP = (LayoutParams) getLayoutParams();
            contentLP.bottomMargin = mBottomMargin;
        }

        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mShouldAnimate) {
                    fadeIn();
                } else {
                    setVisibility(VISIBLE);
                    notifyOnDisplayed();
                }
            }
        }, mDelayInMillis);

        return true;
    }

    private void addLockOrientationListener(final Activity activity) {
        if (mOrientationListener == null) {
            mOrientationListener = new IShowcaseListener() {

                @Override
                public void onShowcaseDisplayed(MaterialShowcaseView showcaseView) {
                    if (!activity.isFinishing()) {
                        int beforeOrientation = activity.getResources().getConfiguration().orientation;
                        if (beforeOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        } else if (beforeOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                        } else {
                            //noinspection ResourceType
                            activity.setRequestedOrientation(beforeOrientation);
                        }
                    }
                }

                @Override
                public void onShowcaseDismissed(MaterialShowcaseView showcaseView, DismissedType dismissedType) {
                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
                }
            };
        }
        addShowcaseListener(mOrientationListener);
    }

    void removeLockOrientationListener() {
        if (mListeners.contains(mOrientationListener)) {
            mListeners.remove(mOrientationListener);
        }
    }

    public void hide() {

        if (mShouldAnimate) {
            fadeOut();
        } else {
            removeFromWindow();
        }
    }

    public void fadeIn() {
        setVisibility(INVISIBLE);

        mAnimationFactory.fadeInView(this, mFadeDurationInMillis,
                new IAnimationFactory.AnimationStartListener() {
                    @Override
                    public void onAnimationStart() {
                        setVisibility(View.VISIBLE);
                        notifyOnDisplayed();
                    }
                }
        );
    }

    public void fadeOut() {

        mAnimationFactory.fadeOutView(this, mFadeDurationInMillis, new IAnimationFactory.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(INVISIBLE);
                removeFromWindow();
            }
        });
    }

    public void resetSingleUse() {
        if (mSingleUse && mPrefsManager != null) mPrefsManager.resetShowcase();
    }

    public boolean handleBackPressed() {
        return true;
    }

    // L-Left, T-Top, R-Right, B-Bottom
    public enum ViewLocation {
        LT, RT, RB, LB, OTHER
    }

    // SKIP - Skip, NOF - NextOrFinished
    public enum DismissedType {
        SKIP_CLICKED, NOF_CLICKED, OTHER
    }

    /**
     * BUILDER CLASS
     * Gives us a builder utility class with a fluent API for eaily configuring showcase views
     */
    public static class Builder {
        final MaterialShowcaseView showcaseView;

        private final Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;

            showcaseView = new MaterialShowcaseView(activity);
        }

        /**
         * Set the title text shown on the ShowcaseView.
         */
        public Builder setTarget(View target) {
            showcaseView.setTarget(new ViewTarget(target));
            return this;
        }

        /**
         * Set the title text shown on the ShowcaseView.
         */
        public Builder setTarget(View target, ViewTarget.CentreArea centreArea) {
            showcaseView.setTarget(new ViewTarget(target, centreArea));
            return this;
        }

        /**
         * Set the title text shown on the ShowcaseView.
         */
        public Builder setDismissText(int resId) {
            return setDismissText(activity.getString(resId));
        }

        public Builder setDismissText(CharSequence dismissText) {
            showcaseView.setDismissText(dismissText);
            return this;
        }

        /**
         * Set the title text shown on the ShowcaseView.
         */
        public Builder setContentText(int resId) {
            return setContentText(activity.getString(resId));
        }

        /**
         * Set the descriptive text shown on the ShowcaseView.
         */
        public Builder setContentText(CharSequence text) {
            showcaseView.setContentText(text);
            return this;
        }

        public Builder setSkipGuideVisibility(int visibility) {
            showcaseView.setSkipGuideVisibility(visibility);
            return this;
        }

        public Builder setContentTextSize(float size) {
            showcaseView.setContentTextSize(size);
            return this;
        }

        public Builder setButtonTextSize(float size) {
            showcaseView.setButtonTextSize(size);
            return this;
        }

        /**
         * Use auto radius, if true then the showcase circle will auto size based on the target view
         * Defaults to true
         */
        public Builder setUseAutoRadius(boolean useAutoRadius) {
            showcaseView.setUseAutoRadius(useAutoRadius);
            return this;
        }

        /**
         * Manually define a radius in pixels - should set setUseAutoRadius to false
         * Defaults to 200 pixels
         */
        public Builder setRadius(int radius) {
            showcaseView.setRadius(radius);
            return this;
        }

        public Builder setAutoRadiusScaleFactor(double radiusScaleFactor) {
            showcaseView.setAutoRadiusScaleFactor(radiusScaleFactor);
            return this;
        }

        public Builder setDismissOnTouch(boolean dismissOnTouch) {
            showcaseView.setDismissOnTouch(dismissOnTouch);
            return this;
        }

        public Builder setMaskColour(int maskColour) {
            showcaseView.setMaskColour(maskColour);
            return this;
        }

        public Builder setContentTextColor(int textColour) {
            showcaseView.setContentTextColor(textColour);
            return this;
        }

        public Builder setDismissTextColor(int textColour) {
            showcaseView.setDismissTextColor(textColour);
            return this;
        }

        public Builder setDelay(int delayInMillis) {
            showcaseView.setDelay(delayInMillis);
            return this;
        }

        public Builder setFadeDuration(int fadeDurationInMillis) {
            showcaseView.setFadeDuration(fadeDurationInMillis);
            return this;
        }

        public Builder setHasSubSequence(boolean hasSubSequence) {
            showcaseView.setHasSubSequence(hasSubSequence);
            return this;
        }

        public Builder setOrientationFixed(boolean orientationFixed) {
            showcaseView.setOrientationFixed(orientationFixed);
            return this;
        }

        public Builder setListener(IShowcaseListener listener) {
            showcaseView.addShowcaseListener(listener);
            return this;
        }

        public Builder setTypefaceToTextView(Typeface tf) {
            showcaseView.setTypefaceToTextView(tf);
            return this;
        }

        public Builder singleUse(String showcaseID) {
            showcaseView.singleUse(showcaseID);
            return this;
        }

        public MaterialShowcaseView build() {
            return showcaseView;
        }

        public MaterialShowcaseView show() {
            showcaseView.show(activity);
            return showcaseView;
        }

        public Builder setTutorialViewType(TutorialViewType tutorialViewType) {
            showcaseView.tutorialViewType = tutorialViewType;
            return this;
        }
    }

    /**
     * REDRAW LISTENER - this ensures we redraw after activity finishes laying out
     */
    private class UpdateOnGlobalLayout implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            if (mShouldRedraw) {
                mShouldRedraw = false;
                setTarget(mTarget);
            }
        }
    }

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
