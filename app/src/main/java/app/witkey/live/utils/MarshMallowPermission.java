package app.witkey.live.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import app.witkey.live.R;

public class MarshMallowPermission {
    private Activity activity;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForReadStorage() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForCamera() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionForExternalStorage(int requestCode, Fragment fragment) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertOP.showAlert(activity, "Alert", "External Storage permission needed. Please allow in WitKeyApplication Settings for additional functionality.");
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionForExternalStorage(final int requestCode, final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertOP.showAlert(activity, "Alert", activity.getString(R.string.storage_permission_desc),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    requestCode);
                        }
                    });


        } else {
            activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionForReadStorage(final int requestCode, final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            AlertOP.showAlert(activity, "Alert", activity.getString(R.string.storage_permission_desc),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           /* activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    requestCode);*/
                        }
                    });


        } else {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionForCamera(final int requestCode, final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            AlertOP.showAlert(activity, "Alert", activity.getString(R.string.camera_permission_confirmation),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                                    requestCode);
                        }
                    });


        } else {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    requestCode);
        }
    }

    //permission for Phone

    public boolean checkPermissionForPhone() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionForPhone(int requestCode, Fragment fragment) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CALL_PHONE)) {
            AlertOP.showAlert(activity, "Alert", "Phone call permission needed. Please allow in WitKeyApplication Settings for additional functionality.");
        } else {
            fragment.requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    requestCode);
        }
    }

    public boolean checkPermissionForMicrophone() {
        int result = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionForMicrophone(final int requestCode, final Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.RECORD_AUDIO)) {
            AlertOP.showAlert(activity, "Alert", activity.getString(R.string.record_audio_permission_not_granted),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                                    requestCode);
                        }
                    });
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO},
                    requestCode);
        }
    }
}