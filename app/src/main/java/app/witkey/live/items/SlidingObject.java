package app.witkey.live.items;

import java.io.Serializable;

/**
 * created by developer on 4/27/2017.
 */

public class SlidingObject implements Serializable {
    String mShortDescription;
    String mLongDescription;
    int mDummyImageResource;

    public SlidingObject(String mShortDescription, String mLongDescription, int mDummyImageResource) {
        this.mShortDescription = mShortDescription;
        this.mLongDescription = mLongDescription;
        this.mDummyImageResource = mDummyImageResource;
    }

    public int getmDummyImageResource() {
        return mDummyImageResource;
    }

    public void setmDummyImageResource(int mDummyImageResource) {
        this.mDummyImageResource = mDummyImageResource;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public void setmShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getmLongDescription() {
        return mLongDescription;
    }

    public void setmLongDescription(String mLongDescription) {
        this.mLongDescription = mLongDescription;
    }
}
