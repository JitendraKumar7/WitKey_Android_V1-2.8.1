package app.witkey.live.activities.expansion;

/**
 * created by developer2 on 07-07-2017.
 */

/**
 * This is a little helper class that demonstrates simple testing of an
 * Expansion APK file delivered by Market.
 */

public class XAPKFile {
    public final boolean mIsMain;
    public final int mFileVersion;
    public final long mFileSize;

        XAPKFile(boolean isMain, int fileVersion, long fileSize) {
            mIsMain = isMain;
            mFileVersion = fileVersion;
            mFileSize = fileSize;
        }
}