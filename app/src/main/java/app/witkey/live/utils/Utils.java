package app.witkey.live.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import app.witkey.live.R;

import app.witkey.live.items.UserLevelBO;
import app.witkey.live.utils.customviews.BlurImageView;
import app.witkey.live.utils.customviews.CircleTransform;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * created by developer on 9/21/2017.
 */

public class Utils {

    // METHOD TO SHOW TOAST MESSAGES
    public static void showToast(Context c, String message) {

        Toast.makeText(c, "" + message, Toast.LENGTH_LONG).show();
    }

    // METHOD TO GET CURRENT TIME STAMP
    public static String getCurrentTimeStamp() {

        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        return s.format(new Date());
    }

    // METHOD TO GET CURRENT TIME
    public static String getCurrentTime() {

        SimpleDateFormat s = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return s.format(new Date());
    }

    // METHOD TO GET CURRENT TIME
    public static String getCurrentDate() {

        SimpleDateFormat s = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return s.format(new Date());
    }

    // METHOD TO GET CURRENT TIME STAMP
    public static String getCurrentTimeStamp_Streams() {

        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(new Date());
    }

    // METHOD TO GET DEVICE SCREEN HEIGHT
    public static int getScreenHeight(Context c) {
        int screenHeight = 0;
        try {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        } catch (Exception e) {
            e.printStackTrace();
            return screenHeight;
        }
        return screenHeight;
    }

    // METHOD TO GET DEVICE SCREEN WIDTH
    public static int getScreenWidth(Context c) {
        int screenWidth = 0;
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        return screenWidth;
    }

    public static void setImageRounded(final ImageView img, final String url, final Context context) {
        if (url != null && url.length() > 0) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.user_place_holder_round)
                    .centerCrop()
                    .error(R.drawable.user_place_holder_round)
                    .into(new BitmapImageViewTarget(img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            Glide.with(context)
                    .load(R.drawable.user_place_holder_round)
                    .asBitmap()
                    .placeholder(R.drawable.user_place_holder_round)
                    .transform(new CircleTransform(context))
                    .error(R.drawable.user_place_holder_round)
                    .centerCrop()
                    .into(img);
        }

    }


