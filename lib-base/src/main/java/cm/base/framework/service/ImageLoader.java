package cm.base.framework.service;

import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 * 图片加载
 */
public class ImageLoader {

    public static void loadImage(ImageView imageView,String url){
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

}
