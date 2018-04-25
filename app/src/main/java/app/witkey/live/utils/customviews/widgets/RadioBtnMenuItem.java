package app.witkey.live.utils.customviews.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RadioButton;

import app.witkey.live.R;


public class RadioBtnMenuItem extends RadioButton {

    private String TAG = this.getClass().getSimpleName();

    public RadioBtnMenuItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        int id = getId();
//        switch (id) {
//            case R.id.rb_home:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_home_selector, 0, 0, 0);
//                break;
//            case R.id.rb_news:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_news_selector, 0, 0, 0);
//                break;
//            case R.id.rb_tour:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_tours_selector, 0, 0, 0);
//                break;
//            case R.id.rb_plan:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_plan_selector, 0, 0, 0);
//                break;
//            case R.id.rb_featured:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_featured_selector, 0, 0, 0);
//                break;
//            case R.id.rb_favorite:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_favorite_selector, 0, 0, 0);
//                break;
//            case R.id.rb_blog:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_blog_selector, 0, 0, 0);
//                break;
//            case R.id.rb_contact:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_contact_selector, 0, 0, 0);
//                break;
//            case R.id.rb_about_us:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_about_us_selector, 0, 0, 0);
//                break;
//            case R.id.rb_logout:
//                this.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_logout_selector, 0, 0, 0);
//                break;
//        }

    }

    @Override
    public void setChecked(boolean t) {
        if (t) {
            this.setBackgroundResource(R.color.grey);
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            this.setBackgroundResource(R.color.white);
            this.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }

        super.setChecked(t);
    }

}