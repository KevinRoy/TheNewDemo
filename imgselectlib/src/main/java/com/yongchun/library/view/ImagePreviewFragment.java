package com.yongchun.library.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.yongchun.library.R;

import java.io.File;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by dee on 15/11/25.
 */
public class ImagePreviewFragment extends Fragment {

    public static final String PATH = "path";

    public static ImagePreviewFragment getInstance(String path) {
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PATH, path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_image_preview, container, false);
        final ImageView imageView = (ImageView) contentView.findViewById(R.id.preview_image);
        final PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
        final LinearLayout previewImageBg = (LinearLayout) contentView.findViewById(R.id.preview_image_bg);
        final ImagePreviewActivity activity = (ImagePreviewActivity) getActivity();
        if (!activity.isNetworkImage) { //加载本地
            Glide.with(container.getContext())
                    .load(new File(getArguments().getString(PATH)))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(480, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                            mAttacher.update();
                        }
                    });
        }
        if (activity.isNetworkImage) { //加载网络
            previewImageBg.setBackgroundColor(Color.rgb(0, 0, 0));
            Glide.with(container.getContext())
                    .load(getArguments().getString(PATH))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(480, 800) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView.setImageBitmap(resource);
                            mAttacher.update();
                        }
                    });
        }
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                activity.switchBarVisibility();
            }
        });

        return contentView;
    }
}
