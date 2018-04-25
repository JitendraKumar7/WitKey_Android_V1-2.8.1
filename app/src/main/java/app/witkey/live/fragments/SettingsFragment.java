package app.witkey.live.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.witkey.live.R;
import app.witkey.live.activities.ToolbarFragment;
import app.witkey.live.utils.toolbar.MenuItemImgOrStr;
import app.witkey.live.utils.toolbar.ToolbarOp;

public class SettingsFragment extends ToolbarFragment {
    private Context context;
    private String TAG = this.getClass().getSimpleName();
    private boolean isHidden;
    // TODO HAVE TO REMOVE THIS CLASS
    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings,
                container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize all views
        initViews(view);

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }



    @Override
    public void refreshToolbar() {
        Log.i("", "refreshToolbar: ");
        MenuItemImgOrStr menuItemImgOrStr = new MenuItemImgOrStr(R.drawable.ic_launcher_round, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        ToolbarOp.refresh(getView(), getActivity(), getString(R.string.title_settings),
                null, ToolbarOp.Theme.Dark, 0, null, menuItemImgOrStr);
    }



    private void initViews(View view) {
        //Initialize main content Linear layout.

    }

    private void stopSwipeLoader() {
    }


}