package app.witkey.live.utils.animations;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import app.witkey.live.R;
import app.witkey.live.utils.LogUtils;

/**
 * created by developer on 10/02/2017.
 */

public class Animations {

    static Animations animations;
    static AnimationDrawable frameAnimation;
    static boolean mHeaderIsShow;
    static boolean floatingStatus = true;

    public Animations() {

    }

    public static Animations getAnimations() {

        if (animations == null)
            animations = new Animations();

        return animations;
    }

    public static AlphaAnimation getBlinkAnimationInstance() {
        AlphaAnimation blinkAnimation = new AlphaAnimation(1, 0);
        blinkAnimation.setDuration(500);
        blinkAnimation.setInterpolator(new LinearInterpolator());
        blinkAnimation.setRepeatCount(Animation.INFINITE);
        blinkAnimation.setRepeatMode(Animation.REVERSE);
        return blinkAnimation;
    }

    public static TranslateAnimation getRightToLeftAnimation() {
        TranslateAnimation animation = new TranslateAnimation(250.0f, 0.0f, 0.0f, 0.0f); // new TranslateAnimation (float fromXDelta,float toXDelta, float fromYDelta, float toYDelta)
        animation.setDuration(350); // animation duration
        animation.setRepeatCount(0); // animation repeat count
        animation.setFillAfter(false);
        return animation;
    }

    public void setCircularRotationonView(final Activity mContext, View view, float rotate_from, float rotate_to, final Class activitytoStart) {

        RotateAnimation r = new RotateAnimation(rotate_from, rotate_to, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        r.setDuration((800));
        r.setStartOffset(200);
        r.setRepeatCount(2);
        view.startAnimation(r);

        r.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContext.startActivity(new Intent(mContext, activitytoStart));
                mContext.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    boolean isIncludedLayoutVisible = false;

    public void performScaleAnimation(Activity mActivity, final View view, float mPointX, float mPointY) {
        final View dummyFrame = mActivity.findViewById(R.id.optionView);

        if (dummyFrame.getVisibility() == View.GONE) {
            dummyFrame.setVisibility(View.VISIBLE);
        }
        Animation anim = null;

        if (isIncludedLayoutVisible) {
            //close popup animation....
            anim = new ScaleAnimation(1f, 0f, 1f, 0f,
                    Animation.RELATIVE_TO_SELF, mPointX,
                    Animation.RELATIVE_TO_SELF, mPointY);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    if (dummyFrame.getVisibility() == View.VISIBLE) {
                        dummyFrame.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            isIncludedLayoutVisible = false;
        } else {

            //Open popup animation....
            isIncludedLayoutVisible = true;

            anim = new ScaleAnimation(0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, mPointX,
                    Animation.RELATIVE_TO_SELF, mPointY);
            view.setVisibility(View.VISIBLE);

        }

        anim.setFillAfter(false);
        anim.setDuration(300);
        view.startAnimation(anim);
    }

    public void performScaleAnimation(final View view, float mPointX, float mPointY) {
        Animation anim = null;
        if (isIncludedLayoutVisible) {
            //hide the popup...
            anim = new ScaleAnimation(1f, 0f, 1f, 0f,
                    Animation.RELATIVE_TO_SELF, mPointX,
                    Animation.RELATIVE_TO_SELF, mPointY);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(View.GONE);
                    isIncludedLayoutVisible = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        } else {
            //show the popup...
            isIncludedLayoutVisible = true;

            anim = new ScaleAnimation(0f, 1.0f, 0.0f, 1.0f,
                    Animation.RELATIVE_TO_SELF, mPointX,
                    Animation.RELATIVE_TO_SELF, mPointY);
            view.setVisibility(View.VISIBLE);

        }

        anim.setFillAfter(false);
        anim.setDuration(300);
        view.startAnimation(anim);
    }


    Context context;
    Animation animationslidetop, animationslidebottom;

    public Animations(Context context) {
        this.context = context;
        animationslidetop = AnimationUtils.loadAnimation(this.context, R.anim.slide_up_dialog);
        animationslidebottom = AnimationUtils.loadAnimation(this.context, R.anim.slide_down_dialog);
    }

    public void animateBottomFrame(View bottomFrame) {
        if (bottomFrame.getVisibility() == View.VISIBLE) {
            bottomFrame.setVisibility(View.INVISIBLE);
            bottomFrame.startAnimation(animationslidebottom);
        } else {
            bottomFrame.setVisibility(View.VISIBLE);
            bottomFrame.startAnimation(animationslidetop);

        }
    }

    public void animateBottomFrame(View bottomFrame, int viewAfterAnim) {
        if (bottomFrame.getVisibility() == View.VISIBLE) {
            bottomFrame.setVisibility(View.INVISIBLE);
            bottomFrame.startAnimation(animationslidebottom);
        } else {
            bottomFrame.setVisibility(View.VISIBLE);
            bottomFrame.startAnimation(animationslidetop);

        }
    }

    private float xCurrentPos, yCurrentPos;

    public void mTrailAnimation(ImageView logoFocus) {
        xCurrentPos = logoFocus.getLeft();
        yCurrentPos = logoFocus.getTop();

        Animation anim = new TranslateAnimation(xCurrentPos, xCurrentPos + 150, yCurrentPos, yCurrentPos);
        anim.setDuration(10000);
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation arg0) {
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                xCurrentPos -= 150;
            }
        });
        logoFocus.startAnimation(anim);
    }

