package com.laorencel.uilibrary.databinding;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

public class ImageViewAdapter {
    @BindingAdapter(value = "android:src")
    public static void setSrc(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter(value = "android:src")
    public static void setSrc(ImageView imageView, int srcId) {
        imageView.setImageResource(srcId);
    }

    @BindingAdapter(value = "app:imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
//        Glide.with(imageView.getContext())
//                .load(url)
////                .placeholder(R.mipmap.ic_launcher)
//                .into(imageView);
    }
}