    public static void setImageSimple(ImageView img, final String url, Context context) {
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.user_place_holder_square)
                    .into(img);
        } else {
            img.setImageResource(R.drawable.user_place_holder_square);
        }
    }

    public static void setImageSimpleWitkey(ImageView img, final String url, Context context) {
        if (url != null) {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.place_holder_videos)
                    .transform(new CenterCrop(context.getApplicationContext()), new GlideRoundTransform(context,15))
                    .into(img);
        } else {

            img.setImageResource(R.drawable.place_holder_videos);
        }
    }

    public static void setImageCornersRound(final ImageView img, final String url, final Context context) {

        if (url != null) {

            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.user_place_holder_square)
                    .centerCrop()
                    .error(R.drawable.user_place_holder_square)
                    .into(new BitmapImageViewTarget(img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(50f);
                            img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            img.setImageResource(R.drawable.user_place_holder_square);
        }
    }

    public static void setImageCornersRoundWitkey(final ImageView img, final String url, final Context context, final float cornerRadius) {

        if (url != null) {

            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.place_holder_videos)
                    .centerCrop()
                    .error(R.drawable.place_holder_videos)
                    .into(new BitmapImageViewTarget(img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCornerRadius(cornerRadius);
                            img.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            img.setImageResource(R.drawable.place_holder_videos);
        }
    }

    public static void setImageSimpleBlur(final ImageView img, final String url, final Context context) {

        if (url != null) {

            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .placeholder(R.drawable.user_place_holder_blur)
                    .error(R.drawable.user_place_holder_blur)
                    .into(new BitmapImageViewTarget(img) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            img.setImageBitmap(BlurImageView.blur(context, resource));
                        }
                    });
        } else {
            img.setImageResource(R.drawable.user_place_holder_blur);
        }
    }

    // METHOD TO CONVERT BITMAP CUSTOM CORNERS ROUND
    public static Bitmap getRoundedCornerOptionsBitmap(Bitmap bitmap, int pixels, boolean roundBL, boolean roundBR, boolean roundTL, boolean roundTR) {
        try {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float roundPx = pixels;

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// draw round
            // 4Corner

            if (roundBL) {
                Rect rectTL = new Rect(0, 0, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
                canvas.drawRect(rectTL, paint);
            }
            if (roundBR) {
                Rect rectTR = new Rect(bitmap.getWidth() / 2, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
                canvas.drawRect(rectTR, paint);
            }
            if (roundTR) {
                Rect rectBR = new Rect(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight());
                canvas.drawRect(rectBR, paint);
            }
            if (roundTL) {
                Rect rectBL = new Rect(0, bitmap.getHeight() / 2, bitmap.getWidth() / 2, bitmap.getHeight());
                canvas.drawRect(rectBL, paint);
            }

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

            return output;
        } catch (Exception e) {
            LogUtils.e("getRoundedCornerBitmap", "" + e.getMessage());
        }
        return bitmap;
    }

    public static int dp2px(Context context, int dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static String firstAlphabetCap(String text) {
        if (!text.isEmpty()) {
            return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
        }
        return text;
    }

    public static String getDifBtwn2Dates_old(Context context, String startDateString) {
        //1 minute = 60 seconds
        //1 hour = 60 x 60 = 3600
        //1 day = 3600 x 24 = 86400
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = simpleDateFormat.parse(startDateString);
            Date endDate = simpleDateFormat.parse(getCurrentTimeStamp_Streams());

            long different = endDate.getTime() - startDate.getTime();

            System.out.println("startDate : " + startDate);
            System.out.println("endDate : " + endDate);
            System.out.println("different : " + different);

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;

            /*System.out.printf(
                    "%d days, %d hours, %d minutes, %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/
            String time = "";

            if (elapsedDays > 0) {
                return ((int) elapsedDays < 2) ? elapsedDays + " " + context.getString(R.string.day_ago) : elapsedDays + " " + context.getString(R.string.days_ago);
            } else if (elapsedHours > 0) {
                return ((int) elapsedHours < 2) ? elapsedHours + " " + context.getString(R.string.hr_ago) : elapsedHours + " " + context.getString(R.string.hrs_ago);
            } else if (elapsedMinutes > 0) {
                return ((int) elapsedMinutes < 2) ? elapsedMinutes + " " + context.getString(R.string.min_ago) : elapsedMinutes + " " + context.getString(R.string.mins_ago);
            } else if (elapsedSeconds > 0) {
                return ((int) elapsedSeconds < 2) ? elapsedSeconds + " " + context.getString(R.string.sec_ago) : elapsedSeconds + " " + context.getString(R.string.secs_ago);
            }

            return time;
        } catch (Exception e) {
            LogUtils.e("getDifBtwn2Dates", "" + e);
            return "1 " + context.getString(R.string.hr_ago);
        }
    }


    public static String getDifBtwn2Dates(Context context, String startDateString) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC();
            DateTime dateTimeStart = DateTime.parse(startDateString, dateTimeFormatter);
            dateTimeStart = dateTimeStart.withZone(DateTimeZone.getDefault());

            DateTime currentDateTime = new DateTime();
            Days days = Days.daysBetween(dateTimeStart, currentDateTime);
            Hours hours = Hours.hoursBetween(dateTimeStart, currentDateTime);
            Minutes minutes = Minutes.minutesBetween(dateTimeStart, currentDateTime);
            Seconds seconds = Seconds.secondsBetween(dateTimeStart, currentDateTime);

            if (days.getDays() > 0) {
                return (days.getDays() < 2) ? days.getDays() + " " + context.getString(R.string.day_ago) : days.getDays() + " " + context.getString(R.string.days_ago);
            } else if (hours.getHours() > 0) {
                return (hours.getHours() < 2) ? hours.getHours() + " " + context.getString(R.string.hr_ago) : hours.getHours() + " " + context.getString(R.string.hrs_ago);
            } else if (minutes.getMinutes() > 0) {
                return (minutes.getMinutes() < 2) ? minutes.getMinutes() + " " + context.getString(R.string.min_ago) : minutes.getMinutes() + " " + context.getString(R.string.mins_ago);
            } else if (seconds.getSeconds() > 0) {
                return (seconds.getSeconds() < 2) ? seconds.getSeconds() + " " + context.getString(R.string.sec_ago) : seconds.getSeconds() + " " + context.getString(R.string.secs_ago);
            }
        } catch (Exception e) {
            LogUtils.e("getDifBtwn2Dates", "" + e);
        }
        return "1 " + context.getString(R.string.min_ago);
    }


    public static String howMuchTimePastFromNow(Context context,
                                                String startDateString, String elseFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = simpleDateFormat.parse(startDateString);

            long miliseconds = startDate.getTime();
            long milisecondsCurrent = Calendar.getInstance().getTimeInMillis();

            long diff = milisecondsCurrent - miliseconds;

            long seconds = diff / 1000;
            long mins = seconds / 60;
            long hours = mins / 60;
            long days = hours / 24;
            long months = hours / 30;
            long years = months / 12;

        /*Calendar tocheck = Calendar.getInstance();
        tocheck.setTimeInMillis(miliseconds);
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(milisecondsCurrent);*/

       /* if ((tocheck.get(Calendar.DATE)+1) == current.get(Calendar.DATE)) {
            return ("Yesterday at " + (convertDate(miliseconds,
                    Constants.dateFormat9).toUpperCase(Locale.getDefault())));
            // return (convertDate(miliseconds, elseFormat).toUpperCase(Locale
            // .getDefault()));
        }*/
            if (years > 0) {
                return (years + " Years Ago");
            } else if (months > 0) {
                return (months + " Months Ago");
            } else if (days > 0) {
                return (days + " Days Ago");
            } else if (hours > 0) {
                return (hours + " Hours Ago");
            } else if (mins > 0) {
                return (mins + " Minutes Ago");
            } else {
                return ("Just Now");
            }
        } catch (Exception e) {
            LogUtils.e("howMuchTimePastFromNow", "" + e);
            return ("1 Days Ago");
        }
    }

    public static String getShortString(String text, int size) {
        if (text != null && !text.isEmpty()) {
            if (text.length() > size) {
                return text.substring(0, size) + "...";
            } else {
                return text;
            }
        } else {
            return "";
        }
    }

    public static boolean hasInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static int getChatJoinedIndex() {

        switch (new Random().nextInt(8)) {
            case 0:
                return R.string.entered_text;
            case 1:
                return R.string.arrived_text;
            case 2:
                return R.string.justJoined_text;
            case 3:
                return R.string.isHere_text;
            case 4:
                return R.string.withUs_text;
            case 5:
                return R.string.joinTheParty_text;
            case 6:
                return R.string.finallyHere_text;
            case 7:
                return R.string.comeToChat_text;
            default:
                return R.string.entered_text_;
        }
    }

    public static File getFileFromAssets(Context context, String filename) {
        try {
            File file = new File(context.getCacheDir(), filename);
            final AssetFileDescriptor assetFileDescriptor = context.getResources().getAssets().openFd(filename);
            FileInputStream input = assetFileDescriptor.createInputStream();
            byte[] buf = new byte[(int) assetFileDescriptor.getDeclaredLength()];
            int bytesRead = input.read(buf);
            input.close();
            if (bytesRead != buf.length) {
                LogUtils.e("getFileFromAssets ", "Asset read failed");
            }
            FileOutputStream output = new FileOutputStream(file);
            output.write(buf, 0, bytesRead);
            output.close();
            return file;
        } catch (Exception ex) {
            LogUtils.e("getFileFromAssets", "" + ex.getMessage());
        }
        return null;
    }

    // METHOD TO GET TOP STATUS BAR HEIGHT
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // METHOD TO CALCULATE USER LEVEL
    public static UserLevelBO getUserLevel(int level) {

        if (level >= EnumUtils.UserLevelTypes.EARTH_FROM && level <= EnumUtils.UserLevelTypes.EARTH_TO) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_EARTH, R.drawable.user_level_1_9, EnumUtils.UserLevelTypes.EARTH_FROM, EnumUtils.UserLevelTypes.EARTH_TO);
        } else if (level >= EnumUtils.UserLevelTypes.FIRE_FROM && level <= EnumUtils.UserLevelTypes.FIRE_TO) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_FIRE, R.drawable.user_level_10_29, EnumUtils.UserLevelTypes.FIRE_FROM, EnumUtils.UserLevelTypes.FIRE_TO);
        } else if (level >= EnumUtils.UserLevelTypes.WATER_FROM && level <= EnumUtils.UserLevelTypes.WATER_TO) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_WATER, R.drawable.user_level_30_49, EnumUtils.UserLevelTypes.WATER_FROM, EnumUtils.UserLevelTypes.WATER_TO);
        } else if (level >= EnumUtils.UserLevelTypes.WOOD_FROM && level <= EnumUtils.UserLevelTypes.WOOD_TO) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_WOOD, R.drawable.user_level_50_99, EnumUtils.UserLevelTypes.WOOD_FROM, EnumUtils.UserLevelTypes.WOOD_TO);
        } else if (level >= EnumUtils.UserLevelTypes.GOLD_FROM && level <= EnumUtils.UserLevelTypes.GOLD_TO) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_GOLD, R.drawable.user_level_100_149, EnumUtils.UserLevelTypes.GOLD_FROM, EnumUtils.UserLevelTypes.GOLD_TO);
        } else if (level >= EnumUtils.UserLevelTypes.PRESTIGE_FROM && level <= EnumUtils.UserLevelTypes.PRESTIGE_TO) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_PRESTIGE, R.drawable.user_level_150_199, EnumUtils.UserLevelTypes.PRESTIGE_FROM, EnumUtils.UserLevelTypes.PRESTIGE_TO);
