package app.witkey.live.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.arasthel.asyncjob.AsyncJob;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.items.GiftZipItemBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.stream.FaceunityActivity;
import app.witkey.live.tasks.VolleyFileDownloadTask;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;

import net.lingala.zip4j.core.ZipFile;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * created by developer on 12/11/2017.
 */

public class GiftsUpdateUtils {

    public static Context context;
    private static String newZipList = "";
    public static String GIFTS = ".Gifs";
    public static String APP_NAME = "WitKey";
    public static String WITKEY_GIFTS_FOLDER_PATH = APP_NAME + File.separator + GIFTS;
    public static AppSettingsBO appSettingsBO;
    private static ProgressDialog pDialog = null;

    public static void checkForGiftUpdated(Context context) {
        GiftsUpdateUtils.context = context;
        String oldZipList = UserSharedPreference.readUserCurrentZipGiftList();
        if (Utils.hasInternetConnection(context)) {

            File giftsFolder = new File(Environment.getExternalStorageDirectory(), WITKEY_GIFTS_FOLDER_PATH);
            if (!giftsFolder.exists()) {
                giftsFolder.mkdirs();
                UserSharedPreference.saveUserCurrentGiftVersion("0");
            }
            /*TODO HAVE TO USE THIS CHECK ONCE SAME LOGIC ON BOTH CONDITIONS*/
            if (isGiftsVersionUpdated()) {
                /*DOWNLOAD AND REPLACE FOLDERS*/
                if (!newZipList.isEmpty()) {
                    /*FILES OR FOLDERS NOT FOUND*/
                    if (oldZipList != null && !oldZipList.isEmpty()) {
                        checkForCompleteDownload(oldZipList, newZipList);
                    } else {
                         /*GET FROM SERVER*/
                        getGiftsListNetworkCall(context, true);
                    }
                } else {
                    getGiftsListNetworkCall(context, false);
                }
            } else {
                /*CHECK FOR FOLDER COMPLETE*/
                if (oldZipList != null && !oldZipList.isEmpty()) {
                    if (!newZipList.isEmpty()) {
                    /*FILES OR FOLDERS NOT FOUND*/
                        checkForCompleteDownload(oldZipList, newZipList);
                    } else {
                        getGiftsListNetworkCall(context, false);
                    }
                } else {
                    /*GET FROM SERVER*/
                    getGiftsListNetworkCall(context, true);
                }
            }
        }
    }

