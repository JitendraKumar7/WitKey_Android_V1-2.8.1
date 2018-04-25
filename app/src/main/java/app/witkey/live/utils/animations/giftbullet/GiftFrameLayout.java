package app.witkey.live.utils.animations.giftbullet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import app.witkey.live.R;

import app.witkey.live.utils.Utils;


public class GiftFrameLayout extends FrameLayout {

    private LayoutInflater mInflater;

    RelativeLayout anim_rl;
    ImageView anim_gift, anim_light, anim_header;
    TextView anim_nickname, anim_sign;
    StrokeTextView anim_num;
    Context mContext;
    int starNum = 1;
    int repeatCount = 0;
    private boolean isShowing = false;

    private String nick;

    private ObjectAnimator scaleGiftNum;

    public GiftFrameLayout(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public GiftFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        initView();
    }


    private void initView() {
        View view = mInflater.inflate(R.layout.layout_gift_bullet_animation, this, false);
        anim_rl = (RelativeLayout) view.findViewById(R.id.animation_person_rl);
        anim_gift = (ImageView) view.findViewById(R.id.animation_gift);
        anim_light = (ImageView) view.findViewById(R.id.animation_light);
        anim_num = (StrokeTextView) view.findViewById(R.id.animation_num);
        anim_header = (ImageView) view.findViewById(R.id.gift_userheader_iv);
        anim_nickname = (TextView) view.findViewById(R.id.gift_usernickname_tv);
        anim_sign = (TextView) view.findViewById(R.id.gift_usersign_tv);
        this.addView(view);
    }

    public void hideView() {
        anim_gift.setVisibility(INVISIBLE);
        anim_light.setVisibility(INVISIBLE);
        anim_num.setVisibility(INVISIBLE);
    }

    public String getNick() {
        return nick;
    }

    private GiftSendModel model;

    public GiftSendModel getModel() {
        return model;
    }

    public boolean equalsCurrentModel(GiftSendModel model) {
        return this.model.equals(model);
    }


    public void setModel(GiftSendModel model) {
        this.model = model;
        if (0 != model.getGiftCount()) {
            this.repeatCount = model.getGiftCount();
            setRepeatCount(model.getGiftCount());
        }
        if (!TextUtils.isEmpty(model.getNickname())) {
            anim_nickname.setText(model.getNickname());
        }
        if (!TextUtils.isEmpty(model.getSig())) {
            anim_sign.setText(mContext.getString(R.string.gift_sent) + "【" + model.getSig() + "】");
        }
        Glide.with(mContext)
                .load(model.getGiftURL())
                .centerCrop()
                .into(anim_gift);

        Utils.setImageRounded(anim_header, model.getUserImage(), mContext);

//        anim_gift.setImageResource(model.getGiftRes());
        this.nick = anim_nickname.getText().toString();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    public void setRepeatCount(int repeatCount) {
        this.repeatCount = repeatCount;
        if (scaleGiftNum != null) {
            scaleGiftNum.setRepeatCount(repeatCount - 1);
        }
    }

    public AnimatorSet startAnimation(final int repeatCount) {
        hideView();
        ObjectAnimator flyFromLtoR = GiftAnimationUtil.createFlyFromLtoR(anim_rl, -getWidth(), 0, 400, new OvershootInterpolator());
        flyFromLtoR.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                GiftFrameLayout.this.setVisibility(View.VISIBLE);
                GiftFrameLayout.this.setAlpha(1f);
                isShowing = true;
                anim_num.setText("x " + 1);
                Log.i("TAG", "flyFromLtoR A start");
            }
        });
        ObjectAnimator flyFromLtoR2 = GiftAnimationUtil.createFlyFromLtoR(anim_gift, -getWidth(), 0, 400, new DecelerateInterpolator());
        flyFromLtoR2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                anim_gift.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                GiftAnimationUtil.startAnimationDrawable(anim_light);
                anim_num.setVisibility(View.VISIBLE);
            }
        });
        scaleGiftNum = GiftAnimationUtil.scaleGiftNum(anim_num, repeatCount);
        scaleGiftNum.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                anim_num.setText("x " + (++starNum));
            }
        });
        ObjectAnimator fadeAnimator = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 0, -100, 300, 400);
        fadeAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                GiftFrameLayout.this.setVisibility(View.INVISIBLE);
            }
        });
        ObjectAnimator fadeAnimator2 = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 100, 0, 20, 0);

        AnimatorSet animatorSet = GiftAnimationUtil.startAnimation(flyFromLtoR, flyFromLtoR2, scaleGiftNum, fadeAnimator, fadeAnimator2);
        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                starNum = 1;
                isShowing = false;
            }

        });
        return animatorSet;
    }


}
