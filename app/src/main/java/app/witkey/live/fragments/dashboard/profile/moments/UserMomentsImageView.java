package app.witkey.live.fragments.dashboard.profile.moments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import app.witkey.live.R;
import app.witkey.live.fragments.abstracts.BaseFragment;
import app.witkey.live.utils.LogUtils;
import app.witkey.live.utils.MarshMallowPermission;
import app.witkey.live.utils.ScreenShotUtils;
import app.witkey.live.utils.Utils;
import app.witkey.live.utils.customviews.imagezoomview.ZoomageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Edited by developer on 10/03/2017.
 */

public class UserMomentsImageView extends BaseFragment implements View.OnClickListener {

    private Context context;
    String imageURL;
    Bitmap bitmap;

    public static final int REQUEST_STORAGE_PERMISSION = 201;

    @BindView(R.id.imageViewZoom)
    ZoomageView imageViewZoom;
    @BindView(R.id.cancelBTN)
    ImageView cancelBTN;
    @BindView(R.id.downloadBTN)
    ImageView downloadBTN;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private String TAG = this.getClass().getSimpleName();

    public static UserMomentsImageView newInstance(String imageURl) {
        Bundle args = new Bundle();
        args.putString("IMAGEURL", imageURl);
        UserMomentsImageView fragment = new UserMomentsImageView();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_moments_image_view, container, false);
        context = inflater.getContext();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        try {
            if (getArguments() != null) {
                imageURL = getArguments().getString("IMAGEURL");
            }
        } catch (Exception e) {
            LogUtils.e("UserMoments", "" + e.getMessage());
        }

        initView();
        initListeners();

    }

    private void initView() {

        bitmap = null;
        progressBar.setVisibility(View.VISIBLE);


        Glide.with(context)
                .load(imageURL)
                .asBitmap()
                .placeholder(R.drawable.place_holder_videos)
                .error(R.drawable.place_holder_videos)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        progressBar.setVisibility(View.GONE);
                        bitmap = resource;
                        imageViewZoom.setImageBitmap(bitmap);
                    }
                });
    }

    private void initListeners() {
        cancelBTN.setOnClickListener(this);
        downloadBTN.setOnClickListener(this);

        imageViewZoom.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                imageViewZoom.setImageResource(R.drawable.car);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelBTN:
                getActivity().onBackPressed();
                break;
            case R.id.downloadBTN:
                MarshMallowPermission permission = new MarshMallowPermission(getActivity());
                if (permission.checkPermissionForExternalStorage()) {
                    saveImage();
                } else {
                    permission.requestPermissionForExternalStorage(REQUEST_STORAGE_PERMISSION, this);
                }
                break;
        }
    }

    private void saveImage() {
        try {
            if (bitmap != null) {
                ScreenShotUtils.saveMomentImage(bitmap, getActivity());
            } else {
                Utils.showToast(getActivity(), getString(R.string.nothing_to_save));
            }
        } catch (Exception e) {
            LogUtils.e("saveImage", "" + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveImage();

                } else {
                    Utils.showToast(getActivity(), getString(R.string.storage_permission_not_granted));
                }
                break;
        }
    }
}