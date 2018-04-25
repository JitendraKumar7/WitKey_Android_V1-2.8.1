package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.GiftPanelAdapter;
import app.witkey.live.adapters.dashboard.stream.GiftsSlideViewAdapter;
import app.witkey.live.items.GiftPanelBO;
import app.witkey.live.items.GiftPanelItemBO;
import app.witkey.live.items.UserBO;
import app.witkey.live.items.UserProgressDetailBO;
import app.witkey.live.utils.EnumUtils;
import app.witkey.live.utils.Keys;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.animations.BubbleViewAnimation;
import app.witkey.live.utils.customviews.CustomTextView;
import app.witkey.live.utils.eventbus.Events;
import app.witkey.live.utils.eventbus.GlobalBus;
import app.witkey.live.utils.sharedpreferences.ObjectSharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveBroadcastGiftPanelDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.giftPanelTopTextView)
    CustomTextView giftPanelTopTextView;

    @BindView(R.id.giftsRecyclerView)
    RecyclerView giftsRecyclerView;

    @BindView(R.id.giftCardParent)
    LinearLayout giftCardParent;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.chips)
    ImageView chips;
    @BindView(R.id.addGiftCount)
    ImageView addGiftCount;
    @BindView(R.id.giftCountTV)
    CustomTextView giftCountTV;
    @BindView(R.id.bubbleView)
    BubbleViewAnimation bubbleView;
    @BindView(R.id.indicators)
    LinearLayout indicators;
    @BindView(R.id.coinsParentTV)
    LinearLayout coinsParentTV;
    @BindView(R.id.giftsVP)
    ViewPager giftsVP;
    @BindView(R.id.rechargeTV)
    CustomTextView rechargeTV;
    @BindView(R.id.userEnergy)
    CustomTextView userEnergy;
    @BindView(R.id.addChips)
    ImageView addChips;
    GiftsSlideViewAdapter giftsSlideViewAdapter;
    ImageView[] imageViews;

    UserBO userBO;
    UserProgressDetailBO userProgressDetailBO;
    double userCoins = 0;
    int userLevel = 1;
    int userGiftLevel = 1;
    public static int SHOW_CHAT = 1, HIDE_CHAT = 0;

    private GiftPanelAdapter giftPanelAdapter_;
    List<GiftPanelItemBO> giftPanelItemBOList;
    private List<GiftPanelBO> giftPanelBOs;
    private List<GiftPanelItemBO> giftPanelBOs_;

    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {

            switch (newState) {
                case BottomSheetBehavior.STATE_DRAGGING:
                    break;
                case BottomSheetBehavior.STATE_COLLAPSED:
                    break;
                case BottomSheetBehavior.STATE_EXPANDED:
                    break;
                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;
                case BottomSheetBehavior.STATE_SETTLING:
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    public GoLiveBroadcastGiftPanelDialog() {

    }

    public static GoLiveBroadcastGiftPanelDialog newInstance(String jsonElements, int giftPanelUserLevel) {
        Bundle args = new Bundle();
        args.putString("GIFTSDETAIL", jsonElements);
        args.putInt("USERLEVEL", giftPanelUserLevel);
        GoLiveBroadcastGiftPanelDialog fragment = new GoLiveBroadcastGiftPanelDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_broadcast_gift_panel, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);
        try {
            if (getArguments() != null) {
                String jsonArray = getArguments().getString("GIFTSDETAIL");
                userGiftLevel = getArguments().getInt("USERLEVEL");
                if (jsonArray != null && !jsonArray.isEmpty()) {
                    giftPanelItemBOList = new Gson().fromJson(jsonArray.toString(),
                            new TypeToken<List<GiftPanelItemBO>>() {
                            }.getType());
                }
            }

            userProgressDetailBO = ObjectSharedPreference.getObject(UserProgressDetailBO.class, Keys.USER_PROGRESS_DETAIL);
            userBO = ObjectSharedPreference.getObject(UserBO.class, Keys.USER_OBJECT);
            userCoins = userBO.getChips();
            userLevel = userProgressDetailBO.getUser_enegry();
        } catch (Exception e) {
            LogUtils.e("GoLiveBroadcastGiftPanelDialog", "" + e.getMessage());
        }

        initViews();
        setUpLockedLevelData();
        addUserLockedLevels(giftPanelItemBOList);
//        setUpData();
//        setUpGiftPanelRecycler(giftPanelBOs);
//        setUpGiftPanelRecycler_(giftPanelItemBOList);


        View parent = (View) bottomSheetView.getParent();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ((View) bottomSheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }

        parent.setFitsSystemWindows(true);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        bottomSheetBehavior.setPeekHeight(Utils.getScreenHeight(getActivity()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getDialog().getWindow();
            if (window != null)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("SHOW_VIEWER_CHAT_VIEW"));
                    dismiss();
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {
            }
        });

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("SHOW_VIEWER_CHAT_VIEW"));
                dismiss();
                return true;
            }
        });
    }

    private void initViews() {
        btnSubmit.setOnClickListener(this);
        rechargeTV.setOnClickListener(this);
        addGiftCount.setOnClickListener(this);
        coinsParentTV.setOnClickListener(this);
        addChips.setOnClickListener(this);
        chips.setOnClickListener(this);
//        rechargeTV.setText(getActivity().getString(R.string.recharge) + userCoins);
//        userEnergy.setText(getActivity().getString(R.string.energy_) + " " + userLevel);
        rechargeTV.setText("" + (int) userCoins);
        userEnergy.setText("" + " " + userLevel);
        SpannableString spannableString = SpannableString.valueOf(getString(R.string.text_broadcaster_when_sending_gift_and_your_name_will_glow_yellow));
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.witkey_yellow)), spannableString.length() - 6, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        giftPanelTopTextView.setText(spannableString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                try {
                    GiftPanelItemBO giftPanelBO = giftsSlideViewAdapter.getGiftId();
                    if (giftPanelBO != null) {

                        if (giftPanelBO.getIs_active() == EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK) {
                            double giftPrice = giftPanelBO.getPrice();
                            int giftCount = 1;
                            try {
                                giftCount = Integer.parseInt(giftCountTV.getText().toString());
                                if (giftCount > 1) {
                                    giftPrice = giftPrice * giftCount;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if (userCoins >= giftPrice) {

//                                userCoins = userCoins - giftPanelBO.getPrice();
//                                userBO.setChips(userCoins);
//                                ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
//                                rechargeTV.setText(getActivity().getString(R.string.recharge) + userCoins);
                                double price = 0;
                                price = giftPanelBO.getPrice();
                                new CallEventBusAsyncTask().execute(giftPanelBO.getId(), giftPanelBO.getName(), (int) price, giftCount, giftPanelBO.getIcon_url(), giftPanelBO.getType());
                                this.dismiss();
                            } else {
                                Utils.showToast(getActivity(), getString(R.string.not_enough_coins));
                            }
                        }

                    } else {
                        // in case of no gift selected
                        //Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                    }

                /*if (goLiveGiftPanelAdapter != null && goLiveGiftPanelAdapter.getItems().size() > 0 && goLiveGiftPanelAdapter.getSelectedIndex() != -1) {
                    GiftPanelBO giftPanelBO = goLiveGiftPanelAdapter.getItems().get(goLiveGiftPanelAdapter.getSelectedIndex());
                    if (giftPanelBO.getStatus() == EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK) {
                        if (userCoins >= giftPanelBO.getPrice()) {

                            userCoins = userCoins - giftPanelBO.getPrice();
                            userBO.setChips(userCoins);
                            ObjectSharedPreference.saveObject(userBO, Keys.USER_OBJECT);
                            rechargeTV.setText(getActivity().getString(R.string.recharge) + userCoins);

                            bubbleView.setBubbleDrawable(getResources().getDrawable(giftPanelBO.getLocalImage()), Utils.dp2px(getContext(), 40), Utils.dp2px(getContext(), 40));
                            View child = giftsRecyclerView.getLayoutManager().findViewByPosition(goLiveGiftPanelAdapter.getSelectedIndex());
                            goLiveGiftPanelAdapter.addBubbleView(child, giftPanelBO);

                            this.dismiss();
                        } else {
                            Utils.showToast(getActivity(), getString(R.string.not_enough_coins));
                        }
                    }
                } else {
                    // in case of no gift selected
                    //Utils.showToast(getActivity(), getString(R.string.will_be_implemented_later));
                }*/
                } catch (Exception e) {
                    LogUtils.e("GoLiveBroadcastGiftPanelDialog", "" + e.getMessage());
                }
                break;
            case R.id.chips:
            case R.id.addChips:
            case R.id.rechargeTV:
            case R.id.coinsParentTV:
                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("SHOW_PURCHASE_COINS"));
//                GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("GT_Pay"));
                this.dismiss();
                break;
            case R.id.addGiftCount:
                showGiftCount(getActivity(), v);
                break;
        }
    }

    /*private void setUpData() {
        giftPanelBOs = new ArrayList<>();

        giftPanelBOs.add(new GiftPanelBO(R.drawable.lollipop, 1000, "0", "Loll Pop", 150, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.icecream, 1001, "0", "Ice Cream", 150, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.popcorn, 1002, "0", "Pop Corn", 150, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.rose, 1003, "0", "Rose", 300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.lips, 1004, "0", "Hot Lips", 500, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.hearts, 1005, "0", "Heart to Heart", 500, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.hand, 1006, "0", "Hand Encore", 700, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.skateboard, 1007, "0", "Skate Board", 700, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.champagne, 1008, "0", "Champagne", 700, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.headphone, 1009, "0", "Headphone", 900, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.hotairballoon, 1010, "0", "Hot Air Balloon Rising", 900, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.guiter, 1011, "0", "Guitar", 900, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gold_coins, 1012, "0", "Gold Coins", 1400, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gold_ring, 1013, "0", "Gold Ring", 1400, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.stars, 1014, "0", "Gold Star", 1400, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.goldbar, 1015, "0", "Gold Bar", 1500, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.trophy, 1016, "0", "Trophy", 1500, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.treehouse, 1017, "0", "Tree House", 2000, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.piano, 1018, "0", "Piano The Grande", 2000, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.car, 1019, "0", "Car", 2500, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.goldaward, 1020, "0", "Gold Award My Idol", 2800, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.snowglobe, 1021, "0", "Snow Glob", 300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.lightsticks, 1022, "0", "Light Stick", 300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));

        giftPanelBOs.add(new GiftPanelBO(R.drawable.xmas_sock, 1023, "0", "Christmas Sock", 3000, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.xmas_tree, 1024, "0", "Christmas Tree", 3300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gingerbread_basket, 1025, "0", "Gingerbread Basket", 3300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.xmas_nut_cracker, 1026, "0", "Nut Cracker", 3300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.xmas_ornament, 1027, "0", "Ornament", 3300, "url", EnumUtils.GiftLevel.ENTRY_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_UNLOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));

        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 1, "0", "JUNIOR LEVEL", 0, "url", EnumUtils.GiftLevel.JUNIOR_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 2, "0", "MIDDLE LEVEL", 0, "url", EnumUtils.GiftLevel.MIDDLE_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 3, "0", "HIGH LEVEL", 0, "url", EnumUtils.GiftLevel.HIGH_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 4, "0", "PRO LEVEL", 0, "url", EnumUtils.GiftLevel.PRO_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 5, "0", "UPGRADED LEVEL", 0, "url", EnumUtils.GiftLevel.UPGRADED_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 6, "0", "VIP LEVEL", 0, "url", EnumUtils.GiftLevel.VIP_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
        giftPanelBOs.add(new GiftPanelBO(R.drawable.gift_new, 7, "0", "MISC LEVEL", 0, "url", EnumUtils.GiftLevel.MISC_LEVEL, EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.GIFT_UPDATE_STATUS_VERSION));
    }*/

    private void setUpLockedLevelData() {
        giftPanelBOs_ = new ArrayList<>();

        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "ENTRY LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.ENTRY_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "JUNIOR LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.JUNIOR_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "MIDDLE LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.MIDDLE_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "HIGH LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.HIGH_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "PRO LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.PRO_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "UPGRADED LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.UPGRADED_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "VIP LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.VIP_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
        giftPanelBOs_.add(new GiftPanelItemBO(1000, 0, "MISC LEVEL", EnumUtils.GiftLevel.GIFT_STATUS_LOCK, EnumUtils.GiftLevel.MISC_LEVEL, 0, "url", "http://witkeyapp.com/witkey_dev/public/images-gift/gift_new.png", 1));
    }

    /*private void setUpGiftPanelRecycler(List<GiftPanelItemBO> giftPanelBOList) {

        if (giftPanelBOList != null && giftPanelBOList.size() > 0) {

            final int pageCount = (int) Math.ceil(giftPanelBOList.size() / 10.0);
            imageViews = addIndicators(getActivity(), indicators, pageCount);

            giftsSlideViewAdapter = new GiftsSlideViewAdapter(getActivity(), giftPanelBOList, bubbleView, btnSubmit);
            //giftsVP.setOffscreenPageLimit(0);
            giftsVP.setAdapter(giftsSlideViewAdapter);

            giftsVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                            Utils.showToast(mContext, "onPageScrolled " + totalCount + "-" + (1 + position));
                }

                @Override
                public void onPageSelected(int position) {
                    moveIndicator(getActivity(), pageCount, imageViews, position);

                    if (position == 0) {
                        if (giftsSlideViewAdapter.getGiftPageList().get(position + 1) != null) {
                            giftsSlideViewAdapter.getGiftPageList().get(position + 1).refreshSelectedStates();
                        }
                    } else if (position == giftsSlideViewAdapter.getCount() - 1) {
                        if (giftsSlideViewAdapter.getGiftPageList().get(position - 1) != null) {
                            giftsSlideViewAdapter.getGiftPageList().get(position - 1).refreshSelectedStates();
                        }
                    } else if (position > 0 && position < giftsSlideViewAdapter.getCount() - 1 && (position + 1) < giftsSlideViewAdapter.getGiftPageList().size()) {
                        if (giftsSlideViewAdapter.getGiftPageList().get(position + 1) != null) {
                            giftsSlideViewAdapter.getGiftPageList().get(position + 1).refreshSelectedStates();
                        }
                        if (giftsSlideViewAdapter.getGiftPageList().get(position - 1) != null) {
                            giftsSlideViewAdapter.getGiftPageList().get(position - 1).refreshSelectedStates();
                        }
                    } else if (position > 0 && position < giftsSlideViewAdapter.getCount() - 1) {
                        if (giftsSlideViewAdapter.getGiftPageList().get(position - 1) != null) {
                            giftsSlideViewAdapter.getGiftPageList().get(position - 1).refreshSelectedStates();
                        }
                    }
//                            Utils.showToast(mContext, "onPageSelected " + totalCount + "-" + (1 + position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            *//*giftsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));

            giftsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            goLiveGiftPanelAdapter = new GoLiveGiftPanelAdapter(giftPanelBOList, getActivity(), giftsRecyclerView, bubbleView, btnSubmit);
            giftsRecyclerView.setAdapter(goLiveGiftPanelAdapter);*//*

        }
    }*/

    private void setUpGiftPanelRecycler_(List<GiftPanelItemBO> giftPanelItemBOList) {

        if (giftPanelItemBOList != null && giftPanelItemBOList.size() > 0) {

            final int pageCount = (int) Math.ceil(giftPanelItemBOList.size() / 10.0);
            imageViews = addIndicators(getActivity(), indicators, pageCount);

            giftsSlideViewAdapter = new GiftsSlideViewAdapter(getActivity(), giftPanelItemBOList, bubbleView, btnSubmit);
            //giftsVP.setOffscreenPageLimit(0);
            giftsVP.setAdapter(giftsSlideViewAdapter);

            giftsVP.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                            Utils.showToast(mContext, "onPageScrolled " + totalCount + "-" + (1 + position));
                }

                @Override
                public void onPageSelected(int position) {
                    try {
                        moveIndicator(getActivity(), pageCount, imageViews, position);

                        if (position == 0) {
                            if (giftsSlideViewAdapter.getGiftPageList().get(position + 1) != null) {
                                giftsSlideViewAdapter.getGiftPageList().get(position + 1).refreshSelectedStates();
                            }
                        } else if (position == giftsSlideViewAdapter.getCount() - 1) {
                            if (giftsSlideViewAdapter.getGiftPageList().get(position - 1) != null) {
                                giftsSlideViewAdapter.getGiftPageList().get(position - 1).refreshSelectedStates();
                            }
                        } else if (position > 0 && position < giftsSlideViewAdapter.getCount() - 1 && (position + 1) < giftsSlideViewAdapter.getGiftPageList().size()) {
                            if (giftsSlideViewAdapter.getGiftPageList().get(position + 1) != null) {
                                giftsSlideViewAdapter.getGiftPageList().get(position + 1).refreshSelectedStates();
                            }
                            if (giftsSlideViewAdapter.getGiftPageList().get(position - 1) != null) {
                                giftsSlideViewAdapter.getGiftPageList().get(position - 1).refreshSelectedStates();
                            }
                        } else if (position > 0 && position < giftsSlideViewAdapter.getCount() - 1) {
                            if (giftsSlideViewAdapter.getGiftPageList().get(position - 1) != null) {
                                giftsSlideViewAdapter.getGiftPageList().get(position - 1).refreshSelectedStates();
                            }
                        }
//                            Utils.showToast(mContext, "onPageSelected " + totalCount + "-" + (1 + position));
                    } catch (Exception e) {
                        LogUtils.e("onPageSelected", "" + e.getMessage());
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

//            giftsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2, LinearLayoutManager.HORIZONTAL, false));
//
//            giftsRecyclerView.setItemAnimator(new DefaultItemAnimator());
//            goLiveGiftPanelAdapter = new GoLiveGiftPanelAdapter(giftPanelBOList, getActivity(), giftsRecyclerView, bubbleView, btnSubmit);
//            giftsRecyclerView.setAdapter(goLiveGiftPanelAdapter);

        }
    }

    private static ImageView[] addIndicators(Context mContext, LinearLayout view, int count) {
        ImageView[] imageViews = new ImageView[count];
        view.removeAllViews();

        for (int i = 0; i < count; i++) {
            imageViews[i] = new ImageView(mContext);
            imageViews[i].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_un_selected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(18, 18);
            params.setMargins(5, 0, 5, 0);

            view.addView(imageViews[i], params);
        }

        imageViews[0].setImageDrawable(mContext.getResources().getDrawable(R.drawable.slider_dot_selected));
        return imageViews;
    }

    private static void moveIndicator(Context context, int totalCount, ImageView[] imageViews, int currentPosition) {
        try {
            for (int i = 0; i < totalCount; i++) {
                imageViews[i].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_un_selected));
            }
            imageViews[currentPosition].setImageDrawable(context.getResources().getDrawable(R.drawable.slider_dot_selected));
        } catch (Exception e) {
            LogUtils.e("moveIndicator", "" + e.getMessage());
        }
    }

    private void showGiftCount(final Context context, View view) {

        PopupMenu popup = new PopupMenu(context, view);

        popup.getMenuInflater().inflate(R.menu.gift_count, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                giftCountTV.setText(item.getTitle() + "");
                return true;
            }
        });
        popup.show();
    }

    private class CallEventBusAsyncTask extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object... params) {
            GlobalBus.getBus().post(new Events.FragmentToGoLiveUpActivityMessage("Gifts_ID", (String) params[1], (int) params[0], (int) params[2], (int) params[3], (String) params[4], (int) params[5]));
            return null;
        }
    }

    private void addUserLockedLevels(List<GiftPanelItemBO> giftPanelItemBOList) {

        if (giftPanelItemBOList != null && giftPanelItemBOList.size() > 0) {

            for (int index = userGiftLevel; index < giftPanelBOs_.size(); index++) {
                giftPanelItemBOList.add(giftPanelBOs_.get(index));
            }
            setUpGiftPanelRecycler_(giftPanelItemBOList);
        } else {
            setUpGiftPanelRecycler_(giftPanelBOs_);
        }
    }
}

