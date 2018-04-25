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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import app.witkey.live.R;
import app.witkey.live.interfaces.AsyncResponseCallBack;
import app.witkey.live.items.AppSettingsBO;
import app.witkey.live.items.StreamBO;
import app.witkey.live.items.TaskItem;
import app.witkey.live.items.UserBO;
import app.witkey.live.tasks.WebServicesVolleyTask;
import app.witkey.live.utils.AlertOP;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.WebServiceUtils;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;
import app.witkey.live.utils.sharedpreferences.UserSharedPreference;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RewardPopup extends Dialog {


    @BindView(R.id.tv_promo)
    TextView tvPromo;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.btnclaim)
    Button btnclaim;
    UserBO userBO;
    boolean claimStatus = false;

    public RewardPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_reward);
        ButterKnife.bind(this);

        try {
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
        } catch (Exception e) {
            LogUtils.e("RewardPopup", "" + e.getMessage());
        }
        tvPromo.setText(Html.fromHtml(getContext().getString(R.string.promo_text_1)
                + " <font color='#ec7c31'> " + getContext().getString(R.string.chips_text)
                + " </font> " + getContext().getString(R.string.promo_text_2_a)));

        btnclaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!claimStatus) {
                    setPromotionAvailNetworkCall(getContext(), userBO.getId());
                } else {
                    dismiss();
                }
            }
        });
    }

    // METHOD TO SET PROMOTION AVAIL NETWORK CALL
    //add-promotions-transaction
    //user_id
    //POST

    private void setPromotionAvailNetworkCall(final Context context, String userID) {

        HashMap<String, Object> serviceParams = new HashMap<String, Object>();
        HashMap<String, Object> tokenServiceHeaderParams = new HashMap<String, Object>();

        tokenServiceHeaderParams.put(Keys.TOKEN, UserSharedPreference.readUserToken());
        serviceParams.put(Keys.USER_ID, userID);

        new WebServicesVolleyTask(context, true, "",
                EnumUtils.ServiceName.add_promotions_transaction,
                EnumUtils.RequestMethod.POST, serviceParams, tokenServiceHeaderParams, new AsyncResponseCallBack() {

            @Override
            public void onTaskComplete(TaskItem taskItem) {

                if (taskItem != null) {

                    if (taskItem.isError()) {
                       /*DO SOMETHING*/
                        Utils.showToast(getContext(), context.getString(R.string.promotion_over));
                        dismiss();
                    } else {
                        try {
                            if (taskItem.getResponse() != null) {
                                JSONObject json = new JSONObject(taskItem.getResponse());
                                String availed = json.getString("is_availed");
                                userBO.setPromotion_avail(1);
                                if (availed.equals("1")) {/*1 -> PROMOTION ACTIVE*/
                                    userBO.setChips(userBO.getChips() + 1000.0);
                                    claimStatus = true;
                                }
                                ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
                                if (claimStatus) {
                                    showNext();
                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (!claimStatus) {
                    Utils.showToast(getContext(), context.getString(R.string.already_collected));
                    dismiss();
                }
            }
        });
    }

    private void showNext() {
        if (claimStatus) {
            imageView.setImageResource(R.drawable.reward_bg_2);
            btnclaim.setText(getContext().getString(R.string.lets_start));
        }
    }
}
