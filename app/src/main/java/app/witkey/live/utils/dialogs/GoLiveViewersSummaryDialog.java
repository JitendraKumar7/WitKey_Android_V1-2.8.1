package app.witkey.live.utils.dialogs;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;

import app.witkey.live.R;
import app.witkey.live.adapters.dashboard.stream.GoLiveViewersSummaryAdapter;
import app.witkey.live.items.ConversationBO;
import app.witkey.live.utils.customviews.CustomTextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoLiveViewersSummaryDialog extends BottomSheetDialogFragment implements View.OnClickListener, GoLiveViewersSummaryAdapter.ClickListeners {

    @BindView(R.id.currentViewersRecyclerView)
    RecyclerView currentViewersRecyclerView;

    @BindView(R.id.currentViewersTextView)
    CustomTextView currentViewersTextView;

    @BindView(R.id.ll_no_result)
    LinearLayout ll_no_result;
    @BindView(R.id.noResultTextView)
    CustomTextView noResultTextView;
    @BindView(R.id.noResultTextView_)
    CustomTextView noResultTextView_;

    private GoLiveViewersSummaryAdapter goLiveViewersSummaryAdapter;
    //private List<ViewersSummaryBO> viewersSummaryBOs;

    private ArrayList<ConversationBO> onlineConversationBOs;

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

    public GoLiveViewersSummaryDialog() {

    }

    public static GoLiveViewersSummaryDialog newInstance(ArrayList<ConversationBO> onlineConversationBOs) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("CONVERSATIONBO", onlineConversationBOs);
        GoLiveViewersSummaryDialog fragment = new GoLiveViewersSummaryDialog();
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

        if (getArguments() != null) {
            onlineConversationBOs = getArguments().getParcelableArrayList("CONVERSATIONBO");
        }

        View bottomSheetView = View.inflate(getContext(), R.layout.dialog_go_live_viewers_summary, null);
        dialog.setContentView(bottomSheetView);
        ButterKnife.bind(this, bottomSheetView);

        initViews();
        //setUpData();
        setUpViewerRecycler();

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) bottomSheetView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        ((View) bottomSheetView.getParent()).setBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.transparent));
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getDialog().getWindow();
            if (window != null)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void initViews() {
        String currentViewerText = getString(R.string.current_viewers);
        String numbers = "0";
        if (onlineConversationBOs != null)
            numbers = onlineConversationBOs.size() + "";

        String currentViewerNumber = " " + numbers;

        SpannableString spannableString = new SpannableString(currentViewerText + currentViewerNumber);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), currentViewerText.length(), currentViewerText.length() + currentViewerNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.witkey_orange)), currentViewerText.length(), currentViewerText.length() + currentViewerNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f), currentViewerText.length(), currentViewerText.length() + currentViewerNumber.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        currentViewersTextView.setText(spannableString);
    }

    @Override
    public void onClick(View v) {

    }

    private void setUpData() {
       /* viewersSummaryBOs = new ArrayList<>();

        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Smith", R.drawable.user_level_1_9, "Earth 4", R.drawable.dollar, "7,345"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Jake", R.drawable.user_level_50_99, "WOOD 77", R.drawable.dollar, "44,988"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Connor", R.drawable.user_level_150_199, "PRESTIGE 186", R.drawable.dollar, "558,234"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Wyatt", R.drawable.user_level_1_9, "FIRE 10", R.drawable.dollar, "15,768"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Cody", R.drawable.user_level_gold_level, "GOLD 10", R.drawable.dollar, "122,083"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Scott", R.drawable.user_level_1_9, "Earth 4", R.drawable.dollar, "7,345"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Logan", R.drawable.user_level_50_99, "WOOD 77", R.drawable.dollar, "44,988"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Jacob", R.drawable.user_level_150_199, "PRESTIGE 186", R.drawable.dollar, "558,234"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Maxwell", R.drawable.user_level_1_9, "FIRE 10", R.drawable.dollar, "15,768"));
        viewersSummaryBOs.add(new ViewersSummaryBO("https://www.w3schools.com/w3css/img_avatar3.png", "Emma", R.drawable.user_level_gold_level, "GOLD 10", R.drawable.dollar, "122,083"));*/
    }

    private void setUpViewerRecycler() {

        if (onlineConversationBOs != null && onlineConversationBOs.size() > 0) {

            showNoResult(false);

            currentViewersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            currentViewersRecyclerView.setItemAnimator(new DefaultItemAnimator());
            goLiveViewersSummaryAdapter = new GoLiveViewersSummaryAdapter(onlineConversationBOs, getActivity(), currentViewersRecyclerView);

            goLiveViewersSummaryAdapter.setClickListener(this);
            currentViewersRecyclerView.setAdapter(goLiveViewersSummaryAdapter);
        } else {
            showNoResult(true);
        }
    }

    @Override
    public void onRowClick(int position) {
        if (onlineConversationBOs != null && onlineConversationBOs.size() > 0) {
            new GoLiveViewersProfilePageDialog().newInstance(onlineConversationBOs.get(position)).show(getActivity().getSupportFragmentManager(), "Option 9");
        }
//        new GoLiveViewersProfilePageDialog().show(getActivity().getSupportFragmentManager(), "Option 7");
    }

    private void showNoResult(boolean isShow) {
        if (isShow) {
            currentViewersRecyclerView.setVisibility(View.GONE);
            ll_no_result.setVisibility(View.VISIBLE);
            noResultTextView.setText(R.string.no_viewer);
            noResultTextView_.setText(R.string.no_viewer_here);
        } else {
            ll_no_result.setVisibility(View.GONE);
            currentViewersRecyclerView.setVisibility(View.VISIBLE);
        }
    }
}

