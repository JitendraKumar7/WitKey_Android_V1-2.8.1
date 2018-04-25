package app.witkey.live.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import app.witkey.live.Constants;
import app.witkey.live.R;

import static android.content.ContentValues.TAG;

/**
 * created by developer on 9/29/2017.
 */

public class ScreenShotUtils {

    //  METHOD TO GET SCREEEN SHOT
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    //     // METHOD TO CREATE DIRECTORY
    public static File getMainDirectoryName(Context context) {
        File mainDir = new File(Environment.getExternalStorageDirectory(), Constants.APP_NAME + File.separator + Constants.WITKEY_SCREEN_SHOT);
        if (!mainDir.exists()) {
            if (mainDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mainDir);
        }
        return mainDir;
    }

    // METHOD TO CREATE NEW FILE
    public static File getNewImageFile(Context context) {
        File mainDir = new File(Environment.getExternalStorageDirectory(), Constants.APP_NAME);
        if (!mainDir.exists()) {
            if (mainDir.mkdir())
                Log.e("Create Directory", "Main Directory Created : " + mainDir);
        }
        return new File(mainDir.getPath() + File.separator + Utils.getCurrentTimeStamp() + ".jpg");
    }

    // METHOD TO STORE TAKEN SCREEN SHOT
    public static File store(Bitmap bm, String fileName, File saveFilePath) {
        File dir = new File(saveFilePath.getAbsolutePath());
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(saveFilePath.getAbsolutePath(), fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //  METHOD TO SHARE SCREEN SHOT
    private static void shareScreenShot(File file, Context context) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.app_name)));
    }

    public static void takeScreenShot(View view, Context context) {
        Bitmap b = getScreenShot(view);
        if (b != null) {
            File saveFile = ScreenShotUtils.getMainDirectoryName(context);
            File file = ScreenShotUtils.store(b, "screenshot" + Utils.getCurrentTimeStamp() + ".png", saveFile);//save the screenshot to selected path
            Toast.makeText(context, "Image saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "bitmap is null", Toast.LENGTH_SHORT).show();
        }
    }

    // METHOD TO SAVE BITMAP
    public static void SaveScreenShot(Bitmap bitmap, Context context) {

        BufferedOutputStream bitmapStream = null;
        if (bitmap != null) {
            try {

                File jpegFile = ScreenShotUtils.getNewImageFile(context);
                if (jpegFile != null) {
                    bitmapStream = new BufferedOutputStream(new FileOutputStream(jpegFile));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bitmapStream);
//                    bitmap.recycle();
                    Utils.showToast(context, context.getString(R.string.screen_shot_saved));
                } else {
                    Utils.showToast(context, context.getString(R.string.screen_shot_dir_exception));
                }

            } catch (Exception e) {
                LogUtils.i(TAG, e.getMessage());
            } finally {
                if (bitmapStream != null) {
                    try {
                        bitmapStream.close();
                    } catch (IOException closeException) {
                        // ignore exception on close
                    }
                }
            }
        } else {
            Utils.showToast(context, context.getString(R.string.nothing_to_save));
        }
    }

    // METHOD TO SAVE BITMAP
    public static void saveMomentImage(Bitmap bitmap, Context context) {

        BufferedOutputStream bitmapStream = null;
        if (bitmap != null) {
            try {

                File jpegFile = ScreenShotUtils.getNewImageFile(context);
                if (jpegFile != null) {
                    bitmapStream = new BufferedOutputStream(new FileOutputStream(jpegFile));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bitmapStream);
//                    bitmap.recycle();
                    Utils.showToast(context, context.getString(R.string.image_saved));
                } else {
                    Utils.showToast(context, context.getString(R.string.image_dir_exception));
                }

            } catch (Exception e) {
                LogUtils.i(TAG, e.getMessage());
            } finally {
                if (bitmapStream != null) {
                    try {
                        bitmapStream.close();
                    } catch (IOException closeException) {
                        // ignore exception on close
                    }
                }
            }
        } else {
            Utils.showToast(context, context.getString(R.string.nothing_to_save));
        }
    }

    //  METHOD TO SHARE BITMAP SCREEN SHOT
    public static void shareBitmapScreenShot(Bitmap bitmap, Context context) {

        BufferedOutputStream bitmapStream = null;
        if (bitmap != null) {
            try {

                File jpegFile = ScreenShotUtils.getNewImageFile(context);
                if (jpegFile != null) {
                    bitmapStream = new BufferedOutputStream(new FileOutputStream(jpegFile));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bitmapStream);
//                    bitmap.recycle();
                    shareScreenShot(jpegFile, context);
                } else {
                    Utils.showToast(context, context.getString(R.string.screen_shot_dir_exception));
                }

            } catch (Exception e) {
                LogUtils.i(TAG, e.getMessage());
            } finally {
                if (bitmapStream != null) {
                    try {
                        bitmapStream.close();
                    } catch (IOException closeException) {
                        // ignore exception on close
                    }
                }
            }
        } else {
            Utils.showToast(context, context.getString(R.string.nothing_to_share));
        }
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        try {
            String filePath = "";
            String wholeID = DocumentsContract.getDocumentId(uri);

            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);

            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
            return filePath;
        } catch (Exception e) {
            LogUtils.e("getRealPathFromURI_API19", "" + e.getMessage());

        }
        return "";
    }

    public static String getRealPathFromContentURI(Context context, Uri uri) {
        String filePath = "";

        try {
            // Split at slash
            String[] splitted = uri.toString().split("/");
            //use last item, last item is ID in the array
            String id = splitted[splitted.length - 1];

            String[] column = {MediaStore.Images.Media.DATA};

            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{id}, null);

            int columnIndex;
            if (cursor != null) {
                columnIndex = cursor.getColumnIndex(column[0]);
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }


    @SuppressLint("NewApi")
    public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}