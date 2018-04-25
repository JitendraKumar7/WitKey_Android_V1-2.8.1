package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import app.witkey.live.R;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PromoPopup extends Dialog {


    @BindView(R.id.tv_promo)
    TextView tvPromo;

    public PromoPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_promo);
        ButterKnife.bind(this);

        tvPromo.setText(Html.fromHtml(getContext().getString(R.string.promo_text_1)
                + " <font color='#ec7c31'> " + getContext().getString(R.string.chips_text)
                + " </font> " + getContext().getString(R.string.promo_text_2_a)));

//        AppSettingsBO appSettingsBO = ObjectSharedPreference.getObject(AppSettingsBO.class, Keys.APP_SETTINGS_OBJECT);
        /*tvPromo.setText(Html.fromHtml(getContext().getString(R.string.promo_text_1)
                + " <font color='#ec7c31'> " + getContext().getString(R.string.chips_text)
                + " </font> " + getContext().getString(R.string.promo_text_2)
                + " <font color='#ec7c31'><b> " + appSettingsBO.getPromo_code()
                + " </b></font> " + getContext().getString(R.string.promo_text_3)));*/
    }


}