    public static void buttonBounceAnimation(Context context, View view) {
        try {
            if (context != null) {
                Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
                myAnim.setDuration(2000);
                // Use bounce interpolator with amplitude 0.2 and frequency 20
                BounceAnimation interpolator = new BounceAnimation(0.1, 50.0);
//        BounceAnimation interpolator = new BounceAnimation(0.9, 100.0);
                myAnim.setInterpolator(interpolator);
                view.startAnimation(myAnim);
            }
        } catch (Exception e) {
            LogUtils.e("buttonBounceAnimation", "" + e.getMessage());
        }
    }

    public static void giftBounceAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.gift_bounce_infinite);
        myAnim.setDuration(5000);
        myAnim.setRepeatCount(Animation.INFINITE);
        // Use bounce interpolator with amplitude 0.2 and frequency 20
        BounceAnimation interpolator = new BounceAnimation(1, 50.0);
//        BounceAnimation interpolator = new BounceAnimation(0.9, 100.0);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                giftBounceAnimation(context, view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void showBottomView(View view) {
        if (!mHeaderIsShow) {
            view.setClickable(true);
            view.setFocusable(true);
            view.setEnabled(true);
            view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(200);
            animate.setFillAfter(true);
            view.startAnimation(animate);
            mHeaderIsShow = true;
        }
    }

    public static void hideBottomView(View view) {
        if (mHeaderIsShow) {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(200);
            animate.setFillAfter(true);
            view.startAnimation(animate);
            mHeaderIsShow = false;
            view.setClickable(false);
            view.setFocusable(false);
            view.setEnabled(false);
            view.setVisibility(View.GONE);
            view.clearAnimation();
        }
    }

    public static void showFloatingView(View view) {
        if (!floatingStatus) {
            view.setClickable(true);
            view.setFocusable(true);
            view.setEnabled(true);
            view.setVisibility(View.VISIBLE);
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    view.getHeight(),  // fromYDelta
                    0);                // toYDelta
            animate.setDuration(200);
            animate.setFillAfter(true);
            view.startAnimation(animate);
            floatingStatus = true;
        }
    }

    public static void hideFloatingView(View view) {
        if (floatingStatus) {
            TranslateAnimation animate = new TranslateAnimation(
                    0,                 // fromXDelta
                    0,                 // toXDelta
                    0,                 // fromYDelta
                    view.getHeight()); // toYDelta
            animate.setDuration(200);
            animate.setFillAfter(true);
            view.startAnimation(animate);
            floatingStatus = false;
            view.setClickable(false);
            view.setFocusable(false);
            view.setEnabled(false);
            view.setVisibility(View.GONE);
        }
    }

    public static void slideInRightViewAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.slide_gift_in_from_right);
        myAnim.setDuration(600);
        myAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setVisibility(View.VISIBLE);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                slideOutLeftViewAnimation(context, view);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void slideUpViewAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.slide_up_dialog);
        myAnim.setDuration(600);
        myAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setVisibility(View.VISIBLE);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingStatus = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void slideDownViewAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.slide_down_dialog);
        myAnim.setDuration(600);
        myAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.setVisibility(View.VISIBLE);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingStatus = false;
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void slideOutLeftViewAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.slide_gift_out_to_left);
        myAnim.setDuration(400);
        myAnim.setStartOffset(2000);
        myAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public static void heartViewAnimation(final Context context, final View view) {
        Animation myAnim = null;
        frameAnimation = null;
        myAnim = AnimationUtils.loadAnimation(context, R.anim.heart_bounce_infinite);
        myAnim.setDuration(2500);
        view.setBackgroundResource(R.drawable.heart_animation_list);
        frameAnimation = (AnimationDrawable) view.getBackground();
        BounceAnimation interpolator = new BounceAnimation(0.5, 25.0);
        myAnim.setInterpolator(interpolator);
        view.setVisibility(View.VISIBLE);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    /*public static void smallToLargeStarViewAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.heart_small_to_large_infinite);
        frameAnimation = null;
        myAnim.setDuration(1000);
        view.setBackgroundResource(R.drawable.star_animation_list);
        frameAnimation = (AnimationDrawable) view.getBackground();
        myAnim.setInterpolator(new LinearInterpolator());
        view.setVisibility(View.VISIBLE);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }*/

    /*public static void smallToLargeGuiterViewAnimation(final Context context, final View view) {
        Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.heart_small_to_large_infinite);
        frameAnimation = null;
        myAnim.setDuration(1000);
        view.setBackgroundResource(R.drawable.star_animation_list);
        frameAnimation = (AnimationDrawable) view.getBackground();
        myAnim.setInterpolator(new LinearInterpolator());
        view.setVisibility(View.VISIBLE);
        view.startAnimation(myAnim);

        myAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameAnimation.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }*/

    public static void showHideBottomFloatingView(Context context, View view, boolean show) {
        if (show && !floatingStatus) {
            slideUpViewAnimation(context, view);
        } else if (floatingStatus) {
            slideDownViewAnimation(context, view);
        }
    }


}