package app.witkey.live.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import app.witkey.live.R;
import app.witkey.live.utils.customviews.CircleTransform;

import java.io.ByteArrayOutputStream;


/**
 * created by developer on 6/9/2017.
 */

public class ImageOP {

    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    /*helping method*/
    private void setCenterImage(final String imageUrl, final ImageView imv, final Context context) {
        try{
            if(imageUrl != null){

                Glide.with(context).load(imageUrl).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(new BitmapImageViewTarget(imv) {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                                super.onResourceReady(bitmap, anim);
                                Glide.with(context).load(imageUrl).centerCrop().into(imv);
                            }
                        });
            }else{
                imv.setImageResource(R.drawable.ic_launcher_round);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void setImage(ImageView img, final int path, Context context, int placeHolder, int errorHolder) {
        try{
            Glide.with(context)
                    .load(path)
                    .placeholder(placeHolder)
                    .error(errorHolder)
                    .into(img);
        }catch (OutOfMemoryError error){
            error.printStackTrace();
        }

    }

    public static void setImageRounded(ImageView img, final int path, Context context, int placeHolder) {
        try{
            Glide.with(context)
                    .load(path)
                    .placeholder(placeHolder)
                    .transform(new CircleTransform(context))
                    .into(img);
        }catch (OutOfMemoryError error){
            error.printStackTrace();
        }
    }

    public static void setImageRounded(ImageView img, final String url, Context context,
                                       int placeHolder, int errorHolder) {
        if (url != null) {
            try{
                Glide.with(context)
                        .load(url)
                        .placeholder(placeHolder)
                        .transform(new CircleTransform(context))
                        .error(errorHolder)
                        .into(img);
            }catch (OutOfMemoryError error){
                error.printStackTrace();
            }

        } else {
            img.setImageResource(placeHolder);
        }

    }
}
