package app.witkey.live.fragments.dashboard.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;

public class MachoFragment extends BaseFragment {
    // TODO TO BE REMOVE
    private Context context;
    private String TAG = this.getClass().getSimpleName();

    public static MachoFragment newInstance() {
        Bundle args = new Bundle();
        MachoFragment fragment = new MachoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_macho, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}