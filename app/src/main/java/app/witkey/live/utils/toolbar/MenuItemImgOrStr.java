package app.witkey.live.utils.toolbar;

import android.view.View.OnClickListener;

public class MenuItemImgOrStr extends MenuItem {

    private int resourceId;
    private OnClickListener onClickListener;
    private String title;
    public MenuItemImgOrStr(int _resourceId, OnClickListener _onClickListener) {
        resourceId = _resourceId;
        title = null;
        onClickListener = _onClickListener;
    }
    public MenuItemImgOrStr(String str, OnClickListener _onClickListener) {
        resourceId = -1;
        title = str;
        onClickListener = _onClickListener;
    }

    public String getTitle() {
        return title;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
