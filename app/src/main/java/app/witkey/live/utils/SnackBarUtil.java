package app.witkey.live.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SnackBarUtil {

    /**
     * Snackbar with no action Button and Default Background (Default One)
     **/
    public static void showSnackbar(Context context, String message, boolean isLengthLong) {
        try {
            Snackbar snack = Snackbar.make(
                    (((Activity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_LONG);
            View view = snack.getView();
            TextView tv = (TextView) view
                    .findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Snackbar with no action Button and Custom Background
     **/
    public static void showSnackbar(Context context, String message, int bgColor,
                                    boolean isLengthLong) {
        try {
            Snackbar snack = Snackbar.make(
                    (((Activity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_LONG);
            View view = snack.getView();
            view.setBackgroundColor(bgColor);
            TextView tv = (TextView) view
                    .findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snack.show();
        } catch (Exception ex) {

        }

    }

    /**
     * Snackbar with action Button and Custom Background
     **/
    public static void showSnackbar(Context context, String message,
                                    int bgColor, String actionButton, boolean isLengthLong,
                                    OnClickListener actionButtonClickListener) {
        try {
            Snackbar snack = Snackbar.make(
                    (((Activity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);

            snack.setAction(actionButton, actionButtonClickListener);

            View view = snack.getView();
            view.setBackgroundColor(bgColor);
            TextView tv = (TextView) view
                    .findViewById(android.support.design.R.id.snackbar_text);

            TextView tvAction = (TextView) view
                    .findViewById(android.support.design.R.id.snackbar_action);
            tvAction.setTextSize(16);
            tvAction.setTextColor(Color.WHITE);

            tv.setTextColor(Color.WHITE);
            snack.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Snackbar with action Button and Default Background
     **/
    public static void showSnackbar(Context context, String message,
                                    String actionButton, boolean isLengthLong,
                                    OnClickListener actionButtonClickListener) {
        try{
            Snackbar snack = Snackbar.make(
                    (((Activity) context).findViewById(android.R.id.content)),
                    message + "", Snackbar.LENGTH_SHORT);
            if (isLengthLong)
                snack.setDuration(Snackbar.LENGTH_INDEFINITE);
            snack.setAction(actionButton, actionButtonClickListener);
            View view = snack.getView();
            TextView tv = (TextView) view
                    .findViewById(android.support.design.R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);

            TextView tvAction = (TextView) view
                    .findViewById(android.support.design.R.id.snackbar_action);
            tvAction.setTextSize(16);
            tvAction.setTextColor(Color.WHITE);

            snack.show();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}// main
