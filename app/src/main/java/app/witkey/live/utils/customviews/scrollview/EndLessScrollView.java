package app.witkey.live.utils.customviews.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * created by developer on 10/26/2017.
 */

public class EndLessScrollView extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    public EndLessScrollView(Context context) {
        super(context);
    }

    public EndLessScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public EndLessScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {

        View view = (View) getChildAt(getChildCount() - 1);
        int diff = (view.getBottom() - (getHeight() + getScrollY()));
        if (diff == 0) { // if diff is zero, then the bottom has been reached
            if (scrollViewListener != null) {
                scrollViewListener.onScrollEnded(this, x, y, oldx, oldy);
            }
        }
        super.onScrollChanged(x, y, oldx, oldy);
    }
}