//        } else if (level >= EnumUtils.UserLevelTypes.VIP_FROM && level <= EnumUtils.UserLevelTypes.VIP_TO) {
        } else if (level >= EnumUtils.UserLevelTypes.VIP_FROM) {
            return new UserLevelBO(EnumUtils.UserLevelTypes.TYPE_VIP, R.drawable.user_level_200_499, EnumUtils.UserLevelTypes.VIP_FROM, EnumUtils.UserLevelTypes.VIP_TO);
        }
        return null;
    }

    // METHOD TO CALCULATE BROADCASTER LEVEL
    public static UserLevelBO getBroadcasterLevel(int level) {

        if (level >= EnumUtils.BroadcasterLevelTypes.PEAR_FROM && level <= EnumUtils.BroadcasterLevelTypes.PEAR_TO) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_PEAR, R.drawable.pearl, EnumUtils.BroadcasterLevelTypes.PEAR_FROM, EnumUtils.BroadcasterLevelTypes.PEAR_TO);
        } else if (level >= EnumUtils.BroadcasterLevelTypes.AMETHYST_FROM && level <= EnumUtils.BroadcasterLevelTypes.AMETHYST_TO) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_AMETHYST, R.drawable.amethyst, EnumUtils.BroadcasterLevelTypes.AMETHYST_FROM, EnumUtils.BroadcasterLevelTypes.AMETHYST_TO);
        } else if (level >= EnumUtils.BroadcasterLevelTypes.RUBY_FROM && level <= EnumUtils.BroadcasterLevelTypes.RUBY_TO) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_RUBY, R.drawable.ruby, EnumUtils.BroadcasterLevelTypes.RUBY_FROM, EnumUtils.BroadcasterLevelTypes.RUBY_TO);
        } else if (level >= EnumUtils.BroadcasterLevelTypes.SAPPHIRE_FROM && level <= EnumUtils.BroadcasterLevelTypes.SAPPHIRE_TO) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_SAPPHIRE, R.drawable.sapphire, EnumUtils.BroadcasterLevelTypes.SAPPHIRE_FROM, EnumUtils.BroadcasterLevelTypes.SAPPHIRE_TO);
        } else if (level >= EnumUtils.BroadcasterLevelTypes.EMERALD_FROM && level <= EnumUtils.BroadcasterLevelTypes.EMERALD_TO) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_EMERALD, R.drawable.emerald_stone, EnumUtils.BroadcasterLevelTypes.EMERALD_FROM, EnumUtils.BroadcasterLevelTypes.EMERALD_TO);
        } else if (level >= EnumUtils.BroadcasterLevelTypes.DIAMOND_FROM && level <= EnumUtils.BroadcasterLevelTypes.DIAMOND_TO) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_DIAMOND, R.drawable.diamond, EnumUtils.BroadcasterLevelTypes.DIAMOND_FROM, EnumUtils.BroadcasterLevelTypes.DIAMOND_TO);