    /*METHOD TO CHECK IF PARENT GIFT VERSION IS UPDATED*/
    private static boolean isGiftsVersionUpdated() { /*TODO HAVE TO REMOVE THIS METHOD USING 2 TIMES*/
        try {
            appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
            String userGiftVersion = UserSharedPreference.readUserCurrentGiftVersion();/*TO SAVE*/
            if (appSettingsBO != null && !appSettingsBO.getGift_version().isEmpty() && userGiftVersion != null && !userGiftVersion.isEmpty()
                    && !appSettingsBO.getGift_version().equals(userGiftVersion)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void checkForCompleteDownload(String oldList, String newList) {
        if (oldList == null || oldList.isEmpty() || newList == null || newList.isEmpty())
            return;

        try {
            List<GiftZipItemBO> giftOldZipItemBOList = new Gson().fromJson(oldList,
                    new TypeToken<List<GiftZipItemBO>>() {
                    }.getType());

            List<GiftZipItemBO> giftNewZipItemBOList = new Gson().fromJson(newList,
                    new TypeToken<List<GiftZipItemBO>>() {
                    }.getType());


            if (giftOldZipItemBOList != null && giftOldZipItemBOList.size() > 0 && giftNewZipItemBOList != null && giftNewZipItemBOList.size() > 0) {
                newZipList = "";
                checkForFileExist(giftOldZipItemBOList, giftNewZipItemBOList, 0);
            } else {
                return;
            }
        } catch (Exception e) {
            LogUtils.e("checkForCompleteDownload", "" + e.getMessage());
        }
    }


    // METHOD TO GET ALL GIFTS DETAIL NETWORK CALL
//    http://18.220.157.19/witkey_dev/api/download-gifts
//    type get
    private static void getGiftsListNetworkCall(final Context context, final boolean firstDownload) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());

        new WebServicesVolleyTask(context, true, "Checking for updates",/*TESTING*/
                EnumUtils.ServiceName.download_gifts,
                EnumUtils.RequestMethod.GET, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                        /*DO SOME THING*/
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {

                                JSONObject jsonObject = new JSONObject(taskItem.getResponse());
                                if (firstDownload) {
                                    UserSharedPreference.saveUserCurrentZipGiftList(jsonObject.getJSONArray("gifts_detail").toString());
                                }
                                newZipList = jsonObject.getJSONArray("gifts_detail").toString();
                                checkForGiftUpdated(context);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    /*METHOD TO DOWNLOAD GIFT ZIP FILE*/
    private static void downloadGiftZipFiles(Context context, final String zipUrl, final String fileName, final int index, final List<GiftZipItemBO> giftOldZipItemBOList, final List<GiftZipItemBO> giftNewZipItemBOList) { //if (Utils.hasInternetConnection(this)) {}
        int levelCount = index;
        showProgress(context, " Level " + (levelCount + 1));/*TESTING*/
        new VolleyFileDownloadTask(context, Request.Method.GET, zipUrl,
                new Response.Listener<byte[]>() {
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            if (response != null) {

                                File file = new File(Environment.getExternalStorageDirectory() + File.separator + WITKEY_GIFTS_FOLDER_PATH + File.separator + fileName + ".zip");
                                FileOutputStream output = new FileOutputStream(file);
                                output.write(response);
                                output.close();

                                extractAndLoadFiles(file, index, giftOldZipItemBOList, giftNewZipItemBOList);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }, null);
    }

    private static void extractAndLoadFiles(final File downloadedZipFile, final int index, final List<GiftZipItemBO> giftOldZipItemBOList, final List<GiftZipItemBO> giftNewZipItemBOList) {
        new AsyncJob.AsyncJobBuilder<File[]>()
                .doInBackground(new AsyncJob.AsyncAction<File[]>() {
                    @Override
                    public File[] doAsync() {

                        if (downloadedZipFile != null) {
                            try {
                                ZipFile zipFile = new ZipFile(downloadedZipFile);
                                zipFile.extractAll(Environment.getExternalStorageDirectory() + File.separator + WITKEY_GIFTS_FOLDER_PATH);

                                downloadedZipFile.delete();

                                return new File[2];
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }
                })
                .doWhenFinished(new AsyncJob.AsyncResultAction<File[]>() {
                    int currentIndex = index;

                    @Override
                    public void onResult(final File[] result) {
                        if (result != null) {
                            hideProgress();/*TESTING*/
                            currentIndex++;
                            Utils.showToast(context, "Gift Level " + currentIndex + " Zip downloaded and unziped");
                            if (currentIndex < giftOldZipItemBOList.size()) {
                                checkForFileExist(giftOldZipItemBOList, giftNewZipItemBOList, currentIndex);
                            } else if (currentIndex == giftOldZipItemBOList.size()) {/*UPDATE CACHE HERE*/
                                UserSharedPreference.saveUserCurrentZipGiftList(new Gson().toJson(giftNewZipItemBOList));
                                if (appSettingsBO != null) {
                                    UserSharedPreference.saveUserCurrentGiftVersion(appSettingsBO.getGift_version());
                                }
                            }
                        }
                    }
                }).create().start();
    }


    private static void checkForFileExist(List<GiftZipItemBO> giftOldZipItemBOList, List<GiftZipItemBO> giftNewZipItemBOList, int index) {
        try { /*CHECK OBJECT ID HERE*/

            if (index >= giftOldZipItemBOList.size() || index >= giftNewZipItemBOList.size()) {
                if (index == giftOldZipItemBOList.size()) { /*UPDATE CACHE HERE*/
                    UserSharedPreference.saveUserCurrentZipGiftList(new Gson().toJson(giftNewZipItemBOList));
                    if (appSettingsBO != null) {
                        UserSharedPreference.saveUserCurrentGiftVersion(appSettingsBO.getGift_version());
                    }
                }
                return;
            }

            if (giftNewZipItemBOList.get(index).getCount() == 0) { /*IF COUNT 0 MOVE TO NEXT*/
                index++;
                if (index < giftOldZipItemBOList.size()) {
                    checkForFileExist(giftOldZipItemBOList, giftNewZipItemBOList, index);
                } else if (index == giftOldZipItemBOList.size()) {/*UPDATE CACHE HERE*/
                    UserSharedPreference.saveUserCurrentZipGiftList(new Gson().toJson(giftNewZipItemBOList));
                    if (appSettingsBO != null) {
                        UserSharedPreference.saveUserCurrentGiftVersion(appSettingsBO.getGift_version());
                    }
                }
            }

            File folder = new File(Environment.getExternalStorageDirectory(), WITKEY_GIFTS_FOLDER_PATH + File.separator + giftNewZipItemBOList.get(index).getGift_level());
            if (folder.exists()) { /*CHECK FOR FILE*/
//                if (folder.list() != null && folder.list().length == giftNewZipItemBOList.get(index).getCount()) { /* CHECK FOR GIFT COUNT*/

                if ((folder.list() != null && folder.list().length == giftNewZipItemBOList.get(index).getCount()) && (giftOldZipItemBOList.get(index).getVersion() == giftNewZipItemBOList.get(index).getVersion())) {
                    index++;
                    if (index < giftOldZipItemBOList.size()) {
                        checkForFileExist(giftOldZipItemBOList, giftNewZipItemBOList, index);
                    } else if (index == giftOldZipItemBOList.size()) {/*UPDATE CACHE HERE*/
                        UserSharedPreference.saveUserCurrentZipGiftList(new Gson().toJson(giftNewZipItemBOList));
                        if (appSettingsBO != null) {
                            UserSharedPreference.saveUserCurrentGiftVersion(appSettingsBO.getGift_version());
                        }
                    }
                } else {
                        /*DOWNLOAD*/
                    if (createRemoveGiftsDir(WITKEY_GIFTS_FOLDER_PATH + File.separator + giftNewZipItemBOList.get(index).getGift_level(), false)) {
                        downloadGiftZipFiles(context, giftNewZipItemBOList.get(index).getZip_url(), "temp", index, giftOldZipItemBOList, giftNewZipItemBOList);
                    }
                }
                /*} else {
                    *//*DOWNLOAD*//*
                    if (createRemoveGiftsDir(WITKEY_GIFTS_FOLDER_PATH + File.separator + giftNewZipItemBOList.get(index).getGift_level(), false)) {
                        downloadGiftZipFiles(context, giftNewZipItemBOList.get(index).getZip_url(), "temp", index, giftOldZipItemBOList, giftNewZipItemBOList);
                    }
                }*/
            } else {
                /*DOWNLOAD*/
                if (createRemoveGiftsDir(WITKEY_GIFTS_FOLDER_PATH + File.separator + giftNewZipItemBOList.get(index).getGift_level(), true)) {
                    downloadGiftZipFiles(context, giftNewZipItemBOList.get(index).getZip_url(), "temp", index, giftOldZipItemBOList, giftNewZipItemBOList);
                }
            }

        } catch (Exception e) {
            LogUtils.e("checkForCompleteDownload", "" + e.getMessage());
        }
    }

    private static boolean createRemoveGiftsDir(String path, boolean create) {
        try {
            File giftsFolder = new File(Environment.getExternalStorageDirectory(), WITKEY_GIFTS_FOLDER_PATH);
            if (!giftsFolder.exists()) {
                giftsFolder.mkdir();
            }

            File folder = new File(Environment.getExternalStorageDirectory(), path);
            if (create) {
                if (!folder.exists()) {
                    if (folder.mkdir()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                if (folder.exists()) {
                    if (folder.isDirectory()) {
                        File[] files = folder.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            files[i].delete();
                        }
                        return folder.delete();
                    } else {
                        return folder.delete();
                    }
                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtils.e("createGiftsDir", "" + e.getMessage());
            return false;
        }
    }

    private static void showProgress(Context context, String text) {
        try {
            if (pDialog == null) {
                pDialog = new ProgressDialog(context);
            }
            if (!pDialog.isShowing() && context != null) {
                pDialog.setMessage("Downloading " + text + " gifts please wait... ");
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);

                pDialog.show();
            }
        } catch (Exception e) {
            LogUtils.e("", "" + e.getMessage());
        }
    }

    // METHOD TO HIDE PROGRESS DIALOG

    private static void hideProgress() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void onUpdateDialog(Context context) {
        new AlertDialog.Builder(context).setCancelable(true)
                .setTitle(R.string.download_social_gifts)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                }).show();
    }


}
