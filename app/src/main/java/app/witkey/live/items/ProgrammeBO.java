package app.witkey.live.items;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgrammeBO implements Parcelable {

    private int localImage;
    private String imageUrl;

    public ProgrammeBO(int localImage, String imageUrl) {
        this.localImage = localImage;
        this.imageUrl = imageUrl;
    }

    protected ProgrammeBO(Parcel in) {
        localImage = in.readInt();
        imageUrl = in.readString();
    }

    public static final Creator<ProgrammeBO> CREATOR = new Creator<ProgrammeBO>() {
        @Override
        public ProgrammeBO createFromParcel(Parcel in) {
            return new ProgrammeBO(in);
        }

        @Override
        public ProgrammeBO[] newArray(int size) {
            return new ProgrammeBO[size];
        }
    };

    public int getLocalImage() {
        return localImage;
    }

    public void setLocalImage(int localImage) {
        this.localImage = localImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(localImage);
        dest.writeString(imageUrl);
    }
}