//        } else if (level >= EnumUtils.BroadcasterLevelTypes.STAR_DIAMOND_FROM && level <= EnumUtils.BroadcasterLevelTypes.STAR_DIAMOND_TO) {
        } else if (level >= EnumUtils.BroadcasterLevelTypes.STAR_DIAMOND_FROM) {
            return new UserLevelBO(EnumUtils.BroadcasterLevelTypes.TYPE_STAR_DIAMOND, R.drawable.diamond, EnumUtils.BroadcasterLevelTypes.STAR_DIAMOND_FROM, EnumUtils.BroadcasterLevelTypes.STAR_DIAMOND_TO);
        }
        return null;
    }

    public static void openPlaystore(Context context) {
        final String appPackageName = "app.witkey.live";//context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
//   /XfTG2hCWX6JrANt5Y3n6yCvlbM=
    public static String getProjectHashKey(Context context) {
        String hashKey = "";
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "app.witkey.live",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                hashKey = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Utils.showToast(context, "KeyHash:" + hashKey);
                Log.d("KeyHash:", hashKey);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashKey;
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("profanity/Profanity.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (Exception ex) {
            LogUtils.e("loadJSONFromAsset", "" + ex.getMessage());
            return null;
        }
        return json;
    }

    //https://stackoverflow.com/questions/33254492/how-to-censored-bad-words-offensive-words-in-android-studio
    public static String checkForProfanity(Context context, String input) {
        String output = input;
        String profanityWords = loadJSONFromAsset(context);

        if (profanityWords != null && (!profanityWords.isEmpty())) {
            try {
                JSONArray jsonArray = new JSONArray(profanityWords);
                for (int index = 0; index < jsonArray.length(); index++) {
                    Pattern rx = Pattern.compile("\\b" + jsonArray.get(index).toString() + "\\b", Pattern.CASE_INSENSITIVE);
                    output = rx.matcher(output).replaceAll(new String(new char[jsonArray.get(index).toString().length()]).replace('\0', '*'));
                }

            } catch (Exception e) {
                LogUtils.e("checkForProfanity", "" + e.getMessage());
                return input;
            }
            return output;
        } else {
            return input;
        }
    }

    public static boolean createGiftsDir(String path) {
        try {
//            String dirPath = context.getFilesDir().getAbsolutePath() + File.separator + Constants.GIFTS;
//            File folder = new File(dirPath);
            File folder = new File(Environment.getExternalStorageDirectory(), path);
            if (!folder.exists()) {
                if (folder.mkdir()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            LogUtils.e("createGiftsDir", "" + e.getMessage());
            return false;
        }
    }

    private Uri.Builder builder;

    public Uri getUriFromUrl(String thisUrl) {
        try {
            URL url = new URL(thisUrl);
            builder = new Uri.Builder()
                    .scheme(url.getProtocol())
                    .authority(url.getAuthority())
                    .appendPath(url.getPath());
            return builder.build();
        } catch (Exception e) {
            LogUtils.e("getUriFromUrl", "" + e.getMessage());
        }
        return null;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getTimeFormatedFromMiliSec(String src) {
        try {
            long millisec = Long.parseLong(src);

            if (millisec == 0)
                return "00:00:00";

            long totalSecs = millisec / 1000;
            long hours = (totalSecs / 3600);
            long mins = (totalSecs / 60) % 60;
            long secs = totalSecs % 60;
            String minsString = (mins == 0) ? "00" : ((mins < 10) ? "0" + mins : "" + mins);
            String secsString = (secs == 0) ? "00" : ((secs < 10) ? "0" + secs : "" + secs);
            if (hours > 0)
                return hours + ":" + minsString + ":" + secsString;
            else if (mins > 0)
                return "00:" + minsString + ":" + secsString;
            else return "00:00:" + secsString;


//            return TimeUnit.MILLISECONDS.toHours(millisec) + ":" + TimeUnit.MILLISECONDS.toMinutes(millisec) + ":" + TimeUnit.MILLISECONDS.toSeconds(millisec);
//            return (new SimpleDateFormat("HH:mm:ss", Locale.getDefault())).format(new Time(millisec));
        } catch (Exception e) {
            e.printStackTrace();
            return "00:00:00";
        }
    }

    /*public static String getTimeFormatedFromMiliSec(String src) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long milliSeconds = Long.parseLong(src);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }*/

    public static String getStarDetail(String date) {
        try {
            if (!TextUtils.isEmpty(date) && !date.equals("N/A")) {
                int day = Integer.parseInt(date.split("-")[2]);
                int month = Integer.parseInt(date.split("-")[1]);

                if (month == 1) {
                    if (day >= 21) {
                        return "Aquarius";
                    } else {
                        return "Capricorn";
                    }
                } else if (month == 2) {
                    if (day >= 20) {
                        return "Pisces";
                    } else {
                        return "Aquarius";
                    }
                } else if (month == 3) {
                    if (day >= 21) {
                        return "Aries";
                    } else {
                        return "Pisces";
                    }
                } else if (month == 4) {
                    if (day >= 21) {
                        return "Taurus";
                    } else {
                        return "Aries";
                    }
                } else if (month == 5) {
                    if (day >= 22) {
                        return "Gemini";
                    } else {
                        return "Taurus";
                    }
                } else if (month == 6) {
                    if (day >= 22) {
                        return "Cancer";
                    } else {
                        return "Gemini";
                    }
                } else if (month == 7) {
                    if (day >= 23) {
                        return "Leo";
                    } else {
                        return "Cancer";
                    }
                } else if (month == 8) {
                    if (day >= 23) {
                        return "Virgo";
                    } else {
                        return "Leo";
                    }
                } else if (month == 9) {
                    if (day >= 24) {
                        return "Libra";
                    } else {
                        return "Virgo";
                    }
                } else if (month == 10) {
                    if (day >= 24) {
                        return "Scorpio";
                    } else {
                        return "Libra";
                    }
                } else if (month == 11) {
                    if (day >= 23) {
                        return "Sagittarius";
                    } else {
                        return "Scorpio";
                    }
                } else if (month == 12) {
                    if (day >= 22) {
                        return "Capricorn";
                    } else {
                        return "Sagittarius";
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.e("getStarDetail", "" + e.getMessage());
            return "N/A";
        }
        return "N/A";
    }

    public static String calculateUserDOB(String date) {
        try {
            if (TextUtils.isEmpty(date) || date.contains("0000")) {
                return "N/A";
            }

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date startDate = simpleDateFormat.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            LocalDate birthDate = new LocalDate(calendar.get(Calendar.YEAR), (calendar.get(Calendar.MONTH) + 1), calendar.get(Calendar.DATE));
            LocalDate now = new LocalDate();
            Years age = Years.yearsBetween(birthDate, now);
            return age.getYears() == 0 ? "N/A" : age.getYears() + (age.getYears() > 1 ? " Years" : " Year");
        } catch (Exception e) {
            LogUtils.e("calculateUserDOB", "" + e.getMessage());
            return "N/A";
        }
    }
}
