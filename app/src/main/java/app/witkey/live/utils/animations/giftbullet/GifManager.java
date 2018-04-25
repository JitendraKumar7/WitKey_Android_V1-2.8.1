package app.witkey.live.utils.animations.giftbullet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

import java.util.ArrayList;
import java.util.List;

/**
 * created by developer on 10/26/2017.
 */

public class GifManager {


    GiftQueue queue;
    List<GiftFrameLayout> viewList;

    public GifManager() {
        viewList = new ArrayList<>();
        queue = new GiftQueue();
    }

    public void addView(GiftFrameLayout giftFrameLayout) {
        viewList.add(giftFrameLayout);
    }

    public void addGift(GiftSendModel giftSendModel) {

        GiftFrameLayout showView = getShowCurrView(giftSendModel);
        if (showView != null) {
            int count = giftSendModel.getGiftCount() + showView.getRepeatCount();
            showView.setRepeatCount(count);
            return;
        }
        queue.add(giftSendModel);
        beingAnimotion();
    }

    public void beingAnimotion() {
        GiftFrameLayout hideView = getHideView();
        if (hideView != null) {
            beginAnimotion(hideView);
        }
    }

    private GiftFrameLayout getShowCurrView(GiftSendModel model) {
        for (GiftFrameLayout giftFrameLayout : viewList) {
            if (giftFrameLayout.isShowing() && giftFrameLayout.equalsCurrentModel(model)) {
                return giftFrameLayout;
            }
        }
        return null;
    }

    private GiftFrameLayout getHideView() {
        for (GiftFrameLayout giftFrameLayout : viewList) {
            if (!giftFrameLayout.isShowing()) {
                return giftFrameLayout;
            }
        }
        return null;
    }

    public void beginAnimotion(final GiftFrameLayout view) {

        GiftSendModel model = queue.removeTop();
        if (model == null) {
            return;
        }
        view.setModel(model);
        AnimatorSet animatorSet = view.startAnimation(model.getGiftCount());
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                synchronized (queue) {
                    if (!queue.isEmpty()) {
                        beginAnimotion(view);
                    }
                }
            }
        });
    }
}
