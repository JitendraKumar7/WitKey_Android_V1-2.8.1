package app.witkey.live.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import app.witkey.live.R;

public class AlertOP {


    public static void showAlert(Context context, String title, String message, final int buttonColor,
                                 int messageColor, String pBtnText, String nBtnText,
                                 OnClickListener onClickListener) {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            if (title != null)
                builder.setTitle(title);
            builder.setMessage(message);

            // Set dialog positive button and clickListener
            if (onClickListener != null) {
                builder.setPositiveButton(pBtnText, onClickListener);
            } else {
                builder.setPositiveButton(pBtnText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            // if also have negative button then set it
            if (nBtnText != null) {
                builder.setNegativeButton(nBtnText, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            // Set dialog style like button color, text color etc...
            final AlertDialog dialog = builder.create();
            if (buttonColor != -1) {

                int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertMessage", null, null);
                if (textViewId != 0) {
                    TextView tv = (TextView) dialog.findViewById(textViewId);
                    tv.setTextColor(messageColor);
                }

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(buttonColor);
                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(buttonColor);
                    }
                });
            }

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void showAlert(Context context, String title, String message) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            if (title != null)
                builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            final AlertDialog dialog = builder.create();
            if (dialog != null)
                dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public static void showAlert(Context context, String title, String message, OnClickListener onClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            if (title != null)
                builder.setTitle(title);
            builder.setCancelable(false);
            builder.setMessage(message);
            if (onClickListener != null) {
                builder.setPositiveButton(android.R.string.ok, onClickListener);
            } else {
                builder.setPositiveButton(android.R.string.ok, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            final AlertDialog dialog = builder.create();

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void showAlert(Context context, String positiveButtonText, String negativeButtonText, String title, String message, OnClickListener onClickListener, OnClickListener onClickListenerNegative) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogTheme);
            if (title != null)
                builder.setTitle(title);
            builder.setCancelable(false);
            builder.setMessage(message);
            if (onClickListener != null) {
                builder.setPositiveButton(positiveButtonText, onClickListener);
            }
            builder.setNegativeButton(negativeButtonText, onClickListenerNegative);

            final AlertDialog dialog = builder.create();

            dialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}//main
