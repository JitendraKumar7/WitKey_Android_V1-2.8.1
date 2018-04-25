package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.HashMap;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ForceUpdatePopup extends Dialog {


    @BindView(R.id.btncancel)
    Button btncancel;
    @BindView(R.id.btnupdate)
    Button btnupdate;

    boolean forceUpdateStatus = false;

    public ForceUpdatePopup(@NonNull Context context, boolean forceUpdateStatus_) {
        super(context);
        this.forceUpdateStatus = forceUpdateStatus;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_force_update);
        ButterKnife.bind(this);

        if (forceUpdateStatus) {
            btncancel.setVisibility(View.GONE);
        } else {
            btncancel.setVisibility(View.VISIBLE);
        }

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openPlaystore(getContext());
            }
        });

        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!forceUpdateStatus) {
                    dismiss();
                }
            }
        });


    }
}
